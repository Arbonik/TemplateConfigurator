import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp


@Composable
fun RandomDwellingConfigEditor(
    zoneId: Int,
    config: RandomDwellingConfig,
    onConfigChanged: (RandomDwellingConfig) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Заголовок с иконкой
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.HomeWork,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "Random Dwelling Configuration",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Основные параметры
        ElevatedCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "Main Parameters",
                    style = MaterialTheme.typography.titleMedium
                )

                // Min/Max Count
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = config.MinCount.toString(),
                        onValueChange = {
                            onConfigChanged(config.copy(MinCount = it.toIntOrNull() ?: 0))
                        },
                        label = { Text("Min Count") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )

                    OutlinedTextField(
                        value = config.MaxCount.toString(),
                        onValueChange = {
                            onConfigChanged(config.copy(MaxCount = it.toIntOrNull() ?: 0))
                        },
                        label = { Text("Max Count") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )
                }

                // Tiers Count (nullable)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = config.MinTiersCount?.toString() ?: "",
                        onValueChange = {
                            onConfigChanged(
                                config.copy(
                                    MinTiersCount = it.toIntOrNull()
                                )
                            )
                        },
                        label = { Text("Min Tiers Count (optional)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        trailingIcon = {
                            if (config.MinTiersCount != null) {
                                IconButton(onClick = {
                                    onConfigChanged(config.copy(MinTiersCount = null))
                                }) {
                                    Icon(Icons.Default.Clear, null)
                                }
                            }
                        }
                    )

                    OutlinedTextField(
                        value = config.MaxTiersCount?.toString() ?: "",
                        onValueChange = {
                            onConfigChanged(
                                config.copy(
                                    MaxTiersCount = it.toIntOrNull()
                                )
                            )
                        },
                        label = { Text("Max Tiers Count (optional)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        trailingIcon = {
                            if (config.MaxTiersCount != null) {
                                IconButton(onClick = {
                                    onConfigChanged(config.copy(MaxTiersCount = null))
                                }) {
                                    Icon(Icons.Default.Clear, null)
                                }
                            }
                        }
                    )
                }

                // Count per Tier (nullable)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = config.MinCountPerTier?.toString() ?: "",
                        onValueChange = {
                            onConfigChanged(
                                config.copy(
                                    MinCountPerTier = it.toIntOrNull()
                                )
                            )
                        },
                        label = { Text("Min Count Per Tier (optional)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        trailingIcon = {
                            if (config.MinCountPerTier != null) {
                                IconButton(onClick = {
                                    onConfigChanged(config.copy(MinCountPerTier = null))
                                }) {
                                    Icon(Icons.Default.Clear, null)
                                }
                            }
                        }
                    )

                    OutlinedTextField(
                        value = config.MaxCountPerTier?.toString() ?: "",
                        onValueChange = {
                            onConfigChanged(
                                config.copy(
                                    MaxCountPerTier = it.toIntOrNull()
                                )
                            )
                        },
                        label = { Text("Max Count Per Tier (optional)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        trailingIcon = {
                            if (config.MaxCountPerTier != null) {
                                IconButton(onClick = {
                                    onConfigChanged(config.copy(MaxCountPerTier = null))
                                }) {
                                    Icon(Icons.Default.Clear, null)
                                }
                            }
                        }
                    )
                }

                // Uniform Distribution switch
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Switch(
                        checked = config.UniformDistribution ?: false,
                        onCheckedChange = {
                            onConfigChanged(config.copy(UniformDistribution = if (it) it else null))
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("Uniform Distribution (optional)")
                }
            }
        }

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
                                checked = config.AllowedTiers.contains(tier),
                                onCheckedChange = { checked ->
                                    onConfigChanged(
                                        if (checked)
                                            config.copy(AllowedTiers = config.AllowedTiers + tier)
                                        else
                                            config.copy(AllowedTiers = config.AllowedTiers - tier)
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
                                checked = config.AllowedTiers.contains(tier),
                                onCheckedChange = { checked ->
                                    onConfigChanged(
                                        if (checked)
                                            config.copy(AllowedTiers = config.AllowedTiers + tier)
                                        else
                                            config.copy(AllowedTiers = config.AllowedTiers - tier)
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }

        // Валидация и предупреждения
        if (config.MinCount > config.MaxCount) {
            AlertMessage(
                "Warning: Min Count cannot be greater than Max Count",
                AlertSeverity.WARNING
            )
        }

        config.MinTiersCount?.let { minTiers ->
            config.MaxTiersCount?.let { maxTiers ->
                if (minTiers > maxTiers) {
                    AlertMessage(
                        "Warning: Min Tiers Count cannot be greater than Max Tiers Count",
                        AlertSeverity.WARNING
                    )
                }
            }
        }

        config.MinCountPerTier?.let { minPerTier ->
            config.MaxCountPerTier?.let { maxPerTier ->
                if (minPerTier > maxPerTier) {
                    AlertMessage(
                        "Warning: Min Count Per Tier cannot be greater than Max Count Per Tier",
                        AlertSeverity.WARNING
                    )
                }
            }
        }
    }
}

@Composable
fun TierCheckbox(
    tier: Long,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary,
                uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        Text("Tier $tier", style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun AlertMessage(message: String, severity: AlertSeverity) {
    val color = when (severity) {
        AlertSeverity.WARNING -> MaterialTheme.colorScheme.errorContainer
        AlertSeverity.INFO -> MaterialTheme.colorScheme.tertiaryContainer
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color, MaterialTheme.shapes.small)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = when (severity) {
                AlertSeverity.WARNING -> Icons.Default.Warning
                AlertSeverity.INFO -> Icons.Default.Info
            },
            contentDescription = null,
            tint = when (severity) {
                AlertSeverity.WARNING -> MaterialTheme.colorScheme.error
                AlertSeverity.INFO -> MaterialTheme.colorScheme.tertiary
            }
        )
        Spacer(Modifier.width(8.dp))
        Text(message)
    }
}

private enum class AlertSeverity {
    WARNING, INFO
}