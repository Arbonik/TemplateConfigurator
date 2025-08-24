package project.ui

import ConnectionModel
import EnumDropdownRow
import RoadType
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectionEditor(
    zones: List<ZoneGenerationConfig>,
    connection: ConnectionModel?,
    onConnectionChanged: (ConnectionModel) -> Unit,
) {
    if (connection != null) {
        Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(8.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(
                    "Connection ${connection.SourceZoneIndex} → ${connection.DestZoneIndex}",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            EnumDropdownRow(
                label = "Source Zone:",
                currentValue = connection.SourceZoneIndex,
                itemTitle = { it.toString() },
                values = zones.map { it.ZoneId },
                onValueSelected = {
                    onConnectionChanged(connection.copy(SourceZoneIndex = it))
                },
            )

            EnumDropdownRow(
                label = "Destination Zone:",
                currentValue = connection.DestZoneIndex,
                itemTitle = { it.toString() },
                values = zones.map { it.ZoneId },
                onValueSelected = {
                    onConnectionChanged(connection.copy(DestZoneIndex = it))
                },
            )

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier) {
                Checkbox(
                    checked = connection.IsMain,
                    onCheckedChange = { newValue ->
                        onConnectionChanged(connection.copy(IsMain = newValue))
                    }
                )
                Text("Is Main Connection", modifier = Modifier.padding(start = 8.dp))
            }

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier) {
                Checkbox(
                    checked = connection.RemoveConnection ?: false,
                    onCheckedChange = { newValue ->
                        onConnectionChanged(connection.copy(RemoveConnection = newValue))
                    }
                )
                Text("RemoveConnection", modifier = Modifier.padding(start = 8.dp))
            }

            if (connection.RemoveConnection != true) {

                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier) {
                    Checkbox(
                        checked = connection.TwoWay ?: false,
                        onCheckedChange = { newValue ->
                            onConnectionChanged(connection.copy(TwoWay = newValue))
                        }
                    )
                    Text("Two Way", modifier = Modifier.padding(start = 8.dp))
                }

                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier) {
                    Checkbox(
                        checked = connection.Guarded ?: false,
                        onCheckedChange = { newValue ->
                            onConnectionChanged(connection.copy(Guarded = newValue))
                        }
                    )
                    Text("Guarded", modifier = Modifier.padding(start = 8.dp))
                }

                connection.Guarded?.let {
                    DecimalInputField(
                        value = connection.GuardStrenght?.toString() ?: "",
                        title = "Guard Strength",
                        modifier = Modifier.fillMaxWidth(),
                        onValueChange = { newValue ->
                            onConnectionChanged(connection.copy(GuardStrenght = newValue.toIntOrNull()))
                        }
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier) {
                    Checkbox(
                        checked = connection.Wide ?: false,
                        onCheckedChange = { newValue ->
                            onConnectionChanged(connection.copy(Wide = newValue))
                        }
                    )
                    Text("Wide", modifier = Modifier.padding(start = 8.dp))
                }

                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier) {
                    Checkbox(
                        checked = connection.StaticPos ?: false,
                        onCheckedChange = { newValue ->
                            onConnectionChanged(connection.copy(StaticPos = newValue))
                        }
                    )
                    Text("Static Position", modifier = Modifier.padding(start = 8.dp))
                }

                if (connection.StaticPos == true) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        DecimalInputField(
                            value = connection.StartPointX?.toString() ?: "",
                            title = "Start Point X",
                            modifier = Modifier.weight(1f).padding(end = 4.dp),
                            onValueChange = { newValue ->
                                onConnectionChanged(connection.copy(StartPointX = newValue.toIntOrNull()))
                            }
                        )

                        DecimalInputField(
                            value = connection.StartPointY?.toString() ?: "",
                            title = "Start Point Y",
                            modifier = Modifier.weight(1f).padding(start = 4.dp),
                            onValueChange = { newValue ->
                                onConnectionChanged(connection.copy(StartPointY = newValue.toIntOrNull()))
                            }
                        )
                    }

                    DecimalInputField(
                        value = connection.MinRadiusToSearch?.toString() ?: "",
                        title = "Min Radius To Search",
                        onValueChange = { newValue ->
                            onConnectionChanged(connection.copy(MinRadiusToSearch = newValue.toIntOrNull()))
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    DecimalInputField(
                        value = connection.MaxRadiusToSearch?.toString() ?: "",
                        title = "Max Radius To Search",
                        onValueChange = { newValue ->
                            onConnectionChanged(connection.copy(MaxRadiusToSearch = newValue.toIntOrNull()))
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }


                if (connection.IsMain == false) {
                    DecimalInputField(
                        value = connection.MinRadiusToMain?.toString() ?: "",
                        title = "Min Radius To Main",
                        onValueChange = { newValue ->
                            onConnectionChanged(connection.copy(MinRadiusToMain = newValue.toIntOrNull()))
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    DecimalInputField(
                        value = connection.MaxRadiusToMain?.toString() ?: "",
                        title = "Max Radius To Main",
                        onValueChange = { newValue ->
                            onConnectionChanged(connection.copy(MaxRadiusToMain = newValue.toIntOrNull()))
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                EnumDropdownRow(
                    label = "Road Type:",
                    currentValue = RoadType.entries.find {
                        it.number == connection.RoadType
                    } ?: RoadType.MAINROAD,
                    values = RoadType.entries.toList(),
                    itemTitle = {
                        "${it.description} (${it.name})"
                    },
                    onValueSelected = {
                        onConnectionChanged(connection.copy(RoadType = (it as RoadType).number))
                    },
                )
            }
        }
    } else Text("Зона не выбрана")
}
