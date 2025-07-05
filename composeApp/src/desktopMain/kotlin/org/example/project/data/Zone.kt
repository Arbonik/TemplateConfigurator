package org.example.project.data

import kotlinx.serialization.Serializable
import org.example.project.data.enums.BuildingTexture
import org.example.project.data.enums.TerrainType

@Serializable
data class Zone(
    val ZoneId: Int, // индекс зоны из шаблона
    val TerrainType: TerrainType,
    val MirrorZoneId: Int?,
    val DwellingGenerationConfig: DwellingGenerationConfig? = null,
    val MineGenerationConfig: MineGenerationConfig? = null,
    val DwellingTexture: BuildingTexture,
    val AbandonedMines : IntValueConfig? = null,
    val UpgBuildingsDensity : IntValueConfig? = null,
    val TreasureDensity : IntValueConfig? = null,
    val TreasureChestDensity : IntValueConfig? = null,
    val Prisons : IntValueConfig? = null,
    val TownGuardStrenght : IntValueConfig? = null,
    val ShopPoints: IntValueConfig? = null,
    val ShrinePoints : IntValueConfig? = null,
    val LuckMoralBuildingsDensity : IntValueConfig? = null,
    val ResourceBuildingsDensity : IntValueConfig? = null,
    val TreasureBuildingPoints : IntValueConfig? = null,
    val TreasureBlocksTotalValue : IntValueConfig? = null,
    val DenOfThieves : IntValueConfig? = null,
    val RedwoodObservatoryDensity : IntValueConfig? = null,
    val Size : IntValueConfig? = null,
    val Town : Boolean? = null,
    val CreaturesConfiguration: CreaturesConfiguration? = null,
    val ZoneStartPointX : IntValueConfig? = null,
    val ZoneStartPointY : IntValueConfig? = null,
    val MainTownStartPointX : IntValueConfig? = null,
    val MainTownStartPointY : IntValueConfig? = null,
    val MainTownRotationDirection : IntValueConfig? = null,
    val TreasureBlocksScalingFromTownDist : Boolean? = null,
    val DistBetweenTreasureBlocks : IntValueConfig? = null,
)