package com.tomakeitgo.y2024

import com.tomakeitgo.R
import com.tomakeitgo.util.Region2D
import com.tomakeitgo.util.World2D

fun main() {
    val world = World2D.fromSequence(R.fileLinesAsSequence("/2024/12_sample.txt"))

//    val world = World2D.fromSequence(listOf("XX", "XX").asSequence())
    val types = world.distinctValues()
    val data = types
        .map { it to world.findAll(it).map { entry -> entry.first } }
        .map { it.first to it.second.map { p -> Region2D(world, setOf(p)) } }
        .map { it.first to Region2D.merge(it.second) }
        .map { pairs ->
            pairs.second.map { it.points.size * it.borderSize(pairs.first) }.sum() to
                     pairs.second.map { it.points.size * it.sides(pairs.first) }
        }


    data.map { it.first }.sum().also { println(it) }
    data.map { it.second.sum() }.sum().also { println(it) }


}


