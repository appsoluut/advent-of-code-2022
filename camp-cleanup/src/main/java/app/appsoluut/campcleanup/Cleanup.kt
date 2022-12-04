package app.appsoluut.campcleanup

import java.io.File

class Cleanup constructor(
    private val verbose: Boolean,
    private val source: String
) {

    fun partA() {
        println("-- First part of the puzzle --")
        val sections = parse(File(source))
        var reconsider = 0
        sections.forEachIndexed { index, pairs ->
            var inclusive = false
            if (pairs.first.first >= pairs.second.first && pairs.first.last <= pairs.second.last) {
                inclusive = true
            } else if (pairs.second.first >= pairs.first.first && pairs.second.last <= pairs.first.last) {
                inclusive = true
            }

            if (inclusive) {
                reconsider++
            }

            if (verbose) {
                println("Pair ${index + 1}: Has overlap = $inclusive")
            }
        }

        println("""
            There are ${sections.size} pairs.
            - Total with overlap: $reconsider.
        """.trimIndent())
    }

    fun partB() {
        println("-- Second part of the puzzle --")
        val sections = parse(File(source))
        var reconsider = 0
        sections.forEachIndexed { index, pairs ->
            var inclusive = false
            if (pairs.first.first >= pairs.second.first && pairs.first.first <= pairs.second.last) {
                inclusive = true
            } else if (pairs.second.first >= pairs.first.first && pairs.second.first <= pairs.first.last) {
                inclusive = true
            }

            if (inclusive) {
                reconsider++
            }

            if (verbose) {
                println("Pair ${index + 1}: Has overlap = $inclusive")
            }
        }

        println("""
            There are ${sections.size} pairs.
            - Total with overlap: $reconsider.
        """.trimIndent())
    }

    private fun parse(source: File): List<Pair<IntRange, IntRange>> {
        if (verbose) {
            println("-- Parsing file $source --")
        }

        val sectionList = mutableListOf<Pair<IntRange, IntRange>>()

        source.forEachLine { line ->
            val section = line.split(",").map(::toSection)
            sectionList.add(Pair(section[0], section[1]))
        }

        return sectionList.toList()
    }

    private fun toSection(range: String): IntRange {
        val r = range.split("-")
        return IntRange(r[0].toInt(), r[1].toInt())
    }
}
