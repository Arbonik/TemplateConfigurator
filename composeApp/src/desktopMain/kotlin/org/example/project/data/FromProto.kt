import kotlinx.serialization.Serializable
import org.example.project.data.enums.CastleType
import java.util.LinkedHashMap

//import kotlinx.serialization.Serializable
//
//@Serializable
//data class TemplateGenerationConfig(
//    val TemplateName: String,
//    val Zones: List<ZoneGenerationConfig>,
//    val Connections: List<ConnectionModel>,
//    val TerrainConfigs: List<TerrainConfig>,
//    val StartBuildingConfigs: List<StartBuildingConfig>,
//    val GeneralData: GeneralData,
//    val BaseArmyMultiplier: Double? = null,
//    val ArmyMultipliers: Map<String, Double>,
//    val ScriptFeaturesConfig: ScriptFeaturesConfig,
//    val EntitiesBanConfig: EntitiesBanModel,
//    val StartSpellsConfig: StartSpellsConfig,
//    val CustomBuildingConfigs: List<CustomBuildingConfig>,
//    val CreatureBanksPool: CreatureBanksPool,
//    val ZoneRandomizationConfig: ZoneRandomizationConfig
//)
//
@Serializable
data class EntitiesBanModel(
    val BannedHeroes: List<HeroType> = listOf(),
    val BannedSpells: List<SpellType> = listOf(),
    val BannedArtifacts: List<ArtifactType> = listOf(),
    val BannedBases: BasesBanModel = BasesBanModel(),
    val BanMaradeur: Boolean? = null
)


@Serializable
data class GeneralData(
    val Mine1LevelGuardLevel: Int? = null,
    val Mine2LevelGuardLevel: Int? = null,
    val MineGoldGuardLevel: Int? = null
)

@Serializable
data class IntValueConfig(
    val MinValue: Int? = null,
    val MaxValue: Int? = null
) {
    constructor(value: Int) : this(value, value)
}

//
@Serializable
data class CreaturesConfiguration(
    val ReplacementsCount: IntValueConfig,
    val TerrainFaction: Boolean? = null,
    val NonPlayersFactions: Boolean? = null,
    val NoGrades: Boolean? = null,
    val Grades: Boolean? = null,
    val Neutrals: Boolean? = null,
    val BaseCostMultiplier: Double? = null,
    val BaseResourcesMultiplier: Double? = null,
    val BaseGrowMultiplier: Double? = null,
    val CreatureModifiers: List<CreatureModifier>? = null,
    val CreatureTierReplacements: List<CreatureTierReplacement>? = null,
    val NonUniqueReplacements: Boolean? = null
)

@Serializable
data class CreatureModifier(
    val Tier: Int,
    val CostMultiplier: Double? = null,
    val ResourcesMultiplier: Double? = null,
    val GrowMultiplier: Double? = null
)

@Serializable
data class CreatureTierReplacement(
    val Tier: Int,
    val CreatureIds: List<String>
)


@Serializable
data class DwellingGenerationConfig(
    val BuildingTexture: BuildingTextureConfig,
    val CreaturesConfiguration: CreaturesConfiguration,
    val RandomDwellingConfig: RandomDwellingConfig? = null,
    val StaticDwellingConfigs: StaticDwellingConfigs? = null,
    val DwellingByPointsConfig: DwellingByPointsConfig? = null,
    val DependantDwellingConfig: DependantDwellingConfig? = null,
)

@Serializable
data class RandomDwellingConfig(
    val MinCount: Int,
    val MaxCount: Int,
    val MinTiersCount: Int? = null,
    val MaxTiersCount: Int? = null,
    val UniformDistribution: Boolean? = null,
    val AllowedTiers: List<Long>,
    val MinCountPerTier: Int? = null,
    val MaxCountPerTier: Int? = null
)

@Serializable
data class StaticDwellingConfigs(
    val DwellingValue: List<DwellingValue>
)

@Serializable
data class DwellingByPointsConfig(
    val PointsCount: Long,
    val DwellingPoints: DwellingValue,
    val DwellingPointsByFaction: Map<String, DwellingValue>,
    val MinTiersCount: Int? = null,
    val MaxTiersCount: Int? = null,
    val AllowedTiers: List<Long>,
    val MinCountPerTier: DwellingValue,
    val MinCountPerTierByFaction: Map<String, DwellingValue>,
    val MaxCountPerTier: DwellingValue,
    val MaxCountPerTierByFaction: Map<String, DwellingValue>
)

@Serializable
data class DependantDwellingConfig(
    val ZoneId: Long,
    val MinCount: Int,
    val MaxCount: Int,
    val MinTiersCount: Int? = null,
    val MaxTiersCount: Int? = null,
    val UniformDistribution: Boolean? = null,
    val MinCountPerTier: Int? = null,
    val MaxCountPerTier: Int? = null,
    val IsCopyMode: Boolean
)

@Serializable
data class DwellingValue(
    val T1: Int? = null,
    val T2: Int? = null,
    val T3: Int? = null,
    val T4: Int? = null,
    val T5: Int? = null,
    val T6: Int? = null,
    val T7: Int? = null
) {
    fun isEmpty() = T1 == null && T2 == null && T3 == null && T4 == null && T5 == null && T6 == null && T7 == null
}

//
@Serializable
data class ResourcesConfig(
    val Wood: IntValueConfig?,
    val Ore: IntValueConfig?,
    val Mercury: IntValueConfig?,
    val Crystals: IntValueConfig?,
    val Sulfur: IntValueConfig?,
    val Gems: IntValueConfig?,
    val Gold: IntValueConfig?
)

//
//
@Serializable
data class ZoneGenerationConfig(
    val ZoneId: Int,
    val TerrainType: TerrainType,
    val MirrorZoneId: Int? = null,
    val DwellingGenerationConfig: DwellingGenerationConfig? = null,
    val MineGenerationConfig: ResourcesConfig? = null,
    val AbandonedMines: IntValueConfig? = null,
    val UpgBuildingsDensity: IntValueConfig? = null,
    val TreasureDensity: IntValueConfig? = null,
    val TreasureChestDensity: IntValueConfig? = null,
    val Prisons: IntValueConfig? = null,
    val TownGuardStrenght: IntValueConfig? = null,
    val ShopPoints: IntValueConfig? = null,
    val ShrinePoints: IntValueConfig? = null,
    val LuckMoralBuildingsDensity: IntValueConfig? = null,
    val ResourceBuildingsDensity: IntValueConfig? = null,
    val TreasureBuildingPoints: IntValueConfig? = null,
    val TreasureBlocksTotalValue: IntValueConfig? = null,
    val DenOfThieves: IntValueConfig? = null,
    val RedwoodObservatoryDensity: IntValueConfig? = null,
    val Size: IntValueConfig? = null,
    val Town: Boolean? = null,
    val ZoneStartPointX: IntValueConfig? = null,
    val ZoneStartPointY: IntValueConfig? = null,
    val MainTownStartPointX: IntValueConfig? = null,
    val MainTownStartPointY: IntValueConfig? = null,
    val MainTownRotationDirection: IntValueConfig? = null,
    val TreasureBlocksScalingFromTownDist: Boolean? = null,
    val DistBetweenTreasureBlocks: IntValueConfig? = null
)

