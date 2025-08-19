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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

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
                },
                enabled = true
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
                Text("Добавить зону")
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

        Divider(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
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
            CompactConnectionListItem(
                connection,
                {
                    onZoneSelected(index)
                },
                isSelected = selectedIndex == index,
                onDelete = { deleteZone(connection) }
            )
        }
    }
}

@Composable
fun <T> MyListItem(
    item: T,
    isSelected: Boolean,
    onClick: () -> Unit,
    deleteZone: (T) -> Unit,
    content: @Composable RowScope.(T) -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
    } else {
        MaterialTheme.colorScheme.background
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = backgroundColor,
        onClick = onClick
    ) {
        Row {
            content(item)
            Spacer(Modifier.weight(1f))
            Button(
                onClick = { deleteZone(item) },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}

@Composable
fun CompactConnectionListItem(
    connection: ConnectionModel,
    onClick: () -> Unit,
    onDelete: () -> Unit,  // Новый параметр для обработки удаления
    modifier: Modifier = Modifier,
    isSelected: Boolean = false
) {
    Box(
        modifier = modifier
            .width(200.dp)
            .padding(horizontal = 4.dp, vertical = 2.dp)
            .clip(RoundedCornerShape(4.dp))
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = when {
                    isSelected -> MaterialTheme.colorScheme.secondary
                    connection.IsMain -> MaterialTheme.colorScheme.primary
                    else -> Color.Gray
                },
                shape = RoundedCornerShape(4.dp)
            )
            .background(
                when {
                    isSelected -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                    connection.IsMain -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    else -> MaterialTheme.colorScheme.surface
                }
            )
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Column {
            // Первая строка - основная информация и кнопка удаления
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Основная информация о соединении
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "${connection.SourceZoneIndex}",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Start,
                        color = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface
                    )

                    Icon(
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = "Direction",
                        modifier = Modifier.size(16.dp),
                        tint = when {
                            isSelected -> MaterialTheme.colorScheme.secondary
                            connection.IsMain -> MaterialTheme.colorScheme.primary
                            else -> Color.Gray
                        }
                    )

                    Text(
                        text = "${connection.DestZoneIndex}",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.End,
                        color = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface
                    )
                }

                // Кнопка удаления
                IconButton(
                    onClick = { onDelete() },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Delete connection",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            // Вторая строка - статус
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (connection.IsMain) "MAIN" else "secondary",
                    color = when {
                        isSelected -> MaterialTheme.colorScheme.secondary
                        connection.IsMain -> MaterialTheme.colorScheme.primary
                        else -> Color.Gray
                    },
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(top = 4.dp)
                )

                if (connection.Guarded == true) {
                    Text(
                        text = "GUARDED ${connection.GuardStrenght}",
                        color = if (isSelected) MaterialTheme.colorScheme.secondary else Color.Red,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}