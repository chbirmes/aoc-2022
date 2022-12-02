import java.lang.IllegalArgumentException

fun main() {

    fun score1(opponentChoice: Char, ownChoice: Char) =
        when (Pair(opponentChoice, ownChoice)) {
            Pair('A', 'X') -> 1 + 3
            Pair('A', 'Y') -> 2 + 6
            Pair('A', 'Z') -> 3 + 0
            Pair('B', 'X') -> 1 + 0
            Pair('B', 'Y') -> 2 + 3
            Pair('B', 'Z') -> 3 + 6
            Pair('C', 'X') -> 1 + 6
            Pair('C', 'Y') -> 2 + 0
            Pair('C', 'Z') -> 3 + 3
            else -> throw IllegalArgumentException()
        }

    fun part1(input: List<String>): Int = input.sumOf { score1(it[0], it[2]) }

    fun score2(opponentChoice: Char, outcome: Char) =
        when (Pair(opponentChoice, outcome)) {
            Pair('A', 'X') -> 3 + 0
            Pair('A', 'Y') -> 1 + 3
            Pair('A', 'Z') -> 2 + 6
            Pair('B', 'X') -> 1 + 0
            Pair('B', 'Y') -> 2 + 3
            Pair('B', 'Z') -> 3 + 6
            Pair('C', 'X') -> 2 + 0
            Pair('C', 'Y') -> 3 + 3
            Pair('C', 'Z') -> 1 + 6
            else -> throw IllegalArgumentException()
        }

    fun part2(input: List<String>): Int = input.sumOf { score2(it[0], it[2]) }


    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
