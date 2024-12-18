import utils.*
import java.util.*

fun main() {
    Day18 { WithSampleData }.test(22)
    Day18 { WithInputData }.solve()
}

class Day18(dataType: () -> DataType) : Day("RAM Run", dataType) {

    private val size = if (isTest) 7 else 71
    private val coordinates = input.splitLines()
        .map {
            val (x, y) = it.extractInts()
            Position(x, y)
        }.let {
            if (isTest) {
                it.take(12)
            } else it.take(1024)
        }


    override fun part1(): Any? {

        val start = Position.origin
        val end = Position(size - 1, size - 1)
        val field = Field(size, size) { "." }
            .insertAt(coordinates.associateWith { "#" })

        val queue = PriorityQueue<Pair<Position, Int>>(compareBy { it.second })
        queue.add(start to 0)
        val seen = mutableSetOf<Position>()

        while (queue.isNotEmpty()) {
            val (position, steps) = queue.poll()
            if (!seen.add(position)) continue

            if (position == end) {
                return steps
            }

            val neighbors = position.get4Neighbors()
                .filter { it in field }
            for (neighbor in neighbors) {
                if (field[neighbor] == "#") continue
                queue.add(neighbor to steps + 1)
            }
        }
        throw Exception("No path found")
    }

    override fun part2(): Any? {
        return "not yet implement"
    }
}           