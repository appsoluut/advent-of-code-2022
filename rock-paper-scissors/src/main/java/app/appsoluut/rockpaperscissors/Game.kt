package app.appsoluut.rockpaperscissors

class Game {
    private val SCORE_WIN = 6
    private val SCORE_DRAW = 3
    private val SCORE_LOSE = 0

    private val SCORE_ROCK = 1
    private val SCORE_PAPER = 2
    private val SCORE_SCISSORS = 3
    private val SCORE_UNKNOWN = 0

    fun scoreHands(theirs: Hand, mine: Hand): Round {
        return determineStatus(theirs, mine)
    }

    fun scoreSecret(theirs: Hand, status: GameStatus): Round {
        return determineSecret(theirs, status)
    }

    private fun determineStatus(theirs: Hand, mine: Hand): Round {
        val status = processAsHands(theirs, mine)
        val statusScore = when(status) {
            GameStatus.Win -> SCORE_WIN
            GameStatus.Lose -> SCORE_LOSE
            GameStatus.Draw -> SCORE_DRAW
        }

        val handScore = when(mine) {
            Hand.Rock -> SCORE_ROCK
            Hand.Paper -> SCORE_PAPER
            Hand.Scissors -> SCORE_SCISSORS
            Hand.Unknown -> SCORE_UNKNOWN
        }

        return Round(
            status = status,
            theirs = theirs,
            mine = mine,
            handScore = handScore,
            statusScore = statusScore
        )
    }

    private fun determineSecret(theirs: Hand, status: GameStatus): Round {
        val statusScore = when(status) {
            GameStatus.Win -> SCORE_WIN
            GameStatus.Lose -> SCORE_LOSE
            GameStatus.Draw -> SCORE_DRAW
        }

        val mine = when(status) {
            GameStatus.Win -> when(theirs) {
                Hand.Rock -> Hand.Paper
                Hand.Paper -> Hand.Scissors
                Hand.Scissors -> Hand.Rock
                Hand.Unknown -> Hand.Unknown
            }
            GameStatus.Lose -> when(theirs) {
                Hand.Rock -> Hand.Scissors
                Hand.Paper -> Hand.Rock
                Hand.Scissors -> Hand.Paper
                Hand.Unknown -> Hand.Unknown
            }
            GameStatus.Draw -> theirs
        }

        val handScore = when(mine) {
            Hand.Rock -> SCORE_ROCK
            Hand.Paper -> SCORE_PAPER
            Hand.Scissors -> SCORE_SCISSORS
            Hand.Unknown -> SCORE_UNKNOWN
        }

        return Round(
            status = status,
            theirs = theirs,
            mine = mine,
            handScore = handScore,
            statusScore = statusScore
        )
    }

    private fun processAsHands(theirs: Hand, mine: Hand): GameStatus {
        val status = when (mine) {
            Hand.Rock -> when(theirs) {
                Hand.Rock -> GameStatus.Draw
                Hand.Paper -> GameStatus.Lose
                Hand.Scissors -> GameStatus.Win
                else -> GameStatus.Lose
            }
            Hand.Paper -> when(theirs) {
                Hand.Rock -> GameStatus.Win
                Hand.Paper -> GameStatus.Draw
                Hand.Scissors -> GameStatus.Lose
                else -> GameStatus.Lose
            }
            Hand.Scissors -> when(theirs) {
                Hand.Rock -> GameStatus.Lose
                Hand.Paper -> GameStatus.Win
                Hand.Scissors -> GameStatus.Draw
                else -> GameStatus.Lose
            }
            else -> GameStatus.Lose
        }

        return status
    }

    data class Round(
        val status: GameStatus,
        val theirs: Hand,
        val mine: Hand,
        val handScore: Int,
        val statusScore: Int,
        val totalScore: Int = handScore + statusScore
    )
}