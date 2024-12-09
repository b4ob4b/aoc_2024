import utils.*

fun main() {
    Day08 { WithSampleData }.test(14, 34)
    Day08 { WithInputData }.solve()
}

class Day08(dataType: () -> DataType) : Day("Resonant Collinearity", dataType) {

    private val field = input.toGrid().toField()
    private val antennas = field
        .search("[a-zA-Z0-9]".toRegex())
        .associateWith { field[it] }
        .entries
        .groupBy({ it.value }, { it.key })

    override fun part1() = antennas.calculateNumberOfAntinodes(1..1)

    override fun part2() = antennas.calculateNumberOfAntinodes(0..field.numberOfX)

    private fun Map<String, List<Position>>.calculateNumberOfAntinodes(sendingRange: IntRange) =
        flatMap { (_, positions) -> positions.findAntinodes(sendingRange) }
            .toSet()
            .count { it in field }

    private fun List<Position>.findAntinodes(sendingRange: IntRange) =
        combinations(2)
            .flatMap { (a, b) ->
                (sendingRange).flatMap { d ->
                    listOf(a + d * (a - b), b - d * (a - b))
                }
            }

    private fun Field<String>.search(regex: Regex) =
        sequence {
            (0 until numberOfY).flatMap { y ->
                (0 until numberOfX).map { x ->
                    if (field[y][x].matches(regex)) yield(Position(x, y))
                }
            }
        }
}           