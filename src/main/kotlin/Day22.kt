import utils.*

fun main() {
    Day22 { WithSampleData }.test(37327623L)
    Day22 { WithSample2Data }.test(part2 = 23)
    Day22 { WithInputData }.solve()
}

class Day22(dataType: () -> DataType) : Day("Monkey Market", dataType) {

    private val secretNumbers = input.splitLines().map { it.toLong() }

    override fun part1() = secretNumbers.sumOf { secretNumber ->
        (1..2000).fold(secretNumber) { acc, _ -> acc.calculateNextSecretNumber() }
    }

    override fun part2(): Int {
        val listOfSecrets = secretNumbers.map { secretNumber2 ->
            (1..2000).runningFold(secretNumber2) { secretNumber, _ ->
                secretNumber.calculateNextSecretNumber()
            }
                .map { it.toString().last().digitToInt() }
                .windowed(2) { (first, second) -> second to second - first }
                .windowed(4) { it.map { pair -> pair.second } to it.last().first }
        }

        val candidates = listOfSecrets.asSequence().flatten()
            .groupingBy { it.first }
            .fold(0) { acc, element -> acc + element.second }
            .toList()
            .sortedByDescending { it.second }

        return candidates.take(10)
            .maxOf { candidate ->
                listOfSecrets
                    .mapNotNull { secrets -> secrets.firstOrNull { it.first == candidate.first } }
                    .sumOf { it.second }
            }
    }


    private fun Long.calculateNextSecretNumber(): Long {
        val moduloNumber = 16777216

        val step1 = this * 64
        val step2 = this xor step1
        val step3 = step2 % moduloNumber
        val step4 = step3 / 32
        val step5 = step3 xor step4
        val step6 = step5 % moduloNumber
        val step7 = step6 * 2048
        val step8 = step7 xor step6
        val step9 = step8 % moduloNumber
        return step9
    }
}           