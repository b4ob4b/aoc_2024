import utils.*
import utils.navigation.Direction4

fun main() {
    Day10 { WithSampleData }.test(36, 81)
    Day10 { WithInputData }.solve()
}

class Day10(dataType: () -> DataType) : Day("Hoof It", dataType) {

    private val field = input.toGrid { it.toInt() }.toField()
    private val paths
        get() = field
            .search(0)
            .flatMap {
                Step(it, it, 0).walk()
            }

    override fun part1() = paths.countUnique()

    override fun part2() = paths.count()

    data class Step(
        val origin: Position,
        val position: Position,
        val height: Int,
    )

    private fun Sequence<Step>.countUnique() =
        map { it.position to it.origin }
            .toSet()
            .size

    private fun Step.walk(): List<Step> {
        if (height == 9) return listOf(this)

        val nextPositions = Direction4.entries.map { direction ->
            position.doMovement(direction)
        }.filter {
            it in field && field[it] == height + 1
        }

        if (nextPositions.isEmpty()) return emptyList()

        return nextPositions.flatMap { nextPosition ->
            Step(origin, nextPosition, height + 1).walk()
        }
    }
}