package org.example.project.data

import kotlinx.serialization.Serializable

@Serializable
data class GuardsPool(
    val Values: List<String>
)