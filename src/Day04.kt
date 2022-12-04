fun main() {

    fun part1(input: List<String>): Int =
        input
            .map { it.toRangePair() }
            .count { it.first.contains(it.second) || it.second.contains(it.first) }

    fun part2(input: List<String>): Int =
        input
            .map { it.toRangePair() }
            .count { it.containsOverlap() }


    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}

private fun String.toRangePair() =
    split(',').map { range ->
        range.split('-')
            .map { it.toInt() }
    }
        .map { it[0]..it[1] }
        .let { Pair(it[0], it[1]) }

private fun IntRange.contains(other: IntRange) =
    contains(other.first) && contains(other.last)

private fun Pair<IntRange, IntRange>.containsOverlap() =
    first.contains(second.first) || first.contains(second.last) || second.contains(first)
