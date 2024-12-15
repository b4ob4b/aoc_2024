import utils.*
import utils.navigation.Direction4

fun main() {
    Day15 { WithSample2Data }.test(2028)
    Day15 { WithSampleData }.test(10092, 9021)
    Day15 { WithInputData }.solve()
}

class Day15(dataType: () -> DataType) : Day("Warehouse Woes", dataType) {

    private val data = input.split("\n\n".toRegex())
    private val warehouse = data[0].toGrid().toField()
    private val warehouse2 = data[0].toGrid {
        when (it) {
            "#" -> "##"
            "O" -> "[]"
            "." -> ".."
            "@" -> "@."
            else -> throw Exception("unknown symbol $it")
        }
    }
        .map {
            it.joinToString("").split("")
        }
        .toField()
    private val path = data[1]
        .splitLines()
        .flatMap {
            it.split("").filter { it.isNotEmpty() }
        }
        .map {
            when (it) {
                "^" -> Direction4.North
                ">" -> Direction4.East
                "v" -> Direction4.South
                "<" -> Direction4.West
                else -> throw Exception("unknown symbol $it")
            }
        }

    private val wall = "#"
    private val lanternfish = "@"
    private val empty = "."
    private val box = "O"


    override fun part1(): Int {
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
        return path.fold(warehouse2) { warehouse, direction4 ->
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

                "[", "]" -> {
                    var canBeMoved: Boolean? = null
                    var checkPosition = look.doMovement(direction4)
                    while (canBeMoved == null) {
                        if (warehouse[checkPosition] == empty) {
                            canBeMoved = true
                        } else if (warehouse[checkPosition] == "[" || warehouse[checkPosition] == "]") {
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
                            when (direction4) {
                                Direction4.West -> {
                                    return@fold (look.doMovement(direction4)..checkPosition)
                                        .foldIndexed(warehouse) { index, warehouse2, position ->
                                            if (index % 2 == 0) {
                                                warehouse2.insertAt(position, "]")
                                            } else {
                                                warehouse2.insertAt(position, "[")
                                            }
                                        }.insertAt(lanternFish, empty)
                                        .insertAt(look, lanternfish)
                                }

                                Direction4.North -> {
                                    val boxesToMove = warehouse.checkBox(look, direction4)

                                    return@fold boxesToMove.fold(warehouse) { warehouse2, (from, to) ->
                                        warehouse2.insertAt(to, warehouse2[from])
                                            .insertAt(from, empty)
                                    }.let {
                                        if (boxesToMove.isEmpty()) {
                                            it
                                        } else {
                                            it.insertAt(look, lanternfish)
                                                .insertAt(lanternFish, empty)
                                        }
                                    }
                                }

                                Direction4.East -> {
                                    return@fold (look.doMovement(direction4)..checkPosition)
                                        .foldIndexed(warehouse) { index, warehouse2, position ->
                                            if (index % 2 == 0) {
                                                warehouse2.insertAt(position, "[")
                                            } else {
                                                warehouse2.insertAt(position, "]")
                                            }
                                        }.insertAt(lanternFish, empty)
                                        .insertAt(look, lanternfish)
                                }

                                Direction4.South -> {
                                    val boxesToMove = warehouse.checkBox(look, direction4)

                                    return@fold boxesToMove.fold(warehouse) { warehouse2, (from, to) ->
                                        warehouse2.insertAt(to, warehouse2[from])
                                            .insertAt(from, empty)
                                    }.let {
                                        if (boxesToMove.isEmpty()) {
                                            it
                                        } else {
                                            it.insertAt(look, lanternfish)
                                                .insertAt(lanternFish, empty)
                                        }
                                    }
                                }
                            }

                        }

                        else -> {
                            throw Exception("canBeMoved should not be null")
                        }
                    }
                }
            }
            throw Exception("other state should not happen")
        }
            .mapToList { position, cell -> if (cell == "[") position else null }
            .filterNotNull()
            .map { it.copy(y = warehouse2.height - 1 - it.y) }
            .sumOf { (x, y) ->
                x - 1 + y * 100
            }
    }

    private fun Field<String>.checkBox(position: Position, direction4: Direction4): Set<Pair<Position, Position>> {
        val directionToCheck1 = when (direction4) {
            Direction4.North -> Direction4.West
            Direction4.South -> Direction4.West
            else -> throw Exception("direction should be North or South")
        }
        val directionToCheck2 = when (direction4) {
            Direction4.North -> Direction4.East
            Direction4.South -> Direction4.East
            else -> throw Exception("direction should be North or South")
        }

        val partnerPiece = when (this[position]) {
            "]" -> position.doMovement(directionToCheck1)
            "[" -> position.doMovement(directionToCheck2)
            else -> throw Exception("position should be [ or ]")
        }
        val positionsToCheck = listOf(position, partnerPiece)
            .map { it.doMovement(direction4) }

        val canBeMoved = when {
            positionsToCheck.any { this[it] == wall } -> false
            positionsToCheck.any { this[it] == "]" || this[it] == "[" } -> null
            positionsToCheck.all { this[it] == empty } -> true
            else -> throw Exception("unknown object in the way")
        }

        val positionsToMove = mutableSetOf<Pair<Position, Position>>()
        if (canBeMoved == null) {
            val box1 = if (this[positionsToCheck[0]] == "[" || this[positionsToCheck[0]] == "]") {
                this.checkBox(positionsToCheck[0], direction4)
            } else {
                null
            }
            val box2 = if (this[positionsToCheck[1]] == "[" || this[positionsToCheck[1]] == "]") {
                this.checkBox(positionsToCheck[1], direction4)
            } else {
                null
            }

            if (box1 != null && box1.isEmpty() || box2 != null && box2.isEmpty()) {
                return emptySet()
            } else {
                if (box1 != null) positionsToMove.addAll(box1)
                if (box2 != null) positionsToMove.addAll(box2)
            }
        } else if (canBeMoved) {
            positionsToMove.add(position to positionsToCheck[0])
            positionsToMove.add(partnerPiece to positionsToCheck[1])
        }

        if (positionsToMove.isEmpty()) {
            return emptySet()
        } else {
            positionsToMove.add(position to positionsToCheck[0])
            positionsToMove.add(partnerPiece to positionsToCheck[1])
        }

        return positionsToMove
    }
}