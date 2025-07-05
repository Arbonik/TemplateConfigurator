package org.example.project.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp


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