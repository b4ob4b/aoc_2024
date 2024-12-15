import utils.*
import utils.navigation.Direction4

fun main() {
    Day15 { WithSample2Data }.test(2028)
    Day15 { WithSampleData }.test(10092)
    Day15 { WithInputData }.solve()
}

class Day15(dataType: () -> DataType) : Day("Warehouse Woes", dataType) {

    private val data = input.split("\n\n".toRegex())
    private val warehouse = data[0].toGrid().toField()
    private val path = data[1]
        .splitLines()
        .flatMap {
            it.split("").filter { it.isNotEmpty() }
        }
        .map {
            when {
                it == "^" -> Direction4.North
                it == ">" -> Direction4.East
                it == "v" -> Direction4.South
                it == "<" -> Direction4.West
                else -> throw Exception("unknown symbol $it")
            }
        }

    private val wall = "#"
    private val lanternfish = "@"
    private val empty = "."
    private val box = "O"


    override fun part1(): Any? {
        return path.fold(warehouse) { warehouse, direction4 ->
            val lanternFish = warehouse.search(lanternfish).single()
            val look = lanternFish.doMovement(direction4)
            when (warehouse[look]) {
                wall -> {
                    return@fold warehouse
                }

                empty -> {
                    return@fold warehouse
                        .insertAt(lanternFish, empty)
                        .insertAt(look, lanternfish)
                }

                box -> {
                    var canBeMoved: Boolean? = null
                    var checkPosition = look.doMovement(direction4)
                    while (canBeMoved == null) {
                        if (warehouse[checkPosition] == empty) {
                            canBeMoved = true
                        } else if (warehouse[checkPosition] == box) {
                            checkPosition = checkPosition.doMovement(direction4)
                        } else if (warehouse[checkPosition] == wall) {
                            canBeMoved = false
                        } else {
                            throw Exception("movement should not exist")
                        }
                    }

                    when (canBeMoved) {
                        false -> {
                            return@fold warehouse
                        }

                        true -> {
                            return@fold warehouse
                                .insertAt(lanternFish, empty)
                                .insertAt(look, lanternfish)
                                .insertAt(checkPosition, box)
                        }

                        else -> {
                            throw Exception("canBeMoved should not be null")
                        }
                    }
                }
            }
            throw Exception("other state should not happen")
        }
            .mapToList { position, cell -> if (cell == box) position else null }
            .filterNotNull()
            .map { it.copy(y = warehouse.height - 1 - it.y) }
            .sumOf { (x, y) ->
                x + y * 100
            }
    }

    override fun part2(): Any? {
        return "not yet implement"
    }
}           