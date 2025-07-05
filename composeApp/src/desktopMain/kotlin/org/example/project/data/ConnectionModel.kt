package org.example.project.data

import kotlinx.serialization.Serializable

@Serializable
data class ConnectionModel(
    val sourceZoneIndex: Int,
    val destZoneIndex: Int,
    val isMain: Boolean,
    val twoWay: Boolean? = null,
    val guardStrenght: Int? = null,
    val guarded: Boolean? = null,
    val wide: Boolean? = null,
    val staticPos: Boolean? = null,
    val startPointX: Int? = null,
    val startPointY: Int? = null,
    val minRadiusToSearch: Int? = null,
    val maxRadiusToSearch: Int? = null,
    val minRadiusToMain: Int? = null,
    val maxRadiusToMain: Int? = null,
    val roadType: Int? = null
)