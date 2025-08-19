import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ScriptFeaturesConfigEditor(
    config: ScriptFeaturesConfig,
    onConfigChanged: (ScriptFeaturesConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentConfig by remember { mutableStateOf(config) }

    Column(
        modifier = modifier.padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("Script Features Configuration", style = MaterialTheme.typography.headlineMedium)

        CastleCaptureEditor(
            model = currentConfig.CastleCaptureProps ?: CastleCaptureModel(),
            onModelChanged = {
                currentConfig = currentConfig.copy(CastleCaptureProps = it.takeIf { model ->
                    model != CastleCaptureModel() // Only include if not default
                })
                onConfigChanged(currentConfig)
            }
        )
        GmRebuildEditor(
            model = currentConfig.GmRebuildProps ?: GMRebuildModel(),
            onModelChanged = {
                currentConfig = currentConfig.copy(GmRebuildProps = it.takeIf { model ->
                    model != GMRebuildModel() // Only include if not default
                })
                onConfigChanged(currentConfig)
            }
        )
        GloballyDisabledBuildingsEditor(
            model = currentConfig.GloballyDisabledBuildingsProps ?: GloballyDisabledBuildingsModel(),
            onModelChanged = {
                currentConfig = currentConfig.copy(GloballyDisabledBuildingsProps = it.takeIf { model ->
                    model.Buildings.isNotEmpty() // Only include if not empty
                })
                onConfigChanged(currentConfig)
            }
        )
        ForcedFinalBattleListEditor(
            models = currentConfig.ForcedFinalBattleProps,
            onModelsChanged = {
                currentConfig = currentConfig.copy(ForcedFinalBattleProps = it)
                onConfigChanged(currentConfig)
            }
        )
        AdditionalStartCastlesEditor(
            castles = currentConfig.AdditionalStartCastles,
            onCastlesChanged = {
                currentConfig = currentConfig.copy(AdditionalStartCastles = it)
                onConfigChanged(currentConfig)
            }
        )
    }
}

@Composable
private fun CastleCaptureEditor(
    model: CastleCaptureModel,
    onModelChanged: (CastleCaptureModel) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentModel by remember { mutableStateOf(model) }

    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Castle Capture Properties", style = MaterialTheme.typography.headlineSmall)

            NumberInput(
                label = "Coordinate X",
                value = currentModel.CoordinateX,
                onValueChanged = {
                    currentModel = currentModel.copy(CoordinateX = it)
                    onModelChanged(currentModel)
                }
            )

            NumberInput(
                label = "Coordinate Y",
                value = currentModel.CoordinateY,
                onValueChanged = {
                    currentModel = currentModel.copy(CoordinateY = it)
                    onModelChanged(currentModel)
                }
            )

            NumberInput(
                label = "Search Radius",
                value = currentModel.SearchRadius,
                onValueChanged = {
                    currentModel = currentModel.copy(SearchRadius = it?.toInt())
                    onModelChanged(currentModel)
                }
            )

            NumberInput(
                label = "Event Timer",
                value = currentModel.EventTimer,
                onValueChanged = {
                    currentModel = currentModel.copy(EventTimer = it)
                    onModelChanged(currentModel)
                }
            )

            CheckboxInput(
                label = "Disable Fortifications",
                checked = currentModel.DisableFortifications ?: false,
                onCheckedChanged = {
                    currentModel = currentModel.copy(DisableFortifications = it)
                    onModelChanged(currentModel)
                }
            )

            CheckboxInput(
                label = "Is Forced Final Battle",
                checked = currentModel.IsForcedFinalBattle ?: false,
                onCheckedChanged = {
                    currentModel = currentModel.copy(IsForcedFinalBattle = it)
                    onModelChanged(currentModel)
                }
            )
        }
    }
}

@Composable
private fun GmRebuildEditor(
    model: GMRebuildModel,
    onModelChanged: (GMRebuildModel) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentModel by remember { mutableStateOf(model) }

    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("GM Rebuild Properties", style = MaterialTheme.typography.headlineSmall)

            NumberInput(
                label = "Minimal GM Level",
                value = currentModel.MinimalGMLevel,
                onValueChanged = {
                    currentModel = currentModel.copy(MinimalGMLevel = it)
                    onModelChanged(currentModel)
                }
            )

            NumberInput(
                label = "Minimal War Cries Level",
                value = currentModel.MinimalWarCriesLevel,
                onValueChanged = {
                    currentModel = currentModel.copy(MinimalWarCriesLevel = it)
                    onModelChanged(currentModel)
                }
            )

            Text("Rebuild Cost", style = MaterialTheme.typography.titleMedium)
            currentModel.RebuildCost?.let { resources ->
                ResourcesEditor(
                    resources = resources,
                    onResourcesChanged = {
                        currentModel = currentModel.copy(RebuildCost = it)
                        onModelChanged(currentModel)
                    }
                )
            } ?: run {
                Button(onClick = {
                    currentModel = currentModel.copy(RebuildCost = ResourcesModel())
                    onModelChanged(currentModel)
                }) {
                    Text("Add Rebuild Cost")
                }
            }
        }
    }
}

