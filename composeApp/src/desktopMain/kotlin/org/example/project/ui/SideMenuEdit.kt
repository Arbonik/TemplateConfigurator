package org.example.project.ui


import TerrainBuildingsConfig
import TerrainConfig
import TerrainType
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.example.project.ui.common.EnumDropdown

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
            HorizontalDivider()

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

            if (terrains.size < TerrainType.entries.size)
                Button(
                    onClick = {
                        val nextTerrainType = TerrainType.entries.first { type ->
                            terrains.none { it.TerrainType == type }
                        }
                        val newTerrain = TerrainConfig(
                            nextTerrainType,
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


        var mirrorCheck by remember(terrain) { mutableStateOf(terrain.MirrorTerrainType != null) }

        Row(
            verticalAlignment = CenterVertically,
        ) {
            Text("Mirror enable")
            Checkbox(
                checked = mirrorCheck,
                onCheckedChange = {
                    mirrorCheck = it
                },
            )
            if (mirrorCheck) {
                EnumDropdown(
                    value = (terrain.MirrorTerrainType ?: TerrainType.SecondPlayer).name,
                    label = "Mirror Terrain Type ID",
                    values = TerrainType.entries.map { it.name },
                    onBuildingChanged = { building ->
                        onTerrainChanged(
                            terrain.copy(
                                MirrorTerrainType = TerrainType.valueOf(building)
                            )
                        )
                    }
                )
            }
        }
        if (mirrorCheck.not()) {
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
                },
                title = { it }
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
                items = terrain.BuildingsToAdd,
                onItemRemoved = { buildingIdStr ->
                    buildingIdStr?.let { buildingId ->
                        onTerrainChanged(
                            terrain.copy(
                                BuildingsToAdd = terrain.BuildingsToAdd - buildingId
                            )
                        )
                    }
                },
                title = { it.toString() }
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
                if (config.ClearBuildings != true) {
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
                        },
                        title = { it }
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
                        },
                        title = { it }
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
                            checked = config.AddCreatureBanksPool == true,
                            onCheckedChange = { newValue ->
                                onConfigChanged(config.copy(AddCreatureBanksPool = newValue))
                            }
                        )
                        Text("Add Creature Banks Pool", modifier = Modifier.padding(start = 8.dp))
                    }
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

