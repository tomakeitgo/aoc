package com.tomakeitgo.y2024

import com.tomakeitgo.R


fun main() {
    val bounds = 101 to 103

    partOne(bounds)

    for (index in 0..10000) {


        val robots = R
            .fileLinesAsSequence("/2024/14_input.txt")
            .map { D14.Robot.fromString(it) }
            .map { it.step(index, bounds) }

        val positions = robots.map { it.position }.toSet();

        val readOut = points(0 to 0, bounds)
            .map { if (positions.contains(it)) "X" else "_" }
            .fold("") { acc, it -> acc + it }


//        println("---- $index ----")
        val picture = "."
            .repeat(bounds.first)
            .toRegex().findAll(readOut)
            .map { it.value }

        if (picture.any() { it.contains("XXXXXXX") }) {
            println("---- $index ----")
            picture.joinToString("\n").also { println(it) }
        }


    }


}

private fun partOne(bounds: Pair<Int, Int>) {
    val robots = R
        .fileLinesAsSequence("/2024/14_input.txt")
        .map { D14.Robot.fromString(it) }
        .map { it.step(100, bounds) }
    partOne(bounds, robots)
}


private fun partOne(
    bounds: Pair<Int, Int>,
    robots: Sequence<D14.Robot>
) {
    val firstSize = bounds.first / 2
    val secondSize = bounds.second / 2

    val q1 = points(0 to 0, firstSize - 1 to secondSize - 1)
        .map { p ->
            robots.count { it.position == p }
        }
        .sum()

    val q2 = points(firstSize + 1 to 0, bounds.first - 1 to secondSize - 1)
        .map { p ->
            robots.count { it.position == p }
        }
        .sum()

    val q3 = points(0 to secondSize + 1, firstSize - 1 to bounds.second - 1)
        .map { p ->
            robots.count { it.position == p }
        }
        .sum()

    val q4 = points(firstSize + 1 to secondSize + 1, bounds.first - 1 to bounds.second - 1)
        .map { p ->
            robots.count { it.position == p }
        }
        .sum()

    println(q1.toLong() * q2.toLong() * q3.toLong() * q4.toLong())
}

private fun points(topLeft: Pair<Int, Int>, bottomRight: Pair<Int, Int>): List<Pair<Int, Int>> {
    return (topLeft.first..bottomRight.first).flatMap { first ->
        (topLeft.second..bottomRight.second).map { second ->
            first to second
        }
    }
}

class D14 {
    class Robot(val position: Pair<Int, Int>, val vector: Pair<Int, Int>) {

        fun step(jump: Int, bounds: Pair<Int, Int>): Robot {
            val toAdd = vector.first.toLong() * jump.toLong() to vector.second.toLong() * jump.toLong()
            val dest =
                ((position.first + toAdd.first) % bounds.first) to (position.second + toAdd.second) % bounds.second

            val normalizedFirst = if (dest.first < 0) bounds.first + dest.first else dest.first
            val normalizedSecond = if (dest.second < 0) bounds.second + dest.second else dest.second
            return Robot(normalizedFirst.toInt() to normalizedSecond.toInt(), vector)
        }

        override fun toString(): String {
            return "Robot(position=$position, vector=$vector)"
        }

        companion object {
            fun fromString(line: String): Robot {
                val regex = "p=(\\d+),(\\d+) v=(-?\\d+),(-?\\d+)".toRegex()
                val parts = regex.find(line)!!.groupValues
                return Robot(
                    parts[1].toInt() to parts[2].toInt(),
                    parts[3].toInt() to parts[4].toInt()
                )
            }
        }

    }
}