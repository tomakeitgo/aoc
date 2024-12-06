package com.tomakeitgo.y2024

import com.tomakeitgo.R

fun main() {
    val map = buildMap("/2024/06_input.txt")

    simulateWalkPartOne(map) // 4964
    map
        .filter { it.value == '.' }
        .map {
            val attempt = map.toMutableMap()
            attempt[it.key] = '#'
            detectLoop(attempt)
        }
        .count { it }
        .also(::println)
}

private fun detectLoop(map: Map<Pair<Int, Int>, Char>): Boolean {
    var current = map.filter { it.value == '^' }.map { it.key }.first()

    var direction = Pair(-1, 0);
    val visited = mutableSetOf(current to direction)

    var next = add(current, direction)

    while (map.containsKey(next)) {
        current = next;
        var toCheck = add(current, direction)
        while (map[toCheck] == '#') {
            direction = rotate(direction)
            toCheck = add(current, direction)
        }
        next = toCheck;
        if(visited.add(current to direction).not()) return true
    }

    return false
}
private fun simulateWalkPartOne(map: Map<Pair<Int, Int>, Char>) {
    var current = map.filter { it.value == '^' }.map { it.key }.first()
    val visited = mutableSetOf(current)

    var direction = Pair(-1, 0);

    var next = add(current, direction)

    while (map.containsKey(next)) {
        current = next;
        var toCheck = add(current, direction)
        while (map[toCheck] == '#') {
            direction = rotate(direction);
            toCheck = add(current, direction)
        }
        next = toCheck;
        visited.add(current);
    }

    println(visited.size)
}

private fun rotate(dir: Pair<Int, Int>): Pair<Int, Int> {
    return dir.second to -dir.first
}

private fun add(point: Pair<Int, Int>, direction: Pair<Int, Int>): Pair<Int, Int> {
    return point.first + direction.first to point.second + direction.second
}

private fun buildMap(input: String) = R
    .fileLinesAsSequence(input)
    .flatMapIndexed { rowIndex, line ->
        line.mapIndexed { columnIndex, char ->
            (rowIndex to columnIndex) to char
        }
    }
    .associate { it }