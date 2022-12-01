package app.appsoluut.caloriecounting

import java.io.File

class Calories constructor(
    private val verbose: Boolean = false,
    private val source: String,
    private val top: Int
) {
    fun run() {
        if (verbose) {
            println("Reading file: $source")
        }

        val file = File(source)
        val elves = parse(file)
        println("*** Found ${elves.size} elves! ***")

        val winner = findMaxCalories(elves)
        println("*** Elf carrying the most amount of calories is... Elf number ${winner.number} with ${winner.calories} calories!")
        println("")

        val runners = top(elves = elves, amount = top)
        println("*** Top $top elves:")
        runners.forEachIndexed { index, elf ->
            println("${index + 1}. $elf")
        }
        println("\nTotal calories: ${runners.sumOf { it.calories }}")
        println("")
        println("-- Done! --")
    }

    private fun parse(file: File): Map<Int, Int> {
        val elves = mutableMapOf<Int, Int>()

        var currentElf = 1
        file.forEachLine { line ->
            if (line.isBlank()) {
                if (verbose) {
                    println("Elf $currentElf -> Carrying: ${elves[currentElf]}")
                }
                currentElf++
            } else {
                val calories = line.toInt()

                if (elves.containsKey(currentElf)) {
                    elves[currentElf]?.let { carrying ->
                        elves[currentElf] = carrying + calories
                    }
                } else {
                    elves[currentElf] = calories
                }
            }
        }

        return elves.toMap()
    }

    private fun findMaxCalories(elves: Map<Int, Int>): Elf {
        if (verbose) {
            println("... Starting to look for elf that carries the most calories")
        }

        val max = elves.maxBy { it.value }
        if (verbose) {
            println("Max value: $max")
        }
        return Elf(
            number = max.key,
            calories = max.value
        )
    }

    private fun top(elves: Map<Int, Int>, amount: Int = 3): List<Elf> {
        val sorted = elves.toList().sortedBy { (_, value) -> value }.asReversed().take(amount).map {
            Elf(
                number = it.first,
                calories = it.second
            )
        }
        return sorted
    }

    data class Elf(
        val number: Int,
        val calories: Int
    )
}