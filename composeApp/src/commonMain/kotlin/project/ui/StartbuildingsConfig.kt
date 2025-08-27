import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import project.ui.common.CommonVerticalListItem
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
                    CommonVerticalListItem(
                        item = config,
                        isSelected = index == selectionConfigIndex,
                        onDelete = {
                            onConfigsUpdated(
                                configs - config
                            )
                        },
                        onSelected = { selectionConfigIndex = index },
                    ) {
                        Text("Config ${index + 1}", modifier = Modifier.padding(start = 8.dp))
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

        Button(
            onClick = { showBuildingsDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Buildings (${config.Buildings.size} selected)")
        }

        if (showBuildingsDialog) {
            SearchableEnumDialog(
                label = "Select Building",
                items = BuildingType.entries - config.Buildings.toSet(),
                itemTitle = { it.description },
                onDismiss = { showBuildingsDialog = false },
                onItemSelected = { newBuilding ->
                    onConfigUpdated(config.copy(Buildings = config.Buildings + newBuilding))
                    showBuildingsDialog = false
                }
            )
        }
        // Show selected buildings as chips
        FlowRow(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            config.Buildings.forEach { building ->
                Chip(
                    label = { Text(building.description) },
                    modifier = Modifier.padding(4.dp).clickable {
                        onConfigUpdated(
                            config.copy(
                                Buildings = config.Buildings - building
                            )
                        )
                    },
                    trailingIcon = {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Remove",
                            modifier = Modifier.size(16.dp).align(Alignment.CenterVertically)
                        )
                    }
                )
            }
        }
    }
}
