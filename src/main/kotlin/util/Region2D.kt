package com.tomakeitgo.util

import java.util.*

class Region2D(
    val world: World2D,
    val points: Set<Pair<Int, Int>>
) {
    fun isAdjacent(other: Region2D): Boolean {
        for (p1 in other.points) {
            for (p2 in points) {
                if (world.isAdjacent(p1, p2)) {
                    return true
                }
            }
        }
        return false
    }

    fun merge(other: Region2D): Region2D {
        val points = mutableSetOf<Pair<Int, Int>>()
        points.addAll(other.points)
        points.addAll(this.points)
        return Region2D(world, points)
    }

    fun borderSize(char: Char): Int {
        return points.sumOf {
            world
                .findAdjacentAllowOffWorld(it)
                .count { border -> world.map[border] != char }
        }
    }

    fun sides(char: Char): Int {
        World2D.printPoints(points)


        val list = points
            .flatMap {
                world
                    .findAdjacentAllowOffWorld(it)
                    .filter { border -> world.map[border] != char }
            }.toMutableSet()

        World2D.printPoints(list)

        return 0
    }

    override fun toString(): String {
        return "Region2D(world=$world, points=$points)"
    }

    companion object {
        fun merge(regions: List<Region2D>): List<Region2D> {
            val toProcess = LinkedList(regions)
            while (hasAdj(toProcess)) {
                val item = toProcess.removeFirst()
                val matching = toProcess
                    .filter { it.isAdjacent(item) }
                    .fold(item) { acc, r -> acc.merge(r) }

                toProcess.removeAll { it.isAdjacent(item) }
                toProcess.addLast(matching)
            }

            return toProcess
        }

        private fun hasAdj(regions: List<Region2D>): Boolean {
            val filter = regions.flatMap { item ->
                regions.map { item to it }
            }
                .filter { it.first != it.second }

            return filter

                .any { it.first.isAdjacent(it.second) }
        }
    }
}