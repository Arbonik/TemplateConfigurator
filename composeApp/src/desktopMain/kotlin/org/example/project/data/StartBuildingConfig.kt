package org.example.project.data

import kotlinx.serialization.Serializable
import org.example.project.data.enums.BuildingMode
import org.example.project.data.enums.TerrainType

@Serializable
data class StartBuildingConfig(
    val ApplyAllTerrains: Boolean? = null,
    val TerrainType: TerrainType,
    val Buildings: List<String>,
    val BuildingMode: BuildingMode,
)