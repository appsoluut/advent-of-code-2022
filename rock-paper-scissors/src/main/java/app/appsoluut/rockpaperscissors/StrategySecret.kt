package app.appsoluut.rockpaperscissors

import java.io.File

class StrategySecret constructor(
    private val verbose: Boolean = false,
    private val source: String
) : GamePlayer {
    private val game: Game = Game()

    override fun run() {
        println("-- Super Secret Strategy Mode --\n")

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
                val secret = line.split(" ")
                val roundResult = game.scoreSecret(
                    theirs = Hand.map(secret[0]), // hand
                    status = GameStatus.map(secret[1]) // status
                )
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