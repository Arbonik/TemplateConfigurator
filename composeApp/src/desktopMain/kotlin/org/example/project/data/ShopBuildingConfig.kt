package org.example.project.data

import kotlinx.serialization.Serializable

@Serializable
data class ShopBuildingConfig(
    val Id: Int,
    val Value: Long?,
    val GuardStrenght: Long?,
    val CreatureBuildingConfig: CreatureBuildingConfig? = null,
    val CreatureBankConfig: CreatureBankConfig,
    val XdbRef : String? = null
)