package com.tomakeitgo.y2024

import com.tomakeitgo.R
import kotlin.math.cos

fun main() {
    val input = R
        .fileLinesAsSequence("/2024/13_sample.txt")
        .filter { it.isNotBlank() }
        .mapIndexed { index, item ->
            index / 3 to item
        }
        .groupBy { key -> key.first }
        .entries
        .map { it.value.map { it.second } }
        .map { D13.from(it).solveB() }
        .sum()
        .also(::println)

}

class D13(
    val a: Button,
    val b: Button,
    val c: Pair<Long, Long>
) {
    fun solve(offset: Long): Long {
        return (0..100L).flatMap { aPresses ->
            (0..100L).map { bPresses ->
                ((a.cost * aPresses) + (b.cost * bPresses)) to (((a.x * aPresses) + (b.x * bPresses)) to ((a.y * aPresses) + (b.y * bPresses)))
            }
        }
            .filter { it.second == c.first + offset to c.second + offset }
            .map { it.first }
            .sum()
    }

    fun solveB(): Long {
        // (x1 yf - y1 xF) / (-x2 y1 + y2 ) = c2
        // (xF - x2 c2) / x1 = c1

        val bTokens = ((a.x * c.second) - (a.y * c.first)) / ((-b.x * a.y) + b.y)
        val aTokens = (c.first - (b.x * bTokens)) / a.x

        println(bTokens)
        println(aTokens)

        return 0
    }

    class Button(val x: Long, val y: Long, val cost: Int) {
        override fun toString(): String {
            return "Button(x=$x, y=$y, cost=$cost)"
        }
    }

    override fun toString(): String {
        return "D13(a=$a, b=$b, c=$c)"
    }

    companion object {
        fun from(l: List<String>): D13 {
            val location = "Prize: X=(\\d+), Y=(\\d+)"
                .toRegex()
                .findAll(l[2])
                .map { it.groupValues[1].toLong() to it.groupValues[2].toLong() }
                .first()

            return D13(
                button(l[0], 3),
                button(l[1], 1),
                location
            )
        }

        private fun button(input: String, cost: Int): Button {
            return "Button [AB]: X([+-]\\d+), Y([+-]\\d+)"
                .toRegex()
                .findAll(input)
                .map { Button(it.groupValues[1].toLong(), it.groupValues[2].toLong(), cost) }
                .first()
        }
    }
}