@Composable
private fun ResourcesEditor(
    resources: ResourcesModel,
    onResourcesChanged: (ResourcesModel) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentResources by remember { mutableStateOf(resources) }

    Column(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        NumberInput(
            label = "Wood",
            value = currentResources.Wood,
            onValueChanged = {
                currentResources = currentResources.copy(Wood = it?.toInt())
                onResourcesChanged(currentResources)
            }
        )

        NumberInput(
            label = "Ore",
            value = currentResources.Ore,
            onValueChanged = {
                currentResources = currentResources.copy(Ore = it?.toInt())
                onResourcesChanged(currentResources)
            }
        )

        NumberInput(
            label = "Mercury",
            value = currentResources.Mercury,
            onValueChanged = {
                currentResources = currentResources.copy(Mercury = it?.toInt())
                onResourcesChanged(currentResources)
            }
        )

        NumberInput(
            label = "Sulfur",
            value = currentResources.Sulfur,
            onValueChanged = {
                currentResources = currentResources.copy(Sulfur = it?.toInt())
                onResourcesChanged(currentResources)
            }
        )

        NumberInput(
            label = "Gem",
            value = currentResources.Gem,
            onValueChanged = {
                currentResources = currentResources.copy(Gem = it?.toInt())
                onResourcesChanged(currentResources)
            }
        )

        NumberInput(
            label = "Crystal",
            value = currentResources.Crystal,
            onValueChanged = {
                currentResources = currentResources.copy(Crystal = it?.toInt())
                onResourcesChanged(currentResources)
            }
        )

        NumberInput(
            label = "Gold",
            value = currentResources.Gold,
            onValueChanged = {
                currentResources = currentResources.copy(Gold = it?.toInt())
                onResourcesChanged(currentResources)
            }
        )
    }
}

