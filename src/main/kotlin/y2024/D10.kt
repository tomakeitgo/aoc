package com.tomakeitgo.y2024

import com.tomakeitgo.R
import com.tomakeitgo.util.World2D
import java.util.*

fun main() {
    val world = World2D
        .fromSequence(R.fileLinesAsSequence("/2024/10_input.txt"))

    val seq = LinkedList(world.findAll('0')).map { it ->
        var count = 0
        val points = LinkedList(listOf(it))
        val output = mutableSetOf<Pair<Int, Int>>()

        while (points.isNotEmpty()) {
            val entry = points.removeFirst()
            world.matchingPointsInDirection(
                entry.first,
                D10.directions,
                D10.nextLevel[entry.second] ?: '0'
            ).forEach { point ->
                if (point.second == '9') {
                    output.add(point.first)
                    count++
                } else {
                    points.add(point)
                }
            }
        }

        output.size to count
    }

    //part one 501
    seq.map { it.first }
        .reduce(Int::plus)
        .also(::println)

    //part two 1017
    seq.map { it.second }
        .reduce(Int::plus)
        .also(::println)

}

class D10 {
    companion object {
        val nextLevel = mapOf(
            '0' to '1',
            '1' to '2',
            '2' to '3',
            '3' to '4',
            '4' to '5',
            '5' to '6',
            '6' to '7',
            '7' to '8',
            '8' to '9',
        );

        val directions = listOf(
            0 to -1,
            -1 to 0,
            1 to 0,
            0 to 1
        )
    }
}

