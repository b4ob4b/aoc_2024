import utils.*

fun main() {
    Day07 { WithSampleData }.test(3749L, 11387L)
    Day07 { WithInputData }.solve()
}

class Day07(dataType: () -> DataType) : Day("Bridge Repair", dataType) {

    private val equations = input.splitLines().map { line ->
        val (left, right) = line.split(": ")
        Equation(left.toLong(), right.extractInts().map { it.toLong() })
    }


    override fun part1() = equations solveWith listOf(Operation.Add, Operation.Multiply)

    override fun part2() = equations solveWith listOf(Operation.Add, Operation.Multiply, Operation.Concatenate)

    private infix fun List<Equation>.solveWith(operations: List<Operation>) =
        filter { equation ->
            equation.solve(operations).any { it.left == it.right.single() }
        }
            .sumOf { it.left }

    private data class Equation(val left: Long, val right: List<Long>) {
        fun solve(operations: List<Operation>): Sequence<Equation> = sequence {
            if (right.size == 1) yield(this@Equation) else {
                val (operand1, operand2) = right.take(2)
                val rest = right.drop(2)
                operations.forEach { operation ->
                    yieldAll(
                        Equation(left, listOf(operation.apply(operand1, operand2)) + rest)
                            .solve(operations)
                    )
                }
            }
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