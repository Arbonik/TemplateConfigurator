package project.ui

import ConnectionModel
import ZoneEditor
import ZoneGenerationConfig
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.IconButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import project.ui.common.CommonListItem

@Composable
fun ConnectionModelEditor(
    zones: List<ZoneGenerationConfig>,
    connections: List<ConnectionModel>,
    onConnectionaUpdated: (List<ConnectionModel>) -> Unit
) {
    var selectedConnectionIndex by remember {
        mutableStateOf(0)
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    val newconnection = ConnectionModel(
                        SourceZoneIndex = zones.random().ZoneId,
                        DestZoneIndex = zones.random().ZoneId,
                        IsMain = true
                    )
                    onConnectionaUpdated(connections + newconnection)
                    selectedConnectionIndex = connections.lastIndex

                },
                enabled = true
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
                Text("Добавить связь")
            }

            ConnectionModelList(
                connections = connections,
                selectedIndex = selectedConnectionIndex,
                onZoneSelected = { index -> selectedConnectionIndex = index },
                deleteZone = {
                    selectedConnectionIndex = 0
                    onConnectionaUpdated(
                        connections - it
                    )
                },
                modifier = Modifier.width(200.dp)
            )
        }

        VerticalDivider(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
        )

        ConnectionEditor(
            zones = zones,
            connection = connections.getOrNull(selectedConnectionIndex),
            onConnectionChanged = { updatedConnection ->
                onConnectionaUpdated(
                    connections.toMutableList().apply {
                        set(selectedConnectionIndex, updatedConnection)
                    }
                )
            }
        )
    }
}

@Composable
private fun ConnectionModelList(
    connections: List<ConnectionModel>,
    selectedIndex: Int,
    onZoneSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    deleteZone: (ConnectionModel) -> Unit
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(connections) { index, connection ->
            CommonListItem(
                item = connection,
                isSelected = selectedIndex == index,
                onDelete = deleteZone,
                onSelected = { onZoneSelected(index) }
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                ) {
                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = "${connection.SourceZoneIndex}",
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Start,
                            )

                            Icon(
                                imageVector = Icons.Default.SwapHoriz,
                                contentDescription = "Direction",
                                modifier = Modifier.size(16.dp),
                                tint = when {
                                    connection.IsMain -> MaterialTheme.colorScheme.primary
                                    else -> Color.Gray
                                }
                            )

                            Text(
                                text = "${connection.DestZoneIndex}",
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.End,
                            )
                        }
                    }
                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (connection.IsMain) "MAIN" else "secondary",
                            color = when {
                                connection.IsMain -> MaterialTheme.colorScheme.primary
                                else -> Color.Gray
                            },
                            style = MaterialTheme.typography.labelSmall,
                        )

                        if (connection.Guarded == true) {
                            Text(
                                text = "GUARDED ${connection.GuardStrenght}",
                                style = MaterialTheme.typography.labelSmall,
                            )
                        }
                    }
                }
            }
        }
    }
}