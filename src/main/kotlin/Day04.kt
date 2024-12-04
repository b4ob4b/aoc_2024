import utils.*

fun main() {
    Day04(IO.TYPE.SAMPLE).test(18, 9)
    Day04().solve()
}

class Day04(inputType: IO.TYPE = IO.TYPE.INPUT) : Day("Ceres Search", inputType = inputType) {

    private val wordPuzzle = input.splitLines().map { it.split("") }.toField()

    override fun part1(): Int {
        val directions = Position.origin.get8Neighbours()
        return wordPuzzle.search("X")
            .sumOf { positionOfX ->
                val words = directions.map { positionOfX.wordInDirection(it) }
                words.count { it == "XMAS" }
            }
    }

    override fun part2(): Int {
        return wordPuzzle.search("A").count { positionOfA ->
            val words = positionOfA.wordsInDiagonals()
            words?.all { setOf("MAS", "SAM").contains(it) } ?: false
        }
    }

    private fun Position.wordInDirection(direction: Position) =
        (this + 3 * direction)
            .takeIf { it in wordPuzzle }
            ?.let { (0..3).joinToString("") { wordPuzzle[this + it * direction] } }
            ?: ""

    private fun Position.wordsInDiagonals(): List<String>? {
        val diagonal1 = listOf(
            Position(-1, -1),
            Position.origin,
            Position(1, 1),
        )
        val diagonal2 = listOf(
            Position(1, -1),
            Position.origin,
            Position(-1, 1),
        )
        return listOf(diagonal1, diagonal2)
            .map { diagonal -> diagonal.map { it + this } }
            .takeIf { positions -> positions.flatten().all { it in wordPuzzle } }
            ?.map { letters -> letters.joinToString("") { wordPuzzle[it] } }
    }
}           