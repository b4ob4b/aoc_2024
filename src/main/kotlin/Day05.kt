import utils.*

fun main() {
    Day05(IO.TYPE.SAMPLE).test(143, 123)
    Day05().solve()
}

class Day05(inputType: IO.TYPE = IO.TYPE.INPUT) : Day("Print Queue", inputType = inputType) {

    private val data = input.split("\\n\\n".toRegex())
    private val rules = data[0].splitLines()
        .map { it.split("|").map(String::toInt) }
        .map { (a, b) -> Pair(a, b) }
    private val updates = data[1].splitLines().map(String::extractInts)

    override fun part1(): Int = updates.sumOf {
        it.takeIf { it.isValid() }?.getMiddlePage() ?: 0
    }

    override fun part2() = updates.filter { !it.isValid() }.sumOf { it.reorderUpdate().getMiddlePage() }

    private fun List<Int>.isValid() = withIndex().all { (index, r) ->
        rules.filter { it.first == r }
            .map { it.second }
            .none { it in take(index + 1) }
    }

    private fun List<Int>.reorderUpdate(): List<Int> {
        val orderedUpdate = this.toMutableList()
        while (!orderedUpdate.isValid()) {
            orderedUpdate.indices.forEach { i ->
                val currentNumber = orderedUpdate[i]
                val forbiddenNumbersBefore = rules.filter { it.first == currentNumber }.map { it.second }
                val numberToSwap = orderedUpdate.take(i).firstOrNull { it in forbiddenNumbersBefore }
                if (numberToSwap != null) {
                    orderedUpdate.swap(i, orderedUpdate.indexOf(numberToSwap))
                }
            }
        }
        return orderedUpdate
    }

    private fun List<Int>.getMiddlePage() = this[this.size / 2]

    private fun MutableList<Int>.swap(index1: Int, index2: Int) {
        this[index1] = this[index2].also { this[index2] = this[index1] }
    }
}