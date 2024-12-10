import utils.*
import utils.navigation.Direction4

fun main() {
    Day10 { WithSampleData }.test(36, 81)
    Day10 { WithInputData }.solve()
}

class Day10(dataType: () -> DataType) : Day("Hoof It", dataType) {

    private val hill = input.toGrid { it.toInt() }.toField()
    private val paths
        get() = hill
            .search(0)
            .flatMap {
                Step(it, it, 0).walk()
            }

    override fun part1() = paths.countUnique()

    override fun part2() = paths.count()

    private fun Sequence<Step>.countUnique() = map { it.position to it.origin }.toSet().size

    private data class Step(
        val origin: Position,
        val position: Position,
        val height: Int,
    )

    private fun Step.walk(): Sequence<Step> = sequence {
        if (height == 9) yield(this@walk)
        Direction4.entries
            .map { direction -> position.doMovement(direction) }
            .filter { it in hill && hill[it] == height + 1 }
            .forEach { nextPosition ->
                yieldAll(Step(origin, nextPosition, height + 1).walk())
            }
    }
}