package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SoupKitchen
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.CookCandidate
import com.example.data.model.CuisineCategory
import com.example.data.model.MealOrder
import com.example.data.model.OrderStatus
import com.example.ui.theme.FreshHerbGreen
import com.example.ui.theme.HerbGreenContainer
import com.example.ui.theme.SaffronAmber
import com.example.ui.theme.TerracottaDark
import com.example.ui.theme.TerracottaPrimary

@Composable
fun CustomerHomeScreen(
  selectedLocation: String,
  availableLocations: List<String>,
  onLocationSelected: (String) -> Unit,
  onBuildMealClick: () -> Unit,
  onViewKitchenProfile: (CookCandidate) -> Unit,
  activeOrder: MealOrder?,
  onTrackOrderClick: () -> Unit,
  verifiedCooks: List<CookCandidate>,
  cuisines: List<CuisineCategory>,
  modifier: Modifier = Modifier
) {
  var locationDropdownOpen by remember { mutableStateOf(false) }
  var searchQuery by remember { mutableStateOf("") }

  // Pulsing animation for online cooks badge
  val infiniteTransition = rememberInfiniteTransition(label = "pulse")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 0.9f,
    targetValue = 1.25f,
    animationSpec = infiniteRepeatable(
      animation = tween(1000, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulseScale"
  )

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("customer_home_screen"),
    contentPadding = PaddingValues(bottom = 96.dp)
  ) {
    // 1. Top Location Header
    item {
      Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
            .clickable { locationDropdownOpen = true }
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .testTag("location_selector")
        ) {
          Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Location",
            tint = TerracottaPrimary,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = selectedLocation,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.width(4.dp))
          Icon(
            imageVector = Icons.Default.ExpandMore,
            contentDescription = "Change Location",
            modifier = Modifier.size(18.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        DropdownMenu(
          expanded = locationDropdownOpen,
          onDismissRequest = { locationDropdownOpen = false }
        ) {
          availableLocations.forEach { loc ->
            DropdownMenuItem(
              text = { Text(loc, fontWeight = if (loc == selectedLocation) FontWeight.Bold else FontWeight.Normal) },
              onClick = {
                onLocationSelected(loc)
                locationDropdownOpen = false
              }
            )
          }
        }
      }
    }

    // 2. Active Order Notice Banner (if any)
    if (activeOrder != null && activeOrder.status != OrderStatus.CANCELLED) {
      item {
        Card(
          onClick = onTrackOrderClick,
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(
            containerColor = if (activeOrder.status == OrderStatus.DELIVERED) HerbGreenContainer else MaterialTheme.colorScheme.primaryContainer
          ),
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("active_order_banner")
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(8.dp)
                    .background(FreshHerbGreen, CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "ACTIVE MEAL ORDER • ${activeOrder.id}",
                  style = MaterialTheme.typography.labelSmall,
                  fontWeight = FontWeight.Bold,
                  color = TerracottaDark
                )
              }
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "${activeOrder.base.name} with ${activeOrder.ingredients.joinToString { it.name }}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
              )
              Text(
                text = "Status: ${activeOrder.status.title}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
            Button(
              onClick = onTrackOrderClick,
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(containerColor = TerracottaPrimary),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
              Text("Track", fontSize = 13.sp)
              Icon(Icons.Default.ChevronRight, contentDescription = null, modifier = Modifier.size(16.dp))
            }
          }
        }
      }
    }

    // 3. Greeting Headline & Search Bar
    item {
      Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
          text = "What would you like?",
          style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
          color = MaterialTheme.colorScheme.onBackground
        )
        Text(
          text = "Fresh home meals cooked by neighborhood home kitchens.",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
          value = searchQuery,
          onValueChange = { searchQuery = it },
          placeholder = { Text("Search food, ingredients, or home kitchens...") },
          leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Search", tint = TerracottaPrimary)
          },
          singleLine = true,
          shape = RoundedCornerShape(16.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = TerracottaPrimary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline
          ),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("food_search_bar")
        )
      }
    }

    // 4. Hero Card: BUILD A MEAL (The Distinctive Core Experience)
    item {
      Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 10.dp)
          .testTag("hero_build_meal_card")
      ) {
        Column {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(170.dp)
          ) {
            Image(
              painter = painterResource(id = R.drawable.img_home_food_hero),
              contentDescription = "Fresh Home Food",
              contentScale = ContentScale.Crop,
              modifier = Modifier.fillMaxSize()
            )
            // Gradient scrim
            Box(
              modifier = Modifier
                .fillMaxSize()
                .background(
                  Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color(0xDD211D1B)),
                    startY = 80f
                  )
                )
            )

            // Badge
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = TerracottaPrimary,
              modifier = Modifier
                .padding(14.dp)
                .align(Alignment.TopStart)
            ) {
              Text(
                text = "CUSTOM COOKING",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
              )
            }

            Column(
              modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
            ) {
              Text(
                text = "Build Your Food",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
              Text(
                text = "Pick base, ingredients, preparation & taste",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.9f)
              )
            }
          }

          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = "Say: \"I want this food, made this way\"",
                  style = MaterialTheme.typography.bodyMedium,
                  fontWeight = FontWeight.Medium,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = "Calculates instant intelligent fair pricing",
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
              onClick = onBuildMealClick,
              shape = RoundedCornerShape(16.dp),
              colors = ButtonDefaults.buttonColors(containerColor = TerracottaPrimary),
              modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("build_a_meal_button")
            ) {
              Icon(Icons.Default.Restaurant, contentDescription = null, modifier = Modifier.size(20.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "BUILD A MEAL",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
              )
            }
          }
        }
      }
    }

    // 5. Live Indicator: "14 cooks online around you right now"
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = HerbGreenContainer),
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 6.dp)
          .testTag("online_cooks_counter_card")
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(20.dp)
          ) {
            Box(
              modifier = Modifier
                .size(16.dp)
                .scale(pulseScale)
                .background(FreshHerbGreen.copy(alpha = 0.3f), CircleShape)
            )
            Box(
              modifier = Modifier
                .size(10.dp)
                .background(FreshHerbGreen, CircleShape)
            )
          }

          Spacer(modifier = Modifier.width(10.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "14 cooks online around you right now",
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = FontWeight.Bold,
              color = FreshHerbGreen
            )
            Text(
              text = "Ready to accept your customized order immediately",
              style = MaterialTheme.typography.bodySmall,
              color = Color(0xFF1B5E20)
            )
          }

          Icon(
            imageVector = Icons.Default.Verified,
            contentDescription = "Verified Network",
            tint = FreshHerbGreen,
            modifier = Modifier.size(20.dp)
          )
        }
      }
    }

    // 6. Section: 🍱 Available Near You (Cuisine Categories with starting prices)
    item {
      Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 8.dp)) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "🍱 Available near you",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onBackground
            )
          }
          Text(
            text = "Starting ₹109",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = TerracottaPrimary
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
          contentPadding = PaddingValues(horizontal = 16.dp),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          items(cuisines) { cuisine ->
            CuisineQuickCard(
              cuisine = cuisine,
              onClick = onBuildMealClick
            )
          }
        }
      }
    }

    // 7. Verified Home Kitchens Preview
    item {
      Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Verified Neighborhood Kitchens",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
          )
          Text(
            text = "View All",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = TerracottaPrimary
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        verifiedCooks.take(3).forEach { cook ->
          HomeKitchenRowCard(
            cook = cook,
            onClick = { onViewKitchenProfile(cook) }
          )
          Spacer(modifier = Modifier.height(10.dp))
        }
      }
    }
  }
}

