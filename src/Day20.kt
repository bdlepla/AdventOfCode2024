import kotlin.math.abs
import kotlin.time.measureTimedValue

fun main() {

    fun List<String>.parse():Pair<Pair<Coord, Coord>, List<Coord>> {
        val walls = mutableListOf<Coord>()
        val start = mutableListOf<Coord>()
        val end = mutableListOf<Coord>()
        for (row in 0..< count()) {
            for (col in 0..< this[0].count()) {
                val coord = row to col
                val c = get(coord)
                when (c) {
                    'S' -> start.add(coord)
                    'E' -> end.add(coord)
                    '#' -> walls.add(coord)
                    else -> {}
                }
            }
        }
        return (start.first() to end.first()) to walls
    }

    fun Coord.getNeighbors():List<Coord> =
        listOf(
            first - 1 to second,
            first + 1 to second,
            first to second - 1,
            first to second + 1
        )

    fun Coord.manhattanDistance(other:Coord) = abs(first - other.first) + abs(second - other.second)

    fun buildMap(start:Coord, end:Coord, walls:List<Coord>):Map<Coord, Int> {
        val ret = mutableMapOf<Coord, Int>()
        val queue = PriorityQueue<Coord>()
        queue.add(start, 0)
        while (queue.isNotEmpty()) {
            val (curr, cost) = queue.pop()
            if (ret.containsKey(curr)) continue
            ret[curr] = cost
            if (curr == end) return ret
            for (neighbor in curr.getNeighbors()) {
                if (neighbor !in walls) {
                    queue.add(neighbor, cost+1)
                }
            }
        }

        throw Exception("No solution found for solve")
    }

    fun part1(input:List<String>, limit:Int):Long {
        val (startEnd, walls) = input.parse()
        val (start, end) = startEnd
        val distmap = buildMap(start, end, walls)
        return distmap
            .toList()
            .allTuples()
            .count { (a, b) ->
                val (p1, c1) = a
                val (p2, c2) = b
                p1.manhattanDistance(p2) == 2 && abs(c1 - c2) - 2 >= limit
            }
            .toLong()
    }

    fun part2(input:List<String>):Long {
        val (startEnd, walls) = input.parse()
        val (start, end) = startEnd
        val distmap = buildMap(start, end, walls)
        return distmap
            .toList()
            .allTuples()
            .count { (a, b) ->
                val (p1, c1) = a
                val (p2, c2) = b
                val skipSize = p1.manhattanDistance(p2)
                skipSize <= 20 && abs(c1 - c2) - skipSize >= 100
            }
            .toLong()
    }

    fun timePart1(input:List<String>, limit:Int):Long {
        val (ret, time) = measureTimedValue {
            part1(input, limit)
        }
        check(time.inWholeMilliseconds < 10000L)
        return ret
    }

    fun timePart2(input:List<String>):Long {
        val (ret, time) = measureTimedValue {
            part2(input)
        }
        check(time.inWholeMilliseconds < 10000L)
        return ret
    }

    val testInput = """
        ###############
        #...#...#.....#
        #.#.#.#.#.###.#
        #S#...#.#.#...#
        #######.#.#.###
        #######.#.#...#
        #######.#.###.#
        ###..E#...#...#
        ###.#######.###
        #...###...#...#
        #.#####.#.###.#
        #.#...#.#.#...#
        #.#.#.#.#.#.###
        #...#...#...###
        ###############
    """.trimIndent().lines()

//    check(timePart1(testInput, 1) == 382L)
//    check(timePart2(testInput) == 16L)

    // Read the input from the `src/Day20.txt` file.
    val input = readInput("Day20")
    timePart1(input, 100).println()
    timePart2(input).println()
}
