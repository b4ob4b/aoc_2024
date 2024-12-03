import utils.*

fun main() {
    Day03(IO.TYPE.SAMPLE).test(161)
    Day03(IO.TYPE.SAMPLE2).test(part2 = 48)
    Day03().solve()
}

class Day03(inputType: IO.TYPE = IO.TYPE.INPUT) : Day("Mull It Over", inputType = inputType) {

    private val memory = input.replace("\n", "")

    override fun part1(): Int = memory.parseMultiplications().sum()

    override fun part2(): Int {
        val pattern = "don't\\(\\).*?do\\(\\)".toRegex()
        return memory
            .replace(pattern, "")
            .parseMultiplications()
            .sum()
    }

    private fun String.parseMultiplications(): List<Int> {
        val pattern = "mul\\(\\d+,\\d+\\)".toRegex()
        val matches = pattern.findAll(this)
        return matches.toList().map { match ->
            match.value.extractInts()
                .product()
        }
    }
}           