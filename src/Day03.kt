
fun main() {

    fun List<String>.extractValidLines(filterForDoAndDoNot: Boolean): List<String> {
        var canDo = true
        return this.
            filter {  !filterForDoAndDoNot ||
                        when (it) {
                            "don't()" -> {canDo = false; false}
                            "do()" -> {canDo = true; false}
                            else -> canDo
                        }
            }
    }

    fun List<String>.parse(regex:Regex, filterForDoAndDoNot:Boolean=false):List<Pair<Long, Long>> =
        this
            .flatMap { line -> regex.findAll(line).map{ it.groups[1]!!.value } }
            .extractValidLines(filterForDoAndDoNot)
            .map {
                val numbers = it.substring(4).dropLast(1).split(',')
                numbers[0].toLong() to numbers[1].toLong()
            }


    // look for multiple "mul(##,##)" on each input line
    val regex1 = Regex(".*?(mul\\(\\d+,\\d+\\))(?:(?!mul).)*")
    fun part1(input: List<String>) = input.parse(regex1, false).sumOf { (a,b)-> a*b }

    // look for multiple "mul(##,##)"'" or "'"do()'"' or "don't()" on each input line
    val regex2 = Regex(".*?(mul\\(\\d+,\\d+\\)|don't\\(\\)|do\\(\\))(?:(?!mul)(?!do)(?!don't).)*")
    fun part2(input: List<String>) = input.parse(regex2, true).sumOf{ (a,b)-> a*b }

    val testInput = """
        xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))
    """.trimIndent().lines()
    check(part1(testInput) == 161L)
    check(part2(testInput) == 48L)

    // Read the input from the `src/Day03.txt` file.
    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
