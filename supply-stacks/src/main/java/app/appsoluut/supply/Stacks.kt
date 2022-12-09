package app.appsoluut.supply

class Stacks constructor(
    private val verbose: Boolean,
    private val source: String
) {
    private val parser = Parser(verbose)

    fun partA() {
        println("-- First part of the puzzle --")
        parser.parse(source)

        var amount: Int
        var moved: Boolean
        var index: Int
        var step = 0
        val stopAtStep = parser.moves.size
        parser.moves.forEach { move ->
            if (step++ <= stopAtStep) {
                if (verbose) {
                    println("Step: $step")
                }
                amount = 0
                while(amount < move.amount) {
                    index = 0
                    moved = false
                    while(index < parser.stackHeight && !moved) {
                        try {
                            val row = parser.stacks[index]
                            if (row[move.from]?.isNotBlank() == true) {
                                println("$index. Move ${row[move.from]} to ${move.to}")
                                drop(row[move.from]!!, move.to)
                                row[move.from] = null
                                moved = true
                            }
                        } catch (e: java.lang.IndexOutOfBoundsException) {
                            // Doesn't have this many elements, continue
                        }
                        index++
                    }
                    amount++

                    if (verbose) {
                        parser.dumpStacks()
                    }
                }

                if (verbose) {
                    println("----")
                }
            }
        }

        val suppliesOnTop = getSupplyOnTopOfEachStack().joinToString("")
        parser.dumpStacks()
        println("""
            Done moving all supplies around.
            - Supplies on top of each stack are: $suppliesOnTop.
        """.trimIndent())
    }

    private fun getSupplyOnTopOfEachStack(): List<String> {
        val supplies = mutableListOf<String>()
        var found: Boolean
        var startColumn = 0
        for (repeat in 0 until parser.stacks[0].size) {
            found = false
            for (row in 0 until parser.stacks.size) {
                for (column in startColumn until parser.stacks[row].size) {
                    if (parser.stacks[row][column] != null) {
                        supplies.add(parser.stacks[row][column]!!)
                        found = true
                    }
                    break
                }
                if (found) {
                    startColumn++
                    break
                }
            }
        }
        return supplies.toList()
    }

    private fun drop(supply: String, column: Int) {
        var index = 0
        while (index < parser.stackHeight) {
            val row = parser.stacks[index]

            if (verbose) {
                println("* Dropping $supply @ $index -> row: $row")
            }
            try {
                if (index == 0 && row[column]?.isNotBlank() == true) {
                    val newRow = mutableListOf<String?>()
                    for (i in 0 .. column) {
                        newRow.add(if (i == column) supply else null)
                    }
                    parser.stacks.add(0, newRow)
                    parser.stackHeight++
                    break
                } else if (row[column] == null && (index == parser.stacks.size - 1 || parser.stacks[index + 1][column] != null)) {
                    row[column] = supply
                    break
                }
                index++
            } catch (e: java.lang.IndexOutOfBoundsException) {
                if (verbose) {
                    println("Adding row")
                }
                if (row.size < parser.stackWidth) {
                    row.add(null)
                } else {
                    index++
                }
            }
        }
    }

    fun partB() {
        println("-- Second part of the puzzle --")
        parser.parse(source)

        var moved: Boolean
        var index: Int
        var step = 0
        val stopAtStep = parser.moves.size
        parser.moves.forEach { move ->
            if (step++ <= stopAtStep) {
                if (verbose) {
                    println("Step: $step - Amount: ${move.amount}")
                }

                for (amount in move.amount - 1 downTo 0) {
                    index = 0
                    moved = false
                    while(index < parser.stackHeight && !moved) {
                        try {
                            if (parser.stacks[index][move.from]?.isNotBlank() == true) {
                                val row = parser.stacks[index + amount]
                                println("$index. Move ${row[move.from]} to ${move.to}")
                                drop(row[move.from]!!, move.to)
                                row[move.from] = null
                                moved = true
                            }
                        } catch (e: java.lang.IndexOutOfBoundsException) {
                            // Doesn't have this many elements, continue
                        }

                        index++
                    }

                    if (verbose) {
                        parser.dumpStacks()
                    }
                }

                if (verbose) {
                    println("----")
                }
            }
        }

        val suppliesOnTop = getSupplyOnTopOfEachStack().joinToString("")
        parser.dumpStacks()
        println("""
            Done moving all supplies around.
            - Supplies on top of each stack are: $suppliesOnTop.
        """.trimIndent())
    }
}
