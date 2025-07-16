package org.example.project.data.enums

enum class CastleType(val number: Int) {
    UNDEFINED(0),
    HUMANS(1),
    INFERNO(2),
    NECROPOLIS(3),
    ELVES(4),
    LIGA(5),
    MAGES(6),
    DWARFS(7),
    HORDE(8),
    RANDOM(9);

    companion object {
        fun fromNumber(number: Int): CastleType? = values().find { it.number == number }
    }
}