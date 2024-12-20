import utils.*
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.collections.List

fun main() {
    Day20 { WithSampleData }.test()
    Day20 { WithInputData }.solve()
}

class Day20(dataType: () -> DataType) : Day("", dataType) {

    private val maze = input.toGrid().toField()
    private val wall = "#"
    private val start = "S"
    private val end = "E"
    private val raceTrack = "."
    private val startPosition = maze.search(start).single()

    override fun part1(): Any? {
        val cheatPositionsList = mutableSetOf<List<Position>>()

        maze.search(raceTrack).forEach { position ->
            val queue = ArrayDeque<MutableList<Position>>()
            queue.add(mutableListOf(position))
            val visited = mutableSetOf<List<Position>>()
            while (queue.isNotEmpty()) {
                val current = queue.removeFirst()
                if (current.size == 3) continue
                if (!visited.add(current)) continue
                current.last().get4Neighbors().filter { neighbor ->
                    maze[neighbor] == wall && neighbor.x != 0 && neighbor.y != 0 && neighbor.x != maze.width - 1 && neighbor.y != maze.height - 1
                }.forEach { neighbor ->
                    queue.add((current + neighbor).toMutableList())
                }
            }

            visited
                .filter { it.isNotEmpty() }
                .forEach { list ->
                    cheatPositionsList.add(list.filter { maze[it] == wall })
                }
        }

        val costWithoutCheat = runMaze(emptyList())

        return cheatPositionsList
            .map { cheatPositions -> runMaze(cheatPositions) }
            .map { costWithoutCheat - it }
            .filter { it >= 100 }
            .size
    }

    private fun runMaze(cheatPositions: List<Position>): Int {
        val queue = PriorityQueue<Pair<Position, Int>>(compareBy { it.second })
        val visited = mutableSetOf<Position>()
        queue.add(startPosition to 0)

        while (queue.isNotEmpty()) {
            val (position, cost) = queue.poll()

            if (!visited.add(position)) continue

            if (maze[position] == end) {
                return cost
            }
            position.get4Neighbors().forEach { neighbor ->
                if (maze[neighbor] != wall || neighbor in cheatPositions) {
                    queue.add(neighbor to cost + 1)
                }
            }
        }
        throw Exception("No path found")
    }

    override fun part2(): Any? {
        return "not yet implement"
    }
}           