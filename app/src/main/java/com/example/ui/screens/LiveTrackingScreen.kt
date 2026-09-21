package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.OutdoorGrill
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.SoupKitchen
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TakeoutDining
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MealOrder
import com.example.data.model.OrderStatus
import com.example.ui.theme.FreshHerbGreen
import com.example.ui.theme.HerbGreenContainer
import com.example.ui.theme.SaffronAmber
import com.example.ui.theme.TerracottaDark
import com.example.ui.theme.TerracottaPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveTrackingScreen(
  order: MealOrder,
  onBackClick: () -> Unit,
  onAdvanceStage: () -> Unit,
  onSubmitRating: (Int, String) -> Unit,
  onSwitchToCookApp: () -> Unit,
  modifier: Modifier = Modifier
) {
  var showRatingDialog by remember { mutableStateOf(false) }
  var ratingScore by remember { mutableStateOf(5) }
  var reviewComment by remember { mutableStateOf("") }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text("Live Order Tracking", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("Order #${order.id}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        },
        navigationIcon = {
          IconButton(onClick = onBackClick) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        },
        actions = {
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.padding(end = 12.dp)
          ) {
            Text(
              text = "₹${order.pricing.totalCost}",
              fontWeight = FontWeight.Bold,
              color = TerracottaDark,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
      )
    },
    modifier = modifier.testTag("live_tracking_screen")
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // 1. Current Status Hero Card
      item {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(
            containerColor = if (order.status == OrderStatus.DELIVERED) HerbGreenContainer else MaterialTheme.colorScheme.primaryContainer
          ),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = order.status.title.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = if (order.status == OrderStatus.DELIVERED) FreshHerbGreen else TerracottaDark
              )
              if (order.status != OrderStatus.DELIVERED) {
                Text(
                  text = "ETA: ~${order.assignedCook?.estPrepMinutes ?: 30} mins",
                  style = MaterialTheme.typography.labelSmall,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
              }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
              text = when (order.status) {
                OrderStatus.ACCEPTED -> "${order.assignedCook?.name ?: "Cook"} accepted your order."
                OrderStatus.PREPARING -> "Ingredients being prepared fresh."
                OrderStatus.COOKING -> "Your food is cooking on the stove."
                OrderStatus.PACKING -> "Your meal is being hygienically packed."
                OrderStatus.DELIVERY -> "Your hot meal is on its way."
                OrderStatus.DELIVERED -> "Enjoy your meal!"
                else -> "Searching nearby cooks..."
              },
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.ExtraBold,
              color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
              text = "Delivering to ${order.customerAddress}",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }

      // 2. The 6-Step Modern Infrastructure Timeline (Section 7 from spec)
      item {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Text(
              text = "Live Preparation Stages",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(14.dp))

            val stages = listOf(
              Triple(OrderStatus.ACCEPTED, "🟢 Accepted", "${order.assignedCook?.name ?: "Cook"} accepted your order"),
              Triple(OrderStatus.PREPARING, "🟡 Preparing", "Ingredients being washed & chopped"),
              Triple(OrderStatus.COOKING, "🟠 Cooking", "Your food is cooking on stove"),
              Triple(OrderStatus.PACKING, "🔵 Packing", "Your meal is being packed hot"),
              Triple(OrderStatus.DELIVERY, "🚴 Delivery", "Delivery partner is on the way"),
              Triple(OrderStatus.DELIVERED, "✅ Delivered", "Delivered hot to your door")
            )

            stages.forEachIndexed { index, (stageStatus, title, subtitle) ->
              val isDone = order.status.stepIndex >= stageStatus.stepIndex
              val isCurrent = order.status == stageStatus

              TimelineItem(
                title = title,
                subtitle = subtitle,
                isDone = isDone,
                isCurrent = isCurrent,
                isLast = index == stages.lastIndex
              )
            }
          }
        }
      }

      // 3. Cook Trust & Contact Card
      if (order.assignedCook != null) {
        item {
          val cook = order.assignedCook
          Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(48.dp)
                  .clip(CircleShape)
                  .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
              ) {
                Text("👩‍🍳", fontSize = 24.sp)
              }

              Spacer(modifier = Modifier.width(12.dp))

              Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = cook.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Verified",
                    tint = FreshHerbGreen,
                    modifier = Modifier.size(16.dp)
                  )
                }
                Text(
                  text = cook.kitchenName,
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                  text = "${cook.distanceKm} km away • ⭐ ${cook.rating}",
                  style = MaterialTheme.typography.labelSmall,
                  color = TerracottaPrimary,
                  fontWeight = FontWeight.SemiBold
                )
              }

              Row {
                IconButton(
                  onClick = { /* simulated call */ },
                  modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                  Icon(Icons.Default.Call, contentDescription = "Call Cook", tint = TerracottaPrimary)
                }
                Spacer(modifier = Modifier.width(6.dp))
                IconButton(
                  onClick = { /* simulated chat */ },
                  modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                  Icon(Icons.Default.Message, contentDescription = "Message Cook", tint = TerracottaPrimary)
                }
              }
            }
          }
        }
      }

      // 4. Meal Customization Summary & Receipt
      item {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = "Order Details",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "${order.portion.label} • ${order.spiceLevel.label}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = "Base: ${order.base.name} (${order.base.icon})",
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = FontWeight.SemiBold
            )
            Text(
              text = "Ingredients: ${order.ingredients.joinToString { it.name }}",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
              text = "Preparation: ${order.preparation.name} style",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (order.notes.isNotBlank()) {
              Text(
                text = "Notes: \"${order.notes}\"",
                style = MaterialTheme.typography.bodySmall,
                color = TerracottaDark,
                fontWeight = FontWeight.Medium
              )
            }

            Divider(modifier = Modifier.padding(vertical = 10.dp))

            PriceRow("Ingredients + Cooking", "₹${order.pricing.cookEarnings}")
            PriceRow("Packaging + Delivery + Platform", "₹${order.pricing.packagingCost + order.pricing.deliveryFee + order.pricing.platformFee}")
            Row(
              modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Total Paid via UPI", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
              Text("₹${order.pricing.totalCost}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = TerracottaPrimary)
            }
          }
        }
      }

      // 5. Test/Interactive Controls (Advance Stage & Role switch)
      item {
        Column(modifier = Modifier.fillMaxWidth()) {
          if (order.status != OrderStatus.DELIVERED) {
            Button(
              onClick = onAdvanceStage,
              shape = RoundedCornerShape(14.dp),
              colors = ButtonDefaults.buttonColors(containerColor = TerracottaPrimary),
              modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
              Text("Next Stage: ${getNextStageName(order.status)}")
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
              onClick = onSwitchToCookApp,
              shape = RoundedCornerShape(14.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text("Switch to Cook Console to update directly")
            }
          } else {
            // Delivered -> Rating affordance
            Card(
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = HerbGreenContainer),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Text(
                  text = "How was the food cooked by ${order.assignedCook?.name}?",
                  style = MaterialTheme.typography.titleSmall,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF1B5E20)
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                  (1..5).forEach { star ->
                    Icon(
                      imageVector = if (star <= ratingScore) Icons.Default.Star else Icons.Outlined.Star,
                      contentDescription = "Star $star",
                      tint = SaffronAmber,
                      modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .clickable { ratingScore = star }
                    )
                  }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                  value = reviewComment,
                  onValueChange = { reviewComment = it },
                  placeholder = { Text("E.g. Perfectly spiced, hot homestyle taste!") },
                  modifier = Modifier.fillMaxWidth(),
                  shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                  onClick = { onSubmitRating(ratingScore, reviewComment) },
                  colors = ButtonDefaults.buttonColors(containerColor = FreshHerbGreen),
                  shape = RoundedCornerShape(12.dp)
                ) {
                  Text("Submit Review")
                }
              }
            }
          }
          Spacer(modifier = Modifier.height(40.dp))
        }
      }
    }
  }
}

