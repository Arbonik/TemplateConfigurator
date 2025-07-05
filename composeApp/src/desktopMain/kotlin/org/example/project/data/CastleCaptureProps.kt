package org.example.project.data

import kotlinx.serialization.Serializable

@Serializable
data class CastleCaptureProps(
    val CoordinateX: Long?,
    val CoordinateY: Long?,
    val SearchRadius: Int?,
    val CaptureTimer: Long?,
    val DisableFortifications: Boolean,
)