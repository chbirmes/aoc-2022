fun main() {

    fun part1(input: List<String>): Int =
        input.asSequence()
            .filter { it.isNotEmpty() }
            .map { PacketElement.ElementList.parse(it) }
            .chunked(2)
            .map { Pair(it[0], it[1]) }
            .mapIndexed { index, pair -> if (pair.first < pair.second) index + 1 else 0 }
            .sum()

    fun part2(input: List<String>): Int {
        val divider1 = PacketElement.ElementList.parse("[[2]]")
        val divider2 = PacketElement.ElementList.parse("[[6]]")
        val elementLists = input.asSequence()
            .filter { it.isNotEmpty() }
            .map { PacketElement.ElementList.parse(it) } + divider1 + divider2
        return elementLists.sorted()
            .let { (it.indexOf(divider1) + 1) * (it.indexOf(divider2) + 1) }
    }


    val testInput = readInput("Day13_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 140)

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}

sealed class PacketElement : Comparable<PacketElement> {

    override operator fun compareTo(other: PacketElement): Int {
        return when (this) {
            is ElementList -> when (other) {
                is ElementList -> {
                    list.asSequence().zip(other.list.asSequence()) { a, b -> a.compareTo(b) }
                        .firstOrNull { it != 0 }
                        ?: list.size.compareTo(other.list.size)
                }

                is SingleInt -> compareTo(ElementList(mutableListOf(other)))
            }

            is SingleInt -> when (other) {
                is ElementList -> ElementList(mutableListOf(this)).compareTo(other)
                is SingleInt -> i.compareTo(other.i)
            }
        }
    }

    data class SingleInt(val i: Int) : PacketElement()

    data class ElementList(val list: MutableList<PacketElement> = mutableListOf()) : PacketElement() {

        var parent: ElementList? = null

        companion object {
            fun parse(s: String): ElementList {
                val root = ElementList()
                parseStep(s.substring(1, s.length - 1), root)
                return root
            }

            private tailrec fun parseStep(s: String, current: ElementList) {
                if (s.isNotEmpty()) {
                    val (rest, newCurrent) = when (s.first()) {
                        '[' -> {
                            val newList = ElementList()
                            newList.parent = current
                            current.list.add(newList)
                            Pair(s.drop(1), newList)
                        }

                        ']' -> Pair(s.drop(1), current.parent!!)
                        ',' -> Pair(s.drop(1), current)
                        else -> {
                            val number = s.takeWhile { it.isDigit() }
                            current.list.add(SingleInt(number.toInt()))
                            Pair(s.substringAfter(number), current)
                        }
                    }
                    parseStep(rest, newCurrent)
                }
            }
        }
    }
}
