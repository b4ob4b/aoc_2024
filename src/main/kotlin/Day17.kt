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
        fun run(): Map<String, Int> {
            return values
                .chunked(2)
                .fold(registers.toMutableMap()) { registers: MutableMap<String, Int>, (opcode: Int, combo: Int) ->

                    val comboOperand = when (combo) {
                        in 0..3 -> combo
                        4 -> registers["A"]!!
                        5 -> registers["B"]!!
                        6 -> registers["C"]!!
                        7 -> 7
                        else -> {
                            throw Exception("unknown combo $combo")
                        }
                    }

                    when (opcode) {
                        0 -> {
                            val numerator = registers["A"]!!
                            val denominator = comboOperand * comboOperand
                            registers["A"] = numerator / denominator
                        }

                        1 -> {
                            registers["B"] = registers["B"]!! xor combo
                        }

                        2 -> {
                            registers["B"] = comboOperand % 8
                        }

                        3 -> {
                            if (registers["A"] != 0) {
                                throw NotImplementedError("pointer thing has to be implemented :)")
//                                set pointer to $combo
//                                after that, the pointer doesn't jump by 2
                            }
                        }

                        4 -> {
                            registers["B"] = registers["B"]!! xor registers["C"]!!
                        }

                        5 -> {
                            val output = comboOperand % 8
                            print("$output, ")
                        }

                        6 -> {
                            val numerator = registers["A"]!!
                            val denominator = comboOperand * comboOperand
                            registers["B"] = numerator / denominator
                        }

                        7 -> {
                            val numerator = registers["A"]!!
                            val denominator = comboOperand * comboOperand
                            registers["C"] = numerator / denominator
                        }
                    }
                    registers
                }.toMap()
        }
    }

    override fun part1(): Any? {
        Program(mapOf("A" to 0, "B" to 0, "C" to 9), listOf(2, 6)).run().print()
        Program(mapOf("A" to 10, "B" to 0, "C" to 0), listOf(5, 0, 5, 1, 5, 4)).run().print()
        // some missing program
        Program(mapOf("A" to 0, "B" to 29, "C" to 0), listOf(1, 7)).run().print()
        Program(mapOf("A" to 0, "B" to 2024, "C" to 43690), listOf(4, 0)).run().print()
        return "not yet implement"
    }

    override fun part2(): Any? {
        return "not yet implement"
    }
}           