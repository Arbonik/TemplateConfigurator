package org.example.project.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.example.project.data.*
import org.example.project.data.enums.BuildingMode
import org.example.project.data.enums.BuildingTexture
import org.example.project.data.enums.TerrainType
import org.example.project.ui.common.EnumDropdown

@Composable
fun ResourceCostEditor(
    cost: GMRebuildCost,
    onCostChanged: (GMRebuildCost) -> Unit
) {
    Column {
        // Wood
        OutlinedTextField(
            value = cost.Wood.toString(),
            onValueChange = { newValue ->
                newValue.toLongOrNull()?.let { wood ->
                    onCostChanged(cost.copy(Wood = wood))
                }
            },
            label = { Text("Wood") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )

        // Ore
        OutlinedTextField(
            value = cost.Ore.toString(),
            onValueChange = { newValue ->
                newValue.toLongOrNull()?.let { ore ->
                    onCostChanged(cost.copy(Ore = ore))
                }
            },
            label = { Text("Ore") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )

        // Mercury
        OutlinedTextField(
            value = cost.Mercury.toString(),
            onValueChange = { newValue ->
                newValue.toLongOrNull()?.let { mercury ->
                    onCostChanged(cost.copy(Mercury = mercury))
                }
            },
            label = { Text("Mercury") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )

        // Sulfur
        OutlinedTextField(
            value = cost.Sulfur.toString(),
            onValueChange = { newValue ->
                newValue.toLongOrNull()?.let { sulfur ->
                    onCostChanged(cost.copy(Sulfur = sulfur))
                }
            },
            label = { Text("Sulfur") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )

        // Gem
        OutlinedTextField(
            value = cost.Gem.toString(),
            onValueChange = { newValue ->
                newValue.toLongOrNull()?.let { gem ->
                    onCostChanged(cost.copy(Gem = gem))
                }
            },
            label = { Text("Gem") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )

        // Crystal
        OutlinedTextField(
            value = cost.Crystal.toString(),
            onValueChange = { newValue ->
                newValue.toLongOrNull()?.let { crystal ->
                    onCostChanged(cost.copy(Crystal = crystal))
                }
            },
            label = { Text("Crystal") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )

        // Gold
        OutlinedTextField(
            value = cost.Gold.toString(),
            onValueChange = { newValue ->
                newValue.toLongOrNull()?.let { gold ->
                    onCostChanged(cost.copy(Gold = gold))
                }
            },
            label = { Text("Gold") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )
    }
}

@Composable
fun TerrainConfigSection(
    terrains: List<TerrainConfig>,
    onTerrainsChanged: (List<TerrainConfig>) -> Unit
) {
    var selectedTerrainIndex by remember { mutableStateOf<Int?>(null) }

    Row(modifier = Modifier.fillMaxSize()) {
        // Terrain list
        Column(modifier = Modifier.width(200.dp).padding(8.dp).verticalScroll(rememberScrollState())) {
            Text("Terrain Configs", style = MaterialTheme.typography.headlineSmall)
            Divider()

            terrains.forEachIndexed { index, terrain ->
                Button(
                    onClick = { selectedTerrainIndex = index },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTerrainIndex == index)
                            MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text(terrain.TerrainType.toString(), color = MaterialTheme.colorScheme.onSurface)
                }
            }

            Button(
                onClick = {
                    val newTerrain = TerrainConfig(
                        TerrainType = TerrainType.values().firstOrNull { type ->
                            !terrains.any { it.TerrainType == type }
                        } ?: TerrainType.Terrain1,
                        MirrorTerrainType = null,
                        BuildingsToDelete = emptyList(),
                        BuildingsToAdd = emptyList(),
                        NewLuckMoraleBuildings = null,
                        NewShopBuildings = null,
                        NewResourceGivers = null,
                        NewUpgradeBuildings = null,
                        NewShrines = null,
                        NewTreasuryBuildings = null,
                        NewBuffBuildings = null
                    )
                    onTerrainsChanged(terrains + newTerrain)
                },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Text("Add Terrain Config")
            }
        }

        // Terrain details
        Box(modifier = Modifier.weight(1f).padding(8.dp)) {
            selectedTerrainIndex?.let { index ->
                TerrainEditor(
                    terrain = terrains[index],
                    onTerrainChanged = { updatedTerrain ->
                        onTerrainsChanged(terrains.toMutableList().apply {
                            set(index, updatedTerrain)
                        })
                    },
                    onDelete = {
                        onTerrainsChanged(terrains.toMutableList().apply { removeAt(index) })
                        selectedTerrainIndex = null
                    }
                )
            } ?: run {
                Text(
                    "Select a terrain to edit",
                    modifier = Modifier.fillMaxSize().wrapContentSize()
                )
            }
        }
    }
}

@Composable
fun TerrainEditor(
    terrain: TerrainConfig,
    onTerrainChanged: (TerrainConfig) -> Unit,
    onDelete: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
        Text("Terrain Configuration", style = MaterialTheme.typography.headlineMedium)

        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(terrain.TerrainType.toString(), style = MaterialTheme.typography.titleMedium)
            Button(
                onClick = onDelete,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text("Delete Config")
            }
        }

        // Mirror Terrain Type
        OutlinedTextField(
            value = terrain.MirrorTerrainType?.toString() ?: "",
            onValueChange = { newValue ->
                onTerrainChanged(
                    terrain.copy(
                        MirrorTerrainType = newValue.toIntOrNull()
                    )
                )
            },
            label = { Text("Mirror Terrain Type ID") },
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )

        // Buildings to Delete
        Text(
            "Buildings to Delete", style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 16.dp)
        )
        ChipGroup(
            items = terrain.BuildingsToDelete,
            onItemRemoved = { buildingId ->
                onTerrainChanged(
                    terrain.copy(
                        BuildingsToDelete = terrain.BuildingsToDelete - buildingId
                    )
                )
            }
        )
        var newBuildingToDelete by remember { mutableStateOf("") }
        Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            OutlinedTextField(
                value = newBuildingToDelete,
                onValueChange = { newBuildingToDelete = it },
                label = { Text("Add Building ID to Delete") },
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = {
                    if (newBuildingToDelete.isNotBlank()) {
                        onTerrainChanged(
                            terrain.copy(
                                BuildingsToDelete = terrain.BuildingsToDelete + newBuildingToDelete
                            )
                        )
                        newBuildingToDelete = ""
                    }
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Add")
            }
        }

        // Buildings to Add
        Text(
            "Buildings to Add", style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 16.dp)
        )
        ChipGroup(
            items = terrain.BuildingsToAdd.map { it.toString() },
            onItemRemoved = { buildingIdStr ->
                buildingIdStr.toIntOrNull()?.let { buildingId ->
                    onTerrainChanged(
                        terrain.copy(
                            BuildingsToAdd = terrain.BuildingsToAdd - buildingId
                        )
                    )
                }
            }
        )
        var newBuildingToAdd by remember { mutableStateOf("") }
        Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            OutlinedTextField(
                value = newBuildingToAdd,
                onValueChange = { newBuildingToAdd = it },
                label = { Text("Add Building ID to Add") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )
            Button(
                onClick = {
                    newBuildingToAdd.toIntOrNull()?.let { buildingId ->
                        if (!terrain.BuildingsToAdd.contains(buildingId)) {
                            onTerrainChanged(
                                terrain.copy(
                                    BuildingsToAdd = terrain.BuildingsToAdd + buildingId
                                )
                            )
                            newBuildingToAdd = ""
                        }
                    }
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Add")
            }
        }

        // Building Category Configurations
        val buildingCategories = listOf(
            "Luck/Morale Buildings" to terrain.NewLuckMoraleBuildings,
            "Shop Buildings" to terrain.NewShopBuildings,
            "Resource Givers" to terrain.NewResourceGivers,
            "Upgrade Buildings" to terrain.NewUpgradeBuildings,
            "Shrines" to terrain.NewShrines,
            "Treasury Buildings" to terrain.NewTreasuryBuildings,
            "Buff Buildings" to terrain.NewBuffBuildings
        )

        buildingCategories.forEach { (name, config) ->
            Text(
                name, style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
            TerrainBuildingsConfigEditor(
                config = config,
                onConfigChanged = { newConfig ->
                    val updatedTerrain = when (name) {
                        "Luck/Morale Buildings" -> terrain.copy(NewLuckMoraleBuildings = newConfig)
                        "Shop Buildings" -> terrain.copy(NewShopBuildings = newConfig)
                        "Resource Givers" -> terrain.copy(NewResourceGivers = newConfig)
                        "Upgrade Buildings" -> terrain.copy(NewUpgradeBuildings = newConfig)
                        "Shrines" -> terrain.copy(NewShrines = newConfig)
                        "Treasury Buildings" -> terrain.copy(NewTreasuryBuildings = newConfig)
                        "Buff Buildings" -> terrain.copy(NewBuffBuildings = newConfig)
                        else -> terrain
                    }
                    onTerrainChanged(updatedTerrain)
                },
                onDelete = {
                    val updatedTerrain = when (name) {
                        "Luck/Morale Buildings" -> terrain.copy(NewLuckMoraleBuildings = null)
                        "Shop Buildings" -> terrain.copy(NewShopBuildings = null)
                        "Resource Givers" -> terrain.copy(NewResourceGivers = null)
                        "Upgrade Buildings" -> terrain.copy(NewUpgradeBuildings = null)
                        "Shrines" -> terrain.copy(NewShrines = null)
                        "Treasury Buildings" -> terrain.copy(NewTreasuryBuildings = null)
                        "Buff Buildings" -> terrain.copy(NewBuffBuildings = null)
                        else -> terrain
                    }
                    onTerrainChanged(updatedTerrain)
                }
            )
        }
    }
}

@Composable
fun TerrainBuildingsConfigEditor(
    config: TerrainBuildingsConfig?,
    onConfigChanged: (TerrainBuildingsConfig) -> Unit,
    onDelete: () -> Unit
) {
    if (config != null) {
        Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Configuration", style = MaterialTheme.typography.titleSmall)
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }

                // Clear Buildings checkbox
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Checkbox(
                        checked = config.ClearBuildings ?: false,
                        onCheckedChange = { newValue ->
                            onConfigChanged(config.copy(ClearBuildings = newValue))
                        }
                    )
                    Text("Clear Existing Buildings", modifier = Modifier.padding(start = 8.dp))
                }

                // Buildings to Delete
                Text("Buildings to Delete", style = MaterialTheme.typography.titleSmall)
                ChipGroup(
                    items = config.BuildingsToDelete,
                    onItemRemoved = { buildingId ->
                        onConfigChanged(
                            config.copy(
                                BuildingsToDelete = config.BuildingsToDelete - buildingId
                            )
                        )
                    }
                )
                var newBuildingToDelete by remember { mutableStateOf("") }
                Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                    OutlinedTextField(
                        value = newBuildingToDelete,
                        onValueChange = { newBuildingToDelete = it },
                        label = { Text("Add Building ID to Delete") },
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = {
                            if (newBuildingToDelete.isNotBlank()) {
                                onConfigChanged(
                                    config.copy(
                                        BuildingsToDelete = config.BuildingsToDelete + newBuildingToDelete
                                    )
                                )
                                newBuildingToDelete = ""
                            }
                        },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text("Add")
                    }
                }

                // Buildings to Add
                Text(
                    "Buildings to Add", style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(top = 16.dp)
                )
                ChipGroup(
                    items = config.BuildingsToAdd.map { it.toString() },
                    onItemRemoved = { buildingIdStr ->
                        buildingIdStr.toIntOrNull()?.let { buildingId ->
                            onConfigChanged(
                                config.copy(
                                    BuildingsToAdd = config.BuildingsToAdd - buildingId
                                )
                            )
                        }
                    }
                )
                var newBuildingToAdd by remember { mutableStateOf("") }
                Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                    OutlinedTextField(
                        value = newBuildingToAdd,
                        onValueChange = { newBuildingToAdd = it },
                        label = { Text("Add Building ID to Add") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        )
                    )
                    Button(
                        onClick = {
                            newBuildingToAdd.toIntOrNull()?.let { buildingId ->
                                if (!config.BuildingsToAdd.contains(buildingId)) {
                                    onConfigChanged(
                                        config.copy(
                                            BuildingsToAdd = config.BuildingsToAdd + buildingId
                                        )
                                    )
                                    newBuildingToAdd = ""
                                }
                            }
                        },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text("Add")
                    }
                }

                // Add Creature Banks Pool checkbox
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Checkbox(
                        checked = config.AddCreatureBanksPool,
                        onCheckedChange = { newValue ->
                            onConfigChanged(config.copy(AddCreatureBanksPool = newValue))
                        }
                    )
                    Text("Add Creature Banks Pool", modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
    } else {
        Button(
            onClick = {
                onConfigChanged(
                    TerrainBuildingsConfig(
                        ClearBuildings = null,
                        BuildingsToDelete = emptyList(),
                        BuildingsToAdd = emptyList(),
                        AddCreatureBanksPool = false
                    )
                )
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Add Configuration")
        }
    }
}

@Composable
fun ZoneRandomizationConfigSection(
    randomization: ZoneRandomizationConfig?,
    onRandomizationChanged: (ZoneRandomizationConfig?) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
        Text("Zone Randomization Configuration", style = MaterialTheme.typography.headlineMedium)

        if (randomization == null) {
            Button(
                onClick = {
                    onRandomizationChanged(
                        ZoneRandomizationConfig(
                            ZonesToSwap = emptyList(),
                            IsSymmetricalSwap = false,
                            ZonesToRandomize = 0
                        )
                    )
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Enable Zone Randomization")
            }
        } else {
            Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Zones to Swap
                    Text("Zones to Swap", style = MaterialTheme.typography.titleMedium)

                    var newZoneToSwap by remember { mutableStateOf("") }
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                        OutlinedTextField(
                            value = newZoneToSwap,
                            onValueChange = { newZoneToSwap = it },
                            label = { Text("Add Zone ID") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number
                            )
                        )
                        Button(
                            onClick = {
                                newZoneToSwap.toIntOrNull()?.let { zoneId ->
                                    if (!randomization.ZonesToSwap.contains(zoneId)) {
                                        onRandomizationChanged(
                                            randomization.copy(
                                                ZonesToSwap = randomization.ZonesToSwap + zoneId
                                            )
                                        )
                                        newZoneToSwap = ""
                                    }
                                }
                            },
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text("Add")
                        }
                    }

                    // Current zones to swap
                    if (randomization.ZonesToSwap.isNotEmpty()) {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            randomization.ZonesToSwap.forEach { zoneId ->
                                InputChip(
                                    onClick = {},
                                    label = { Text(zoneId.toString()) },
                                    trailingIcon = {
                                        IconButton(
                                            onClick = {
                                                onRandomizationChanged(
                                                    randomization.copy(
                                                        ZonesToSwap = randomization.ZonesToSwap - zoneId
                                                    )
                                                )
                                            }
                                        ) {
                                            Icon(Icons.Default.Close, contentDescription = "Remove")
                                        }
                                    },
                                    selected = false,
                                    modifier = Modifier.padding(2.dp)
                                )
                            }
                        }
                    } else {
                        Text(
                            "No zones selected for swapping",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }

                    // Symmetrical swap option
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
                        Checkbox(
                            checked = randomization.IsSymmetricalSwap,
                            onCheckedChange = { newValue ->
                                onRandomizationChanged(randomization.copy(IsSymmetricalSwap = newValue))
                            }
                        )
                        Text("Symmetrical Swap", modifier = Modifier.padding(start = 8.dp))
                    }

                    // Zones to Randomize
                    OutlinedTextField(
                        value = randomization.ZonesToRandomize.toString(),
                        onValueChange = { newValue ->
                            newValue.toLongOrNull()?.let { count ->
                                onRandomizationChanged(randomization.copy(ZonesToRandomize = count))
                            }
                        },
                        label = { Text("Number of Zones to Randomize") },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        )
                    )

                    // Disable button
                    Button(
                        onClick = { onRandomizationChanged(null) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        ),
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                    ) {
                        Text("Disable Randomization")
                    }
                }
            }
        }
    }
}

