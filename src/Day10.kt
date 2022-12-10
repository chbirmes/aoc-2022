import kotlin.math.absoluteValue

fun main() {

    fun part1(input: List<String>): Int =
        input.fold(listOf(CpuState())) { stateList, instruction ->
            stateList + stateList.last().processInstruction(instruction)
        }
            .asSequence()
            .mapIndexed { index, cpuState -> (index + 1) * cpuState.registerValue }
            .filterIndexed { index, _ -> (index + 1) % 40 == 20 }
            .sum()

    fun part2(input: List<String>): String =
        input.fold(listOf(CpuState())) { stateList, instruction ->
            stateList + stateList.last().processInstruction(instruction)
        }
            .mapIndexed { index, cpuState ->
                val crtPosition = index % 40
                if ((crtPosition - cpuState.registerValue).absoluteValue <= 1)
                    '#'
                else
                    '.'
            }
            .dropLast(1)
            .chunked(40)
            .joinToString(separator = "\n") { it.joinToString(separator = "") }


    val testInput = readInput("Day10_test")
    check(part1(testInput) == 13140)
    check(
        part2(testInput) == """
        ##..##..##..##..##..##..##..##..##..##..
        ###...###...###...###...###...###...###.
        ####....####....####....####....####....
        #####.....#####.....#####.....#####.....
        ######......######......######......####
        #######.......#######.......#######.....
    """.trimIndent()
    )

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}

@JvmInline
private value class CpuState(val registerValue: Int = 1) {
    fun processInstruction(instruction: String) =
        when (instruction) {
            "noop" -> listOf(CpuState(registerValue))
            else -> listOf(CpuState(registerValue), CpuState(registerValue + instruction.drop(5).toInt()))
        }
}