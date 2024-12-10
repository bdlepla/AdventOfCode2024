
fun main() {

    fun Grid.findFrequencies():Pair<List<Char>, List<Pair<Char, Coord>>> {
        val positions = this
            .flatMapIndexed { row, line ->
                line.mapIndexed { col, c -> c to (row to col) }
            }
            .filter { it.first != '.' }

        val frequencies = positions.map { it.first }.distinct()
        return frequencies to positions
    }

    operator fun Coord.minus(other:Coord):Coord =
        (this.first - other.first) to (this.second - other.second)

    operator fun Coord.plus(other:Coord):Coord =
        (this.first + other.first) to (this.second + other.second)

    fun List<Coord>.calculateAntinodes():List<Coord> =
        this.allTuples().flatMap {
            val diff = it.second - it.first
            val first = it.first - diff
            val second = it.second + diff
            listOf(first, second)
        }

    fun part1(input:Grid):Long {
        val numRows = input.count()
        val numCols = input[0].count()
        fun isValid(coord:Coord):Boolean {
            return coord.first in 0..<numRows && coord.second in 0..<numCols
        }
        val (frequencies, positions) = input.findFrequencies()
        return frequencies.flatMap { frequency ->
                positions
                    .filter { it.first == frequency }
                    .map { it.second }
                    .calculateAntinodes()
                    .filter { isValid(it) }
            }
            .distinct().count().toLong()
    }

    fun part2(input:Grid):Long {
        val numRows = input.count()
        val numCols = input[0].count()
        fun isValid(coord:Coord):Boolean {
            return coord.first in 0..<numRows && coord.second in 0..<numCols
        }
        val (frequencies, positions) = input.findFrequencies()
        return frequencies.flatMap { frequency ->
            val antinodes = positions
                .filter { it.first == frequency }
                .map { it.second }
                .allTuples()
                .flatMap {
                    val ret = mutableListOf<Coord>()
                    val diff = it.second - it.first
                    var beforeFirst = it.first
                    while (isValid(beforeFirst) ) {
                        ret.add(beforeFirst)
                        beforeFirst -= diff
                    }
                    var afterSecond = it.second
                    while (isValid(afterSecond)) {
                        ret.add(afterSecond)
                        afterSecond += diff
                    }
                    ret
                }
            antinodes
        }.distinct().count().toLong()
    }

    val testInput = """
        ............
        ........0...
        .....0......
        .......0....
        ....0.......
        ......A.....
        ............
        ............
        ........A...
        .........A..
        ............
        ............
    """.trimIndent().lines()
    check(part1(testInput) == 14L)
    check(part2(testInput) == 34L)

    // Read the input from the `src/Day08.txt` file.
    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