@Composable
fun GeneralDataConfigSection(
    generalData: GeneralData,
    onGeneralDataChanged: (GeneralData) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
        Text("General Data Configuration", style = MaterialTheme.typography.headlineMedium)

        // Disable Dwelling Icon
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
            Checkbox(
                checked = generalData.DisableDwellingIcon,
                onCheckedChange = { newValue ->
                    onGeneralDataChanged(generalData.copy(DisableDwellingIcon = newValue))
                }
            )
            Text("Disable Dwelling Icon", modifier = Modifier.padding(start = 8.dp))
        }

        // Mine Guard Levels
        Text(
            "Mine Guard Levels",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 16.dp)
        )

        // Level 1 Mines Guard Level
        OutlinedTextField(
            value = generalData.Mine1LevelGuardLevel?.toString() ?: "",
            onValueChange = { newValue ->
                onGeneralDataChanged(
                    generalData.copy(
                        Mine1LevelGuardLevel = newValue.toIntOrNull()
                    )
                )
            },
            label = { Text("Level 1 Mines Guard Level") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        // Level 2 Mines Guard Level
        OutlinedTextField(
            value = generalData.Mine2LevelGuardLevel?.toString() ?: "",
            onValueChange = { newValue ->
                onGeneralDataChanged(
                    generalData.copy(
                        Mine2LevelGuardLevel = newValue.toIntOrNull()
                    )
                )
            },
            label = { Text("Level 2 Mines Guard Level") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        // Gold Mines Guard Level
        OutlinedTextField(
            value = generalData.MineGoldGuardLevel?.toString() ?: "",
            onValueChange = { newValue ->
                onGeneralDataChanged(
                    generalData.copy(
                        MineGoldGuardLevel = newValue.toIntOrNull()
                    )
                )
            },
            label = { Text("Gold Mines Guard Level") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        // Additional parameters can be added here as needed
        /*
        Text("Additional Parameters", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))
        OutlinedTextField(
            value = generalData.AdditionalParameter?.toString() ?: "",
            onValueChange = { newValue ->
                onGeneralDataChanged(generalData.copy(AdditionalParameter = newValue.toIntOrNull()))
            },
            label = { Text("Additional Parameter") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )
        */
    }
}

@Composable
fun CreatureBanksConfigSection(
    pool: CreatureBanksPool,
    onPoolChanged: (CreatureBanksPool) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
        Text("Creature Banks Pool", style = MaterialTheme.typography.headlineMedium)

        // Banks Amount Configuration
        Text("Banks Amount", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))
        IntConfigEditor(
            name = "BanksAmount",
            config = pool.BanksAmount,
            onConfigChanged = { newConfig ->
                onPoolChanged(pool.copy(BanksAmount = newConfig))
            },
            onDelete = {} // Нельзя удалить, это обязательное поле
        )

        // Faction Options
        Text("Allowed Factions", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                Checkbox(
                    checked = pool.NonPlayerFactions,
                    onCheckedChange = { newValue ->
                        onPoolChanged(pool.copy(NonPlayerFactions = newValue))
                    }
                )
                Text("Non-Player Factions", modifier = Modifier.padding(start = 8.dp))
            }

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                Checkbox(
                    checked = pool.PlayerFactions,
                    onCheckedChange = { newValue ->
                        onPoolChanged(pool.copy(PlayerFactions = newValue))
                    }
                )
                Text("Player Factions", modifier = Modifier.padding(start = 8.dp))
            }
        }

        // Пример добавления дополнительных параметров, если они появятся в будущем
        /*
        Text("Additional Parameters", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))
        OutlinedTextField(
            value = pool.AdditionalParameter?.toString() ?: "",
            onValueChange = { newValue ->
                onPoolChanged(pool.copy(AdditionalParameter = newValue.toIntOrNull()))
            },
            label = { Text("Additional Parameter") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )
        */
    }
}

