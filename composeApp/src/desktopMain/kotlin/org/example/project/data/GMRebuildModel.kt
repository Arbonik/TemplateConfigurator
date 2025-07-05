package org.example.project.data

import kotlinx.serialization.Serializable

@Serializable
data class GMRebuildModel(
    val MinimalGMLevel : Long?,
    val MinimalWarcriesLevel : Long?,
    val RebuildCost : GMRebuildCost
)
