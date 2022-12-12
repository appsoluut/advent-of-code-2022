package app.appsoluut.signal

import java.io.File

class Signal constructor(
    private val verbose: Boolean,
    private val source: String
) {
    private var value = 1
    private val signals = mutableListOf<Int>()
    private val commands = mutableMapOf<Int, Int>()
    private val multiplyAt = listOf(20, 60, 100, 140, 180, 220)
    private val screenWidth = 40
    private val screenHeight = 6

    init {
        parse()
    }

    fun sum() {
        println("Total instructions: ${commands.size} (cycles: ${commands.keys.last()})")
        for (cycle in 1 ..commands.keys.last()) {
            if (verbose) {
                print("- Start of $cycle -> Value = $value -> ${commands[cycle]} ")
            }
            if (multiplyAt.contains(cycle)) {
                val mul = value * cycle
                signals.add(mul)
                if (verbose) {
                    print("-> multi: $mul (str: ${signals.last()}) ")
                }
            }
            commands[cycle]?.let { addx ->
                value += addx
            }
            if (verbose) {
                println("-> End of cycle: $value")
            }
        }
        println("- End value: $value")
        signals.forEachIndexed { index, strength ->
            println("+ Signal at ${multiplyAt[index]} = $strength")
        }
        println("- Total strength: ${signals.sum()}")
    }

    fun draw() {
        for (cycle in 1 .. commands.keys.last()) {
            val pixel = (cycle - 1) % 40
            print(if (pixel >= value - 1 && pixel < value + 2) "#" else ".")
            if (pixel == 39) {
                println()
            }
            commands[cycle]?.let { addx ->
                value += addx
            }
        }
    }

    private fun parse() {
        val file = File(source)
        file.forEachLine { line ->
            val cmd = line.split(" ", limit = 2)
            if (verbose) {
                println("-> $cmd")
            }
            val cycle = when(cmd[0]) {
                "noop" -> Cycle.NOOP
                "addx" -> Cycle.ADDX
                else -> Cycle.INVALID
            }

            val key = (commands.keys.lastOrNull() ?: 0) + cycle.cycles
            val calc = if (cmd.size == 2) cmd[1].toInt() else 0
            commands[key] = calc
        }
    }

    sealed class Cycle(val cycles: Int) {
        object ADDX : Cycle(2)
        object NOOP : Cycle(1)
        object INVALID : Cycle(0)
    }
}
