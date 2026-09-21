package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CookCandidate
import com.example.data.model.MealOrder
import com.example.data.model.OrderStatus
import com.example.ui.theme.FreshHerbGreen
import com.example.ui.theme.HerbGreenContainer
import com.example.ui.theme.SaffronAmber
import com.example.ui.theme.TerracottaDark
import com.example.ui.theme.TerracottaPrimary

@Composable
fun CookMatchingScreen(
  order: MealOrder,
  candidateCooks: List<CookCandidate>,
  onViewOrderClick: () -> Unit,
  onSimulateCookAccept: (CookCandidate) -> Unit,
  onCancelOrderClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isMatched = order.status != OrderStatus.SEARCHING && order.assignedCook != null

  // Radar Animation
  val infiniteTransition = rememberInfiniteTransition(label = "radar")
  val radarScale1 by infiniteTransition.animateFloat(
    initialValue = 0.5f,
    targetValue = 1.4f,
    animationSpec = infiniteRepeatable(
      animation = tween(2000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "radar1"
  )
  val radarAlpha1 by infiniteTransition.animateFloat(
    initialValue = 0.6f,
    targetValue = 0.0f,
    animationSpec = infiniteRepeatable(
      animation = tween(2000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "alpha1"
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .verticalScroll(rememberScrollState())
      .padding(16.dp)
      .testTag("cook_matching_screen"),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Top Bar with Cancel / Dismiss
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = if (isMatched) "Order Confirmed" else "Matching Cook",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
      )
      if (!isMatched) {
        IconButton(onClick = onCancelOrderClick) {
          Icon(Icons.Default.Close, contentDescription = "Cancel matching")
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    if (!isMatched) {
      // 1. Radar Scanning Visualization
      Box(
        modifier = Modifier
          .size(160.dp)
          .padding(8.dp),
        contentAlignment = Alignment.Center
      ) {
        // Outer pulsing ring
        Box(
          modifier = Modifier
            .size(150.dp)
            .scale(radarScale1)
            .background(TerracottaPrimary.copy(alpha = radarAlpha1), CircleShape)
        )
        // Inner ring
        Box(
          modifier = Modifier
            .size(110.dp)
            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f), CircleShape)
        )
        // Center Icon
        Box(
          modifier = Modifier
            .size(64.dp)
            .background(TerracottaPrimary, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Radar,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(32.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = "🔎 Finding a cook...",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.ExtraBold,
        color = MaterialTheme.colorScheme.onBackground
      )
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = "7 suitable cooks nearby in your neighborhood",
        style = MaterialTheme.typography.bodyMedium,
        color = TerracottaPrimary,
        fontWeight = FontWeight.SemiBold
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Matching Criteria Engine Badge
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text(
            text = "Matching Engine evaluating:",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "• Distance & Delivery radius\n• Cuisine compatibility (${order.base.name} + ${order.preparation.name})\n• Fresh ingredients stock\n• Workload capacity & On-time score",
            style = MaterialTheme.typography.bodySmall,
            lineHeight = 18.sp,
            color = MaterialTheme.colorScheme.onSurface
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Broadcasted Cooks candidate list
      Text(
        text = "Receiving cooks in radius:",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.align(Alignment.Start)
      )
      Spacer(modifier = Modifier.height(8.dp))

      candidateCooks.forEach { cook ->
        CandidateCookRow(
          cook = cook,
          onQuickAccept = { onSimulateCookAccept(cook) }
        )
        Spacer(modifier = Modifier.height(8.dp))
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Rapido-style mechanism explanation card
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = HerbGreenContainer.copy(alpha = 0.7f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.Lock, contentDescription = null, tint = FreshHerbGreen, modifier = Modifier.size(20.dp))
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = "Rapido-style Atomic Lock: The first verified cook to tap ACCEPT instantly locks this order. No double-bookings.",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF1B5E20)
          )
        }
      }
    } else {
      // 2. COOK FOUND SCREEN (Section 6 from spec)
      AnimatedVisibility(
        visible = true,
        enter = fadeIn() + slideInVertically()
      ) {
        val cook = order.assignedCook!!
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.fillMaxWidth()
        ) {
          // Top Success Badge
          Surface(
            shape = RoundedCornerShape(20.dp),
            color = HerbGreenContainer,
            modifier = Modifier.padding(bottom = 12.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
              Icon(Icons.Default.CheckCircle, contentDescription = null, tint = FreshHerbGreen, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "👩‍🍳 COOK FOUND",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.ExtraBold,
                color = FreshHerbGreen
              )
            }
          }

          Text(
            text = cook.kitchenName,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = cook.cuisines.joinToString(" • "),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(8.dp))

          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Star, contentDescription = "Rating", tint = SaffronAmber, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "${cook.rating}",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = " (${cook.completedOrders} orders) • ${cook.distanceKm} km away",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Trust Highlights Card
          Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Text(
                text = "Verified Trust & Hygiene",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.height(12.dp))

              Row(modifier = Modifier.fillMaxWidth()) {
                TrustBadgeItem(
                  icon = Icons.Default.Verified,
                  title = "Verified Identity",
                  subtitle = "Govt ID approved",
                  modifier = Modifier.weight(1f)
                )
                TrustBadgeItem(
                  icon = Icons.Default.Security,
                  title = "Kitchen Verified",
                  subtitle = "Hygiene checked",
                  modifier = Modifier.weight(1f)
                )
              }

              Spacer(modifier = Modifier.height(12.dp))

              Row(modifier = Modifier.fillMaxWidth()) {
                TrustBadgeItem(
                  icon = Icons.Default.Timer,
                  title = "${cook.onTimeRate}% On-Time",
                  subtitle = "Fast preparation",
                  modifier = Modifier.weight(1f)
                )
                TrustBadgeItem(
                  icon = Icons.Default.Star,
                  title = "${cook.repeatRate}% Repeat",
                  subtitle = "Customer loyalty",
                  modifier = Modifier.weight(1f)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Prep time & Amount summary
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = "Estimated preparation:",
                  style = MaterialTheme.typography.labelMedium,
                  color = TerracottaDark
                )
                Text(
                  text = "${cook.estPrepMinutes} minutes",
                  style = MaterialTheme.typography.titleLarge,
                  fontWeight = FontWeight.Bold,
                  color = TerracottaDark
                )
              }
              Text(
                text = "₹${order.pricing.totalCost}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = TerracottaPrimary
              )
            }
          }

          Spacer(modifier = Modifier.height(20.dp))

          Button(
            onClick = onViewOrderClick,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TerracottaPrimary),
            modifier = Modifier
              .fillMaxWidth()
              .height(54.dp)
              .testTag("view_order_button")
          ) {
            Text(
              text = "VIEW ORDER & LIVE TRACKING",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }
  }
}

@Composable
fun CandidateCookRow(
  cook: CookCandidate,
  onQuickAccept: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
        Box(
          modifier = Modifier
            .size(8.dp)
            .background(TerracottaPrimary, CircleShape)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = cook.name,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "${cook.distanceKm} km • ${cook.rating}★ • ${cook.cuisines.first()}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      OutlinedButton(
        onClick = onQuickAccept,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = FreshHerbGreen),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp)
      ) {
        Text("Test Accept", fontSize = 12.sp, fontWeight = FontWeight.Bold)
      }
    }
  }
}

@Composable
fun TrustBadgeItem(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  title: String,
  subtitle: String,
  modifier: Modifier = Modifier
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier.padding(horizontal = 4.dp)
  ) {
    Box(
      modifier = Modifier
        .size(36.dp)
        .clip(CircleShape)
        .background(HerbGreenContainer),
      contentAlignment = Alignment.Center
    ) {
      Icon(icon, contentDescription = null, tint = FreshHerbGreen, modifier = Modifier.size(20.dp))
    }
    Spacer(modifier = Modifier.width(8.dp))
    Column {
      Text(title, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
      Text(subtitle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp)
    }
  }
}
