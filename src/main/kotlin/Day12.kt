import utils.*

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
        val flowersToProcess = ArrayDeque<Position>().apply { add(start) }
        val seen = mutableSetOf<Position>()
        var area = 0
        var fullPerimeter = 0

        while (flowersToProcess.isNotEmpty()) {
            val current = flowersToProcess.removeFirst()
            if (current in seen) continue
            area++
            seen.add(current)

            current.get4Neighbors().forEach { neighbor ->
                if (neighbor !in field || field[neighbor] != flower) {
                    fullPerimeter++
                } else if (neighbor !in seen) {
                    flowersToProcess.add(neighbor)
                }
            }
        }
        return Triple(area, fullPerimeter, seen)
    }


    private fun findNumberOfSides(flowerGroup: Set<Position>, flower: String): Int {
        val bitMask2x2 = setOf(
            Position(0, 0),
            Position(1, 0),
            Position(0, 1),
            Position(1, 1),
        )
        val oneEdgeBitMasks = setOf(
            setOf(Position(0, 0)),
            setOf(Position(1, 0)),
            setOf(Position(0, 1)),
            setOf(Position(1, 1)),
            setOf(Position(0, 0), Position(1, 0), Position(0, 1)),
            setOf(Position(1, 0), Position(0, 1), Position(1, 1)),
            setOf(Position(0, 1), Position(1, 1), Position(0, 0)),
            setOf(Position(1, 1), Position(0, 0), Position(1, 0)),
        )
        val twoEdgeBitMasks = setOf(
            setOf(Position(0, 0), Position(1, 1)),
            setOf(Position(1, 0), Position(0, 1)),
        )

        val y0 = flowerGroup.minOf { it.y }
        val y1 = flowerGroup.maxOf { it.y }
        val x0 = flowerGroup.minOf { it.x }
        val x1 = flowerGroup.maxOf { it.x }

        val offset = Position(x0 - 1, y0 - 1)

        val transformedField = Field(x1 - x0 + 3, y1 - y0 + 3) { "." }
            .insertAt(flowerGroup.map { it - offset }.associateWith { flower })

        var sides = 0

        (0 until (transformedField.height - 1)).forEach { y ->
            (0 until (transformedField.width - 1)).forEach { x ->
                bitMask2x2
                    .map { it + Position(x, y) }
                    .filter { transformedField[it] == flower }
                    .map { it - Position(x, y) }
                    .toSet()
                    .let {
                        when (it) {
                            in oneEdgeBitMasks -> sides++
                            in twoEdgeBitMasks -> sides += 2
                        }
                    }
            }
        }

        return sides
    }
}