//
//@Composable
//fun ZoneRandomizationConfigSection(
//    randomization: ZoneRandomizationConfig?,
//    onRandomizationChanged: (ZoneRandomizationConfig?) -> Unit
//) {
//    Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
//        Text("Zone Randomization Configuration", style = MaterialTheme.typography.headlineMedium)
//
//        if (randomization == null) {
//            Button(
//                onClick = {
//                    onRandomizationChanged(
//                        ZoneRandomizationConfig(
//                            ZonesToSwap = emptyList(),
//                            IsSymmetricalSwap = false,
//                            ZonesToRandomize = 0
//                        )
//                    )
//                },
//                modifier = Modifier.padding(16.dp)
//            ) {
//                Text("Enable Zone Randomization")
//            }
//        } else {
//            Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
//                Column(modifier = Modifier.padding(16.dp)) {
//                    // Zones to Swap
//                    Text("Zones to Swap", style = MaterialTheme.typography.titleMedium)
//
//                    var newZoneToSwap by remember { mutableStateOf("") }
//                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
//                        OutlinedTextField(
//                            value = newZoneToSwap,
//                            onValueChange = { newZoneToSwap = it },
//                            label = { Text("Add Zone ID") },
//                            modifier = Modifier.weight(1f),
//                            keyboardOptions = KeyboardOptions.Default.copy(
//                                keyboardType = KeyboardType.Number
//                            )
//                        )
//                        Button(
//                            onClick = {
//                                newZoneToSwap.toIntOrNull()?.let { zoneId ->
//                                    if (!randomization.ZonesToSwap.contains(zoneId)) {
//                                        onRandomizationChanged(
//                                            randomization.copy(
//                                                ZonesToSwap = randomization.ZonesToSwap + zoneId
//                                            )
//                                        )
//                                        newZoneToSwap = ""
//                                    }
//                                }
//                            },
//                            modifier = Modifier.padding(start = 8.dp)
//                        ) {
//                            Text("Add")
//                        }
//                    }
//
//                    // Current zones to swap
//                    if (randomization.ZonesToSwap.isNotEmpty()) {
//                        FlowRow(
//                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
//                            horizontalArrangement = Arrangement.spacedBy(4.dp)
//                        ) {
//                            randomization.ZonesToSwap.forEach { zoneId ->
//                                InputChip(
//                                    onClick = {},
//                                    label = { Text(zoneId.toString()) },
//                                    trailingIcon = {
//                                        IconButton(
//                                            onClick = {
//                                                onRandomizationChanged(
//                                                    randomization.copy(
//                                                        ZonesToSwap = randomization.ZonesToSwap - zoneId
//                                                    )
//                                                )
//                                            }
//                                        ) {
//                                            Icon(Icons.Default.Close, contentDescription = "Remove")
//                                        }
//                                    },
//                                    selected = false,
//                                    modifier = Modifier.padding(2.dp)
//                                )
//                            }
//                        }
//                    } else {
//                        Text(
//                            "No zones selected for swapping",
//                            style = MaterialTheme.typography.bodySmall,
//                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
//                        )
//                    }
//
//                    // Symmetrical swap option
//                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
//                        Checkbox(
//                            checked = randomization.IsSymmetricalSwap,
//                            onCheckedChange = { newValue ->
//                                onRandomizationChanged(randomization.copy(IsSymmetricalSwap = newValue))
//                            }
//                        )
//                        Text("Symmetrical Swap", modifier = Modifier.padding(start = 8.dp))
//                    }
//
//                    // Zones to Randomize
//                    OutlinedTextField(
//                        value = randomization.ZonesToRandomize.toString(),
//                        onValueChange = { newValue ->
//                            newValue.toLongOrNull()?.let { count ->
//                                onRandomizationChanged(randomization.copy(ZonesToRandomize = count))
//                            }
//                        },
//                        label = { Text("Number of Zones to Randomize") },
//                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
//                        keyboardOptions = KeyboardOptions.Default.copy(
//                            keyboardType = KeyboardType.Number
//                        )
//                    )
//
//                    // Disable button
//                    Button(
//                        onClick = { onRandomizationChanged(null) },
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = MaterialTheme.colorScheme.errorContainer
//                        ),
//                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
//                    ) {
//                        Text("Disable Randomization")
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun GeneralDataConfigSection(
//    generalData: GeneralData,
//    onGeneralDataChanged: (GeneralData) -> Unit
//) {
//    Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
//        Text("General Data Configuration", style = MaterialTheme.typography.headlineMedium)
//
//        // Disable Dwelling Icon
//        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
//            Checkbox(
//                checked = generalData.DisableDwellingIcon,
//                onCheckedChange = { newValue ->
//                    onGeneralDataChanged(generalData.copy(DisableDwellingIcon = newValue))
//                }
//            )
//            Text("Disable Dwelling Icon", modifier = Modifier.padding(start = 8.dp))
//        }
//
//        // Mine Guard Levels
//        Text(
//            "Mine Guard Levels",
//            style = MaterialTheme.typography.titleMedium,
//            modifier = Modifier.padding(top = 16.dp)
//        )
//
//        // Level 1 Mines Guard Level
//        OutlinedTextField(
//            value = generalData.Mine1LevelGuardLevel?.toString() ?: "",
//            onValueChange = { newValue ->
//                onGeneralDataChanged(
//                    generalData.copy(
//                        Mine1LevelGuardLevel = newValue.toIntOrNull()
//                    )
//                )
//            },
//            label = { Text("Level 1 Mines Guard Level") },
//            modifier = Modifier.fillMaxWidth().padding(8.dp)
//        )
//
//        // Level 2 Mines Guard Level
//        OutlinedTextField(
//            value = generalData.Mine2LevelGuardLevel?.toString() ?: "",
//            onValueChange = { newValue ->
//                onGeneralDataChanged(
//                    generalData.copy(
//                        Mine2LevelGuardLevel = newValue.toIntOrNull()
//                    )
//                )
//            },
//            label = { Text("Level 2 Mines Guard Level") },
//            modifier = Modifier.fillMaxWidth().padding(8.dp)
//        )
//
//        // Gold Mines Guard Level
//        OutlinedTextField(
//            value = generalData.MineGoldGuardLevel?.toString() ?: "",
//            onValueChange = { newValue ->
//                onGeneralDataChanged(
//                    generalData.copy(
//                        MineGoldGuardLevel = newValue.toIntOrNull()
//                    )
//                )
//            },
//            label = { Text("Gold Mines Guard Level") },
//            modifier = Modifier.fillMaxWidth().padding(8.dp)
//        )
//    }
//}
//
//@Composable
//fun CreatureBanksConfigSection(
//    pool: CreatureBanksPool,
//    onPoolChanged: (CreatureBanksPool) -> Unit
//) {
//    Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
//        Text("Creature Banks Pool", style = MaterialTheme.typography.headlineMedium)
//
//        // Banks Amount Configuration
//        Text("Banks Amount", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))
//        IntConfigEditor(
//            name = "BanksAmount",
//            config = pool.BanksAmount,
//            onConfigChanged = { newConfig ->
//                onPoolChanged(pool.copy(BanksAmount = newConfig))
//            },
//            onDelete = {} // Нельзя удалить, это обязательное поле
//        )
//
//        // Faction Options
//        Text("Allowed Factions", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))
//        Column(modifier = Modifier.padding(vertical = 8.dp)) {
//            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
//                Checkbox(
//                    checked = pool.NonPlayerFactions,
//                    onCheckedChange = { newValue ->
//                        onPoolChanged(pool.copy(NonPlayerFactions = newValue))
//                    }
//                )
//                Text("Non-Player Factions", modifier = Modifier.padding(start = 8.dp))
//            }
//
//            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
//                Checkbox(
//                    checked = pool.PlayerFactions,
//                    onCheckedChange = { newValue ->
//                        onPoolChanged(pool.copy(PlayerFactions = newValue))
//                    }
//                )
//                Text("Player Factions", modifier = Modifier.padding(start = 8.dp))
//            }
//        }
//    }
//}
//
//@Composable
//fun SidebarNavigation(
//    selectedSection: String,
//    onSectionSelected: (String) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Surface(modifier = modifier.fillMaxHeight(), tonalElevation = 4.dp) {
//        Column(modifier = Modifier.padding(8.dp).verticalScroll(rememberScrollState())) {
//            Text("Configuration", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))
//
//            val sections = listOf(
//                "Settings" to Icons.Default.Save,
//                "General" to Icons.Default.Settings,
//                "Zones" to Icons.Default.Map,
//                "Army" to Icons.Default.MilitaryTech,
//                "Connections" to Icons.Default.Link,
//                "Shops" to Icons.Default.Store,
//                "Terrains" to Icons.Default.Terrain,
//                "Scripts" to Icons.Default.Code,
//                "Bans" to Icons.Default.Block,
//                "CreatureBanks" to Icons.Default.AccountTree,
//                "GeneralData" to Icons.Default.Info,
//                "StartBuildings" to Icons.Default.Home,
//                "ZoneRandomization" to Icons.Default.Shuffle
//            )
//
//            sections.forEach { (section, icon) ->
//                NavigationDrawerItem(
//                    icon = { Icon(icon, contentDescription = null) },
//                    label = { Text(section) },
//                    selected = selectedSection == section,
//                    onClick = { onSectionSelected(section) },
//                    modifier = Modifier.padding(vertical = 4.dp)
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun GeneralConfigSection(
//    config: TemplateGenerationConfig,
//    onConfigChanged: (TemplateGenerationConfig) -> Unit
//) {
//    Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
//        Text("Template Configuration", style = MaterialTheme.typography.headlineMedium)
//
//        OutlinedTextField(
//            value = config.TemplateName,
//            onValueChange = { newName ->
//                onConfigChanged(config.copy(TemplateName = newName))
//            },
//            label = { Text("Template Name") },
//            modifier = Modifier.fillMaxWidth().padding(8.dp)
//        )
//
//        DecimalInputField(
//            value = config.BaseArmyMultiplier.toString(),
//            title = "Base Army Multiplier",
//            onValueChange = { newValue ->
//                onConfigChanged(config.copy(BaseArmyMultiplier = newValue.toDoubleOrNull()))
//            }
//        )
//    }
//}
//
//@Composable
//fun IntConfigEditor(
//    name: String,
//    config: IntValueConfig?,
//    onConfigChanged: (IntValueConfig) -> Unit,
//    onDelete: () -> Unit
//) {
//    if (config != null) {
//        Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
//            Column(modifier = Modifier.padding(8.dp)) {
//                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
//                    Text(name, style = MaterialTheme.typography.titleSmall)
//                    IconButton(onClick = onDelete) {
//                        Icon(Icons.Default.Delete, contentDescription = "Delete")
//                    }
//                }
//
//                Row(modifier = Modifier.fillMaxWidth()) {
//                    DecimalInputField(
//                        value = config.MinValue.toString(),
//                        title = "Min Value",
//                        onValueChange = { newValue ->
//                            newValue.toIntOrNull()?.let { min ->
//                                onConfigChanged(config.copy(MinValue = min))
//                            }
//                        },
//                        modifier = Modifier.weight(1f).padding(end = 4.dp)
//                    )
//
//                    DecimalInputField(
//                        value = config.MaxValue.toString(),
//                        title = "Max Value",
//                        onValueChange = { newValue ->
//                            newValue.toIntOrNull()?.let { max ->
//                                onConfigChanged(config.copy(MaxValue = max))
//                            }
//                        },
//                        modifier = Modifier.weight(1f).padding(start = 4.dp)
//                    )
//                }
//            }
//        }
//    } else {
//        Button(
//            onClick = {
//                onConfigChanged(IntValueConfig(1, 3))
//            },
//            modifier = Modifier.padding(8.dp)
//        ) {
//            Text("Add $name Config")
//        }
//    }
//}
//
//@Composable
//fun DwellingConfigEditor(
//    config: DwellingGenerationConfig,
//    onConfigChanged: (DwellingGenerationConfig) -> Unit,
//    onDelete: () -> Unit
//) {
//    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
//        Column(modifier = Modifier.padding(8.dp)) {
//            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
//                Text("Dwelling Generation Config", style = MaterialTheme.typography.titleSmall)
//                IconButton(onClick = onDelete) {
//                    Icon(Icons.Default.Delete, contentDescription = "Delete")
//                }
//            }
//            Row(modifier = Modifier.fillMaxWidth()) {
//                DecimalInputField(
//                    value = config.MinCount.toString(),
//                    title = "Min Count",
//                    onValueChange = { newValue ->
//                        newValue.toIntOrNull()?.let { min ->
//                            onConfigChanged(config.copy(MinCount = min))
//                        }
//                    },
//                    modifier = Modifier.weight(1f).padding(end = 4.dp)
//                )
//
//                DecimalInputField(
//                    value = config.MaxCount.toString(),
//                    title = "Max Count",
//                    onValueChange = { newValue ->
//                        newValue.toIntOrNull()?.let { max ->
//                            onConfigChanged(config.copy(MaxCount = max))
//                        }
//                    },
//                    modifier = Modifier.weight(1f).padding(start = 4.dp)
//                )
//            }
//
//            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
//                DecimalInputField(
//                    value = config.MinTiersCount.toString(),
//                    title = "Min Tiers Count",
//                    onValueChange = { newValue ->
//                        newValue.toIntOrNull()?.let { min ->
//                            onConfigChanged(config.copy(MinTiersCount = min))
//                        }
//                    },
//                    modifier = Modifier.weight(1f).padding(end = 4.dp)
//                )
//
//                DecimalInputField(
//                    value = config.MaxTiersCount.toString(),
//                    title = "Max Tiers Count",
//                    onValueChange = { newValue ->
//                        newValue.toIntOrNull()?.let { max ->
//                            onConfigChanged(config.copy(MaxTiersCount = max))
//                        }
//                    },
//                    modifier = Modifier.weight(1f).padding(start = 4.dp)
//                )
//            }
//
//            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
//                Checkbox(
//                    checked = config.UniformDistribution,
//                    onCheckedChange = { newValue ->
//                        onConfigChanged(config.copy(UniformDistribution = newValue))
//                    }
//                )
//                Text("Uniform Distribution", modifier = Modifier.padding(start = 8.dp))
//            }
//
//            Text("Allowed Tiers", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(top = 8.dp))
//            ChipGroup(
//                items = (1..7).toList(),
//                selectedItems = config.AllowedTiers,
//                onItemSelected = { tier, selected ->
//                    val newTiers = if (selected) {
//                        config.AllowedTiers + tier
//                    } else {
//                        config.AllowedTiers - tier
//                    }.distinct().sorted()
//                    onConfigChanged(config.copy(AllowedTiers = newTiers))
//                },
//                itemContent = { tier -> Text("Tier $tier") }
//            )
//        }
//    }
//}
//
//@Composable
//fun MineConfigEditor(
//    config: ResourcesConfig,
//    onConfigChanged: (ResourcesConfig) -> Unit,
//    onDelete: () -> Unit
//) {
//    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
//        Column(modifier = Modifier.padding(8.dp)) {
//            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
//                Text("Mine Generation Config", style = MaterialTheme.typography.titleSmall)
//                IconButton(onClick = onDelete) {
//                    Icon(Icons.Default.Delete, contentDescription = "Delete")
//                }
//            }
//
//            val mineTypes = listOf(
//                "Wood" to config.Wood,
//                "Ore" to config.Ore,
//                "Mercury" to config.Mercury,
//                "Crystals" to config.Crystals,
//                "Sulfur" to config.Sulfur,
//                "Gems" to config.Gems,
//                "Gold" to config.Gold
//            )
//
//            mineTypes.forEach { (type, count) ->
//                DecimalInputField(
//                    value = count.toString(),
//                    title = type,
//                    onValueChange = { newValue ->
//                        val newCount = IntValueConfig(newValue.toIntOrNull())
//                        val newConfig = when (type) {
//                            "Wood" -> config.copy(Wood = newCount)
//                            "Ore" -> config.copy(Ore = newCount)
//                            "Mercury" -> config.copy(Mercury = newCount)
//                            "Crystals" -> config.copy(Crystals = newCount)
//                            "Sulfur" -> config.copy(Sulfur = newCount)
//                            "Gems" -> config.copy(Gems = newCount)
//                            "Gold" -> config.copy(Gold = newCount)
//                            else -> config
//                        }
//                        onConfigChanged(newConfig)
//                    },
//                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun CreaturesConfigEditor(
//    config: CreaturesConfiguration,
//    onConfigChanged: (CreaturesConfiguration) -> Unit,
//    onDelete: () -> Unit
//) {
//    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
//        Column(modifier = Modifier.padding(8.dp)) {
//            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
//                Text("Creatures Configuration", style = MaterialTheme.typography.titleSmall)
//                IconButton(onClick = onDelete) {
//                    Icon(Icons.Default.Delete, contentDescription = "Delete")
//                }
//            }
//
//            IntConfigEditor(
//                name = "ReplacementsCount",
//                config = config.ReplacementsCount,
//                onConfigChanged = { newConfig ->
//                    onConfigChanged(config.copy(ReplacementsCount = newConfig))
//                },
//                onDelete = {}
//            )
//
//            Column(modifier = Modifier.padding(vertical = 8.dp)) {
//                Text("Options", style = MaterialTheme.typography.titleSmall)
//
//                val options = listOf(
//                    "TerrainFaction" to config.TerrainFaction,
//                    "NonPlayersFactions" to config.NonPlayersFactions,
//                    "NoGrades" to config.NoGrades,
//                    "Grades" to config.Grades,
//                    "Neutrals" to config.Neutrals,
//                    "NonUniqueReplacements" to config.NonUniqueReplacements
//                )
//
//                options.forEach { (name, value) ->
//                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
//                        Checkbox(
//                            checked = value ?: false,
//                            onCheckedChange = { newValue ->
//                                val newConfig = when (name) {
//                                    "TerrainFaction" -> config.copy(TerrainFaction = newValue)
//                                    "NonPlayersFactions" -> config.copy(NonPlayersFactions = newValue)
//                                    "NoGrades" -> config.copy(NoGrades = newValue)
//                                    "Grades" -> config.copy(Grades = newValue)
//                                    "Neutrals" -> config.copy(Neutrals = newValue)
//                                    "NonUniqueReplacements" -> config.copy(NonUniqueReplacements = newValue)
//                                    else -> config
//                                }
//                                onConfigChanged(newConfig)
//                            }
//                        )
//                        Text(name.replace(Regex("([A-Z])"), " $1").trim(), modifier = Modifier.padding(start = 8.dp))
//                    }
//                }
//            }
//
//            Column(modifier = Modifier.padding(vertical = 8.dp)) {
//                Text("Multipliers", style = MaterialTheme.typography.titleSmall)
//
//                val multipliers = listOf(
//                    "BaseCostMultiplier" to config.BaseCostMultiplier,
//                    "BaseResourcesMultiplier" to config.BaseResourcesMultiplier,
//                    "BaseGrowMultiplier" to config.BaseGrowMultiplier
//                )
//
//                multipliers.forEach { (name, value) ->
//                    DecimalInputField(
//                        value = value?.toString() ?: "",
//                        title = name.replace(Regex("([A-Z])"), " $1").trim(),
//                        onValueChange = { newValue ->
//                            val newMultiplier = newValue.toDoubleOrNull()
//                            val newConfig = when (name) {
//                                "BaseCostMultiplier" -> config.copy(BaseCostMultiplier = newMultiplier)
//                                "BaseResourcesMultiplier" -> config.copy(BaseResourcesMultiplier = newMultiplier)
//                                "BaseGrowMultiplier" -> config.copy(BaseGrowMultiplier = newMultiplier)
//                                else -> config
//                            }
//                            onConfigChanged(newConfig)
//                        },
//                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
//                    )
//                }
//            }
//
//            Text(
//                "Creature Modifiers",
//                style = MaterialTheme.typography.titleSmall,
//                modifier = Modifier.padding(top = 8.dp)
//            )
//            CreatureModifiersList(
//                modifiers = config.CreatureModifiers,
//                onModifiersChanged = { newModifiers ->
//                    onConfigChanged(config.copy(CreatureModifiers = newModifiers))
//                }
//            )
//
//            Text(
//                "Creature Tier Replacements",
//                style = MaterialTheme.typography.titleSmall,
//                modifier = Modifier.padding(top = 8.dp)
//            )
//            CreatureTierReplacementsList(
//                replacements = config.CreatureTierReplacements,
//                onReplacementsChanged = { newReplacements ->
//                    onConfigChanged(config.copy(CreatureTierReplacements = newReplacements))
//                }
//            )
//        }
//    }
//}
//
//@Composable
//fun CreatureModifiersList(
//    modifiers: List<CreatureModifier>,
//    onModifiersChanged: (List<CreatureModifier>) -> Unit
//) {
//    Column {
//        modifiers.forEachIndexed { index, modifier ->
//            Card(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
//                Column(modifier = Modifier.padding(8.dp)) {
//                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
//                        Text("Modifier ${index + 1}", style = MaterialTheme.typography.titleSmall)
//                        IconButton(onClick = {
//                            onModifiersChanged(modifiers.toMutableList().apply { removeAt(index) })
//                        }) {
//                            Icon(Icons.Default.Delete, contentDescription = "Delete")
//                        }
//                    }
//// Tier (integer input)
//                    DecimalInputField(
//                        value = modifier.Tier.toString(),
//                        title = "Tier",
//                        onValueChange = { newValue ->
//                            newValue.toIntOrNull()?.let { tier ->
//                                val newModifiers = modifiers.toMutableList().apply {
//                                    set(index, modifier.copy(Tier = tier))
//                                }
//                                onModifiersChanged(newModifiers)
//                            }
//                        },
//                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
//                    )
//
//// Cost Multiplier (decimal input)
//                    DecimalInputField(
//                        value = modifier.CostMultiplier?.toString() ?: "",
//                        title = "Cost Multiplier",
//                        onValueChange = { newValue ->
//                            val newModifiers = modifiers.toMutableList().apply {
//                                set(index, modifier.copy(CostMultiplier = newValue.toDoubleOrNull()))
//                            }
//                            onModifiersChanged(newModifiers)
//                        },
//                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
//                    )
//
//// Resources Multiplier (decimal input)
//                    DecimalInputField(
//                        value = modifier.ResourcesMultiplier?.toString() ?: "",
//                        title = "Resources Multiplier",
//                        onValueChange = { newValue ->
//                            val newModifiers = modifiers.toMutableList().apply {
//                                set(index, modifier.copy(ResourcesMultiplier = newValue.toDoubleOrNull()))
//                            }
//                            onModifiersChanged(newModifiers)
//                        },
//                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
//                    )
//
//// Grow Multiplier (decimal input)
//                    DecimalInputField(
//                        value = modifier.GrowMultiplier.toString(),
//                        title = "Grow Multiplier",
//                        onValueChange = { newValue ->
//                            val newModifiers = modifiers.toMutableList().apply {
//                                set(index, modifier.copy(GrowMultiplier = newValue.toDoubleOrNull()))
//                            }
//                            onModifiersChanged(newModifiers)
//                        },
//                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
//                    )
//                }
//            }
//        }
//
//        Button(
//            onClick = {
//                onModifiersChanged(
//                    modifiers + CreatureModifier(
//                        Tier = 1,
//                        CostMultiplier = 1.0,
//                        ResourcesMultiplier = 1.0,
//                        GrowMultiplier = 1.0
//                    )
//                )
//            },
//            modifier = Modifier.padding(top = 8.dp)
//        ) {
//            Text("Add Modifier")
//        }
//    }
//}
//
//@Composable
//fun CreatureTierReplacementsList(
//    replacements: List<CreatureTierReplacement>,
//    onReplacementsChanged: (List<CreatureTierReplacement>) -> Unit
//) {
//    Column {
//        replacements.forEachIndexed { index, replacement ->
//            Card(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
//                Column(modifier = Modifier.padding(8.dp)) {
//                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
//                        Text("Replacement ${index + 1}", style = MaterialTheme.typography.titleSmall)
//                        IconButton(onClick = {
//                            onReplacementsChanged(replacements.toMutableList().apply { removeAt(index) })
//                        }) {
//                            Icon(Icons.Default.Delete, contentDescription = "Delete")
//                        }
//                    }
//
//                    DecimalInputField(
//                        value = replacement.Tier.toString(),
//                        onValueChange = { newValue ->
//                            val newReplacements = replacements.toMutableList().apply {
//                                set(index, replacement.copy(Tier = newValue.toIntOrNull() ?: 1))
//                            }
//                            onReplacementsChanged(newReplacements)
//                        },
//                        title = "Tier",
//                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
//                    )
//                    Text(
//                        "Creature IDs",
//                        style = MaterialTheme.typography.titleSmall,
//                        modifier = Modifier.padding(top = 8.dp)
//                    )
//                    ChipGroup(
//                        items = replacement.CreatureIds,
//                        title = { it },
//                        onItemRemoved = { creatureId ->
//                            val newIds = replacement.CreatureIds.toMutableList().apply { remove(creatureId) }
//                            val newReplacements = replacements.toMutableList().apply {
//                                set(index, replacement.copy(CreatureIds = newIds))
//                            }
//                            onReplacementsChanged(newReplacements)
//                        }
//                    )
//
//                    var newCreatureId by remember { mutableStateOf("") }
//                    Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
//                        OutlinedTextField(
//                            value = newCreatureId,
//                            onValueChange = { newCreatureId = it },
//                            label = { Text("New Creature ID") },
//                            modifier = Modifier.weight(1f)
//                        )
//                        Button(
//                            onClick = {
//                                if (newCreatureId.isNotBlank()) {
//                                    val newIds = replacement.CreatureIds + newCreatureId
//                                    val newReplacements = replacements.toMutableList().apply {
//                                        set(index, replacement.copy(CreatureIds = newIds))
//                                    }
//                                    onReplacementsChanged(newReplacements)
//                                    newCreatureId = ""
//                                }
//                            },
//                            modifier = Modifier.padding(start = 8.dp)
//                        ) {
//                            Text("Add")
//                        }
//                    }
//                }
//            }
//        }
//
//        Button(
//            onClick = {
//                onReplacementsChanged(
//                    replacements + CreatureTierReplacement(
//                        CreatureIds = emptyList(),
//                        Tier = 1
//                    )
//                )
//            },
//            modifier = Modifier.padding(top = 8.dp)
//        ) {
//            Text("Add Replacement")
//        }
//    }
//}
//
@Composable
fun <T> ChipGroup(
    items: List<T>,
    title: (T) -> String,
    onItemRemoved: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        items.forEach { item ->
            InputChip(
                onClick = {},
                label = { Text(title(item)) },
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

//@Composable
//fun <T> ChipGroup(
//    items: List<T>,
//    selectedItems: List<T>,
//    onItemSelected: (T, Boolean) -> Unit,
//    itemContent: @Composable (T) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    FlowRow(
//        horizontalArrangement = Arrangement.spacedBy(4.dp),
//        modifier = modifier.fillMaxWidth()
//    ) {
//        items.forEach { item ->
//            FilterChip(
//                selected = selectedItems.contains(item),
//                onClick = { onItemSelected(item, !selectedItems.contains(item)) },
//                label = { itemContent(item) },
//                modifier = Modifier.padding(2.dp)
//            )
//        }
//    }
//}
//
//// Similar implementations for other sections (ArmyConfigSection, ConnectionsConfigSection, etc.)
//// Each would follow the same pattern as the ZonesConfigSection with appropriate editors for their data structures
//
//@Composable
//fun ArmyConfigSection(
//    baseMultiplier: Double?,
//    multipliers: ArmyMultipliersEntry,
//    onBaseMultiplierChanged: (Double?) -> Unit,
//    onMultipliersChanged: (ArmyMultipliersEntry) -> Unit
//) {
//    Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
//        Text("Army Multipliers", style = MaterialTheme.typography.headlineMedium)
//
//        DecimalInputField(
//            value = baseMultiplier.toString(),
//            title = "Base Army Multiplier",
//            onValueChange = { newValue ->
//                onBaseMultiplierChanged(newValue.toDoubleOrNull())
//            },
//            modifier = Modifier.fillMaxWidth().padding(8.dp)
//        )
//
//        multipliers.forEach { (faction, multiplier) ->
//            DecimalInputField(
//                value = multiplier.toString(),
//                onValueChange = { newValue ->
//                    val newMultiplier = newValue.toDoubleOrNull()
//                    multipliers[faction] = newMultiplier
//                    onMultipliersChanged(multipliers)
//                },
//                title = "$faction Multiplier",
//                modifier = Modifier.fillMaxWidth().padding(8.dp)
//            )
//        }
//    }
//}
//
//@Composable
//fun CreatureBuildingConfigEditor(
//    config: CreatureBuildingConfig,
//    onConfigChanged: (CreatureBuildingConfig) -> Unit,
//    onDelete: () -> Unit
//) {
//    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
//        Column(modifier = Modifier.padding(8.dp)) {
//            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
//                Text("Creature Building Config", style = MaterialTheme.typography.titleSmall)
//                IconButton(onClick = onDelete) {
//                    Icon(Icons.Default.Delete, contentDescription = "Delete")
//                }
//            }
//
//            Text("Tiers Pool", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(top = 8.dp))
//            ChipGroup(
//                items = (1..7).toList(),
//                selectedItems = config.TiersPool,
//                onItemSelected = { tier, selected ->
//                    val newTiers = if (selected) {
//                        config.TiersPool + tier
//                    } else {
//                        config.TiersPool - tier
//                    }.distinct().sorted()
//                    onConfigChanged(config.copy(TiersPool = newTiers))
//                },
//                itemContent = { tier -> Text("Tier $tier") }
//            )
//
//            Column(modifier = Modifier.padding(vertical = 8.dp)) {
//                Text("Options", style = MaterialTheme.typography.titleSmall)
//
//                val options = listOf(
//                    "NoGrades" to config.NoGrades,
//                    "Grades" to config.Grades,
//                    "Neutrals" to config.Neutrals
//                )
//
//                options.forEach { (name, value) ->
//                    value?.let {
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            modifier = Modifier.padding(vertical = 4.dp)
//                        ) {
//                            Checkbox(
//                                checked = it,
//                                onCheckedChange = { newValue ->
//                                    val newConfig = when (name) {
//                                        "NoGrades" -> config.copy(NoGrades = newValue)
//                                        "Grades" -> config.copy(Grades = newValue)
//                                        "Neutrals" -> config.copy(Neutrals = newValue)
//                                        else -> config
//                                    }
//                                    onConfigChanged(newConfig)
//                                }
//                            )
//                            Text(
//                                name.replace(Regex("([A-Z])"), " $1").trim(),
//                                modifier = Modifier.padding(start = 8.dp)
//                            )
//                        }
//                    }
//                }
//            }
//
//            Text("Creature IDs", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(top = 8.dp))
//            ChipGroup(
//                items = config.CreatureIds,
//                onItemRemoved = { creatureId ->
//                    val newIds = config.CreatureIds.toMutableList().apply { remove(creatureId) }
//                    onConfigChanged(config.copy(CreatureIds = newIds))
//                }
//            )
//
//            var newCreatureId by remember { mutableStateOf("") }
//            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
//                OutlinedTextField(
//                    value = newCreatureId,
//                    onValueChange = { newCreatureId = it },
//                    label = { Text("New Creature ID") },
//                    modifier = Modifier.weight(1f)
//                )
//                Button(
//                    onClick = {
//                        if (newCreatureId.isNotBlank()) {
//                            val newIds = config.CreatureIds + newCreatureId
//                            onConfigChanged(config.copy(CreatureIds = newIds))
//                            newCreatureId = ""
//                        }
//                    },
//                    modifier = Modifier.padding(start = 8.dp)
//                ) {
//                    Text("Add")
//                }
//            }
//
//            Column(modifier = Modifier.padding(vertical = 8.dp)) {
//                Text("Multipliers", style = MaterialTheme.typography.titleSmall)
//
//                val multipliers = listOf(
//                    "CostMultiplier" to config.CostMultiplier,
//                    "ResourcesMultiplier" to config.ResourcesMultiplier,
//                    "GrowMultiplier" to config.GrowMultiplier
//                )
//
//                multipliers.forEach { (name, value) ->
//                    value?.let {
//                        OutlinedTextField(
//                            value = it.toString(),
//                            onValueChange = { newValue ->
//                                newValue.toDoubleOrNull()?.let { multiplier ->
//                                    val newConfig = when (name) {
//                                        "CostMultiplier" -> config.copy(CostMultiplier = multiplier)
//                                        "ResourcesMultiplier" -> config.copy(ResourcesMultiplier = multiplier)
//                                        "GrowMultiplier" -> config.copy(GrowMultiplier = multiplier)
//                                        else -> config
//                                    }
//                                    onConfigChanged(newConfig)
//                                }
//                            },
//                            label = { Text(name.replace(Regex("([A-Z])"), " $1").trim()) },
//                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun CreatureBankConfigEditor(
//    config: SealedBuildingType.CreatureBankConfig,
//    onConfigChanged: (SealedBuildingType.CreatureBankConfig) -> Unit
//) {
//    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
//        Column(modifier = Modifier.padding(8.dp)) {
//            Text("Creature Bank Config", style = MaterialTheme.typography.titleSmall)
//
//            OutlinedTextField(
//                value = config.Name,
//                onValueChange = { newValue ->
//                    onConfigChanged(config.copy(Name = newValue))
//                },
//                label = { Text("Name") },
//                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
//            )
//
//            Text("Creatures Pool", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(top = 8.dp))
//            ChipGroup(
//                items = config.CreaturesPool,
//                title = {it},
//                onItemRemoved = { creatureId ->
//                    val newPool = config.CreaturesPool.toMutableList().apply { remove(creatureId) }
//                    onConfigChanged(config.copy(CreaturesPool = newPool))
//                }
//            )
//
//            var newCreatureId by remember { mutableStateOf("") }
//            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
//                OutlinedTextField(
//                    value = newCreatureId,
//                    onValueChange = { newCreatureId = it },
//                    label = { Text("New Creature ID") },
//                    modifier = Modifier.weight(1f)
//                )
//                Button(
//                    onClick = {
//                        if (newCreatureId.isNotBlank()) {
//                            val newPool = config.CreaturesPool + newCreatureId
//                            onConfigChanged(config.copy(CreaturesPool = newPool))
//                            newCreatureId = ""
//                        }
//                    },
//                    modifier = Modifier.padding(start = 8.dp)
//                ) {
//                    Text("Add")
//                }
//            }
//
//            Text("Guards Pool", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(top = 8.dp))
//            config.GuardsPool.forEachIndexed { index, guardsPool ->
//                Card(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
//                    Column(modifier = Modifier.padding(8.dp)) {
//                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
//                            Text("Guards Pool ${index + 1}", style = MaterialTheme.typography.titleSmall)
//                            IconButton(onClick = {
//                                val newPools = config.GuardsPool.toMutableList().apply { removeAt(index) }
//                                onConfigChanged(config.copy(GuardsPool = newPools))
//                            }) {
//                                Icon(Icons.Default.Delete, contentDescription = "Delete")
//                            }
//                        }
//
//                        ChipGroup(
//                            items = guardsPool.Values,
//                            onItemRemoved = { creatureId ->
//                                val newValues = guardsPool.Values.toMutableList().apply { remove(creatureId) }
//                                val newPools = config.GuardsPool.toMutableList().apply {
//                                    set(index, guardsPool.copy(Values = newValues))
//                                }
//                                onConfigChanged(config.copy(GuardsPool = newPools))
//                            }
//                        )
//
//                        var newGuardId by remember { mutableStateOf("") }
//                        Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
//                            OutlinedTextField(
//                                value = newGuardId,
//                                onValueChange = { newGuardId = it },
//                                label = { Text("New Guard ID") },
//                                modifier = Modifier.weight(1f)
//                            )
//                            Button(
//                                onClick = {
//                                    if (newGuardId.isNotBlank()) {
//                                        val newValues = guardsPool.Values + newGuardId
//                                        val newPools = config.GuardsPool.toMutableList().apply {
//                                            set(index, guardsPool.copy(Values = newValues))
//                                        }
//                                        onConfigChanged(config.copy(GuardsPool = newPools))
//                                        newGuardId = ""
//                                    }
//                                },
//                                modifier = Modifier.padding(start = 8.dp)
//                            ) {
//                                Text("Add")
//                            }
//                        }
//                    }
//                }
//            }
//
//            Button(
//                onClick = {
//                    onConfigChanged(config.copy(GuardsPool = config.GuardsPool + GuardsPool(emptyList())))
//                },
//                modifier = Modifier.padding(top = 8.dp)
//            ) {
//                Text("Add Guards Pool")
//            }
//
//            Column(modifier = Modifier.padding(vertical = 8.dp)) {
//                Text("Multipliers", style = MaterialTheme.typography.titleSmall)
//
//                val multipliers = listOf(
//                    "CreatureCostMultiplier" to config.CreatureCostMultiplier,
//                    "CreatureGrowMultiplier" to config.CreatureGrowMultiplier,
//                    "CreatureResourcesMultiplier" to config.CreatureResourcesMultiplier,
//                    "GuardGrowMultiplier" to config.GuardGrowMultiplier
//                )
//
//                multipliers.forEach { (name, value) ->
//                    DecimalInputField(
//                        value = value.toString(),
//                        onValueChange = { newValue ->
//                            val multiplier = newValue.toDoubleOrNull()
//                            val newConfig = when (name) {
//                                "CreatureCostMultiplier" -> config.copy(CreatureCostMultiplier = multiplier)
//                                "CreatureGrowMultiplier" -> config.copy(CreatureGrowMultiplier = multiplier)
//                                "CreatureResourcesMultiplier" -> config.copy(CreatureResourcesMultiplier = multiplier)
//                                "GuardGrowMultiplier" -> config.copy(GuardGrowMultiplier = multiplier)
//                                else -> config
//                            }
//                            onConfigChanged(newConfig)
//                        },
//                        title = name.replace(Regex("([A-Z])"), " $1").trim(),
//                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
//                    )
//                }
//            }
//        }
//    }
//}
//
