package org.example.project.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.example.project.data.CastleCaptureProps
import org.example.project.data.ForcedFinalBattleModel
import org.example.project.data.GMRebuildCost
import org.example.project.data.GMRebuildModel
import org.example.project.data.GloballyDisabledBuildingsProps
import org.example.project.data.ScriptFeaturesConfig
import org.example.project.data.enums.BuildingType
import org.example.project.ui.components.PickerDialog


@Composable
fun ScriptFeaturesConfigSection(
    scripts: ScriptFeaturesConfig,
    onScriptsChanged: (ScriptFeaturesConfig) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
        Text("Script Features Configuration", style = MaterialTheme.typography.headlineMedium)

        // Castle Capture Props
        Text(
            "Castle Capture", style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 16.dp)
        )
        CastleCaptureEditor(
            props = scripts.CastleCaptureProps,
            onPropsChanged = { newProps ->
                onScriptsChanged(scripts.copy(CastleCaptureProps = newProps))
            }
        )

        // GM Rebuild Props
        Text(
            "GM Rebuild", style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 24.dp)
        )
        GMRebuildEditor(
            props = scripts.GMRebuildProps,
            onPropsChanged = { newProps ->
                onScriptsChanged(scripts.copy(GMRebuildProps = newProps))
            },
            onDelete = {
                onScriptsChanged(scripts.copy(GMRebuildProps = null))
            }
        )

        // Globally Disabled Buildings
        Text(
            "Disabled Buildings", style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 24.dp)
        )
        GloballyDisabledBuildingsEditor(
            props = scripts.GloballyDisabledBuildingsProps,
            onPropsChanged = { newProps ->
                onScriptsChanged(scripts.copy(GloballyDisabledBuildingsProps = newProps))
            },
            onDelete = {
                onScriptsChanged(scripts.copy(GloballyDisabledBuildingsProps = null))
            }
        )

        // Forced Final Battle
        Text(
            "Final Battle", style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 24.dp)
        )
        ForcedFinalBattleEditor(
            props = scripts.ForcedFinalBattleProps,
            onPropsChanged = { newProps ->
                onScriptsChanged(scripts.copy(ForcedFinalBattleProps = newProps))
            },
            onDelete = {
                onScriptsChanged(scripts.copy(ForcedFinalBattleProps = null))
            }
        )
    }
}


@Composable
fun ForcedFinalBattleEditor(
    props: ForcedFinalBattleModel?,
    onPropsChanged: (ForcedFinalBattleModel) -> Unit,
    onDelete: () -> Unit
) {
    if (props != null) {
        Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Final Battle Settings", style = MaterialTheme.typography.titleMedium)
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }

                // Week and Day
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = props.Week.toString(),
                        onValueChange = { newValue ->
                            newValue.toLongOrNull()?.let { week ->
                                onPropsChanged(props.copy(Week = week))
                            }
                        },
                        label = { Text("Week") },
                        modifier = Modifier.weight(1f).padding(end = 4.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        )
                    )
                    OutlinedTextField(
                        value = props.Day.toString(),
                        onValueChange = { newValue ->
                            newValue.toLongOrNull()?.let { day ->
                                onPropsChanged(props.copy(Day = day))
                            }
                        },
                        label = { Text("Day") },
                        modifier = Modifier.weight(1f).padding(start = 4.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        )
                    )
                }
            }
        }
    } else {
        Button(
            onClick = {
                onPropsChanged(
                    ForcedFinalBattleModel(
                        Week = 1,
                        Day = 1
                    )
                )
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Enable Forced Final Battle")
        }
    }
}


