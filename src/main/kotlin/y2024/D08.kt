package com.tomakeitgo.y2024

import com.tomakeitgo.R
import com.tomakeitgo.util.World2D

fun main() {
    val world = World2D.fromSequence(R.fileLinesAsSequence("/2024/08_input.txt"))

    partOne(world) // 359
    partTwo(world) // 1293

}

private fun partOne(world: World2D) {
    world
        .distinctValues { it != '.' }
        .asSequence()
        .map {
            it to world.findAll(it).map { it.first }
        }
        .map {
            it.first to it.second.flatMap { first ->
                it.second
                    .filter { second -> second != first }
                    .flatMap { second ->
                        listOf(
                            addPoints(first, scaleComponentDist(componentDist(first, second), 2)),
                            addPoints(second, scaleComponentDist(componentDist(second, first), 2))
                        )
                    }
            }
        }
        .flatMap { it.second }
        .filter(world::exists)
        .distinct()
        .count()
        .also(::println)
}

private fun partTwo(world: World2D) {
    world
        .distinctValues { it != '.' }
        .asSequence()
        .map {
            it to world.findAll(it).map { it.first }
        }
        .map {
            it.first to it.second.flatMap { first ->
                it.second
                    .filter { second -> second != first }
                    .flatMap { second ->
                        (1..50).flatMap {
                            listOf(
                                addPoints(first, scaleComponentDist(componentDist(first, second), it)),
                                addPoints(second, scaleComponentDist(componentDist(second, first), it))
                            )
                        }
                    }
            }
        }
        .flatMap { it.second }
        .filter(world::exists)
        .distinct()
        .count()
        .also(::println)
}

// 0 1 2 3 4 5 6 7 8
// X . . . X . . . X
fun componentDist(a: Pair<Int, Int>, b: Pair<Int, Int>): Pair<Int, Int> {
    return b.first - a.first to b.second - a.second
}

fun scaleComponentDist(a: Pair<Int, Int>, scalar: Int): Pair<Int, Int> {
    return a.first * scalar to a.second * scalar
}

fun addPoints(a: Pair<Int, Int>, b: Pair<Int, Int>): Pair<Int, Int> {
    return a.first + b.first to a.second + b.second
}