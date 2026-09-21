package com.example.data.model

data class MealBase(
  val id: String,
  val name: String,
  val icon: String,
  val baseCost: Int
)

data class Ingredient(
  val id: String,
  val name: String,
  val icon: String,
  val cost: Int,
  val isVeg: Boolean
)

data class PreparationStyle(
  val id: String,
  val name: String,
  val effortCost: Int,
  val description: String
)

enum class SpiceLevel(val label: String, val emoji: String) {
  MILD("Mild", "🌱"),
  MEDIUM("Medium", "🌶️"),
  SPICY("Spicy", "🔥")
}

enum class PortionSize(val label: String, val multiplier: Float, val subtitle: String) {
  ONE_PERSON("1 person", 1.0f, "Standard plate"),
  TWO_PEOPLE("2 people", 1.8f, "Double meal"),
  FAMILY("Family", 3.2f, "3-4 members")
}

data class PricingBreakdown(
  val ingredientsCost: Int,
  val cookingEffort: Int,
  val packagingCost: Int = 8,
  val deliveryFee: Int = 18,
  val platformFee: Int = 8,
  val totalCost: Int,
  val cookEarnings: Int
)

enum class OrderStatus(val title: String, val description: String, val stepIndex: Int) {
  SEARCHING("Finding a cook...", "Broadcasting to qualified home kitchens nearby", 0),
  ACCEPTED("Accepted", "Cook accepted your order", 1),
  PREPARING("Preparing", "Fresh ingredients being washed & chopped", 2),
  COOKING("Cooking", "Your food is being freshly cooked on the stove", 3),
  PACKING("Packing", "Your meal is being sealed hot & hygienically", 4),
  DELIVERY("On the way", "Delivery partner picked up your package", 5),
  DELIVERED("Delivered", "Enjoy your wholesome home-cooked meal!", 6),
  CANCELLED("Cancelled", "Order was cancelled", -1)
}

data class CookCandidate(
  val id: String,
  val name: String,
  val kitchenName: String,
  val rating: Double,
  val distanceKm: Double,
  val cuisines: List<String>,
  val completedOrders: Int,
  val onTimeRate: Int,
  val repeatRate: Int,
  val estPrepMinutes: Int,
  val specialties: List<String>,
  val lunchSlotsAvailable: Int,
  val dinnerSlotsAvailable: Int,
  val isVerifiedKitchen: Boolean = true,
  val isGovtIdVerified: Boolean = true,
  val phone: String = "+91 98765 43210"
)

data class MealOrder(
  val id: String,
  val customerName: String,
  val customerAddress: String,
  val base: MealBase,
  val ingredients: List<Ingredient>,
  val preparation: PreparationStyle,
  val spiceLevel: SpiceLevel,
  val portion: PortionSize,
  val notes: String = "",
  val pricing: PricingBreakdown,
  val status: OrderStatus = OrderStatus.SEARCHING,
  val assignedCook: CookCandidate? = null,
  val createdAt: Long = System.currentTimeMillis(),
  val acceptedAt: Long? = null,
  val customerRating: Int? = null,
  val reviewText: String? = null
)

data class CuisineCategory(
  val name: String,
  val startingPrice: Int,
  val bannerEmoji: String,
  val description: String
)

data class KitchenSubscription(
  val id: String,
  val planName: String,
  val mealsCount: Int,
  val pricePerMeal: Int,
  val savingsPercent: Int,
  val features: List<String>
)
