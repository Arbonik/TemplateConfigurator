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
import org.example.project.data.CreaturesConfiguration
import org.example.project.data.DwellingGenerationConfig
import org.example.project.data.IntValueConfig
import org.example.project.data.MineGenerationConfig
import org.example.project.data.Zone
import org.example.project.data.enums.BuildingTexture
import org.example.project.data.enums.TerrainType


@Composable
fun ZonesConfigSection(
    zones: List<Zone>,
    onZonesChanged: (List<Zone>) -> Unit
) {
    var selectedZoneIndex by remember { mutableStateOf<Int?>(null) }

    Row(modifier = Modifier.fillMaxSize()) {
        // Zone list
        Column(modifier = Modifier.width(200.dp).padding(8.dp).verticalScroll(rememberScrollState())) {
            Text("Zones", style = MaterialTheme.typography.headlineSmall)
            Divider()
            zones.forEachIndexed { index, zone ->
                Button(
                    onClick = { selectedZoneIndex = index },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedZoneIndex == index) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text("Zone ${zone.ZoneId}", color = MaterialTheme.colorScheme.onSurface)
                }
            }

            Button(
                onClick = {
                    val newZoneId = (zones.maxOfOrNull { it.ZoneId } ?: 0) + 1
                    onZonesChanged(
                        zones + Zone(
                            ZoneId = newZoneId,
                            TerrainType = TerrainType.Terrain1,
                            MirrorZoneId = null,
                            DwellingTexture = BuildingTexture.Default
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Text("Add Zone")
            }
        }

        // Zone details
        Box(modifier = Modifier.weight(1f).padding(8.dp)) {
            selectedZoneIndex?.let { index ->
                ZoneEditor(
                    allZones = zones,
                    zone = zones[index],
                    onZoneChanged = { updatedZone ->
                        onZonesChanged(zones.toMutableList().apply {
                            set(index, updatedZone)
                        })
                    },
                    onDelete = {
                        onZonesChanged(zones.toMutableList().apply { removeAt(index) })
                        selectedZoneIndex = null
                    }
                )
            } ?: run {
                Text("Select a zone to edit", modifier = Modifier.fillMaxSize().wrapContentSize())
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZoneEditor(
    allZones: List<Zone>,
    zone: Zone,
    onZoneChanged: (Zone) -> Unit,
    onDelete: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
        Text("Zone Configuration", style = MaterialTheme.typography.headlineMedium)

        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text("Zone ID: ${zone.ZoneId}", style = MaterialTheme.typography.titleMedium)
            Button(
                onClick = onDelete, colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text("Delete Zone")
            }
        }

        // Terrain Type
        var expanded by remember { mutableStateOf(false) }
        Box(modifier = Modifier.padding(8.dp)) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = zone.TerrainType.toString(),
                    onValueChange = {},
                    label = { Text("Terrain Type") },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    TerrainType.values().forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.toString()) },
                            onClick = {
                                onZoneChanged(zone.copy(TerrainType = type))
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        // Mirror Zone ID
        // Terrain Type
        var expandedMirror by remember { mutableStateOf(false) }
        Box(modifier = Modifier.padding(8.dp)) {
            ExposedDropdownMenuBox(
                expanded = expandedMirror,
                onExpandedChange = { expandedMirror = !expandedMirror }
            ) {
                OutlinedTextField(
                    value = zone.MirrorZoneId.toString(),
                    onValueChange = {},
                    label = { Text("Mirror Zone ID") },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMirror) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedMirror,
                    onDismissRequest = { expandedMirror = false }
                ) {
                    (allZones + null).forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type?.ZoneId.toString()) },
                            onClick = {
                                onZoneChanged(zone.copy(MirrorZoneId = type?.ZoneId))
                                expandedMirror = false
                            }
                        )
                    }
                }
            }
        }

        // Dwelling Generation Config
        Text(
            "Dwelling Generation",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
        zone.DwellingGenerationConfig?.let { config ->
            DwellingConfigEditor(
                config = config,
                onConfigChanged = { newConfig ->
                    onZoneChanged(zone.copy(DwellingGenerationConfig = newConfig))
                },
                onDelete = {
                    onZoneChanged(zone.copy(DwellingGenerationConfig = null))
                }
            )
        } ?: run {
            Button(
                onClick = {
                    onZoneChanged(
                        zone.copy(
                            DwellingGenerationConfig = DwellingGenerationConfig(
                                MinCount = 1,
                                MaxCount = 3,
                                MinTiersCount = 1,
                                MaxTiersCount = 3,
                                UniformDistribution = false,
                                AllowedTiers = listOf(1, 2, 3)
                            )
                        )
                    )
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Add Dwelling Config")
            }
        }

        // Mine Generation Config
        Text("Mine Generation", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 8.dp))
        zone.MineGenerationConfig?.let { config ->
            MineConfigEditor(
                config = config,
                onConfigChanged = { newConfig ->
                    onZoneChanged(zone.copy(MineGenerationConfig = newConfig))
                },
                onDelete = {
                    onZoneChanged(zone.copy(MineGenerationConfig = null))
                }
            )
        } ?: run {
            Button(
                onClick = {
                    onZoneChanged(
                        zone.copy(
                            MineGenerationConfig = MineGenerationConfig(
                                Wood = 1,
                                Ore = 1,
                                Mercury = 1,
                                Crystals = 1,
                                Sulfur = 1,
                                Gems = 1,
                                Gold = 1
                            )
                        )
                    )
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Add Mine Config")
            }
        }

//        DwellingTexture
        var expandedTexture by remember { mutableStateOf(false) }
        Box(modifier = Modifier.padding(8.dp)) {
            ExposedDropdownMenuBox(
                expanded = expandedTexture,
                onExpandedChange = { expandedTexture = !expandedTexture }
            ) {
                OutlinedTextField(
                    value = zone.DwellingTexture.toString(),
                    onValueChange = {},
                    label = { Text("DwellingTexture") },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTexture) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedTexture,
                    onDismissRequest = { expandedTexture = false }
                ) {
                    BuildingTexture.values().forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.toString()) },
                            onClick = {
                                onZoneChanged(zone.copy(DwellingTexture = type))
                                expandedTexture = false
                            }
                        )
                    }
                }
            }
        }


        // Town checkbox
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
            Checkbox(
                checked = zone.Town ?: false,
                onCheckedChange = { newValue ->
                    onZoneChanged(zone.copy(Town = newValue))
                }
            )
            Text("Has Town", modifier = Modifier.padding(start = 8.dp))
        }

        // Creatures Configuration
        Text(
            "Creatures Configuration",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
        zone.CreaturesConfiguration?.let { config ->
            CreaturesConfigEditor(
                config = config,
                onConfigChanged = { newConfig ->
                    onZoneChanged(zone.copy(CreaturesConfiguration = newConfig))
                },
                onDelete = {
                    onZoneChanged(zone.copy(CreaturesConfiguration = null))
                }
            )
        } ?: run {
            Button(
                onClick = {
                    onZoneChanged(
                        zone.copy(
                            CreaturesConfiguration = CreaturesConfiguration(
                                ReplacementsCount = IntValueConfig(1, 3),
                                TerrainFaction = true,
                                NonPlayersFactions = true,
                                NoGrades = false,
                                Grades = true,
                                Neutrals = true,
                                BaseCostMultiplier = 1.0,
                                BaseResourcesMultiplier = 1.0,
                                BaseGrowMultiplier = 1.0,
                                CreatureModifiers = emptyList(),
                                CreatureTierReplacements = emptyList(),
                                NonUniqueReplacements = false
                            )
                        )
                    )
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Add Creatures Config")
            }
        }

        // All the IntValueConfig fields
        val intConfigFields = listOf(
            "AbandonedMines" to zone.AbandonedMines,
            "UpgBuildingsDensity" to zone.UpgBuildingsDensity,
            "TreasureDensity" to zone.TreasureDensity,
            "TreasureChestDensity" to zone.TreasureChestDensity,
            "Prisons" to zone.Prisons,
            "TownGuardStrenght" to zone.TownGuardStrenght,
            "ShopPoints" to zone.ShopPoints,
            "ShrinePoints" to zone.ShrinePoints,
            "LuckMoralBuildingsDensity" to zone.LuckMoralBuildingsDensity,
            "ResourceBuildingsDensity" to zone.ResourceBuildingsDensity,
            "TreasureBuildingPoints" to zone.TreasureBuildingPoints,
            "TreasureBlocksTotalValue" to zone.TreasureBlocksTotalValue,
            "DenOfThieves" to zone.DenOfThieves,
            "RedwoodObservatoryDensity" to zone.RedwoodObservatoryDensity,
            "Size" to zone.Size,
            "ZoneStartPointX" to zone.ZoneStartPointX,
            "ZoneStartPointY" to zone.ZoneStartPointY,
            "MainTownStartPointX" to zone.MainTownStartPointX,
            "MainTownStartPointY" to zone.MainTownStartPointY,
            "MainTownRotationDirection" to zone.MainTownRotationDirection,
            "DistBetweenTreasureBlocks" to zone.DistBetweenTreasureBlocks
        )

        Text("Zone Parameters", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 8.dp))
        intConfigFields.forEach { (fieldName, config) ->
            IntConfigEditor(
                name = fieldName,
                config = config,
                onConfigChanged = { newConfig ->
                    val updatedZone = when (fieldName) {
                        "AbandonedMines" -> zone.copy(AbandonedMines = newConfig)
                        "UpgBuildingsDensity" -> zone.copy(UpgBuildingsDensity = newConfig)
                        "TreasureDensity" -> zone.copy(TreasureDensity = newConfig)
                        "TreasureChestDensity" -> zone.copy(TreasureChestDensity = newConfig)
                        "Prisons" -> zone.copy(Prisons = newConfig)
                        "TownGuardStrenght" -> zone.copy(TownGuardStrenght = newConfig)
                        "ShopPoints" -> zone.copy(ShopPoints = newConfig)
                        "ShrinePoints" -> zone.copy(ShrinePoints = newConfig)
                        "LuckMoralBuildingsDensity" -> zone.copy(LuckMoralBuildingsDensity = newConfig)
                        "ResourceBuildingsDensity" -> zone.copy(ResourceBuildingsDensity = newConfig)
                        "TreasureBuildingPoints" -> zone.copy(TreasureBuildingPoints = newConfig)
                        "TreasureBlocksTotalValue" -> zone.copy(TreasureBlocksTotalValue = newConfig)
                        "DenOfThieves" -> zone.copy(DenOfThieves = newConfig)
                        "RedwoodObservatoryDensity" -> zone.copy(RedwoodObservatoryDensity = newConfig)
                        "Size" -> zone.copy(Size = newConfig)
                        "ZoneStartPointX" -> zone.copy(ZoneStartPointX = newConfig)
                        "ZoneStartPointY" -> zone.copy(ZoneStartPointY = newConfig)
                        "MainTownStartPointX" -> zone.copy(MainTownStartPointX = newConfig)
                        "MainTownStartPointY" -> zone.copy(MainTownStartPointY = newConfig)
                        "MainTownRotationDirection" -> zone.copy(MainTownRotationDirection = newConfig)
                        "DistBetweenTreasureBlocks" -> zone.copy(DistBetweenTreasureBlocks = newConfig)
                        else -> zone
                    }
                    onZoneChanged(updatedZone)
                },
                onDelete = {
                    val updatedZone = when (fieldName) {
                        "AbandonedMines" -> zone.copy(AbandonedMines = null)
                        "UpgBuildingsDensity" -> zone.copy(UpgBuildingsDensity = null)
                        "TreasureDensity" -> zone.copy(TreasureDensity = null)
                        "TreasureChestDensity" -> zone.copy(TreasureChestDensity = null)
                        "Prisons" -> zone.copy(Prisons = null)
                        "TownGuardStrenght" -> zone.copy(TownGuardStrenght = null)
                        "ShopPoints" -> zone.copy(ShopPoints = null)
                        "ShrinePoints" -> zone.copy(ShrinePoints = null)
                        "LuckMoralBuildingsDensity" -> zone.copy(LuckMoralBuildingsDensity = null)
                        "ResourceBuildingsDensity" -> zone.copy(ResourceBuildingsDensity = null)
                        "TreasureBuildingPoints" -> zone.copy(TreasureBuildingPoints = null)
                        "TreasureBlocksTotalValue" -> zone.copy(TreasureBlocksTotalValue = null)
                        "DenOfThieves" -> zone.copy(DenOfThieves = null)
                        "RedwoodObservatoryDensity" -> zone.copy(RedwoodObservatoryDensity = null)
                        "Size" -> zone.copy(Size = null)
                        "ZoneStartPointX" -> zone.copy(ZoneStartPointX = null)
                        "ZoneStartPointY" -> zone.copy(ZoneStartPointY = null)
                        "MainTownStartPointX" -> zone.copy(MainTownStartPointX = null)
                        "MainTownStartPointY" -> zone.copy(MainTownStartPointY = null)
                        "MainTownRotationDirection" -> zone.copy(MainTownRotationDirection = null)
                        "DistBetweenTreasureBlocks" -> zone.copy(DistBetweenTreasureBlocks = null)
                        else -> zone
                    }
                    onZoneChanged(updatedZone)
                }
            )
        }

        // Boolean fields
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
            Checkbox(
                checked = zone.TreasureBlocksScalingFromTownDist ?: false,
                onCheckedChange = { newValue ->
                    onZoneChanged(zone.copy(TreasureBlocksScalingFromTownDist = newValue))
                }
            )
            Text("Treasure Blocks Scaling From Town Distance", modifier = Modifier.padding(start = 8.dp))
        }
    }
}