@Composable
fun SidebarNavigation(
    selectedSection: String,
    onSectionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier.fillMaxHeight(), tonalElevation = 4.dp) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text("Configuration", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))

            val sections = listOf(
                "General" to Icons.Default.Settings,
                "Zones" to Icons.Default.Map,
                "Army" to Icons.Default.MilitaryTech,
                "Connections" to Icons.Default.Link,
                "Shops" to Icons.Default.Store,
                "Terrains" to Icons.Default.Terrain,
                "Scripts" to Icons.Default.Code,
                "Bans" to Icons.Default.Block,
                "CreatureBanks" to Icons.Default.AccountTree,
                "GeneralData" to Icons.Default.Info,
                "StartBuildings" to Icons.Default.Home,
                "ZoneRandomization" to Icons.Default.Shuffle
            )

            sections.forEach { (section, icon) ->
                NavigationDrawerItem(
                    icon = { Icon(icon, contentDescription = null) },
                    label = { Text(section) },
                    selected = selectedSection == section,
                    onClick = { onSectionSelected(section) },
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun GeneralConfigSection(
    config: TemplateGenerationConfig,
    onConfigChanged: (TemplateGenerationConfig) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
        Text("Template Configuration", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = config.TemplateName,
            onValueChange = { newName ->
                onConfigChanged(config.copy(TemplateName = newName))
            },
            label = { Text("Template Name") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        OutlinedTextField(
            value = config.BaseArmyMultiplier.toString(),
            onValueChange = { newValue ->
                newValue.toDoubleOrNull()?.let {
                    onConfigChanged(config.copy(BaseArmyMultiplier = it))
                }
            },
            label = { Text("Base Army Multiplier") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )
    }
}

@Composable
fun IntConfigEditor(
    name: String,
    config: IntValueConfig?,
    onConfigChanged: (IntValueConfig) -> Unit,
    onDelete: () -> Unit
) {
    if (config != null) {
        Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Column(modifier = Modifier.padding(8.dp)) {
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text(name, style = MaterialTheme.typography.titleSmall)
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = config.MinValue.toString(),
                        onValueChange = { newValue ->
                            newValue.toIntOrNull()?.let { min ->
                                onConfigChanged(config.copy(MinValue = min))
                            }
                        },
                        label = { Text("Min Value") },
                        modifier = Modifier.weight(1f).padding(end = 4.dp)
                    )

                    OutlinedTextField(
                        value = config.MaxValue.toString(),
                        onValueChange = { newValue ->
                            newValue.toIntOrNull()?.let { max ->
                                onConfigChanged(config.copy(MaxValue = max))
                            }
                        },
                        label = { Text("Max Value") },
                        modifier = Modifier.weight(1f).padding(start = 4.dp)
                    )
                }
            }
        }
    } else {
        Button(
            onClick = {
                onConfigChanged(IntValueConfig(1, 3))
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Add $name Config")
        }
    }
}

@Composable
fun DwellingConfigEditor(
    config: DwellingGenerationConfig,
    onConfigChanged: (DwellingGenerationConfig) -> Unit,
    onDelete: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("Dwelling Generation Config", style = MaterialTheme.typography.titleSmall)
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = config.MinCount.toString(),
                    onValueChange = { newValue ->
                        newValue.toIntOrNull()?.let { min ->
                            onConfigChanged(config.copy(MinCount = min))
                        }
                    },
                    label = { Text("Min Count") },
                    modifier = Modifier.weight(1f).padding(end = 4.dp)
                )

                OutlinedTextField(
                    value = config.MaxCount.toString(),
                    onValueChange = { newValue ->
                        newValue.toIntOrNull()?.let { max ->
                            onConfigChanged(config.copy(MaxCount = max))
                        }
                    },
                    label = { Text("Max Count") },
                    modifier = Modifier.weight(1f).padding(start = 4.dp)
                )
            }

            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                OutlinedTextField(
                    value = config.MinTiersCount.toString(),
                    onValueChange = { newValue ->
                        newValue.toIntOrNull()?.let { min ->
                            onConfigChanged(config.copy(MinTiersCount = min))
                        }
                    },
                    label = { Text("Min Tiers Count") },
                    modifier = Modifier.weight(1f).padding(end = 4.dp)
                )

                OutlinedTextField(
                    value = config.MaxTiersCount.toString(),
                    onValueChange = { newValue ->
                        newValue.toIntOrNull()?.let { max ->
                            onConfigChanged(config.copy(MaxTiersCount = max))
                        }
                    },
                    label = { Text("Max Tiers Count") },
                    modifier = Modifier.weight(1f).padding(start = 4.dp)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                Checkbox(
                    checked = config.UniformDistribution,
                    onCheckedChange = { newValue ->
                        onConfigChanged(config.copy(UniformDistribution = newValue))
                    }
                )
                Text("Uniform Distribution", modifier = Modifier.padding(start = 8.dp))
            }

            Text("Allowed Tiers", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(top = 8.dp))
            ChipGroup(
                items = (1..7).toList(),
                selectedItems = config.AllowedTiers,
                onItemSelected = { tier, selected ->
                    val newTiers = if (selected) {
                        config.AllowedTiers + tier
                    } else {
                        config.AllowedTiers - tier
                    }.distinct().sorted()
                    onConfigChanged(config.copy(AllowedTiers = newTiers))
                },
                itemContent = { tier -> Text("Tier $tier") }
            )
        }
    }
}

