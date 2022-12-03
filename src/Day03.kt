fun main() {

    fun part1(input: List<String>): Int =
        input.map { it.charInBothHalves() }
            .sumOf { it.priority() }

    fun part2(input: List<String>): Int =
        input.chunked(3)
            .map { it.charInEach() }
            .sumOf { it.priority() }


    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}

private fun List<String>.charInEach() =
    drop(1)
        .fold(first().toSet()) {intersection, string -> intersection.intersect(string.toSet())}
        .first()

private fun Char.priority() =
    if (isLowerCase())
        1 + code - 'a'.code
    else
        27 + code - 'A'.code

private fun String.charInBothHalves() =
    take(length / 2)
        .toSet()
        .intersect(takeLast(length / 2).toSet())
        .first()
