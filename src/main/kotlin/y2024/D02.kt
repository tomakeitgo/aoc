package com.tomakeitgo.y2024

import com.tomakeitgo.R
import kotlin.math.absoluteValue

fun main() {
    partOne() // 524
    partTwo() // 569
}

private fun partOne() {
    R
        .fileLinesAsSequence("/2024/02_input.txt")
        .map { line -> line.split("\\s+".toRegex()).map { it.toInt() } }
        .map(::safe)
        .filter { it }
        .count()
        .also(::println)
}

private fun partTwo() {
    R
        .fileLinesAsSequence("/2024/02_input.txt")
        .map { line -> line.split("\\s+".toRegex()).map { it.toInt() } }
        .map(::safeish)
        .filter { it }
        .count()
        .also(::println)
}

fun safe(list: List<Int>): Boolean {
    return isMonotonic(list) && changeBetween(list, 1, 4)
}

fun safeish(list: List<Int>): Boolean {
    if (safe(list)) {
        return true
    }

    for (i in list.indices) {
        if (safe(list.filterIndexed { index, _ -> index != i })) {
            return true
        }
    }

    return false
}

fun changeBetween(list: List<Int>, lower: Int, upper: Int): Boolean {
    var last = list[0]
    for (i in 1..<list.size) {
        val value = list[i]
        if ((last - value).absoluteValue in lower..<upper) {
            last = value
            continue
        }
        return false
    }
    return true
}

fun isMonotonic(list: List<Int>): Boolean {
    var increasingLast = list[0]
    var increasing = true
    var decreasingLast = list[0]
    var decreasing = true
    for (i in list) {
        if (i <= increasingLast) {
            increasingLast = i
        } else {
            increasing = false
        }
        if (i >= decreasingLast) {
            decreasingLast = i
        } else {
            decreasing = false
        }
    }
    return increasing || decreasing
}