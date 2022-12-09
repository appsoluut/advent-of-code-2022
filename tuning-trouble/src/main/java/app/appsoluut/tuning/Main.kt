package app.appsoluut.tuning

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.DefaultHelpFormatter
import com.xenomachina.argparser.default
import com.xenomachina.argparser.mainBody

class TTArgs(parser: ArgParser) {
    val verbose by parser.flagging(
        "-v", "--verbose",
        help = "enable verbose mode"
    )

    val packetSize by parser.storing(
        "-s", "--size",
        help = "packet size"
    ).default("4")

    val source by parser.positional(
        "SOURCE",
        help = "source filename"
    ).default("buffer.txt")
}

val helpFormatter = DefaultHelpFormatter(
    prologue = "Tuning Trouble.",
    epilogue = "Day 6 of Advent of Code 2022 -- https://adventofcode.com/2022/day/6"
)

fun main(args: Array<String>) = mainBody(programName = "Tuning") {
    val parsedArgs = ArgParser(args = args, helpFormatter = helpFormatter).parseInto(::TTArgs)

    parsedArgs.run {
        Tuning(
            verbose = verbose,
            source = source,
        ).search(packetSize = packetSize.toInt())
    }
}

enum class Parts {
    First, Second
}
