import utils.*

fun main() {
    Day07(IO.TYPE.SAMPLE).test(3749L, 11387L)
    Day07().solve()
}

class Day07(inputType: IO.TYPE = IO.TYPE.INPUT) : Day("", inputType = inputType) {

    private val equations = input.splitLines().map { line ->
        val (left, right) = line.split(": ")
        Equation(left.toLong(), right.extractInts().map { it.toLong() })
    }

    private data class Equation(val left: Long, val right: List<Long>) {
        fun solve(p2: Boolean = false): List<Equation> {
            if (right.size == 1) return listOf(this)
            val (r0, r1) = right.take(2)
            val rest = right.drop(2)

            return listOf(
                Equation(left, listOf(r0 + r1) + rest).solve(p2),
                Equation(left, listOf(r0 * r1) + rest).solve(p2),
                if (p2) Equation(left, concatenate(r0, r1) + rest).solve(p2) else emptyList()
            ).flatten()
        }

        private fun concatenate(r0: Long, r1: Long): List<Long> {
            return listOf("$r0$r1".toLong())
        }
    }

    override fun part1(): Long {
        return equations.sumOf { equation ->
            val isValid = equation
                .solve()
                .any { it.left == it.right.single() }
            if (isValid) equation.left else 0
        }
    }


    override fun part2(): Long {
        return equations.filter { equation ->
            val isValid = equation
                .solve(p2 = true)
                .any { it.left == it.right.single() }
            isValid
        }.sumOf { it.left }
    }
}           