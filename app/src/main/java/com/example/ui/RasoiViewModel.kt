package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.RasoiDatabase
import com.example.data.model.CookCandidate
import com.example.data.model.Ingredient
import com.example.data.model.MealBase
import com.example.data.model.MealOrder
import com.example.data.model.OrderStatus
import com.example.data.model.PortionSize
import com.example.data.model.PreparationStyle
import com.example.data.model.PricingBreakdown
import com.example.data.model.SpiceLevel
import com.example.data.pricing.PricingEngine
import com.example.data.repository.RasoiRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

enum class AppRole {
  CUSTOMER,
  COOK,
  KITCHENS
}

data class MealBuilderState(
  val selectedBase: MealBase,
  val selectedIngredients: List<Ingredient>,
  val selectedPrep: PreparationStyle,
  val selectedSpice: SpiceLevel = SpiceLevel.MEDIUM,
  val selectedPortion: PortionSize = PortionSize.ONE_PERSON,
  val notes: String = "",
  val pricing: PricingBreakdown
)

data class CookConsoleState(
  val isOnline: Boolean = true,
  val todayEarnings: Int = 640,
  val completedOrders: Int = 8,
  val availableLunch: Boolean = true,
  val availableDinner: Boolean = true,
  val capacityMeals: Int = 10,
  val selectedCuisines: Set<String> = setOf("Assamese", "Bengali", "North Indian"),
  val serviceRadiusKm: Float = 3.0f,
  val activeCookProfile: CookCandidate
)

class RasoiViewModel(application: Application) : AndroidViewModel(application) {

  private val database = RasoiDatabase.getDatabase(application)
  val repository = RasoiRepository(database.orderDao())

  // Navigation / Role Mode
  private val _activeRole = MutableStateFlow(AppRole.CUSTOMER)
  val activeRole: StateFlow<AppRole> = _activeRole.asStateFlow()

  // Selected Area
  private val _selectedLocation = MutableStateFlow("Gachibowli, Hyderabad")
  val selectedLocation: StateFlow<String> = _selectedLocation.asStateFlow()

  val availableLocations = listOf(
    "Gachibowli, Hyderabad",
    "Madhapur, Hyderabad",
    "Hitec City, Hyderabad",
    "Indiranagar, Bengaluru",
    "Salt Lake, Kolkata",
    "Dispur, Guwahati",
    "Koramangala, Bengaluru"
  )

  // Builder State
  private val defaultBase = repository.sampleBases.first()
  private val defaultIngredients = listOf(
    repository.sampleIngredients.first { it.name.contains("Potato") },
    repository.sampleIngredients.first { it.name.contains("Tomato") },
    repository.sampleIngredients.first { it.name.contains("Egg") }
  )
  private val defaultPrep = repository.samplePreparations.first()

  private val _builderState = MutableStateFlow(
    MealBuilderState(
      selectedBase = defaultBase,
      selectedIngredients = defaultIngredients,
      selectedPrep = defaultPrep,
      selectedSpice = SpiceLevel.MEDIUM,
      selectedPortion = PortionSize.ONE_PERSON,
      notes = "",
      pricing = PricingEngine.calculatePrice(
        defaultBase,
        defaultIngredients,
        defaultPrep,
        PortionSize.ONE_PERSON
      )
    )
  )
  val builderState: StateFlow<MealBuilderState> = _builderState.asStateFlow()

