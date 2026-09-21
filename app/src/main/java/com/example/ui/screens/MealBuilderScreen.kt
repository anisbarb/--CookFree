package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.data.model.Ingredient
import com.example.data.model.MealBase
import com.example.data.model.PortionSize
import com.example.data.model.PreparationStyle
import com.example.data.model.PricingBreakdown
import com.example.data.model.SpiceLevel
import com.example.ui.MealBuilderState
import com.example.ui.theme.FreshHerbGreen
import com.example.ui.theme.HerbGreenContainer
import com.example.ui.theme.SaffronAmber
import com.example.ui.theme.TerracottaDark
import com.example.ui.theme.TerracottaPrimary

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MealBuilderScreen(
  builderState: MealBuilderState,
  availableBases: List<MealBase>,
  availableIngredients: List<Ingredient>,
  availablePreparations: List<PreparationStyle>,
  onSelectBase: (MealBase) -> Unit,
  onToggleIngredient: (Ingredient) -> Unit,
  onSelectPreparation: (PreparationStyle) -> Unit,
  onSelectSpice: (SpiceLevel) -> Unit,
  onSelectPortion: (PortionSize) -> Unit,
  onNotesChange: (String) -> Unit,
  onFindCookClick: () -> Unit,
  onBackClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  var showPricingDetails by remember { mutableStateOf(false) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text("Build Your Food", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("Made your way, freshly cooked", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        },
        navigationIcon = {
          IconButton(onClick = onBackClick) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.surface
        )
      )
    },
    bottomBar = {
      // Sticky Bottom Price and Find a Cook Action Bar
      Surface(
        tonalElevation = 8.dp,
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "Estimated Price",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Row(verticalAlignment = Alignment.Bottom) {
                Text(
                  text = "₹${builderState.pricing.totalCost}",
                  style = MaterialTheme.typography.headlineMedium,
                  fontWeight = FontWeight.ExtraBold,
                  color = TerracottaPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                  shape = RoundedCornerShape(8.dp),
                  color = HerbGreenContainer
                ) {
                  Text(
                    text = "Cook earns ₹${builderState.pricing.cookEarnings}",
                    style = MaterialTheme.typography.labelSmall,
                    color = FreshHerbGreen,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              }
            }

            Button(
              onClick = onFindCookClick,
              shape = RoundedCornerShape(16.dp),
              colors = ButtonDefaults.buttonColors(containerColor = TerracottaPrimary),
              contentPadding = PaddingValues(horizontal = 24.dp, vertical = 14.dp),
              modifier = Modifier.testTag("find_a_cook_button")
            ) {
              Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(20.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "FIND A COOK",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }
    },
    modifier = modifier.testTag("meal_builder_screen")
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues),
      contentPadding = PaddingValues(16.dp)
    ) {
      // 1. Choose a Base
      item {
        SectionHeader(
          stepNumber = "1",
          title = "Choose a base",
          subtitle = "What forms the foundation of your meal?"
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          items(availableBases) { base ->
            val isSelected = base.id == builderState.selectedBase.id
            BaseSelectionChip(
              base = base,
              isSelected = isSelected,
              onClick = { onSelectBase(base) }
            )
          }
        }
        Spacer(modifier = Modifier.height(20.dp))
      }

      // 2. Choose Ingredients
      item {
        SectionHeader(
          stepNumber = "2",
          title = "Choose ingredients",
          subtitle = "Pick what goes into your pot (multi-select)"
        )
        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          availableIngredients.forEach { ingredient ->
            val isSelected = builderState.selectedIngredients.any { it.id == ingredient.id }
            IngredientTag(
              ingredient = ingredient,
              isSelected = isSelected,
              onClick = { onToggleIngredient(ingredient) }
            )
          }
        }
        Spacer(modifier = Modifier.height(20.dp))
      }

      // 3. Choose Preparation
      item {
        SectionHeader(
          stepNumber = "3",
          title = "Choose preparation",
          subtitle = "How should the cook prepare it?"
        )
        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          availablePreparations.forEach { prep ->
            val isSelected = prep.id == builderState.selectedPrep.id
            PreparationRow(
              prep = prep,
              isSelected = isSelected,
              onClick = { onSelectPreparation(prep) }
            )
          }
        }
        Spacer(modifier = Modifier.height(20.dp))
      }

      // 4. Choose Taste / Spice Level
      item {
        SectionHeader(
          stepNumber = "4",
          title = "Choose taste",
          subtitle = "Adjust heat & spice intensity"
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          SpiceLevel.values().forEach { spice ->
            val isSelected = spice == builderState.selectedSpice
            SpiceOptionCard(
              spice = spice,
              isSelected = isSelected,
              onClick = { onSelectSpice(spice) },
              modifier = Modifier.weight(1f)
            )
          }
        }
        Spacer(modifier = Modifier.height(20.dp))
      }

      // 5. Quantity / Portion
      item {
        SectionHeader(
          stepNumber = "5",
          title = "Quantity",
          subtitle = "How many people are eating?"
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          PortionSize.values().forEach { portion ->
            val isSelected = portion == builderState.selectedPortion
            PortionOptionCard(
              portion = portion,
              isSelected = isSelected,
              onClick = { onSelectPortion(portion) },
              modifier = Modifier.weight(1f)
            )
          }
        }
        Spacer(modifier = Modifier.height(20.dp))
      }

      // 6. Custom Instructions / Homestyle Notes
      item {
        Text(
          text = "Special Cooking Notes (Optional)",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = "e.g., \"Less oil\", \"Add extra curry leaves\", \"No sugar in curry\"",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = builderState.notes,
          onValueChange = onNotesChange,
          placeholder = { Text("Tell the cook your preferences...") },
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = TerracottaPrimary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline
          ),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("cooking_notes_input")
        )
        Spacer(modifier = Modifier.height(20.dp))
      }

      // 7. Intelligent Pricing Breakdown (As requested in spec)
      item {
        PricingCard(
          pricing = builderState.pricing,
          isExpanded = showPricingDetails,
          onToggleExpand = { showPricingDetails = !showPricingDetails }
        )
        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}

