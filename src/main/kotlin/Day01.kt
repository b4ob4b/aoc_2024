import utils.*
import kotlin.math.abs

fun main() {
    Day01(IO.TYPE.SAMPLE).test(11,31)
    Day01().solve()
}

class Day01(inputType: IO.TYPE = IO.TYPE.INPUT) : Day("Historian Hysteria", inputType = inputType) {

    private val data = input
        .splitLines()
        .map { it.extractInts() }
        .map { it[0] to it[1] }
        .unzip()

    override fun part1(): Int {
        val list1 = data.first.sorted()
        val list2 = data.second.sorted()

        return list1.zip(list2).sumOf { abs(it.second - it.first) }
    }

    override fun part2(): Int {
        val list1 = data.first
        val list2 = data.second

        return list1.sumOf { number ->
            val factor = list2.count { it == number }
            number * factor
        }
    }
}           