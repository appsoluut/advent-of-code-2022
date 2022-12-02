package app.appsoluut.rockpaperscissors

enum class GameStatus(private val type: String) {
    Win("Z"),
    Lose("X"),
    Draw("Y");

    companion object {
        fun map(type: String): GameStatus {
            values().forEach { status ->
                if (status.type == type) {
                    return status
                }
            }
            return Lose
        }
    }
}
