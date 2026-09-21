package com.example.data.pricing

import com.example.data.model.Ingredient
import com.example.data.model.MealBase
import com.example.data.model.PortionSize
import com.example.data.model.PreparationStyle
import com.example.data.model.PricingBreakdown
import kotlin.math.roundToInt

object PricingEngine {
  const val DEFAULT_PACKAGING_FEE = 8
  const val DEFAULT_DELIVERY_FEE = 18
  const val DEFAULT_PLATFORM_FEE = 8

  fun calculatePrice(
    base: MealBase,
    ingredients: List<Ingredient>,
    style: PreparationStyle,
    portion: PortionSize
  ): PricingBreakdown {
    val rawIngredients = base.baseCost + ingredients.sumOf { it.cost }
    val scaledIngredients = (rawIngredients * portion.multiplier).roundToInt()

    val rawEffort = style.effortCost
    val scaledEffort = (rawEffort * (1f + (portion.multiplier - 1f) * 0.5f)).roundToInt()

    val packaging = (DEFAULT_PACKAGING_FEE * (if (portion == PortionSize.FAMILY) 1.5f else 1.0f)).roundToInt()
    val delivery = DEFAULT_DELIVERY_FEE
    val platform = DEFAULT_PLATFORM_FEE

    val total = scaledIngredients + scaledEffort + packaging + delivery + platform
    val cookEarnings = scaledIngredients + scaledEffort

    return PricingBreakdown(
      ingredientsCost = scaledIngredients,
      cookingEffort = scaledEffort,
      packagingCost = packaging,
      deliveryFee = delivery,
      platformFee = platform,
      totalCost = total,
      cookEarnings = cookEarnings
    )
  }
}
