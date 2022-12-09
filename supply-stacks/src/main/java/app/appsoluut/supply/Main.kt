package app.appsoluut.supply

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.DefaultHelpFormatter
import com.xenomachina.argparser.default
import com.xenomachina.argparser.mainBody

class CCArgs(parser: ArgParser) {
    val verbose by parser.flagging(
        "-v", "--verbose",
        help = "enable verbose mode"
    )

    val part by parser.mapping(
        "--1" to Parts.First,
        "--2" to Parts.Second,
        help = "puzzle part to run"
    ).default(Parts.First)

    val source by parser.positional(
        "SOURCE",
        help = "source filename"
    ).default("stacks.txt")
}

val helpFormatter = DefaultHelpFormatter(
    prologue = "Supply Stacks.",
    epilogue = "Day 5 of Advent of Code 2022 -- https://adventofcode.com/2022/day/5"
)

fun main(args: Array<String>) = mainBody(programName = "Supply") {
    val parsedArgs = ArgParser(args = args, helpFormatter = helpFormatter).parseInto(::CCArgs)

    parsedArgs.run {
        Stacks(
            verbose = verbose,
            source = source,
        ).run {
            when(part) {
                Parts.First -> partA()
                Parts.Second -> partB()
            }
        }
    }
}

enum class Parts {
    First, Second
}
