package com.tomakeitgo.y2024

import com.tomakeitgo.R

fun main() {
    val input = R.fileLinesAsSequence("/2024/05_input.txt")

    val rules = buildRules(input)
    val orders = buildOrderings(input)
    val rankedOrders = orders.map { line ->
        val inOrder = line.mapIndexed { index, pageNumber ->
            (rules[pageNumber] ?: emptyList()).all {
                val indexOf = line.indexOf(it)
                indexOf == -1 || index <= indexOf
            }
        }
            .all { it }
        inOrder to line
    }

    rankedOrders
        .filter { it.first }
        .map { it.second[it.second.size / 2] }
        .reduce(Int::plus)
        .also(::println) //5248

    rankedOrders
        .filter { it.first.not() }
        .map { it.second }
        .map {
            it.sortedWith { a, b ->
                if (rules.getOrDefault(a, emptyList()).contains(b)) -1 else 1
            }
        }
        .map { it[it.size / 2] }
        .reduce(Int::plus)
        .also(::println) //4507

}

private fun buildOrderings(input: Sequence<String>): Sequence<List<Int>> {
    return input
        .filter { it.contains(",") }
        .map {
            it
                .split(",")
                .map(String::toInt)
        }
}

private fun buildRules(input: Sequence<String>): Map<Int, List<Int>> {
    return input
        .filter { it.contains("|") }
        .map {
            it
                .split("|")
                .map(String::toInt)
        }
        .groupBy(
            { it[0] },
            { it[1] }
        )
}