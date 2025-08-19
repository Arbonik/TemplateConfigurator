import BuildingConfigItem
import BuildingDialogItem
import BuildingSelectionDialog
import BuildingTextureConfig
import Chip
import CreatureBankConfig
import CreatureBankConfigEditor
import CreatureBuildingConfig
import CreatureBuildingConfigEditor
import CreatureIdsEditor
import CustomBuildingConfig
import CustomBuildingConfigEditor
import DefaultBuilding
import DefaultBuildingConfig
import DefaultBuildingEditorWithDialog
import DefaultBuildingPicker
import EnumDialogData
import EnumDropdownRow
import IntValueConfig
import MageEyeConfig
import MageEyeConfigEditor
import MultiplierField
import NullableIntValueConfigEditor
import NullableNumberInputField
import OptionalIntField
import PandoraBoxConfig
import PandoraBoxConfigEditor
import PlayerType
import PlayerTypeSelector
import RadioButtonWithLabel
import ResourceBuildingConfig
import ResourceBuildingConfigEditor
import ResourceConfigCard
import ResourcesConfig
import RoadType
import RunesSelectionSection
import RunicChestConfig
import RunicChestConfigEditor
import ScriptBuilding
import ScriptBuildingConfig
import ScriptBuildingConfigEditor
import SealedTypeSelector
import SearchableEnumDialog
import SpellType
import SwitchWithLabel
import TierCheckbox
import TiersPoolEditor
import XdbRefEditor
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import clearSealedTypes
import getActiveSealedType
import setSealedType
import updateConfig
import kotlin.collections.plus

@Composable
fun BuildingConfigEditor(
    buildingConfigs: List<CustomBuildingConfig>,
    onConfigsUpdated: (List<CustomBuildingConfig>) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedConnectionIndex by remember {
        mutableStateOf(0)
    }
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp),
        ) {
            Button(
                onClick = {
                    val new = CustomBuildingConfig(
                        Id = (buildingConfigs.maxOfOrNull { it.Id } ?: 0) + 1
                    )
                    onConfigsUpdated(buildingConfigs + new)
                },
                enabled = true
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
                Text("Добавить строение")
            }

            FlowColumn(
                modifier = Modifier.width(200.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                buildingConfigs.forEachIndexed { index, config ->
                    BuildingConfigItem(
                        config = config,
                        isSelected = index == selectedConnectionIndex,
                        modifier = Modifier.clickable(onClick = { selectedConnectionIndex = index })
                    )
                }
            }
        }
        Divider(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )

        if (buildingConfigs.isNotEmpty())
            CustomBuildingConfigEditor(
                config = buildingConfigs[selectedConnectionIndex],
                onConfigChanged = { updated ->
                    onConfigsUpdated(
                        buildingConfigs.toMutableList().apply {
                            set(selectedConnectionIndex, updated)
                        }
                    )
                }
            )
    }
}

