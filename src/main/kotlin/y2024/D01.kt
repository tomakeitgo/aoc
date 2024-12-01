package com.tomakeitgo.y2024

import com.tomakeitgo.R
import kotlin.math.absoluteValue

fun main() {
    val pairs = R
        .fileLinesAsSequence("/2024/01_input.txt")
        .map { it.split("\\s+".toRegex()) }
        .map { it[0].toInt() to it[1].toInt() }



    partOne(pairs) // 2264607
    partTwo(pairs) // 19457120
}

private fun partOne(pairs: Sequence<Pair<Int, Int>>) {
    val left = mutableListOf<Int>()
    val right = mutableListOf<Int>()
    pairs.forEach {
        left.add(it.first)
        right.add(it.second)
    }
    left.sort()
    right.sort()

    var value = 0
    left.forEachIndexed { index, i ->
        value += (i - right[index]).absoluteValue
    }
    println(value)
}

private fun partTwo(pairs: Sequence<Pair<Int, Int>>) {
    val freq = mutableMapOf<Int, Int>()
    pairs.forEach {
        freq[it.second] = (freq[it.second] ?: 0) + 1
    }
    pairs.map {
        it.first * freq.getOrDefault(it.first, 0)
    }
        .reduce(Int::plus)
        .also(::println)
}
