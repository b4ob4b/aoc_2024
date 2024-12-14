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

    override fun part1(): Int {
        var robots = robots
        repeat(100) {
            robots = robots.map { it.move() }

        }

        val quadrants = field.createQuadrants()

        return quadrants.map { quadrant ->
            robots.count { it.position in quadrant }
        }.product()

    }

    override fun part2(): Int {
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


    private fun Field<String>.createQuadrants(): List<List<Position>> = with(this) {
        val x = numberOfX
        val y = numberOfY
        return listOf(
            createQuadrant(0 until x / 2, 0 until y / 2),
            createQuadrant(x / 2 until x, 0 until y / 2),
            createQuadrant(0 until x / 2, y / 2 until y),
            createQuadrant(x / 2 until x, y / 2 until y)
        )
    }

    private fun createQuadrant(xRange: IntRange, yRange: IntRange): List<Position> {
        return xRange.flatMap { x ->
            yRange.map { y ->
                Position(x, y)
            }
        }
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