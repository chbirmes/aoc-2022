import kotlin.math.absoluteValue
import kotlin.math.sign

fun main() {

    fun solveForTailLength(tailLength: Int, input: List<String>): Int {
        val tail = List(tailLength) { origin }
        return input.asSequence()
            .flatMap { it.toMoves() }
            .fold(KnotState(origin, tail)) { state, move -> state.process(move) }
            .visitedByLastTailKnot
            .size
    }

    fun part1(input: List<String>): Int = solveForTailLength(1, input)

    fun part2(input: List<String>): Int = solveForTailLength(9, input)


    val testInput = readInput("Day09_test")
    check(part1(testInput) == 13)
    val testInput2 = readInput("Day09_test2")
    check(part2(testInput2) == 36)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}

private fun String.toMoves() = List(drop(2).toInt()) { first() }

private data class Position(val x: Int, val y: Int) {
    fun move(move: Char): Position =
        when (move) {
            'R' -> Position(x + 1, y)
            'L' -> Position(x - 1, y)
            'U' -> Position(x, y + 1)
            'D' -> Position(x, y - 1)
            else -> throw IllegalArgumentException()
        }

    fun follow(other: Position): Position {
        val relativeOther = Position(other.x - x, other.y - y)
        return if (relativeOther.x.absoluteValue < 2 && relativeOther.y.absoluteValue < 2) {
            this
        } else {
            Position(x + relativeOther.x.sign, y + relativeOther.y.sign)
        }
    }
}

private val origin = Position(0, 0)

private data class KnotState(val head: Position, val tail: List<Position>, val visitedByLastTailKnot: Set<Position> = setOf(tail.last())) {
    fun process(move: Char): KnotState {
        val newHead = head.move(move)
        val newTail = tail.fold(listOf(newHead)) { newList, knot ->
            newList + knot.follow(newList.last())
        }
            .drop(1)
        return KnotState(newHead, newTail, visitedByLastTailKnot + newTail.last())
    }
}