@Composable
fun MineConfigEditor(
    config: MineGenerationConfig,
    onConfigChanged: (MineGenerationConfig) -> Unit,
    onDelete: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("Mine Generation Config", style = MaterialTheme.typography.titleSmall)
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }

            val mineTypes = listOf(
                "Wood" to config.Wood,
                "Ore" to config.Ore,
                "Mercury" to config.Mercury,
                "Crystals" to config.Crystals,
                "Sulfur" to config.Sulfur,
                "Gems" to config.Gems,
                "Gold" to config.Gold
            )

            mineTypes.forEach { (type, count) ->
                OutlinedTextField(
                    value = count.toString(),
                    onValueChange = { newValue ->
                        newValue.toIntOrNull()?.let { newCount ->
                            val newConfig = when (type) {
                                "Wood" -> config.copy(Wood = newCount)
                                "Ore" -> config.copy(Ore = newCount)
                                "Mercury" -> config.copy(Mercury = newCount)
                                "Crystals" -> config.copy(Crystals = newCount)
                                "Sulfur" -> config.copy(Sulfur = newCount)
                                "Gems" -> config.copy(Gems = newCount)
                                "Gold" -> config.copy(Gold = newCount)
                                else -> config
                            }
                            onConfigChanged(newConfig)
                        }
                    },
                    label = { Text(type) },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun CreaturesConfigEditor(
    config: CreaturesConfiguration,
    onConfigChanged: (CreaturesConfiguration) -> Unit,
    onDelete: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("Creatures Configuration", style = MaterialTheme.typography.titleSmall)
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }

            IntConfigEditor(
                name = "ReplacementsCount",
                config = config.ReplacementsCount,
                onConfigChanged = { newConfig ->
                    onConfigChanged(config.copy(ReplacementsCount = newConfig))
                },
                onDelete = {}
            )

            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text("Options", style = MaterialTheme.typography.titleSmall)

                val options = listOf(
                    "TerrainFaction" to config.TerrainFaction,
                    "NonPlayersFactions" to config.NonPlayersFactions,
                    "NoGrades" to config.NoGrades,
                    "Grades" to config.Grades,
                    "Neutrals" to config.Neutrals,
                    "NonUniqueReplacements" to config.NonUniqueReplacements
                )

                options.forEach { (name, value) ->
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                        Checkbox(
                            checked = value,
                            onCheckedChange = { newValue ->
                                val newConfig = when (name) {
                                    "TerrainFaction" -> config.copy(TerrainFaction = newValue)
                                    "NonPlayersFactions" -> config.copy(NonPlayersFactions = newValue)
                                    "NoGrades" -> config.copy(NoGrades = newValue)
                                    "Grades" -> config.copy(Grades = newValue)
                                    "Neutrals" -> config.copy(Neutrals = newValue)
                                    "NonUniqueReplacements" -> config.copy(NonUniqueReplacements = newValue)
                                    else -> config
                                }
                                onConfigChanged(newConfig)
                            }
                        )
                        Text(name.replace(Regex("([A-Z])"), " $1").trim(), modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }

            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text("Multipliers", style = MaterialTheme.typography.titleSmall)

                val multipliers = listOf(
                    "BaseCostMultiplier" to config.BaseCostMultiplier,
                    "BaseResourcesMultiplier" to config.BaseResourcesMultiplier,
                    "BaseGrowMultiplier" to config.BaseGrowMultiplier
                )

                multipliers.forEach { (name, value) ->
                    OutlinedTextField(
                        value = value?.toString() ?: "",
                        onValueChange = { newValue ->
                            newValue.toDoubleOrNull()?.let { newMultiplier ->
                                val newConfig = when (name) {
                                    "BaseCostMultiplier" -> config.copy(BaseCostMultiplier = newMultiplier)
                                    "BaseResourcesMultiplier" -> config.copy(BaseResourcesMultiplier = newMultiplier)
                                    "BaseGrowMultiplier" -> config.copy(BaseGrowMultiplier = newMultiplier)
                                    else -> config
                                }
                                onConfigChanged(newConfig)
                            }
                        },
                        label = { Text(name.replace(Regex("([A-Z])"), " $1").trim()) },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    )
                }
            }

            Text(
                "Creature Modifiers",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(top = 8.dp)
            )
            CreatureModifiersList(
                modifiers = config.CreatureModifiers,
                onModifiersChanged = { newModifiers ->
                    onConfigChanged(config.copy(CreatureModifiers = newModifiers))
                }
            )

            Text(
                "Creature Tier Replacements",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(top = 8.dp)
            )
            CreatureTierReplacementsList(
                replacements = config.CreatureTierReplacements,
                onReplacementsChanged = { newReplacements ->
                    onConfigChanged(config.copy(CreatureTierReplacements = newReplacements))
                }
            )
        }
    }
}

