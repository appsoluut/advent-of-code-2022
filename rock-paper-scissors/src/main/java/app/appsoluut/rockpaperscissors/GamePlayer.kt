package app.appsoluut.rockpaperscissors

interface GamePlayer {
    fun run()

    fun printStats(rounds: List<Game.Round>) {
        var totalScore = 0
        rounds.forEach { round ->
            totalScore += round.totalScore
        }

        println("""
            You have played ${rounds.size} games.
            
            This strategy results in the following:
            - Wins : ${rounds.filter { it.status == GameStatus.Win }.size}
            - Draws: ${rounds.filter { it.status == GameStatus.Draw }.size}
            - Lost : ${rounds.filter { it.status == GameStatus.Lose }.size}
            
            Your total score is: $totalScore
        """.trimIndent())
    }
}