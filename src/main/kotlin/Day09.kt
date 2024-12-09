import utils.*

fun main() {
    Day09(IO.TYPE.SAMPLE).test(1928L, 2858L)
    Day09().solve()
}

class Day09(inputType: IO.TYPE = IO.TYPE.INPUT) : Day("Disk Fragmenter", inputType = inputType) {

    private val memory = input
        .extractToList("\\d".toRegex())
        .map { it.toInt() }
        .mapIndexed { index, length ->
            val value = if (index % 2 == 0) index / 2 else null
            val type = if (index % 2 == 0) Type.USED else Type.FREE
            Block(value, type, length)
        }
        .filter { it.length > 0 }

    data class Block(val value: Int?, val type: Type, val length: Int)

    enum class Type {
        USED, FREE
    }

    override fun part1(): Long {
        val memory = memory.toMutableList()
        while (memory.hasFreeSpace()) {
            val emtpyBlock = memory.withIndex().first { (i, b) -> b.type == Type.FREE }
            val blockToMove = memory.withIndex().last { (i, b) -> b.type == Type.USED }

            if (emtpyBlock.value.length == blockToMove.value.length) {
                memory.removeAt(blockToMove.index)
                memory[emtpyBlock.index] = blockToMove.value
            } else if (emtpyBlock.value.length > blockToMove.value.length) {
                memory.removeAt(blockToMove.index)
                memory.removeAt(emtpyBlock.index)
                memory.addAll(
                    emtpyBlock.index,
                    listOf(
                        blockToMove.value,
                        Block(null, Type.FREE, emtpyBlock.value.length - blockToMove.value.length)
                    )
                )
            } else if (emtpyBlock.value.length < blockToMove.value.length) {
                memory[blockToMove.index] = Block(
                    blockToMove.value.value, Type.USED, blockToMove.value.length - emtpyBlock
                        .value.length
                )
                memory[emtpyBlock.index] = Block(
                    blockToMove.value.value, Type.USED, emtpyBlock.value.length
                )
            }

            if (memory.last().type == Type.FREE) {
                memory.removeAt(memory.lastIndex)
            }
        }
        return memory.calculateCheckSum()
    }

    override fun part2(): Long {
        val memory = memory.toMutableList()

        var c = memory.count { it.type == Type.USED } - 1

        while (c > 0) {
            val blockToMove = memory.withIndex()
                .filter { (i, block) ->
                    block.type == Type.USED
                }.withIndex()
                .last { (filteredIndex, globalIndexedBlock) ->
                    val (_, block) = globalIndexedBlock
                    block.type == Type.USED && filteredIndex == c
                }.value

            if (memory.take(blockToMove.index).filter { it.type == Type.FREE }.none {
                    it.length >= blockToMove.value
                        .length
                }) {
                c--
                continue
            }

            val emtpyBlock = memory.withIndex()
                .first { (i, block) ->
                    block.type == Type.FREE && block.length >= blockToMove.value.length
                }

            if (emtpyBlock.value.length == blockToMove.value.length) {
                memory[blockToMove.index] = Block(null, Type.FREE, blockToMove.value.length)
                memory[emtpyBlock.index] = blockToMove.value
            } else if (emtpyBlock.value.length > blockToMove.value.length) {
                memory[blockToMove.index] = Block(null, Type.FREE, blockToMove.value.length)
                memory.removeAt(emtpyBlock.index)
                memory.addAll(
                    emtpyBlock.index,
                    listOf(
                        blockToMove.value,
                        Block(null, Type.FREE, emtpyBlock.value.length - blockToMove.value.length)
                    )
                )
            }
        }
        return memory.calculateCheckSum()
    }

    private fun List<Block>.hasFreeSpace(): Boolean = any { it.type == Type.FREE }

    private fun List<Block>.calculateCheckSum() = fold(Pair(0, 0L)) { (index, overAllCheckSum), block ->
        val checkSum = (0 until block.length).sumOf { i ->
            (index + i) * (block.value ?: 0).toLong()
        }
        Pair(block.length + index, overAllCheckSum + checkSum)
    }.second

    private fun List<Block>.print1() {
        forEach { block ->
            when (block.type) {
                Type.USED -> repeat(block.length) { print("(${block.value})") }
//                Type.USED -> repeat(block.length) { print(block.value) }
                Type.FREE -> repeat(block.length) { print(".") }
            }
        }.also {
            println()
        }
    }
}           