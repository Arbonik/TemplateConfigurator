import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import project.data.enums.CastleType
import project.ui.common.NullableFiled
import kotlin.collections.plus

@Composable
fun StartBuildingConfigEditor(
    configs: List<StartBuildingConfig>,
    onConfigsUpdated: (List<StartBuildingConfig>) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectionConfigIndex by remember { mutableStateOf(if (configs.isEmpty()) -1 else 0) }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .width(200.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    val newbuilding = StartBuildingConfig(
                        ApplyAllTerrains = null,
                        TerrainType = null,
                        CastleType = null,
                        emptyList(),
                        BuildingMode = null
                    )
                    onConfigsUpdated(configs + newbuilding)
                    selectionConfigIndex = configs.lastIndex

                },
                enabled = true
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
                Text("Добавить конфиг")
            }

            Column(
                modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())
            ) {
                configs.forEachIndexed { index, config ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectionConfigIndex = index
                            }
                            .padding(8.dp),
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Config ${index + 1}")
                                    IconButton(
                                        onClick = {
                                            onConfigsUpdated(
                                                configs - config
                                            )
                                        }
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                                    }
                            }
                        }
                    }
                }
            }
        }

        VerticalDivider(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
        )

        if (configs.isNotEmpty())
            ConfigEditor(
                config = configs[selectionConfigIndex],
                onConfigUpdated = {
                    onConfigsUpdated(configs.toMutableList().apply {
                        set(selectionConfigIndex, it)
                    })
                },
            )
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

        if (config.ApplyAllTerrains != true)
            NullableFiled(
                value = config.TerrainType,
                label = "Terrain Type:",
                onValueChange = { onConfigUpdated(config.copy(TerrainType = it)) },
                defaultValue = TerrainType.FirstPlayer,
            ) {
                EnumDropdownRow(
                    label = "",
                    currentValue = config.TerrainType,
                    itemTitle = { it.toString() },
                    values = TerrainType.values().toList(),
                    onValueSelected = {
                        onConfigUpdated(config.copy(TerrainType = it))
                    }
                )
            }

        if (config.ApplyAllTerrains != true && config.TerrainType == null)
            EnumDropdownRow(
                label = "Castle Type:",
                currentValue = config.CastleType,
                itemTitle = { it.toString() },
                values = CastleType.values().toList(),
                onValueSelected = {
                    onConfigUpdated(config.copy(CastleType = it))
                }
            )

        EnumDropdownRow(
            label = "Building Mode:",
            currentValue = config.BuildingMode,
            itemTitle = { it.toString() },
            values = BuildingMode.values().toList(),
            onValueSelected = {
                onConfigUpdated(config.copy(BuildingMode = it))
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