@Composable
fun TimelineItem(
  title: String,
  subtitle: String,
  isDone: Boolean,
  isCurrent: Boolean,
  isLast: Boolean
) {
  Row(modifier = Modifier.fillMaxWidth()) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Box(
        modifier = Modifier
          .size(24.dp)
          .clip(CircleShape)
          .background(
            when {
              isCurrent -> TerracottaPrimary
              isDone -> FreshHerbGreen
              else -> MaterialTheme.colorScheme.surfaceVariant
            }
          ),
        contentAlignment = Alignment.Center
      ) {
        if (isDone) {
          Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
        }
      }
      if (!isLast) {
        Box(
          modifier = Modifier
            .width(2.dp)
            .height(34.dp)
            .background(if (isDone) FreshHerbGreen else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
        )
      }
    }

    Spacer(modifier = Modifier.width(12.dp))

    Column(modifier = Modifier.padding(bottom = if (isLast) 0.dp else 16.dp)) {
      Text(
        text = title,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = if (isCurrent || isDone) FontWeight.Bold else FontWeight.Normal,
        color = if (isCurrent) TerracottaPrimary else MaterialTheme.colorScheme.onSurface
      )
      Text(
        text = subtitle,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}

private fun getNextStageName(status: OrderStatus): String {
  return when (status) {
    OrderStatus.SEARCHING -> "Accepted"
    OrderStatus.ACCEPTED -> "Start Preparing"
    OrderStatus.PREPARING -> "Start Cooking"
    OrderStatus.COOKING -> "Food Packed"
    OrderStatus.PACKING -> "Hand to Delivery"
    OrderStatus.DELIVERY -> "Delivered"
    else -> "Completed"
  }
}
