
fun main() {

    fun List<String>.parse():Pair<List<Coord>, Grid> {
        val zeroes = mutableListOf<Coord>()
        this.flatMapIndexed{ row, line ->
            line.mapIndexed { col, c ->
                if (c == '0') zeroes.add(row to col)
            }
        }
        return zeroes to this
    }

    fun Coord.getNextNeighbors(map:Grid):List<Coord> {
        val (row, col) = this
        val num = map.get(this).toString().toInt()
        return listOf(row-1 to col, row+1 to col, row to col-1, row to col+1)
            .filter{ map.isValid(it) && map.get(it).toString().toInt() == num+1 }
    }

    fun Coord.score(map:Grid):Int {
        var ret = 0

        val setAlreadySeen = mutableSetOf(this)
        val queue = mutableListOf(this)
        while (queue.isNotEmpty()) {
            val current = queue.removeLast()
            if (map.get(current) == '9')
            {
                ret++
                continue
            }

            queue.addAll(current.getNextNeighbors(map)
                .filter { setAlreadySeen.add(it) })
        }

        return ret
    }


    fun Coord.score2(map:Grid):Int {
        var ret = 0

        val queue = mutableListOf(this)
        while (queue.isNotEmpty()) {
            val current = queue.removeLast()
            if (map.get(current) == '9')
            {
                ret++
                continue
            }

            queue.addAll(current.getNextNeighbors(map))
        }

        return ret
    }

    fun part1(input:List<String>):Int {
        val (trailheads, map) = input.parse()
        return trailheads.sumOf { it.score(map) }
    }

    fun part2(input:List<String>):Int {
        val (trailheads, map) = input.parse()
        return trailheads.sumOf { it.score2(map) }
    }

    val testInput = """
        89010123
        78121874
        87430965
        96549874
        45678903
        32019012
        01329801
        10456732
    """.trimIndent().lines()
    check(part1(testInput) == 36)
    check(part2(testInput) == 81)

    // Read the input from the `src/Day10.txt` file.
    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