@Serializable
data class ConnectionModel(
    val SourceZoneIndex: Int,
    val DestZoneIndex: Int,
    val IsMain: Boolean,
    val RemoveConnection: Boolean? = null,
    val TwoWay: Boolean? = null,
    val GuardStrenght: Int? = null,
    val Guarded: Boolean? = null,
    val Wide: Boolean? = null,
    val StaticPos: Boolean? = null,
    val StartPointX: Int? = null,
    val StartPointY: Int? = null,
    val MinRadiusToSearch: Int? = null,
    val MaxRadiusToSearch: Int? = null,
    val MinRadiusToMain: Int? = null,
    val MaxRadiusToMain: Int? = null,
    val RoadType: Int? = null
)

@Serializable
data class TerrainConfig(
    val TerrainType: TerrainType,
    val MirrorTerrainType: TerrainType? = null,
    val BuildingsToDelete: List<String> = listOf(),
    val BuildingsToAdd: List<Int> = listOf(),
    val NewLuckMoraleBuildings: TerrainBuildingsConfig? = null,
    val NewShopBuildings: TerrainBuildingsConfig? = null,
    val NewResourceGivers: TerrainBuildingsConfig? = null,
    val NewUpgradeBuildings: TerrainBuildingsConfig? = null,
    val NewShrines: TerrainBuildingsConfig? = null,
    val NewTreasuryBuildings: TerrainBuildingsConfig? = null,
    val NewBuffBuildings: TerrainBuildingsConfig? = null
)

@Serializable
data class TerrainBuildingsConfig(
    val ClearBuildings: Boolean? = null,
    val BuildingsToDelete: List<String> = listOf(),
    val BuildingsToAdd: List<Int> = listOf(),
    val AddCreatureBanksPool: Boolean? = null
) {
    fun objects(name: String) = when (name) {
        "NewLuckMoraleBuildings" -> listOf(
            MapBuilding.FAERIE_RING,
            MapBuilding.FOUNTAIN_OF_FORTUNE,
            MapBuilding.IDOL_OF_FORTUNE,
            MapBuilding.TEMPLE,
            MapBuilding.RALLY_FLAG,
            MapBuilding.RANDOM_SANCTUARY,
            MapBuilding.SHAMAN_OF_NOMMADS,
            MapBuilding.FOUNTAIN_OF_YOUTH,
            MapBuilding.LAKE_OF_SCARLETT_SWAN,
            MapBuilding.LAKE_OF_MERMAIDS,
            MapBuilding.STABLES,
        )

        "NewShopBuildings" -> listOf(
            MapBuilding.ELEMENTAL_CONFLUX,
            MapBuilding.WAR_MACHINE_FACTORY,
            MapBuilding.HOUSE_OF_ASTROLOGER,
            MapBuilding.REFUGEE_CAMP,
            MapBuilding.SPELL_SHOP,
            MapBuilding.BLACK_MARKET,
        )

        "NewResourceGivers" -> listOf(
            MapBuilding.MYSTICAL_GARDEN,
            MapBuilding.WINDMILL,
            MapBuilding.ENCHANTED_TREASURE,

            )

        "NewUpgradeBuildings" -> listOf(
            MapBuilding.MERCENARY_CAMP,
            MapBuilding.MARLETTO_TOWER,
            MapBuilding.GARDEN_OF_REVELATION,
            MapBuilding.STAR_AXIS,
            MapBuilding.WAR_ACADEMY,
            MapBuilding.MAGIC_SPRING,
            MapBuilding.TREE_OF_KNOWLEDGE,
            MapBuilding.SCHOOL_OF_MAGIC,
            MapBuilding.ARENA,
            MapBuilding.LIBRARY_OF_ENLIGHTENMENT,
        )

        "NewShrines" -> listOf(
            MapBuilding.SHRINE_OF_MAGIC_1,
            MapBuilding.SHRINE_OF_MAGIC_2,
            MapBuilding.SHRINE_OF_MAGIC_3,
        )

        "NewTreasuryBuildings" -> listOf(
            MapBuilding.CRYPT,
            MapBuilding.GARGOYLE_STONEVAULT,
            MapBuilding.DWARVEN_TREASURY,
            MapBuilding.DEMONBANK,
            MapBuilding.ELEMENTAL_STOCKPILE,
            MapBuilding.MONASTERYBANK,
        )

        "NewBuffBuildings" -> listOf(
            MapBuilding.ELEMENTAL_STOCKPILE,
            MapBuilding.MONASTERYBANK,
            MapBuilding.MAGIC_SPRING,
            MapBuilding.TREANT_THICKET,
            MapBuilding.WITCH_BANK,
            MapBuilding.NECRO_ESTATE,
            MapBuilding.ORC_DEPOSIT,
            MapBuilding.DRAGON_UTOPIA,
        )

        else -> emptyList()
    }
}

//
//@Serializable
//data class CreatureBanksPool(
//    val NonPlayerFactions: Boolean? = null,
//    val PlayerFactions: Boolean? = null,
//    val BanksAmount: IntValueConfig
//)
//
//@Serializable
//data class CustomBuildingConfig(
//    val Id: Int,
//    val Value: Long,
//    val GuardStrenght: Long,
//    val BuildingTexture: BuildingTextureConfig,
//    val CreatureBuildingConfig: SealedBuildingType.CreatureBuildingConfig? = null,
//    val XdbRef: SealedBuildingType.XdbRef? = null,
//    val PandoraBoxConfig: SealedBuildingType.PandoraBoxConfig? = null,
//    val ScriptBuildingConfig: SealedBuildingType.ScriptBuildingConfig? = null,
//    val ResourceBuildingConfig: SealedBuildingType.ResourceBuildingConfig? = null,
//    val MageEyeConfig: SealedBuildingType.MageEyeConfig? = null,
//    val RunicChestConfig: SealedBuildingType.RunicChestConfig? = null,
//    val DefaultBuildingConfig: SealedBuildingType.DefaultBuildingConfig? = null,
//    val CreatureBankConfig: SealedBuildingType.CreatureBankConfig? = null,
//)
//
//data class UiCustomBuildingConfig(
//    val Id: Int,
//    val Value: Long,
//    val GuardStrenght: Long,
//    val BuildingTexture: BuildingTextureConfig,
//    val buildingType: BuildingType
//)
//
//@Serializable
//sealed class SealedBuildingType {
//    @Serializable
//    data class CreatureBuildingConfig(
//        val TiersPool: List<Long>,
//        val NoGrades: Boolean? = null,
//        val Grades: Boolean? = null,
//        val Neutrals: Boolean? = null,
//        val NonPLayerFactions: Boolean? = null,
//        val PLayerFactions: Boolean? = null,
//        val PlayerType: PlayerType,
//        val CreatureIds: List<String>,
//        val CostMultiplier: Double? = null,
//        val ResourcesMultiplier: Double? = null,
//        val GrowMultiplier: Double? = null,
//        val IsDwelling: Boolean? = null
//    ) : SealedBuildingType()
//
//    @Serializable
//    data class XdbRef(val Value: String) : SealedBuildingType()
//
//    @Serializable
//    data class PandoraBoxConfig(
//        val GoldAmount: List<Long>,
//        val ExpAmount: List<Long>,
//        val Artifacts: List<PandoraArtifactConfig>,
//        val PandoraCreatureConfig: List<PandoraCreatureConfig>,
//        val Spells: List<PandoraSpellConfig>,
//        val Resources: List<ResourcesConfig>
//    ) : SealedBuildingType()
//
//    @Serializable
//    data class ScriptBuildingConfig(val ScriptBuilding: ScriptBuilding) : SealedBuildingType()
//
//    @Serializable
//    data class ResourceBuildingConfig(val ResourcesConfigs: List<ResourcesConfig>) : SealedBuildingType()
//
//    @Serializable
//    data class MageEyeConfig(
//        val CoordinateX: Long,
//        val CoordinateY: Long,
//        val Radius: Int? = null
//    ) : SealedBuildingType()
//
//    @Serializable
//    data class RunicChestConfig(
//        val Runes: List<SpellType>,
//        val RuneTiers: List<Long>,
//        val Count: Int? = null,
//        val ExpAmount: Int? = null
//    ) : SealedBuildingType()
//
//    @Serializable
//    data class DefaultBuildingConfig(val DefaultBuilding: DefaultBuilding) : SealedBuildingType()
//
//    @Serializable
//    data class CreatureBankConfig(
//        val Name: String,
//        val CreaturesPool: List<String>,
//        val GuardsPool: List<List<String>>,
//        val CreatureCostMultiplier: Double? = null,
//        val CreatureResourcesMultiplier: Double? = null,
//        val CreatureGrowMultiplier: Double? = null,
//        val GuardGrowMultiplier: Double? = null
//    ) : SealedBuildingType()
//}
//
//@Serializable
//data class PandoraArtifactConfig(
//    val Artifacts: List<ArtifactType>,
//    val ArtifactCategories: List<ArtifactCategory>,
//    val ArtifactSlots: List<ArtifactSlot>,
//    val CostRanges: List<IntValueConfig>,
//    val Count: Int? = null
//)
//
//@Serializable
//data class PandoraCreatureConfig(
//    val TiersPool: List<Long>,
//    val NoGrades: Boolean? = null,
//    val Grades: Boolean? = null,
//    val Neutrals: Boolean? = null,
//    val NonPlayerFactions: Boolean? = null,
//    val PlayerFactions: Boolean? = null,
//    val PlayerType: PlayerType,
//    val CreatureIds: List<String>,
//    val GrowMultiplier: Double? = null
//)
//
//@Serializable
//data class PandoraSpellConfig(
//    val Spells: List<SpellType>,
//    val MagicSchools: List<MagicSchool>,
//    val MagicTiers: List<Long>,
//    val RuneTiers: List<Long>,
//    val WarCryTiers: List<Long>,
//    val Count: Int? = null
//)
//
@Serializable
data class StartBuildingConfig(
    val ApplyAllTerrains: Boolean? = null,
    val TerrainType: TerrainType? = null,
    val CastleType: CastleType? = null,
    val Buildings: List<BuildingType> = listOf(),
    val BuildingMode: BuildingMode? = null
)

