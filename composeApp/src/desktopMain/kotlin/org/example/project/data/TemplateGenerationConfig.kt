package org.example.project.data

import kotlinx.serialization.Serializable


@Serializable
// Корневой класс
data class TemplateGenerationConfig(
    val TemplateName: String,
    val Zones: List<Zone>,
    val BaseArmyMultiplier: Double = 1.0,
    val ArmyMultipliers: ArmyMultipliers,
    val Connections: List<ConnectionModel>,
    val ShopBuildingConfigs: List<ShopBuildingConfig>,
    val TerrainConfig: List<TerrainConfig>,
    val ScriptFeaturesConfig: ScriptFeaturesConfig,
    val EntitiesBanConfig: EntitiesBanConfig,
    val CreatureBanksPool: CreatureBanksPool,
    val GeneralData: GeneralData,
    val StartBuildingConfigs: List<StartBuildingConfig>,
    val ZoneRandomizationConfig : ZoneRandomizationConfig? = null
)
