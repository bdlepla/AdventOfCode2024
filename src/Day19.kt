import kotlin.time.measureTimedValue

fun main() {

    fun List<String>.parse():Pair<List<String>, List<String>> =
        first()
            .split(", ")
            .sortedBy { it.length } to drop(2)

    fun makeDesign(design: String, towels:List<String>, cache: MutableMap<String, Long> = mutableMapOf()): Long =
        if (design.isEmpty()) 1
        else cache.getOrPut(design) {
            towels
                .filter { towel -> design.startsWith(towel) }
                .sumOf { towel -> makeDesign(design.removePrefix(towel), towels, cache) }
        }

    fun part1(input:List<String>):Long {
        val (towels, designs) = input.parse()
        return designs.count{ design -> makeDesign(design, towels) > 0}.toLong()
    }

    fun part2(input:List<String>):Long {
        val (towels, designs) = input.parse()
        return designs.sumOf{ design -> makeDesign(design, towels)}
    }

    fun timePart1(input:List<String>):Long {
        val (ret, time) = measureTimedValue {
            part1(input)
        }
        check(time.inWholeMilliseconds < 1000L)
        return ret
    }

    fun timePart2(input:List<String>):Long {
        val (ret, time) = measureTimedValue {
            part2(input)
        }
        check(time.inWholeMilliseconds < 1000L)
        return ret
    }

    val testInput = """
        r, wr, b, g, bwu, rb, gb, br

        brwrr
        bggr
        gbbr
        rrbgbr
        ubwu
        bwurrg
        brgr
        bbrgwb
    """.trimIndent().lines()

    check(timePart1(testInput) == 6L)
    check(timePart2(testInput) == 16L)

    // Read the input from the `src/Day19.txt` file.
    val input = readInput("Day19")
    timePart1(input).println()
    timePart2(input).println()
}
