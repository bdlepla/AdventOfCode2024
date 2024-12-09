
fun main() {
    fun List<String>.parse():List<Pair<Long, List<Long>>> =
        // a number followed by a colon followed by numbers separated by space
        this.map {
            val (test, theRest) = it.split(": ")
            test.toLong() to theRest.split(' ').map{it.toLong()}
        }

    fun Int.raisedToPower(num:Int):Int = (0..<num).fold(1){a, _ -> a * this}

    // operations is bit coded: 0 for add, 1 for multiply
    fun List<Long>.calculate(operations:Int):Long =
        this.drop(1).fold(this[0] to operations) { (acc, op), e ->
            when(op.rem(2)) {
                0 -> acc + e
                else -> acc * e
             } to op/2
        }.first

    fun Pair<Long, List<Long>>.canBeSolved():Boolean {
        val (testValue, operands) = this
        val numberOfTests = operands.count()-1
        return (0..<2.raisedToPower(numberOfTests))
            .any{testValue == operands.calculate(it)}
    }


    // operations is a combination of 3: 0 = add, 1 = multiply, 2 = concat
    fun List<Long>.calculate2(operations:Int):Long =
        this.drop(1).fold(this[0] to operations) { (acc, op), e ->
            when(op.rem(3)) {
                0 -> acc + e
                1 -> acc * e
                else -> (acc.toString() + e.toString()).toLong()
            } to op/3
        }.first

    fun Pair<Long, List<Long>>.canBeSolvedWithConcat():Boolean {
        if (this.canBeSolved()) return true

        val (testValue, operands) = this
        val numberOfTests = operands.count()-1
        return (0..<3.raisedToPower(numberOfTests))
            .any{testValue == operands.calculate2(it)}

}

    fun part1(input:Grid):Long = input.parse().filter{it.canBeSolved()}.sumOf{it.first}

    fun part2(input:Grid):Long = input.parse()
        .filter{it.canBeSolvedWithConcat()}
        .sumOf{it.first}

    val testInput = """
        190: 10 19
        3267: 81 40 27
        83: 17 5
        156: 15 6
        7290: 6 8 6 15
        161011: 16 10 13
        192: 17 8 14
        21037: 9 7 18 13
        292: 11 6 16 20
    """.trimIndent().lines()
    check(part1(testInput) == 3749L)
    check(part2(testInput) == 11387L)

    // Read the input from the `src/Day07.txt` file.
    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
