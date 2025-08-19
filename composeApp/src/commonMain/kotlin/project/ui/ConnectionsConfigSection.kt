package project.ui

import ConnectionModel
import ZoneGenerationConfig
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
import project.ui.common.AddButton
import project.ui.common.DecimalInputField


//@Composable
//fun ConnectionsConfigSection(
//    zones: List<ZoneGenerationConfig>,
//    connections: List<ConnectionModel>,
//    onConnectionsChanged: (List<ConnectionModel>) -> Unit
//) {
//    var selectedConnectionIndex by remember { mutableStateOf<Int?>(null) }
//
//    Row(modifier = Modifier.fillMaxSize()) {
//        // Connections list
//        Column(modifier = Modifier.width(200.dp).padding(8.dp).verticalScroll(rememberScrollState())) {
//            Text("Connections", style = MaterialTheme.typography.headlineSmall)
//            Divider()
//
//            connections.forEachIndexed { index, connection ->
//                Button(
//                    onClick = { selectedConnectionIndex = index },
//                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = if (selectedConnectionIndex == index) MaterialTheme.colorScheme.primaryContainer
//                        else MaterialTheme.colorScheme.surface
//                    )
//                ) {
//                    Text(
//                        "${connection.SourceZoneIndex} ← → ${connection.DestZoneIndex}",
//                        color = MaterialTheme.colorScheme.onSurface
//                    )
//                }
//            }
//
//            if (zones.isEmpty()) {
//                Text("No zones found", modifier = Modifier.fillMaxSize().wrapContentSize())
//            } else {
//                AddButton {
//                    onConnectionsChanged(
//                        connections + ConnectionModel(
//                            SourceZoneIndex = zones.random().ZoneId,
//                            DestZoneIndex = zones.random().ZoneId,
//                            IsMain =  false
//                        )
//                    )
//                }
//            }
//        }
//
//        // Connection details
//        Box(modifier = Modifier.weight(1f).padding(8.dp)) {
//            selectedConnectionIndex?.let { index ->
//                ConnectionEditor(
//                    zones = zones,
//                    connection = connections[index],
//                    onConnectionChanged = { updatedConnection ->
//                        onConnectionsChanged(connections.toMutableList().apply {
//                            set(index, updatedConnection)
//                        })
//                    }
//                )
//            } ?: run {
//                Text("Select a connection to edit", modifier = Modifier.fillMaxSize().wrapContentSize())
//            }
//        }
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectionEditor(
    zones: List<ZoneGenerationConfig>,
    connection: ConnectionModel?,
    onConnectionChanged: (ConnectionModel) -> Unit,
) {
    if (connection != null) {
        Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
            Text("Connection Configuration", style = MaterialTheme.typography.headlineMedium)

            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(
                    "Connection ${connection.SourceZoneIndex} → ${connection.DestZoneIndex}",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Box(modifier = Modifier.padding(8.dp)) {
                var sourceExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = sourceExpanded,
                    onExpandedChange = { sourceExpanded = !sourceExpanded }
                ) {
                    OutlinedTextField(
                        value = connection.SourceZoneIndex.toString(),
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
                                    onConnectionChanged(connection.copy(SourceZoneIndex = type))
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
                        value = connection.DestZoneIndex.toString(),
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
                                    onConnectionChanged(connection.copy(DestZoneIndex = type))
                                    destExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
                Checkbox(
                    checked = connection.IsMain,
                    onCheckedChange = { newValue ->
                        onConnectionChanged(connection.copy(IsMain = newValue))
                    }
                )
                Text("Is Main Connection", modifier = Modifier.padding(start = 8.dp))
            }

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
                Checkbox(
                    checked = connection.RemoveConnection ?: false,
                    onCheckedChange = { newValue ->
                        onConnectionChanged(connection.copy(RemoveConnection = newValue))
                    }
                )
                Text("RemoveConnection", modifier = Modifier.padding(start = 8.dp))
            }

            if (connection.RemoveConnection != true) {

                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
                    Checkbox(
                        checked = connection.TwoWay ?: false,
                        onCheckedChange = { newValue ->
                            onConnectionChanged(connection.copy(TwoWay = newValue))
                        }
                    )
                    Text("Two Way", modifier = Modifier.padding(start = 8.dp))
                }

                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
                    Checkbox(
                        checked = connection.Guarded ?: false,
                        onCheckedChange = { newValue ->
                            onConnectionChanged(connection.copy(Guarded = newValue))
                        }
                    )
                    Text("Guarded", modifier = Modifier.padding(start = 8.dp))
                }

                connection.Guarded?.let {
                    _root_ide_package_.project.ui.common.DecimalInputField(
                        value = connection.GuardStrenght?.toString() ?: "",
                        title = "Guard Strength",
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        onValueChange = { newValue ->
                            onConnectionChanged(connection.copy(GuardStrenght = newValue.toIntOrNull()))
                        }
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
                    Checkbox(
                        checked = connection.Wide ?: false,
                        onCheckedChange = { newValue ->
                            onConnectionChanged(connection.copy(Wide = newValue))
                        }
                    )
                    Text("Wide", modifier = Modifier.padding(start = 8.dp))
                }

                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
                    Checkbox(
                        checked = connection.StaticPos ?: false,
                        onCheckedChange = { newValue ->
                            onConnectionChanged(connection.copy(StaticPos = newValue))
                        }
                    )
                    Text("Static Position", modifier = Modifier.padding(start = 8.dp))
                }

                if (connection.StaticPos == true) {
                    Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                        _root_ide_package_.project.ui.common.DecimalInputField(
                            value = connection.StartPointX?.toString() ?: "",
                            title = "Start Point X",
                            modifier = Modifier.weight(1f).padding(end = 4.dp),
                            onValueChange = { newValue ->
                                onConnectionChanged(connection.copy(StartPointX = newValue.toIntOrNull()))
                            }
                        )

                        _root_ide_package_.project.ui.common.DecimalInputField(
                            value = connection.StartPointY?.toString() ?: "",
                            title = "Start Point Y",
                            modifier = Modifier.weight(1f).padding(start = 4.dp),
                            onValueChange = { newValue ->
                                onConnectionChanged(connection.copy(StartPointY = newValue.toIntOrNull()))
                            }
                        )
                    }

                    _root_ide_package_.project.ui.common.DecimalInputField(
                        value = connection.MinRadiusToSearch?.toString() ?: "",
                        title = "Min Radius To Search",
                        onValueChange = { newValue ->
                            onConnectionChanged(connection.copy(MinRadiusToSearch = newValue.toIntOrNull()))
                        },
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    )

                    _root_ide_package_.project.ui.common.DecimalInputField(
                        value = connection.MaxRadiusToSearch?.toString() ?: "",
                        title = "Max Radius To Search",
                        onValueChange = { newValue ->
                            onConnectionChanged(connection.copy(MaxRadiusToSearch = newValue.toIntOrNull()))
                        },
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    )
                }


                if (connection.IsMain == false) {
                    _root_ide_package_.project.ui.common.DecimalInputField(
                        value = connection.MinRadiusToMain?.toString() ?: "",
                        title = "Min Radius To Main",
                        onValueChange = { newValue ->
                            onConnectionChanged(connection.copy(MinRadiusToMain = newValue.toIntOrNull()))
                        },
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    )

                    _root_ide_package_.project.ui.common.DecimalInputField(
                        value = connection.MaxRadiusToMain?.toString() ?: "",
                        title = "Max Radius To Main",
                        onValueChange = { newValue ->
                            onConnectionChanged(connection.copy(MaxRadiusToMain = newValue.toIntOrNull()))
                        },
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    )
                }
                Box(modifier = Modifier.padding(8.dp)) {
                    var expanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = connection.RoadType?.toString() ?: "",
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
                                        onConnectionChanged(connection.copy(RoadType = type))
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    } else Text("Зона не выбрана")
}
