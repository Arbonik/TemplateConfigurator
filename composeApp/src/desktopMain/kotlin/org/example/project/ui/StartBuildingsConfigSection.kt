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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import org.example.project.data.StartBuildingConfig
import org.example.project.data.enums.BuildingMode
import org.example.project.data.enums.TerrainType
import org.example.project.ui.common.EnumDropdown


@Composable
fun StartBuildingsConfigSection(
    buildings: List<StartBuildingConfig>,
    onBuildingsChanged: (List<StartBuildingConfig>) -> Unit
) {
    var selectedBuildingIndex by remember { mutableStateOf<Int?>(null) }

    Row(modifier = Modifier.fillMaxSize()) {
        // Buildings list
        Column(modifier = Modifier.width(200.dp).padding(8.dp).verticalScroll(rememberScrollState())) {
            Text("Start Buildings", style = MaterialTheme.typography.headlineSmall)
            Divider()

            buildings.forEachIndexed { index, building ->
                Button(
                    onClick = { selectedBuildingIndex = index },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedBuildingIndex == index)
                            MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text(building.TerrainType.toString(), color = MaterialTheme.colorScheme.onSurface)
                }
            }

            Button(
                onClick = {
                    onBuildingsChanged(
                        buildings + StartBuildingConfig(
                            ApplyAllTerrains = null,
                            TerrainType = TerrainType.FirstPlayer,
                            Buildings = emptyList(),
                            BuildingMode = BuildingMode.All
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Text("Add Start Building Config")
            }
        }

        // Building details
        Box(modifier = Modifier.weight(1f).padding(8.dp)) {
            selectedBuildingIndex?.let { index ->
                StartBuildingEditor(
                    building = buildings[index],
                    onBuildingChanged = { updatedBuilding ->
                        onBuildingsChanged(buildings.toMutableList().apply {
                            set(index, updatedBuilding)
                        })
                    },
                    onDelete = {
                        onBuildingsChanged(buildings.toMutableList().apply { removeAt(index) })
                        selectedBuildingIndex = null
                    }
                )
            } ?: run {
                Text(
                    "Select a building config to edit",
                    modifier = Modifier.fillMaxSize().wrapContentSize()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartBuildingEditor(
    building: StartBuildingConfig,
    onBuildingChanged: (StartBuildingConfig) -> Unit,
    onDelete: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
        Text("Start Building Configuration", style = MaterialTheme.typography.headlineMedium)

        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text("For Terrain: ${building.TerrainType}", style = MaterialTheme.typography.titleMedium)
            Button(
                onClick = onDelete,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text("Delete Config")
            }
        }

        // Apply to all terrains
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
            Checkbox(
                checked = building.ApplyAllTerrains ?: false,
                onCheckedChange = { newValue ->
                    onBuildingChanged(building.copy(ApplyAllTerrains = newValue))
                }
            )
            Text("Apply to All Terrains", modifier = Modifier.padding(start = 8.dp))
        }

        // Terrain Type (only editable if not applying to all terrains)
        if (building.ApplyAllTerrains != true) {
            EnumDropdown(
                value = building.TerrainType.toString(),
                label = "Terrain Type",
                values = TerrainType.values().map { it.toString() },
                onBuildingChanged = { onBuildingChanged(building.copy(TerrainType = TerrainType.valueOf(it))) }
            )
        }

        EnumDropdown(
            value = building.BuildingMode.toString(),
            label = "Building Mode",
            values = BuildingMode.values().map { it.toString() },
            onBuildingChanged = { onBuildingChanged(building.copy(BuildingMode = BuildingMode.valueOf(it))) }
        )

        // Buildings List
        Text("Buildings", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))
        ChipGroup(
            items = building.Buildings,
            onItemRemoved = { buildingId ->
                onBuildingChanged(
                    building.copy(
                        Buildings = building.Buildings - buildingId
                    )
                )
            }
        )

        // Add new building
        var newBuildingId by remember { mutableStateOf("") }
        Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            OutlinedTextField(
                value = newBuildingId,
                onValueChange = { newBuildingId = it },
                label = { Text("New Building ID") },
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = {
                    if (newBuildingId.isNotBlank()) {
                        onBuildingChanged(
                            building.copy(
                                Buildings = building.Buildings + newBuildingId
                            )
                        )
                        newBuildingId = ""
                    }
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Add Building")
            }
        }
    }
}
