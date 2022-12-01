fun main() {

    fun descendingGroupSums(input: String) =
        input.split("\n\n")
            .map { group -> group.split("\n").sumOf { it.toInt() } }
            .sortedDescending()

    fun part1(input: String) =
        descendingGroupSums(input)
            .first()

    fun part2(input: String) =
        descendingGroupSums(input)
            .take(3)
            .sum()

    val testInput = readInputAsString("Day01_test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInputAsString("Day01")
    println(part1(input))
    println(part2(input))
}
