package org.example.project.data

import kotlinx.serialization.Serializable

@Serializable
data class CreatureTierReplacement(
    val CreatureIds: List<String>,
    val Tier: Int?
)