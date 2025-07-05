package org.example.project.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.unit.dp
import org.example.project.data.ConnectionModel
import org.example.project.data.Zone
import org.example.project.ui.common.AddButton
import org.example.project.ui.common.DecimalInputField


@Composable
fun ConnectionsConfigSection(
    zones: List<Zone>,
    connections: List<ConnectionModel>,
    onConnectionsChanged: (List<ConnectionModel>) -> Unit
) {
    var selectedConnectionIndex by remember { mutableStateOf<Int?>(null) }

    Row(modifier = Modifier.fillMaxSize()) {
        // Connections list
        Column(modifier = Modifier.width(200.dp).padding(8.dp).verticalScroll(rememberScrollState())) {
            Text("Connections", style = MaterialTheme.typography.headlineSmall)
            Divider()

            connections.forEachIndexed { index, connection ->
                Button(
                    onClick = { selectedConnectionIndex = index },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedConnectionIndex == index) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text(
                        "${connection.sourceZoneIndex} ← → ${connection.destZoneIndex}",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            if (zones.isEmpty()) {
                Text("No zones found", modifier = Modifier.fillMaxSize().wrapContentSize())
            } else {
                AddButton {
                    onConnectionsChanged(
                        connections + ConnectionModel(
                            sourceZoneIndex = zones.first().ZoneId,
                            destZoneIndex = zones.last().ZoneId,
                            isMain = false
                        )
                    )
                }
            }
        }

        // Connection details
        Box(modifier = Modifier.weight(1f).padding(8.dp)) {
            selectedConnectionIndex?.let { index ->
                ConnectionEditor(
                    zones = zones,
                    connection = connections[index],
                    onConnectionChanged = { updatedConnection ->
                        onConnectionsChanged(connections.toMutableList().apply {
                            set(index, updatedConnection)
                        })
                    },
                    onDelete = {
                        onConnectionsChanged(connections.toMutableList().apply { removeAt(index) })
                        selectedConnectionIndex = null
                    }
                )
            } ?: run {
                Text("Select a connection to edit", modifier = Modifier.fillMaxSize().wrapContentSize())
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectionEditor(
    zones: List<Zone>,
    connection: ConnectionModel,
    onConnectionChanged: (ConnectionModel) -> Unit,
    onDelete: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
        Text("Connection Configuration", style = MaterialTheme.typography.headlineMedium)

        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(
                "Connection ${connection.sourceZoneIndex} → ${connection.destZoneIndex}",
                style = MaterialTheme.typography.titleMedium
            )
            Button(
                onClick = onDelete, colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text("Delete Connection")
            }
        }

        Box(modifier = Modifier.padding(8.dp)) {
            var sourceExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = sourceExpanded,
                onExpandedChange = { sourceExpanded = !sourceExpanded }
            ) {
                OutlinedTextField(
                    value = connection.sourceZoneIndex.toString(),
                    onValueChange = {},
                    label = { Text("Source Zone") },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = sourceExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = sourceExpanded,
                    onDismissRequest = { sourceExpanded = false }
                ) {
                    zones.map { it.ZoneId }.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.toString()) },
                            onClick = {
                                onConnectionChanged(connection.copy(sourceZoneIndex = type))
                                sourceExpanded = false
                            }
                        )
                    }
                }
            }
        }

        Box(modifier = Modifier.padding(8.dp)) {
            var destExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = destExpanded,
                onExpandedChange = { destExpanded = !destExpanded }
            ) {
                OutlinedTextField(
                    value = connection.destZoneIndex.toString(),
                    onValueChange = {},
                    label = { Text("Destination Zone") },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = destExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = destExpanded,
                    onDismissRequest = { destExpanded = false }
                ) {
                    zones.map { it.ZoneId }.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.toString()) },
                            onClick = {
                                onConnectionChanged(connection.copy(destZoneIndex = type))
                                destExpanded = false
                            }
                        )
                    }
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
            Checkbox(
                checked = connection.isMain,
                onCheckedChange = { newValue ->
                    onConnectionChanged(connection.copy(isMain = newValue))
                }
            )
            Text("Is Main Connection", modifier = Modifier.padding(start = 8.dp))
        }

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
            Checkbox(
                checked = connection.twoWay ?: false,
                onCheckedChange = { newValue ->
                    onConnectionChanged(connection.copy(twoWay = newValue))
                }
            )
            Text("Two Way", modifier = Modifier.padding(start = 8.dp))
        }

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
            Checkbox(
                checked = connection.guarded ?: false,
                onCheckedChange = { newValue ->
                    onConnectionChanged(connection.copy(guarded = newValue))
                }
            )
            Text("Guarded", modifier = Modifier.padding(start = 8.dp))
        }

        connection.guarded?.let {
            DecimalInputField(
                value = connection.guardStrenght?.toString() ?: "",
                title = "Guard Strength",
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                onValueChange = { newValue ->
                    onConnectionChanged(connection.copy(guardStrenght = newValue.toIntOrNull()))
                }
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
            Checkbox(
                checked = connection.wide ?: false,
                onCheckedChange = { newValue ->
                    onConnectionChanged(connection.copy(wide = newValue))
                }
            )
            Text("Wide", modifier = Modifier.padding(start = 8.dp))
        }

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
            Checkbox(
                checked = connection.staticPos ?: false,
                onCheckedChange = { newValue ->
                    onConnectionChanged(connection.copy(staticPos = newValue))
                }
            )
            Text("Static Position", modifier = Modifier.padding(start = 8.dp))
        }

        if (connection.staticPos == true) {
            Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                DecimalInputField(
                    value = connection.startPointX?.toString() ?: "",
                    title = "Start Point X",
                    modifier = Modifier.weight(1f).padding(end = 4.dp),
                    onValueChange = { newValue ->
                        onConnectionChanged(connection.copy(startPointX = newValue.toIntOrNull()))
                    }
                )

                DecimalInputField(
                    value = connection.startPointY?.toString() ?: "",
                    title = "Start Point Y",
                    modifier = Modifier.weight(1f).padding(start = 4.dp),
                    onValueChange = { newValue ->
                        onConnectionChanged(connection.copy(startPointY = newValue.toIntOrNull()))
                    }
                )
            }
        }

        DecimalInputField(
            value = connection.minRadiusToSearch?.toString() ?: "",
            title = "Min Radius To Search",
            onValueChange = { newValue ->
                onConnectionChanged(connection.copy(minRadiusToSearch = newValue.toIntOrNull()))
            },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        DecimalInputField(
            value = connection.maxRadiusToSearch?.toString() ?: "",
            title = "Max Radius To Search",
            onValueChange = { newValue ->
                onConnectionChanged(connection.copy(maxRadiusToSearch = newValue.toIntOrNull()))
            },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        DecimalInputField(
            value = connection.minRadiusToMain?.toString() ?: "",
            title = "Min Radius To Main",
            onValueChange = { newValue ->
                onConnectionChanged(connection.copy(minRadiusToMain = newValue.toIntOrNull()))
            },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        DecimalInputField(
            value = connection.maxRadiusToMain?.toString() ?: "",
            title = "Max Radius To Main",
            onValueChange = { newValue ->
                onConnectionChanged(connection.copy(maxRadiusToMain = newValue.toIntOrNull()))
            },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        Box(modifier = Modifier.padding(8.dp)) {
            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = connection.roadType?.toString() ?: "",
                    onValueChange = {},
                    label = { Text("Road Type") },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    listOf(0, 1, 2).forEach { type ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    when (type) {
                                        0 -> "Без дороги"; 1 -> "земляная"; 2 -> "каменная"
                                        else -> ""
                                    }
                                )
                            },
                            onClick = {
                                onConnectionChanged(connection.copy(roadType = type))
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}
