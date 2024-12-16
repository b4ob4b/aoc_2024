import utils.*
import utils.navigation.Direction4
import utils.navigation.Rotation

fun main() {
    Day16 { WithSampleData }.test(7036)
    Day16 { WithInputData }.solve()
}

class Day16(dataType: () -> DataType) : Day("Reindeer Maze", dataType) {

    private val maze = input.toGrid().toField()
    private val wall = "#"
    private val start = "S"
    private val end = "E"

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

    private data class Step(val position: Position, val direction4: Direction4, val price: Int)

    override fun part2(): Any? {
        return "not yet implement"
    }
}           