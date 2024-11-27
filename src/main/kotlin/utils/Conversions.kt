package utils

fun main() {
    """
        1
        2
    """.trimIndent().splitLines().print()
    // [1, 2]

    "10100".binaryToDecimal().print()
    // 20

    "Abc: 10,2,3,4".extractToList("\\d+").print()
    // [10, 2, 3, 4]

    "Abc: 10,2,3,4".extractToList("\\w").print()
    // [A, b, c, 1, 0, 2, 3, 4]

    """
        123
        456
    """.trimIndent().toGrid().print()
    // [[1, 2, 3], [4, 5, 6]]

    """
        1,2,3
        4,5,6
    """.trimIndent().toGrid(",") { it.toInt() + 1 }.print()
    // [[2, 3, 4], [5, 6, 7]]
}

fun <T> T.print() = this.also { println(it) }

fun String.splitLines() = split("\n")

fun String.splitLinesToInt() = split("\n").map(String::toInt)

fun String.binaryToDecimal() = Integer.parseInt(this, 2)

fun String.extractToList(pattern: Regex) = pattern.findAll(this).map { it.value }.toList()

fun String.extractToList(pattern: String) = this.extractToList(Regex(pattern))

fun String.extractInts() = this.extractToList("-?\\d+").map { it.toInt() }

fun String.toGrid(separator: String = "", filterBlanks: Boolean = true) =
    this.splitLines().map { line ->
        line.split(separator).let {
            if (filterBlanks) {
                it.filter { it.isNotBlank() }
            } else {
                it
            }
        }
    }

fun <T> String.toGrid(separator: String = "", mapCell: ((String) -> T)): List<List<T>> =
    this.toGrid(separator).map { it.map(mapCell) }

fun String.toPosition3D() = this.split(",").map { it.toInt() }
    .let { (x, y, z) -> Position3D(x, y, z) }

fun <T> List<T>.toInfiniteSequence() = this.let { generateSequence(it) { it }.flatten() }

fun List<Int>.gcd() = reduce { a, b ->
    val lcm = listOf(a, b).lcm().toInt()
    a * b / lcm
}

fun List<Int>.lcm(): Long {
    val maxNumber = this.maxOrNull() ?: return -1
    return generateSequence(maxNumber.toLong()) { it + maxNumber }
        .first { multiple ->
            this.all { multiple % it == 0L }
        }
}