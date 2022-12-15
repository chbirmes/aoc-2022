fun main() {

    fun part1(input: List<String>): Int =
        HeightGrid.parse(input).shortestPathLengthToTopFrom { it.isStart }

    fun part2(input: List<String>): Int =
        HeightGrid.parse(input).shortestPathLengthToTopFrom { it.height == 'a' }


    val testInput = readInput("Day12_test")
    check(part1(testInput) == 31)
    check(part2(testInput) == 29)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}

class HeightGrid(private val cells: List<List<Cell>>) {

    fun shortestPathLengthToTopFrom(cellPredicate: (Cell) -> Boolean): Int {
        val start = positionsWhere(cellPredicate)
        val sequence = generateSequence(start) { step(it) }
        return sequence.indexOfFirst {
            it.map { visited -> cellAt(visited) }
                .any { cell -> cell.isGoal }
        }
    }

    private fun positionsWhere(cellPredicate: (Cell) -> Boolean): Set<Position> =
        cells.flatMapIndexed { y, row ->
            row.mapIndexed { x: Int, cell: Cell ->
                if (cellPredicate.invoke(cell)) Position(x, y) else null
            }
        }
            .filterNotNull()
            .toSet()

    private fun cellAt(position: Position) = cells[position.y][position.x]

    private fun nonVisitedNeighbors(p: Position) =
        p.neighbors()
            .filter { it.y in cells.indices && it.x in cells.first().indices }
            .filter { cellAt(it).height.code - cellAt(p).height.code <= 1 }
            .filterNot { cellAt(it).wasVisited }

    private fun step(lastVisited: Set<Position>): Set<Position> {
        return lastVisited.flatMap { nonVisitedNeighbors(it) }
            .onEach { cellAt(it).wasVisited = true }
            .toSet()
    }

    data class Cell(
        val height: Char,
        val isStart: Boolean = false,
        val isGoal: Boolean = false,
        var wasVisited: Boolean = false
    )

    data class Position(val x: Int, val y: Int) {
        fun neighbors() = setOf(copy(x = x - 1), copy(x = x + 1), copy(y = y - 1), copy(y = y + 1))
    }

    companion object {
        fun parse(input: List<String>): HeightGrid {
            val cells = input.map { line ->
                line.map {
                    when (it) {
                        'S' -> Cell('a', isStart = true, wasVisited = true)
                        'E' -> Cell('z', isGoal = true)
                        else -> Cell(it)
                    }
                }
            }
            return HeightGrid(cells)
        }
    }

}
