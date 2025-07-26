package org.example.project.ui.dwellingGenerationConfig

import DwellingValue
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DwellingValueEditor(
    value: DwellingValue,
    onValueChanged: (DwellingValue) -> Unit,
    label: String? = null,
    modifier: Modifier = Modifier
) {
    val t1 = remember(value) { value.T1?.toString() ?: "" }
    val t2 = remember(value) { value.T2?.toString() ?: "" }
    val t3 = remember(value) { value.T3?.toString() ?: "" }
    val t4 = remember(value) { value.T4?.toString() ?: "" }
    val t5 = remember(value) { value.T5?.toString() ?: "" }
    val t6 = remember(value) { value.T6?.toString() ?: "" }
    val t7 = remember(value) { value.T7?.toString() ?: "" }

    Column(modifier = modifier) {
        label?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Отображаем существующие не-null значения
            listOf(
                "T1" to t1,
                "T2" to t2,
                "T3" to t3,
                "T4" to t4,
                "T5" to t5,
                "T6" to t6,
                "T7" to t7
            ).forEach { (key, fieldValue) ->
                OutlinedTextField(
                    value = fieldValue,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty()) {
                            onValueChanged(updateDwellingValue(value, key, null))
                        } else {
                            newValue.toIntOrNull()?.let {
                                onValueChanged(updateDwellingValue(value, key, it))
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults
                        .colors()
                        .copy(unfocusedContainerColor = if (fieldValue.isEmpty()) Color.LightGray else Color.Transparent),
                    label = { Text(key) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }
        }
    }
}

// Остальные функции остаются без изменений
// Вспомогательные функции остаются теми же:
private fun updateDwellingValue(
    current: DwellingValue,
    field: String,
    newValue: Int?
): DwellingValue {
    return when (field) {
        "T1" -> current.copy(T1 = newValue)
        "T2" -> current.copy(T2 = newValue)
        "T3" -> current.copy(T3 = newValue)
        "T4" -> current.copy(T4 = newValue)
        "T5" -> current.copy(T5 = newValue)
        "T6" -> current.copy(T6 = newValue)
        "T7" -> current.copy(T7 = newValue)
        else -> current
    }
}