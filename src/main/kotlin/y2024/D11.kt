package com.tomakeitgo.y2024

import com.tomakeitgo.R

fun main() {
    val input = R
        .readFile("/2024/11_input.txt")
        .trim()
        .split("\\s+".toRegex())
        .map(String::toLong)


    input
        .sumOf {
            partOneRockRules(it, 25, 1)
        }
        .also(::println)

    input
        .sumOf {
            partOneRockRules(it, 75, 1)
        }
        .also(::println)

}


/*
If the stone is engraved with the number 0,
it is replaced by a stone engraved with the number 1.

If the stone is engraved with a number that has an even number of digits,
it is replaced by two stones. The left half of the digits are engraved on the new left stone,
and the right half of the digits are engraved on the new right stone. (The new numbers don't keep extra leading zeroes: 1000 would become stones 10 and 0.)

If none of the other rules apply, the stone is replaced by a new stone;
the old stone's number multiplied by 2024 is engraved on the new stone.
 */
fun partOneRockRules(value: Long, reduction: Int, count: Long): Long {
    val key = "${value}_${reduction}_${count}"
    if (RockCache.cache.contains(key)) {
        return RockCache.cache.getOrDefault(key, -1L)
    }
    if (reduction == 0) {
        RockCache.cache[key] = count
        return count
    }
    if (value == 0L) {
        val i = partOneRockRules(1L, reduction - 1, 1)
        RockCache.cache[key] = i
        return i
    }
    val digits = value.toString()
    if (digits.length % 2 == 1) {
        val i = partOneRockRules(value * 2024, reduction - 1, 1)
        RockCache.cache[key] = i
        return i
    }
    val i = partOneRockRules(digits.substring(0, digits.length / 2).toLong(), reduction - 1, 1) +
            partOneRockRules(digits.substring(digits.length / 2).toLong(), reduction - 1, 1)
    RockCache.cache[key] = i
    return i

}

class RockCache {
    companion object {
        val cache: MutableMap<String, Long> = mutableMapOf()
    }
}
