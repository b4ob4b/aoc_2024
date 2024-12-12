import utils.*
import utils.navigation.Direction4

fun main() {
    Day12 { WithSampleData }.test(1930, 1206)
    Day12 { WithInputData }.solve()
}

class Day12(dataType: () -> DataType) : Day("Garden Groups", dataType) {

    private val field = input.toGrid().toField()

    override fun part1() = calculatePrice().first

    override fun part2() = calculatePrice().second

    private fun calculatePrice(): Pair<Int, Int> {
        val flowersToCheck = field.allPositions.toMutableSet()
        var totalPrice = 0
        var totalDiscountedPrice = 0

        while (flowersToCheck.isNotEmpty()) {
            val position = flowersToCheck.first()
            val flower = field[position]

            val (area, fullPerimeter, seen) = calculateAreaAndPerimeter(position, flower)
            flowersToCheck.removeAll(seen)

            totalPrice += area * fullPerimeter
            totalDiscountedPrice += area * findNumberOfSides(seen, flower)
        }

        return totalPrice to totalDiscountedPrice
    }

    private fun calculateAreaAndPerimeter(start: Position, flower: String): Triple<Int, Int, Set<Position>> {
        val flowersInGroup = mutableSetOf(start)
        val seen = mutableSetOf<Position>()
        var area = 0
        var fullPerimeter = 0

        while (flowersInGroup.isNotEmpty()) {
            val current = flowersInGroup.first()
            flowersInGroup.remove(current)
            area++
            seen.add(current)

            Direction4.entries.forEach { direction ->
                val neighbor = current.doMovement(direction)
                if (neighbor !in field || field[neighbor] != flower) {
                    fullPerimeter++
                } else if (neighbor !in seen) {
                    flowersInGroup.add(neighbor)
                }
            }
        }
        return Triple(area, fullPerimeter, seen)
    }


    private fun findNumberOfSides(seen: Set<Position>, flower: String): Int {
        val bitMasks = setOf(
            setOf(Position(0, 0)),
            setOf(Position(1, 0)),
            setOf(Position(0, 1)),
            setOf(Position(1, 1)),
            setOf(Position(0, 0), Position(1, 0), Position(0, 1)),
            setOf(Position(1, 0), Position(0, 1), Position(1, 1)),
            setOf(Position(0, 1), Position(1, 1), Position(0, 0)),
            setOf(Position(1, 1), Position(0, 0), Position(1, 0)),
        )
        val bitMasks2 = setOf(
            setOf(Position(0, 0), Position(1, 1)),
            setOf(Position(1, 0), Position(0, 1)),
        )

        val y0 = seen.minOf { it.y }
        val y1 = seen.maxOf { it.y }
        val x0 = seen.minOf { it.x }
        val x1 = seen.maxOf { it.x }

        val offset = Position(x0 - 1, y0 - 1)

        val transformedField = Field(x1 - x0 + 3, y1 - y0 + 3) { "." }
            .insertAt(seen.map { it - offset }.associateWith { flower })

        var smallPerimeter = 0

        (0 until (transformedField.numberOfY - 1)).forEach { y ->
            (0 until (transformedField.numberOfX - 1)).forEach { x ->
                val mask = setOf(
                    Position(x, y),
                    Position(x + 1, y),
                    Position(x, y + 1),
                    Position(x + 1, y + 1),
                )
                mask.filter { transformedField[it] == flower }.map { it - Position(x, y) }.toSet().let {
                    when (it) {
                        in bitMasks -> smallPerimeter++
                        in bitMasks2 -> smallPerimeter += 2
                    }
                }
            }
        }

        return smallPerimeter
    }
}