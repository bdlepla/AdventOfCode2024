import kotlin.math.abs
import kotlin.math.sign

fun main() {

    // each line is a list of numbers separated by a single space
    fun parse(input:List<String>) = input.map{ line -> line.split(' ').map{ num -> num.toInt() }}

    fun isSafe(levels:List<Int>):Boolean {
        val (signs, magnitudes) = levels
            .zipWithNext{ a, b -> a-b }
            .map{ it.sign to abs(it) }
            .unzip()

        return signs.unanimous() && magnitudes.all{ it in 1 ..3 }
    }

    fun part1(input: List<String>) = parse(input).count { isSafe(it) }

    fun canBeMadeSafe(levels:List<Int>) = isSafe(levels) ||
        (0..<levels.count())
            .asSequence()
            .map { levels.take(it)+levels.drop(it+1) } // remove 'it' index from levels
            .any { isSafe(it) }


    fun part2(input: List<String>) = parse(input).count { canBeMadeSafe(it) }

    val testInput = """
        7 6 4 2 1
        1 2 7 8 9
        9 7 6 2 1
        1 3 2 4 5
        8 6 4 4 1
        1 3 6 7 9
    """.trimIndent().lines()
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    // Read the input from the `src/Day02.txt` file.
    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
