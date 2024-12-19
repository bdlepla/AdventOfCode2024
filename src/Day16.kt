import kotlin.time.measureTimedValue

fun main() {

    fun Grid.findStartEnd():Pair<Coord, Coord> {
        var start = 0 to 0
        var end = 0 to 0
        for (row in 0..< count()) {
            for (col in 0..< this[0].count()) {
                val coord = row to col
                if (get(coord) == 'S')
                    start = coord
                else if (get(coord) == 'E')
                    end = coord
            }
        }
        return start to end
    }

    fun Coord.getValidNeighbors(map:Grid, dir:Direction):List<Triple<Coord, Direction, Int>> {
        val (row, col) = this
        val neighbors = when (dir) {
            Direction.North -> {
                listOf (
                    Triple(row-1 to col, Direction.North, 1),
                    Triple(this, Direction.West, 1000),
                    Triple(this, Direction.East, 1000)
                )
            }
            Direction.South -> {
                listOf(
                    Triple(row+1 to col, Direction.South, 1),
                    Triple(this, Direction.West, 1000),
                    Triple(this, Direction.East, 1000)
                )
            }
            Direction.East -> {
                listOf(
                    Triple(row to col+1, Direction.East, 1),
                    Triple(this, Direction.North, 1000),
                    Triple(this, Direction.South, 1000)
                )
            }
            Direction.West -> {
                listOf(
                    Triple(row to col-1, Direction.West, 1),
                    Triple(this, Direction.North, 1000),
                    Triple(this, Direction.South, 1000)
                )
            }
            else -> throw Exception("$dir is not used in this puzzle.")
        }

        return neighbors.filter { map.get(it.first) != '#'}
    }

    fun part1(input:List<String>):Long {
        val (start, end) = input.findStartEnd()
        val queue = PriorityQueue<Pair<Coord, Direction>>()
        queue.add(start to  Direction.East, 0)
        val shortest = mutableMapOf<Pair<Coord, Direction>, Int>()

        while (queue.isNotEmpty()) {
            val (data, cost) = queue.pop()
            val (coord, dir) = data
            if (coord == end) return cost.toLong()
            val cachedShortest = shortest.getOrDefault(data, Int.MAX_VALUE )
            if (cachedShortest < cost) continue
            shortest[data] = cost

            for ((nextCoord, nextDir, nextCost) in coord.getValidNeighbors(input, dir)) {
                queue.add(nextCoord to nextDir, cost+nextCost)
            }
        }

        throw Exception("No solution for part 1.")
    }

    fun part2(input:List<String>):Long {
        val (start, end) = input.findStartEnd()
        val queue = PriorityQueue<Triple<Coord, Direction, List<Coord>>>()
        queue.add(Triple(start, Direction.East, listOf(start)), 0)
        val shortest = mutableMapOf<Pair<Coord, Direction>, Int>()
        var least = Int.MAX_VALUE
        val bestSeats = mutableSetOf(start)
        bestSeats.add(end)

        while (queue.isNotEmpty()) {
            val (data, cost) = queue.pop()
            val (coord, dir, previous) = data
            if (coord == end) {
                if (cost > least) {
                    break
                }
                least = cost
                bestSeats.addAll(previous)
                continue
            }

            val cachedShortest = shortest.getOrDefault(coord to dir, Int.MAX_VALUE )
            if (cachedShortest < cost) continue
            shortest[coord to dir] = cost

            for ((nextCoord, nextDir, nextCost) in coord.getValidNeighbors(input, dir)) {
                queue.add(Triple(nextCoord, nextDir, previous.toMutableList()+nextCoord), cost+nextCost)
            }
        }

//        println("")
//        for (row in 0..< input.count()) {
//            for (col in 0 ..< input[0].count()) {
//                val coord = row to col
//                if (coord in bestSeats) print('O')
//                else print(input.get(coord))
//            }
//            println("")
//        }
//        println("")

        return bestSeats.count().toLong()
    }

    fun timePart1(input:List<String>):Long {
        val (ret, time) = measureTimedValue {
            part1(input)
        }
        //println("Time of part1 = $time")
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
        ###############
        #.......#....E#
        #.#.###.#.###.#
        #.....#.#...#.#
        #.###.#####.#.#
        #.#.#.......#.#
        #.#.#####.###.#
        #...........#.#
        ###.#.#####.#.#
        #...#.....#.#.#
        #.#.#.###.#.#.#
        #.....#...#.#.#
        #.###.#.#.#.#.#
        #S..#.....#...#
        ###############
    """.trimIndent().lines()

    val testInputB = """
        #################
        #...#...#...#..E#
        #.#.#.#.#.#.#.#.#
        #.#.#.#...#...#.#
        #.#.#.#.###.#.#.#
        #...#.#.#.....#.#
        #.#.#.#.#.#####.#
        #.#...#.#.#.....#
        #.#.#####.#.###.#
        #.#.#.......#...#
        #.#.###.#####.###
        #.#.#...#.....#.#
        #.#.#.#####.###.#
        #.#.#.........#.#
        #.#.#.#########.#
        #S#.............#
        #################
    """.trimIndent().lines()

    check(timePart1(testInput) == 7036L)
    check(timePart1(testInputB) == 11048L)
    check(timePart2(testInput) == 45L)
    check(timePart2(testInputB) == 64L)

    // Read the input from the `src/Day16.txt` file.
    val input = readInput("Day16")
    timePart1(input).println() // 72432
    timePart2(input).println()
}