@Serializable
data class ScriptFeaturesConfig(
    val CastleCaptureProps: CastleCaptureModel? = null,
    val GmRebuildProps: GMRebuildModel? = null,
    val GloballyDisabledBuildingsProps: GloballyDisabledBuildingsModel? = null,
    val ForcedFinalBattleProps: ForcedFinalBattleModel? = null,
    val AdditionalStartCastles: List<AdditionalStartCastle> = listOf()
)

@Serializable
data class CastleCaptureModel(
    val CoordinateX: Long? = null,
    val CoordinateY: Long? = null,
    val SearchRadius: Int? = null,
    val EventTimer: Long? = null,
    val DisableFortifications: Boolean? = null,
    val IsForcedFinalBattle: Boolean? = null
)

@Serializable
data class AdditionalStartCastle(
    val StartCoordinateX: Long? = null,
    val StartCoordinateY: Long? = null,
    val SearchRadius: Int? = null,
    val TargetCoordinateX: Long? = null,
    val TargetCoordinateY: Long? = null,
    val TargetSearchRadius: Int? = null
)

@Serializable
data class ResourcesModel(
    val Wood: Int? = null,
    val Ore: Int? = null,
    val Mercury: Int? = null,
    val Sulfur: Int? = null,
    val Gem: Int? = null,
    val Crystal: Int? = null,
    val Gold: Int? = null
)

@Serializable
data class GMRebuildModel(
    val MinimalGMLevel: Long? = null,
    val MinimalWarCriesLevel: Long? = null,
    val RebuildCost: ResourcesModel? = null
)

@Serializable
data class GloballyDisabledBuildingsModel(
    val Buildings: List<BuildingType> = listOf()
)

@Serializable
data class ForcedFinalBattleModel(
    val Week: Long? = null,
    val Day: Long? = null
)

@Serializable
data class ZoneRandomizationConfig(
    val ZonesToSwap: List<IntArray>,
    val IsSymmetricalSwap: Boolean? = null,
    val ZonesToRandomize: List<Long>
)

@Serializable
data class StartSpellsByPlayer(
    val PlayerType: PlayerType,
    val Spells: List<SpellType>
)
enum class PlayerType(
    val number: Int,
    val description: String
) {
    ANY(0, "любой игрок"),
    FIRST(1, "первый игрок"),
    SECOND(2, "второй игрок");
}

@Serializable
data class StartSpellsByRace(
    val CastleType: CastleType,
    val Spells: List<SpellType>
)

@Serializable
data class StartSpellsByHero(
    val HeroType: HeroType,
    val Spells: List<SpellType>
)

@Serializable
data class StartSpellsConfig(
    val GlobalSpells: List<SpellType>,
    val SpellsByPlayers: List<StartSpellsByPlayer>,
    val SpellsByRaces: List<StartSpellsByRace>,
    val SpellsByHeroes: List<StartSpellsByHero>
)

@Serializable
data class BannedBasesByClass(
    val Class: HeroClassType,
    val Skills: List<SkillType>
)

@Serializable
data class BasesBanModel(
    val CommonBannedSkills: List<SkillType> = listOf(),
    val SkillsBannedForClass: List<BannedBasesByClass> = listOf()
)

