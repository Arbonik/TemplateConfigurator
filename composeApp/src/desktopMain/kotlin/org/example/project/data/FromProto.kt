import org.example.project.data.enums.CastleType
import org.example.project.data.enums.BuildingTextureConfig
import org.example.project.data.enums.DefaultBuilding
import org.example.project.data.ScriptBuilding
import org.example.project.data.enums.MagicSchool
import org.example.project.data.enums.ArtifactSlot
import org.example.project.data.enums.ArtifactCategory
import org.example.project.data.enums.ArtifactType
import org.example.project.data.enums.BuildingMode
import org.example.project.data.enums.BuildingType
import org.example.project.data.enums.PlayerType
import org.example.project.data.enums.SkillType
import org.example.project.data.enums.HeroClassType
import org.example.project.data.enums.HeroType
import org.example.project.data.enums.SpellType
import org.example.project.data.enums.TerrainType

data class TemplateGenerationConfig(
    val templateName: String,
    val zones: List<ZoneGenerationConfig>,
    val connections: List<ConnectionModel>,
    val terrainConfigs: List<TerrainConfig>,
    val startBuildingConfigs: List<StartBuildingConfig>,
    val generalData: GeneralData,
    val baseArmyMultiplier: Double?,
    val armyMultipliers: Map<String, Double>,
    val scriptFeaturesConfig: ScriptFeaturesConfig,
    val entitiesBanConfig: EntitiesBanModel,
    val startSpellsConfig: StartSpellsConfig,
    val customBuildingConfigs: List<CustomBuildingConfig>,
    val creatureBanksPool: CreatureBanksPool,
    val zoneRandomizationConfig: ZoneRandomizationConfig
)

data class EntitiesBanModel(
    val bannedHeroes: List<HeroType>,
    val bannedSpells: List<SpellType>,
    val bannedArtifacts: List<ArtifactType>,
    val bannedBases: BasesBanModel,
    val banMaradeur: Boolean?
)

data class GeneralData(
    val mine1LevelGuardLevel: Int?,
    val mine2LevelGuardLevel: Int?,
    val mineGoldGuardLevel: Int?
)

data class IntValueConfig(
    val minValue: Int?,
    val maxValue: Int?
)

data class CreaturesConfiguration(
    val replacementsCount: IntValueConfig,
    val terrainFaction: Boolean?,
    val nonPlayersFactions: Boolean?,
    val noGrades: Boolean?,
    val grades: Boolean?,
    val neutrals: Boolean?,
    val baseCostMultiplier: Double?,
    val baseResourcesMultiplier: Double?,
    val baseGrowMultiplier: Double?,
    val creatureModifiers: List<CreatureModifier>,
    val creatureTierReplacements: List<CreatureTierReplacement>,
    val nonUniqueReplacements: Boolean?
)

data class CreatureModifier(
    val tier: Int,
    val costMultiplier: Double?,
    val resourcesMultiplier: Double?,
    val growMultiplier: Double?
)

data class CreatureTierReplacement(
    val tier: Int,
    val creatureIds: List<String>
)

data class DwellingGenerationConfig(
    val buildingTexture: BuildingTextureConfig,
    val creaturesConfiguration: CreaturesConfiguration,
    val generationType: GenerationType
) {
    sealed class GenerationType {
        data class RandomDwellingConfig(
            val minCount: Int,
            val maxCount: Int,
            val minTiersCount: Int?,
            val maxTiersCount: Int?,
            val uniformDistribution: Boolean?,
            val allowedTiers: List<Long>,
            val minCountPerTier: Int?,
            val maxCountPerTier: Int?
        ) : GenerationType()

        data class StaticDwellingConfigs(
            val dwellingValue: List<DwellingValue>
        ) : GenerationType()

        data class DwellingByPointsConfig(
            val pointsCount: Long,
            val dwellingPoints: DwellingValue,
            val dwellingPointsByFaction: Map<String, DwellingValue>,
            val minTiersCount: Int?,
            val maxTiersCount: Int?,
            val allowedTiers: List<Long>,
            val minCountPerTier: DwellingValue,
            val minCountPerTierByFaction: Map<String, DwellingValue>,
            val maxCountPerTier: DwellingValue,
            val maxCountPerTierByFaction: Map<String, DwellingValue>
        ) : GenerationType()

        data class DependantDwellingConfig(
            val zoneId: Long,
            val minCount: Int,
            val maxCount: Int,
            val minTiersCount: Int?,
            val maxTiersCount: Int?,
            val uniformDistribution: Boolean?,
            val minCountPerTier: Int?,
            val maxCountPerTier: Int?,
            val isCopyMode: Boolean
        ) : GenerationType()
    }
}