@Composable
fun CuisineQuickCard(
  cuisine: CuisineCategory,
  onClick: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    modifier = Modifier
      .width(130.dp)
      .clickable { onClick() }
  ) {
    Column(
      modifier = Modifier.padding(12.dp),
      horizontalAlignment = Alignment.Start
    ) {
      Text(
        text = cuisine.bannerEmoji,
        fontSize = 28.sp
      )
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = cuisine.name,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        maxLines = 1
      )
      Text(
        text = "₹${cuisine.startingPrice}",
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.ExtraBold,
        color = TerracottaPrimary
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = "Available now",
        style = MaterialTheme.typography.labelSmall,
        fontSize = 10.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}

@Composable
fun HomeKitchenRowCard(
  cook: CookCandidate,
  onClick: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(48.dp)
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "👩‍🍳",
          fontSize = 22.sp
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = cook.kitchenName,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.width(6.dp))
          Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Verified Kitchen",
            tint = FreshHerbGreen,
            modifier = Modifier.size(14.dp)
          )
        }

        Text(
          text = cook.cuisines.joinToString(" • "),
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 1
        )

        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(top = 2.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "Rating",
            tint = SaffronAmber,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(2.dp))
          Text(
            text = "${cook.rating}",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = " • ${cook.distanceKm} km • ~${cook.estPrepMinutes} min",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Icon(
        imageVector = Icons.Default.ChevronRight,
        contentDescription = "View",
        tint = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}
