package utils

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class FieldTest {

    private val field = Field(
        listOf(
            listOf(1, 2, 3),
            listOf(4, 5, 6)
        )
    )

    @Test
    fun constructor() {
        Field(2, 3) { 1 } shouldBe Field(
            listOf(
                listOf(1, 1),
                listOf(1, 1),
                listOf(1, 1),
            )
        )
    }

    @Test
    fun properties() {
        field.numberOfX shouldBe 3
        field.numberOfY shouldBe 2
        field.xIndices.toList() shouldBe listOf(0, 1, 2)
        field.yIndices.toList() shouldBe listOf(0, 1)
    }

    @Test
    fun operatorFun() {
        field[Position.origin] shouldBe 1
    }

    @Test
    fun insertAtPosition() {
        field.insertAt(Position.origin, 10) shouldBe Field(
            listOf(
                listOf(10, 2, 3),
                listOf(4, 5, 6),
            )
        )
    }

    @Test
    fun search() {
        field.search(3).first() shouldBe Position(2, 0)
    }

    @Test
    fun insertAtPositions() {
        val positionMap = listOf(Position.origin, Position(2, 1)).associateWith { 5 }
        field.insertAt(positionMap) shouldBe Field(
            listOf(
                listOf(5, 2, 3),
                listOf(4, 5, 5),
            )
        )
    }

    @Test
    fun map() {
        field.map { _, cell ->
            cell * 2
        } shouldBe Field(
            listOf(
                listOf(2, 4, 6),
                listOf(8, 10, 12),
            )
        )
    }

    @Test
    fun highlight() {
        field.highlight { _, cell ->
            cell % 2 == 0
        }
    }

}