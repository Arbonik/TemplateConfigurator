package project.ui

import ZoneRandomizationConfig
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog


private sealed interface ZoneSelectionContext {
    data class ZonesToSwap(val array: IntArray) : ZoneSelectionContext
    object ZonesToRandomize : ZoneSelectionContext
}

@Composable
fun ZoneRandomizationConfigEditor(
    config: ZoneRandomizationConfig?,
    onConfigChanged: (ZoneRandomizationConfig) -> Unit,
    modifier: Modifier = Modifier,
    availableZoneIds: List<Int> = emptyList() // List of available zone IDs for selection
) {
    var showZoneSelectionDialog by remember { mutableStateOf<ZoneSelectionContext?>(null) }

    LaunchedEffect(Unit){
        if (config == null)
            onConfigChanged(ZoneRandomizationConfig(
                ZonesToSwap = emptyList(), null, listOf())
            )
    }

    FlowColumn(
        modifier = modifier.padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Zone Randomization Configuration", style = MaterialTheme.typography.headlineSmall)
        // Zones to Swap section
        Text("Zones to Swap", style = MaterialTheme.typography.titleMedium)
        Text(
            "First element in each array can be swapped with any other in the same array",
            style = MaterialTheme.typography.bodySmall
        )

        (config?.ZonesToSwap ?: emptyList()).forEach { zoneArray ->
            ZoneArrayEditor(
                zoneArray = zoneArray.toList(),
                onEditClicked = {
                    showZoneSelectionDialog = ZoneSelectionContext.ZonesToSwap(zoneArray)
                },
                onRemoveClicked = {
                    if (config != null)
                        onConfigChanged(
                            config.copy(
                                ZonesToSwap = config.ZonesToSwap.filter { it !== zoneArray }
                            ))
                }
            )
        }

        Button(
            onClick = {
                val newArray = intArrayOf()
                config?.let {
                    onConfigChanged(
                        it.copy(
                            ZonesToSwap = config.ZonesToSwap + newArray
                        )
                    )
                }
                showZoneSelectionDialog = ZoneSelectionContext.ZonesToSwap(newArray)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Zone Swap Group")
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Switch(
                checked = config?.IsSymmetricalSwap ?: false,
                onCheckedChange = { isChecked ->
                    config?.let { onConfigChanged(it.copy(IsSymmetricalSwap = isChecked)) }
                },
                modifier = Modifier.padding(end = 8.dp)
            )
            Column {
                Text("Symmetrical Swap", style = MaterialTheme.typography.titleMedium)
                Text(
                    "Swaps will be symmetric across all zone groups",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Text("Zones to Randomize", style = MaterialTheme.typography.titleMedium)
        Text(
            "These zones will be randomized by coordinates",
            style = MaterialTheme.typography.bodySmall
        )

        (config?.ZonesToRandomize ?: emptyList()).forEach { zoneId ->
            ZoneIdItem(
                zoneId = zoneId,
                onRemoveClicked = {
                    if (config != null)
                        onConfigChanged(
                            config.copy(
                                ZonesToRandomize = config.ZonesToRandomize.filter { it != zoneId }
                            ))
                }
            )
        }

        Button(
            onClick = {
                showZoneSelectionDialog = ZoneSelectionContext.ZonesToRandomize
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Zone to Randomize")
        }
    }

    // Zone selection dialog
    showZoneSelectionDialog?.let { context ->
        ZoneSelectionDialog(
            availableZoneIds = availableZoneIds,
            onDismiss = { showZoneSelectionDialog = null },
            onConfirm = { selectedIds ->
                when (context) {
                    is ZoneSelectionContext.ZonesToSwap -> {
                        if (config != null) {
                            val updatedZones = config.ZonesToSwap.map {
                                if (it === context.array) selectedIds.toIntArray() else it
                            }
                            onConfigChanged(config.copy(ZonesToSwap = updatedZones))
                        }
                    }

                    ZoneSelectionContext.ZonesToRandomize -> {
                        if (config != null) {
                            val updatedZones = config.ZonesToRandomize + selectedIds.map { it.toLong() }
                            onConfigChanged(config.copy(ZonesToRandomize = updatedZones))
                        }
                    }
                }
                showZoneSelectionDialog = null
            },
            initialSelection = when (context) {
                is ZoneSelectionContext.ZonesToSwap -> context.array.toList()
                ZoneSelectionContext.ZonesToRandomize -> emptyList()
            }
        )
    }
}


@Composable
private fun ZoneArrayEditor(
    zoneArray: List<Int>,
    onEditClicked: () -> Unit,
    onRemoveClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 1.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                if (zoneArray.isEmpty()) {
                    Text("Empty group", style = MaterialTheme.typography.bodySmall)
                } else {
                    Text(zoneArray.joinToString(", "), style = MaterialTheme.typography.bodyMedium)
                }
            }

            Button(
                onClick = onEditClicked,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text("Edit")
            }

            IconButton(onClick = onRemoveClicked) {
                Icon(Icons.Default.Delete, contentDescription = "Remove")
            }
        }
    }
}

@Composable
private fun ZoneIdItem(
    zoneId: Long,
    onRemoveClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 1.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Text(zoneId.toString(), style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onRemoveClicked) {
                Icon(Icons.Default.Delete, contentDescription = "Remove")
            }
        }
    }
}

@Composable
private fun ZoneSelectionDialog(
    availableZoneIds: List<Int>,
    initialSelection: List<Int>,
    onDismiss: () -> Unit,
    onConfirm: (List<Int>) -> Unit,
) {
    var selectedIds by remember { mutableStateOf(initialSelection.toSet()) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.large,
            modifier = Modifier.widthIn(min = 280.dp, max = 560.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Select Zone IDs", style = MaterialTheme.typography.headlineSmall)

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.heightIn(max = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(availableZoneIds) { zoneId ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Checkbox(
                                checked = selectedIds.contains(zoneId),
                                onCheckedChange = { isChecked ->
                                    selectedIds = if (isChecked) {
                                        selectedIds + zoneId
                                    } else {
                                        selectedIds - zoneId
                                    }
                                }
                            )
                            Text(zoneId.toString(), modifier = Modifier.padding(start = 8.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = { onConfirm(selectedIds.toList()) }
                    ) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}
