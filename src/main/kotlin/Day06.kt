import utils.*
import utils.navigation.Direction4
import utils.navigation.Rotation

fun main() {
    Day06(IO.TYPE.SAMPLE).test(41, 6)
    Day06().solve()
}

class Day06(inputType: IO.TYPE = IO.TYPE.INPUT) : Day("", inputType = inputType) {

    private val field = input.toGrid().toField()
    private val path = mutableSetOf<Position>()

    data class Guard(val position: Position, val faceDirection: Direction4) {
        fun move() = copy(position = position.doMovement(faceDirection))
        fun turnRight() = copy(faceDirection = faceDirection.rotateBy(Rotation.Right))
        fun look() = position.doMovement(faceDirection)
    }

    override fun part1(): Int {
        val startPosition = field.search("^").single()
        val guard = Guard(startPosition, Direction4.North)
        var newGuard = guard
        path.add(guard.position)
        while (true) {
            val newPosition = newGuard.look()
            if (newPosition in field) {
                if (field[newPosition] == "#") {
                    newGuard = newGuard.turnRight()
                } else {
                    newGuard = newGuard.move()
                    path.add(newGuard.position)
                }
            } else {
                break
            }
        }

        return path.size
    }

    override fun part2(): Any? {
        val maxSizeOfPath = path.size

        return path.count { additionalObjectPosition ->
            val startPosition = field.search("^").single()
            val guard = Guard(startPosition, Direction4.North)
            var newGuard = guard
            val newPath = mutableSetOf(guard.position)
            var steps = 0
            var isInLoop = false
            while (steps < maxSizeOfPath * 2) {
                val newPosition = newGuard.look()
                if (steps == (maxSizeOfPath * 2 - 1)) {
                    isInLoop = true
                }
                if (newPosition in field) {
                    if (field[newPosition] == "#" || newPosition == additionalObjectPosition) {
                        newGuard = newGuard.turnRight()
                    } else {
                        newGuard = newGuard.move()
                        steps += 1
                        newPath.add(newGuard.position)
                    }
                } else {
                    break
                }
            }
            isInLoop
        }
    }
}           