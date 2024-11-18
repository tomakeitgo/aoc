package com.tomakeitgo.y2023

import com.tomakeitgo.R

fun main() {
    partOne() //2006
    partTwo() //84911
}

private fun partOne() {
    val validThings = mapOf(
        Pair("red", 12),
        Pair("green", 13),
        Pair("blue", 14)
    )

    R
        .fileLinesAsSequence("/2023/02_input.txt")
        .map(Game::fromString)
        .filter {
            it.isValid(validThings)
        }
        .map(Game::id)
        .reduce(Int::plus)
        .also(::println)
}

private fun partTwo() {
    R
        .fileLinesAsSequence("/2023/02_input.txt")
        .map(Game::fromString)
        .map(Game::power)
        .reduce(Int::plus)
        .also(::println)
}

private class Game(
    var id: Int,
    val draws: List<Map<String, Int>> = mutableListOf()
) {
    fun isValid(toCheck: Map<String, Int>): Boolean {
        return draws.all {
            for (key in toCheck.keys) {
                if (it.getOrDefault(key, 0) > (toCheck[key] ?: 0)) {
                    return false
                }
            }
            true
        }
    }

    fun min(): Map<String, Int> {
        val maxes = mutableMapOf<String, Int>()
        draws.forEach {
            for (key in it.keys) {
                val currentMax = maxes.getOrDefault(key, 0)
                val toCheckIfAgainst = it.getOrDefault(key, 0)

                if (currentMax < toCheckIfAgainst) {
                    maxes[key] = toCheckIfAgainst
                }
            }
        }
        return maxes;
    }

    fun power(): Int {
        return min().values.reduce(Int::times)
    }

    override fun toString(): String {
        return "Game(id=$id, draws=$draws)"
    }

    companion object {
        fun fromString(line: String): Game {
            val idAndDraws = line.split("[ ]*:[ ]*".toRegex())
            val id = idAndDraws[0].replace("[^0-9]".toRegex(), "").toInt()
            val draws = idAndDraws[1]
                .split("[ ]*;[ ]*".toRegex())
                .map {
                    val draw = mutableMapOf<String, Int>()
                    it
                        .split("[ ]*,[ ]*".toRegex())
                        .map {
                            it.split(" ")
                        }
                        .forEach {
                            draw[it[1]] = it[0].toInt()
                        }

                    draw
                }
            return Game(id, draws)
        }
    }
}