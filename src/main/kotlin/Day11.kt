import utils.*

fun main() {
    Day11 { WithSampleData }.test(55312L)
    Day11 { WithInputData }.solve()
}

class Day11(dataType: () -> DataType) : Day("", dataType) {

    private val stones = input.extractInts().map { Stone(it.toLong()) }

    data class Stone(val value: Long) {
        fun blink(): List<Stone> {
            return when {
                value == 0L -> listOf(Stone(1L))

                value.toString().length % 2 == 0 -> value.toString()
                    .chunked(value.toString().length / 2)
                    .map { Stone(it.toLong()) }

                else -> listOf(Stone(value * 2024L))
            }
        }
    }

    override fun part1() = stones.blink(25)

    override fun part2() = stones.blink(75)

    private fun List<Stone>.blink(times: Int): Long {
        var stones = this.groupingBy { it }.eachCount().mapValues { it.value.toLong() }
        repeat(times) {
            stones = stones.entries.map { (stone, amount) ->
                stone.blink()
                    .groupingBy { it.value }
                    .eachCount()
                    .mapValues { it.value * amount }
            }.merge().mapKeys { Stone(it.key) }
        }
        return stones.map { it.value }.sum()
    }


    private fun List<Map<Long, Long>>.merge(): Map<Long, Long> {
        return this.flatMap { it.entries }
            .groupingBy { it.key }
            .fold(0L) { acc, entry -> acc + entry.value }
    }
}           