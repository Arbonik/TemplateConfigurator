package org.example.project.data.enums

enum class BuildingTextureConfig(
    val number: Int,
    val description: String
) {
    BuildingTextureConfigNull(0, ""),
    DefaultDwellingByTerrain(1, "Двелинг в соответствии с террейном"),
    DefaultDwellingByCreature(2, "Двелинг в соотстветствии с юнитом"),
    NeutralCreatureBuilding(3, "Здание нейтралов"),
    TowerByCreature(4, "Башня в соотстветствии с юнитом"),
    TowerByTerrain(5, "Башня в соответствии с террейном"),
    TowerGates(10, "Текстура портала"),
    RunicChest(11, "Рунная коробка"),
    DwarvenCareer(12, "Шахта с редкими ресурсами"),
    WoodShelter(13, "Склад дерева"),
    OreShelter(14, "Склад руды"),
    RandomChest(20, "Сундук с неизвестным наполнением"),
    ArtsChest(21, "Сундук с артефактом"),
    GoldChest(22, "Сундук с золотом"),
    ExpChest(23, "Сундук с экспой"),
    ResourceChest(24, "Сундук с реурсами"),
    SpellsChest(25, "Сундук с заклинаниями"),
    PandoraBox(100, "ящик пандоры"),
    Random(101, "случайная текстура");

    companion object {
        fun fromNumber(number: Int): BuildingTextureConfig? = values().find { it.number == number }
    }
}