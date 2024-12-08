import utils.*

fun main() {
    Day08(IO.TYPE.SAMPLE).test(14, 34)
    Day08().solve()
}

class Day08(inputType: IO.TYPE = IO.TYPE.INPUT) : Day("Resonant Collinearity", inputType = inputType) {

    private val field = input.toGrid().toField()
    private val senderTypes = field.search("[a-zA-Z0-9]".toRegex())
    private val senderPositions = senderTypes.map { field[it] }.toSet()

    override fun part1() = calculateNumberOfAntinodes(1..1)

    override fun part2() = calculateNumberOfAntinodes(0..field.numberOfX)

    private fun calculateNumberOfAntinodes(sendingRange: IntRange) =
        senderPositions
            .flatMap { sender ->
                field.search(sender).findAntinodes(sendingRange)
            }
            .toSet()
            .count { it in field }

    private fun Sequence<Position>.findAntinodes(sendingRange: IntRange) =
        toSet()
            .combinations(2)
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