package com.example

import com.example.data.model.Ingredient
import com.example.data.model.MealBase
import com.example.data.model.PortionSize
import com.example.data.model.PreparationStyle
import com.example.data.pricing.PricingEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

  @Test
  fun testIntelligentPricingFormula() {
    val rice = MealBase("b1", "Rice", "🍚", 25)
    val ingredients = listOf(
      Ingredient("i1", "Potato", "🥔", 12, isVeg = true),
      Ingredient("i2", "Tomato", "🍅", 10, isVeg = true),
      Ingredient("i3", "Egg", "🥚", 20, isVeg = false)
    )
    val curry = PreparationStyle("p2", "Curry", 20, "Homestyle curry sauce")

    // Pricing calculation
    val pricing = PricingEngine.calculatePrice(
      base = rice,
      ingredients = ingredients,
      style = curry,
      portion = PortionSize.ONE_PERSON
    )

    // Formula checks
    assertEquals(25 + 12 + 10 + 20, pricing.ingredientsCost) // 67
    assertEquals(20, pricing.cookingEffort) // 20
    assertEquals(8, pricing.packagingCost) // 8
    assertEquals(18, pricing.deliveryFee) // 18
    assertEquals(8, pricing.platformFee) // 8

    val expectedTotal = pricing.ingredientsCost + pricing.cookingEffort + pricing.packagingCost + pricing.deliveryFee + pricing.platformFee
    assertEquals(expectedTotal, pricing.totalCost)
    assertEquals(pricing.ingredientsCost + pricing.cookingEffort, pricing.cookEarnings)
    assertTrue(pricing.totalCost > pricing.cookEarnings)
  }

  @Test
  fun testPortionMultipliers() {
    val rice = MealBase("b1", "Rice", "🍚", 20)
    val ingredients = listOf(Ingredient("i1", "Potato", "🥔", 10, isVeg = true))
    val prep = PreparationStyle("p1", "Fried", 10, "Stir fry")

    val singlePricing = PricingEngine.calculatePrice(rice, ingredients, prep, PortionSize.ONE_PERSON)
    val twoPersonPricing = PricingEngine.calculatePrice(rice, ingredients, prep, PortionSize.TWO_PEOPLE)

    assertTrue(twoPersonPricing.ingredientsCost > singlePricing.ingredientsCost)
    assertTrue(twoPersonPricing.totalCost > singlePricing.totalCost)
  }
}
