package app.appsoluut.tree

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.DefaultHelpFormatter
import com.xenomachina.argparser.default
import com.xenomachina.argparser.mainBody

class TTHArgs(parser: ArgParser) {
    val verbose by parser.flagging(
        "-v", "--verbose",
        help = "enable verbose mode"
    )

    val mode by parser.mapping(
        "--visible" to Parts.Visible,
        "--score" to Parts.Score,
        help = "mode of operandi"
    ).default(Parts.Visible)

    val source by parser.positional(
        "SOURCE",
        help = "source filename"
    ).default("trees.txt")
}

val helpFormatter = DefaultHelpFormatter(
    prologue = "Treetop Tree House. Determine the best tree to build a treetop house in.",
    epilogue = "Day 8 of Advent of Code 2022 -- https://adventofcode.com/2022/day/8"
)

fun main(args: Array<String>) = mainBody(programName = "Tree") {
    val parsedArgs = ArgParser(args = args, helpFormatter = helpFormatter).parseInto(::TTHArgs)

    parsedArgs.run {
        Treetop(
            verbose = verbose,
            source = source,
        ).run {
            when(mode) {
                Parts.Visible -> countVisible()
                Parts.Score -> scenicScore()
            }
        }
    }
}

enum class Parts {
    Visible, Score
}
