package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.example.data.model.CookCandidate
import com.example.data.model.KitchenSubscription
import com.example.ui.theme.FreshHerbGreen
import com.example.ui.theme.HerbGreenContainer
import com.example.ui.theme.SaffronAmber
import com.example.ui.theme.TerracottaDark
import com.example.ui.theme.TerracottaPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KitchenDiscoveryScreen(
  verifiedCooks: List<CookCandidate>,
  subscriptions: List<KitchenSubscription>,
  selectedCook: CookCandidate?,
  onSelectCook: (CookCandidate?) -> Unit,
  onOrderFromKitchen: (CookCandidate) -> Unit,
  modifier: Modifier = Modifier
) {
  var subscribedMessage by remember { mutableStateOf<String?>(null) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .testTag("kitchen_discovery_screen")
  ) {
    LazyColumn(
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // 1. Title Header (Section 12 from spec)
      item {
        Column {
          Text(
            text = "🍲 Cooking Near You",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Discovering people and home kitchens, not corporations.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      // 2. Vision Banner: Nationwide local food network (Section 11 from spec)
      item {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text("🇮🇳", fontSize = 22.sp)
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Hyperlocal Food Network",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = TerracottaDark
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Home kitchens across Telangana, Andhra, Bengal, Assam, Bihar, Odisha & Kerala. Powered by local cooking ability + verified kitchens.",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }
      }

      // 3. Kitchens List
      items(verifiedCooks) { cook ->
        KitchenStorefrontCard(
          cook = cook,
          onClick = { onSelectCook(cook) }
        )
      }
    }

    // 4. Kitchen Profile Modal / Storefront (Section 10 & 13 from spec)
    if (selectedCook != null) {
      val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
      ModalBottomSheet(
        onDismissRequest = { onSelectCook(null) },
        sheetState = sheetState
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .padding(bottom = 32.dp)
        ) {
          // Storefront Header
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text("🏠", fontSize = 28.sp)
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = selectedCook.kitchenName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Icon(Icons.Default.Verified, contentDescription = null, tint = FreshHerbGreen, modifier = Modifier.size(18.dp))
                }
                Text(
                  text = "Cook: ${selectedCook.name} • ${selectedCook.distanceKm} km away",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Statistics: 1,247 meals completed, 98% on-time, 96% repeat customers
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
              .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
          ) {
            StatColumn("⭐ ${selectedCook.rating}", "Rating")
            StatDivider()
            StatColumn("${selectedCook.completedOrders}", "Meals made")
            StatDivider()
            StatColumn("${selectedCook.onTimeRate}%", "On-time")
            StatDivider()
            StatColumn("${selectedCook.repeatRate}%", "Repeat users")
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Specialties
          Text(
            text = "Specialties",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(6.dp))
          Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            selectedCook.specialties.take(3).forEach { specialty ->
              Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
              ) {
                Text(
                  text = "🍛 $specialty",
                  style = MaterialTheme.typography.bodySmall,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Today's availability: 12 lunch slots, 7 dinner slots
          Text(
            text = "Today's Availability",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(6.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Card(
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(containerColor = HerbGreenContainer),
              modifier = Modifier.weight(1f)
            ) {
              Column(modifier = Modifier.padding(10.dp)) {
                Text("☀️ Lunch slots", style = MaterialTheme.typography.labelSmall, color = FreshHerbGreen)
                Text("${selectedCook.lunchSlotsAvailable} available", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = FreshHerbGreen)
              }
            }
            Card(
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(containerColor = HerbGreenContainer),
              modifier = Modifier.weight(1f)
            ) {
              Column(modifier = Modifier.padding(10.dp)) {
                Text("🌙 Dinner slots", style = MaterialTheme.typography.labelSmall, color = FreshHerbGreen)
                Text("${selectedCook.dinnerSlotsAvailable} available", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = FreshHerbGreen)
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Subscriptions Section (Section 13 from spec)
          Text(
            text = "❤️ Subscribe to ${selectedCook.kitchenName}",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "Predictable home-cooked food for you, predictable demand for the cook.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(8.dp))

          subscriptions.forEach { plan ->
            Card(
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
              border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Column {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(plan.planName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(shape = RoundedCornerShape(6.dp), color = HerbGreenContainer) {
                      Text("Save ${plan.savingsPercent}%", color = FreshHerbGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                    }
                  }
                  Text("₹${plan.pricePerMeal} / meal • ${plan.mealsCount} meals total", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                Button(
                  onClick = { subscribedMessage = "Subscribed to ${plan.planName}!" },
                  shape = RoundedCornerShape(10.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = TerracottaPrimary),
                  contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                  Text("Subscribe", fontSize = 12.sp)
                }
              }
            }
          }

          if (subscribedMessage != null) {
            Text(
              text = "✅ $subscribedMessage",
              color = FreshHerbGreen,
              fontWeight = FontWeight.Bold,
              style = MaterialTheme.typography.bodySmall,
              modifier = Modifier.padding(top = 8.dp)
            )
          }

          Spacer(modifier = Modifier.height(18.dp))

          Button(
            onClick = {
              onOrderFromKitchen(selectedCook)
              onSelectCook(null)
            },
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TerracottaPrimary),
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp)
          ) {
            Icon(Icons.Default.Restaurant, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("CUSTOMIZE MEAL FOR THIS KITCHEN", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}

@Composable
fun KitchenStorefrontCard(
  cook: CookCandidate,
  onClick: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(44.dp)
              .clip(CircleShape)
              .background(TerracottaPrimary.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
          ) {
            Text("👩‍🍳", fontSize = 22.sp)
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = cook.kitchenName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.width(4.dp))
              Icon(Icons.Default.CheckCircle, contentDescription = "Verified", tint = FreshHerbGreen, modifier = Modifier.size(16.dp))
            }
            Text(
              text = cook.cuisines.joinToString(" • "),
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        Surface(
          shape = RoundedCornerShape(10.dp),
          color = MaterialTheme.colorScheme.primaryContainer
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Icon(Icons.Default.Star, contentDescription = null, tint = SaffronAmber, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(2.dp))
            Text(
              text = "${cook.rating}",
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "${cook.distanceKm} km away • ${cook.lunchSlotsAvailable} lunch / ${cook.dinnerSlotsAvailable} dinner slots",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
          text = "View Kitchen →",
          style = MaterialTheme.typography.labelMedium,
          fontWeight = FontWeight.Bold,
          color = TerracottaPrimary
        )
      }
    }
  }
}

@Composable
fun StatColumn(value: String, label: String) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(value, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
    Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp)
  }
}

@Composable
fun StatDivider() {
  Box(
    modifier = Modifier
      .width(1.dp)
      .height(28.dp)
      .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
  )
}
