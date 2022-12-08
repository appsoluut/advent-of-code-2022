package app.appsoluut.tree

import java.io.File

class Treetop constructor(
    private val verbose: Boolean,
    private val source: String
) {

    private fun parse(): List<List<Int>> {
        val file = File(source)
        val trees: MutableList<List<Int>> = mutableListOf()
        file.forEachLine { line ->
            if (verbose) {
                println("Line: $line")
            }
            trees.add(line.chunked(1).map { it.toInt() })
        }
        return trees.toList()
    }

    fun countVisible() {
        val trees = parse()
        if (verbose) {
            println("--- Trees: ---")
            trees.forEach {
                println(it)
            }
        }

        var counter = 0
        for (row in 1 until trees.size - 1) {
            if (verbose) {
                print("Row $row: ")
            }
            for (column in 1 until trees[row].size - 1) {
                val visible = isVisible(row, column, trees)
                if (visible) {
                    counter++
                }
                if (verbose) {
                    print("${trees[row][column]}[" + if (visible) {"x"} else {" "} + "] ")
                }
            }
            if (verbose) {
                println()
            }
        }

        val surrounding = (trees.size * 2) + (trees[0].size * 2) - 4
        println("""
            Visible trees edge = $surrounding.
            Visible trees inside = $counter.
            -----
            Total: ${surrounding + counter}.
        """.trimIndent())
    }

    fun scenicScore() {
        val trees = parse()

        var scenicScore = 0
        var x = 0
        var y = 0
        for (row in 1 until trees.size - 1) {
            if (verbose) {
                println("Row $row")
            }
            for (column in 1 until trees[row].size - 1) {
                val score = score(row, column, trees)
                if (verbose) {
                    println("* Tree: ${trees[row][column]} - Score: $score")
                }
                if (score > scenicScore) {
                    scenicScore = score
                    x = column
                    y = row
                }
            }
            if (verbose) {
                println("----")
            }
        }

        println("""
            Highest scenic score: $scenicScore.
            Located at: $x,$y.
            Tree: ${trees[y][x]}.
        """.trimIndent())
    }

    private fun horizontal(height: Int, trees: List<Int>): Boolean {
        var highest = 0
        trees.forEach {
            if (it > highest) {
                highest = it
            }
        }
        return highest < height
    }

    private fun vertical(height: Int, column: Int, from: Int = 0, to: Int, trees: List<List<Int>>, reversed: Boolean = false): Boolean {
        var highest = 0

        val iterator = if (reversed) {
            to - 1 downTo from
        } else {
            from until to
        }

        for (row in iterator) {
            if (trees[row][column] > highest) {
                highest = trees[row][column]
            }
        }
        return highest < height
    }

    private fun isVisible(row: Int, column: Int, trees: List<List<Int>>): Boolean {
        val height = trees[row][column]
        var visible = horizontal(height, trees[row].subList(0, column))
        if (!visible) {
             visible = horizontal(height, trees[row].subList(column + 1, trees[row].size).asReversed())
        }
        if (!visible) {
            visible = vertical(height, column, 0, row, trees)
        }
        if (!visible) {
            visible = vertical(height, column, row + 1, trees.size, trees, true)
        }
        return visible
    }

    private fun score(row: Int, column: Int, trees: List<List<Int>>): Int {
        var score = 0

        val height = trees[row][column]

        var distanceLeft = 0
        var distanceRight = 0
        var distanceUp = 0
        var distanceDown = 0
        for (x in column - 1 downTo 0) {
            distanceLeft++
            if (trees[row][x] >= height)
                break
        }

        for (x in column + 1 until trees[row].size) {
            distanceRight++
            if (trees[row][x] >= height)
                break
        }

        for (y in row - 1 downTo 0) {
            distanceUp++
            if (trees[y][column] >= height)
                break
        }

        for (y in row + 1 until trees.size) {
            distanceDown++
            if (trees[y][column] >= height)
                break
        }

        score = distanceUp * distanceLeft * distanceDown * distanceRight

        if (verbose) {
            println("""
                Left: $distanceLeft
                Right: $distanceRight
                Up: $distanceUp
                Down: $distanceDown
                Score: $score
            """.trimIndent())
        }

        return score
    }
}
