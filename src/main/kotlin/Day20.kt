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

    private data class Step(val position: Position, val cost: Int)

    override fun part2(): Int {
        val endPosition = maze.search(end).single()
        val fromStart = bfs(startPosition, endPosition)
        val fromEnd = bfs(endPosition, startPosition)

        val costWithoutCheat = fromStart.single { it.position == endPosition }.cost

        return fromStart.asSequence().flatMap { source ->
            fromEnd.map { target ->
                if (source.position == target.position) return@map null

                val distance = (target.position - source.position).manhattenDistance
                if (distance <= 20) {
                    source.cost + target.cost + distance
                } else null
            }
        }.filterNotNull()
            .filter { it < costWithoutCheat }
            .map { costWithoutCheat - it }
            .count { it >= 100 }
    }

    private fun bfs(startPosition: Position, endPosition: Position): Set<Step> {
        val queue = ArrayDeque<Step>()
        val visited = mutableSetOf<Step>()
        queue.add(Step(startPosition, 0))

        while (queue.isNotEmpty()) {
            val (position, cost) = queue.removeFirst()

            if (!visited.add(Step(position, cost))) continue

            if (position == endPosition) {
                break
            }
            position.get4Neighbors().forEach { neighbor ->
                if (maze[neighbor] != wall && neighbor !in visited.map { it.position }) {
                    queue.add(Step(neighbor, cost + 1))
                }
            }
        }
        return visited.toSet()
    }
}           