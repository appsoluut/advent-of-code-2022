package app.appsoluut.monkey

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.DefaultHelpFormatter
import com.xenomachina.argparser.default
import com.xenomachina.argparser.mainBody

class MonkeyArgs(parser: ArgParser) {
    val verbose by parser.flagging(
        "-v", "--verbose",
        help = "enable verbose mode"
    )

    val mode by parser.mapping(
        "--inspect" to Mode.Inspect,
        "--manage" to Mode.Manage,
        help = "monkey business"
    ).default(Mode.Inspect)

    val source by parser.positional(
        "SOURCE",
        help = "source filename"
    ).default("monkeys.txt")
}

enum class Mode {
    Inspect, Manage
}

val helpFormatter = DefaultHelpFormatter(
    prologue = "Monkey in the Middle.",
    epilogue = "Day 11 of Advent of Code 2022 -- https://adventofcode.com/2022/day/11"
)

fun main(args: Array<String>) = mainBody(programName = "Monkey") {
    val parsedArgs = ArgParser(args = args, helpFormatter = helpFormatter).parseInto(::MonkeyArgs)

    parsedArgs.run {
        KeepAway(
            verbose = verbose,
            source = source,
        ).run {
            when(mode) {
                Mode.Inspect -> inspect()
                Mode.Manage -> manage()
            }
        }
    }
}
