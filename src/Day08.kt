fun main() {

    fun part1(input: List<String>): Int {
        val grid = parseGrid(input)

        val rowScanResult = grid.indices
            .fold(emptySet<Pair<Int, Int>>()) { acc, i -> acc + grid.scanRow(i) }

        val columnScanResult = grid.first().indices
            .fold(emptySet<Pair<Int, Int>>()) { acc, i -> acc + grid.scanColumn(i) }

        return (rowScanResult + columnScanResult).size
    }

    fun part2(input: List<String>): Int {
        val grid = parseGrid(input)
        return input.indices.flatMap { y ->
            input.first().indices.map { x ->
                grid.viewNorth(x, y) * grid.viewSouth(x, y) * grid.viewEast(x, y) * grid.viewWest(x, y)
            }
        }
            .max()
    }


    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}

private fun parseGrid(input: List<String>) =
    input.map { line -> line.map { it.digitToInt() } }

private fun List<List<Int>>.column(x: Int): List<Int> =
    buildList { this@column.forEach { line -> add(line[x]) } }

private fun List<Int>.maxIncreasingIndices(): Set<Int> {
    val leftScanResult = foldIndexed(Pair(-1, emptySet<Int>())) { index, acc, current ->
        if (current > acc.first)
            Pair(current, acc.second + index)
        else
            acc
    }
        .second

    val rightScanResult = foldRightIndexed(Pair(-1, emptySet<Int>())) { index, current, acc ->
        if (current > acc.first)
            Pair(current, acc.second + index)
        else
            acc
    }
        .second

    return leftScanResult + rightScanResult
}

private fun List<List<Int>>.scanRow(y: Int) =
    this[y].maxIncreasingIndices()
        .map { Pair(it, y) }
        .toSet()

private fun List<List<Int>>.scanColumn(x: Int) =
    column(x).maxIncreasingIndices()
        .map { Pair(x, it) }
        .toSet()

private fun List<List<Int>>.viewWest(x: Int, y: Int): Int {
    val tree = this[y][x]
    return this[y].asSequence()
        .drop(x + 1)
        .countVisibleFrom(tree)
}

private fun List<List<Int>>.viewEast(x: Int, y: Int): Int {
    val row = this[y]
    val tree = row[x]
    return row.asReversed()
        .asSequence()
        .drop(row.size - x)
        .countVisibleFrom(tree)
}

private fun List<List<Int>>.viewSouth(x: Int, y: Int): Int {
    val column = column(x)
    val tree = column[y]
    return column.asSequence()
        .drop(y + 1)
        .countVisibleFrom(tree)
}

private fun List<List<Int>>.viewNorth(x: Int, y: Int): Int {
    val column = column(x)
    val tree = column[y]
    return column.asReversed()
        .asSequence()
        .drop(column.size - y)
        .countVisibleFrom(tree)
}

private fun Sequence<Int>.countVisibleFrom(fromHeight: Int) =
    indexOfFirst { it >= fromHeight }
        .let { if (it == -1) count() else it + 1 }