//enum class ArtifactCategory(
//    val number: Int,
//    val description: String
//) {
//    MINOR(0, "Минор"),
//    MAJOR(1, "Мажор"),
//    RELIC(2, "Реликт"),
//    GRAIL(3, "");
//}
//
//enum class ArtifactSlot(
//    val number: Int,
//    val description: String
//) {
//    INVENTORY(0, "только в сумке"),
//    PRIMARY(1, "оружие"),
//    SECONDARY(2, "щит"),
//    HEAD(3, "голова"),
//    CHEST(4, "грудь"),
//    NECK(5, "шея"),
//    FINGER(6, "кольцо"),
//    FEET(7, "сапоги"),
//    SHOULDERS(8, "плечи"),
//    MISC_SLOT(9, "карман");
//}
//
@Serializable
enum class ArtifactType(
    val number: Int,
    val description: String
) {
    SwordOfRuins(1, "Меч мощи"),
    DwarfKingAxe(2, "Секира короля гномов"),
    WandOfSpell(3, "Палочка с заклинанием"),
    UnicornBow(4, "Лук из рога единорога"),
    TitansTrident(5, "Трезубец титанов"),
    StaffOfTheUnderworld(6, "Посох Преисподней"),
    Shackles(7, "Кандалы неизбежности"),
    FourLeafClover(8, "Четырехлистный клевер"),
    IceShield(9, "Ледяной щит"),
    Sextant(10, "Секстант морских эльфов"),
    CrownOfLion(11, "Корона льва"),
    CrownOfManyEyes(12, "Корона всевидящего"),
    ArmorOfForgottenHero(13, "Доспехи забытого героя"),
    BreastplateOfPower(14, "Нагрудник огромной мощи"),
    PedantOfMastery(15, "Кулон мастерства"),
    NecklaceOfLion(16, "Ошейник льва"),
    NecklaceOfBloodyClaw(17, "Ожерелье кровавого когтя"),
    EvercoldIcycle(18, "Кулон ледяных объятий"),
    NecklaceOfVictory(19, "Ожерелье победы"),
    RingOfLightningProtection(20, "Кольцо защиты от молний"),
    RingOfVitality(21, "Кольцо жизненной силы"),
    RingOfHaste(22, "Кольцо скорости"),
    NightmarishRing(23, "Кольцо сломленного духа"),
    BootsOfSwiftJourney(24, "Сапоги путешественника"),
    GoldenHorseshoe(25, "Золотая подкова"),
    BootsOfOpenRoad(26, "Сапоги открытого пути"),
    BootsOfMagicDefence(27, "Сапоги магической защиты"),
    EndlessSackOfGold(28, "Сумка бесконечного золота"),
    EndlessBagOfGold(29, "Мешочек бесконечного золота"),
    CapeOfLion(31, "Накидка с гривой льва"),
    PhoenixFeatherCape(32, "Накидка из перьев феникса"),
    CloakOfMourning(33, "Плащ смертоносной тени"),
    TurbanOfEnlightenment(34, "Тюрбан просвещения"),
    ChainMailOfEnlightenment(35, "Кольчуга просвещения"),
    DragonScaleArmor(36, "Доспех из чешуи дракона"),
    DragonScaleShield(37, "Щит из чешуи дракона"),
    DragonBoneGraves(38, "Поножи из кости дракона"),
    DragonWingMantle(39, "Мантия из крыльев дракона"),
    DragonTeethNecklace(40, "Ожерелье из зубов дракона"),
    DragonTalonCrown(41, "Корона из когтей дракона"),
    DragonEyeRing(42, "Кольцо глаз дракона"),
    DragonFlameTongue(43, "Пламенный язык дракона"),
    RobeOfSarIssa(44, "Халат Сар-Иссы"),
    StaffOfSarIssa(45, "Посох Сар-Иссы"),
    CrownOfSarIssa(46, "Корона Сар-Иссы"),
    RingOfSarIssa(47, "Кольцо Сар-Иссы"),
    DwarvenKingCuirass(48, "Кираса короля гномов"),
    DwarvenKingGreaves(49, "Поножи короля гномов"),
    DwarvenKingHelmet(50, "Шлем короля гномов"),
    DwarvenKingShield(51, "Щит короля гномов"),
    ScrollOfSpell(52, "Свиток с заклинанием"),
    NecromancerHelm(55, "Шлем некроманта"),
    ArmorOfValor(56, "Доспехи бесстрашия"),
    WindstriderBoots(57, "Сапоги странника"),
    Moonblade(58, "Лунный клинок"),
    RingOfCelerity(59, "Кольцо стремительности"),
    ElementalWaistband(60, "Кольцо элементалей"),
    EmeraldSlippers(61, "Изумрудные туфли"),
    CloakOfSylanna(62, "Плащ силанны"),
    CursedWaistband(63, "Проклятое кольцо"),
    TunicOfFlesh(64, "Туника из плоти"),
    RingOfCaution(65, "Кольцо предостережения"),
    HelmOfChaos(66, "Шлем хаоса"),
    PendantOfConflux(67, "Кулон поглощения"),
    SandalsOfBlessed(68, "Сандалии святого"),
    SandroCloak(69, "Плащ Сандро"),
    NecromancerRing(70, "Кольцо грешников"),
    NecromancerAmulet(71, "Амулет некроманта"),
    OgreClub(74, "Дубина людоеда"),
    OgreShield(75, "Щит людоеда"),
    TomeOfChaos(76, "Том магии Хаоса"),
    TomeOfLight(77, "Том магии Света"),
    TomeOfDark(78, "Том магии Тьмы"),
    TomeOfSummon(79, "Том магии Призыва"),
    BeginnerMagicStick(80, "Волшебная палочка новичка"),
    RunicAxe(81, "Рунный боевой топор"),
    RunicHarness(82, "Рунная боевая упряжь"),
    SkullOfMarkal(83, "Шлем Маркела"),
    BearhideWraps(84, "Тайные защитные покровы"),
    DwarvenHammer(85, "Гномий кузнечный молот"),
    RuneOfFlame(86, "Руна пламени"),
    TarotDesk(87, "Колода Таро"),
    CrownOfLeadership(88, "Корона лидерства"),
    MaskOfDoppelganger(89, "Маска справедливости"),
    EdgeOfBalance(90, "На грани равновесия"),
    RingOfMachines(91, "Кольцо родства с машинами"),
    HornOfPlenty(92, "Рог изобилия"),
    RingOfUnsummon(93, "Кольцо изгнания"),
    BookOfPower(94, "Том Силы"),
    TreebornQuiver(95, "Изумительный колчан"),
    RingOfSilence(97, "Кольцо безмолвия");
}

@Serializable
enum class BuildingTextureConfig(
    val number: Int,
    val description: String
) {
    BuildingTextureConfigNull(0, ""),
    DefaultDwellingByTerrain(1, "Двелинг в соответствии с террейном"),
    DefaultDwellingByCreature(2, "Двелинг в соотстветствии с юнитом"),
    NeutralCreatureBuilding(3, "Здание нейтралов"),
    TowerByCreature(4, "Башня в соотстветствии с юнитом"),
    TowerByTerrain(5, "Башня в соответствии с террейном"),
    TowerGates(10, "Текстура портала"),
    RunicChest(11, "Рунная коробка"),
    DwarvenCareer(12, "Шахта с редкими ресурсами"),
    WoodShelter(13, "Склад дерева"),
    OreShelter(14, "Склад руды"),
    RandomChest(20, "Сундук с неизвестным наполнением"),
    ArtsChest(21, "Сундук с артефактом"),
    GoldChest(22, "Сундук с золотом"),
    ExpChest(23, "Сундук с экспой"),
    ResourceChest(24, "Сундук с реурсами"),
    SpellsChest(25, "Сундук с заклинаниями"),
    PandoraBox(100, "ящик пандоры"),
    Random(101, "случайная текстура");
}

