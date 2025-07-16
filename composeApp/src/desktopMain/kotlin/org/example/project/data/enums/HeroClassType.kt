package org.example.project.data.enums

enum class HeroClassType(
    val number: Int,
    val description: String
) {
    NONE(0, ""),
    KNIGHT(1, "Рыцарь"),
    DEMONLORD(2, "Повелитель демонов"),
    NECROMANCER(3, "Некромант"),
    RANGER(4, "Рейнджер"),
    WARLOCK(5, "Чернокнижник"),
    WIZARD(6, "Маг"),
    RUNEMAGE(7, "Рунный жрец"),
    BARBARIAN(8, "Варвар");

    companion object {
        fun fromNumber(number: Int): HeroClassType? {
            return values().find { it.number == number }
        }
    }
}