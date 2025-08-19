package project.ui

import ConnectionModel
import CreatureBanksPool
import CustomBuildingConfig
import EntitiesBanModel
import GeneralData
import ScriptFeaturesConfig
import StartBuildingConfig
import StartSpellsConfig
import TerrainConfig
import ZoneGenerationConfig
import ZoneRandomizationConfig
import kotlinx.serialization.Serializable

@Serializable
data class TemplateGenerationConfig(
    val TemplateName: String,
    val Zones: List<ZoneGenerationConfig>,
    val Connections: List<ConnectionModel>,
    val TerrainConfigs: List<TerrainConfig> = listOf(),
    val StartBuildingConfigs: List<StartBuildingConfig> = listOf(),
    val GeneralData: GeneralData? = null,
    val BaseArmyMultiplier: Double? = null,
    val multipliers: Map<String, Double> = mapOf(),
    val ScriptFeaturesConfig: ScriptFeaturesConfig,
    val EntitiesBanConfig: EntitiesBanModel = EntitiesBanModel(),
    val StartSpellsConfig: StartSpellsConfig? = null,
    val CustomBuildingConfigs: List<CustomBuildingConfig>,
    val CreatureBanksPool: CreatureBanksPool? = null,
    val ZoneRandomizationConfig: ZoneRandomizationConfig? = null
)