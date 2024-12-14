package com.tomakeitgo.y2024

import com.tomakeitgo.R
import com.tomakeitgo.util.Region2D
import com.tomakeitgo.util.World2D
import java.util.LinkedList

fun main() {
    val world = World2D.fromSequence(R.fileLinesAsSequence("/2024/12_sample.txt"))

    world.isAdjacent(0 to 0, 1 to 1).also(::println)
    val types = world.distinctValues()
    val data = types
        .map { it to world.findAll(it).map { entry -> entry.first } }
        .map { it.first to it.second.map { p -> Region2D(world, setOf(p)) } }
        .map { it.first to Region2D.merge(it.second) }
        .map { pairs ->
            pairs.second.map { it.points.size * it.borderSize(pairs.first) }.sum() to
                    pairs.first to pairs.second.map {it.sides(pairs.first) }
        }

    data.forEach(::println)


}