@Composable
fun CreatureModifiersList(
    modifiers: List<CreatureModifier>,
    onModifiersChanged: (List<CreatureModifier>) -> Unit
) {
    Column {
        modifiers.forEachIndexed { index, modifier ->
            Card(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text("Modifier ${index + 1}", style = MaterialTheme.typography.titleSmall)
                        IconButton(onClick = {
                            onModifiersChanged(modifiers.toMutableList().apply { removeAt(index) })
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }

                    OutlinedTextField(
                        value = modifier.Tier.toString(),
                        onValueChange = { newValue ->
                            newValue.toIntOrNull()?.let { tier ->
                                val newModifiers = modifiers.toMutableList().apply {
                                    set(index, modifier.copy(Tier = tier))
                                }
                                onModifiersChanged(newModifiers)
                            }
                        },
                        label = { Text("Tier") },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    )

                    OutlinedTextField(
                        value = modifier.CostMultiplier?.toString() ?: "",
                        onValueChange = { newValue ->
                            newValue.toDoubleOrNull()?.let { multiplier ->
                                val newModifiers = modifiers.toMutableList().apply {
                                    set(index, modifier.copy(CostMultiplier = multiplier))
                                }
                                onModifiersChanged(newModifiers)
                            }
                        },
                        label = { Text("Cost Multiplier") },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    )

                    OutlinedTextField(
                        value = modifier.ResourcesMultiplier?.toString() ?: "",
                        onValueChange = { newValue ->
                            newValue.toDoubleOrNull()?.let { multiplier ->
                                val newModifiers = modifiers.toMutableList().apply {
                                    set(index, modifier.copy(ResourcesMultiplier = multiplier))
                                }
                                onModifiersChanged(newModifiers)
                            }
                        },
                        label = { Text("Resources Multiplier") },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    )

                    OutlinedTextField(
                        value = modifier.GrowMultiplier.toString(),
                        onValueChange = { newValue ->
                            newValue.toDoubleOrNull()?.let { multiplier ->
                                val newModifiers = modifiers.toMutableList().apply {
                                    set(index, modifier.copy(GrowMultiplier = multiplier))
                                }
                                onModifiersChanged(newModifiers)
                            }
                        },
                        label = { Text("Grow Multiplier") },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    )
                }
            }
        }

        Button(
            onClick = {
                onModifiersChanged(
                    modifiers + CreatureModifier(
                        Tier = 1,
                        CostMultiplier = 1.0,
                        ResourcesMultiplier = 1.0,
                        GrowMultiplier = 1.0
                    )
                )
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Add Modifier")
        }
    }
}

@Composable
fun CreatureTierReplacementsList(
    replacements: List<CreatureTierReplacement>,
    onReplacementsChanged: (List<CreatureTierReplacement>) -> Unit
) {
    Column {
        replacements.forEachIndexed { index, replacement ->
            Card(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text("Replacement ${index + 1}", style = MaterialTheme.typography.titleSmall)
                        IconButton(onClick = {
                            onReplacementsChanged(replacements.toMutableList().apply { removeAt(index) })
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }

                    OutlinedTextField(
                        value = replacement.Tier.toString(),
                        onValueChange = { newValue ->
                            newValue.toIntOrNull()?.let { tier ->
                                val newReplacements = replacements.toMutableList().apply {
                                    set(index, replacement.copy(Tier = tier))
                                }
                                onReplacementsChanged(newReplacements)
                            }
                        },
                        label = { Text("Tier") },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    )

                    Text(
                        "Creature IDs",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    ChipGroup(
                        items = replacement.CreatureIds,
                        onItemRemoved = { creatureId ->
                            val newIds = replacement.CreatureIds.toMutableList().apply { remove(creatureId) }
                            val newReplacements = replacements.toMutableList().apply {
                                set(index, replacement.copy(CreatureIds = newIds))
                            }
                            onReplacementsChanged(newReplacements)
                        }
                    )

                    var newCreatureId by remember { mutableStateOf("") }
                    Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                        OutlinedTextField(
                            value = newCreatureId,
                            onValueChange = { newCreatureId = it },
                            label = { Text("New Creature ID") },
                            modifier = Modifier.weight(1f)
                        )
                        Button(
                            onClick = {
                                if (newCreatureId.isNotBlank()) {
                                    val newIds = replacement.CreatureIds + newCreatureId
                                    val newReplacements = replacements.toMutableList().apply {
                                        set(index, replacement.copy(CreatureIds = newIds))
                                    }
                                    onReplacementsChanged(newReplacements)
                                    newCreatureId = ""
                                }
                            },
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text("Add")
                        }
                    }
                }
            }
        }

        Button(
            onClick = {
                onReplacementsChanged(
                    replacements + CreatureTierReplacement(
                        CreatureIds = emptyList(),
                        Tier = 1
                    )
                )
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Add Replacement")
        }
    }
}

@Composable
fun ChipGroup(
    items: List<String>,
    onItemRemoved: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        items.forEach { item ->
            InputChip(
                onClick = {},
                label = { Text(item) },
                trailingIcon = {
                    IconButton(onClick = { onItemRemoved(item) }) {
                        Icon(Icons.Default.Close, contentDescription = "Remove")
                    }
                },
                selected = false,
                modifier = Modifier.padding(2.dp)
            )
        }
    }
}

@Composable
fun <T> ChipGroup(
    items: List<T>,
    selectedItems: List<T>,
    onItemSelected: (T, Boolean) -> Unit,
    itemContent: @Composable (T) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        items.forEach { item ->
            FilterChip(
                selected = selectedItems.contains(item),
                onClick = { onItemSelected(item, !selectedItems.contains(item)) },
                label = { itemContent(item) },
                modifier = Modifier.padding(2.dp)
            )
        }
    }
}

