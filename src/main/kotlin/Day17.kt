import utils.*
import kotlin.math.pow

fun main() {
    Day17 { WithSampleData }.test("4,6,3,5,6,3,5,2,1,0")
    Day17 { WithInputData }.solve()
}

class Day17(dataType: () -> DataType) : Day("Chronospatial Computer", dataType) {

    private val data = input.split("\n\n".toRegex())
    private val registers = data.first().splitLines().associate {
        val (name, value) = "[ABC]|\\d+".toRegex().findAll(it).map { it.value }.toList()
        name to value.toLong()
    }

    private val programString = data.last().split(" ").last()
    private val program = Program(registers, programString.extractInts())

    override fun part1() = program.run()

    override fun part2() = 0L.findPossibleRegisterAValues(programString.extractInts().size - 1)
        .filter { registerAValue ->
            runProgram(registerAValue) == programString
        }
        .min()

    private fun runProgram(registerAValue: Long) = Program(
        mapOf(
            "A" to registerAValue,
            "B" to 0,
            "C" to 0,
        ), programString.extractInts()
    ).run()

    private data class Program(val registers: Map<String, Long>, val values: List<Int>) {
        fun run(): String {
            val registers = registers.toMutableMap()
            var instructionPointer = 0
            var output = ""

            while (instructionPointer < values.size) {
                val opcode = values[instructionPointer]
                val literalCombo = values[instructionPointer + 1]

                val comboOperand = when (literalCombo) {
                    in 0..3 -> literalCombo
                    4 -> registers["A"]!!
                    5 -> registers["B"]!!
                    6 -> registers["C"]!!
                    7 -> 7
                    else -> {
                        throw Exception("unknown combo $literalCombo")
                    }
                }

                when (opcode) {
                    0 -> {
                        val numerator = registers["A"]!!
                        val denominator = 2.0.pow(comboOperand.toDouble()).toInt()
                        registers["A"] = numerator / denominator
                    }

                    1 -> {
                        registers["B"] = registers["B"]!! xor literalCombo.toLong()
                    }

                    2 -> {
                        registers["B"] = comboOperand.toLong() % 8
                    }

                    3 -> {
                        if (registers["A"] != 0L) {
                            instructionPointer = literalCombo
                        } else {
                            instructionPointer += 2
                        }
                    }

                    4 -> {
                        registers["B"] = registers["B"]!! xor registers["C"]!!
                    }

                    5 -> {
                        output += "${if (output.isNotEmpty()) "," else ""}${comboOperand.toLong() % 8}"
                    }

                    6 -> {
                        val numerator = registers["A"]!!
                        val denominator = 2.0.pow(comboOperand.toDouble()).toInt()
                        registers["B"] = numerator / denominator
                    }

                    7 -> {
                        val numerator = registers["A"]!!
                        val denominator = 2.0.pow(comboOperand.toDouble()).toInt()
                        registers["C"] = numerator / denominator
                    }
                }

                if (opcode != 3) instructionPointer += 2
            }

            return output
        }
    }

    private fun Long.findPossibleRegisterAValues(exponent: Int): List<Long> {
        if (exponent < 0) return listOf(this)
        val string = data.last()
        val program = string.extractInts()
        val possibleValues = mutableSetOf<Long>()

        val minimumCounter = if (this == 0L) 1 else 0
        var counter = 8
        var output: String
        var registerAValue: Long
        while (counter >= minimumCounter) {
            counter -= 1

            registerAValue = counter * 8.0.pow(exponent).toLong() + this
            val registers = mapOf(
                "A" to registerAValue,
                "B" to 0,
                "C" to 0,
            )
            output = Program(registers, program).run()

            if (program[exponent] == output.extractInts().getOrNull(exponent)) {
                possibleValues.add(registerAValue)
            }

        }
        return possibleValues.flatMap { it.findPossibleRegisterAValues(exponent - 1) }
    }

}           