package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
  @PrimaryKey val id: String,
  val customerName: String,
  val customerAddress: String,
  val baseName: String,
  val baseCost: Int,
  val ingredientsSummary: String,
  val preparationName: String,
  val spiceLevel: String,
  val portion: String,
  val notes: String,
  val totalCost: Int,
  val cookEarnings: Int,
  val status: String,
  val assignedCookId: String?,
  val assignedCookName: String?,
  val assignedCookKitchen: String?,
  val assignedCookRating: Double?,
  val assignedCookDistance: Double?,
  val createdAt: Long,
  val acceptedAt: Long?,
  val rating: Int?,
  val review: String?
)
