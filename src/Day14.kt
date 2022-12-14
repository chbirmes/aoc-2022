import kotlin.math.max
import kotlin.math.min

fun main() {

    fun part1(input: List<String>): Int {
        val cave = Cave.parse(input, withFloor = false)
        return generateSequence { Sand() }
            .map { it.restPosition(cave) }
            .indexOfFirst { it == null }
    }

    fun part2(input: List<String>): Int {
        val cave = Cave.parse(input, withFloor = true)
        return generateSequence { Sand() }
            .map { it.restPosition(cave) }
            .indexOfFirst { it == sandSource } + 1
    }


    val testInput = readInput("Day14_test")
    check(part1(testInput) == 24)
    check(part2(testInput) == 93)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}

val sandSource = Cave.Position(500, 0)

class Sand(var position: Cave.Position = sandSource) {

    tailrec fun restPosition(cave: Cave): Cave.Position? =
        if (!cave.isAnythingBelow(position.y)) {
            null
        } else {
            val nextFree = sequenceOf(
                Cave.Position(position.x, position.y + 1),
                Cave.Position(position.x - 1, position.y + 1),
                Cave.Position(position.x + 1, position.y + 1),
            )
                .firstOrNull { !cave.isBlocked(it) }
            if (nextFree == null) {
                position.also { cave.blockedPositions.add(it) }
            } else {
                position = nextFree
                restPosition(cave)
            }
        }
}

class Cave(val blockedPositions: MutableSet<Position>, private val withFloor: Boolean) {

    private val lowestBlocked = blockedPositions.maxOf { it.y }

    fun isAnythingBelow(y: Int) = withFloor || y < lowestBlocked

    fun isBlocked(position: Position) =
        (withFloor && position.y >= lowestBlocked + 2) || blockedPositions.contains(position)

    data class Position(val x: Int, val y: Int) {
        fun positionsTo(other: Position): Set<Position> =
            if (x == other.x) {
                ((min(y, other.y))..(max(y, other.y)))
                    .map { Position(x, it) }
                    .toSet()
            } else {
                ((min(x, other.x))..(max(x, other.x)))
                    .map { Position(it, y) }
                    .toSet()
            }
    }

    companion object {
        fun parse(input: List<String>, withFloor: Boolean): Cave {
            val positions = input.asSequence()
                .flatMap { line ->
                    line.split(" -> ")
                        .map { Position(it.substringBefore(',').toInt(), it.substringAfter(',').toInt()) }
                        .windowed(2)
                        .flatMap { it[0].positionsTo(it[1]) }
                }
                .toMutableSet()
            return Cave(positions, withFloor)
        }
    }

}
