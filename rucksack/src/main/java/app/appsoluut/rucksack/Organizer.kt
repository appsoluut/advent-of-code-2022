package app.appsoluut.rucksack

import java.io.File

class Organizer constructor(
    private val verbose: Boolean,
    private val source: String
) {
    private val priorities = "abcdefghijklmnopqrstuvwxyz"

    fun partA() {
        println("-- First part of the puzzle --")
        val rucksacks = parse(File(source))
        val duplicates = findDuplicateItems(rucksacks)

        dumpStats(duplicates)
    }

    fun partB() {
        println("-- Second part of the puzzle --")
        val groups = parse(File(source)).chunked(3)
        var totalPriority = 0
        groups.forEachIndexed { index, group ->
            if (verbose) {
                println("Group ${index + 1}")
                println("Rucksacks")
            }

            val packs = mutableMapOf<Int, List<String>>()
            group.forEachIndexed { packIndex, rucksack ->
                if (verbose) {
                    println("- $rucksack")
                }

                val compartment = rucksack.compartments[0].chunked(1).toMutableList()
                compartment.addAll(rucksack.compartments[1].chunked(1))

                packs[packIndex] = compartment.toList().distinct()
            }

            val intersect = packs[0]!!.intersect(packs[1]!!.toSet()).intersect(packs[2]!!.toSet()).first()
            val priority = if (intersect.uppercase() == intersect) {
                priorities.uppercase().indexOf(intersect) + 27
            } else {
                priorities.indexOf(intersect) + 1
            }

            if (verbose) {
                println("* Group ${index + 1} common item is $intersect => $priority")
            }

            totalPriority += priority
        }

        println("""
            There are ${groups.size} groups.
            - Total priority $totalPriority.
        """.trimIndent())
    }

    private fun dumpStats(duplicates: List<Duplicate>) {
        var totalPriority = 0

        duplicates.forEach {
            totalPriority += it.priority
        }

        println("""
            There are ${duplicates.size} rucksacks.
            - Total priority $totalPriority.
        """.trimIndent())
    }

    private fun findDuplicateItems(rucksacks: List<Rucksack>): List<Duplicate> {
        if (verbose) {
            println("-- Looking for doubles in different compartments --")
        }

        var duplicates = mutableListOf<Duplicate>()

        rucksacks.forEach { rucksack ->
            val compartment1 = rucksack.compartments[0].chunked(1).distinct()
            val compartment2 = rucksack.compartments[1].chunked(1).distinct()

            val intersect = compartment2.intersect(compartment1.toSet()).first()
            val priority = if (intersect.uppercase() == intersect) {
                priorities.uppercase().indexOf(intersect) + 27
            } else {
                priorities.indexOf(intersect) + 1
            }

            if (verbose) {
                println("* Rucksack ${rucksack.rucksack}. found duplicates: $intersect => $priority")
            }

            duplicates.add(Duplicate(
                rucksack = rucksack,
                intersect = intersect,
                priority = priority
            ))
        }

        if (verbose) {
            println("-- Done --\n")
        }

        return duplicates.toList()
    }

    private fun parse(source: File): List<Rucksack> {
        if (verbose) {
            println("-- Parsing file $source --")
        }

        val rucksacks = mutableListOf<Rucksack>()
        var rucksack = 1

        source.forEachLine { line ->
            val sack = Rucksack(
                rucksack = rucksack++,
                compartments = line.chunked(line.length / 2)
            )

            if (verbose) {
                println("* Rucksack: $sack")
            }

            rucksacks.add(sack)
        }

        if (verbose) {
            println("-- Done --\n")
        }

        return rucksacks.toList()
    }
}

data class Rucksack(
    val rucksack: Int,
    val compartments: List<String>
)

data class Duplicate(
    val rucksack: Rucksack,
    val intersect: String,
    val priority: Int
)