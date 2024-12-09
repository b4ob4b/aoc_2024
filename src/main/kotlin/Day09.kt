import utils.*

fun main() {
    Day09 { WithSampleData }.test(1928L, 2858L)
    Day09 { WithInputData }.solve()
}

class Day09(dataType: () -> DataType) : Day("Disk Fragmenter", dataType) {

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
            val emptyBlock = memory.withIndex().first { it.value.type == Type.FREE }
            val blockToMove = memory.withIndex().last { it.value.type == Type.USED }

            when {
                emptyBlock.value.length == blockToMove.value.length -> {
                    memory[emptyBlock.index] = blockToMove.value
                    memory.removeAt(blockToMove.index)
                }

                emptyBlock.value.length > blockToMove.value.length -> {
                    memory.removeAt(blockToMove.index)
                    memory[emptyBlock.index] = blockToMove.value
                    memory.add(
                        emptyBlock.index + 1,
                        Block(null, Type.FREE, emptyBlock.value.length - blockToMove.value.length)
                    )
                }

                else -> {
                    memory[blockToMove.index] =
                        Block(blockToMove.value.value, Type.USED, blockToMove.value.length - emptyBlock.value.length)
                    memory[emptyBlock.index] = Block(blockToMove.value.value, Type.USED, emptyBlock.value.length)
                }
            }

            if (memory.last().type == Type.FREE) {
                memory.removeAt(memory.lastIndex)
            }
        }
        return memory.calculateCheckSum()
    }

    override fun part2(): Long {
        val memory = memory.toMutableList()

        var indexToCheck = memory.count { it.type == Type.USED } - 1

        while (indexToCheck > 0) {
            val blockToMove = memory.withIndex()
                .filter { (_, block) ->
                    block.type == Type.USED
                }.withIndex()
                .last { (filteredIndex, globalIndexedBlock) ->
                    val (_, block) = globalIndexedBlock
                    block.type == Type.USED && filteredIndex == indexToCheck
                }.value

            if (blockToMove.isNotMovable(memory)) {
                indexToCheck--
                continue
            }

            val emptyBlock = memory.withIndex()
                .first { (_, block) ->
                    block.type == Type.FREE && block.length >= blockToMove.value.length
                }

            when {
                emptyBlock.value.length == blockToMove.value.length -> {
                    memory[blockToMove.index] = Block(null, Type.FREE, blockToMove.value.length)
                    memory[emptyBlock.index] = blockToMove.value
                }

                emptyBlock.value.length > blockToMove.value.length -> {
                    memory[blockToMove.index] = Block(null, Type.FREE, blockToMove.value.length)
                    memory.removeAt(emptyBlock.index)
                    memory.addAll(
                        emptyBlock.index,
                        listOf(
                            blockToMove.value,
                            Block(null, Type.FREE, emptyBlock.value.length - blockToMove.value.length)
                        )
                    )
                }
            }
        }
        return memory.calculateCheckSum()
    }

    private fun IndexedValue<Block>.isNotMovable(memory: List<Block>): Boolean {
        return memory.take(index).none { it.type == Type.FREE && it.length >= value.length }
    }

    private fun List<Block>.hasFreeSpace(): Boolean = any { it.type == Type.FREE }

    private fun List<Block>.calculateCheckSum() = fold(Pair(0, 0L)) { (index, overAllCheckSum), block ->
        val checkSum = (0 until block.length).sumOf { i ->
            (index + i) * (block.value ?: 0).toLong()
        }
        Pair(block.length + index, overAllCheckSum + checkSum)
    }.second
}