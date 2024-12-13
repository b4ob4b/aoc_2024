import utils.*

fun main() {
    Day13 { WithSampleData }.test(480L)
    Day13 { WithInputData }.solve()
}

class Day13(dataType: () -> DataType) : Day("Claw Contraption", dataType) {

    private val equations = input.split("\\n\\n".toRegex()).map {
        val lines = it.splitLines()
        val (a1, a2) = lines[0].extractInts().map { it.toLong() }
        val (b1, b2) = lines[1].extractInts().map { it.toLong() }
        val (result1, result2) = lines[2].extractInts().map { it.toLong() }
        Equation(a1, b1, result1) to Equation(a2, b2, result2)
    }


    private data class Equation(val a: Long, val b: Long, val result: Long) {}

    override fun part1() = equations.solve()
    override fun part2() = equations.solve(10000000000000L)

    private fun List<Pair<Equation, Equation>>.solve(conversionOffset: Long = 0L) =
        sumOf { equation ->
            val x = equation.first.let { it.copy(result = it.result + conversionOffset) }
            val y = equation.second.let { it.copy(result = it.result + conversionOffset) }

            val m = (x.result * y.b - y.result * x.b).toDouble() / (x.a * y.b - y.a * x.b)
            val n = (x.result - x.a * m) / x.b
            if (m % 1 != 0.0 || n % 1 != 0.0) 0L
            else m.toLong() * 3 + n.toLong()
        }

}           