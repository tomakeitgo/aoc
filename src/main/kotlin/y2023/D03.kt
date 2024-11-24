package com.tomakeitgo.y2023

import com.tomakeitgo.R

val pointsToCheck = listOf(
    Pair(-1, -1),
    Pair(-1, 0),
    Pair(-1, 1),

    Pair(0, -1),
    Pair(0, 0),
    Pair(0, 1),

    Pair(1, -1),
    Pair(1, 0),
    Pair(1, 1),
)

fun main() {
    val m = buildMap("/2023/03_input.txt")

    partOne(m) //  537732
    partTwo(m) //84883664
}



private fun partOne(m: Map<Pair<Int, Int>, Token>) {
    m
        .entries
        .filter { it.value.isSymbol() || it.value.isGear() }
        .map {
            pointsToCheck.map { point ->
                m[Pair(it.key.first + point.first, it.key.second + point.second)] ?: Token(
                    "",
                    0,
                    Token.EMPTY
                )
            }
                .filter { it.isNumber() }
                .distinct()
                .map { it.value }
                .reduce(Int::plus)
        }
        .reduce(Int::plus)
        .also(::println)
}

private fun partTwo(m: Map<Pair<Int, Int>, Token>) {
    m
        .entries
        .filter { it.value.isGear() }
        .map {
            pointsToCheck.map { point ->
                m[Pair(it.key.first + point.first, it.key.second + point.second)] ?: Token(
                    "",
                    0,
                    Token.EMPTY
                )
            }
                .filter { it.isNumber() }
                .distinct()
        }
        .filter { it.size > 1 }
        .map { it.map { it.value }.reduce(Int::times) }
        .reduce(Int::plus)
        .also(::println)
}

private fun buildMap(path: String): Map<Pair<Int, Int>, Token> {
    val m = mutableMapOf<Pair<Int, Int>, Token>()

    R
        .fileLinesAsSequence(path)
        .forEachIndexed { index, line ->
            var currentNumber = Token("", 0, Token.NUMBER)
            for ((lineIndex, char) in line.withIndex()) {
                if (char.isDigit()) {
                    currentNumber.token += char
                    currentNumber.value = currentNumber.value * 10 + char.digitToInt()
                    m[Pair(index, lineIndex)] = currentNumber
                } else if (char == '.') {
                    m[Pair(index, lineIndex)] = Token(char.toString(), 0, Token.EMPTY);
                    //clear the number
                    currentNumber = Token("", 0, Token.NUMBER);
                } else if (char == '*') {
                    m[Pair(index, lineIndex)] = Token(char.toString(), 0, Token.GEAR);
                    //clear the number
                    currentNumber = Token("", 0, Token.NUMBER);
                } else {
                    m[Pair(index, lineIndex)] = Token(char.toString(), 0, Token.SYMBOL);
                    //clear the number
                    currentNumber = Token("", 0, Token.NUMBER);
                }

            }
        }
    return m
}

class Token(
    var token: String,
    var value: Int,
    var type: Int
) {
    companion object {
        val EMPTY: Int = 1
        val SYMBOL: Int = 2
        val NUMBER: Int = 3
        val GEAR: Int = 4
    }

    fun isNumber(): Boolean {
        return type == NUMBER
    }

    fun isSymbol(): Boolean {
        return type == SYMBOL
    }

    fun isEmpty(): Boolean {
        return type == EMPTY
    }

    fun isGear(): Boolean {
        return type == GEAR
    }

    override fun toString(): String {
        return "Token(token='$token', value=$value, type=$type)"
    }
}