package project.ui.dwellingGenerationConfig

import DependantDwellingConfig
import NumberInput
import NumberInputNullable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DependantDwellingConfigEditor(
    config: DependantDwellingConfig,
    onConfigUpdated: (DependantDwellingConfig) -> Unit
) {
    Column {
        NumberInput(
            label = "зона от которой зависят настройки двелингов",
            config.ZoneId,
            onValueChange = {
                onConfigUpdated(
                    config.copy(ZoneId = it.toLong())
                )
            }
        )
        NumberInput(
            label = "минимальное количество двелов",
            config.MinCount,
            onValueChange = {
                onConfigUpdated(
                    config.copy(MinCount = it.toInt())
                )
            }
        )
        NumberInput(
            label = "максимальное количество двелов",
            config.MaxCount,
            onValueChange = {
                onConfigUpdated(
                    config.copy(MaxCount = it.toInt())
                )
            }
        )
        NumberInputNullable(
            label = "минимальное количество тиров существ в двелинга",
            config.MinTiersCount,
            onValueChange = {
                onConfigUpdated(
                    config.copy(MinTiersCount = it?.toInt())
                )
            }
        )
        NumberInputNullable(
            label = "максимальное количество тиров существ в двелинга",
            config.MaxTiersCount,
            onValueChange = {
                onConfigUpdated(
                    config.copy(MaxTiersCount = it?.toInt())
                )
            }
        )
        // Uniform Distribution switch
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Switch(
                checked = config.UniformDistribution ?: false,
                onCheckedChange = {
                    onConfigUpdated(config.copy(UniformDistribution = if (it) it else null))
                },
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("Uniform Distribution (optional)")
        }
        NumberInputNullable(
            label = "настройка минимального количества двелов для конкретного тира",
            config.MinCountPerTier,
            onValueChange = {
                onConfigUpdated(
                    config.copy(MinCountPerTier = it?.toInt())
                )
            }
        )
        NumberInputNullable(
            label = "настройка максимального количества двелов для тира",
            config.MaxCountPerTier,
            onValueChange = {
                onConfigUpdated(
                    config.copy(MaxCountPerTier = it?.toInt())
                )
            }
        )
        // Uniform Distribution switch
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Switch(
                checked = config.IsCopyMode,
                onCheckedChange = {
                    onConfigUpdated(config.copy(IsCopyMode = it))
                },
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("IsCopyMode")
        }
    }
}