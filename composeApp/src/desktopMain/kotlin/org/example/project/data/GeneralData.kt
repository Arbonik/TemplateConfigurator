package org.example.project.data

import kotlinx.serialization.Serializable

@Serializable
data class GeneralData(
    val DisableDwellingIcon: Boolean,
    val Mine1LevelGuardLevel : Int? = null,
    val Mine2LevelGuardLevel : Int? = null,
    val MineGoldGuardLevel : Int? = null,
)