@Composable
fun SectionHeader(
  stepNumber: String,
  title: String,
  subtitle: String
) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    Box(
      modifier = Modifier
        .size(24.dp)
        .clip(CircleShape)
        .background(TerracottaPrimary),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = stepNumber,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp
      )
    }
    Spacer(modifier = Modifier.width(8.dp))
    Column {
      Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
      )
      Text(
        text = subtitle,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}

@Composable
fun BaseSelectionChip(
  base: MealBase,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  Surface(
    shape = RoundedCornerShape(16.dp),
    color = if (isSelected) TerracottaPrimary else MaterialTheme.colorScheme.surface,
    border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
    modifier = Modifier.clickable { onClick() }
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
      Text(base.icon, fontSize = 20.sp)
      Spacer(modifier = Modifier.width(8.dp))
      Column {
        Text(
          text = base.name,
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
          color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = "₹${base.baseCost}",
          style = MaterialTheme.typography.labelSmall,
          color = if (isSelected) Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  }
}

@Composable
fun IngredientTag(
  ingredient: Ingredient,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  FilterChip(
    selected = isSelected,
    onClick = onClick,
    label = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(ingredient.icon, fontSize = 16.sp)
        Spacer(modifier = Modifier.width(6.dp))
        Text(ingredient.name, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
        Spacer(modifier = Modifier.width(4.dp))
        Text("+₹${ingredient.cost}", fontSize = 11.sp, color = if (isSelected) Color.White.copy(alpha = 0.9f) else MaterialTheme.colorScheme.onSurfaceVariant)
      }
    },
    leadingIcon = if (isSelected) {
      { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White) }
    } else null,
    colors = FilterChipDefaults.filterChipColors(
      selectedContainerColor = TerracottaPrimary,
      selectedLabelColor = Color.White
    ),
    shape = RoundedCornerShape(12.dp)
  )
}

@Composable
fun PreparationRow(
  prep: PreparationStyle,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
    ),
    border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, TerracottaPrimary) else null,
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
          .size(20.dp)
          .clip(CircleShape)
          .border(2.dp, if (isSelected) TerracottaPrimary else MaterialTheme.colorScheme.outline, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        if (isSelected) {
          Box(
            modifier = Modifier
              .size(10.dp)
              .clip(CircleShape)
              .background(TerracottaPrimary)
          )
        }
      }
      Spacer(modifier = Modifier.width(12.dp))
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = prep.name,
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = prep.description,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
      Text(
        text = "₹${prep.effortCost} effort",
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.SemiBold,
        color = TerracottaDark
      )
    }
  }
}

@Composable
fun SpiceOptionCard(
  spice: SpiceLevel,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
    ),
    border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, TerracottaPrimary) else null,
    modifier = modifier.clickable { onClick() }
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(spice.emoji, fontSize = 24.sp)
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = spice.label,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
      )
    }
  }
}

@Composable
fun PortionOptionCard(
  portion: PortionSize,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
    ),
    border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, TerracottaPrimary) else null,
    modifier = modifier.clickable { onClick() }
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp, horizontal = 8.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = portion.label,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = portion.subtitle,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontSize = 10.sp
      )
    }
  }
}

@Composable
fun PricingCard(
  pricing: PricingBreakdown,
  isExpanded: Boolean,
  onToggleExpand: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Shield, contentDescription = null, tint = FreshHerbGreen, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Intelligent Transparent Pricing",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
          )
        }
        Text(
          text = if (isExpanded) "Hide details" else "View breakdown",
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.Bold,
          color = TerracottaPrimary,
          modifier = Modifier.clickable { onToggleExpand() }
        )
      }

      Text(
        text = "The platform protects both sides. Customer pays fair rates, cook earns direct wages.",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(vertical = 4.dp)
      )

      Divider(modifier = Modifier.padding(vertical = 8.dp))

      // The exact formula from prompt:
      PriceRow("Ingredients", "₹${pricing.ingredientsCost}")
      PriceRow("Cooking effort", "₹${pricing.cookingEffort}")

      if (isExpanded) {
        PriceRow("Packaging", "₹${pricing.packagingCost}")
        PriceRow("Delivery", "₹${pricing.deliveryFee}")
        PriceRow("Platform fee", "₹${pricing.platformFee}")
      } else {
        PriceRow("Packaging + Delivery + Platform", "₹${pricing.packagingCost + pricing.deliveryFee + pricing.platformFee}")
      }

      Divider(modifier = Modifier.padding(vertical = 8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Total Customer Pays",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = "₹${pricing.totalCost}",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.ExtraBold,
          color = TerracottaPrimary
        )
      }

      Spacer(modifier = Modifier.height(6.dp))

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .background(HerbGreenContainer)
          .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Cook Net Earnings",
          style = MaterialTheme.typography.bodySmall,
          fontWeight = FontWeight.Bold,
          color = FreshHerbGreen
        )
        Text(
          text = "₹${pricing.cookEarnings}",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.ExtraBold,
          color = FreshHerbGreen
        )
      }
    }
  }
}

@Composable
fun PriceRow(label: String, amount: String) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 3.dp),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    Text(amount, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
  }
}
