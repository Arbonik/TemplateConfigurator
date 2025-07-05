package org.example.project.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.project.data.CreatureBankConfig
import org.example.project.data.CreatureBuildingConfig
import org.example.project.data.ShopBuildingConfig
import org.example.project.ui.common.DecimalInputField
import kotlin.text.ifEmpty

@Composable
fun ShopConfigSection(
    shops: List<ShopBuildingConfig>,
    onShopsChanged: (List<ShopBuildingConfig>) -> Unit
) {
    var selectedShopIndex by remember { mutableStateOf<Int?>(null) }

    Row(modifier = Modifier.fillMaxSize()) {
        // Shop list
        Column(modifier = Modifier.width(200.dp).padding(8.dp).verticalScroll(rememberScrollState())) {
            Text("Shops", style = MaterialTheme.typography.headlineSmall)
            HorizontalDivider()

            shops.forEachIndexed { index, shop ->
                Button(
                    onClick = { selectedShopIndex = index },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedShopIndex == index) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text("Shop ${shop.Id}", color = MaterialTheme.colorScheme.onSurface)
                }
            }

            Button(
                onClick = {
                    val newId = (shops.maxOfOrNull { it.Id } ?: 0) + 1
                    onShopsChanged(
                        shops + ShopBuildingConfig(
                            Id = newId,
                            Value = 0,
                            GuardStrenght = 0,
                            CreatureBankConfig = CreatureBankConfig(
                                Name = "",
                                CreaturesPool = emptyList(),
                                GuardsPool = emptyList(),
                                CreatureCostMultiplier = 1.0,
                                CreatureGrowMultiplier = 1.0,
                                CreatureResourcesMultiplier = 1.0,
                                GuardGrowMultiplier = 1.0
                            )
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Text("Add Shop")
            }
        }

        // Shop details
        Box(modifier = Modifier.weight(1f).padding(8.dp)) {
            selectedShopIndex?.let { index ->
                ShopEditor(
                    shop = shops[index],
                    onShopChanged = { updatedShop ->
                        onShopsChanged(shops.toMutableList().apply {
                            set(index, updatedShop)
                        })
                    },
                    onDelete = {
                        onShopsChanged(shops.toMutableList().apply { removeAt(index) })
                        selectedShopIndex = null
                    }
                )
            } ?: run {
                Text("Select a shop to edit", modifier = Modifier.fillMaxSize().wrapContentSize())
            }
        }
    }
}

@Composable
fun ShopEditor(
    shop: ShopBuildingConfig,
    onShopChanged: (ShopBuildingConfig) -> Unit,
    onDelete: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
        Text("Shop Configuration", style = MaterialTheme.typography.headlineMedium)

        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text("Shop ID: ${shop.Id}", style = MaterialTheme.typography.titleMedium)
            Button(
                onClick = onDelete, colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text("Delete Shop")
            }
        }
// Value (numeric field)
        DecimalInputField(
            value = shop.Value.toString(),
            title = "Value",
            onValueChange = { newValue ->
                onShopChanged(shop.copy(Value = newValue.toLongOrNull()))
            },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

// Guard Strength (numeric field)
        DecimalInputField(
            value = shop.GuardStrenght.toString(),
            title = "Guard Strength",
            onValueChange = { newValue ->
                onShopChanged(shop.copy(GuardStrenght = newValue.toLongOrNull()))
            },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        DecimalInputField(
            value = shop.XdbRef ?: "",
            onValueChange = { newValue ->
                onShopChanged(shop.copy(XdbRef = newValue.ifEmpty { null }))
            },
            title = "Xdb Reference",
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        Text(
            "Creature Building Config",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
        shop.CreatureBuildingConfig?.let { config ->
            CreatureBuildingConfigEditor(
                config = config,
                onConfigChanged = { newConfig ->
                    onShopChanged(shop.copy(CreatureBuildingConfig = newConfig))
                },
                onDelete = {
                    onShopChanged(shop.copy(CreatureBuildingConfig = null))
                }
            )
        } ?: run {
            Button(
                onClick = {
                    onShopChanged(
                        shop.copy(
                            CreatureBuildingConfig = CreatureBuildingConfig(
                                TiersPool = emptyList(),
                                NoGrades = null,
                                Grades = null,
                                Neutrals = null,
                                CreatureIds = emptyList(),
                                CostMultiplier = null,
                                ResourcesMultiplier = null,
                                GrowMultiplier = null
                            )
                        )
                    )
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Add Creature Building Config")
            }
        }

        Text(
            "Creature Bank Config",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
        CreatureBankConfigEditor(
            config = shop.CreatureBankConfig,
            onConfigChanged = { newConfig ->
                onShopChanged(shop.copy(CreatureBankConfig = newConfig))
            }
        )
    }
}