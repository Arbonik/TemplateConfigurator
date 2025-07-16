package org.example.project.data.enums

enum class ArtifactSlot(
    val number: Int,
    val description: String
) {
    INVENTORY(0, "только в сумке"),
    PRIMARY(1, "оружие"),
    SECONDARY(2, "щит"),
    HEAD(3, "голова"),
    CHEST(4, "грудь"),
    NECK(5, "шея"),
    FINGER(6, "кольцо"),
    FEET(7, "сапоги"),
    SHOULDERS(8, "плечи"),
    MISC_SLOT(9, "карман");

    companion object {
        fun fromNumber(number: Int): ArtifactSlot? {
            return values().firstOrNull { it.number == number }
        }
    }
}