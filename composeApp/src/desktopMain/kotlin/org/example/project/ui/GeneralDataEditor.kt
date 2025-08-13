package org.example.project.ui

import GeneralData
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralDataEditor(
    initialData: GeneralData?,
    onSave: (GeneralData) -> Unit,
    modifier: Modifier = Modifier
) {
    var mine1Level by remember { mutableStateOf(initialData?.Mine1LevelGuardLevel?.toString() ?: "") }
    var mine2Level by remember { mutableStateOf(initialData?.Mine2LevelGuardLevel?.toString() ?: "") }
    var mineGoldLevel by remember { mutableStateOf(initialData?.MineGoldGuardLevel?.toString() ?: "") }

    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Guard Levels Configuration",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        // Mine 1 Level Guard
        OutlinedTextField(
            value = mine1Level,
            onValueChange = {
                mine1Level = it
                showError = false
                val mine1Int = mine1Level.toIntOrNull()
                val mine2Int = mine2Level.toIntOrNull()
                val mineGoldInt = mineGoldLevel.toIntOrNull()
                onSave(
                    GeneralData(
                        Mine1LevelGuardLevel = mine1Int,
                        Mine2LevelGuardLevel = mine2Int,
                        MineGoldGuardLevel = mineGoldInt
                    )
                )
            },
            label = { Text("Mine 1 Level Guard") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            isError = showError && mine1Level.isNotEmpty() && mine1Level.toIntOrNull() == null
        )

        // Mine 2 Level Guard
        OutlinedTextField(
            value = mine2Level,
            onValueChange = {
                mine2Level = it
                showError = false
                val mine1Int = mine1Level.toIntOrNull()
                val mine2Int = mine2Level.toIntOrNull()
                val mineGoldInt = mineGoldLevel.toIntOrNull()
                onSave(
                    GeneralData(
                        Mine1LevelGuardLevel = mine1Int,
                        Mine2LevelGuardLevel = mine2Int,
                        MineGoldGuardLevel = mineGoldInt
                    )
                )
            },
            label = { Text("Mine 2 Level Guard") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            isError = showError && mine2Level.isNotEmpty() && mine2Level.toIntOrNull() == null
        )

        // Gold Mine Guard
        OutlinedTextField(
            value = mineGoldLevel,
            onValueChange = {
                mineGoldLevel = it
                showError = false

                val mine1Int = mine1Level.toIntOrNull()
                val mine2Int = mine2Level.toIntOrNull()
                val mineGoldInt = mineGoldLevel.toIntOrNull()
                onSave(
                    GeneralData(
                        Mine1LevelGuardLevel = mine1Int,
                        Mine2LevelGuardLevel = mine2Int,
                        MineGoldGuardLevel = mineGoldInt
                    )
                )
            },
            label = { Text("Gold Mine Guard") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            isError = showError && mineGoldLevel.isNotEmpty() && mineGoldLevel.toIntOrNull() == null
        )

        if (showError) {
            Text(
                text = "Please enter valid numbers for all fields",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}