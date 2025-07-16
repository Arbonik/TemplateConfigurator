package org.example.project.data.enums

enum class PlayerType(
    val number: Int,
    val description: String
) {
    ANY(0, "любой игрок игроки"),
    FIRST(1, "первый игрока"),
    SECOND(2, "второй игрока");

    companion object {
        fun fromNumber(number: Int): PlayerType? {
            return values().find { it.number == number }
        }
    }
}