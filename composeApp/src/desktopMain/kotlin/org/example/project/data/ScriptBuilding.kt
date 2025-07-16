package org.example.project.data

enum class ScriptBuilding(
    val number: Int,
    val description: String
) {
    TowerPortal(
        number = 0,
        description = "Портал перемещающий игрока к ближайшему родному городу"
    ),
    // Здесь можно добавить другие элементы enum, если они есть
}