package org.example.project.data.enums

enum class ArtifactCategory(
    val number: Int,
    val description: String
) {
    MINOR(0, "Минор"),
    MAJOR(1, "Мажор"),
    RELIC(2, "Реликт"),
    GRAIL(3, "");

    companion object {
        fun fromNumber(number: Int): ArtifactCategory? {
            return values().find { it.number == number }
        }
    }
}