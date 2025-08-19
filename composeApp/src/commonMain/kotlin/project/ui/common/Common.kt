package project.ui.common

import IntValueConfig
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import project.ui.theme.HeadingText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnumDropdown(
    value : String,
    label : String,
    values : List<String>,
    onBuildingChanged : (String) -> Unit,
){
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = Modifier.padding(8.dp)) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = {},
                label = { Text(label) },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                values.forEach { type ->
                    androidx.compose.material3.DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            onBuildingChanged(type)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AddButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(start = 8.dp)
    ) {
        Text("Add")
    }
}

@Composable
fun DecimalInputField(
    value: String,
    title : String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
) {

    var text by remember {
        mutableStateOf(value)
    }

    OutlinedTextField(
        modifier = modifier.padding(8.dp),
        value = text,
        onValueChange = {
            text = it
            onValueChange(it)
        },
        label = { Text(title) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
        ),
    )
}


@Composable
fun IntValueConfigEditor(
    config: IntValueConfig?,
    onConfigChanged: (IntValueConfig?) -> Unit,
    modifier: Modifier = Modifier
) {
    var isRandom by remember { mutableStateOf(config?.MinValue != config?.MaxValue) }
    var minValueText by remember {
        mutableStateOf(TextFieldValue(config?.MinValue?.toString() ?: ""))
    }
    var maxValueText by remember {
        mutableStateOf(TextFieldValue(config?.MaxValue?.toString() ?: ""))
    }

    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Checkbox(
                checked = isRandom,
                onCheckedChange = {
                    isRandom = it
                    if (!it) {
                        // Если рандом выключен, копируем minValue в maxValue
                        maxValueText = minValueText
                        _root_ide_package_.project.ui.common.updateConfig(
                            minValueText.text,
                            minValueText.text,
                            onConfigChanged
                        )
                    } else {
                        // Если рандом включен, сохраняем текущие значения
                        _root_ide_package_.project.ui.common.updateConfig(
                            minValueText.text,
                            maxValueText.text,
                            onConfigChanged
                        )
                    }
                }
            )
            _root_ide_package_.project.ui.theme.HeadingText("Рандом")
        }

        if (isRandom) {
            // Режим рандома - два поля ввода
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextField(
                    value = minValueText,
                    onValueChange = {
                        minValueText = it
                        _root_ide_package_.project.ui.common.updateConfig(it.text, maxValueText.text, onConfigChanged)
                    },
                    label = { Text("Min значение") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                TextField(
                    value = maxValueText,
                    onValueChange = {
                        maxValueText = it
                        _root_ide_package_.project.ui.common.updateConfig(minValueText.text, it.text, onConfigChanged)
                    },
                    label = { Text("Max значение") },
                    modifier = Modifier.weight(1f)
                )
            }
        } else {
            // Одиночный режим - одно поле ввода
            TextField(
                value = minValueText,
                onValueChange = {
                    minValueText = it
                    maxValueText = it
                    _root_ide_package_.project.ui.common.updateConfig(it.text, it.text, onConfigChanged)
                },
                label = { Text("Значение") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

private fun updateConfig(
    minText: String,
    maxText: String,
    onConfigChanged: (IntValueConfig) -> Unit
) {
    val minValue = minText.toIntOrNull()
    val maxValue = maxText.toIntOrNull()

    onConfigChanged(IntValueConfig(minValue, maxValue))
}