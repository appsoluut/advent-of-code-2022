package app.appsoluut.rockpaperscissors

import java.io.File

class StrategyHands constructor(
    private val verbose: Boolean = false,
    private val source: String
) : GamePlayer {
    private val game: Game = Game()

    override fun run() {
        println("-- Hands Strategy Mode --\n")
        if (verbose) {
            println("Using strategy file input: $source")
        }

        val file = File(source)
        val rounds = parse(file)
        printStats(rounds)
    }

    private fun parse(file: File): List<Game.Round> {
        var round = 1
        val rounds = mutableListOf<Game.Round>()
        file.forEachLine { line ->
            if (line.isNotBlank()) {
                val hands = line.split(" ").map { Hand.map(it) }
                val roundResult = game.scoreHands(hands[0], hands[1])
                if (verbose) {
                    println("Round $round -> $roundResult")
                }
                rounds.add(roundResult)
                round++
            }
        }
        return rounds.toList()
    }
}