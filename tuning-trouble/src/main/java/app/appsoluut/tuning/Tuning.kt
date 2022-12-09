package app.appsoluut.tuning

import java.io.File

class Tuning constructor(
    private val verbose: Boolean,
    private val source: String
) {

    fun search(packetSize: Int) {
        println("-- Search for marker position with packet size $packetSize --")
        val position = findMarker(File(source), packetSize = packetSize)

        if (position != -1) {
            println("- Marker found at position $position.")
        } else {
            println("- [error] No marker was found!")
        }
    }

    private fun findMarker(source: File, packetSize: Int): Int {
        if (verbose) {
            println("-- Parsing file $source --")
        }

        var offset = 1

        val token = mutableListOf<Char>()

        val reader = source.reader()
        var byte = reader.read()
        while(byte != -1) {
            token.add(byte.toChar())

            if (token.size > packetSize) {
                token.removeFirstOrNull()
            }

            if (token.size == packetSize && !hasDuplicates(token)) {
                return offset
            }

            offset++
            byte = reader.read()
        }
        return -1
    }

    private fun hasDuplicates(list: List<Char>): Boolean {
        list.fold(mutableSetOf<Char>()) { seen, element ->
                if (!seen.add(element)) {
                    return true
                }
                seen
            }
        return false
    }
}