@Serializable
enum class BuildingMode {
    All,
    StartCastle,
    NeutralCastle
}
//enum class BuildingTexture {
//    Default,
//    HumansDwelling,
//    InfernoDwelling,
//    NecropolisDwelling,
//    ElvesDwelling,
//    LigaDwelling,
//    MagesDwelling,
//    DwarfsDwelling,
//    HordeDwelling,
//    NeutralCreatureTower,
//    HumansTower,
//    InfernoTower,
//    NecropolisTower,
//    ElvesTower,
//    LigaTower,
//    MagesTower,
//    DwarfsTower,
//    HordeTower,
//    PandoraBox,
//    ShopPointTower
//}
//
enum class BuildingType(
    val number: Int,
    val description: String
) {
    T1(0, "Т1 двелл"),
    T1G(1, "Т1 гс"),
    T2(2, "Т2 двелл"),
    T2G(3, "Т2 гс"),
    T3(4, "Т3 двелл"),
    T3G(5, "Т3 гс"),
    T4(6, "Т4 двелл"),
    T4G(7, "Т4 гс"),
    T5(8, "Т5 двелл"),
    T5G(9, "Т5 гс"),
    T6(10, "Т6 двелл"),
    T6G(11, "Т6 гс"),
    T7(12, "Т7 двелл"),
    T7G(13, "Т7 гс"),
    Tavern(14, "Таверна"),
    Forge(15, "Кузница"),
    Market1(16, "Рынок"),
    Market2(17, "Склад"),
    GM1(18, "Гм лвл 1"),
    GM2(19, "Гм лвл 2"),
    GM3(20, "Гм лвл 3"),
    GM4(21, "Гм лвл 4"),
    GM5(22, "Гм лвл 5"),
    Fort1(23, "Форт"),
    Fort2(24, "Цитадель"),
    Fort3(25, "Замок"),
    Gold1(26, "Дом старейшин"),
    Gold2(27, "Ратуша"),
    Gold3(28, "Магистрат"),
    Gold4(29, "Капитолий"),
    Asha(30, "Здание за Слезу Асхи"),
    HavenTrainingGrounds(31, "Трен лвл 1"),
    HavenHeroesMonument(32, "Трен лвл 2"),
    HavenStables(33, "Конюшня"),
    HavenFarms(34, "Фермы на прирост крестьян"),
    InfernoHellGates(35, "+10% к гейтингу"),
    InfernoDemonsGrow(36, "Прирост демонов +2"),
    InfernoHallOfHorror(37, "Прирост коней +1"),
    InfernoSacrificialPit(38, "Жертвенная яма"),
    NecromancyAmplifier(39, "+1000 очков некромантии"),
    NecromancyUnholyTemple(40, "Конвертер нежити"),
    NecromancyGraves(41, "Прирост скелетов +6"),
    NecromancyDragonTombstone(42, "Прирост драконов +1"),
    PreserveAvengerGuild(43, "Гильдия мстителей"),
    PreserveAvengerBrotherhood(44, "Братство мстителей, +10% шанс прока заклятых"),
    PreserveMysticPond(45, "Пруд +ресурсы"),
    PreserveSparklingFountain(46, "Фонтан +удачи"),
    PreserveBloomingGrove(47, "Прирост фей"),
    PreserveTreantSamplings(48, "Прирост энтов"),
    DungeonAltarOfElements(49, "Элементальные цепочки"),
    DungeonAltarOfPrimalChaos(50, "+10% урона от цепочек"),
    DungeonRitualPit(51, "Ритуальная яма"),
    DungeonTradePost(52, "Лавка артефактов"),
    DungeonHallOfIntrigue(53, "+1 знания героям"),
    AcademyLibrary(54, "Библиотека"),
    AcademyArcaneForge(55, "Кузница миниартефактов"),
    AcademyArtifactMerchant(56, "Лавка артефактов"),
    AcademyTreasureCave(57, "+500 золота и прирост джиннов"),
    FortressRunicShrine1(58, "Руны лвл 1"),
    FortressRunicShrine2(59, "Руны лвл 2"),
    FortressRunicShrine3(60, "Руны лвл 3"),
    FortressArena(61, "Прирост берсерков"),
    FortressGuardPost(62, "Сторожевая башня +отряд карликов"),
    FortressStoneworks(63, "Укрепленные стены"),
    FortressRunicAcademy(64, "Прирост жрецов рун"),
    StrongholdHallOfTrial(65, "Кличи лвл 1"),
    StrongholdHallOfMastership(66, "Кличи лвл 2"),
    StrongholdHallOfMight(67, "Кличи лвл 3"),
    StrongholdGarbagePile(68, "Прирост гоблинов"),
    StrongholdTravellerShelter(69, "Талисманы для походных артов"),
    StrongholdPileOfOurFoes(70, "Куча черепов +кровь на старте боя"),
    StrongholdSlaveMarket(71, "Рынок рабов");
}
//
//enum class CastleType(val number: Int) {
//    UNDEFINED(0),
//    HUMANS(1),
//    INFERNO(2),
//    NECROPOLIS(3),
//    ELVES(4),
//    LIGA(5),
//    MAGES(6),
//    DWARFS(7),
//    HORDE(8),
//    RANDOM(9);
//}
//
//enum class DefaultBuilding(
//    val number: Int,
//    val description: String
//) {
//    GoldChest5k(0, "Сундук на 5k золота"),
//    GoldChest10k(1, "Сундук на 10k золота"),
//    GoldChest15k(2, "Сундук на 15k золота"),
//    GoldChest20k(3, "Сундук на 20k золота"),
//    ExpChest5k(4, "Сундук на 5k опыта"),
//    ExpChest10k(5, "Сундук на 10k опыта"),
//    ExpChest15k(6, "Сундук на 15k опыта"),
//    ExpChest20k(7, "Сундук на 20k опыта"),
//    MinorArtChest(8, "Сундук с малым артефактом"),
//    MajorArtChest(9, "Сундук с большим артефактом"),
//    RelicArtChest(10, "Сундук с реликтовым артефактом"),
//    FullDestructiveMagicChest(11, "Сундук со всеми заклинаниями Разрушения (Все кличи для орка)"),
//    FullDarkMagicChest(12, "Сундук со всеми заклинаниями Тьмы (Все кличи для орка)"),
//    FullLightMagicChest(13, "Сундук со всеми заклинаниями Света (Все кличи для орка)"),
//    FullSummoningMagicChest(14, "Сундук со всеми заклинаниями Призыва (Все кличи для орка)"),
//    FullAdventureMagicChest(15, "Сундук со всеми заклинаниями Приключений (Все кличи для орка)"),
//    RandomFullMagicSchoolChest(16, "Сундук со всеми заклинаниями случайной школы (Все кличи для орка)"),
//    FullT1MagicChest(17, "Сундук со всеми заклинаниями 1 уровня (Т1 для орка)"),
//    FullT2MagicChest(18, "Сундук со всеми заклинаниями 2 уровня (Т2 для орка)"),
//    FullT3MagicChest(19, "Сундук со всеми заклинаниями 3 уровня (Т2 для орка)"),
//    FullT4MagicChest(20, "Сундук со всеми заклинаниями 4 уровня (T3 для орка)"),
//    FullT5MagicChest(21, "Сундук со всеми заклинаниями 5 уровня (T3 для орка)"),
//    RandomT1MagicChest(22, "Сундук со случайным заклинанием 1 уровня (T1 для орка)"),
//    RandomT2MagicChest(23, "Сундук со случайным заклинанием 2 уровня (T2 для орка)"),
//    RandomT3MagicChest(24, "Сундук со случайным заклинанием 3 уровня (T2 для орка)"),
//    RandomT4MagicChest(25, "Сундук со случайным заклинанием 4 уровня (T3 для орка)"),
//    RandomT5MagicChest(26, "Сундук со случайным заклинанием 5 уровня (T3 для орка)"),
//    RandomT1CreatureBox(27, "Ящик со случайным грейженным существом 1 ранга x3 прирост"),
//    RandomT2CreatureBox(28, "Ящик со случайным грейженным существом 2 ранга x3 прирост"),
//    RandomT3CreatureBox(29, "Ящик со случайным грейженным существом 3 ранга x3 прирост"),
//    RandomT4CreatureBox(30, "Ящик со случайным грейженным существом 4 ранга x3 прирост"),
//    RandomT5CreatureBox(31, "Ящик со случайным грейженным существом 5 ранга x3 прирост"),
//    RandomT6CreatureBox(32, "Ящик со случайным грейженным существом 6 ранга x3 прирост"),
//    RandomT7CreatureBox(33, "Ящик со случайным грейженным существом 7 ранга x3 прирост"),
//    HighTierRunicChest(34, "Сундук с высокоуровневыми (т3-т5) рунами и 5к экспы герою не гнома"),
//    RandomRunicChest(35, "Сундук со случайной руной и 3к экспы герою не гнома"),
//    T1RunicChest(36, "Сундук с руной 1 ранга и 1к экспы герою не гнома"),
//    T2RunicChest(37, "Сундук с руной 2 ранга и 2к экспы герою не гнома"),
//    T3RunicChest(38, "Сундук с руной 3 ранга и 3к экспы герою не гнома"),
//    T4RunicChest(39, "Сундук с руной 4 ранга и 4к экспы герою не гнома"),
//    T5RunicChest(40, "Сундук с руной 5 ранга и 5к экспы герою не гнома"),
//    WoodGiver(41, "Здание выдающее 5-10 дерева"),
//    OreGiver(42, "Здание выдающее 5-10 руды"),
//    RareResourceGiver(43, "Здание выдающее 5-10 случайного редкого ресурса"),
//    DefaultBuildingTowerPortal(44, "Портал в родной город");
//}
@Serializable
enum class HeroClassType(
    val number: Int,
    val description: String
) {
    KNIGHT(1, "Рыцарь"),
    DEMONLORD(2, "Повелитель демонов"),
    NECROMANCER(3, "Некромант"),
    RANGER(4, "Рейнджер"),
    WARLOCK(5, "Чернокнижник"),
    WIZARD(6, "Маг"),
    RUNEMAGE(7, "Рунный жрец"),
    BARBARIAN(8, "Варвар");
}

