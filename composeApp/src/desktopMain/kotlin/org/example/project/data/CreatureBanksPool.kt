package org.example.project.data

import kotlinx.serialization.Serializable

@Serializable
data class CreatureBanksPool(
    val BanksAmount: IntValueConfig,
    val NonPlayerFactions: Boolean,
    val PlayerFactions: Boolean
)