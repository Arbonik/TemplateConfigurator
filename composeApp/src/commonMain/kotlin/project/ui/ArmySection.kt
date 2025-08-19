package project.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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


@Composable
fun ArmySection(
    baseMultiplier: Double?,
    multipliers: Map<String, Double>,
    onChangeBaseMultiplier: (Double?) -> Unit,
    onChangeMultiplier: (Map<String, Double>) -> Unit,
) {
    var localBaseMultiplier by remember(baseMultiplier) {
        mutableStateOf(baseMultiplier?.toString() ?: "")
    }
    val localMultipliers = remember(multipliers) {
        multipliers.toMutableMap()
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Base multiplier section
        Text("Base Army Multiplier", style = MaterialTheme.typography.labelSmall)
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(
                onClick = {
                    localBaseMultiplier = ""
                    onChangeBaseMultiplier(null)
                }
            ) {
                Text("Reset Base")
            }
            Spacer(Modifier.width(8.dp))
            OutlinedTextField(
                value = localBaseMultiplier,
                onValueChange = {
                    localBaseMultiplier = it
                    val value = it.toDoubleOrNull()
                    onChangeBaseMultiplier(value)
                },
                label = { Text("Base multiplier (optional)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Faction multipliers section
        Text("Faction Multipliers", style = MaterialTheme.typography.labelSmall)
        allFactions.forEach { faction ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = localMultipliers[faction]?.toString() ?: "",
                    onValueChange = {
                        val value = it.toDoubleOrNull()
                        if (value != null) {
                            localMultipliers[faction] = value
                        } else {
                            localMultipliers.remove(faction)
                        }
                        onChangeMultiplier(localMultipliers)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.width(100.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = faction,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Button(
            onClick = {
                allFactions.forEach { faction ->
                    localMultipliers.remove(faction)
                }
                onChangeMultiplier(localMultipliers)
            }
        ) {
            Text("Reset All Factions")
        }
    }
}

val allFactions = listOf("Humans", "Inferno", "Necropolis", "Elves", "Liga", "Mages", "Dwarfs", "Horde")

