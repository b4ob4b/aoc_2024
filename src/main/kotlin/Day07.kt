import utils.*

fun main() {
    Day07(IO.TYPE.SAMPLE).test(3749L, 11387L)
    Day07().solve()
}

class Day07(inputType: IO.TYPE = IO.TYPE.INPUT) : Day("Bridge Repair", inputType = inputType) {

    private val equations = input.splitLines().map { line ->
        val (left, right) = line.split(": ")
        Equation(left.toLong(), right.extractInts().map { it.toLong() })
    }


    override fun part1() = equations solveWith listOf(Operation.Add, Operation.Multiply)

    override fun part2() = equations solveWith listOf(Operation.Add, Operation.Multiply, Operation.Concatenate)

    private infix fun List<Equation>.solveWith(operations: List<Operation>) = filter { equation ->
        val isValid = equation
            .solve(operations)
            .any { it.left == it.right.single() }
        isValid
    }.sumOf { it.left }

    private data class Equation(val left: Long, val right: List<Long>) {
        fun solve(operations: List<Operation>): List<Equation> {
            if (right.sum() > left) return emptyList()
            if (right.size == 1) return listOf(this)
            val (r0, r1) = right.take(2)
            val rest = right.drop(2)

            return operations.map { Equation(left, listOf(it.apply(r0, r1)) + rest).solve(operations) }.flatten()
        }
    }

    private enum class Operation {
        Add, Multiply, Concatenate;

        fun apply(left: Long, right: Long) = when (this) {
            Add -> left + right
            Multiply -> left * right
            Concatenate -> "$left$right".toLong()
        }
    }
}