@Composable
fun GloballyDisabledBuildingsEditor(
    props: GloballyDisabledBuildingsProps?,
    onPropsChanged: (GloballyDisabledBuildingsProps) -> Unit,
    onDelete: () -> Unit
) {
    if (props != null) {
        Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Disabled Buildings", style = MaterialTheme.typography.titleMedium)
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }

                // Buildings List
                ChipGroup(
                    items = props.Buildings.map { it.toString() },
                    onItemRemoved = { buildingId ->
                        onPropsChanged(props.copy(Buildings = props.Buildings - BuildingType.valueOf(buildingId)))
                    }
                )

                // Add new building
                Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                    var isDialogOpen by remember { mutableStateOf(false) }
                    PickerDialog(
                        show = isDialogOpen,
                        onDismiss = { isDialogOpen = false },
                        items = BuildingType.values().toList(),
                        text = { it.description },
                        onBuildingSelected = { buildingId ->
                            onPropsChanged(props.copy(Buildings = props.Buildings + buildingId))
                            isDialogOpen = false
                        }
                    )
                    Button(
                        onClick = {
                            isDialogOpen = true
                        },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text("Add")
                    }
                }
            }
        }
    } else {
        Button(
            onClick = {
                onPropsChanged(GloballyDisabledBuildingsProps(emptyList()))
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Enable Building Restrictions")
        }
    }
}


@Composable
fun GMRebuildEditor(
    props: GMRebuildModel?,
    onPropsChanged: (GMRebuildModel) -> Unit,
    onDelete: () -> Unit
) {
    if (props != null) {
        Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("GM Rebuild Settings", style = MaterialTheme.typography.titleMedium)
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }

                // Minimal Levels
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = props.MinimalGMLevel.toString(),
                        onValueChange = { newValue ->
                            newValue.toLongOrNull()?.let { level ->
                                onPropsChanged(props.copy(MinimalGMLevel = level))
                            }
                        },
                        label = { Text("Min GM Level") },
                        modifier = Modifier.weight(1f).padding(end = 4.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        )
                    )
                    OutlinedTextField(
                        value = props.MinimalWarcriesLevel.toString(),
                        onValueChange = { newValue ->
                            newValue.toLongOrNull()?.let { level ->
                                onPropsChanged(props.copy(MinimalWarcriesLevel = level))
                            }
                        },
                        label = { Text("Min Warcries Level") },
                        modifier = Modifier.weight(1f).padding(start = 4.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        )
                    )
                }

                // Rebuild Cost
                Text(
                    "Rebuild Cost", style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(top = 16.dp)
                )
                ResourceCostEditor(
                    cost = props.RebuildCost,
                    onCostChanged = { newCost ->
                        onPropsChanged(props.copy(RebuildCost = newCost))
                    }
                )
            }
        }
    } else {
        Button(
            onClick = {
                onPropsChanged(
                    GMRebuildModel(
                        MinimalGMLevel = 0,
                        MinimalWarcriesLevel = 0,
                        RebuildCost = GMRebuildCost()
                    )
                )
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Enable GM Rebuild")
        }
    }
}


@Composable
fun CastleCaptureEditor(
    props: CastleCaptureProps,
    onPropsChanged: (CastleCaptureProps) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Coordinates
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = props.CoordinateX.toString(),
                    onValueChange = { newValue ->
                        newValue.toLongOrNull()?.let { x ->
                            onPropsChanged(props.copy(CoordinateX = x))
                        }
                    },
                    label = { Text("Coordinate X") },
                    modifier = Modifier.weight(1f).padding(end = 4.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    )
                )
                OutlinedTextField(
                    value = props.CoordinateY.toString(),
                    onValueChange = { newValue ->
                        newValue.toLongOrNull()?.let { y ->
                            onPropsChanged(props.copy(CoordinateY = y))
                        }
                    },
                    label = { Text("Coordinate Y") },
                    modifier = Modifier.weight(1f).padding(start = 4.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    )
                )
            }

            // Search Radius
            OutlinedTextField(
                value = props.SearchRadius.toString(),
                onValueChange = { newValue ->
                    newValue.toIntOrNull()?.let { radius ->
                        onPropsChanged(props.copy(SearchRadius = radius))
                    }
                },
                label = { Text("Search Radius") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )

            // Capture Timer
            OutlinedTextField(
                value = props.CaptureTimer.toString(),
                onValueChange = { newValue ->
                    newValue.toLongOrNull()?.let { timer ->
                        onPropsChanged(props.copy(CaptureTimer = timer))
                    }
                },
                label = { Text("Capture Timer") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )

            // Disable Fortifications
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Checkbox(
                    checked = props.DisableFortifications,
                    onCheckedChange = { newValue ->
                        onPropsChanged(props.copy(DisableFortifications = newValue))
                    }
                )
                Text("Disable Fortifications", modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}
