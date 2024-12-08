package com.tomakeitgo.y2023

import com.tomakeitgo.R
import java.util.LinkedList
import kotlin.math.pow

fun main() {
    val cards = R
        .fileLinesAsSequence("/2023/04_input.txt")
        .map(Card::fromLine)

    partOne(cards)
    partTwo(cards)
}

private fun partOne(cards: Sequence<Card>) {
    cards
        .map {
            it.partOneScore() to it
        }
        .map { 2.toDouble().pow(it.first.toDouble() - 1) }
        .map { it.toInt() }
        .reduce(Int::plus)
        .also(::println)
}

private fun partTwo(cards: Sequence<Card>) {
    val cardsById = cards.associateBy { it.id }
    val toProcess = cards.toCollection(LinkedList())
    var processed = 0
    while (toProcess.isNotEmpty()) {
        val card = toProcess.removeFirst()
        (1..card.partOneScore())
            .map { it + card.id }
            .map { cardsById[it] }
            .filterNotNull()
            .forEach { toProcess.add(it) }
        processed++
    }
    println(processed)
}

private class Card(val id: Int, val winningNumbers: Set<Int>, val cardNumber: Set<Int>) {

    fun partOneScore(): Int {
        return cardNumber
            .map {
                winningNumbers.contains(it)
            }
            .count { it }
    }

    override fun toString(): String {
        return "Card(id=$id, winningNumbers=$winningNumbers, cardNumber=$cardNumber)"
    }

    companion object {
        fun fromLine(line: String): Card {
            val l = line.replace("Card ", "")
            val colon = l.indexOf(":")
            val pipe = l.indexOf("|")


            return Card(
                l.substring(0, l.indexOf(":")).trim().toInt(),
                l.substring(colon + 1, pipe).trim().split("[ ]+".toRegex()).map { it.trim().toInt() }.toSet(),
                l.substring(pipe + 1).trim().split("[ ]+".toRegex()).map { it.trim().toInt() }.toSet()
            )
        }
    }
}