@Composable
fun CustomBuildingConfigEditor(
    config: CustomBuildingConfig,
    onConfigChanged: (CustomBuildingConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    var showEnumDialog by remember(config) { mutableStateOf<EnumDialogData<*>?>(null) }
    var currentSealedType by remember(config) { mutableStateOf(config.getActiveSealedType()) }

    Column(
        modifier = modifier.padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Basic fields
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("ID:", modifier = Modifier.width(100.dp))
            OutlinedTextField(
                value = config.Id.toString(),
                onValueChange = { onConfigChanged(config.copy(Id = it.toIntOrNull() ?: config.Id)) },
                modifier = Modifier.weight(1f)
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Value:", modifier = Modifier.width(100.dp))
            OutlinedTextField(
                value = config.Value.toString(),
                onValueChange = { onConfigChanged(config.copy(Value = it.toLongOrNull() ?: config.Value)) },
                modifier = Modifier.weight(1f)
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Guard Strength:", modifier = Modifier.width(100.dp))
            OutlinedTextField(
                value = config.GuardStrenght.toString(),
                onValueChange = {
                    onConfigChanged(
                        config.copy(
                            GuardStrenght = it.toLongOrNull() ?: config.GuardStrenght
                        )
                    )
                },
                modifier = Modifier.weight(1f)
            )
        }

        // Enum fields with dropdown
        EnumDropdownRow(
            label = "Building Texture:",
            currentValue = config.BuildingTexture ?: BuildingTextureConfig.DefaultDwellingByTerrain,
            values = BuildingTextureConfig.entries.toList(),
            onValueSelected = {
                onConfigChanged(config.copy(BuildingTexture = it as BuildingTextureConfig))
            },
            showDialog = {
                showEnumDialog = it
            }
        )

        EnumDropdownRow(
            label = "Road Type:",
            currentValue = config.RoadType ?: RoadType.MAINROAD,
            values = RoadType.entries.toList(),
            onValueSelected = { onConfigChanged(config.copy(RoadType = it as RoadType)) },
            showDialog = {
                showEnumDialog = it
            }
        )

        // Sealed type selector
        Text("Building Type:", style = MaterialTheme.typography.titleMedium)
        SealedTypeSelector(
            currentType = currentSealedType,
            onTypeSelected = { newType ->
                currentSealedType = newType
                onConfigChanged(config.clearSealedTypes().setSealedType(newType))
            }
        )

        // Sealed type editor
        when (val type = currentSealedType) {
            is CreatureBuildingConfig -> CreatureBuildingConfigEditor(
                config = type,
                onConfigChanged = { newConfig ->
                    onConfigChanged(config.copy(CreatureBuildingConfig = newConfig))
                }
            )

            is String -> XdbRefEditor(
                config = type,
                onConfigChanged = { newConfig ->
                    onConfigChanged(config.copy(XdbRef = newConfig))
                }
            )

            is PandoraBoxConfig -> PandoraBoxConfigEditor(
                config = type,
                onConfigChanged = { newConfig ->
                    onConfigChanged(config.copy(PandoraBoxConfig = newConfig))
                }
            )

            is ScriptBuildingConfig -> ScriptBuildingConfigEditor(
                config = type,
                onConfigChanged = { newConfig ->
                    onConfigChanged(config.copy(ScriptBuildingConfig = newConfig))
                }
            )

            is ResourceBuildingConfig -> ResourceBuildingConfigEditor(
                config = type,
                onConfigChanged = { newConfig ->
                    onConfigChanged(config.copy(ResourceBuildingConfig = newConfig))
                }
            )

            is MageEyeConfig -> MageEyeConfigEditor(
                initialConfig = type,
                onConfigChanged = { newConfig ->
                    onConfigChanged(config.copy(MageEyeConfig = newConfig))
                }
            )

            is RunicChestConfig -> RunicChestConfigEditor(
                config = type,
                onConfigChanged = { newConfig ->
                    onConfigChanged(config.copy(RunicChestConfig = newConfig))
                }
            )

            is DefaultBuildingConfig -> DefaultBuildingEditorWithDialog(
                initialConfig = type,
                onConfigChanged = { newConfig ->
                    onConfigChanged(config.copy(DefaultBuildingConfig = newConfig))
                }
            )

            is CreatureBankConfig -> CreatureBankConfigEditor(
                initialConfig = type,
                onConfigChange = { newConfig ->
                    onConfigChanged(config.copy(CreatureBankConfig = newConfig))
                }
            )
            else -> { /* No sealed type selected */ }
        }
    }

    // Enum selection dialog
    showEnumDialog?.let { dialogData ->
        SearchableEnumDialog(
            title = dialogData.title,
            items = dialogData.items,
            onDismiss = { showEnumDialog = null },
            onItemSelected = { selected ->
                dialogData.onSelected(selected)
                showEnumDialog = null
            }
        )
    }
}


@Composable
fun CreatureBankConfigEditor(
    initialConfig: CreatureBankConfig,
    onConfigChange: (CreatureBankConfig) -> Unit
) {
    var config by remember { mutableStateOf(initialConfig) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Creature Bank Config Editor",
            style = MaterialTheme.typography.headlineSmall
        )

        // Name field
        OutlinedTextField(
            value = config.Name,
            onValueChange = { newName ->
                config = config.copy(Name = newName)
                onConfigChange(config)
            },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        // Creatures Pool
        Text("Creatures Pool", style = MaterialTheme.typography.titleMedium)
        FlowColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            config.CreaturesPool.forEachIndexed { index, creature ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = creature,
                        onValueChange = { newValue ->
                            val newList = config.CreaturesPool.toMutableList()
                            newList[index] = newValue
                            config = config.copy(CreaturesPool = newList)
                            onConfigChange(config)
                        },
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = {
                            val newList = config.CreaturesPool.toMutableList()
                            newList.removeAt(index)
                            config = config.copy(CreaturesPool = newList)
                            onConfigChange(config)
                        }
                    ) {
                        Text("X")
                    }
                }
            }
        }
        Button(
            onClick = {
                config = config.copy(CreaturesPool = config.CreaturesPool + "")
                onConfigChange(config)
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Add Creature")
        }

        // Guards Pool
        Text("Guards Pool", style = MaterialTheme.typography.titleMedium)
        LazyColumn(
            modifier = Modifier
                .heightIn(max = 200.dp)
                .fillMaxWidth()
        ) {
            itemsIndexed(config.GuardsPool) { guardIndex, guards ->
                Column {
                    Text("Guard Group ${guardIndex + 1}")
                    guards.forEachIndexed { index, guard ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = guard,
                                onValueChange = { newValue ->
                                    val newGuardsPool = config.GuardsPool.toMutableList()
                                    newGuardsPool[guardIndex] = guards.toMutableList().apply {
                                        set(index, newValue)
                                    }
                                    config = config.copy(GuardsPool = newGuardsPool)
                                    onConfigChange(config)
                                },
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = {
                                    val newGuardsPool = config.GuardsPool.toMutableList()
                                    newGuardsPool[guardIndex] = guards.toMutableList().apply {
                                        removeAt(index)
                                    }
                                    config = config.copy(GuardsPool = newGuardsPool)
                                    onConfigChange(config)
                                }
                            ) {
                                Text("X")
                            }
                        }
                    }
                    Button(
                        onClick = {
                            val newGuardsPool = config.GuardsPool.toMutableList()
                            newGuardsPool[guardIndex] = guards + ""
                            config = config.copy(GuardsPool = newGuardsPool)
                            onConfigChange(config)
                        }
                    ) {
                        Text("Add Guard to Group ${guardIndex + 1}")
                    }
                }
            }
        }
        Button(
            onClick = {
                config = config.copy(GuardsPool = config.GuardsPool + listOf(emptyList()))
                onConfigChange(config)
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Add Guard Group")
        }

        // Multiplier fields
        MultiplierField(
            label = "Creature Cost Multiplier",
            value = config.CreatureCostMultiplier,
            onValueChange = { newValue ->
                config = config.copy(CreatureCostMultiplier = newValue)
                onConfigChange(config)
            }
        )
        MultiplierField(
            label = "Creature Resources Multiplier",
            value = config.CreatureResourcesMultiplier,
            onValueChange = { newValue ->
                config = config.copy(CreatureResourcesMultiplier = newValue)
                onConfigChange(config)
            }
        )
        MultiplierField(
            label = "Creature Grow Multiplier",
            value = config.CreatureGrowMultiplier,
            onValueChange = { newValue ->
                config = config.copy(CreatureGrowMultiplier = newValue)
                onConfigChange(config)
            }
        )
        MultiplierField(
            label = "Guard Grow Multiplier",
            value = config.GuardGrowMultiplier,
            onValueChange = { newValue ->
                config = config.copy(GuardGrowMultiplier = newValue)
                onConfigChange(config)
            }
        )
    }
}