@Serializable
enum class HeroType(
    val number: Int,
    val description: String
) {
    NotSelected(0, ""),
    Brand(1, "Бранд"),
    Bersy(2, "Ибба"),
    Egil(3, "Эрлинг"),
    Ottar(4, "Хельмар"),
    Una(5, "Инга"),
    Ingvar(6, "Ингвар"),
    Skeggy(7, "Карли"),
    Vegeyr(8, "Свея"),
    Metlirn(9, "Анвэн"),
    Diraya(10, "Дираэль"),
    Gillion(11, "Гильраэн"),
    Ossir(12, "Оссир"),
    Nadaur(13, "Таланар"),
    Elleshar(14, "Винраэль"),
    Linaas(15, "Вингаэль"),
    Itil(16, "Ильфина"),
    Hero3(17, "Гаруна"),
    Hero4(18, "Гошак"),
    Hero7(19, "Хаггеш"),
    Hero9(20, "Киган"),
    Hero1(21, "Краг"),
    Hero6(22, "Шак-Карукат"),
    Hero8(23, "Тилсек"),
    Hero2(24, "Аргат"),
    Orrin(25, "Дугал"),
    Nathaniel(26, "Эллайна"),
    Ving(27, "Айрис"),
    Sarge(28, "Аксель"),
    Mardigo(29, "Ласло"),
    Maeve(30, "Мив"),
    Brem(31, "Рутгер"),
    Christian(32, "Витторио"),
    Efion(33, "Аластор"),
    Deleb(34, "Дэлеб"),
    Calid(35, "Грол"),
    Grok(36, "Грок"),
    Oddrema(37, "Джезебет"),
    Marder(38, "Марбас"),
    Jazaz(39, "Ниброс"),
    Nymus(40, "Нимус"),
    Eruina(41, "Эрин"),
    Menel(42, "Кифра"),
    Dalom(43, "Летос"),
    Inagost(44, "Синитар"),
    Ferigl(45, "Соргалл"),
    Ohtarig(46, "Вайшан"),
    Almegir(47, "Ирбет"),
    Urunir(48, "Иранна"),
    Faiz(49, "Фаиз"),
    Tan(50, "Джалиб"),
    Havez(51, "Хафиз"),
    Sufi(52, "Ора"),
    Razzak(53, "Нархиз"),
    Nur(54, "Назир"),
    Astral(55, "Нура"),
    Isher(56, "Раззак"),
    Nemor(57, "Дейдра"),
    Gles(58, "Каспар"),
    Tamika(59, "Лукреция"),
    Muscip(60, "Наадир"),
    Straker(61, "Орсон"),
    Effig(62, "Равенна"),
    Pelt(63, "Влад"),
    Aberrar(64, "Золтан"),
    Rolf(65, ""),
    Tolgar(66, ""),
    Vulfsten(67, ""),
    Hangvul(93, ""),
    Alaron(68, ""),
    Faidaen(69, ""),
    Tieru(70, ""),
    Gotai(71, ""),
    Kurak(72, ""),
    Kudjin(73, ""),
    Kuniak(94, ""),
    Alarik(74, ""),
    Dunkan(75, ""),
    Isabel(76, ""),
    Valeria(77, ""),
    Nikolas_Hum(95, ""),
    Godrik(96, ""),
    Frida(98, ""),
    Agrail(78, ""),
    Biara(79, ""),
    Orlando(80, ""),
    Vlastelin(81, ""),
    Illaia(82, ""),
    Railag(83, ""),
    Shadia(84, ""),
    Tralsai(85, ""),
    Maahir(86, ""),
    Sairus(87, ""),
    Zehir(88, ""),
    Temkhan(97, ""),
    Arantir(89, ""),
    Markel(90, ""),
    Nikolas(91, ""),
    Ornella(92, ""),
    Davius55(1000, ""),
    Emilia55(1001, ""),
    Gurvilin55(1002, ""),
    Josephine55(1003, ""),
    Minasli55(1004, ""),
    Rissa55(1005, ""),
    Theodorus55(1006, ""),
    Agbeth55(1007, ""),
    Darkstorm55(1008, ""),
    Kastore55(1009, ""),
    Ranleth55(1010, ""),
    Sephinroth55(1011, ""),
    Sylsai55(1012, ""),
    Bart55(1013, ""),
    Haegeir55(1014, ""),
    Hedwig55(1015, ""),
    Maximus55(1016, ""),
    Tazar55(1017, ""),
    Uland55(1018, ""),
    Benedikt55(1019, ""),
    Bertrand55(1020, ""),
    Gabrielle55(1021, ""),
    Jeddite55(1022, ""),
    Laszlo55(1023, ""),
    Lorenzo55(1024, ""),
    Orlando55(1025, ""),
    Ash55(1026, ""),
    Calh55(1027, ""),
    Calid55(1028, ""),
    Harkenraz55(1029, ""),
    Malustar55(1030, ""),
    Zydar55(1031, ""),
    Aislinn55(1032, ""),
    Archilus55(1033, ""),
    Giovanni55(1034, ""),
    Nimbus55(1035, ""),
    Sandro55(1036, ""),
    Thant55(1037, ""),
    Vidomina55(1038, ""),
    Xerxon55(1039, ""),
    Elleshar55(1040, ""),
    Gem55(1041, ""),
    Ivor55(1042, ""),
    Jenova55(1043, ""),
    Kyrre55(1044, ""),
    Melodia55(1045, ""),
    Mephala55(1046, ""),
    Tieru55(1047, ""),
    Azar55(1048, ""),
    CragHack55(1049, ""),
    Erika55(1050, ""),
    Kraal55(1051, ""),
    Matewa55(1052, ""),
    Mukha55(1053, ""),
    Shiva55(1054, ""),
    Zouleika55(1055, "");
}

//enum class MagicSchool(
//    val number: Int,
//    val description: String
//) {
//    DESTRUCTIVE(0, "Хаос"),
//    DARK(1, "Тьма"),
//    LIGHT(2, "Свет"),
//    SUMMONING(3, "Призыв"),
//    ADVENTURE(4, "Контроль"),
//    RUNIC(5, "Руны гномов"),
//    WARCRIES(6, "Кличи орков"),
//    SPECIAL(7, "Абилки существ/героев и технические спеллы");
//}
//

