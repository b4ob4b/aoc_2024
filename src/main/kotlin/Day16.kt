import utils.*
import utils.navigation.Direction4
import utils.navigation.Rotation

fun main() {
    Day16 { WithSampleData }.test(7036, 45)
    Day16 { WithInputData }.solve()
}

class Day16(dataType: () -> DataType) : Day("Reindeer Maze", dataType) {

    private val maze = input.toGrid().toField()
    private val wall = "#"
    private val start = "S"
    private val end = "E"
    private var cheapestPathCost = -1
    private val startPosition = maze.search(start).single()

    private data class Step(
        val position: Position,
        val direction4: Direction4,
        val price: Int,
        val path: List<Position> = emptyList()
    )

    override fun part1(): Int {
        val steps = mutableSetOf(Step(startPosition, Direction4.East, 0))
        val seen = mutableSetOf<Pair<Position, Direction4>>()

        while (true) {
            val step = steps.minByOrNull { it.price } ?: break
            steps.remove(step)
            if (!seen.add(step.position to step.direction4)) continue

            if (maze[step.position] == end) {
                cheapestPathCost = step.price
                return step.price
            }
            steps.checkNext(step)
        }
        throw Exception("No path found")
    }

    override fun part2(): Int {
        val steps = mutableListOf(Step(startPosition, Direction4.East, 0, listOf(startPosition)))
        val tilesVisited = mutableMapOf<Pair<Position, Direction4>, Int>()
        val tilesOptimalPath = mutableSetOf<Position>()

        while (true) {
            val step = steps.minByOrNull { it.price } ?: break
            steps.remove(step)

            if (step.price > cheapestPathCost) continue

            val pair = step.position to step.direction4
            if (step.price <= tilesVisited.getOrDefault(pair, Int.MAX_VALUE)) {
                tilesVisited[pair] = step.price
            } else {
                continue
            }

            if (maze[step.position] == end) {
                tilesOptimalPath.addAll(step.path)
                continue
            }

            steps.checkNext(step)
        }
        return tilesOptimalPath.size
    }

    private fun MutableCollection<Step>.checkNext(step: Step) {
        Rotation.entries.map { step.direction4.rotateBy(it) }
            .map { step.position.doMovement(it) to it }
            .filter { maze[it.first] != wall && it.first !in step.path }
            .forEach {
                this.add(Step(it.first, it.second, step.price + 1001, step.path + it.first))
            }
        val next = step.position.doMovement(step.direction4)
        if (maze[next] != wall && next !in step.path) {
            this.add(Step(next, step.direction4, step.price + 1, step.path + next))
        }
    }
}           