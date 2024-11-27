package utils

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class ConversionsTest {

    @Test
    fun toInfiniteSequence() {
        listOf(1, 2, 3).toInfiniteSequence().take(5).toList() shouldBe listOf(1, 2, 3, 1, 2)
    }

    @Test
    fun gcd() {
        listOf(18).gcd() shouldBe 18
        listOf(42, 18).gcd() shouldBe 6
        listOf(42, 18, 54).gcd() shouldBe 6
    }

    @Test
    fun lcm() {
        emptyList<Int>().lcm() shouldBe -1
        listOf(12).lcm() shouldBe 12
        listOf(12, 21).lcm() shouldBe 84
    }

    @Test
    fun toPosition3D() {
        "3,4,5".toPosition3D() shouldBe Position3D(3, 4, 5)
    }

    @Test
    fun toGrid() {
        val grid = """
            1,2,3
            4,5,6
        """.trimIndent()
            .toGrid(",") { it.toInt() + 1 }

        grid shouldBe listOf(listOf(2, 3, 4), listOf(5, 6, 7))
    }


}