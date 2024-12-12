package com.tomakeitgo.y2023

import com.tomakeitgo.R

fun main() {
    val lines = R
        .fileLinesAsSequence("/2023/05_sample.txt")
        .filter { it.isNotEmpty() }

    var current = "" to "";
    var seeds = emptyList<Int>();
    val mappings = mutableMapOf<Pair<String, String>, MutableList<List<Int>>>()
    for (line in lines) {
        if (line.startsWith("seeds")) {
            seeds = line
                .replace("seeds: ", "")
                .split(" ")
                .map { it.toInt() }
                .toList()
        } else if (line.contains("-to-")) {
            val parts = line
                .replace("-to-", " ")
                .replace(" map:", "")
                .split(" ")
            current = Pair(parts[0], parts[1])
        } else {
            mappings
                .computeIfAbsent(current) { mutableListOf() }
                .add(line.split(" ").map { it.toInt() }.toList())
        }
    }

    println(seeds)
    mappings.size.also { println(it) }
    println(mappings)
    mappings.map {
        it.key to it.value.map { a ->

            Pair(
                (a[0]..(a[0] + a[2])).toList(),
                (a[1]..(a[1] + a[2])).toList()
            )
        }
    }

}