@Composable
private fun MultiplierField(
    label: String,
    value: Double?,
    onValueChange: (Double?) -> Unit
) {
    OutlinedTextField(
        value = value?.toString() ?: "",
        onValueChange = { newValue ->
            onValueChange(newValue.toDoubleOrNull())
        },
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun DefaultBuildingPicker(
    selectedBuilding: DefaultBuilding,
    onBuildingSelected: (DefaultBuilding) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        OutlinedButton(
            onClick = { showDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = selectedBuilding.description,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Выбрать здание")
        }

        Text(
            text = "ID: ${selectedBuilding.number}",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(top = 4.dp)
        )
    }

    if (showDialog) {
        BuildingSelectionDialog(
            selectedBuilding = selectedBuilding,
            onDismiss = { showDialog = false },
            onBuildingSelected = {
                onBuildingSelected(it)
                showDialog = false
            }
        )
    }
}

@Composable
private fun BuildingSelectionDialog(
    selectedBuilding: DefaultBuilding,
    onDismiss: () -> Unit,
    onBuildingSelected: (DefaultBuilding) -> Unit
) {
    val buildings = remember { DefaultBuilding.values().toList() }
    var searchQuery by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Выберите здание") },
        text = {
            Column {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Поиск...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )

                val filteredBuildings = buildings.filter {
                    it.description.contains(searchQuery, ignoreCase = true) ||
                            it.name.contains(searchQuery, ignoreCase = true)
                }

                LazyColumn (
                    modifier = Modifier.heightIn(max = 400.dp)//.verticalScroll(rememberScrollState())
                ) {
                    items(filteredBuildings) { building ->
                        BuildingDialogItem(
                            building = building,
                            isSelected = building == selectedBuilding,
                            onSelected = { onBuildingSelected(building) }
                        )
                    }
                }
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text("Отмена")
            }
        },
        confirmButton = {}
    )
}

