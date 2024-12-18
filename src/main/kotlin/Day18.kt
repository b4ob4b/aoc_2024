import utils.*
import java.util.*

fun main() {
    Day18(mapOf("fieldSize" to 7, "bitSize" to 12)) { WithSampleData }.test(22, "6,1")
    Day18(mapOf("fieldSize" to 71, "bitSize" to 1024)) { WithInputData }.solve()
}

class Day18(private val dayProperty: Map<String, Int> = emptyMap(), dataType: () -> DataType) :
    Day("RAM Run", dataType) {

    private val coordinates = input.splitLines()
        .map {
            val (x, y) = it.extractInts()
            Position(x, y)
        }

    override fun part1() = findPath(coordinates.take(dayProperty["bitSize"] ?: 0).toSet())

    override fun part2() = coordinates.indices.takeWhile {
        findPath(coordinates.take(it).toSet()) != null
    }
        .last()
        .let {
            val coordinate = coordinates[it]
            "${coordinate.x},${coordinate.y}"
        }

    private fun findPath(coordinates: Set<Position>): Int? {
        val wall = "#"
        val start = Position.origin
        val fieldSize = dayProperty["fieldSize"] ?: 0
        val field = Field(fieldSize, fieldSize) { "." }
            .insertAt(coordinates.associateWith { wall })
        val end = Position(field.width - 1, field.height - 1)

        val queue = PriorityQueue<Pair<Position, Int>>(compareBy { it.second })
        queue.add(start to 0)
        val seen = mutableSetOf<Position>()

        while (queue.isNotEmpty()) {
            val (position, steps) = queue.poll()
            if (!seen.add(position)) continue

            if (position == end) {
                return steps
            }

            position.get4Neighbors()
                .filter { it in field && field[it] != wall }
                .forEach {
                    queue.add(it to steps + 1)
                }
        }
        return null
    }
}