@Composable
private fun GloballyDisabledBuildingsEditor(
    model: GloballyDisabledBuildingsModel,
    onModelChanged: (GloballyDisabledBuildingsModel) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentModel by remember { mutableStateOf(model) }
    val allBuildingTypes = remember { BuildingType.values().toList() }

    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Globally Disabled Buildings", style = MaterialTheme.typography.headlineSmall)

            if (currentModel.Buildings.isEmpty()) {
                Text("No buildings disabled", style = MaterialTheme.typography.bodyMedium)
            } else {
                FlowColumn {
                    currentModel.Buildings.forEach { building ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(building.description)
                            IconButton(onClick = {
                                currentModel = currentModel.copy(
                                    Buildings = currentModel.Buildings - building
                                )
                                onModelChanged(currentModel)
                            }) {
                                Icon(Icons.Default.Delete, "Remove")
                            }
                        }
                    }
                }
            }

            var showSelector by remember { mutableStateOf(false) }

            Button(onClick = { showSelector = true }) {
                Text("Add Building")
            }

            if (showSelector) {
                AlertDialog(
                    onDismissRequest = { showSelector = false },
                    title = { Text("Select Building to Disable") },
                    text = {
                        FlowColumn {
                            allBuildingTypes.filter { it !in currentModel.Buildings }.forEach { building ->
                                Button(
                                    onClick = {
                                        currentModel = currentModel.copy(
                                            Buildings = currentModel.Buildings + building
                                        )
                                        onModelChanged(currentModel)
                                        showSelector = false
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(building.description)
                                }
                            }
                        }
                    },
                    confirmButton = {
                        Button(onClick = { showSelector = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ForcedFinalBattleListEditor(
    models: List<ForcedFinalBattleModel>,
    onModelsChanged: (List<ForcedFinalBattleModel>) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentModels by remember { mutableStateOf(models) }

    Column(modifier = modifier) {
        // Header with add button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Forced Final Battles",
                style = MaterialTheme.typography.headlineSmall
            )

            IconButton(
                onClick = {
                    currentModels = currentModels + ForcedFinalBattleModel()
                    onModelsChanged(currentModels)
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add battle")
            }
        }

        // List of editors
        FlowColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            currentModels.forEachIndexed{ index, model ->
                ForcedFinalBattleEditor(
                    model = model,
                    onModelChanged = { updatedModel ->
                        currentModels = currentModels.toMutableList().apply {
                            set(index, updatedModel)
                        }
                        onModelsChanged(currentModels)
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // Delete button for each item
                IconButton(
                    onClick = {
                        currentModels = currentModels.toMutableList().apply {
                            removeAt(index)
                        }
                        onModelsChanged(currentModels)
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete battle")
                }
            }
        }
    }
}

@Composable
private fun ForcedFinalBattleEditor(
    model: ForcedFinalBattleModel,
    onModelChanged: (ForcedFinalBattleModel) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentModel by remember { mutableStateOf(model) }

    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Forced Final Battle", style = MaterialTheme.typography.headlineSmall)

            NumberInput(
                label = "Week",
                value = currentModel.Week,
                onValueChanged = {
                    currentModel = currentModel.copy(Week = it)
                    onModelChanged(currentModel)
                }
            )

            NumberInput(
                label = "Day",
                value = currentModel.Day,
                onValueChanged = {
                    currentModel = currentModel.copy(Day = it)
                    onModelChanged(currentModel)
                }
            )
        }
    }
}

@Composable
private fun AdditionalStartCastlesEditor(
    castles: List<AdditionalStartCastle>,
    onCastlesChanged: (List<AdditionalStartCastle>) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentCastles by remember { mutableStateOf(castles) }

    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Additional Start Castles", style = MaterialTheme.typography.headlineSmall)

            if (currentCastles.isEmpty()) {
                Text("No additional start castles configured", style = MaterialTheme.typography.bodyMedium)
            } else {
                FlowColumn {
                    currentCastles.forEach { castle ->
                        AdditionalStartCastleItem(
                            castle = castle,
                            onCastleChanged = { updated ->
                                currentCastles = currentCastles.map { if (it == castle) updated else it }
                                onCastlesChanged(currentCastles)
                            },
                            onRemove = {
                                currentCastles = currentCastles - castle
                                onCastlesChanged(currentCastles)
                            }
                        )
                    }
                }
            }

            Button(onClick = {
                currentCastles = currentCastles + AdditionalStartCastle()
                onCastlesChanged(currentCastles)
            }) {
                Text("Add Start Castle")
            }
        }
    }
}

@Composable
private fun AdditionalStartCastleItem(
    castle: AdditionalStartCastle,
    onCastleChanged: (AdditionalStartCastle) -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentCastle by remember { mutableStateOf(castle) }

    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Start Castle Configuration", style = MaterialTheme.typography.titleMedium)
                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.Delete, "Remove")
                }
            }

            Text("Start Position", style = MaterialTheme.typography.labelMedium)
            NumberInput(
                label = "Coordinate X",
                value = currentCastle.StartCoordinateX,
                onValueChanged = {
                    currentCastle = currentCastle.copy(StartCoordinateX = it)
                    onCastleChanged(currentCastle)
                }
            )

            NumberInput(
                label = "Coordinate Y",
                value = currentCastle.StartCoordinateY,
                onValueChanged = {
                    currentCastle = currentCastle.copy(StartCoordinateY = it)
                    onCastleChanged(currentCastle)
                }
            )

            NumberInput(
                label = "Search Radius",
                value = currentCastle.SearchRadius,
                onValueChanged = {
                    currentCastle = currentCastle.copy(SearchRadius = it?.toInt())
                    onCastleChanged(currentCastle)
                }
            )

            Text("Target Position", style = MaterialTheme.typography.labelMedium)
            NumberInput(
                label = "Coordinate X",
                value = currentCastle.TargetCoordinateX,
                onValueChanged = {
                    currentCastle = currentCastle.copy(TargetCoordinateX = it)
                    onCastleChanged(currentCastle)
                }
            )

            NumberInput(
                label = "Coordinate Y",
                value = currentCastle.TargetCoordinateY,
                onValueChanged = {
                    currentCastle = currentCastle.copy(TargetCoordinateY = it)
                    onCastleChanged(currentCastle)
                }
            )

            NumberInput(
                label = "Search Radius",
                value = currentCastle.TargetSearchRadius,
                onValueChanged = {
                    currentCastle = currentCastle.copy(TargetSearchRadius = it?.toInt())
                    onCastleChanged(currentCastle)
                }
            )
        }
    }
}

// Helper components

@Composable
private fun NumberInput(
    label: String,
    value: Long?,
    onValueChanged: (Long?) -> Unit,
    modifier: Modifier = Modifier
) {
    var textValue by remember { mutableStateOf(value?.toString() ?: "") }

    OutlinedTextField(
        value = textValue,
        onValueChange = {
            textValue = it
            onValueChanged(it.toLongOrNull())
        },
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
    )
}

@Composable
private fun NumberInput(
    label: String,
    value: Int?,
    onValueChanged: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    var textValue by remember { mutableStateOf(value?.toString() ?: "") }

    OutlinedTextField(
        value = textValue,
        onValueChange = {
            textValue = it
            onValueChanged(it.toIntOrNull())
        },
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
    )
}

@Composable
private fun CheckboxInput(
    label: String,
    checked: Boolean,
    onCheckedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChanged
        )
        Text(label, modifier = Modifier.padding(start = 8.dp))
    }
}