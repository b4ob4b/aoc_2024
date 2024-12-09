import utils.*

fun main() {
    Day05 { WithSampleData }.test(143, 123)
    Day05 { WithInputData }.solve()
}

class Day05(dataType: () -> DataType) : Day("Print Queue", dataType) {

    private val data = input.split("\\n\\n".toRegex())
    private val rules = data[0].splitLines()
        .map { it.split("|").map(String::toInt) }
        .map { (a, b) -> Pair(a, b) }
    private val updates = data[1].splitLines().map(String::extractInts)

    override fun part1(): Int = updates.sumOf {
        it.takeIf { it.isValid() }?.getMiddlePage() ?: 0
    }

    override fun part2() = updates.filter { !it.isValid() }.sumOf { it.reorderUpdate().getMiddlePage() }

    private fun List<Int>.isValid() = withIndex().all { (index, page) ->
        page.getInvalidPageBefore().none { it in take(index + 1) }
    }

    private fun List<Int>.reorderUpdate(): List<Int> {
        val pages = this.toMutableList()
        while (!pages.isValid()) {
            pages.indices.forEach { pageIndex ->
                pages
                    .take(pageIndex)
                    .firstOrNull { it in pages[pageIndex].getInvalidPageBefore() }
                    ?.also {
                        pages.swap(pageIndex, pages.indexOf(it))
                    }
            }
        }
        return pages
    }

    private fun List<Int>.getMiddlePage() = this[this.size / 2]

    private fun MutableList<Int>.swap(index1: Int, index2: Int) {
        this[index1] = this[index2].also { this[index2] = this[index1] }
    }

    private fun Int.getInvalidPageBefore() = rules.filter { it.first == this }.map { it.second }
}