@Composable
private fun BuildingDialogItem(
    building: DefaultBuilding,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface,
        onClick = onSelected
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${building.number}.",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.width(32.dp)
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = building.description,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = building.name,
                    style = MaterialTheme.typography.labelSmall
                )
            }
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Выбрано",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

// Пример использования
@Composable
fun DefaultBuildingEditorWithDialog(
    initialConfig: DefaultBuildingConfig,
    onConfigChanged: (DefaultBuildingConfig) -> Unit
) {
    var currentBuilding by remember { mutableStateOf(initialConfig.DefaultBuilding) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Настройка здания по умолчанию",
            style = MaterialTheme.typography.headlineSmall
        )

        DefaultBuildingPicker(
            selectedBuilding = currentBuilding,
            onBuildingSelected = {
                currentBuilding = it
                onConfigChanged(DefaultBuildingConfig(it))
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Дополнительные элементы UI если нужно
    }
}

@Composable
fun RunicChestConfigEditor(
    config: RunicChestConfig,
    onConfigChanged: (RunicChestConfig) -> Unit,
    modifier: Modifier = Modifier
) {

    FlowColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Runic Chest Configuration", style = MaterialTheme.typography.headlineMedium)
        RunesSelectionSection(
            selectedRunes = config.Runes,
            onRunesChanged = { newRunes ->
                onConfigChanged(config.copy(Runes = newRunes))
            }
        )

        Text("тиры рун которые могут выпасть в коробке", style = MaterialTheme.typography.headlineSmall)

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            for (tier in 1L..3L) {
                TierCheckbox(
                    tier = tier,
                    checked = config.RuneTiers.contains(tier),
                    onCheckedChange = { checked ->
                        onConfigChanged(
                            if (checked)
                                config.copy(RuneTiers = config.RuneTiers + tier)
                            else
                                config.copy(RuneTiers = config.RuneTiers - tier)
                        )
                    }
                )
            }
        }

        OptionalIntField(
            label = "Count",
            value = config.Count,
            onValueChanged = { newCount ->
                onConfigChanged(config.copy(Count = newCount))
            }
        )

        OptionalIntField(
            label = "Experience Amount",
            value = config.ExpAmount,
            onValueChanged = { newExp ->
                onConfigChanged(config.copy(ExpAmount = newExp))
            }
        )
    }
}

@Composable
private fun RunesSelectionSection(
    selectedRunes: List<SpellType>,
    onRunesChanged: (List<SpellType>) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Selected Runes", style = MaterialTheme.typography.titleMedium)

            // Display selected runes as chips
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (selectedRunes.isEmpty()) {
                    Text("No runes selected", style = MaterialTheme.typography.bodySmall)
                } else {
                    selectedRunes.forEach { rune ->
                        FilterChip(
                            selected = true,
                            onClick = {
                                onRunesChanged(selectedRunes - rune)
                            },
                            label = { Text(rune.description) },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove"
                                )
                            }
                        )
                    }
                }
            }

            // Button to add more runes
            Button(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Runes")
            }

            // Dropdown menu for selecting runes
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                SpellType.entries.filter { it.description.contains("Руна") }.forEach { rune ->
                    DropdownMenuItem(
                        text = { Text(rune.description) },
                        onClick = {
                            if (rune !in selectedRunes) {
                                onRunesChanged(selectedRunes + rune)
                            }
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun RuneTiersSection(
    runeTiers: List<Long>,
    onTiersChanged: (List<Long>) -> Unit
) {
    Card {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Rune Tiers", style = MaterialTheme.typography.titleMedium)

            if (runeTiers.isEmpty()) {
                Text("No tiers specified", style = MaterialTheme.typography.bodySmall)
            } else {
                runeTiers.forEachIndexed { index, tier ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Tier ${index + 1}")

                        var tierText by remember { mutableStateOf(tier.toString()) }
                        TextField(
                            value = tierText,
                            onValueChange = { newValue ->
                                if (newValue.all { it.isDigit() }) {
                                    tierText = newValue
                                    val newTiers = runeTiers.toMutableList()
                                    newTiers[index] = newValue.toLongOrNull() ?: tier
                                    onTiersChanged(newTiers)
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )

                        IconButton(
                            onClick = {
                                val newTiers = runeTiers.toMutableList()
                                newTiers.removeAt(index)
                                onTiersChanged(newTiers)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Remove tier"
                            )
                        }
                    }
                }
            }

            Button(
                onClick = {
                    onTiersChanged(runeTiers + 1L) // Add new tier with default value 1
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Tier")
            }
        }
    }
}

@Composable
private fun OptionalIntField(
    label: String,
    value: Int?,
    onValueChanged: (Int?) -> Unit
) {
    var textValue by remember { mutableStateOf(value?.toString() ?: "") }
    var isEnabled by remember { mutableStateOf(value != null) }

    Card {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Checkbox(
                    checked = isEnabled,
                    onCheckedChange = { checked ->
                        isEnabled = checked
                        if (!checked) {
                            onValueChanged(null)
                        } else {
                            onValueChanged(textValue.toIntOrNull() ?: 0)
                        }
                    }
                )
                Text(label, style = MaterialTheme.typography.titleMedium)
            }

            if (isEnabled) {
                TextField(
                    value = textValue,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() }) {
                            textValue = newValue
                            onValueChanged(newValue.toIntOrNull())
                        }
                    },
                    label = { Text("Enter $label") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isEnabled
                )
            }
        }
    }
}


