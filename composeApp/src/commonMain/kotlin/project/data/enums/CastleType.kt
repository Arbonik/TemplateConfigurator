package project.data.enums

import kotlinx.serialization.Serializable

@Serializable
enum class CastleType(val number: Int) {
    Humans(1),
    Inferno(2),
    Necropolis(3),
    Elves(4),
    Liga(5),
    Mages(6),
    Dwarfs(7),
    Horde(8),
    Random(9);
}