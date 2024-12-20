package com.tomakeitgo.util

class World2D(val map: MutableMap<Pair<Int, Int>, Char>) {

    fun distinctValues(filter: (Char) -> Boolean = { true }): List<Char> {
        return map.values.filter(filter).distinct()
    }

    fun findAll(char: Char): List<Pair<Pair<Int, Int>, Char>> {
        return map.entries
            .filter { it.value == char }
            .map { (it.key.first to it.key.second) to it.value }
    }

    fun exists(point: Pair<Int, Int>): Boolean {
        return map.containsKey(point)
    }

    fun matchingPointsInDirection(
        point: Pair<Int, Int>,
        directions: List<Pair<Int, Int>>,
        char: Char
    ): List<Pair<Pair<Int, Int>, Char>> {
        return directions
            .map { it.first + point.first to it.second + point.second }
            .map { it to map.getOrDefault(it, ' ') }
            .filter { it.second == char }
            .toList()
    }

    fun findAdjacent(point: Pair<Int, Int>): List<Pair<Int, Int>> {
        return DIRECTIONS_NO_DIAGONALS
            .map { point.first + it.first to point.second + it.second }
            .filter { exists(it) }
            .toList()
    }

    fun findAdjacentAllowOffWorld(point: Pair<Int, Int>): List<Pair<Int, Int>> {
        return DIRECTIONS_NO_DIAGONALS
            .map { point.first + it.first to point.second + it.second }
            .toList()
    }

    fun findAdjacentDiagAllowOffWorld(point: Pair<Int, Int>): List<Pair<Int, Int>> {
        return DIRECTIONS_WITH_DIAGONALS
            .map { point.first + it.first to point.second + it.second }
            .toList()
    }

    fun findAdjacent(point: Pair<Int, Int>, directions: List<Pair<Int, Int>>): List<Pair<Int, Int>> {
        return directions
            .map { point.first + it.first to point.second + it.second }
            .toList()
    }

    fun isAdjacent(
        point1: Pair<Int, Int>,
        point2: Pair<Int, Int>
    ): Boolean {
        return DIRECTIONS_NO_DIAGONALS
            .map {
                it.first + point1.first == point2.first && it.second + point1.second == point2.second
            }
            .any { it }
    }

    fun swap(point1: Pair<Int, Int>, point2: Pair<Int, Int>) {
        val temp = map[point1]
        map[point1] = map[point2]!!
        map[point2] = temp!!
    }

    fun replace(point: Pair<Int, Int>, char: Char){
        map[point] = char
    }

    override fun toString(): String {
        return "World2D(map=$map)"
    }

    fun print() {
        val list = map.keys;
        if (list.isEmpty()) return

        val xMin = list.map { it.first }.min()
        val xMax = list.map { it.first }.max()

        val yMin = list.map { it.second }.min()
        val yMax = list.map { it.second }.max()

        println("---------------")
        (xMin..xMax).forEach { x ->
            (yMin..yMax).forEach { y ->

                if (list.contains(Pair(x, y)))
                    print(map[Pair(x, y)])
                else
                    print(" ")

            }
            println()
        }

    }

    companion object {

        val DIRECTIONS_NO_DIAGONALS = listOf(
            -1 to 0,
            0 to -1,
            0 to 1,
            1 to 0
        )

        val DIRECTIONS_WITH_DIAGONALS = listOf(
            -1 to -1,
            -1 to 0,
            -1 to 1,
            0 to -1,
            0 to 1,
            1 to -1,
            1 to 0,
            1 to 1
        )
        val DIRECTIONS_ONLY_DIAGONALS = listOf(
            -1 to -1,
            -1 to 1,
            1 to -1,
            1 to 1,
        )

        fun fromSequence(sequence: Sequence<String>): World2D {
            val world = sequence.flatMapIndexed { rowIndex, row ->
                row.mapIndexed { columnIndex, char ->
                    (rowIndex to columnIndex) to char
                }
            }
                .associate { it }

            return World2D(world.toMutableMap())
        }

        fun printPoints(list: Collection<Pair<Int, Int>>) {
            if (list.isEmpty()) return

            val xMin = list.map { it.first }.min()
            val xMax = list.map { it.first }.max()

            val yMin = list.map { it.second }.min()
            val yMax = list.map { it.second }.max()

            println("---------------")
            (yMin..yMax).forEach { y ->
                (xMin..xMax).forEach { x ->
                    if (list.contains(Pair(y, x)))
                        print("X")
                    else
                        print(".")

                }
                println()
            }
        }
    }

}