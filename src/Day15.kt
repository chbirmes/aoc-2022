import kotlin.math.absoluteValue

fun main() {

    fun part1(input: List<String>, y: Int): Int {
        val sensors = input.map { Sensor.parse(it) }
        val beaconXs = sensors.map { it.closestBeaconPosition }
            .filter { it.second == y }
            .map { it.first }
            .toSet()
        val ranges = sensors.map { it.radiusIntersectionWithLine(y) }
            .union()
        return ranges.sumOf { it.size() } - beaconXs.size
    }

    fun part2(input: List<String>, max: Int): Long {
        val sensors = input.map { Sensor.parse(it) }
        val targetRange = 0..max
        return targetRange.asSequence()
            .map { row ->
                val intersections = sensors.map { it.radiusIntersectionWithLine(row) }.union()
                intersections.asSequence()
                    .filter { it.last >= 0 && it.first <= max }
                    .takeIf { it.count() == 2 }
                    ?.sortedBy { it.first }
                    ?.let { (it.first().last + 1) to row }
            }
            .filterNotNull()
            .first()
            .let { (x, y) -> x.toLong() * 4_000_000L + y }
    }


    val testInput = readInput("Day15_test")
    check(part1(testInput, y = 10) == 26)
    check(part2(testInput, max = 20) == 56_000_011L)

    val input = readInput("Day15")
    println(part1(input, y = 2_000_000))
    println(part2(input, max = 4_000_000))
}

fun positionIn(s: String): Pair<Int, Int> =
    s.substringAfter("x=").substringBefore(',').toInt() to s.substringAfter("y=").toInt()

private data class Sensor(val ownPosition: Pair<Int, Int>, val closestBeaconPosition: Pair<Int, Int>) {

    val radius = ownPosition.distanceTo(closestBeaconPosition)

    fun radiusIntersectionWithLine(line: Int): IntRange {
        val distance = (ownPosition.second - line).absoluteValue
        val halfIntersectionLength = radius - distance
        return (ownPosition.first - halfIntersectionLength)..(ownPosition.first + halfIntersectionLength)
    }

    companion object {
        fun parse(line: String): Sensor =
            line.split(":")
                .let { (left, right) -> Sensor(positionIn(left), positionIn(right)) }
    }

}

private fun Pair<Int, Int>.distanceTo(other: Pair<Int, Int>) =
    (first - other.first).absoluteValue + (second - other.second).absoluteValue

private fun IntRange.union(other: IntRange): List<IntRange> =
    if (isEmpty()) {
        listOf(other)
    } else if (other.isEmpty()) {
        listOf(this)
    } else if (contains(other.first)) {
        if (contains(other.last)) {
            listOf(this)
        } else {
            listOf(first..other.last)
        }
    } else if (contains(other.last)) {
        listOf(other.first..last)
    } else if (other.contains(first)) {
        listOf(other)
    } else if (other.first == last + 1) {
        listOf(first..other.last)
    } else if (first == other.last + 1) {
        listOf(other.first..last)
    } else {
        listOf(this, other)
    }

private fun IntRange.size() = if (isEmpty()) 0 else (last - first + 1)

private fun Iterable<IntRange>.union() =
    sortedBy { it.first }.fold(listOf(IntRange.EMPTY)) { rangeList, range ->
        rangeList.dropLast(1) + rangeList.last().union(range)
    }