//enum class ScriptBuilding(
//    val number: Int,
//    val description: String
//) {
//    TowerPortal(
//        number = 0,
//        description = "Портал перемещающий игрока к ближайшему родному городу"
//    ),
//}
//
@Serializable
enum class SkillType(
    val number: Int,
    val description: String
) {
    LOGISTICS(1, "Логистика"),
    WAR_MACHINES(2, "Машины"),
    LEARNING(3, "Образование"),
    LEADERSHIP(4, "Лидерство"),
    LUCK(5, "Удача"),
    OFFENCE(6, "Нападение"),
    DEFENCE(7, "Защита"),
    SORCERY(8, "Чародейство"),
    DESTRUCTIVE_MAGIC(9, "Магия Хаоса"),
    DARK_MAGIC(10, "Магия Тьмы"),
    LIGHT_MAGIC(11, "Магия Света"),
    SUMMONING_MAGIC(12, "Магия Призыва"),
    TRAINING(13, "Контрудар"),
    GATING(14, "Открытие врат"),
    NECROMANCY(15, "Некромантия"),
    AVENGER(16, "Мститель"),
    ARTIFICIER(17, "Мастер артефактов"),
    INVOCATION(18, "Неодолимая магия"),
    RUNELORE(151, "Магия рун"),
    DEMONIC_RAGE(172, "Гнев крови"),
    BARBARIAN_LEARNING(183, "Образование варвара"),
    VOICE(187, "Кличи"),
    SHATTER_DESTRUCTIVE(191, "Приглушение Хаоса"),
    SHATTER_DARK(195, "Приглушение Тьмы"),
    SHATTER_LIGHT(199, "Приглушение Света"),
    SHATTER_SUMMONING(203, "Приглушение Призыва");
}

@Serializable
enum class SpellType(
    val number: Int,
    val description: String
) {
    MagicArrow(1, "Магическая стрела"),
    MagicFist(2, "Магический кулак"),
    LightningBolt(3, "Молния"),
    IceBolt(4, "Льдина"),
    Fireball(5, "Огненный шар"),
    FrostRing(6, "Кольцо холода"),
    ChainLightning(7, "Цепь молний"),
    MeteorShower(8, "Метеоритный дождь"),
    Implosion(9, "Шок земли"),
    Armageddon(10, "Армагеддон"),
    Curse(11, "Ослабление"),
    Slow(12, "Замедление"),
    DisruptingRay(13, "Разрушающий луч"),
    Plague(14, "Чума"),
    Weakness(15, "Немощность"),
    Forgetfullness(17, "Рассеянность"),
    Berserk(18, "Берсерк"),
    Blind(19, "Ослепление"),
    Hypnotize(20, "Подчинение"),
    UnholyWord(21, "Нечестивое слово"),
    Bless(23, "Божественная сила"),
    Haste(24, "Ускорение"),
    Stoneskin(25, "Каменная кожа"),
    Dispel(26, "Снятие чар"),
    Bloodlust(28, "Карающий удар"),
    DeflectArrows(29, "Уклонение"),
    Antimagic(31, "Антимагия"),
    Teleport(32, "Телепорт"),
    CelestialShield(34, "Небесный щит"),
    HolyWord(35, "Святое слово"),
    LandMine(38, "Огненная ловушка"),
    WaspSwarm(39, "Призыв осиного роя"),
    Phantom(40, "Создание фантома"),
    Earthquake(41, "Землетрясение"),
    AnimateDead(42, "Поднятие мертвых"),
    SummonElementals(43, "Призыв элементалей"),
    Resurrect(48, "Воскрешение"),
    SummonBoat(49, "Вызов корабля"),
    DimensionDoor(50, "Астральные врата"),
    TownPortal(51, "Портал в город"),
    SummonCreatures(234, "Вызов подкреплений"),
    ConjurePhoenix(235, "Призыв феникса"),
    Firewall(236, "Огненная стена"),
    StoneSpikes(237, "Каменные шипы"),
    Sorrow(277, "Скорбь"),
    Vampirism(278, "Вампиризм"),
    DeepFreeze(279, "Останавливающий холод"),
    Regeneration(280, "Регенерация"),
    DivineVengeance(281, "Божественная месть"),
    ArcaneCrystal(282, "Магический кристалл"),
    SummonHive(283, "Призыв улья"),
    BladeBarrier(284, "Стена мечей"),
    SummonAirElementals(378, "Призыв элементалей воздуха"),
    SummonEarthElementals(379, "Призыв элементалей земли"),
    SummonFireElementals(380, "Призыв элементалей огня"),
    SummonWaterElementals(381, "Призыв элементалей воды");
}

//
//enum class TemplateType(val number: Int, val description: String) {
//    Uni_S(0, ""),
//    Jebus(1, ""),
//    Moon(2, "")
//}
//
@Serializable
enum class TerrainType {
    FirstPlayer,//(0, "террейн первого игрока"),
    SecondPlayer,//(1, "террейн второго игрока"),
    Terrain1,//(2, "первый случайный свободный террейн"),
    Terrain2,//(3, "второй случайный свободный террейн"),
    Terrain3,//(4, "третий случайный свободный террейн"),
    Terrain4,//(5, "четвёртый случайный свободный террейн"),
    Terrain5,//(6, "пятый случайный свободный террейн"),
    Terrain6,//(7, "шестой случайный свободный террейн")
    Humans,
    Inferno,
    Necropolis,
    Elves,
    Liga,
    Mages,
    Dwarfs,
    Horde
}

