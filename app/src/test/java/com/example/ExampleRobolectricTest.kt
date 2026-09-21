package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.OrderEntity
import com.example.data.local.RasoiDatabase
import com.example.data.model.OrderStatus
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  private lateinit var db: RasoiDatabase

  @Before
  fun createDb() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    db = Room.inMemoryDatabaseBuilder(context, RasoiDatabase::class.java)
      .allowMainThreadQueries()
      .build()
  }

  @After
  fun closeDb() {
    db.close()
  }

  @Test
  fun readStringFromContext() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Rasoi", appName)
  }

  @Test
  fun testAtomicLockOneOrderOneCook() = runBlocking {
    val orderDao = db.orderDao()

    // Insert order in SEARCHING state with no assigned cook
    val initialOrder = OrderEntity(
      id = "ORD-TEST-1",
      customerName = "Anis",
      customerAddress = "Gachibowli",
      baseName = "Rice",
      baseCost = 25,
      ingredientsSummary = "Potato, Tomato",
      preparationName = "Curry",
      spiceLevel = "MEDIUM",
      portion = "1 person",
      notes = "Less oil",
      totalCost = 111,
      cookEarnings = 77,
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
    orderDao.insertOrder(initialOrder)

    // Cook 1 (Rina) attempts to claim order atomically
    val claimCook1 = orderDao.atomicallyClaimOrder(
      orderId = "ORD-TEST-1",
      cookId = "cook_1",
      cookName = "Rina",
      kitchenName = "Rina's Kitchen",
      rating = 4.9,
      distance = 1.1,
      timestamp = System.currentTimeMillis()
    )
    // First cook succeeds
    assertEquals(1, claimCook1)

    // Cook 2 (Fatima) attempts to claim the same order concurrently
    val claimCook2 = orderDao.atomicallyClaimOrder(
      orderId = "ORD-TEST-1",
      cookId = "cook_2",
      cookName = "Fatima",
      kitchenName = "Fatima's Mughlai",
      rating = 4.8,
      distance = 1.4,
      timestamp = System.currentTimeMillis()
    )
    // Second cook fails because order is already atomically locked!
    assertEquals(0, claimCook2)

    // Verify final state in database is assigned to Cook 1
    val order = orderDao.getOrderByIdOnce("ORD-TEST-1")
    assertEquals("cook_1", order?.assignedCookId)
    assertEquals("Rina", order?.assignedCookName)
    assertEquals(OrderStatus.ACCEPTED.name, order?.status)
  }
}