  // Active Order from Room
  val activeOrder: StateFlow<MealOrder?> = repository.activeOrder.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = null
  )

  // Cook App Console State
  private val _cookState = MutableStateFlow(
    CookConsoleState(
      isOnline = true,
      todayEarnings = 640,
      completedOrders = 8,
      activeCookProfile = repository.verifiedCooks.first()
    )
  )
  val cookState: StateFlow<CookConsoleState> = _cookState.asStateFlow()

  // UI Toast / SnackBar events (e.g. for atomic locking results)
  private val _uiEvents = MutableSharedFlow<String>()
  val uiEvents: SharedFlow<String> = _uiEvents.asSharedFlow()

  // Selected Kitchen Storefront for Detail Sheet
  private val _selectedKitchenProfile = MutableStateFlow<CookCandidate?>(null)
  val selectedKitchenProfile: StateFlow<CookCandidate?> = _selectedKitchenProfile.asStateFlow()

  // Auto-simulation job for matching
  private var autoMatchingJob: Job? = null

  fun setRole(role: AppRole) {
    _activeRole.value = role
  }

  fun setLocation(loc: String) {
    _selectedLocation.value = loc
  }

  fun openKitchenProfile(cook: CookCandidate?) {
    _selectedKitchenProfile.value = cook
  }

  // Builder Actions
  fun selectBase(base: MealBase) {
    updateBuilder { it.copy(selectedBase = base) }
  }

  fun toggleIngredient(ingredient: Ingredient) {
    val current = _builderState.value.selectedIngredients
    val updated = if (current.any { it.id == ingredient.id }) {
      if (current.size > 1) current.filterNot { it.id == ingredient.id } else current
    } else {
      current + ingredient
    }
    updateBuilder { it.copy(selectedIngredients = updated) }
  }

  fun selectPreparation(prep: PreparationStyle) {
    updateBuilder { it.copy(selectedPrep = prep) }
  }

  fun selectSpice(spice: SpiceLevel) {
    updateBuilder { it.copy(selectedSpice = spice) }
  }

  fun selectPortion(portion: PortionSize) {
    updateBuilder { it.copy(selectedPortion = portion) }
  }

  fun setNotes(notes: String) {
    updateBuilder { it.copy(notes = notes) }
  }

  private fun updateBuilder(modifier: (MealBuilderState) -> MealBuilderState) {
    val updated = modifier(_builderState.value)
    val pricing = PricingEngine.calculatePrice(
      updated.selectedBase,
      updated.selectedIngredients,
      updated.selectedPrep,
      updated.selectedPortion
    )
    _builderState.value = updated.copy(pricing = pricing)
  }

  // Submit Order & Broadcast to nearby cooks
  fun placeAndBroadcastOrder(customerName: String = "Anis", area: String = "Gachibowli") {
    val current = _builderState.value
    val orderId = "ORD-" + UUID.randomUUID().toString().take(6).uppercase()

    val order = MealOrder(
      id = orderId,
      customerName = customerName,
      customerAddress = "$area, Hyderabad",
      base = current.selectedBase,
      ingredients = current.selectedIngredients,
      preparation = current.selectedPrep,
      spiceLevel = current.selectedSpice,
      portion = current.selectedPortion,
      notes = current.notes,
      pricing = current.pricing,
      status = OrderStatus.SEARCHING
    )

    viewModelScope.launch {
      repository.createAndBroadcastOrder(order)
      _uiEvents.emit("Order broadcasted to 7 nearby home kitchens!")

      // Start realistic simulation if cook doesn't accept manually
      startSimulatedCookResponse(orderId)
    }
  }

  private fun startSimulatedCookResponse(orderId: String) {
    autoMatchingJob?.cancel()
    autoMatchingJob = viewModelScope.launch {
      delay(4000) // 4 seconds delay for user to see finding radar or switch to Cook mode
      val currentOrder = activeOrder.value
      if (currentOrder != null && currentOrder.id == orderId && currentOrder.status == OrderStatus.SEARCHING) {
        val winningCook = repository.verifiedCooks.first()
        val won = repository.atomicallyClaimOrder(orderId, winningCook)
        if (won) {
          _uiEvents.emit("👩‍🍳 ${winningCook.name} accepted your order!")
        }
      }
    }
  }

  // Cook Console Actions
  fun toggleCookOnline() {
    _cookState.value = _cookState.value.copy(isOnline = !_cookState.value.isOnline)
  }

  fun setCookLunchAvailability(available: Boolean) {
    _cookState.value = _cookState.value.copy(availableLunch = available)
  }

  fun setCookDinnerAvailability(available: Boolean) {
    _cookState.value = _cookState.value.copy(availableDinner = available)
  }

  fun setCookCapacity(capacity: Int) {
    _cookState.value = _cookState.value.copy(capacityMeals = capacity)
  }

  fun setCookRadius(radius: Float) {
    _cookState.value = _cookState.value.copy(serviceRadiusKm = radius)
  }

  fun toggleCookCuisine(cuisine: String) {
    val current = _cookState.value.selectedCuisines
    val updated = if (current.contains(cuisine)) current - cuisine else current + cuisine
    _cookState.value = _cookState.value.copy(selectedCuisines = updated)
  }

  /**
   * Rapido-style Atomic Accept by Cook
   */
  fun cookAcceptOrder(orderId: String, claimingCook: CookCandidate = _cookState.value.activeCookProfile) {
    viewModelScope.launch {
      autoMatchingJob?.cancel()
      val won = repository.atomicallyClaimOrder(orderId, claimingCook)
      if (won) {
        _cookState.value = _cookState.value.copy(
          todayEarnings = _cookState.value.todayEarnings + (_builderState.value.pricing.cookEarnings),
          completedOrders = _cookState.value.completedOrders + 1
        )
        _uiEvents.emit("✅ Order accepted! You earned ₹${_builderState.value.pricing.cookEarnings}")
      } else {
        _uiEvents.emit("❌ This order has already been taken by another cook.")
      }
    }
  }

  // Cook advances stage
  fun advanceOrderStatus(orderId: String, currentStatus: OrderStatus) {
    viewModelScope.launch {
      val next = when (currentStatus) {
        OrderStatus.SEARCHING -> OrderStatus.ACCEPTED
        OrderStatus.ACCEPTED -> OrderStatus.PREPARING
        OrderStatus.PREPARING -> OrderStatus.COOKING
        OrderStatus.COOKING -> OrderStatus.PACKING
        OrderStatus.PACKING -> OrderStatus.DELIVERY
        OrderStatus.DELIVERY -> OrderStatus.DELIVERED
        else -> OrderStatus.DELIVERED
      }
      repository.updateOrderStatus(orderId, next)
      _uiEvents.emit("Status updated to ${next.title}")
    }
  }

  fun cancelActiveOrder(orderId: String) {
    viewModelScope.launch {
      autoMatchingJob?.cancel()
      repository.cancelOrder(orderId)
      _uiEvents.emit("Order cancelled")
    }
  }

  fun submitCustomerRating(orderId: String, rating: Int, review: String) {
    viewModelScope.launch {
      repository.submitRating(orderId, rating, review)
      _uiEvents.emit("Thank you for rating your home cook ⭐$rating!")
    }
  }
}
