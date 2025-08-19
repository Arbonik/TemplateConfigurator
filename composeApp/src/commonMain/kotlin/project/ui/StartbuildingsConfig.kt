import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StartBuildingConfigEditor(
    configs: List<StartBuildingConfig>,
    onConfigsUpdated: (List<StartBuildingConfig>) -> Unit,
    modifier: Modifier = Modifier
) {
    var expandedConfigIndex by remember { mutableStateOf(-1) }
    val currentConfigs = remember {
        mutableStateListOf<StartBuildingConfig>()
            .apply { addAll(configs) }
    }

    Column(modifier = modifier) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Building Configurations", style = MaterialTheme.typography.headlineSmall)
            IconButton(
                onClick = {
                    currentConfigs.add(
                        StartBuildingConfig(
                            ApplyAllTerrains = null,
                            TerrainType.FirstPlayer,
                            _root_ide_package_.project.data.enums.CastleType.Humans,
                            emptyList(),
                            BuildingMode.All
                        )
                    )
                    onConfigsUpdated(currentConfigs)
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add new config")
            }
        }

        // Config list
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(currentConfigs) { config ->
                val index = currentConfigs.indexOf(config)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Config ${index + 1}")

                            Row {
                                IconButton(
                                    onClick = {
                                        expandedConfigIndex = if (expandedConfigIndex == index) -1 else index
                                    }
                                ) {
                                    Icon(
                                        if (expandedConfigIndex == index) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                        contentDescription = if (expandedConfigIndex == index) "Collapse" else "Expand"
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        currentConfigs.removeAt(index)
                                        onConfigsUpdated(currentConfigs)
                                        if (expandedConfigIndex == index) expandedConfigIndex = -1
                                    }
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                                }
                            }
                        }

                        if (index == expandedConfigIndex) {
                            ConfigEditor(
                                config = config,
                                onConfigUpdated = { updated ->
                                    currentConfigs[index] = updated
                                    onConfigsUpdated(currentConfigs)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ConfigEditor(
    config: StartBuildingConfig,
    onConfigUpdated: (StartBuildingConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(8.dp)) {
        // ApplyAllTerrains toggle
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        ) {
            Checkbox(
                checked = config.ApplyAllTerrains ?: false,
                onCheckedChange = { checked ->
                    onConfigUpdated(config.copy(ApplyAllTerrains = checked))
                },
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("Apply to all terrains")
        }

        // TerrainType dropdown with search
        DropdownSelector(
            label = "Terrain Type",
            currentValue = config.TerrainType,
            values = TerrainType.values().toList(),
            onValueChanged = { newValue ->
                onConfigUpdated(config.copy(TerrainType = newValue))
            },
            itemToString = { terrain ->
                when (terrain) {
                    TerrainType.FirstPlayer -> "First Player Terrain"
                    TerrainType.SecondPlayer -> "Second Player Terrain"
                    TerrainType.Terrain1 -> "Random Terrain 1"
                    // Add all other cases
                    else -> terrain.toString()
                }
            }
        )

        // CastleType dropdown with search
        DropdownSelector(
            label = "Castle Type",
            currentValue = config.CastleType,
            values = _root_ide_package_.project.data.enums.CastleType.values().toList(),
            onValueChanged = { newValue ->
                onConfigUpdated(config.copy(CastleType = newValue))
            },
            itemToString = { castle ->
                when (castle) {
                    _root_ide_package_.project.data.enums.CastleType.Humans -> "Humans Castle"
                    _root_ide_package_.project.data.enums.CastleType.Inferno -> "Inferno Castle"
                    // Add all other cases
                    else -> castle.toString()
                }
            }
        )

        // BuildingMode dropdown with search
        DropdownSelector(
            label = "Building Mode",
            currentValue = config.BuildingMode,
            values = BuildingMode.values().toList(),
            onValueChanged = { newValue ->
                onConfigUpdated(config.copy(BuildingMode = newValue))
            },
            itemToString = { mode ->
                when (mode) {
                    BuildingMode.All -> "All Buildings"
                    BuildingMode.StartCastle -> "Start Castle Buildings"
                    BuildingMode.NeutralCastle -> "Neutral Castle Buildings"
                    else -> {}
                }.toString()
            }
        )

        // Buildings multi-select with search
        Text("Buildings", modifier = Modifier.padding(top = 8.dp))

        var showBuildingsDialog by remember { mutableStateOf(false) }
        var buildingsSearchQuery by remember { mutableStateOf("") }

        Button(
            onClick = { showBuildingsDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Select Buildings (${config.Buildings.size} selected)")
        }

        if (showBuildingsDialog) {

            val selectedBuildings = remember(config.Buildings) {
                config.Buildings.toMutableStateList()
            }

            AlertDialog(
                onDismissRequest = { showBuildingsDialog = false },
                confirmButton = {
                    Button(
                        onClick = { showBuildingsDialog = false }
                    ) {
                        onConfigUpdated(config.copy(Buildings = selectedBuildings))
                        Text("Apply")
                    }
                },
                text = {
                    Column {
                        // Search field
                        OutlinedTextField(
                            value = buildingsSearchQuery,
                            onValueChange = { buildingsSearchQuery = it },
                            label = { Text("Search buildings") },
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") }
                        )

                        // Filtered buildings list
                        val filteredBuildings = BuildingType.values().filter {
                            it.description.contains(buildingsSearchQuery, ignoreCase = true)
                        }

                        LazyColumn(modifier = Modifier.heightIn(max = 400.dp)) {
                            items(filteredBuildings) { building ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            if (selectedBuildings.contains(building)) {
                                                selectedBuildings.remove(building)
                                            } else {
                                                selectedBuildings.add(building)
                                            }
                                        }
                                ) {
                                    Checkbox(
                                        checked = selectedBuildings.contains(building),
                                        onCheckedChange = null // handled by row click
                                    )
                                    Text(
                                        text = building.description,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                                Divider()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Show selected buildings as chips
        FlowRow(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            config.Buildings.forEach { building ->
                Chip(
                    onClick = {
                        onConfigUpdated(
                            config.copy(
                                Buildings = config.Buildings - building
                            )
                        )
                    },
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text(building.description)
                }
            }
        }
    }
}

// Simple Chip component
@Composable
fun Chip(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
        contentColor = MaterialTheme.colorScheme.primary,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp)
                .clickable(onClick = onClick),
        ) {
            content()
            Spacer(Modifier.width(4.dp))
            Icon(
                Icons.Default.Close,
                contentDescription = "Remove",
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun <T> DropdownSelector(
    label: String,
    currentValue: T,
    values: List<T>,
    onValueChanged: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(label)

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(currentValue.toString(), modifier = Modifier.weight(1f))
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                values.forEach { value ->
                    DropdownMenuItem(
                        onClick = {
                            onValueChanged(value)
                            expanded = false
                        },
                        text = {
                            Text(value.toString())
                        })
                }
            }
        }
    }
}

@Composable
fun <T> DropdownSelector(
    label: String,
    currentValue: T,
    values: List<T>,
    onValueChanged: (T) -> Unit,
    modifier: Modifier = Modifier,
    itemToString: (T) -> String = { it.toString() }
) {
    var showDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(label)

        OutlinedButton(
            onClick = { showDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(itemToString(currentValue), modifier = Modifier.weight(1f))
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Open selector")
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                dismissButton = {
                    Button(
                        onClick = { showDialog = false },
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    ) {
                        Text("Close")
                    }
                },
                confirmButton = {
                },
                text = {
                    Column {
                        // Search field
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            label = { Text("Search") },
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") }
                        )

                        // Filtered list
                        val filteredValues = values.filter {
                            itemToString(it).contains(searchQuery, ignoreCase = true)
                        }

                        LazyColumn(modifier = Modifier.heightIn(max = 400.dp)) {
                            items(filteredValues) { value ->
                                ListItem(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            onValueChanged(value)
                                            showDialog = false
                                        },
                                    headlineContent = { Text(itemToString(value)) },
                                    overlineContent = {
                                        if (value == currentValue) {
                                            Icon(
                                                Icons.Default.Check,
                                                contentDescription = "Selected",
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                )
                                Divider()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}