import kotlin.math.abs

fun main() {

    fun parse(input:List<String>): Pair<List<Int>, List<Int>> {
        // each line on input is 2 numbers separated by white space
        val regExp = Regex("(\\d+)\\s+(\\d+)")
        return input
            .map { regExp.matchEntire(it)!!.groups }
            .map { it[1]!!.value.toInt() to it[2]!!.value.toInt() }
            .unzip()
    }

    fun part1(input: List<String>): Int {
        val (firstList, secondList) = parse(input)
        return firstList.sorted()
            .zip(secondList.sorted())
            .sumOf { (first, second) -> abs(first - second) }
    }

    fun part2(input: List<String>): Int {
        val (firstList, secondList) = parse(input)
        return firstList
            .sumOf { first -> first * secondList.count { it == first } }
    }

    val testInput = """
        3   4
        4   3
        2   5
        1   3
        3   9
        3   3
    """.trimIndent().lines()
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
