package com.tomakeitgo.y2024

import com.tomakeitgo.R
import com.tomakeitgo.util.World2D

fun main() {
    val world = World2D.fromSequence(R.fileLinesAsSequence("/2024/16_input.txt"))
//    println(D16(world).solve()) //91464
//    D16(world).solvePartTwo() // 494
}

class D16(val world: World2D) {
    val start: Pair<Int, Int>
    val end: Pair<Int, Int>
    val allSolutions = mutableSetOf<Pair<Int, Int>>()

    init {
        start = world.findAll('S').first().first
        end = world.findAll('E').first().first
    }

    fun solve(): Long {
        return shortShortestPaths().second!!.toLong()
    }

    fun solvePartTwo() {
        val best = shortShortestPaths()
        allSolutions.addAll(best.first)

        val blanked = mutableSetOf<Pair<Int, Int>>()
        val optionsToBlank = best.first.toMutableSet()


        while (optionsToBlank.isNotEmpty()) {
            val x = optionsToBlank.first()
            blanked.add(x)
            optionsToBlank.remove(x)

            try {
                world.map[x] = '#'
                val p = shortShortestPaths()
                if (p.first.isNotEmpty() && p.second == best.second) {
                    p.first.filter { !blanked.contains(it) }.map { optionsToBlank.add(it) }
                    allSolutions.addAll(p.first)
                }
            } finally {
                world.map[x] = '.'
            }

        }





        World2D.printPoints(allSolutions)
        println(allSolutions.size)
    }


    fun shortShortestPaths(): Pair<List<Pair<Int, Int>>, Int?> {
        val toVisit = mutableSetOf<Pair<Int, Int>>()
        toVisit.add(start)
        toVisit.add(end)
        world
            .findAll('.')
            .forEach { toVisit.add(it.first) }


        val previous = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>>()
        val direction = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>>()
        val distances = toVisit
            .map { it to Int.MAX_VALUE }
            .associate { it }
            .toMutableMap()

        distances[start] = 0
        direction[start] = 0 to -1

        while (toVisit.isNotEmpty()) {
            val u = toVisit
                .map { it to distances.getValue(it) }
                .minBy { it.second }

            toVisit.remove(u.first)

            world
                .findAdjacent(u.first, World2D.DIRECTIONS_NO_DIAGONALS)
                .filter { world.map[it] != '#' }
                .filter { toVisit.contains(it) }
                .forEach {
                    val stepDir = u.first.first - it.first to u.first.second - it.second
                    if (direction[u.first] == null) return@forEach
                    val prevDir = direction[u.first]!!

                    val stepScore = if (stepDir != prevDir)
                        1001
                    else 1

                    val potentialDist = distances.getValue(u.first) + stepScore
                    if (potentialDist < distances[it]!!) {
                        distances[it] = potentialDist
                        previous[it] = u.first
                        direction[it] = stepDir
                    }
                }
        }

        return makePath(previous, end) to distances[end]
    }

    fun makePath(
        points: Map<Pair<Int, Int>, Pair<Int, Int>>,
        point: Pair<Int, Int>
    ): List<Pair<Int, Int>> {
        if (points[point] == start) {
            return listOf(start, point)
        } else {
            if (points[point] == null) return emptyList()
            return makePath(points, points[point]!!) + listOf(point)
        }
    }

}