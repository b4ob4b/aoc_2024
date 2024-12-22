import utils.*
import kotlin.collections.ArrayDeque

fun main() {
    Day20 { WithSampleData }.test()
    Day20 { WithInputData }.solve()
}

class Day20(dataType: () -> DataType) : Day("Race Condition", dataType) {

    private val maze = input.toGrid().toField()
    private val wall = "#"
    private val start = "S"
    private val end = "E"
    private val startPosition = maze.search(start).single()
    private val endPosition = maze.search(end).single()
    private val timesTrackForward = bfs(startPosition, endPosition)
    private val timesTrackBackWards = bfs(endPosition, startPosition)
    private val costWithoutCheat = timesTrackForward.single { it.position == endPosition }.cost

    override fun part1() = calculateAmountCheatsSaving(2)

    override fun part2() = calculateAmountCheatsSaving(20)

    private data class Step(val position: Position, val cost: Int)

    private fun calculateAmountCheatsSaving(savingTime: Int): Int {
        return timesTrackForward.asSequence().flatMap { source ->
            timesTrackBackWards.map { target ->
                if (source.position == target.position) return@map null

                val distance = (target.position - source.position).manhattenDistance
                if (distance <= savingTime) {
                    source.cost + target.cost + distance
                } else null
            }
        }.filterNotNull()
            .filter { it < costWithoutCheat }
            .map { costWithoutCheat - it }
            .count { it >= 100 }
    }

    private fun bfs(startPosition: Position, endPosition: Position): Set<Step> {
        val queue = ArrayDeque<Step>()
        val visited = mutableSetOf<Step>()
        queue.add(Step(startPosition, 0))

        while (queue.isNotEmpty()) {
            val (position, cost) = queue.removeFirst()

            if (!visited.add(Step(position, cost))) continue

            if (position == endPosition) {
                break
            }
            position.get4Neighbors().forEach { neighbor ->
                if (maze[neighbor] != wall && neighbor !in visited.map { it.position }) {
                    queue.add(Step(neighbor, cost + 1))
                }
            }
        }
        return visited.toSet()
    }
}           