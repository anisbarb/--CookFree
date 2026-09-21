package com.example.MainActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dining
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SoupKitchen
import androidx.compose.material.icons.outlined.Dining
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.SoupKitchen
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.OrderStatus
import com.example.ui.AppRole
import com.example.ui.RasoiViewModel
import com.example.ui.screens.CookAppScreen
import com.example.ui.screens.CookMatchingScreen
import com.example.ui.screens.CustomerHomeScreen
import com.example.ui.screens.KitchenDiscoveryScreen
import com.example.ui.screens.LiveTrackingScreen
import com.example.ui.screens.MealBuilderScreen
import com.example.ui.theme.FreshHerbGreen
import com.example.ui.theme.RasoiTheme
import com.example.ui.theme.TerracottaPrimary
import kotlinx.coroutines.flow.collectLatest

enum class CustomerScreenState {
  HOME,
  BUILDER,
  MATCHING,
  TRACKING
}

class MainActivity : ComponentActivity() {

  private val viewModel: RasoiViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      RasoiTheme {
        RasoiApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun RasoiApp(viewModel: RasoiViewModel) {
  val activeRole by viewModel.activeRole.collectAsStateWithLifecycle()
  val selectedLocation by viewModel.selectedLocation.collectAsStateWithLifecycle()
  val builderState by viewModel.builderState.collectAsStateWithLifecycle()
  val activeOrder by viewModel.activeOrder.collectAsStateWithLifecycle()
  val cookState by viewModel.cookState.collectAsStateWithLifecycle()
  val selectedKitchen by viewModel.selectedKitchenProfile.collectAsStateWithLifecycle()

  var customerScreen by remember { mutableStateOf(CustomerScreenState.HOME) }
  val snackbarHostState = remember { SnackbarHostState() }

  // Auto-switch to matching / tracking if active order exists and is in progress
  LaunchedEffect(activeOrder?.status) {
    val status = activeOrder?.status
    if (activeRole == AppRole.CUSTOMER) {
      if (status == OrderStatus.SEARCHING && customerScreen != CustomerScreenState.MATCHING) {
        customerScreen = CustomerScreenState.MATCHING
      }
    }
  }

  // Collect UI events for Toast/Snackbar
  LaunchedEffect(Unit) {
    viewModel.uiEvents.collectLatest { message ->
      snackbarHostState.showSnackbar(message)
    }
  }

  Scaffold(
    snackbarHost = { SnackbarHost(snackbarHostState) },
    bottomBar = {
      NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        modifier = Modifier.testTag("app_navigation_bar")
      ) {
        // 1. Customer Mode Tab
        NavigationBarItem(
          selected = activeRole == AppRole.CUSTOMER,
          onClick = { viewModel.setRole(AppRole.CUSTOMER) },
          icon = {
            if (activeOrder != null && activeOrder?.status != OrderStatus.CANCELLED) {
              BadgedBox(badge = { Badge { Text("1") } }) {
                Icon(
                  if (activeRole == AppRole.CUSTOMER) Icons.Filled.Home else Icons.Outlined.Home,
                  contentDescription = "Customer"
                )
              }
            } else {
              Icon(
                if (activeRole == AppRole.CUSTOMER) Icons.Filled.Home else Icons.Outlined.Home,
                contentDescription = "Customer"
              )
            }
          },
          label = { Text("Customer", fontWeight = if (activeRole == AppRole.CUSTOMER) FontWeight.Bold else FontWeight.Normal) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = TerracottaPrimary,
            selectedTextColor = TerracottaPrimary,
            indicatorColor = MaterialTheme.colorScheme.primaryContainer
          )
        )

        // 2. Cook Mode Tab (Rapido cook interface)
        NavigationBarItem(
          selected = activeRole == AppRole.COOK,
          onClick = { viewModel.setRole(AppRole.COOK) },
          icon = {
            if (activeOrder?.status == OrderStatus.SEARCHING && cookState.isOnline) {
              BadgedBox(badge = { Badge(containerColor = TerracottaPrimary) { Text("NEW") } }) {
                Icon(
                  if (activeRole == AppRole.COOK) Icons.Filled.SoupKitchen else Icons.Outlined.SoupKitchen,
                  contentDescription = "Cook App"
                )
              }
            } else {
              Icon(
                if (activeRole == AppRole.COOK) Icons.Filled.SoupKitchen else Icons.Outlined.SoupKitchen,
                contentDescription = "Cook App"
              )
            }
          },
          label = { Text("Cook App", fontWeight = if (activeRole == AppRole.COOK) FontWeight.Bold else FontWeight.Normal) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = TerracottaPrimary,
            selectedTextColor = TerracottaPrimary,
            indicatorColor = MaterialTheme.colorScheme.primaryContainer
          )
        )

        // 3. Kitchens Discovery Tab
        NavigationBarItem(
          selected = activeRole == AppRole.KITCHENS,
          onClick = { viewModel.setRole(AppRole.KITCHENS) },
          icon = {
            Icon(
              if (activeRole == AppRole.KITCHENS) Icons.Filled.Dining else Icons.Outlined.Dining,
              contentDescription = "Kitchens"
            )
          },
          label = { Text("Kitchens", fontWeight = if (activeRole == AppRole.KITCHENS) FontWeight.Bold else FontWeight.Normal) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = TerracottaPrimary,
            selectedTextColor = TerracottaPrimary,
            indicatorColor = MaterialTheme.colorScheme.primaryContainer
          )
        )
      }
    },
    modifier = Modifier.fillMaxSize()
  ) { innerPadding ->
    Box(modifier = Modifier.padding(innerPadding)) {
      AnimatedContent(
        targetState = activeRole,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "role_transition"
      ) { role ->
        when (role) {
          AppRole.CUSTOMER -> {
            when (customerScreen) {
              CustomerScreenState.HOME -> {
                CustomerHomeScreen(
                  selectedLocation = selectedLocation,
                  availableLocations = viewModel.availableLocations,
                  onLocationSelected = { viewModel.setLocation(it) },
                  onBuildMealClick = { customerScreen = CustomerScreenState.BUILDER },
                  onViewKitchenProfile = { cook ->
                    viewModel.openKitchenProfile(cook)
                    viewModel.setRole(AppRole.KITCHENS)
                  },
                  activeOrder = activeOrder,
                  onTrackOrderClick = {
                    customerScreen = if (activeOrder?.status == OrderStatus.SEARCHING) {
                      CustomerScreenState.MATCHING
                    } else {
                      CustomerScreenState.TRACKING
                    }
                  },
                  verifiedCooks = viewModel.repository.verifiedCooks,
                  cuisines = viewModel.repository.sampleCuisines
                )
              }

              CustomerScreenState.BUILDER -> {
                MealBuilderScreen(
                  builderState = builderState,
                  availableBases = viewModel.repository.sampleBases,
                  availableIngredients = viewModel.repository.sampleIngredients,
                  availablePreparations = viewModel.repository.samplePreparations,
                  onSelectBase = { viewModel.selectBase(it) },
                  onToggleIngredient = { viewModel.toggleIngredient(it) },
                  onSelectPreparation = { viewModel.selectPreparation(it) },
                  onSelectSpice = { viewModel.selectSpice(it) },
                  onSelectPortion = { viewModel.selectPortion(it) },
                  onNotesChange = { viewModel.setNotes(it) },
                  onFindCookClick = {
                    viewModel.placeAndBroadcastOrder(customerName = "Anis", area = selectedLocation.split(",").first())
                    customerScreen = CustomerScreenState.MATCHING
                  },
                  onBackClick = { customerScreen = CustomerScreenState.HOME }
                )
              }

              CustomerScreenState.MATCHING -> {
                if (activeOrder != null) {
                  CookMatchingScreen(
                    order = activeOrder!!,
                    candidateCooks = viewModel.repository.verifiedCooks,
                    onViewOrderClick = { customerScreen = CustomerScreenState.TRACKING },
                    onSimulateCookAccept = { cook ->
                      viewModel.cookAcceptOrder(activeOrder!!.id, cook)
                    },
                    onCancelOrderClick = {
                      viewModel.cancelActiveOrder(activeOrder!!.id)
                      customerScreen = CustomerScreenState.HOME
                    }
                  )
                } else {
                  CustomerHomeScreen(
                    selectedLocation = selectedLocation,
                    availableLocations = viewModel.availableLocations,
                    onLocationSelected = { viewModel.setLocation(it) },
                    onBuildMealClick = { customerScreen = CustomerScreenState.BUILDER },
                    onViewKitchenProfile = { cook ->
                      viewModel.openKitchenProfile(cook)
                      viewModel.setRole(AppRole.KITCHENS)
                    },
                    activeOrder = null,
                    onTrackOrderClick = {},
                    verifiedCooks = viewModel.repository.verifiedCooks,
                    cuisines = viewModel.repository.sampleCuisines
                  )
                }
              }

              CustomerScreenState.TRACKING -> {
                if (activeOrder != null) {
                  LiveTrackingScreen(
                    order = activeOrder!!,
                    onBackClick = { customerScreen = CustomerScreenState.HOME },
                    onAdvanceStage = {
                      viewModel.advanceOrderStatus(activeOrder!!.id, activeOrder!!.status)
                    },
                    onSubmitRating = { score, text ->
                      viewModel.submitCustomerRating(activeOrder!!.id, score, text)
                    },
                    onSwitchToCookApp = {
                      viewModel.setRole(AppRole.COOK)
                    }
                  )
                } else {
                  customerScreen = CustomerScreenState.HOME
                }
              }
            }
          }

          AppRole.COOK -> {
            CookAppScreen(
              cookState = cookState,
              activeOrder = activeOrder,
              onToggleOnline = { viewModel.toggleCookOnline() },
              onAcceptOrder = { orderId ->
                viewModel.cookAcceptOrder(orderId)
              },
              onAdvanceStage = { orderId, current ->
                viewModel.advanceOrderStatus(orderId, current)
              },
              onToggleLunch = { viewModel.setCookLunchAvailability(it) },
              onToggleDinner = { viewModel.setCookDinnerAvailability(it) },
              onChangeCapacity = { viewModel.setCookCapacity(it) },
              onChangeRadius = { viewModel.setCookRadius(it) },
              onToggleCuisine = { viewModel.toggleCookCuisine(it) }
            )
          }

          AppRole.KITCHENS -> {
            KitchenDiscoveryScreen(
              verifiedCooks = viewModel.repository.verifiedCooks,
              subscriptions = viewModel.repository.subscriptionPlans,
              selectedCook = selectedKitchen,
              onSelectCook = { viewModel.openKitchenProfile(it) },
              onOrderFromKitchen = { cook ->
                viewModel.setRole(AppRole.CUSTOMER)
                customerScreen = CustomerScreenState.BUILDER
              }
            )
          }
        }
      }
    }
  }
}
