package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.Dining
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.OutdoorGrill
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.TakeoutDining
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MealOrder
import com.example.data.model.OrderStatus
import com.example.ui.CookConsoleState
import com.example.ui.theme.FreshHerbGreen
import com.example.ui.theme.HerbGreenContainer
import com.example.ui.theme.SaffronAmber
import com.example.ui.theme.TerracottaDark
import com.example.ui.theme.TerracottaPrimary
import kotlin.math.roundToInt

@Composable
fun CookAppScreen(
  cookState: CookConsoleState,
  activeOrder: MealOrder?,
  onToggleOnline: () -> Unit,
  onAcceptOrder: (String) -> Unit,
  onAdvanceStage: (String, OrderStatus) -> Unit,
  onToggleLunch: (Boolean) -> Unit,
  onToggleDinner: (Boolean) -> Unit,
  onChangeCapacity: (Int) -> Unit,
  onChangeRadius: (Float) -> Unit,
  onToggleCuisine: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val availableCuisines = listOf("Assamese", "Bengali", "North Indian", "Andhra", "Hyderabadi", "Bihari")

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .testTag("cook_app_screen"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. Cook Header & Identity
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(TerracottaPrimary),
              contentAlignment = Alignment.Center
            ) {
              Text("👩‍🍳", fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = cookState.activeCookProfile.kitchenName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Partner ID: #CK-9842 • ⭐ ${cookState.activeCookProfile.rating}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          // Online / Offline Switch
          Switch(
            checked = cookState.isOnline,
            onCheckedChange = { onToggleOnline() },
            colors = SwitchDefaults.colors(
              checkedThumbColor = Color.White,
              checkedTrackColor = FreshHerbGreen
            )
          )
        }
      }
    }

    // 2. Status Banner (Offline vs Online)
    item {
      if (!cookState.isOnline) {
        // OFFLINE VIEW (Section 8 from spec)
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Box(
              modifier = Modifier
                .size(16.dp)
                .background(Color.Gray, CircleShape)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "OFFLINE",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.ExtraBold,
              letterSpacing = 2.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
              onClick = onToggleOnline,
              shape = RoundedCornerShape(16.dp),
              colors = ButtonDefaults.buttonColors(containerColor = FreshHerbGreen),
              modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
            ) {
              Icon(Icons.Default.PowerSettingsNew, contentDescription = null)
              Spacer(modifier = Modifier.width(8.dp))
              Text("GO ONLINE", fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceEvenly
            ) {
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Today's earnings", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("₹${cookState.todayEarnings}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = FreshHerbGreen)
              }
              Box(modifier = Modifier.width(1.dp).height(36.dp).background(MaterialTheme.colorScheme.outline))
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Completed", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("${cookState.completedOrders} orders", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      } else {
        // ONLINE VIEW (Section 8 from spec)
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = HerbGreenContainer),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(12.dp)
                  .background(FreshHerbGreen, CircleShape)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "🟢 YOU ARE ONLINE",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = FreshHerbGreen
              )
            }
            Text(
              text = "₹${cookState.todayEarnings} earned today",
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF1B5E20)
            )
          }
        }
      }
    }

    // 3. INCOMING BROADCAST CARD (When active order is searching)
    if (cookState.isOnline && activeOrder != null && activeOrder.status == OrderStatus.SEARCHING) {
      item {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = androidx.compose.foundation.BorderStroke(2.dp, TerracottaPrimary),
          elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("incoming_order_card")
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = TerracottaPrimary)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "NEW ORDER REQUEST",
                  style = MaterialTheme.typography.titleSmall,
                  fontWeight = FontWeight.ExtraBold,
                  color = TerracottaPrimary
                )
              }
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = HerbGreenContainer
              ) {
                Text(
                  text = "You'll earn ₹${activeOrder.pricing.cookEarnings}",
                  style = MaterialTheme.typography.labelMedium,
                  fontWeight = FontWeight.Bold,
                  color = FreshHerbGreen,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Order items breakdown (Section 8 from spec)
            Card(
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Text(
                  text = "${activeOrder.base.name} (${activeOrder.base.icon})",
                  style = MaterialTheme.typography.bodyMedium,
                  fontWeight = FontWeight.Bold
                )
                activeOrder.ingredients.forEach { ing ->
                  Text("• ${ing.name}", style = MaterialTheme.typography.bodySmall)
                }
                Text(
                  text = "Style: ${activeOrder.preparation.name} • ${activeOrder.spiceLevel.label} spicy",
                  style = MaterialTheme.typography.bodySmall,
                  fontWeight = FontWeight.SemiBold,
                  color = TerracottaDark
                )
                if (activeOrder.notes.isNotBlank()) {
                  Text(
                    text = "Customer Note: \"${activeOrder.notes}\"",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Customer: ${activeOrder.customerName} • ${activeOrder.customerAddress}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text("~35 min prep", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Rapido-style ACCEPT button
            Button(
              onClick = { onAcceptOrder(activeOrder.id) },
              shape = RoundedCornerShape(14.dp),
              colors = ButtonDefaults.buttonColors(containerColor = TerracottaPrimary),
              modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("cook_accept_order_button")
            ) {
              Text(
                text = "ACCEPT ₹${activeOrder.pricing.totalCost} (Earn ₹${activeOrder.pricing.cookEarnings})",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp
              )
            }
          }
        }
      }
    }

    // 4. ACTIVE COOKING ORDER IN PROGRESS (Section 8 from spec)
    if (activeOrder != null && activeOrder.assignedCook?.id == cookState.activeCookProfile.id && activeOrder.status != OrderStatus.SEARCHING && activeOrder.status != OrderStatus.DELIVERED) {
      item {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Text(
              text = "ORDER #${activeOrder.id}",
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.Bold,
              color = TerracottaPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Customer: ${activeOrder.customerName}",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "Area: ${activeOrder.customerAddress}",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
              text = "Current Status: ${activeOrder.status.title}",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = FreshHerbGreen
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Stage progression buttons from spec: [ START COOKING ] -> [ FOOD READY ] -> [ PACKED ]
            when (activeOrder.status) {
              OrderStatus.ACCEPTED -> {
                Button(
                  onClick = { onAdvanceStage(activeOrder.id, activeOrder.status) },
                  shape = RoundedCornerShape(14.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = TerracottaPrimary),
                  modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                  Icon(Icons.Default.Restaurant, contentDescription = null)
                  Spacer(modifier = Modifier.width(8.dp))
                  Text("PREPARE INGREDIENTS")
                }
              }
              OrderStatus.PREPARING -> {
                Button(
                  onClick = { onAdvanceStage(activeOrder.id, activeOrder.status) },
                  shape = RoundedCornerShape(14.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = TerracottaPrimary),
                  modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                  Icon(Icons.Default.OutdoorGrill, contentDescription = null)
                  Spacer(modifier = Modifier.width(8.dp))
                  Text("START COOKING")
                }
              }
              OrderStatus.COOKING -> {
                Button(
                  onClick = { onAdvanceStage(activeOrder.id, activeOrder.status) },
                  shape = RoundedCornerShape(14.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = SaffronAmber),
                  modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                  Icon(Icons.Default.Dining, contentDescription = null)
                  Spacer(modifier = Modifier.width(8.dp))
                  Text("FOOD READY")
                }
              }
              OrderStatus.PACKING -> {
                Button(
                  onClick = { onAdvanceStage(activeOrder.id, activeOrder.status) },
                  shape = RoundedCornerShape(14.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = FreshHerbGreen),
                  modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                  Icon(Icons.Default.TakeoutDining, contentDescription = null)
                  Spacer(modifier = Modifier.width(8.dp))
                  Text("PACKED & READY FOR PICKUP")
                }
              }
              OrderStatus.DELIVERY -> {
                Button(
                  onClick = { onAdvanceStage(activeOrder.id, activeOrder.status) },
                  shape = RoundedCornerShape(14.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = FreshHerbGreen),
                  modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                  Icon(Icons.Default.CheckCircle, contentDescription = null)
                  Spacer(modifier = Modifier.width(8.dp))
                  Text("CONFIRM DELIVERED")
                }
              }
              else -> {}
            }
          }
        }
      }
    }

    // 5. COOK CONTROLS AVAILABILITY (Section 9 from spec)
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Tune, contentDescription = null, tint = TerracottaPrimary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Cook Availability & Workload",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
          }

          Text(
            text = "Control when you cook, your capacity and cuisines so you never get overwhelmed.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(vertical = 4.dp)
          )

          Divider(modifier = Modifier.padding(vertical = 10.dp))

          // Available Today: Lunch / Dinner
          Text(
            text = "Available today",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
          )

          Row(modifier = Modifier.fillMaxWidth()) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.weight(1f)
            ) {
              Checkbox(
                checked = cookState.availableLunch,
                onCheckedChange = onToggleLunch,
                colors = CheckboxDefaults.colors(checkedColor = TerracottaPrimary)
              )
              Text("Lunch Slot", style = MaterialTheme.typography.bodyMedium)
            }

            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.weight(1f)
            ) {
              Checkbox(
                checked = cookState.availableDinner,
                onCheckedChange = onToggleDinner,
                colors = CheckboxDefaults.colors(checkedColor = TerracottaPrimary)
              )
              Text("Dinner Slot", style = MaterialTheme.typography.bodyMedium)
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Capacity: 10 meals
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("Meal Capacity", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Text("${cookState.capacityMeals} meals", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.ExtraBold, color = TerracottaPrimary)
          }
          Slider(
            value = cookState.capacityMeals.toFloat(),
            onValueChange = { onChangeCapacity(it.roundToInt()) },
            valueRange = 2f..30f,
            steps = 27,
            colors = SliderDefaults.colors(
              thumbColor = TerracottaPrimary,
              activeTrackColor = TerracottaPrimary
            )
          )

          Spacer(modifier = Modifier.height(10.dp))

          // Radius: Up to 3 km
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("Service Radius", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Text("Up to ${"%.1f".format(cookState.serviceRadiusKm)} km", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.ExtraBold, color = TerracottaPrimary)
          }
          Slider(
            value = cookState.serviceRadiusKm,
            onValueChange = onChangeRadius,
            valueRange = 1.0f..8.0f,
            steps = 14,
            colors = SliderDefaults.colors(
              thumbColor = TerracottaPrimary,
              activeTrackColor = TerracottaPrimary
            )
          )

          Spacer(modifier = Modifier.height(10.dp))

          // Cuisines
          Text("Cuisines you can prepare", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
          Spacer(modifier = Modifier.height(6.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            availableCuisines.take(3).forEach { cuisine ->
              val isSelected = cookState.selectedCuisines.contains(cuisine)
              FilterChip(
                selected = isSelected,
                onClick = { onToggleCuisine(cuisine) },
                label = { Text(cuisine, fontSize = 12.sp) },
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = TerracottaPrimary, selectedLabelColor = Color.White)
              )
            }
          }
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            availableCuisines.drop(3).forEach { cuisine ->
              val isSelected = cookState.selectedCuisines.contains(cuisine)
              FilterChip(
                selected = isSelected,
                onClick = { onToggleCuisine(cuisine) },
                label = { Text(cuisine, fontSize = 12.sp) },
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = TerracottaPrimary, selectedLabelColor = Color.White)
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Flexible Work model summary (Section 9)
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = "Flexibility: You can participate for 2 hours/day, 8 hours/day, or weekends only.",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(10.dp)
            )
          }
        }
      }
    }
  }
}
