package app.appsoluut.rb

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.DefaultHelpFormatter
import com.xenomachina.argparser.default
import com.xenomachina.argparser.mainBody

class RBArgs(parser: ArgParser) {
    val verbose by parser.flagging(
        "-v", "--verbose",
        help = "enable verbose mode"
    )

    val length by parser.storing(
        "-l", "--length",
        help = "length of the rope"
    ).default("2")

    val source by parser.positional(
        "SOURCE",
        help = "source filename"
    ).default("moves.txt")
}

val helpFormatter = DefaultHelpFormatter(
    prologue = "Rope Bridge.",
    epilogue = "Day 9 of Advent of Code 2022 -- https://adventofcode.com/2022/day/9"
)

fun main(args: Array<String>) = mainBody(programName = "RB") {
    val parsedArgs = ArgParser(args = args, helpFormatter = helpFormatter).parseInto(::RBArgs)

    parsedArgs.run {
        Rope(
            verbose = verbose,
            source = source,
        ).run {
            countVisited(length.toInt())
        }
    }
}
