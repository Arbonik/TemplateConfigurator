package org.example.project.data

import kotlinx.serialization.Serializable
//
//data class TemplateGenerationConfig(
//    val TemplateName: String,
//    val Zones: List<Zone>,
//    val BaseArmyMultiplier: Double = 1.0,
//    val ArmyMultipliers: ArmyMultipliers,
//    val Connections: List<ConnectionModel>,
//    val ShopBuildingConfigs: List<ShopBuildingConfig>,
//    val TerrainConfig: List<TerrainConfig>,
//    val ScriptFeaturesConfig: ScriptFeaturesConfig,
//    val EntitiesBanConfig: EntitiesBanConfig,
//    val CreatureBanksPool: CreatureBanksPool,
//    val GeneralData: GeneralData,
//    val StartBuildingConfigs: List<StartBuildingConfig>,
//    val ZoneRandomizationConfig : ZoneRandomizationConfig? = null
//)
//
//data class Zone(
//    val ZoneId: Int, // индекс зоны из шаблона
//    val TerrainType: TerrainType,
//    val MirrorZoneId: Int?,
//    val DwellingGenerationConfig: DwellingGenerationConfig? = null,
//    val MineGenerationConfig: MineGenerationConfig? = null,
//    val DwellingTexture: BuildingTexture,
//    val AbandonedMines : IntValueConfig? = null,
//    val UpgBuildingsDensity : IntValueConfig? = null,
//    val TreasureDensity : IntValueConfig? = null,
//    val TreasureChestDensity : IntValueConfig? = null,
//    val Prisons : IntValueConfig? = null,
//    val TownGuardStrenght : IntValueConfig? = null,
//    val ShopPoints: IntValueConfig? = null,
//    val ShrinePoints : IntValueConfig? = null,
//    val LuckMoralBuildingsDensity : IntValueConfig? = null,
//    val ResourceBuildingsDensity : IntValueConfig? = null,
//    val TreasureBuildingPoints : IntValueConfig? = null,
//    val TreasureBlocksTotalValue : IntValueConfig? = null,
//    val DenOfThieves : IntValueConfig? = null,
//    val RedwoodObservatoryDensity : IntValueConfig? = null,
//    val Size : IntValueConfig? = null,
//    val Town : Boolean? = null,
//    val CreaturesConfiguration: CreaturesConfiguration? = null,
//    val ZoneStartPointX : IntValueConfig? = null,
//    val ZoneStartPointY : IntValueConfig? = null,
//    val MainTownStartPointX : IntValueConfig? = null,
//    val MainTownStartPointY : IntValueConfig? = null,
//    val MainTownRotationDirection : IntValueConfig? = null,
//    val TreasureBlocksScalingFromTownDist : Boolean? = null,
//    val DistBetweenTreasureBlocks : IntValueConfig? = null,
//)
//
//@Serializable
//enum class TerrainType {
//    FirstPlayer,//(0, "террейн первого игрока"),
//    SecondPlayer,//(1, "террейн второго игрока"),
//    Terrain1,//(2, "первый случайный свободный террейн"),
//    Terrain2,//(3, "второй случайный свободный террейн"),
//    Terrain3,//(4, "третий случайный свободный террейн"),
//    Terrain4,//(5, "четвёртый случайный свободный террейн"),
//    Terrain5,//(6, "пятый случайный свободный террейн"),
//    Terrain6,//(7, "шестой случайный свободный террейн")
//}
//@Serializable
//data class DwellingGenerationConfig(
//    val MinCount: Int,
//    val MaxCount: Int,
//    val MinTiersCount: Int,
//    val MaxTiersCount: Int,
//    val UniformDistribution: Boolean,
//    val AllowedTiers: List<Int>,
//)
//
//@Serializable
//data class MineGenerationConfig(
//    val Wood: Int,//Количетсов деревянных шахт
//    val Ore: Int,//Количетсов шахт с рудой
//    val Mercury: Int, //Количетсов шахт с ртутью
//    val Crystals: Int,//Количетсов шахт с кристалами
//    val Sulfur: Int,//Количетсов шахт с серой
//    val Gems: Int,//Количетсов шахт с самоцветами
//    val Gold: Int,//Количетсов шахт с золотом
//)
//
//@Serializable
//data class IntValueConfig(
//    val MaxValue: Int,
//    val MinValue: Int
//)
//
//
//@Serializable
//data class CreaturesConfiguration(
//    val ReplacementsCount: IntValueConfig,
//    val TerrainFaction: Boolean,
//    val NonPlayersFactions: Boolean,
//    val NoGrades: Boolean,
//    val Grades: Boolean,
//    val Neutrals: Boolean,
//    val BaseCostMultiplier: Double,
//    val BaseResourcesMultiplier: Double,
//    val BaseGrowMultiplier: Double? = null,
//    val CreatureModifiers: List<CreatureModifier> = emptyList(),
//    val CreatureTierReplacements: List<CreatureTierReplacement>,
//    val NonUniqueReplacements: Boolean,
//)
//
//@Serializable
//data class CreatureModifier(
//    val Tier: Int,
//    val CostMultiplier: Double? = null,
//    val ResourcesMultiplier: Double? = null,
//    val GrowMultiplier: Double,
//)
//
//@Serializable
//data class CreatureTierReplacement(
//    val CreatureIds: List<String>,
//    val Tier: Int
//)
//
//@Serializable
//data class ArmyMultipliers(
//    val Dwarfs: Double,
//    val Elves: Double,
//    val Horde: Double,
//    val Humans: Double,
//    val Inferno: Double,
//    val Liga: Double,
//    val Mages: Double,
//    val Necropolis: Double
//)
//
//@Serializable
//data class ScriptFeaturesConfig(
//    val CastleCaptureProps: CastleCaptureProps,
//    val GMRebuildProps : GMRebuildModel? = null,
//    val GloballyDisabledBuildingsProps: GloballyDisabledBuildingsProps? = null,
//    val ForcedFinalBattleProps: ForcedFinalBattleModel? = null
//)
//
//@Serializable
//data class CastleCaptureProps(
//    val CoordinateX: Long,
//    val CoordinateY: Long,
//    val SearchRadius: Int,
//    val CaptureTimer: Long,
//    val DisableFortifications: Boolean,
//)
//
//@Serializable
//data class GMRebuildModel(
//    val MinimalGMLevel : Long,
//    val MinimalWarcriesLevel : Long,
//    val RebuildCost : GMRebuildCost
//)
//
//@Serializable
//data class GMRebuildCost(
//    val Wood : Long = 0,
//    val Ore : Long = 0,
//    val Mercury : Long = 0,
//    val Sulfur : Long = 0,
//    val Gem : Long = 0,
//    val Crystal : Long = 0,
//    val Gold : Long = 0
//)
//@Serializable
//data class GloballyDisabledBuildingsProps(
//    val Buildings: List<String>
//)
//
//@Serializable
//data class ForcedFinalBattleModel(
//    val Week : Long,
//    val Day : Long
//)
//
//@Serializable
//data class ConnectionModel(
//    val sourceZoneIndex: Int,
//    val destZoneIndex: Int,
//    val isMain: Boolean,
//    val twoWay: Boolean? = null,
//    val guardStrenght: Int? = null,
//    val guarded: Boolean? = null,
//    val wide: Boolean? = null,
//    val staticPos: Boolean? = null,
//    val startPointX: Int? = null,
//    val startPointY: Int? = null,
//    val minRadiusToSearch: Int? = null,
//    val maxRadiusToSearch: Int? = null,
//    val minRadiusToMain: Int? = null,
//    val maxRadiusToMain: Int? = null,
//    val roadType: Int? = null
//)
//
//@Serializable
//data class ShopBuildingConfig(
//    val Id: Int,
//    val Value: Long,
//    val GuardStrenght: Long,
//    val CreatureBuildingConfig: CreatureBuildingConfig? = null,
//    val CreatureBankConfig: CreatureBankConfig,
//    val XdbRef : String? = null
//)
//
//
//@Serializable
//data class CreatureBuildingConfig(
//    val TiersPool: List<Int>, // набор тиров, один из которых случайно будет выбран для генерации
//    val NoGrades: Boolean?, // добавлять ли негрейд
//    val Grades: Boolean?, // добавлять ли грейд
//    val Neutrals: Boolean?, // добавлять ли нейтралов
//    val CreatureIds: List<String>, // массив существ, одно из которых будет выбрано
//    val CostMultiplier: Double?, // множитель цены в золоте юнитов
//    val ResourcesMultiplier: Double?, // множитель цены в ресурсах юнитов
//    val GrowMultiplier: Double? // множитель прироста юнитов
//)
//
//@Serializable
//data class CreatureBankConfig(
//    val Name: String,
//    val CreaturesPool: List<String>,
//    val GuardsPool: List<GuardsPool>,
//    val CreatureCostMultiplier: Double,
//    val CreatureGrowMultiplier: Double,
//    val CreatureResourcesMultiplier: Double,
//    val GuardGrowMultiplier: Double
//)
//
//@Serializable
//data class GuardsPool(
//    val Values: List<String>
//)
//
//@Serializable
//data class TerrainConfig(
//    val TerrainType: TerrainType,
//    val MirrorTerrainType: Int? = null,
//    val BuildingsToDelete: List<String>,
//    val BuildingsToAdd: List<Int>,
//    val NewLuckMoraleBuildings: TerrainBuildingsConfig? = null,
//    val NewShopBuildings: TerrainBuildingsConfig? = null,
//    val NewResourceGivers: TerrainBuildingsConfig? = null,
//    val NewUpgradeBuildings: TerrainBuildingsConfig? = null,
//    val NewShrines: TerrainBuildingsConfig? = null,
//    val NewTreasuryBuildings: TerrainBuildingsConfig? = null,
//    val NewBuffBuildings: TerrainBuildingsConfig? = null,
//)
//
//@Serializable
//data class TerrainBuildingsConfig(
//    val ClearBuildings: Boolean? = null,
//    val BuildingsToDelete: List<String>,
//    val BuildingsToAdd: List<Int>,
//    val AddCreatureBanksPool: Boolean,
//)
//
//@Serializable
//data class GeneralData(
//    val DisableDwellingIcon: Boolean,
//    val Mine1LevelGuardLevel : Int? = null,
//    val Mine2LevelGuardLevel : Int? = null,
//    val MineGoldGuardLevel : Int? = null,
//)
//
//@Serializable
//data class StartBuildingConfig(
//    val ApplyAllTerrains: Boolean? = null,
//    val TerrainType: TerrainType,
//    val Buildings: List<String>,
//    val BuildingMode: String,
//)
//
//@Serializable
//data class ZoneRandomizationConfig(
//    val ZonesToSwap: List<Int>,
//    val IsSymmetricalSwap : Boolean,
//    val ZonesToRandomize : Long
//)
//@Serializable
//data class CreatureBanksPool(
//    val BanksAmount: IntValueConfig,
//    val NonPlayerFactions: Boolean,
//    val PlayerFactions: Boolean
//)
//
//@Serializable
//data class EntitiesBanConfig(
//    val BannedArtifacts: List<String>,
//    val BannedHeroes: List<String>,
//    val BannedSpells: List<String>
//)
//
//
//