@Serializable
enum class MapBuilding(
    val id: String,
    val russianName: String,
    val value: Int? = null
) {
    // Luck/Moral Buildings
    FAERIE_RING(
        "/MapObjects/Faerie_Ring.(AdvMapBuildingShared).xdb#xpointer(/AdvMapBuildingShared)",
        "Волшебное кольцо"
    ),
    FOUNTAIN_OF_FORTUNE(
        "/MapObjects/Fountain_Of_Fortune.(AdvMapBuildingShared).xdb#xpointer(/AdvMapBuildingShared)",
        "Фонтан удачи"
    ),
    IDOL_OF_FORTUNE(
        "/MapObjects/Idol_Of_Fortune.(AdvMapBuildingShared).xdb#xpointer(/AdvMapBuildingShared)",
        "Идол удачи"
    ),
    TEMPLE(
        "/MapObjects/Temple.(AdvMapBuildingShared).xdb#xpointer(/AdvMapBuildingShared)",
        "Храм"
    ),
    RALLY_FLAG(
        "/MapObjects/Rally_Flag.(AdvMapBuildingShared).xdb#xpointer(/AdvMapBuildingShared)",
        "Знамя сбора"
    ),
    RANDOM_SANCTUARY(
        "/MapObjects/H5A2/RandomSancutuary.xdb#xpointer(/AdvMapBuildingShared)",
        "Случайное святилище"
    ),
    SHAMAN_OF_NOMMADS(
        "/MapObjects/H5A2/ShamanOfNommads.xdb#xpointer(/AdvMapBuildingShared)",
        "Шаман кочевников"
    ),
    FOUNTAIN_OF_YOUTH(
        "/MapObjects/Fountain_Of_Youth.(AdvMapBuildingShared).xdb#xpointer(/AdvMapBuildingShared)",
        "Фонтан молодости"
    ),
    LAKE_OF_SCARLETT_SWAN(
        "/MapObjects/Universe_mod/LakeofScarlettSwan.(AdvMapBuildingShared).xdb#xpointer(/AdvMapBuildingShared)",
        "Озеро алых лебедей"
    ),
    LAKE_OF_MERMAIDS(
        "/MapObjects/Universe_mod/LakeofMermaids.(AdvMapBuildingShared).xdb#xpointer(/AdvMapBuildingShared)",
        "Озеро русалок"
    ),
    STABLES(
        "/MapObjects/Stables.(AdvMapBuildingShared).xdb#xpointer(/AdvMapBuildingShared)",
        "Конюшни"
    ),

    // Shop Buildings
    ELEMENTAL_CONFLUX(
        "/MapObjects/Special/ElementalConflux.xdb#xpointer(/AdvMapDwellingShared)",
        "Слияние стихий",
        5
    ),
    WAR_MACHINE_FACTORY(
        "/MapObjects/Objects-All-Terra/War_Machine_Factory.(AdvMapBuildingShared).xdb#xpointer(/AdvMapBuildingShared)",
        "Мастерская машин",
        5
    ),
    HOUSE_OF_ASTROLOGER(
        "/MapObjects/H5A2/House_Of_Astrologer.(AdvMapBuildingShared).xdb#xpointer(/AdvMapBuildingShared)",
        "Башня астролога",
        10
    ),
    REFUGEE_CAMP(
        "/MapObjects/Special/RefugeeCamp.xdb#xpointer(/AdvMapDwellingShared)",
        "Лагерь беженцев",
        15
    ),
    SPELL_SHOP(
        "/MapObjects/H5A2/SpellShop.xdb#xpointer(/AdvMapBuildingShared)",
        "Библиотека заклинаний",
        15
    ),
    BLACK_MARKET(
        "/MapObjects/Black_Market.(AdvMapBuildingShared).xdb#xpointer(/AdvMapBuildingShared)",
        "Рынок артефактов",
        25
    ),

    // Resource Givers
    MYSTICAL_GARDEN(
        "/MapObjects/Mystical_Garden.(AdvMapBuildingShared).xdb#xpointer(/AdvMapBuildingShared)",
        "Мистический сад"
    ),
    WINDMILL(
        "/MapObjects/Windmill.(AdvMapBuildingShared).xdb#xpointer(/AdvMapBuildingShared)",
        "Ветряная мельница"
    ),
    ENCHANTED_TREASURE(
        "/MapObjects/Universe_mod/EnchantedTreasure.(AdvMapBuildingShared).xdb#xpointer(/AdvMapBuildingShared)",
        "Зачарованная сокровищница"
    ),

    // Upgrade Buildings
    MERCENARY_CAMP(
        "/MapObjects/Mercenary_Camp.(AdvMapBuildingShared).xdb#xpointer(/AdvMapBuildingShared)",
        "Лагерь наёмников",
        6
    ),
    MARLETTO_TOWER(
        "/MapObjects/Marletto_Tower.(AdvMapBuildingShared).xdb#xpointer(/AdvMapBuildingShared)",
        "Башня Марлетто",
        6
    ),
    GARDEN_OF_REVELATION(
        "/MapObjects/Garden_Of_Revelation.(AdvMapBuildingShared).xdb#xpointer(/AdvMapBuildingShared)",
        "Сад откровений",
        6
    ),
    STAR_AXIS(
        "/MapObjects/Star_Axis.(AdvMapBuildingShared).xdb#xpointer(/AdvMapBuildingShared)",
        "Звёздная ось",
        6
    ),
    WAR_ACADEMY(
        "/MapObjects/WarAcademy.(AdvMapBuildingShared).xdb#xpointer(/AdvMapBuildingShared)",
        "Военная академия",
        8
    ),
    MAGIC_SPRING(
        "/MapObjects/Magic_Spring.(AdvMapBuildingShared).xdb#xpointer(/AdvMapBuildingShared)",
        "Магический источник",
        10
    ),
    TREE_OF_KNOWLEDGE(
        "/MapObjects/Tree_of_Knowledge.(AdvMapBuildingShared).xdb#xpointer(/AdvMapBuildingShared)",
        "Древо познания",
        14
    ),
    SCHOOL_OF_MAGIC(
        "/MapObjects/SchoolofMagic.(AdvMapBuildingShared).xdb#xpointer(/AdvMapBuildingShared)",
        "Школа магии",
        16
    ),
    ARENA(
        "/MapObjects/Arena.(AdvMapBuildingShared).xdb#xpointer(/AdvMapBuildingShared)",
        "Арена",
        16
    ),
    LIBRARY_OF_ENLIGHTENMENT(
        "/MapObjects/LibraryOfEnlightenment.(AdvMapBuildingShared).xdb#xpointer(/AdvMapBuildingShared)",
        "Библиотека просветления",
        24
    ),

    // Shrines
    SHRINE_OF_MAGIC_1(
        "/MapObjects/Shrine_Of_Magic_1.(AdvMapShrineShared).xdb#xpointer(/AdvMapShrineShared)",
        "Святилище магии 1 уровня",
        4
    ),
    SHRINE_OF_MAGIC_2(
        "/MapObjects/Shrine_Of_Magic_2.(AdvMapShrineShared).xdb#xpointer(/AdvMapShrineShared)",
        "Святилище магии 2 уровня",
        8
    ),
    SHRINE_OF_MAGIC_3(
        "/MapObjects/Shrine_Of_Magic_3.(AdvMapShrineShared).xdb#xpointer(/AdvMapShrineShared)",
        "Святилище магии 3 уровня",
        12
    ),

    // Treasury Buildings
    CRYPT(
        "/MapObjects/Crypt.(AdvMapBuildingShared).xdb#xpointer(/AdvMapBuildingShared)",
        "Склеп",
        5
    ),
    GARGOYLE_STONEVAULT(
        "/MapObjects/GargoyleStonevault.(AdvMapBuildingShared).xdb#xpointer(/AdvMapBuildingShared)",
        "Гаргулятник",
        5
    ),
    DWARVEN_TREASURY(
        "/MapObjects/DwarvenTreasury.(AdvMapBuildingShared).xdb#xpointer(/AdvMapBuildingShared)",
        "Гномий банк",
        10
    ),
    DEMONBANK(
        "/MapObjects/Universe_mod/Demonbank.(AdvMapBuildingShared).xdb#xpointer(/AdvMapBuildingShared)",
        "Демонический банк",
        10
    ),
    ELEMENTAL_STOCKPILE(
        "/MapObjects/Elemantal_Stockpile.(AdvMapBuildingShared).xdb#xpointer(/AdvMapBuildingShared)",
        "Элементальный склад",
        10
    ),
    MONASTERYBANK(
        "/MapObjects/Universe_mod/Monasterybank.(AdvMapBuildingShared).xdb#xpointer(/AdvMapBuildingShared)",
        "Монастырский банк",
        10
    ),

    // Buff Buildings
    MAGI_VAULT(
        "/MapObjects/MagiVault.xdb#xpointer(/AdvMapBuildingShared)",
        "Магический банк",
        15
    ),
    TREANT_THICKET(
        "/MapObjects/TreantThicket.(AdvMapBuildingShared).xdb#xpointer(/AdvMapBuildingShared)",
        "Энтовый банк",
        15
    ),
    WITCH_BANK(
        "/MapObjects/WitchBank.(AdvMapBuildingShared).xdb#xpointer(/AdvMapBuildingShared)",
        "Кровавый храм",
        15
    ),
    NECRO_ESTATE(
        "/MapObjects/Universe_mod/NecroEstate.(AdvMapBuildingShared).xdb#xpointer(/AdvMapBuildingShared)",
        "Некромантический банк",
        15
    ),
    ORC_DEPOSIT(
        "/MapObjects/Universe_mod/OrcDeposit.(AdvMapBuildingShared).xdb#xpointer(/AdvMapBuildingShared)",
        "Орочий банк",
        35
    ),
    DRAGON_UTOPIA(
        "/MapObjects/Dragon_Utopia.(AdvMapBuildingShared).xdb#xpointer(/AdvMapBuildingShared)",
        "Драконья утопия",
        40
    )
}