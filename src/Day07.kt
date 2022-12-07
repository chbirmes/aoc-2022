fun main() {

    fun part1(input: List<String>): Int {
        val state = DeviceState().also { it.processInput(input) }
        return state.root.recursiveDirectories()
            .map { it.size() }
            .filter { it <= 100_000 }
            .sum()
    }

    fun part2(input: List<String>): Int {
        val state = DeviceState().also { it.processInput(input) }
        val minSizeToDelete = 30_000_000 - (70_000_000 - state.root.size())
        return state.root.recursiveDirectories()
            .map { it.size() }
            .filter { it >= minSizeToDelete }
            .min()
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437)
    check(part2(testInput) == 24933642)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}

private class DeviceState() {
    val root = Directory("/", null)
    var currentDirectory = root

    class Directory(val name: String, var parent: Directory?) {
        val files = mutableListOf<Int>()
        val subdirectories = mutableListOf<Directory>()

        fun size(): Int = files.sum() + subdirectories.sumOf { it.size() }

        fun recursiveDirectories(): Sequence<Directory> =
            sequenceOf(this) + subdirectories.asSequence().flatMap { it.recursiveDirectories() }
    }

    fun processInput(input:List<String>) {
        input.forEach { line ->
            if (line.startsWith("$")) {
                line.drop(2).split(" ").let { command ->
                    if (command[0] == "cd") {
                        changeDirectory(command[1])
                    }
                }
            } else {
                processLsLine(line)
            }
        }
    }

    fun changeDirectory(directory: String) {
        currentDirectory = when (directory) {
            "/" -> root
            ".." -> currentDirectory.parent
            else -> currentDirectory.subdirectories.find { it.name == directory }
        } ?: currentDirectory
    }

    fun processLsLine(line: String) {
        line.split(" ", limit = 2)
            .let {
                it[0].toIntOrNull()?.let { size -> currentDirectory.files.add(size) }
                    ?: currentDirectory.subdirectories.add(
                        Directory(it[1], currentDirectory)
                    )
            }
    }

}

