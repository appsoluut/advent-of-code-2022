package app.appsoluut.supply

import java.io.File

class Parser constructor(
    private val verbose: Boolean
) {
    private var mode = Mode.Stacks
    val stacks: MutableList<MutableList<String?>> = mutableListOf()
    val moves: MutableList<Move> = mutableListOf()
    var stackWidth = 0
    var stackHeight = 0

    fun parse(file: String) {
        val source = File(file)
        if (verbose) {
            println("-- Parsing file $source --")
        }

        source.forEachLine { line ->
            if (line.isBlank()) {
                mode = Mode.Moves
            } else {
                when(mode) {
                    Mode.Stacks -> buildStacks(line)
                    Mode.Moves -> buildMoves(line)
                }
            }
        }

        if (verbose) {
            dumpStacks()
            println()
            dumpMoves()
        }
    }

    fun dumpStacks() {
        println("*** Stacks ($stackWidth x $stackHeight) ***")
        stacks.forEach { row ->
            row.forEach { column ->
                column?.let {
                    print("[$column]")
                } ?: print("   ")
                print(" ")
            }
            println("")
        }
    }

    private fun dumpMoves() {
        println("*** There are ${moves.size} moves ***")
        moves.forEach { move ->
            println("- Move ${move.amount} supplies from column ${move.from} to ${move.to}.")
        }
    }

    private fun buildStacks(line: String) {
        if (verbose) {
            println("* Stack: $line")
        }

        val supplies = line.chunked(4) { it.trim().toString() }
        if (!supplies[0].contains('1')) {
            val stack = mutableListOf<String?>()
            supplies.forEachIndexed { index, supply ->
                if (supply.isNotBlank()) {
                    stack.add(index, supply.filter { it.isLetter() })
                } else {
                    stack.add(index, null)
                }

                if (index + 1 > stackWidth) {
                    stackWidth = index + 1
                }
            }
            stacks.add(stackHeight, stack)
            stackHeight++
        }
    }

    private fun buildMoves(line: String) {
        if (verbose) {
            println("* Move: $line")
        }

        val regex = "move (\\d+) from (\\d+) to (\\d+)".toRegex()
        val matches = regex.find(line)?.groups
        moves.add(Move(
            amount = matches?.get(1)?.value?.toInt() ?: 0,
            from = matches?.get(2)?.value?.toInt()?.minus(1) ?: 0,
            to = matches?.get(3)?.value?.toInt()?.minus(1) ?: 0
        ))
    }

    private enum class Mode {
        Stacks, Moves
    }

    data class Move(
        val amount: Int,
        val from: Int,
        val to: Int
    )
}