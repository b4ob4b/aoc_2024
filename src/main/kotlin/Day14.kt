import utils.*

fun main() {
    Day14 { WithSampleData }.test(12)
    Day14 { WithInputData }.solve()
}

class Day14(dataType: () -> DataType) : Day("Restroom Redoubt", dataType) {

    private val robots = input.splitLines()
        .map {
            val (x, y, vx, vy) = it.extractInts()
            Robot(Position(x, y), Position(vx, vy))
        }
    private val field = when {
        isTest -> Field(11, 7) { "." }
        else -> Field(101, 103) { "." }
    }


    private data class Robot(val position: Position, val velocity: Position) {}

    private fun Robot.move(): Robot {
        val newPosition = with(field) {
            (position + velocity).let { (x, y) ->
                val newX = (x % numberOfX).let { xx -> if (xx < 0) xx + numberOfX else xx }
                val newY = (y % numberOfY).let { yy -> if (yy < 0) yy + numberOfY else yy }
                Position(newX, newY)
            }
        }
        return copy(position = newPosition)
    }

    override fun part1(): Any? {
        var robots = robots
        repeat(100) {
            robots = robots.map { it.move() }

        }

        val q1 = (0..<field.numberOfX / 2).flatMap { x ->
            (0..<field.numberOfY / 2).map { y ->
                Position(x, y)
            }
        }

        val q2 = (field.numberOfX / 2 + 1 until field.numberOfX).flatMap { x ->
            (0..<field.numberOfY / 2).map { y ->
                Position(x, y)
            }
        }

        val q3 = (0..<field.numberOfX / 2).flatMap { x ->
            (field.numberOfY / 2 + 1 until field.numberOfY).map { y ->
                Position(x, y)
            }
        }

        val q4 = (field.numberOfX / 2 + 1 until field.numberOfX).flatMap { x ->
            (field.numberOfY / 2 + 1 until field.numberOfY).map { y ->
                Position(x, y)
            }
        }
        return listOf(
            robots.count { it.position in q1 },
            robots.count { it.position in q2 },
            robots.count { it.position in q3 },
            robots.count { it.position in q4 },
        ).product()
    }

    override fun part2(): Any? {
        var robots = robots

        val areaToSearchXmasTree = field
            .mapToList { position, _ -> position }
            .filter { it.x in xRangeXmasTree && it.y in yRangeXmasTree }


        val minimumRequiredRobotDensity = 300
        var density = 0
        var time = 0
        while (density < minimumRequiredRobotDensity) {
            time++
            robots = robots.map { it.move() }
            density = robots.count { it.position in areaToSearchXmasTree }
        }

        field.printTree(robots)
        return time
    }

    private fun Field<String>.printTree(robots: List<Robot>) =
        insertAt(robots.associate { it.position to "#" })
            .slice(xRangeXmasTree, yRangeXmasTree)
            .field
            .toField()
            .print(true)

    private val xRangeXmasTree = (field.numberOfX / 2 - 5)..(field.numberOfX / 2 + 27)
    private val yRangeXmasTree = (field.numberOfY / 2 - 19)..(field.numberOfY / 2 + 15)
}           