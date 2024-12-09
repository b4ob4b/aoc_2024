import utils.*
import kotlin.math.abs

fun main() {
    Day02 { WithSampleData }.test(2, 4)
    Day02 { WithInputData }.solve()
}

class Day02(dataType: () -> DataType) : Day("Red-Nosed Reports", dataType) {

    private val reports = input
        .splitLines()
        .map { it.extractInts() }

    override fun part1(): Int {
        return reports
            .count { report ->
                report.isSafe()
            }
    }

    override fun part2(): Any {
        return reports.count { report ->
            report.indices.any { level ->
                report.filterIndexed { index, _ -> index != level }.isSafe()
            }
        }
    }

    private fun List<Int>.isSafe(): Boolean {
        return this
            .zipWithNext()
            .map { it.second - it.first }
            .let { differences ->
                val isInRange = differences.all { abs(it) in 1..3 }
                val isIncreasing = differences.all { it > 0 }
                val isDecreasing = differences.all { it < 0 }
                isInRange && (isIncreasing || isDecreasing)
            }
    }
}           