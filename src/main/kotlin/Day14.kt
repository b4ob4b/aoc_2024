import utils.*
import kotlin.math.pow
import kotlin.math.sqrt

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

    private val width = if (isTest) 11 else 101
    private val height = if (isTest) 7 else 103

    private data class Robot(val position: Position, val velocity: Position) {}

    private fun Robot.move(): Robot {
        val newPosition = (position + velocity).let { (x, y) ->
            val newX = (x % width).let { xx -> if (xx < 0) xx + width else xx }
            val newY = (y % height).let { yy -> if (yy < 0) yy + height else yy }
            Position(newX, newY)
        }
        return copy(position = newPosition)
    }

    override fun part1(): Int {
        var robots = robots
        repeat(100) {
            robots = robots.map { it.move() }
        }

        val quadrants = listOf(
            createQuadrant(0 until width / 2, 0 until height / 2),
            createQuadrant(width / 2 + 1 until width, 0 until height / 2),
            createQuadrant(0 until width / 2, height / 2 + 1 until height),
            createQuadrant(width / 2 + 1 until width, height / 2 + 1 until height)
        )

        return quadrants.map { quadrant ->
            robots.count { it.position in quadrant }
        }.product()

    }

    override fun part2(): Int {
        var robots = robots

        val defaultStandardDeviation = robots.map { it.position }.standardDeviation()
        var time = 0
        var standardDeviation = defaultStandardDeviation
        while ((standardDeviation / defaultStandardDeviation) > 0.75) {
            time++
            robots = robots.map { it.move() }
            standardDeviation = robots.map { it.position }.standardDeviation()
        }

        return time
    }

    private fun createQuadrant(xRange: IntRange, yRange: IntRange): List<Position> {
        return xRange.flatMap { x ->
            yRange.map { y ->
                Position(x, y)
            }
        }
    }


    private fun List<Position>.center() = Position(
        x = sumOf { it.x } / size,
        y = sumOf { it.y } / size
    )

    private fun List<Position>.variance() = sumOf { (it - center()).distance() * (it - center()).distance() }
    private fun List<Position>.standardDeviation() = sqrt(variance())
    private fun Position.distance() = sqrt(x.toDouble().pow(2) + y.toDouble().pow(2))

}           