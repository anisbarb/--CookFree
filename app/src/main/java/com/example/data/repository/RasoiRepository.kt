package com.example.data.repository

import com.example.data.local.OrderDao
import com.example.data.local.OrderEntity
import com.example.data.model.CookCandidate
import com.example.data.model.CuisineCategory
import com.example.data.model.Ingredient
import com.example.data.model.KitchenSubscription
import com.example.data.model.MealBase
import com.example.data.model.MealOrder
import com.example.data.model.OrderStatus
import com.example.data.model.PortionSize
import com.example.data.model.PreparationStyle
import com.example.data.model.PricingBreakdown
import com.example.data.model.SpiceLevel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RasoiRepository(private val orderDao: OrderDao) {

  val sampleBases = listOf(
    MealBase("b1", "Rice", "🍚", 20),
    MealBase("b2", "Roti (Phulka)", "🫓", 22),
    MealBase("b3", "Paratha", "🥞", 28),
    MealBase("b4", "Bread", "🍞", 18),
    MealBase("b5", "Noodles", "🍜", 25)
  )

  val sampleIngredients = listOf(
    Ingredient("i1", "Potato (Aloo)", "🥔", 8, true),
    Ingredient("i2", "Tomato", "🍅", 10, true),
    Ingredient("i3", "Onion", "🧅", 8, true),
    Ingredient("i4", "Egg", "🥚", 16, false),
    Ingredient("i5", "Chicken", "🍗", 45, false),
    Ingredient("i6", "Paneer", "🧀", 38, true),
    Ingredient("i7", "Fish (Rohu)", "🐟", 55, false),
    Ingredient("i8", "Vegetables", "🥦", 18, true)
  )

  val samplePreparations = listOf(
    PreparationStyle("p1", "Curry", 35, "Homestyle slow-simmered onion-tomato gravy"),
    PreparationStyle("p2", "Masala", 38, "Rich roasted whole spice blend"),
    PreparationStyle("p3", "Gravy", 32, "Smooth lightly spiced comfort broth"),
    PreparationStyle("p4", "Fried", 28, "Crispy pan-fried with mustard or cumin"),
    PreparationStyle("p5", "Dry (Sukha)", 30, "Tossed in roasted cumin and green chillies"),
    PreparationStyle("p6", "Boiled / Stew", 22, "Light, wholesome, oil-free preparation")
  )

  val sampleCuisines = listOf(
    CuisineCategory("Assamese", 129, "🌿", "Authentic Khar, Masor Tenga, pitika & duck delicacies"),
    CuisineCategory("Bengali", 119, "🐟", "Mustard fish, aloo posto, fragrant luchi & shukto"),
    CuisineCategory("Andhra", 139, "🌶️", "Fiery pappu, gongura pachadi & spicy curries"),
    CuisineCategory("Hyderabadi", 149, "🍲", "Rich baghara baingan, mirchi ka salan & korma"),
    CuisineCategory("North Indian", 109, "🫓", "Dal tadka, homestyle rajma chawal & paneer")
  )

  val verifiedCooks = listOf(
    CookCandidate(
      id = "cook_rina",
      name = "Rina Sharma",
      kitchenName = "Rina's Kitchen",
      rating = 4.9,
      distanceKm = 1.1,
      cuisines = listOf("Assamese", "Bengali", "Indian"),
      completedOrders = 1247,
      onTimeRate = 98,
      repeatRate = 96,
      estPrepMinutes = 35,
      specialties = listOf("Assamese Masor Tenga", "Bengali Egg Curry", "Aloo Pitika", "Chicken Kosha"),
      lunchSlotsAvailable = 12,
      dinnerSlotsAvailable = 7,
      isVerifiedKitchen = true,
      isGovtIdVerified = true
    ),
    CookCandidate(
      id = "cook_fatima",
      name = "Fatima Begum",
      kitchenName = "Fatima's Dastarkhwan",
      rating = 4.8,
      distanceKm = 1.4,
      cuisines = listOf("Hyderabadi", "Mughlai", "North Indian"),
      completedOrders = 980,
      onTimeRate = 97,
      repeatRate = 94,
      estPrepMinutes = 40,
      specialties = listOf("Hyderabadi Chicken Curry", "Baghara Rice", "Dum Aloo"),
      lunchSlotsAvailable = 8,
      dinnerSlotsAvailable = 11,
      isVerifiedKitchen = true,
      isGovtIdVerified = true
    ),
    CookCandidate(
      id = "cook_priya",
      name = "Priya Reddy",
      kitchenName = "Priya's Rayalaseema Rasoi",
      rating = 4.9,
      distanceKm = 1.7,
      cuisines = listOf("Andhra", "South Indian"),
      completedOrders = 1420,
      onTimeRate = 99,
      repeatRate = 97,
      estPrepMinutes = 30,
      specialties = listOf("Andhra Pappu", "Gongura Chicken", "Tomato Charu"),
      lunchSlotsAvailable = 15,
      dinnerSlotsAvailable = 9,
      isVerifiedKitchen = true,
      isGovtIdVerified = true
    ),
    CookCandidate(
      id = "cook_mina",
      name = "Mina Devi",
      kitchenName = "Mina's Ghar Ka Khana",
      rating = 4.7,
      distanceKm = 2.0,
      cuisines = listOf("Bihari", "North Indian", "Homestyle"),
      completedOrders = 830,
      onTimeRate = 95,
      repeatRate = 92,
      estPrepMinutes = 35,
      specialties = listOf("Sattu Paratha", "Aloo Chokha", "Yellow Dal Tadka"),
      lunchSlotsAvailable = 6,
      dinnerSlotsAvailable = 5,
      isVerifiedKitchen = true,
      isGovtIdVerified = true
    ),
    CookCandidate(
      id = "cook_lakshmi",
      name = "Lakshmi Narayanan",
      kitchenName = "Lakshmi's Traditional Kitchen",
      rating = 4.9,
      distanceKm = 2.4,
      cuisines = listOf("Tamil", "Kerala", "South Indian"),
      completedOrders = 1670,
      onTimeRate = 99,
      repeatRate = 98,
      estPrepMinutes = 30,
      specialties = listOf("Mor Kuzhambu", "Kerala Stew", "Avial"),
      lunchSlotsAvailable = 14,
      dinnerSlotsAvailable = 10,
      isVerifiedKitchen = true,
      isGovtIdVerified = true
    )
  )

  val subscriptionPlans = listOf(
    KitchenSubscription(
      id = "sub_10",
      planName = "10 Meals Pack",
      mealsCount = 10,
      pricePerMeal = 99,
      savingsPercent = 15,
      features = listOf("Flexible lunch/dinner choice", "Skip or pause anytime", "Priority cook allocation", "Valid for 30 days")
    ),
    KitchenSubscription(
      id = "sub_20",
      planName = "20 Meals Pack",
      mealsCount = 20,
      pricePerMeal = 89,
      savingsPercent = 25,
      features = listOf("Free custom meal ingredients", "Dedicated home cook match", "Weekend special included", "Valid for 45 days")
    ),
    KitchenSubscription(
      id = "sub_monthly",
      planName = "Monthly Tiffin",
      mealsCount = 30,
      pricePerMeal = 79,
      savingsPercent = 35,
      features = listOf("Guaranteed lunch & dinner slots", "Chef personalization", "Zero delivery fees", "Weekly rotating homestyle menu")
    )
  )

  val activeOrder: Flow<MealOrder?> = orderDao.getActiveOrder().map { entity ->
    entity?.toMealOrder(sampleBases, sampleIngredients, samplePreparations, verifiedCooks)
  }

  val allOrders: Flow<List<MealOrder>> = orderDao.getAllOrders().map { list ->
    list.map { it.toMealOrder(sampleBases, sampleIngredients, samplePreparations, verifiedCooks) }
  }

  suspend fun createAndBroadcastOrder(order: MealOrder) {
    val entity = OrderEntity(
      id = order.id,
      customerName = order.customerName,
      customerAddress = order.customerAddress,
      baseName = order.base.name,
      baseCost = order.base.baseCost,
      ingredientsSummary = order.ingredients.joinToString(", ") { it.name },
      preparationName = order.preparation.name,
      spiceLevel = order.spiceLevel.name,
      portion = order.portion.name,
      notes = order.notes,
      totalCost = order.pricing.totalCost,
      cookEarnings = order.pricing.cookEarnings,
      status = OrderStatus.SEARCHING.name,
      assignedCookId = null,
      assignedCookName = null,
      assignedCookKitchen = null,
      assignedCookRating = null,
      assignedCookDistance = null,
      createdAt = System.currentTimeMillis(),
      acceptedAt = null,
      rating = null,
      review = null
    )
    orderDao.insertOrder(entity)
  }

  /**
   * Atomic lock method:
   * First cook to claim locks the order. Returns true if won, false if already taken.
   */
  suspend fun atomicallyClaimOrder(orderId: String, cook: CookCandidate): Boolean {
    val rows = orderDao.atomicallyClaimOrder(
      orderId = orderId,
      cookId = cook.id,
      cookName = cook.name,
      kitchenName = cook.kitchenName,
      rating = cook.rating,
      distance = cook.distanceKm,
      timestamp = System.currentTimeMillis()
    )
    return rows > 0
  }

  suspend fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
    orderDao.updateOrderStatus(orderId, newStatus.name)
  }

  suspend fun submitRating(orderId: String, rating: Int, review: String) {
    orderDao.submitRating(orderId, rating, review)
  }

  suspend fun cancelOrder(orderId: String) {
    orderDao.updateOrderStatus(orderId, OrderStatus.CANCELLED.name)
  }

  private fun OrderEntity.toMealOrder(
    bases: List<MealBase>,
    ingredientsList: List<Ingredient>,
    preparations: List<PreparationStyle>,
    cooks: List<CookCandidate>
  ): MealOrder {
    val baseObj = bases.firstOrNull { it.name == this.baseName } ?: bases.first()
    val prepObj = preparations.firstOrNull { it.name == this.preparationName } ?: preparations.first()
    val spiceObj = runCatching { SpiceLevel.valueOf(this.spiceLevel) }.getOrDefault(SpiceLevel.MEDIUM)
    val portionObj = runCatching { PortionSize.valueOf(this.portion) }.getOrDefault(PortionSize.ONE_PERSON)
    val ingNames = this.ingredientsSummary.split(", ").map { it.trim() }
    val matchedIngredients = ingredientsList.filter { it.name in ingNames }

    val cookObj = if (this.assignedCookId != null) {
      cooks.firstOrNull { it.id == this.assignedCookId } ?: CookCandidate(
        id = this.assignedCookId,
        name = this.assignedCookName ?: "Verified Cook",
        kitchenName = this.assignedCookKitchen ?: "Home Kitchen",
        rating = this.assignedCookRating ?: 4.9,
        distanceKm = this.assignedCookDistance ?: 1.2,
        cuisines = listOf("Homestyle"),
        completedOrders = 500,
        onTimeRate = 98,
        repeatRate = 95,
        estPrepMinutes = 35,
        specialties = listOf("Homestyle Cooking"),
        lunchSlotsAvailable = 5,
        dinnerSlotsAvailable = 5
      )
    } else null

    val statusEnum = runCatching { OrderStatus.valueOf(this.status) }.getOrDefault(OrderStatus.SEARCHING)

    return MealOrder(
      id = this.id,
      customerName = this.customerName,
      customerAddress = this.customerAddress,
      base = baseObj,
      ingredients = matchedIngredients.ifEmpty { listOf(ingredientsList[0], ingredientsList[1]) },
      preparation = prepObj,
      spiceLevel = spiceObj,
      portion = portionObj,
      notes = this.notes,
      pricing = PricingBreakdown(
        ingredientsCost = this.cookEarnings - prepObj.effortCost,
        cookingEffort = prepObj.effortCost,
        totalCost = this.totalCost,
        cookEarnings = this.cookEarnings
      ),
      status = statusEnum,
      assignedCook = cookObj,
      createdAt = this.createdAt,
      acceptedAt = this.acceptedAt,
      customerRating = this.rating,
      reviewText = this.review
    )
  }
}
