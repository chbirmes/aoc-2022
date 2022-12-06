import java.util.Stack

fun main() {

    fun part1(input: String): String {
        val split = input.split("\n\n")
        val cargoBay = CargoBay.parse(split[0])
        val instructionRegex = "move (\\d+) from (\\d+) to (\\d+)".toRegex()
        split[1].lines().forEach { line ->
            val numbers = instructionRegex.find(line)!!
                .groupValues
                .drop(1)
                .map { it.toInt() }
            cargoBay.moveSingleCrates(numbers[0], numbers[1], numbers[2])
        }
        return cargoBay.topCrates()
    }


    fun part2(input: String): String {
        val split = input.split("\n\n")
        val cargoBay = CargoBay.parse(split[0])
        val instructionRegex = "move (\\d+) from (\\d+) to (\\d+)".toRegex()
        split[1].lines().forEach { line ->
            val numbers = instructionRegex.find(line)!!
                .groupValues
                .drop(1)
                .map { it.toInt() }
            cargoBay.moveStackOfCrates(numbers[0], numbers[1], numbers[2])
        }
        return cargoBay.topCrates()
    }


    val testInput = readInputAsString("Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInputAsString("Day05")
    println(part1(input))
    println(part2(input))
}

private class CargoBay(val stacks: List<Stack<Char>>) {

    fun moveSingleCrates(amount: Int, source: Int, target: Int) {
        repeat(amount) {
            stacks[target - 1].push(stacks[source - 1].pop())
        }
    }

    fun moveStackOfCrates(amount: Int, source: Int, target: Int) {
        val temp = Stack<Char>()
        repeat(amount) {
            temp.push(stacks[source - 1].pop())
        }
        repeat(amount) {
            stacks[target - 1].push(temp.pop())
        }
    }

    fun topCrates() =
        stacks
            .map { it.peek() }
            .joinToString(separator = "")

    companion object {
        fun parse(s: String): CargoBay {
            val lines = s.lines()
            val numberOfStacks = lines.last().last().digitToInt()
            val stacks = buildList {
                repeat(numberOfStacks) { add(Stack<Char>()) }
            }
            lines.dropLast(1).asReversed().forEach { line ->
                stacks.forEachIndexed { index, stack ->
                    val positionInString = index * 4 + 1
                    line.elementAtOrNull(positionInString)?.let {
                        if (it.isLetter()) {
                            stack.push(it)
                        }
                    }
                }
            }
            return CargoBay(stacks)
        }
    }

}
