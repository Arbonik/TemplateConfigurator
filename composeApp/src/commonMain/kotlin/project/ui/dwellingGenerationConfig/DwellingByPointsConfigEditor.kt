import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun DwellingByPointsConfigScreen(
    config: DwellingByPointsConfig,
    onConfigChanged: (DwellingByPointsConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SectionTitle("General Settings")
        NumberInput(
            label = "Points Count",
            value = config.PointsCount,
            onValueChange = { onConfigChanged(config.copy(PointsCount = it.toLong())) }
        )

        _root_ide_package_.project.ui.dwellingGenerationConfig.DwellingValueEditor(
            value = config.DwellingPoints ?: DwellingValue(),
            onValueChanged = { onConfigChanged(config.copy(DwellingPoints = it)) },
        )



        TierRangeInput(
            minTiers = config.MinTiersCount,
            maxTiers = config.MaxTiersCount,
            onMinChange = { onConfigChanged(config.copy(MinTiersCount = it)) },
            onMaxChange = { onConfigChanged(config.copy(MaxTiersCount = it)) }
        )

        AllowedTiersInput(
            AllowedTiers = config.AllowedTiers,
            onConfigChanged = { onConfigChanged(config.copy(AllowedTiers = it)) }
        )

        SectionTitle("Global Dwelling Points")
        _root_ide_package_.project.ui.dwellingGenerationConfig.DwellingValueEditor(
            value = config.DwellingPoints ?: DwellingValue(),
            onValueChanged = { onConfigChanged(config.copy(DwellingPoints = it)) }
        )

        SectionTitle("Per-Tier Limits")
        _root_ide_package_.project.ui.dwellingGenerationConfig.DwellingValueEditor(
            value = config.MinCountPerTier ?: DwellingValue(),
            onValueChanged = { onConfigChanged(config.copy(MinCountPerTier = it)) },
            label = "Min Count",
            modifier = Modifier.padding(bottom = 8.dp)
        )

        _root_ide_package_.project.ui.dwellingGenerationConfig.DwellingValueEditor(
            value = config.MaxCountPerTier ?: DwellingValue(),
            onValueChanged = { onConfigChanged(config.copy(MaxCountPerTier = it)) },
            label = "Max Count",
            modifier = Modifier.padding(bottom = 8.dp)
        )

        SectionTitle("Faction-Specific Settings")
        FactionSpecificEditors(
            factions = _root_ide_package_.project.data.enums.CastleType.entries.map { it.name },
            dwellingPoints = config.DwellingPointsByFaction,
            minCounts = config.MinCountPerTierByFaction,
            maxCounts = config.MaxCountPerTierByFaction,
            onDwellingPointsChange = { onConfigChanged(config.copy(DwellingPointsByFaction = it)) },
            onMinCountsChange = { onConfigChanged(config.copy(MinCountPerTierByFaction = it)) },
            onMaxCountsChange = { onConfigChanged(config.copy(MaxCountPerTierByFaction = it)) }
        )
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun NumberInput(
    label: String,
    value: Number,
    onValueChange: (Number) -> Unit,
    modifier: Modifier = Modifier,
) {
    var textValue by remember { mutableStateOf(value.toString()) }

    OutlinedTextField(
        value = textValue,
        onValueChange = {
            textValue = it
            it.toLongOrNull()?.let { num -> onValueChange(num) }
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun NumberInputNullable(
    label: String,
    value: Number?,
    onValueChange: (Number?) -> Unit,
    modifier: Modifier = Modifier,
) {
    var textValue by remember { mutableStateOf(value?.toString()     ?: "") }

    OutlinedTextField(
        value = textValue,
        onValueChange = {
            textValue = it
            onValueChange(it.toLongOrNull())
        },
        label = { Text(if (value != null) label else "(null) $label") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier.fillMaxWidth()
    )
}


@Composable
private fun TierRangeInput(
    minTiers: Int?,
    maxTiers: Int?,
    onMinChange: (Int?) -> Unit,
    onMaxChange: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        TierNumberInput(
            value = minTiers,
            onValueChange = onMinChange,
            label = "Min Tiers",
            modifier = Modifier.weight(1f)
        )

        TierNumberInput(
            value = maxTiers,
            onValueChange = onMaxChange,
            label = "Max Tiers",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TierNumberInput(
    value: Int?,
    onValueChange: (Int?) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    var textValue by remember { mutableStateOf(value?.toString() ?: "") }

    OutlinedTextField(
        value = textValue,
        onValueChange = {
            textValue = it
            onValueChange(if (it.isEmpty()) null else it.toIntOrNull())
        },
        label = { Text(label) },
        placeholder = { Text("Optional") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier
    )
}

@Composable
private fun AllowedTiersInput(
    AllowedTiers: List<Long>,
    onConfigChanged: (List<Long>) -> Unit,
) {
    // Allowed Tiers editor - теперь как 7 чекбоксов
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Allowed Tiers (select available tiers)",
                style = MaterialTheme.typography.titleMedium
            )

            // 7 чекбоксов в две строки (4 + 3)
            Column {
                // Первая строка: 4 чекбокса
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (tier in 1L..4L) {
                        TierCheckbox(
                            tier = tier,
                            checked = AllowedTiers.contains(tier),
                            onCheckedChange = { checked ->
                                onConfigChanged(
                                    if (checked)
                                        AllowedTiers + tier
                                    else
                                        AllowedTiers - tier
                                )
                            }
                        )
                    }
                }

                // Вторая строка: 3 чекбокса
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (tier in 5L..7L) {
                        TierCheckbox(
                            tier = tier,
                            checked = AllowedTiers.contains(tier),
                            onCheckedChange = { checked ->
                                onConfigChanged(
                                    if (checked)
                                        AllowedTiers + tier
                                    else
                                        AllowedTiers - tier
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FactionSpecificEditors(
    factions: List<String>,
    dwellingPoints: Map<String, DwellingValue>,
    minCounts: Map<String, DwellingValue>,
    maxCounts: Map<String, DwellingValue>,
    onDwellingPointsChange: (Map<String, DwellingValue>) -> Unit,
    onMinCountsChange: (Map<String, DwellingValue>) -> Unit,
    onMaxCountsChange: (Map<String, DwellingValue>) -> Unit,
    modifier: Modifier = Modifier
) {
    var expandedFaction by remember { mutableStateOf<String?>(null) }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    ) {
        factions.forEach { faction ->
            Card(
                onClick = { expandedFaction = if (expandedFaction == faction) null else faction },
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = faction,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Icon(
                            imageVector = if (expandedFaction == faction)
                                Icons.Default.ExpandLess
                            else
                                Icons.Default.ExpandMore,
                            contentDescription = null
                        )
                    }

                    if (expandedFaction == faction) {
                        Spacer(modifier = Modifier.height(12.dp))
                        _root_ide_package_.project.ui.dwellingGenerationConfig.DwellingValueEditor(
                            value = dwellingPoints[faction] ?: DwellingValue(),
                            onValueChanged = {
                                onDwellingPointsChange(dwellingPoints + (faction to it))
                            },
                            label = "Dwelling Points",
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        _root_ide_package_.project.ui.dwellingGenerationConfig.DwellingValueEditor(
                            value = minCounts[faction] ?: DwellingValue(),
                            onValueChanged = {
                                onMinCountsChange(minCounts + (faction to it))
                            },
                            label = "Min Count",
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        _root_ide_package_.project.ui.dwellingGenerationConfig.DwellingValueEditor(
                            value = maxCounts[faction] ?: DwellingValue(),
                            onValueChanged = {
                                onMaxCountsChange(maxCounts + (faction to it))
                            },
                            label = "Max Count",
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                }
            }
        }
    }
}