// Similar implementations for other sections (ArmyConfigSection, ConnectionsConfigSection, etc.)
// Each would follow the same pattern as the ZonesConfigSection with appropriate editors for their data structures

@Composable
fun ArmyConfigSection(
    baseMultiplier: Double,
    multipliers: ArmyMultipliers,
    onBaseMultiplierChanged: (Double) -> Unit,
    onMultipliersChanged: (ArmyMultipliers) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
        Text("Army Multipliers", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = baseMultiplier.toString(),
            onValueChange = { newValue ->
                newValue.toDoubleOrNull()?.let { onBaseMultiplierChanged(it) }
            },
            label = { Text("Base Army Multiplier") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        val factions = listOf(
            "Dwarfs" to multipliers.Dwarfs,
            "Elves" to multipliers.Elves,
            "Horde" to multipliers.Horde,
            "Humans" to multipliers.Humans,
            "Inferno" to multipliers.Inferno,
            "Liga" to multipliers.Liga,
            "Mages" to multipliers.Mages,
            "Necropolis" to multipliers.Necropolis
        )

        factions.forEach { (faction, multiplier) ->
            OutlinedTextField(
                value = multiplier.toString(),
                onValueChange = { newValue ->
                    newValue.toDoubleOrNull()?.let { newMultiplier ->
                        val newMultipliers = when (faction) {
                            "Dwarfs" -> multipliers.copy(Dwarfs = newMultiplier)
                            "Elves" -> multipliers.copy(Elves = newMultiplier)
                            "Horde" -> multipliers.copy(Horde = newMultiplier)
                            "Humans" -> multipliers.copy(Humans = newMultiplier)
                            "Inferno" -> multipliers.copy(Inferno = newMultiplier)
                            "Liga" -> multipliers.copy(Liga = newMultiplier)
                            "Mages" -> multipliers.copy(Mages = newMultiplier)
                            "Necropolis" -> multipliers.copy(Necropolis = newMultiplier)
                            else -> multipliers
                        }
                        onMultipliersChanged(newMultipliers)
                    }
                },
                label = { Text("$faction Multiplier") },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )
        }
    }
}

@Composable
fun CreatureBuildingConfigEditor(
    config: CreatureBuildingConfig,
    onConfigChanged: (CreatureBuildingConfig) -> Unit,
    onDelete: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("Creature Building Config", style = MaterialTheme.typography.titleSmall)
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }

            Text("Tiers Pool", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(top = 8.dp))
            ChipGroup(
                items = (1..7).toList(),
                selectedItems = config.TiersPool,
                onItemSelected = { tier, selected ->
                    val newTiers = if (selected) {
                        config.TiersPool + tier
                    } else {
                        config.TiersPool - tier
                    }.distinct().sorted()
                    onConfigChanged(config.copy(TiersPool = newTiers))
                },
                itemContent = { tier -> Text("Tier $tier") }
            )

            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text("Options", style = MaterialTheme.typography.titleSmall)

                val options = listOf(
                    "NoGrades" to config.NoGrades,
                    "Grades" to config.Grades,
                    "Neutrals" to config.Neutrals
                )

                options.forEach { (name, value) ->
                    value?.let {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = it,
                                onCheckedChange = { newValue ->
                                    val newConfig = when (name) {
                                        "NoGrades" -> config.copy(NoGrades = newValue)
                                        "Grades" -> config.copy(Grades = newValue)
                                        "Neutrals" -> config.copy(Neutrals = newValue)
                                        else -> config
                                    }
                                    onConfigChanged(newConfig)
                                }
                            )
                            Text(
                                name.replace(Regex("([A-Z])"), " $1").trim(),
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }

            Text("Creature IDs", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(top = 8.dp))
            ChipGroup(
                items = config.CreatureIds,
                onItemRemoved = { creatureId ->
                    val newIds = config.CreatureIds.toMutableList().apply { remove(creatureId) }
                    onConfigChanged(config.copy(CreatureIds = newIds))
                }
            )

            var newCreatureId by remember { mutableStateOf("") }
            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                OutlinedTextField(
                    value = newCreatureId,
                    onValueChange = { newCreatureId = it },
                    label = { Text("New Creature ID") },
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {
                        if (newCreatureId.isNotBlank()) {
                            val newIds = config.CreatureIds + newCreatureId
                            onConfigChanged(config.copy(CreatureIds = newIds))
                            newCreatureId = ""
                        }
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Add")
                }
            }

            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text("Multipliers", style = MaterialTheme.typography.titleSmall)

                val multipliers = listOf(
                    "CostMultiplier" to config.CostMultiplier,
                    "ResourcesMultiplier" to config.ResourcesMultiplier,
                    "GrowMultiplier" to config.GrowMultiplier
                )

                multipliers.forEach { (name, value) ->
                    value?.let {
                        OutlinedTextField(
                            value = it.toString(),
                            onValueChange = { newValue ->
                                newValue.toDoubleOrNull()?.let { multiplier ->
                                    val newConfig = when (name) {
                                        "CostMultiplier" -> config.copy(CostMultiplier = multiplier)
                                        "ResourcesMultiplier" -> config.copy(ResourcesMultiplier = multiplier)
                                        "GrowMultiplier" -> config.copy(GrowMultiplier = multiplier)
                                        else -> config
                                    }
                                    onConfigChanged(newConfig)
                                }
                            },
                            label = { Text(name.replace(Regex("([A-Z])"), " $1").trim()) },
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CreatureBankConfigEditor(
    config: CreatureBankConfig,
    onConfigChanged: (CreatureBankConfig) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text("Creature Bank Config", style = MaterialTheme.typography.titleSmall)

            OutlinedTextField(
                value = config.Name,
                onValueChange = { newValue ->
                    onConfigChanged(config.copy(Name = newValue))
                },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            )

            Text("Creatures Pool", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(top = 8.dp))
            ChipGroup(
                items = config.CreaturesPool,
                onItemRemoved = { creatureId ->
                    val newPool = config.CreaturesPool.toMutableList().apply { remove(creatureId) }
                    onConfigChanged(config.copy(CreaturesPool = newPool))
                }
            )

            var newCreatureId by remember { mutableStateOf("") }
            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                OutlinedTextField(
                    value = newCreatureId,
                    onValueChange = { newCreatureId = it },
                    label = { Text("New Creature ID") },
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {
                        if (newCreatureId.isNotBlank()) {
                            val newPool = config.CreaturesPool + newCreatureId
                            onConfigChanged(config.copy(CreaturesPool = newPool))
                            newCreatureId = ""
                        }
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Add")
                }
            }

            Text("Guards Pool", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(top = 8.dp))
            config.GuardsPool.forEachIndexed { index, guardsPool ->
                Card(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("Guards Pool ${index + 1}", style = MaterialTheme.typography.titleSmall)
                            IconButton(onClick = {
                                val newPools = config.GuardsPool.toMutableList().apply { removeAt(index) }
                                onConfigChanged(config.copy(GuardsPool = newPools))
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete")
                            }
                        }

                        ChipGroup(
                            items = guardsPool.Values,
                            onItemRemoved = { creatureId ->
                                val newValues = guardsPool.Values.toMutableList().apply { remove(creatureId) }
                                val newPools = config.GuardsPool.toMutableList().apply {
                                    set(index, guardsPool.copy(Values = newValues))
                                }
                                onConfigChanged(config.copy(GuardsPool = newPools))
                            }
                        )

                        var newGuardId by remember { mutableStateOf("") }
                        Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                            OutlinedTextField(
                                value = newGuardId,
                                onValueChange = { newGuardId = it },
                                label = { Text("New Guard ID") },
                                modifier = Modifier.weight(1f)
                            )
                            Button(
                                onClick = {
                                    if (newGuardId.isNotBlank()) {
                                        val newValues = guardsPool.Values + newGuardId
                                        val newPools = config.GuardsPool.toMutableList().apply {
                                            set(index, guardsPool.copy(Values = newValues))
                                        }
                                        onConfigChanged(config.copy(GuardsPool = newPools))
                                        newGuardId = ""
                                    }
                                },
                                modifier = Modifier.padding(start = 8.dp)
                            ) {
                                Text("Add")
                            }
                        }
                    }
                }
            }

            Button(
                onClick = {
                    onConfigChanged(config.copy(GuardsPool = config.GuardsPool + GuardsPool(emptyList())))
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Add Guards Pool")
            }

            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text("Multipliers", style = MaterialTheme.typography.titleSmall)

                val multipliers = listOf(
                    "CreatureCostMultiplier" to config.CreatureCostMultiplier,
                    "CreatureGrowMultiplier" to config.CreatureGrowMultiplier,
                    "CreatureResourcesMultiplier" to config.CreatureResourcesMultiplier,
                    "GuardGrowMultiplier" to config.GuardGrowMultiplier
                )

                multipliers.forEach { (name, value) ->
                    OutlinedTextField(
                        value = value.toString(),
                        onValueChange = { newValue ->
                            newValue.toDoubleOrNull()?.let { multiplier ->
                                val newConfig = when (name) {
                                    "CreatureCostMultiplier" -> config.copy(CreatureCostMultiplier = multiplier)
                                    "CreatureGrowMultiplier" -> config.copy(CreatureGrowMultiplier = multiplier)
                                    "CreatureResourcesMultiplier" -> config.copy(CreatureResourcesMultiplier = multiplier)
                                    "GuardGrowMultiplier" -> config.copy(GuardGrowMultiplier = multiplier)
                                    else -> config
                                }
                                onConfigChanged(newConfig)
                            }
                        },
                        label = { Text(name.replace(Regex("([A-Z])"), " $1").trim()) },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}
