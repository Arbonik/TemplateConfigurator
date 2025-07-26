package org.example.project.data.enums

enum class CastleType(val number: Int) {
    Undefined(0),
    Humans(1),
    Inferno(2),
    Necropolis(3),
    Elves(4),
    Liga(5),
    Mages(6),
    Dwarfs(7),
    Horde(8),
    Random(9);
    companion object {
        val available = listOf(Humans, Inferno, Necropolis, Elves, Liga, Mages, Dwarfs, Horde)
    }
}