import utils.*

fun main() {
    Day17 { WithSampleData }.test("4,6,3,5,6,3,5,2,1,0")
    Day17 { WithInputData }.solve()
}

class Day17(dataType: () -> DataType) : Day("Chronospatial Computer", dataType) {

    private val data = input.split("\n\n".toRegex())
    private val registers = data.first().splitLines().map {
        val (name, value) = "[ABC]|\\d+".toRegex().findAll(it).map { it.value }.toList()
        name to value.toInt()
    }.toMap()

    private val program = Program(registers, data.last().extractInts())

    private data class Program(val registers: Map<String, Int>, val values: List<Int>) {
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
                        val denominator = Math.pow(2.0, comboOperand.toDouble()).also {
                            if (it > Int.MAX_VALUE) {
                                throw NumberFormatException("number too big for Int")
                            }
                        }.toInt()
                        registers["A"] = numerator / denominator
                    }

                    1 -> {
                        registers["B"] = registers["B"]!! xor literalCombo
                    }

                    2 -> {
                        registers["B"] = comboOperand % 8
                    }

                    3 -> {
                        if (registers["A"] != 0) {
                            instructionPointer = literalCombo
                        } else {
                            instructionPointer += 2
                        }
                    }

                    4 -> {
                        registers["B"] = registers["B"]!! xor registers["C"]!!
                    }

                    5 -> {
                        output += "${comboOperand % 8},"
                    }

                    6 -> {
                        val numerator = registers["A"]!!
                        val denominator = Math.pow(2.0, comboOperand.toDouble()).also {
                            if (it > Int.MAX_VALUE) {
                                throw NumberFormatException("number too big for Int")
                            }
                        }.toInt()
                        registers["B"] = numerator / denominator
                    }

                    7 -> {
                        val numerator = registers["A"]!!
                        val denominator = Math.pow(2.0, comboOperand.toDouble()).also {
                            if (it > Int.MAX_VALUE) {
                                throw NumberFormatException("number too big for Int")
                            }
                        }.toInt()
                        registers["C"] = numerator / denominator
                    }
                }

                if (opcode != 3) instructionPointer += 2
            }

            return output
        }
    }

    override fun part1(): Any? {
        return program.run().let { it.substring(0 until it.length - 1) }
    }

    override fun part2(): Any? {
        return "not yet implement"
    }
}           