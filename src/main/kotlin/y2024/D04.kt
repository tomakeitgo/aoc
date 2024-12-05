package com.tomakeitgo.y2024

import com.tomakeitgo.R

val directions = listOf(
    Pair(-1, -1),
    Pair(-1, 0),
    Pair(-1, 1),
    Pair(0, -1),
    Pair(0, 1),
    Pair(1, -1),
    Pair(1, 0),
    Pair(1, 1)
)

fun main() {
//    val map = buildMap("/2024/04_sample.txt")
    val map = buildMap("/2024/04_input.txt")
    partOne(map) // 2507
    partTwo(map) // 1969

}


private fun partTwo(map: Map<Pair<Int, Int>, Char>) {
    map.entries
        .filter {
            it.value == 'A'
        }
        .map {
            isMas(map, it.key)
        }
        .count { it }
        .also(::println)
}

private fun isMas(map: Map<Pair<Int, Int>, Char>, point: Pair<Int, Int>): Boolean {
    return masAllRotations().any { toCheck ->
        toCheck.all {
            map[it.first.first + point.first to it.first.second + point.second] == it.second
        }
    }
}

private fun masAllRotations(): List<List<Pair<Pair<Int, Int>, Char>>> {
    val first = listOf(
        Pair(-1, -1) to 'M',
        Pair(-1, 1) to 'M',
        Pair(0, 0) to 'A',
        Pair(1, -1) to 'S',
        Pair(1, 1) to 'S',
    )
    val second = rotate(first);
    val third = rotate(second);
    val forth = rotate(third);


    val shapesToSearch = listOf(first, second, third, forth)
    return shapesToSearch
}

fun rotate(toRotate: List<Pair<Pair<Int, Int>, Char>>): List<Pair<Pair<Int, Int>, Char>> {
    return toRotate.map {
        Pair(Pair(it.first.second, -it.first.first), it.second)
    }
}

private fun partOne(
    map: Map<Pair<Int, Int>, Char>
) {
    val searchTerm = listOf('X', 'M', 'A', 'S')
    map.entries
        .filter { it.value == 'X' }
        .map { entry ->
            directions
                .map { matches(map, searchTerm, entry.key, it) }
                .count { it }
        }
        .reduce(Int::plus)
        .also(::println)
}

fun matches(
    map: Map<Pair<Int, Int>, Char>,
    searchTerm: List<Char>,
    point: Pair<Int, Int>,
    direction: Pair<Int, Int>
): Boolean {
    if (searchTerm.isEmpty()) return true
    if (searchTerm[0] != map[point]) return false

    return matches(
        map,
        searchTerm.subList(1, searchTerm.size),
        Pair(direction.first + point.first, direction.second + point.second),
        direction
    )
}

private fun buildMap(inputFile: String): Map<Pair<Int, Int>, Char> {
    return buildMap(R.fileLinesAsSequence(inputFile))
}

private fun buildMap(lines: Sequence<String>): Map<Pair<Int, Int>, Char> {
    return lines
        .flatMapIndexed { rowIndex, row ->
            row.mapIndexed { colIndex, char ->
                Pair(rowIndex, colIndex) to char
            }
        }
        .associate { it }
}