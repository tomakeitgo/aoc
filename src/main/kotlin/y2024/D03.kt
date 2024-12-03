package com.tomakeitgo.y2024

import com.tomakeitgo.R

fun main() {
    val input = R.readFile("/2024/03_input.txt")

    println(sumMultis(input)) //p1: 166905464
    println(sumMultis(stripDonts(input))) //p2: 72948684
}

private fun stripDonts(text: String): String {
    var remaining = text
    var start = remaining.indexOf("don't()")
    if (start == -1) {
        return text
    }

    var buffer = ""
    while (start != -1) {
        buffer += remaining.substring(0, start)

        val end = remaining.indexOf("do()", start)
        if (end == -1) {
            return buffer
        }

        remaining = remaining.substring(end + 4)
        start = remaining.indexOf("don't")
    }

    return buffer + remaining
}

private fun sumMultis(input: String): Int {
    return "mul\\((\\d+),(\\d+)\\)".toRegex()
        .findAll(input)
        .map {
            it
                .groupValues.subList(1, it.groupValues.size)
                .map(String::toInt)
                .reduce(Int::times)
        }
        .reduce(Int::plus)
}