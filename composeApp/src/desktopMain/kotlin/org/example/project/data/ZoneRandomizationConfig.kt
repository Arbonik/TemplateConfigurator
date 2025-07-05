package org.example.project.data

import kotlinx.serialization.Serializable

@Serializable
data class ZoneRandomizationConfig(
    val ZonesToSwap: List<Int>,
    val IsSymmetricalSwap : Boolean,
    val ZonesToRandomize : Long
)
