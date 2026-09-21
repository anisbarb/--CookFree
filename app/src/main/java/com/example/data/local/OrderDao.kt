package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
  @Query("SELECT * FROM orders ORDER BY createdAt DESC")
  fun getAllOrders(): Flow<List<OrderEntity>>

  @Query("SELECT * FROM orders WHERE id = :id LIMIT 1")
  fun getOrderById(id: String): Flow<OrderEntity?>

  @Query("SELECT * FROM orders WHERE id = :id LIMIT 1")
  suspend fun getOrderByIdOnce(id: String): OrderEntity?

  @Query("SELECT * FROM orders WHERE status != 'DELIVERED' AND status != 'CANCELLED' ORDER BY createdAt DESC LIMIT 1")
  fun getActiveOrder(): Flow<OrderEntity?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrder(order: OrderEntity)

  @Update
  suspend fun updateOrder(order: OrderEntity)

  /**
   * Atomic Rapido-style lock:
   * Returns 1 if successfully locked by this cook, 0 if already claimed by another cook.
   */
  @Query("""
    UPDATE orders 
    SET assignedCookId = :cookId, 
        assignedCookName = :cookName, 
        assignedCookKitchen = :kitchenName, 
        assignedCookRating = :rating, 
        assignedCookDistance = :distance, 
        status = 'ACCEPTED', 
        acceptedAt = :timestamp 
    WHERE id = :orderId AND (assignedCookId IS NULL OR assignedCookId = '')
  """)
  suspend fun atomicallyClaimOrder(
    orderId: String,
    cookId: String,
    cookName: String,
    kitchenName: String,
    rating: Double,
    distance: Double,
    timestamp: Long
  ): Int

  @Query("UPDATE orders SET status = :newStatus WHERE id = :orderId")
  suspend fun updateOrderStatus(orderId: String, newStatus: String)

  @Query("UPDATE orders SET rating = :rating, review = :review WHERE id = :orderId")
  suspend fun submitRating(orderId: String, rating: Int, review: String)

  @Query("DELETE FROM orders WHERE id = :orderId")
  suspend fun deleteOrder(orderId: String)
}
