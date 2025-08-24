package project.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnumDropdown(
    value: String,
    label: String,
    values: List<String>,
    onBuildingChanged: (String) -> Unit,
) {
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
    title: String,
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
fun <T> NullableFiled(
    value: T?,
    onValueChange: (T?) -> Unit,
    label: String,
    defaultValue: T,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    var isNull by remember(value) { mutableStateOf(value == null) }
    Column(modifier = modifier.padding(vertical = 8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
        ) {
            Checkbox(
                checked = !isNull,
                onCheckedChange = {
                    isNull = !it
                    if (isNull) {
                        onValueChange(null)
                    } else {
                        onValueChange(defaultValue)
                    }
                }
            )

            Text(label)
        }
        if (value != null) {
            content()
        }
    }
}

@Composable
fun <T> CommonListItem(
    item: T,
    isSelected: Boolean,
    onDelete: (T) -> Unit,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit,
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Диалог подтверждения удаления
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Подтверждение удаления") },
            text = { Text("Вы уверены, что хотите удалить этот элемент?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDelete(item)
                    }
                ) {
                    Text("Удалить")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Отмена")
                }
            }
        )
    }

    Card(
        modifier = modifier.fillMaxWidth().padding(1.dp).clickable(onClick = onSelected),
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
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            content(item)
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = {
                    showDeleteDialog = true
                }
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}