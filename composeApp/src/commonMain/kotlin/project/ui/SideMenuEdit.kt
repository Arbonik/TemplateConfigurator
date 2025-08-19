package project.ui


import TerrainBuildingsConfig
import TerrainConfig
import TerrainType
import ZoneRandomizationConfig
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import project.ui.common.EnumDropdown

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