data class DwellingValue(
    val t1: Int?,
    val t2: Int?,
    val t3: Int?,
    val t4: Int?,
    val t5: Int?,
    val t6: Int?,
    val t7: Int?
)

data class ResourcesConfig(
    val wood: IntValueConfig,
    val ore: IntValueConfig,
    val mercury: IntValueConfig,
    val crystals: IntValueConfig,
    val sulfur: IntValueConfig,
    val gems: IntValueConfig,
    val gold: IntValueConfig
)

data class ZoneGenerationConfig(
    val zoneId: Int,
    val terrainType: TerrainType,
    val mirrorZoneId: Int?,
    val dwellingGenerationConfig: DwellingGenerationConfig?,
    val mineGenerationConfig: ResourcesConfig?,
    val abandonedMines: IntValueConfig?,
    val upgBuildingsDensity: IntValueConfig?,
    val treasureDensity: IntValueConfig?,
    val treasureChestDensity: IntValueConfig?,
    val prisons: IntValueConfig?,
    val townGuardStrenght: IntValueConfig?,
    val shopPoints: IntValueConfig?,
    val shrinePoints: IntValueConfig?,
    val luckMoralBuildingsDensity: IntValueConfig?,
    val resourceBuildingsDensity: IntValueConfig?,
    val treasureBuildingPoints: IntValueConfig?,
    val treasureBlocksTotalValue: IntValueConfig?,
    val denOfThieves: IntValueConfig?,
    val redwoodObservatoryDensity: IntValueConfig?,
    val size: IntValueConfig?,
    val town: Boolean?,
    val zoneStartPointX: IntValueConfig?,
    val zoneStartPointY: IntValueConfig?,
    val mainTownStartPointX: IntValueConfig?,
    val mainTownStartPointY: IntValueConfig?,
    val mainTownRotationDirection: IntValueConfig?,
    val treasureBlocksScalingFromTownDist: Boolean?,
    val distBetweenTreasureBlocks: IntValueConfig?
)

data class ConnectionModel(
    val sourceZoneIndex: Int,
    val destZoneIndex: Int,
    val isMain: Boolean,
    val removeConnection: Boolean?,
    val twoWay: Boolean?,
    val guardStrenght: Int?,
    val guarded: Boolean?,
    val wide: Boolean?,
    val staticPos: Boolean?,
    val startPointX: Int?,
    val startPointY: Int?,
    val minRadiusToSearch: Int?,
    val maxRadiusToSearch: Int?,
    val minRadiusToMain: Int?,
    val maxRadiusToMain: Int?,
    val roadType: Int?
)

data class TerrainConfig(
    val terrainType: TerrainType,
    val mirrorTerrainType: TerrainType,
    val buildingsToDelete: List<String>,
    val buildingsToAdd: List<Int>,
    val newLuckMoraleBuildings: TerrainBuildingsConfig,
    val newShopBuildings: TerrainBuildingsConfig,
    val newResourceGivers: TerrainBuildingsConfig,
    val newUpgradeBuildings: TerrainBuildingsConfig,
    val newShrines: TerrainBuildingsConfig,
    val newTreasuryBuildings: TerrainBuildingsConfig,
    val newBuffBuildings: TerrainBuildingsConfig
)

data class TerrainBuildingsConfig(
    val clearBuildings: Boolean?,
    val buildingsToDelete: List<String>,
    val buildingsToAdd: List<Int>,
    val addCreatureBanksPool: Boolean?
)

data class CreatureBanksPool(
    val nonPlayerFactions: Boolean?,
    val playerFactions: Boolean?,
    val banksAmount: IntValueConfig
)

