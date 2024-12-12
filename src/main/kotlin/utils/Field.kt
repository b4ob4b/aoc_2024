package utils

/*
* x is the column
* y is the row
* y goes "up"
*
* */
data class Field<T>(val field: List<List<T>>) {

    constructor(x: Int, y: Int, element: () -> T) : this(List(x * y) { element.invoke() }.chunked(x))

    val numberOfX = field.first().size
    val numberOfY = field.size
    val allPositions: Sequence<Position> = sequence {
        xIndices.forEach { y ->
            yIndices.forEach { x ->
                yield(Position(x, y))
            }
        }
    }

    val xIndices = 0 until numberOfX
    val yIndices = 0 until numberOfY

    operator fun get(position: Position) = field[position.y][position.x]

    operator fun contains(position: Position): Boolean {
        return position.x in xIndices && position.y in yIndices
    }

    fun search(element: T) = sequence {
        (0 until numberOfY).flatMap { y ->
            (0 until numberOfX).map { x ->
                if (field[y][x] == element) yield(Position(x, y))
            }
        }
    }


    fun insertAt(position: Position, element: T) = this
        .map { positionOfElement, cell ->
            if (position == positionOfElement) element else cell
        }

    fun insertAt(positionMap: Map<Position, T>) = this
        .map { position, cell ->
            if (positionMap.containsKey(position)) positionMap[position]!! else cell
        }

    fun <R> map(it: (position: Position, cell: T) -> R): Field<R> {
        return field
            .mapIndexed { y, xs ->
                xs.mapIndexed { x, cell ->
                    val position = Position(x, y)
                    it(position, cell)
                }
            }
            .reversed()
            .toField()
    }

    fun highlight(highlight: (position: Position, cell: T) -> Boolean) {
        val highlightColor = "\u001b[" + 43 + "m"
        val defaultColor = "\u001b[" + 0 + "m"
        this
            .map { position, cell ->
                if (highlight(position, cell)) {
                    "$highlightColor$cell$defaultColor"
                } else {
                    cell.toString()
                }
            }
            .print()
    }


    override fun toString() = field
        .reversed()
        .joinToString("\n") { row ->
            row.joinToString(" ")
        }
}
