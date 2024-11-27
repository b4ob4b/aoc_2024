package utils.matrix

import utils.print
import utils.toMatrix

data class Matrix<T>(val matrix: List<List<T>>) {

    constructor(rows: Int, cols: Int, element: () -> T) : this(List(rows * cols) { element.invoke() }.chunked(cols))

    val numberOfRows = matrix.size
    val numberOfCols = matrix.first().size

    val rowIndices = 0 until numberOfRows
    val colIndices = 0 until numberOfCols

    operator fun get(point: Position) = matrix[point.row][point.col]

    operator fun contains(position: Position) = position.row in rowIndices && position.col in colIndices

    fun <T> search(element: T) = sequence {
        (0 until numberOfRows).flatMap { row ->
            (0 until numberOfCols).map { col ->
                if (matrix[row][col] == element) yield(Position(row, col))
            }
        }
    }

    fun search(element: (p: Position) -> Boolean) = sequence {
        (0 until numberOfRows).flatMap { row ->
            (0 until numberOfCols).map { col ->
                if (element.invoke(Position(row, col))) yield(Position(row, col))
            }
        }
    }

    fun flipHorizontal(): Matrix<T> {
        return matrix.reversed().toMatrix()
    }

    fun flipVertical(): Matrix<T> {
        return matrix.map { it.reversed() }.toMatrix()
    }

    fun transpose(): Matrix<T> {
        return (0 until numberOfCols).map { col ->
            (0 until numberOfRows).map { row ->
                matrix[row][col]
            }
        }.toMatrix()
    }

    fun rotateClockWise() = this.transpose().flipVertical()

    fun rotateCounterClockWise() = this.transpose().flipHorizontal()

    fun insertAt(position: Position, element: T) = this
        .map { pos, cell: T ->
            if (pos == position) element else cell
        }

    fun insertAt(positionMap: Map<Position, T>) = this
        .map { position, cell: T ->
            if (positionMap.containsKey(position)) positionMap[position]!! else cell
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

    fun <R> map(it: (position: Position, cell: T) -> R): Matrix<R> {
        return matrix
            .mapIndexed { row, rows ->
                rows.mapIndexed { col, cell ->
                    val position = Position(row, col)
                    it(position, cell)
                }
            }.toMatrix()
    }

    override fun toString() = matrix
        .joinToString("\n") { row ->
            row.joinToString(" ")
        }
}