@Composable
fun MageEyeConfigEditor(
    initialConfig: MageEyeConfig,
    onConfigChanged: (MageEyeConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    var coordinateX by remember { mutableStateOf(TextFieldValue(initialConfig.CoordinateX.toString())) }
    var coordinateY by remember { mutableStateOf(TextFieldValue(initialConfig.CoordinateY.toString())) }
    var radius by remember { mutableStateOf(TextFieldValue(initialConfig.Radius?.toString() ?: "")) }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Mage Eye Configuration",
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = coordinateX,
            onValueChange = { newValue ->
                coordinateX = newValue
                updateConfig(coordinateX, coordinateY, radius, onConfigChanged)
            },
            label = { Text("Coordinate X") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )

        OutlinedTextField(
            value = coordinateY,
            onValueChange = { newValue ->
                coordinateY = newValue
                updateConfig(coordinateX, coordinateY, radius, onConfigChanged)
            },
            label = { Text("Coordinate Y") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )

        OutlinedTextField(
            value = radius,
            onValueChange = { newValue ->
                radius = newValue
                updateConfig(coordinateX, coordinateY, radius, onConfigChanged)
            },
            label = { Text("Radius (optional)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
    }
}

private fun updateConfig(
    xText: TextFieldValue,
    yText: TextFieldValue,
    radiusText: TextFieldValue,
    onConfigChanged: (MageEyeConfig) -> Unit
) {
    val x = xText.text.toLongOrNull()
    val y = yText.text.toLongOrNull()
    val radius = radiusText.text.toIntOrNull()

    if (x != null && y != null) {
        onConfigChanged(MageEyeConfig(x, y, radius))
    }
}

@Composable
fun ResourceBuildingConfigEditor(
    config: ResourceBuildingConfig,
    onConfigChanged: (ResourceBuildingConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentConfig by remember { mutableStateOf(config) }

    Column(modifier = modifier.padding(16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Resource Building Configuration",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            IconButton(
                onClick = {
                    val newConfig = ResourcesConfig(
                        Wood = IntValueConfig(null, null),
                        Ore = IntValueConfig(null, null),
                        Mercury = IntValueConfig(null, null),
                        Crystals = IntValueConfig(null, null),
                        Sulfur = IntValueConfig(null, null),
                        Gems = IntValueConfig(null, null),
                        Gold = IntValueConfig(null, null)
                    )
                    currentConfig = currentConfig.copy(
                        ResourcesConfigs = currentConfig.ResourcesConfigs + newConfig
                    )
                    onConfigChanged(currentConfig)
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add new resource configuration")
            }
        }

        FlowColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            currentConfig.ResourcesConfigs.forEach { resourceConfig ->
                ResourceConfigCard(
                    resourceConfig = resourceConfig,
                    onConfigChanged = { updated ->
                        val updatedList = currentConfig.ResourcesConfigs.toMutableList().apply {
                            set(indexOf(resourceConfig), updated)
                        }
                        currentConfig = currentConfig.copy(ResourcesConfigs = updatedList)
                        onConfigChanged(currentConfig)
                    }
                )
            }
        }
    }
}

@Composable
fun ResourceConfigCard(
    resourceConfig: ResourcesConfig,
    onConfigChanged: (ResourcesConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Resource Configuration",
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (expanded) "Collapse" else "Expand"
                    )
                }
            }

            if (expanded) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    NullableIntValueConfigEditor(
                        label = "Wood",
                        config = resourceConfig.Wood,
                        onConfigChanged = { newValue ->
                            onConfigChanged(resourceConfig.copy(Wood = newValue))
                        }
                    )

                    NullableIntValueConfigEditor(
                        label = "Ore",
                        config = resourceConfig.Ore,
                        onConfigChanged = { newValue ->
                            onConfigChanged(resourceConfig.copy(Ore = newValue))
                        }
                    )

                    NullableIntValueConfigEditor(
                        label = "Mercury",
                        config = resourceConfig.Mercury,
                        onConfigChanged = { newValue ->
                            onConfigChanged(resourceConfig.copy(Mercury = newValue))
                        }
                    )

                    NullableIntValueConfigEditor(
                        label = "Crystals",
                        config = resourceConfig.Crystals,
                        onConfigChanged = { newValue ->
                            onConfigChanged(resourceConfig.copy(Crystals = newValue))
                        }
                    )

                    NullableIntValueConfigEditor(
                        label = "Sulfur",
                        config = resourceConfig.Sulfur,
                        onConfigChanged = { newValue ->
                            onConfigChanged(resourceConfig.copy(Sulfur = newValue))
                        }
                    )

                    NullableIntValueConfigEditor(
                        label = "Gems",
                        config = resourceConfig.Gems,
                        onConfigChanged = { newValue ->
                            onConfigChanged(resourceConfig.copy(Gems = newValue))
                        }
                    )

                    NullableIntValueConfigEditor(
                        label = "Gold",
                        config = resourceConfig.Gold,
                        onConfigChanged = { newValue ->
                            onConfigChanged(resourceConfig.copy(Gold = newValue))
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScriptBuildingConfigEditor(
    config: ScriptBuildingConfig,
    onConfigChanged: (ScriptBuildingConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedBuilding by remember { mutableStateOf(config.ScriptBuilding) }

    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "Script Building Configuration",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                readOnly = true,
                value = selectedBuilding.name,
                onValueChange = {},
                label = { Text("Building Type") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                ScriptBuilding.values().forEach { building ->
                    DropdownMenuItem(
                        text = { Text(building.name) },
                        onClick = {
                            selectedBuilding = building
                            expanded = false
                            onConfigChanged(ScriptBuildingConfig(building))
                        }
                    )
                }
            }
        }

        Text(
            text = "Description: ${selectedBuilding.description}",
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            text = "Number: ${selectedBuilding.number}",
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun BuildingConfigItem(
    config: CustomBuildingConfig,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(if (isSelected) 8.dp else 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        border = if (isSelected) {
            BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        } else {
            null
        }
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                "Building #${config.Id}",
                style = MaterialTheme.typography.labelMedium,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Value " + config.Value.toString(),
                color = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "GuardStrenght " + config.GuardStrenght.toString(),
                color = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
}

@Composable
private fun <T : Enum<*>> EnumDropdownRow(
    label: String,
    currentValue: T,
    values: List<T>,
    onValueSelected: (Enum<*>) -> Unit,
    showDialog: (EnumDialogData<T>) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, modifier = Modifier.width(100.dp))
        OutlinedButton(
            onClick = {
                showDialog(
                    EnumDialogData(
                        title = label.trimEnd(':'),
                        items = values,
                        currentSelected = currentValue,
                        onSelected = onValueSelected
                    )
                )
            },
            modifier = Modifier.weight(1f)
        ) {
            Text(currentValue.toString(), modifier = Modifier.weight(1f))
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
        }
    }
}

@Composable
private fun SealedTypeSelector(
    currentType: Any?,
    onTypeSelected: (Any?) -> Unit
) {
    val types = listOf(
        "Creature Building" to CreatureBuildingConfig(
            TiersPool = emptyList(),
            PlayerType = PlayerType.ANY,
            CreatureIds = emptyList()
        ),
        "Xdb Reference" to "",
        "Pandora Box" to PandoraBoxConfig(
            GoldAmount = emptyList(),
            ExpAmount = emptyList(),
            Artifacts = emptyList(),
            PandoraCreatureConfig = emptyList(),
            Spells = emptyList(),
            Resources = emptyList()
        ),
        "Script Building" to ScriptBuildingConfig(ScriptBuilding.TowerPortal),
        "Resource Building" to ResourceBuildingConfig(emptyList()),
        "Mage Eye" to MageEyeConfig(0, 0),
        "Runic Chest" to RunicChestConfig(emptyList(), emptyList()),
        "Default Building" to DefaultBuildingConfig(DefaultBuilding.GoldChest5k),
        "Creature Bank" to CreatureBankConfig("", emptyList(), emptyList()),
        "None" to null
    )

    var expanded by remember { mutableStateOf(false) }
    val selectedLabel = types.find { it.second == currentType }?.first ?: "None"

    Box(modifier = Modifier.wrapContentSize()) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(selectedLabel)
            Icon(
                imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                contentDescription = null
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            types.forEach { (label, type) ->
                DropdownMenuItem(
                    onClick = {
                        onTypeSelected(type)
                        expanded = false
                    },
                    text = { Text(label) }
                )
            }
        }
    }
}

@Composable
fun CreatureBuildingConfigEditor(
    config: CreatureBuildingConfig,
    onConfigChanged: (CreatureBuildingConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Creature Building Configuration", style = MaterialTheme.typography.headlineMedium)
        TiersPoolEditor(
            tiers = config.TiersPool,
            onTiersChanged = { newTiers ->
                onConfigChanged(config.copy(TiersPool = newTiers))
            }
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Filters", style = MaterialTheme.typography.titleMedium)

            // Boolean switches
            SwitchWithLabel(
                label = "No Grades",
                checked = config.NoGrades ?: false,
                onCheckedChange = { checked ->
                    onConfigChanged(config.copy(NoGrades = checked))
                }
            )

            SwitchWithLabel(
                label = "Grades",
                checked = config.Grades ?: false,
                onCheckedChange = { checked ->
                    onConfigChanged(config.copy(Grades = checked))
                }
            )

            SwitchWithLabel(
                label = "Neutrals",
                checked = config.Neutrals ?: false,
                onCheckedChange = { checked ->
                    onConfigChanged(config.copy(Neutrals = checked))
                }
            )

            SwitchWithLabel(
                label = "Non-Player Factions",
                checked = config.NonPLayerFactions ?: false,
                onCheckedChange = { checked ->
                    onConfigChanged(config.copy(NonPLayerFactions = checked))
                }
            )

            SwitchWithLabel(
                label = "Player Factions",
                checked = config.PLayerFactions ?: false,
                onCheckedChange = { checked ->
                    onConfigChanged(config.copy(PLayerFactions = checked))
                }
            )
        }
        PlayerTypeSelector(
            playerType = config.PlayerType,
            onPlayerTypeChanged = { newType ->
                onConfigChanged(config.copy(PlayerType = newType))
            }
        )

        CreatureIdsEditor(
            creatureIds = config.CreatureIds,
            onCreatureIdsChanged = { newIds ->
                onConfigChanged(config.copy(CreatureIds = newIds))
            }
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Multipliers", style = MaterialTheme.typography.titleMedium)

            NullableNumberInputField(
                label = "Cost Multiplier",
                value = config.CostMultiplier,
                onValueChange = { newValue ->
                    onConfigChanged(config.copy(CostMultiplier = newValue))
                }
            )
            NullableNumberInputField(
                label = "Resources Multiplier",
                value = config.ResourcesMultiplier,
                onValueChange = { newValue ->
                    onConfigChanged(config.copy(ResourcesMultiplier = newValue))
                }
            )

            NullableNumberInputField(
                label = "Grow Multiplier",
                value = config.GrowMultiplier,
                onValueChange = { newValue ->
                    onConfigChanged(config.copy(GrowMultiplier = newValue))
                }
            )

            NullableNumberInputField(
                label = "Grow Multiplier",
                value = config.GrowMultiplier,
                onValueChange = { newValue ->
                    onConfigChanged(config.copy(GrowMultiplier = newValue))
                }
            )
        }

        SwitchWithLabel(
            label = "Is Dwelling",
            checked = config.IsDwelling ?: false,
            onCheckedChange = { checked ->
                onConfigChanged(config.copy(IsDwelling = checked))
            }
        )
    }
}

@Composable
private fun TiersPoolEditor(
    tiers: List<Long>,
    onTiersChanged: (List<Long>) -> Unit,
    modifier: Modifier = Modifier
) {
    var newTier by remember { mutableStateOf("") }

    Column(modifier = modifier) {
        Text("Tiers Pool", style = MaterialTheme.typography.titleMedium)

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = newTier,
                onValueChange = { newTier = it },
                label = { Text("Add tier") },
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = {
                    val tier = newTier.toLongOrNull()
                    if (tier != null) {
                        onTiersChanged(tiers + tier)
                        newTier = ""
                    }
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Add")
            }
        }

        if (tiers.isNotEmpty()) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                tiers.forEach { tier ->
                    Chip(
                        onClick = { onTiersChanged(tiers - tier) },
                        label = { Text(tier.toString()) },
                        trailingIcon = { Icon(Icons.Default.Close, contentDescription = "Remove") }
                    )
                }
            }
        }
    }
}

@Composable
private fun PlayerTypeSelector(
    playerType: PlayerType,
    onPlayerTypeChanged: (PlayerType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text("Player Type", style = MaterialTheme.typography.titleMedium)

        PlayerType.values().forEach { type ->
            RadioButtonWithLabel(
                text = type.description,
                selected = playerType == type,
                onSelect = { onPlayerTypeChanged(type) }
            )
        }
    }
}

@Composable
private fun CreatureIdsEditor(
    creatureIds: List<String>,
    onCreatureIdsChanged: (List<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    var newCreatureId by remember { mutableStateOf("") }

    Column(modifier = modifier) {
        Text("Creature IDs", style = MaterialTheme.typography.titleMedium)

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = newCreatureId,
                onValueChange = { newCreatureId = it },
                label = { Text("Add creature ID") },
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = {
                    if (newCreatureId.isNotBlank()) {
                        onCreatureIdsChanged(creatureIds + newCreatureId)
                        newCreatureId = ""
                    }
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Add")
            }
        }

        if (creatureIds.isNotEmpty()) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                creatureIds.forEach { id ->
                    Chip(
                        onClick = { onCreatureIdsChanged(creatureIds - id) },
                        label = { Text(id) },
                        trailingIcon = { Icon(Icons.Default.Close, contentDescription = "Remove") }
                    )
                }
            }
        }
    }
}

