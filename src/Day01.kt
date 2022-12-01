fun main() {

    fun groupSums(input: String) =
        input.split("\n\n")
            .map { group -> group.split("\n").sumOf { it.toInt() } }

    fun part1(input: String) = groupSums(input).max()

    fun part2(input: String) =
        groupSums(input)
            .sortedDescending()
            .take(3)
            .sum()

    val testInput = readInputAsString("Day01_test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInputAsString("Day01")
    println(part1(input))
    println(part2(input))
}
