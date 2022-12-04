package app.appsoluut.campcleanup

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
    ).default("cleanup.txt")
}

val helpFormatter = DefaultHelpFormatter(
    prologue = "Camp Cleanup. Detect the amount of overlapping sections.",
    epilogue = "Day 4 of Advent of Code 2022 -- https://adventofcode.com/2022/day/4"
)

fun main(args: Array<String>) = mainBody(programName = "Cleanup") {
    val parsedArgs = ArgParser(args = args, helpFormatter = helpFormatter).parseInto(::CCArgs)

    parsedArgs.run {
        Cleanup(
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