@Composable
private fun SwitchWithLabel(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text = label, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun RadioButtonWithLabel(
    text: String,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth().clickable(onClick = onSelect)
    ) {
        RadioButton(selected = selected, onClick = null)
        Text(text = text, modifier = Modifier.padding(start = 8.dp))
    }
}

// Simple Chip component implementation
@Composable
private fun Chip(
    onClick: () -> Unit,
    label: @Composable () -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surfaceVariant,
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            label()
            trailingIcon?.invoke()
        }
    }
}

@Composable
private fun XdbRefEditor(
    config: String,
    onConfigChanged: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Xdb Reference", style = MaterialTheme.typography.titleSmall)

        OutlinedTextField(
            value = config,
            onValueChange = { onConfigChanged(it) },
            label = { Text("Reference Value") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// Implement similar editor components for other sealed types...

// Dialog data class
private data class EnumDialogData<T : Enum<*>>(
    val title: String,
    val items: List<T>,
    val currentSelected: T,
    val onSelected: (Enum<*>) -> Unit
)

// Searchable enum dialog
@Composable
private fun SearchableEnumDialog(
    title: String,
    items: List<Enum<*>>,
    onDismiss: () -> Unit,
    onItemSelected: (Enum<*>) -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    val filteredItems = items.filter { it.toString().contains(searchText, ignoreCase = true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    label = { Text("Search") },
                    modifier = Modifier.fillMaxWidth()
                )

                FlowColumn(modifier = Modifier.heightIn(max = 400.dp)) {
                    filteredItems.forEach { item ->
                        ListItem(
                            headlineContent = { Text(item.toString()) },
                            modifier = Modifier.clickable {
                                onItemSelected(item)
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

// Extension functions for CustomBuildingConfig
private fun CustomBuildingConfig.getActiveSealedType(): Any? {
    return listOfNotNull(
        CreatureBuildingConfig,
        XdbRef,
        PandoraBoxConfig,
        ScriptBuildingConfig,
        ResourceBuildingConfig,
        MageEyeConfig,
        RunicChestConfig,
        DefaultBuildingConfig,
        CreatureBankConfig
    ).firstOrNull()
}

private fun CustomBuildingConfig.clearSealedTypes(): CustomBuildingConfig {
    return this.copy(
        CreatureBuildingConfig = null,
        XdbRef = null,
        PandoraBoxConfig = null,
        ScriptBuildingConfig = null,
        ResourceBuildingConfig = null,
        MageEyeConfig = null,
        RunicChestConfig = null,
        DefaultBuildingConfig = null,
        CreatureBankConfig = null
    )
}

private fun CustomBuildingConfig.setSealedType(type: Any?): CustomBuildingConfig {
    if (type == null) return this

    return when (type) {
        is CreatureBuildingConfig -> copy(CreatureBuildingConfig = type)
        is String -> copy(XdbRef = type)
        is PandoraBoxConfig -> copy(PandoraBoxConfig = type)
        is ScriptBuildingConfig -> copy(ScriptBuildingConfig = type)
        is ResourceBuildingConfig -> copy(ResourceBuildingConfig = type)
        is MageEyeConfig -> copy(MageEyeConfig = type)
        is RunicChestConfig -> copy(RunicChestConfig = type)
        is DefaultBuildingConfig -> copy(DefaultBuildingConfig = type)
        is CreatureBankConfig -> copy(CreatureBankConfig = type)
        else -> this
    }
}
