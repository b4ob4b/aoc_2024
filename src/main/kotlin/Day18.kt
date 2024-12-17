import utils.*

fun main() {
    Day18 { WithSampleData }.test()
    Day18 { WithInputData }.solve()
}

class Day18(dataType: () -> DataType) : Day("", dataType) {

    private val data = input.splitLines()
    
    override fun part1(): Any? {
        data
            .print()
        return "not yet implement"
    }

    override fun part2(): Any? {
        return "not yet implement"
    }
}           