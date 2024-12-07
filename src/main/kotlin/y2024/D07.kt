package com.tomakeitgo.y2024

import com.tomakeitgo.R

fun main() {
    val file = "/2024/07_input.txt"
    val equations = buildEquations(file)

    eval(
        equations,
        listOf(
            { left: Long, right: Long -> left + right },
            { left: Long, right: Long -> left * right },
        )
    ) // 3119088655389
    eval(
        equations, listOf(
            { left: Long, right: Long -> left + right },
            { left: Long, right: Long -> left * right },
            { left: Long, right: Long -> (left.toString() + right.toString()).toLong() },
        )
    ) // 264184041398847
}

private fun eval(
    equations: Sequence<Pair<Long, List<Long>>>,
    operators: List<(Long, Long) -> Long>
) {
    equations.map { pairs ->
        maths(pairs.first, 0, operators, pairs.second) to pairs
    }
        .filter { it.first }
        .map { it.second.first }
        .reduce(Long::plus)
        .also(::println)
}

private fun buildEquations(file: String): Sequence<Pair<Long, List<Long>>> {
    val equations = R
        .fileLinesAsSequence(file)
        .map { line ->
            val list = line
                .replace(":", "")
                .split(" ")
                .map { it.toLong() }

            list[0] to list.drop(1)
        }
    return equations
}


fun maths(
    target: Long,
    currentValue: Long,
    operators: List<(Long, Long) -> Long>,
    remaining: List<Long>
): Boolean {
    if (remaining.isEmpty()) {
        return target == currentValue
    }
    for (operator in operators) {
        if (maths(target, operator.invoke(currentValue, remaining[0]), operators, remaining.drop(1))) {
            return true
        }
    }
    return false
}