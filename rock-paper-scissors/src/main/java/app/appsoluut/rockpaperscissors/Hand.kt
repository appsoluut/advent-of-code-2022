package app.appsoluut.rockpaperscissors

enum class Hand(
    private val their: String,
    private val mine: String
) {
    Rock("A", "X"),
    Paper("B", "Y"),
    Scissors("C", "Z"),
    Unknown("", "");

    companion object {
        fun map(type: String): Hand {
            values().forEach { hand ->
                if (hand.their == type || hand.mine == type) {
                    return hand
                }
            }
            return Unknown
        }
    }
}