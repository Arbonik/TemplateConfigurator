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


@Composable
fun ConnectionsConfigSection(
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
                    Text("${connection.sourceZoneIndex} → ${connection.destZoneIndex}", color = MaterialTheme.colorScheme.onSurface)
                }
            }

            Button(
                onClick = {
                    val newSource = connections.maxOfOrNull { it.sourceZoneIndex } ?: 0
                    val newDest = connections.maxOfOrNull { it.destZoneIndex } ?: 0
                    onConnectionsChanged(
                        connections + ConnectionModel(
                            sourceZoneIndex = newSource,
                            destZoneIndex = newDest,
                            isMain = false
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Text("Add Connection")
            }
        }

        // Connection details
        Box(modifier = Modifier.weight(1f).padding(8.dp)) {
            selectedConnectionIndex?.let { index ->
                ConnectionEditor(
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

@Composable
fun ConnectionEditor(
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

        Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            OutlinedTextField(
                value = connection.sourceZoneIndex.toString(),
                onValueChange = { newValue ->
                    newValue.toIntOrNull()?.let { source ->
                        onConnectionChanged(connection.copy(sourceZoneIndex = source))
                    }
                },
                label = { Text("Source Zone") },
                modifier = Modifier.weight(1f).padding(end = 4.dp)
            )

            OutlinedTextField(
                value = connection.destZoneIndex.toString(),
                onValueChange = { newValue ->
                    newValue.toIntOrNull()?.let { dest ->
                        onConnectionChanged(connection.copy(destZoneIndex = dest))
                    }
                },
                label = { Text("Destination Zone") },
                modifier = Modifier.weight(1f).padding(start = 4.dp)
            )
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
            OutlinedTextField(
                value = connection.guardStrenght?.toString() ?: "",
                onValueChange = { newValue ->
                    newValue.toIntOrNull()?.let { strength ->
                        onConnectionChanged(connection.copy(guardStrenght = strength))
                    }
                },
                label = { Text("Guard Strength") },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
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
                OutlinedTextField(
                    value = connection.startPointX?.toString() ?: "",
                    onValueChange = { newValue ->
                        newValue.toIntOrNull()?.let { x ->
                            onConnectionChanged(connection.copy(startPointX = x))
                        }
                    },
                    label = { Text("Start Point X") },
                    modifier = Modifier.weight(1f).padding(end = 4.dp)
                )

                OutlinedTextField(
                    value = connection.startPointY?.toString() ?: "",
                    onValueChange = { newValue ->
                        newValue.toIntOrNull()?.let { y ->
                            onConnectionChanged(connection.copy(startPointY = y))
                        }
                    },
                    label = { Text("Start Point Y") },
                    modifier = Modifier.weight(1f).padding(start = 4.dp)
                )
            }
        }

        OutlinedTextField(
            value = connection.minRadiusToSearch?.toString() ?: "",
            onValueChange = { newValue ->
                newValue.toIntOrNull()?.let { radius ->
                    onConnectionChanged(connection.copy(minRadiusToSearch = radius))
                }
            },
            label = { Text("Min Radius To Search") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        OutlinedTextField(
            value = connection.maxRadiusToSearch?.toString() ?: "",
            onValueChange = { newValue ->
                newValue.toIntOrNull()?.let { radius ->
                    onConnectionChanged(connection.copy(maxRadiusToSearch = radius))
                }
            },
            label = { Text("Max Radius To Search") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        OutlinedTextField(
            value = connection.minRadiusToMain?.toString() ?: "",
            onValueChange = { newValue ->
                newValue.toIntOrNull()?.let { radius ->
                    onConnectionChanged(connection.copy(minRadiusToMain = radius))
                }
            },
            label = { Text("Min Radius To Main") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        OutlinedTextField(
            value = connection.maxRadiusToMain?.toString() ?: "",
            onValueChange = { newValue ->
                newValue.toIntOrNull()?.let { radius ->
                    onConnectionChanged(connection.copy(maxRadiusToMain = radius))
                }
            },
            label = { Text("Max Radius To Main") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        OutlinedTextField(
            value = connection.roadType?.toString() ?: "",
            onValueChange = { newValue ->
                newValue.toIntOrNull()?.let { roadType ->
                    onConnectionChanged(connection.copy(roadType = roadType))
                }
            },
            label = { Text("Road Type") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )
    }
}
