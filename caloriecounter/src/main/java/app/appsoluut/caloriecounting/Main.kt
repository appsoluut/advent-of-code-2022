package app.appsoluut.caloriecounting

import com.xenomachina.argparser.*
import java.io.File

class CalorieArgs(parser: ArgParser) {
    val verbose by parser.flagging(
        "-v", "--verbose",
        help = "enable verbose mode"
    )

    val top by parser.storing(
        "-t", "--top",
        help = "show top amount of elves"
    ).default("3")

    val source by parser.positional(
        "SOURCE",
        help = "source filename"
    ).default("calories.txt")
}

val helpFormatter = DefaultHelpFormatter(
    prologue = "Find the Elf carrying the most Calories. How many total Calories is that Elf carrying?",
    epilogue = "Day 1 of Advent of Coding 2022"
)

fun main(args: Array<String>) = mainBody(programName = "CalorieCounting") {
    val parsedArgs = ArgParser(args = args, helpFormatter = helpFormatter).parseInto(::CalorieArgs)

    parsedArgs.run {
        Calories(
            verbose = verbose,
            source = source,
            top = top.toInt()
        ).run()
    }
}
