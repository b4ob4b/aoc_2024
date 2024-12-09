package utils

import utils.IO.createNextDay
import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.absolute


fun main() {
    createNextDay(true)
}

sealed class DataType {
    abstract val path: String
}

data object WithSampleData : DataType() {
    override val path = "sample"
}

data object WithSample2Data : DataType() {
    override val path = "sample2"
}

data object WithInputData : DataType() {
    override val path = "input"
}

object IO {

    private val basePath = Path("").absolute()
    private val resourcesPath = "$basePath/src/main/resources"
    private val sourceCodePath = "$basePath/src/main/kotlin"

    fun readFile(day: Int, dataType: DataType): String {
        val filePath = "$resourcesPath/$day/${dataType.path}.txt"
        return File(filePath).readText()
    }

    fun createNewDay(day: Int, createInputFiles: Boolean = true) {
        if (createInputFiles) createInputFiles(day)
        createKtFile(day)
    }

    fun createNextDay(createInputFiles: Boolean = true) {
        val lastDay = File(resourcesPath).listFiles()?.maxOfOrNull { it.name.toInt() } ?: 0
        createNewDay(lastDay + 1, createInputFiles)
    }

    private fun createInputFiles(day: Int) {
        val dir = File("$resourcesPath/$day")
        if (dir.mkdir()) {
            if (File("$dir/sample.txt").createNewFile()) println("sample created")
            if (File("$dir/input.txt").createNewFile()) println("input created")
        }
    }

    private fun createKtFile(day: Int) {
        val formattedDay = day.toString().padStart(2, '0')
        val text = """
            import utils.*
            
            fun main() {
                Day$formattedDay { WithSampleData }.test()
                Day$formattedDay { WithInputData }.solve()
            }
            
            class Day$formattedDay(dataType: () -> DataType) : Day("", dataType) {
            
                private val data = input.splitLines()
                
                override fun part1(): Any? {
                    data
                        .print()
                    return "not yet implement"
                }

                override fun part2(): Any? {
                    return "not yet implement"
                }
            }           
        """.trimIndent()

        val dir = File(sourceCodePath)
        if (dir.isDirectory) {
            val file = File("$dir/Day$formattedDay.kt")
            if (!file.exists()) {
                file.writeText(text)
                println("Day$formattedDay.kt created")
            } else {
                println("Day$formattedDay.kt already exists")
            }
        }
    }
}
