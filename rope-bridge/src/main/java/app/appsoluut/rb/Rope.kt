package app.appsoluut.rb

import java.io.File
import kotlin.math.abs

data class Vector2(
    var x: Int,
    var y: Int
)

class Rope constructor(
    private val verbose: Boolean,
    private val source: String
) {
    private val vectors = mapOf(
        "left" to Vector2(-1, 0),
        "right" to Vector2(1, 0),
        "up" to Vector2(0, -1),
        "down" to Vector2(0, 1)
    )

    private val rope = mutableListOf<Vector2>()

    fun countVisited(length: Int) {
        val file = File(source)

        for (index in 0 until length) {
            rope.add(Vector2(0, 0))
        }

        val moves = mutableSetOf<Vector2>()

        file.forEachLine { line ->
            if (verbose) {
                println("- $line")
            }

            val (pos, steps) = line.split(" ", limit = 2)
            moves.add(rope.last().copy())

            for (step in 0 until steps.toInt()) {
                move(pos)?.let { dest ->
                    rope[0].x += dest.x
                    rope[0].y += dest.y

                    for (index in 1 until length) {
                        update(index)
                    }
                    moves.add(rope.last().copy())
                }
            }
        }

        println("Visited: ${moves.size}")
    }

    private fun update(index: Int) {
        val head = rope[index - 1]
        val tail = rope[index]
        val dest = Vector2(
            x = tail.x - head.x,
            y = tail.y - head.y
        )
        if (dest.x == 0 || dest.y == 0) {
            if (abs(dest.x) >= 2) {
                rope[index].x -= sign(dest.x)
            }
            if (abs(dest.y) >= 2) {
                rope[index].y -= sign(dest.y)
            }
        }
        else if (abs(dest.x) != 1 || abs(dest.y) != 1) {
            rope[index].x -= sign(dest.x)
            rope[index].y -= sign(dest.y)
        }
    }

    private fun sign(input: Int): Int {
        return if (input > 0) {
            1
        } else if (input < 0) {
            -1
        } else {
            0
        }
    }

    private fun move(to: String): Vector2? {
        return when(to) {
            "R" -> vectors["right"]
            "U" -> vectors["up"]
            "D" -> vectors["down"]
            "L" -> vectors["left"]
            else -> null
        }
    }
}
