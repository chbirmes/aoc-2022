fun main() {

    fun part1(input: List<String>): Long {
        val monkeys = input.filter { it.isNotEmpty() }
            .chunked(6) { Monkey.parse(it) }
        repeat(20) { monkeys.playRound(true) }
        return monkeys.map { it.inspectionCount }
            .sortedDescending()
            .let { it[0] * it[1] }
    }

    fun part2(input: List<String>): Long {
        val monkeys = input.filter { it.isNotEmpty() }
            .chunked(6) { Monkey.parse(it) }
        repeat(10_000) { monkeys.playRound(false) }
        return monkeys.map { it.inspectionCount }
            .sortedDescending()
            .let { it[0] * it[1] }
    }


    val testInput = readInput("Day11_test")
    check(part1(testInput) == 10605L)
    check(part2(testInput) == 2713310158)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}

private fun List<Monkey>.playRound(withCoolDown: Boolean) {
    val modulo = map { it.testDivisor }.fold(1L, Long::times)
    forEach { monkey ->
        monkey.items.forEach { item ->
            monkey.inspectionCount++
            item.worryLevel = monkey.operation.invoke(item.worryLevel)
            if (withCoolDown) {
                item.coolDown()
            }
            item.worryLevel %= modulo
            if (item.worryLevel % monkey.testDivisor == 0L) {
                this[monkey.trueTarget].items.add(item)
            } else {
                this[monkey.falseTarget].items.add(item)
            }
        }
        monkey.items.clear()
    }
}

private data class Item(var worryLevel: Long) {
    fun coolDown() {
        worryLevel /= 3
    }
}

private class Monkey(
    val items: MutableList<Item>,
    val operation: (Long) -> Long,
    val testDivisor: Long,
    val trueTarget: Int,
    val falseTarget: Int,
    var inspectionCount: Long = 0
) {

    companion object {
        fun parse(lines: List<String>): Monkey {
            val startingItems = lines[1]
                .substringAfter(": ")
                .split(", ")
                .map { Item(it.toLong()) }
            val operation: (Long) -> Long = lines[2]
                .substringAfter("new = old ")
                .split(" ")
                .let { tokens ->
                    { old ->
                        val operator: (Long, Long) -> Long = if (tokens[0] == "+") Long::plus else Long::times
                        val operand = if (tokens[1] == "old") old else tokens[1].toLong()
                        operator.invoke(old, operand)
                    }
                }
            val testDivisor = lines[3].substringAfter("by ").toLong()
            val trueTarget = lines[4].substringAfter("monkey ").toInt()
            val falseTarget = lines[5].substringAfter("monkey ").toInt()
            return Monkey(startingItems.toMutableList(), operation, testDivisor, trueTarget, falseTarget)
        }
    }

}
