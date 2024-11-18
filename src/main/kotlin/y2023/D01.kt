package com.tomakeitgo.y2023

import com.tomakeitgo.R

fun main() {
    partOne("/2023/01_sample.txt") //142
    partOne("/2023/01_input.txt") //56049
    partTwo("/2023/01_input.txt") //54530
}

private fun partOne(path: String) {
    println(
        R
            .fileLinesAsSequence(path)
            .map(::removeNonDigits)
            .map(::findLineValue)
            .reduce(Int::plus)
    )
}

private fun partTwo(path: String) {
    println(
        R
            .fileLinesAsSequence(path)
            .map(::replaceWordsWithDigits)
            .map(::removeNonDigits)
            .map(::findLineValue)
            .reduce(Int::plus)
    )
}

private fun replaceWordsWithDigits(line: String): String {
    return line
        .replace("one", "o1e")
        .replace("two", "t2o")
        .replace("three", "t3e")
        .replace("four", "f4r")
        .replace("five", "f5e")
        .replace("six", "s6x")
        .replace("seven", "s7n")
        .replace("eight", "e8t")
        .replace("nine", "n9e")
        .replace("zero", "z0o")
}

private fun findLineValue(line: String): Int {
    return (line[0] + "" + line[line.length - 1]).toInt()
}

private fun removeNonDigits(text: String): String {
    return text.replace("[^0-9]".toRegex(), "")
}