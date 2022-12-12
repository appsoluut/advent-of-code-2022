package app.appsoluut.signal

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.DefaultHelpFormatter
import com.xenomachina.argparser.default
import com.xenomachina.argparser.mainBody

class SignalArgs(parser: ArgParser) {
    val verbose by parser.flagging(
        "-v", "--verbose",
        help = "enable verbose mode"
    )

    val mode by parser.mapping(
        "--signal" to Mode.Signal,
        "--draw" to Mode.Draw,
        help = "length of the rope"
    ).default(Mode.Signal)

    val source by parser.positional(
        "SOURCE",
        help = "source filename"
    ).default("program.txt")
}

enum class Mode {
    Signal, Draw
}

val helpFormatter = DefaultHelpFormatter(
    prologue = "Cathode-Ray Tube.",
    epilogue = "Day 10 of Advent of Code 2022 -- https://adventofcode.com/2022/day/10"
)

fun main(args: Array<String>) = mainBody(programName = "Signal") {
    val parsedArgs = ArgParser(args = args, helpFormatter = helpFormatter).parseInto(::SignalArgs)

    parsedArgs.run {
        Signal(
            verbose = verbose,
            source = source,
        ).run {
            when(mode) {
                Mode.Signal -> sum()
                Mode.Draw -> draw()
            }
        }
    }
}
