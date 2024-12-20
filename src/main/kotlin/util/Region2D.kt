package com.tomakeitgo.util

import com.tomakeitgo.y2024.main
import java.util.*
import kotlin.Comparator

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
        val border = points.flatMap {
            world.findAdjacent(it, World2D.DIRECTIONS_WITH_DIAGONALS).filter { world.map[it] != char }
        }.toSet()

        val c1 = rotate(listOf(0 to 0, 1 to 0, 0 to 1))
        val c2 = rotate(c1)
        val c3 = rotate(c2)
        val c4 = rotate(c3)

        val toTest = listOf(c1, c2, c3, c4)

        val corners = border.flatMap { potC ->

            toTest.map { rot ->
                potC to rot
                    .map { potC.first + it.first to potC.second + it.second }
                    .all { border.contains(it) }
            }
                .filter { it.second }
                .filter {
                    world
                        .findAdjacent(it.first, World2D.DIRECTIONS_ONLY_DIAGONALS)
                        .any { points.contains(it) }
                }
                .map { it.first }
        }



        println("$char - ${corners.size}")
//        World2D.printPoints(points)
        println(corners)
        World2D.printPoints(border)
        World2D.printPoints(corners)
        println("$char - ${corners.size}")

        return corners.size
    }

    private fun rotate(point: Pair<Int, Int>): Pair<Int, Int> {
        return Pair(point.second, -point.first)
    }

    private fun rotate(points: List<Pair<Int, Int>>): List<Pair<Int, Int>> {
        return points.map { rotate(it) }
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
/*
R  10 .
I  4 .
I 16 15
C 4 .
C 22 20
F 12 11
V 10 9
J 12 .
E 8 .
M 6 .
S 6 .
 */

