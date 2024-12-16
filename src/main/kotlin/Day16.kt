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

    override fun part1(): Int {
        val start = maze.search(start).single()

        val path = mutableSetOf(Step(start, Direction4.East, 0))
        val seen = mutableSetOf<Pair<Position, Direction4>>()

        while (true) {
            val current = path.minByOrNull { it.price } ?: break
            path.remove(current)
            if (current.position to current.direction4 in seen) continue
            seen.add(current.position to current.direction4)

            if (maze[current.position] == end) {
                cheapestPathCost = current.price
                return current.price
            }

            Rotation.entries.map { current.direction4.rotateBy(it) }
                .map { current.position.doMovement(it) to it }
                .filter { maze[it.first] != wall && it !in seen }
                .forEach {
                    path.add(Step(it.first, it.second, current.price + 1001))
                }
            val next = current.position.doMovement(current.direction4)
            if (maze[next] != wall && next to current.direction4 !in seen) {
                path.add(Step(next, current.direction4, current.price + 1))
            }
        }
        throw Exception("No path found")
    }

    private data class Step(
        val position: Position,
        val direction4: Direction4,
        val price: Int,
        val path: List<Position> = emptyList()
    )

    override fun part2(): Int {
        val start = maze.search(start).single()

        val path = mutableListOf(Step(start, Direction4.East, 0, listOf(start)))
        val seen = mutableSetOf<List<Position>>()
        val tilesSeen = mutableSetOf<Position>()
        val tiles2 = mutableMapOf<Pair<Position, Direction4>, Int>()

        while (true) {
            val current = path.minByOrNull { it.price } ?: break
            path.remove(current)

            if (current.price > cheapestPathCost) continue
            if (current.path in seen) continue

            val pair = current.position to current.direction4
            if (pair !in tiles2) {
                tiles2[pair] = current.price
            } else {
                if (current.price <= tiles2[pair]!!) {
                    tiles2[pair] = current.price
                } else {
                    continue
                }
            }

            seen.add(current.path)

            if (maze[current.position] == end) {
                tilesSeen.addAll(current.path)
                continue
            }

            Rotation.entries.map { current.direction4.rotateBy(it) }
                .map { current.position.doMovement(it) to it }
                .filter { maze[it.first] != wall }
                .filter { it.first !in current.path }
                .forEach {
                    path.add(Step(it.first, it.second, current.price + 1001, current.path + it.first))
                }
            val next = current.position.doMovement(current.direction4)
            if (maze[next] != wall && next !in current.path) {
                path.add(Step(next, current.direction4, current.price + 1, current.path + next))
            }
        }
        return tilesSeen.size
    }
}           