package app.appsoluut.monkey

import java.io.File

class KeepAway constructor(
    private val verbose: Boolean,
    private val source: String
) {
    private val items = mutableListOf<Long>()
    private val monkeys = mutableListOf<Monkey>()
    private val interactions = mutableListOf<Long>()
    private var modulo = 0

    init {
        parse()
    }

    fun manage() {
        println("The monkeys are playing Keep Away!")

        val roundsToBePlayed = 10_000
        for (round in 1 .. roundsToBePlayed) {
            monkeys.forEach { monkey ->
                monkey.items.toList().forEach { item ->
                    var worryLevel = items[item]
                    interactions[monkey.id]++
                    val value: Long = if (monkey.operationValue == 0L) worryLevel else monkey.operationValue
                    worryLevel = when (monkey.operation) {
                        Operation.Add -> worryLevel + value
                        Operation.Substract -> worryLevel - value
                        Operation.Divide -> worryLevel / value
                        Operation.Multiply -> worryLevel * value
                    }

                    worryLevel %= modulo
                    items[item] = worryLevel

                    if (worryLevel % monkey.testDivisibleBy == 0L) {
                        monkeys[monkey.testSuccess].items.add(monkey.items.removeFirst())
                    } else {
                        monkeys[monkey.testFail].items.add(monkey.items.removeFirst())
                    }
                }
            }

            if (verbose) {
                println("== After round $round ==")
                interactions.forEachIndexed { id, inspections ->
                    println("Monkey ${monkeys[id].id} inspected items $inspections times.")
                }
            }
        }

        interactions.sortDescending()
        println("== At end ==")
        interactions.forEachIndexed { id, inspections ->
            println("Monkey ${monkeys[id].id} inspected items $inspections times.")
        }
        val results = interactions.take(2)
        println("The monkey business level is ${results[0] * results[1]}.")
    }

    fun inspect() {
        println("The monkeys are playing Keep Away!")

        val roundsToBePlayed = 20
        for (round in 1 .. roundsToBePlayed) {
            monkeys.forEach { monkey ->
                if (verbose) {
                    println("Monkey ${monkey.id}:")
                }

                monkey.items.toList().forEach { item ->
                    var worryLevel = items[item]
                    interactions[monkey.id]++
                    if (verbose) {
                        println("  Monkey inspects an item with a worry level of $worryLevel.")
                    }
                    val value = if (monkey.operationValue == 0L) worryLevel else monkey.operationValue
                    worryLevel = when (monkey.operation) {
                        Operation.Add -> worryLevel + value
                        Operation.Substract -> worryLevel - value
                        Operation.Divide -> worryLevel / value
                        Operation.Multiply -> worryLevel * value
                    }
                    if (verbose) {
                        val what = when (monkey.operation) {
                            Operation.Add -> "increased"
                            Operation.Substract -> "decreased"
                            Operation.Divide -> "divided"
                            Operation.Multiply -> "multiplied"
                        }
                        println("    Worry level is $what by $value to $worryLevel.")
                    }
                    worryLevel = worryLevel.floorDiv(3)
                    if (verbose) {
                        println("    Monkey gets bored with item. Worry level is divided by 3 to $worryLevel.")
                    }

                    items[item] = worryLevel

                    if (worryLevel % monkey.testDivisibleBy == 0L) {
                        monkeys[monkey.testSuccess].items.add(monkey.items.removeFirst())
                        if (verbose) {
                            println("""
                                |    Current worry level is divisible by ${monkey.testDivisibleBy}.
                                |    Item with worry level $worryLevel is thrown to monkey ${monkey.testSuccess}.
                            """.trimMargin())
                        }
                    } else {
                        monkeys[monkey.testFail].items.add(monkey.items.removeFirst())
                        if (verbose) {
                            println("""
                                |    Current worry level is not divisible by ${monkey.testDivisibleBy}.
                                |    Item with worry level $worryLevel is thrown to monkey ${monkey.testFail}.
                            """.trimMargin())
                        }
                    }
                }
            }

            if (verbose) {
                println("\nAfter round $round, the monkeys are holding items with these worry levels:")
                monkeys.forEach { monkey ->
                    println("Monkey ${monkey.id}: ${monkey.items.map { items[it] }}")
                }
                println()
            }
        }

        println("Interactions:")
        interactions.forEachIndexed { id, inspections ->
            println("Monkey ${monkeys[id].id} inspected items $inspections times.")
        }

        interactions.sortDescending()
        val results = interactions.take(2)
        println("The monkey business level is ${results[0] * results[1]}.")
    }

    private fun parse() {
        if (verbose) {
           println("... Starting to parse $source.")
        }
        val file = File(source)

        var monkey: Int = -1
        var operation = Operation.Add
        var operationValue = 0L
        val startingItems = mutableListOf<Int>()
        var testDivisibleBy = 0
        var testSuccess = 0
        var testFail = 0
        file.forEachLine { line ->
            if (verbose) {
                println("- $line")
            }

            val monkeyMatcher = "Monkey (\\d+)".toRegex().find(line)?.groupValues
            monkeyMatcher?.let {
                if (monkey != -1) {
                    addMonkey(
                        id = monkey,
                        items = startingItems,
                        operation = operation,
                        operationValue = operationValue,
                        testDivisibleBy = testDivisibleBy,
                        testSuccess = testSuccess,
                        testFail = testFail
                    )
                }
                monkey = it[1].toInt()
                startingItems.clear()
                operationValue = 0
                testDivisibleBy = 0
                testSuccess = 0
                testFail = 0
            }

            if ("Starting items:".toRegex().containsMatchIn(line)) {
                val itemsMatcher = "(\\d+)".toRegex().findAll(line)
                itemsMatcher.forEach { result ->
                    items.add(result.value.toLong())
                    startingItems.add(items.lastIndex)
                }
            } else if ("Operation: new = old [+\\-/*] (\\d+|old)".toRegex().containsMatchIn(line)) {
                val operationMatcher = "([+\\-/*]) (\\d+|old)".toRegex().findAll(line)
                operationMatcher.forEach { result ->
                    val groups = result.groupValues
                    operation = Operation.find(groups[1][0])
                    operationValue = if (groups[2] == "old") 0 else groups[2].toLong()
                }
            } else if ("Test: divisible by \\d+".toRegex().containsMatchIn(line)) {
                val divMatcher = "(\\d+)".toRegex().find(line)
                testDivisibleBy = divMatcher?.groupValues?.get(1)?.toInt() ?: 0
            } else if ("If true: throw to monkey \\d+".toRegex().containsMatchIn(line)) {
                val trueMatcher = "(\\d+)".toRegex().find(line)
                testSuccess = trueMatcher?.groupValues?.get(1)?.toInt() ?: 0
            } else if ("If false: throw to monkey \\d+".toRegex().containsMatchIn(line)) {
                val falseMatcher = "(\\d+)".toRegex().find(line)
                testFail = falseMatcher?.groupValues?.get(1)?.toInt() ?: 0
            }
        }
        addMonkey(
            id = monkey,
            items = startingItems,
            operation = operation,
            operationValue = operationValue,
            testDivisibleBy = testDivisibleBy,
            testSuccess = testSuccess,
            testFail = testFail
        )

        if (verbose) {
            println("Monkeys: $monkeys")
            println("Items: ${items.mapIndexed { index, i -> "$index = $i" }}")
        }
    }

    private fun lcm(n1: Int, n2: Int): Int {
        return if (n1 > n2) n1 else n2
    }

    private fun addMonkey(
        id: Int,
        items: List<Int>,
        operation: Operation,
        operationValue: Long,
        testDivisibleBy: Int,
        testSuccess: Int,
        testFail: Int
    ) {
        monkeys.add(Monkey(
            id = id,
            items = mutableListOf(*items.toTypedArray()),
            operation = operation,
            operationValue = operationValue,
            testDivisibleBy = testDivisibleBy,
            testSuccess = testSuccess,
            testFail = testFail
        ))
        interactions.add(0)
        modulo = if (modulo == 0) testDivisibleBy else modulo * testDivisibleBy
    }
}

enum class Operation(private val operator: Char) {
    Add('+'),
    Substract('-'),
    Divide('/'),
    Multiply('*');

    companion object {
        fun find(operand: Char): Operation {
            return values().first { operand == it.operator }
        }
    }
}

data class Monkey(
    val id: Int,
    val items: MutableList<Int>,
    val operation: Operation,
    val operationValue: Long,
    val testDivisibleBy: Int,
    val testSuccess: Int,
    val testFail: Int
)