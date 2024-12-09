package com.tomakeitgo.util

class World2D(val map: Map<Pair<Int, Int>, Char>) {

    fun distinctValues(filter: (Char) -> Boolean = { true }): List<Char> {
        return map.values.filter(filter).distinct()
    }

    fun findAll(char: Char): List<Map.Entry<Pair<Int, Int>, Char>> {
        return map.entries.filter { it.value == char }
    }

    fun exists(point: Pair<Int, Int>): Boolean {
        return map.containsKey(point)
    }

    override fun toString(): String {
        return "World2D(map=$map)"
    }

    companion object {
        fun fromSequence(sequence: Sequence<String>): World2D {
            val world = sequence.flatMapIndexed { rowIndex, row ->
                row.mapIndexed { columnIndex, char ->
                    (rowIndex to columnIndex) to char
                }
            }
                .associate { it }

            return World2D(world)
        }
    }

}