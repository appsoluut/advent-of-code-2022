package app.appsoluut.rockpaperscissors

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.DefaultHelpFormatter
import com.xenomachina.argparser.default
import com.xenomachina.argparser.mainBody

class RPSArgs(parser: ArgParser) {
    val verbose by parser.flagging(
        "-v", "--verbose",
        help = "enable verbose mode"
    )

    val mode by parser.mapping(
        "--hands" to Strategy.Hands,
        "--secret" to Strategy.Secret,
        help = "strategy mode to use"
    ).default(Strategy.Hands)

    val source by parser.positional(
        "SOURCE",
        help = "source filename"
    ).default("strategy.txt")
}

val helpFormatter = DefaultHelpFormatter(
    prologue = "Play Rock, Paper, Scissors according to the super secret strategy guide of the elf.",
    epilogue = "Day 2 of Advent of Code 2022 -- https://adventofcode.com/2022/day/2"
)

fun main(args: Array<String>) = mainBody(programName = "RockPaperScissors") {
    val parsedArgs = ArgParser(args = args, helpFormatter = helpFormatter).parseInto(::RPSArgs)

    parsedArgs.run {
        when(mode) {
            Strategy.Hands -> StrategyHands(
                verbose = verbose,
                source = source
            )
            Strategy.Secret -> StrategySecret(
                verbose = verbose,
                source = source
            )
        }.run()
    }
}
