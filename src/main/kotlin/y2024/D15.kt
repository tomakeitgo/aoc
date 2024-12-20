package com.tomakeitgo.y2024

import com.tomakeitgo.R
import com.tomakeitgo.util.World2D

fun main() {
    val input = R.readFile("/2024/15_input.txt")

    val (top, bottom) = input.split("\n\n")
    val instructions = bottom.split("\n").reduce(String::plus)


    partOne(top, instructions) //1552879
                                 //1559949

    val worldString = top
        .replace("O", "[]")
        .replace("#", "##")
        .replace(".", "..")
        .replace("@", "@.")

    val world = World2D.fromSequence(worldString.split("\n").asSequence())
    //world.print()

    val (fish) = world.findAll('@')
    var currentLocation = fish.first

    instructions.toCharArray().forEach {
        val direction = D15.instructionToDir[it] ?: (0 to 0)
      //  println(it)
        if (direction.first == 0) {
            // left - right
            var toMoveTo = nextFreeSpace(world, currentLocation, direction)
            if (toMoveTo == currentLocation) return@forEach

            val reverseDirection = 0 to -direction.second

            while (toMoveTo != currentLocation) {
                val t = toMoveTo.first + reverseDirection.first to toMoveTo.second + reverseDirection.second
                world.swap(t, toMoveTo)
                toMoveTo = t;
            }
            currentLocation = currentLocation.first to currentLocation.second + direction.second
        } else {
            //up - down
            val reverseDirection = -direction.first to -direction.second
            val toMove = nextFreeSpacePartTow(world, direction, listOf(listOf(currentLocation)))
            if (toMove.isNotEmpty()) {
                currentLocation = currentLocation.first + direction.first to currentLocation.second + direction.second
            }


            val movePoints = toMove.flatten().toSet()
            toMove
                //.drop(1)
                .reversed()
                .forEach { row ->
                    row.forEach {
                        val Swappy = it.first + reverseDirection.first to it.second + reverseDirection.second
                        if (movePoints.contains(Swappy)) {
                            world.swap(
                                it,
                                Swappy
                            )
                        }
                    }
                }

        }
        //world.print()
    }
   // world.print()
    score(world, '[')

}

private fun partOne(
    worldString: String,
    instructions: String

) {
    val world = World2D.fromSequence(worldString.split("\n").asSequence())

    val (fish) = world.findAll('@')
    var currentLocation = fish.first



    instructions.toCharArray().forEach {
        val direction = D15.instructionToDir[it] ?: (0 to 0)
        val toMoveTo = nextFreeSpace(world, currentLocation, direction)

        if (toMoveTo != currentLocation) {
            val next = currentLocation.first + direction.first to currentLocation.second + direction.second
            world.swap(next, toMoveTo)
            world.swap(next, currentLocation)
            currentLocation = next
        }
    }


    score(world, 'O')
}

private fun score(world: World2D, char: Char) {

    world
        .findAll(char)
        .map { it.first }
        .map { it.first.toLong() * 100L + it.second.toLong() }
        .sum()
        .also { println(it) }
}

fun nextFreeSpace(world: World2D, location: Pair<Int, Int>, direction: Pair<Int, Int>): Pair<Int, Int> {
    var nextLocation = (location.first + direction.first to location.second + direction.second)
    while (world.map[nextLocation] != '#') {
        if (!world.exists(nextLocation))
            return location
        if (world.map[nextLocation] == '.')
            return nextLocation

        nextLocation = (nextLocation.first + direction.first to nextLocation.second + direction.second)
    }
    return location
}

fun nextFreeSpacePartTow(
    world: World2D,
    direction: Pair<Int, Int>,
    steps: List<List<Pair<Int, Int>>>
): List<List<Pair<Int, Int>>> {
    val currentRow = steps.last();
    // Find Next row
    val nextRow = currentRow.flatMap {
        val point = it.first + direction.first to it.second + direction.second
        if (world.map[point] == '.' && world.map[it] == '.') {
            emptyList()
        } else if (world.map[point] == ']') {
            listOf(point, point.first to point.second - 1)
        } else if (world.map[point] == '[') {
            listOf(point, point.first to point.second + 1)
        } else {
            listOf(point)
        }
    }.distinct()


    if (nextRow.any { world.map[it] == '#' }) {
        // if any blockers return empty list
        return emptyList()
    } else if (nextRow.any { world.map[it] == '[' || world.map[it] == ']' }) {
        // if any barrels find the next step
        val res = steps.toMutableList()
        res.addLast(nextRow)
        return nextFreeSpacePartTow(world, direction, res)
    } else {
        // else return steps + next row
        val res = steps.toMutableList()
        res.addLast(nextRow)
        return res;
    }
}

class D15 {
    companion object {
        val instructionToDir = mapOf(
            '^' to (-1 to 0),
            'v' to (1 to 0),
            '>' to (0 to 1),
            '<' to (0 to -1),
        )
    }
}