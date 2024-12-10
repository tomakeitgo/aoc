package com.tomakeitgo.y2024

import com.tomakeitgo.R

fun main() {
    val memory = R
        .readFile("/2024/09_input.txt")
        .trim()
        .toCharArray()
        .map { it.digitToInt() }
        .flatMapIndexed { index, digit ->
            val fileId = (if (index.mod(2) == 0) index / 2 else -1)
            (0..<digit).map { fileId }
        }
        .toMutableList()

    checkSum(partOne(memory.toMutableList())) //6446899523367
    checkSum(partTwo(memory).toMutableList()) //6478232739671
}

private fun partTwo(memory: MutableList<Int>): MutableList<Int> {
    (memory.max() downTo 0).forEach { fileId ->
        val startOfBlock = memory.indexOf(fileId)
        val endOfBlock = memory.lastIndexOf(fileId)
        val size = endOfBlock - startOfBlock + 1
        val startOfFree = findFreeBlockOfSize(memory, size)
        if (startOfFree in 1..<startOfBlock) {
            (startOfBlock..endOfBlock)
                .forEachIndexed { index, blockIndex ->
                    swapBlock(memory, startOfFree + index, blockIndex)
                }

        }
    }
    return memory
}

private fun partOne(memory: MutableList<Int>): MutableList<Int> {
    for (i in 0 until memory.size) {
        if (memory[i] < 0) {
            for (j in (0 until memory.size).reversed()) {
                if (memory[j] >= 0 && i < j) {
                    swapBlock(memory, i, j)
                    break
                }
            }
        }
    }
    return memory
}

private fun checkSum(memory: MutableList<Int>) {
    memory
        .mapIndexed { index, value ->
            if (value < 0L) 0 else index * value
        }
        .map(Int::toLong)
        .reduce(Long::plus)
        .also(::println)
}

fun swapBlock(list: MutableList<Int>, indexOne: Int, indexTwo: Int) {
    val temp = list[indexOne]
    list[indexOne] = list[indexTwo]
    list[indexTwo] = temp
}

fun findFreeBlockOfSize(list: List<Int>, size: Int): Int {
    if (size <= 0) return -1

    for (i in list.indices) {
        if (
            (0 until size).all {
                i + it < list.size && list[i + it] == -1
            })
            return i
    }
    return -1
}