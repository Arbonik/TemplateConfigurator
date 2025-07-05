package org.example.project.data

import kotlinx.serialization.Serializable

@Serializable
data class GMRebuildCost(
    val Wood : Long? = 0,
    val Ore : Long? = 0,
    val Mercury : Long? = 0,
    val Sulfur : Long? = 0,
    val Gem : Long? = 0,
    val Crystal : Long? = 0,
    val Gold : Long? = 0
)
