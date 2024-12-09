import utils.*
import utils.navigation.Direction4
import utils.navigation.Rotation

fun main() {
    Day06 { WithSampleData }.test(41, 6)
    Day06 { WithInputData }.solve()
}

class Day06(dataType: () -> DataType) : Day("Guard Gallivant", dataType) {

    private val labPlan = input.toGrid().toField()
    private val guard = Guard(labPlan.search("^").single(), Direction4.North)
    private val lab = Lab(labPlan, guard)

    override fun part1(): Int {
        lab.simulateGuardsPath()
        return lab.pathSize
    }

    override fun part2() = lab.countGuardLoops()

    private class Lab(val labPlan: Field<String>, val guard: Guard) {
        private val path = mutableSetOf<Path>()
        private val obstacles = labPlan.search("#").toSet()

        val pathSize: Int
            get() = path.distinctBy { it.position }.size

        fun simulateGuardsPath(additionalObstacle: Position? = null): Boolean {
            path.add(Path(guard.position, guard.faceDirection))
            var guard = this.guard
            while (true) {
                val lookAhead = guard.look()
                if (lookAhead !in labPlan) break
                if (lookAhead in obstacles || lookAhead == additionalObstacle) {
                    guard = guard.turnRight()
                } else {
                    guard = guard.move()
                    val newPath = Path(guard.position, guard.faceDirection)
                    if (newPath in path) return true
                    path.add(newPath)
                }
            }
            return false
        }

        fun countGuardLoops(): Int {
            val possiblePositions = path.map { it.position }.toSet()
            return possiblePositions.count { possiblePosition ->
                path.clear()
                simulateGuardsPath(possiblePosition)
            }
        }
    }

    data class Path(val position: Position, val direction: Direction4)

    data class Guard(val position: Position, val faceDirection: Direction4) {
        fun move() = copy(position = look())
        fun turnRight() = copy(faceDirection = faceDirection.rotateBy(Rotation.Right))
        fun look() = position.doMovement(faceDirection)
    }
}           