data class CustomBuildingConfig(
    val id: Int,
    val value: Long,
    val guardStrenght: Long,
    val buildingTexture: BuildingTextureConfig,
    val buildingType: BuildingType
) {
    sealed class BuildingType {
        data class CreatureBuildingConfig(
            val tiersPool: List<Long>,
            val noGrades: Boolean?,
            val grades: Boolean?,
            val neutrals: Boolean?,
            val nonPLayerFactions: Boolean?,
            val pLayerFactions: Boolean?,
            val playerType: PlayerType,
            val creatureIds: List<String>,
            val costMultiplier: Double?,
            val resourcesMultiplier: Double?,
            val growMultiplier: Double?,
            val isDwelling: Boolean?
        ) : BuildingType()

        data class XdbRef(val value: String) : BuildingType()
        data class PandoraBoxConfig(
            val goldAmount: List<Long>,
            val expAmount: List<Long>,
            val artifacts: List<PandoraArtifactConfig>,
            val pandoraCreatureConfig: List<PandoraCreatureConfig>,
            val spells: List<PandoraSpellConfig>,
            val resources: List<ResourcesConfig>
        ) : BuildingType()

        data class ScriptBuildingConfig(val scriptBuilding: ScriptBuilding) : BuildingType()
        data class ResourceBuildingConfig(val resourcesConfigs: List<ResourcesConfig>) : BuildingType()
        data class MageEyeConfig(
            val coordinateX: Long,
            val coordinateY: Long,
            val radius: Int?
        ) : BuildingType()

        data class RunicChestConfig(
            val runes: List<SpellType>,
            val runeTiers: List<Long>,
            val count: Int?,
            val expAmount: Int?
        ) : BuildingType()

        data class DefaultBuildingConfig(val defaultBuilding: DefaultBuilding) : BuildingType()
        data class CreatureBankConfig(
            val name: String,
            val creaturesPool: List<String>,
            val guardsPool: List<List<String>>,
            val creatureCostMultiplier: Double?,
            val creatureResourcesMultiplier: Double?,
            val creatureGrowMultiplier: Double?,
            val guardGrowMultiplier: Double?
        ) : BuildingType()
    }
}

data class PandoraArtifactConfig(
    val artifacts: List<ArtifactType>,
    val artifactCategories: List<ArtifactCategory>,
    val artifactSlots: List<ArtifactSlot>,
    val costRanges: List<IntValueConfig>,
    val count: Int?
)

data class PandoraCreatureConfig(
    val tiersPool: List<Long>,
    val noGrades: Boolean?,
    val grades: Boolean?,
    val neutrals: Boolean?,
    val nonPlayerFactions: Boolean?,
    val playerFactions: Boolean?,
    val playerType: PlayerType,
    val creatureIds: List<String>,
    val growMultiplier: Double?
)

data class PandoraSpellConfig(
    val spells: List<SpellType>,
    val magicSchools: List<MagicSchool>,
    val magicTiers: List<Long>,
    val runeTiers: List<Long>,
    val warCryTiers: List<Long>,
    val count: Int?
)

data class StartBuildingConfig(
    val applyAllTerrains: Boolean?,
    val terrainType: TerrainType,
    val buildings: List<BuildingType>,
    val buildingMode: BuildingMode
)

data class ScriptFeaturesConfig(
    val castleCaptureProps: CastleCaptureModel,
    val gmRebuildProps: GMRebuildModel,
    val globallyDisabledBuildingsProps: GloballyDisabledBuildingsModel,
    val forcedFinalBattleProps: ForcedFinalBattleModel,
    val additionalStartCastles: List<AdditionalStartCastle>
)

data class CastleCaptureModel(
    val coordinateX: Long,
    val coordinateY: Long,
    val searchRadius: Int?,
    val eventTimer: Long,
    val disableFortifications: Boolean?,
    val isForcedFinalBattle: Boolean
)

data class AdditionalStartCastle(
    val startCoordinateX: Long,
    val startCoordinateY: Long,
    val searchRadius: Int?,
    val targetCoordinateX: Long,
    val targetCoordinateY: Long,
    val targetSearchRadius: Int?
)

data class ResourcesModel(
    val wood: Int?,
    val ore: Int?,
    val mercury: Int?,
    val sulfur: Int?,
    val gem: Int?,
    val crystal: Int?,
    val gold: Int?
)

data class GMRebuildModel(
    val minimalGMLevel: Long,
    val minimalWarCriesLevel: Long,
    val rebuildCost: ResourcesModel
)

data class GloballyDisabledBuildingsModel(
    val buildings: List<BuildingType>
)

data class ForcedFinalBattleModel(
    val week: Long,
    val day: Long
)

data class ZoneRandomizationConfig(
    val zonesToSwap: List<IntArray>,
    val isSymmetricalSwap: Boolean?,
    val zonesToRandomize: List<Long>
)

data class StartSpellsByPlayer(
    val playerType: PlayerType,
    val spells: List<SpellType>
)

data class StartSpellsByRace(
    val castleType: CastleType,
    val spells: List<SpellType>
)

data class StartSpellsByHero(
    val heroType: HeroType,
    val spells: List<SpellType>
)

data class StartSpellsConfig(
    val globalSpells: List<SpellType>,
    val spellsByPlayers: List<StartSpellsByPlayer>,
    val spellsByRaces: List<StartSpellsByRace>,
    val spellsByHeroes: List<StartSpellsByHero>
)

data class BannedBasesByClass(
    val Class: HeroClassType,
    val Skills: List<SkillType>
)

data class BasesBanModel(
    val commonBannedSkills: List<SkillType>,
    val skillsBannedForClass: List<BannedBasesByClass>
)

