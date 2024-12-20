import kotlin.time.measureTimedValue

fun main() {

    fun Coord.toRowCol() = this.second to this.first
    fun Coord.toXandY() = toRowCol()

    fun List<String>.parse():List<Coord> =
        this
            .map { it.split(',') }
            .map { (x, y) -> x.toInt() to y.toInt()}
            .map { it.toRowCol() }

    fun Coord.getNeighbors():List<Coord> {
        val (row, col) = this
        return listOf(
            row-1 to col,
            row+1 to col,
            row to col-1,
            row to col+1
        )
    }

    fun runSimulation(badRam: List<Coord>, size:Pair<Int, Int>):Long {
        val (numRows, numCols) = size

        val start = 0 to 0
        val end = size.first-1 to size.second-1
        val queue = PriorityQueue<Coord>()
        queue.add(start, 0)
        val alreadySeen = mutableSetOf<Coord>()
        while (queue.isNotEmpty()) {
            val (coord, cost) = queue.pop()
            if (coord == end) return cost.toLong()
            val validNeighbors = coord.getNeighbors()
                .filter { it !in badRam}
                .filter { (row, col) -> row in 0..< numRows && col in 0..< numCols }
                .filter { alreadySeen.add(it) }
            for (neighbor in validNeighbors) {
                    queue.add(neighbor, cost+1)
            }
        }
        return -1L
    }

    fun part1(input:List<String>, steps:Int, size:Pair<Int, Int>):Long {
        val badRam = input.parse().take(steps)
        return runSimulation(badRam, size)
    }

    fun part2(input:List<String>, size:Pair<Int, Int>):Coord {
        var badRam = input.parse()
        // binary search for the start != -1 and end == -1
        var startStep = 0
        var endStep = badRam.count()-1
        while (endStep != startStep+1) {
            val midStep = (startStep + endStep) / 2
            val ret = runSimulation(badRam.take(midStep+1), size)
            if (ret == -1L) endStep = midStep
            else startStep = midStep
        }
        return badRam[endStep].toXandY()
    }

    fun timePart1(input:List<String>, steps:Int, size:Pair<Int, Int>):Long {
        val (ret, time) = measureTimedValue {
            part1(input, steps, size)
        }
        check(time.inWholeMilliseconds < 1000L)
        return ret
    }

    fun timePart2(input:List<String>, size:Pair<Int, Int>):Coord {
        val (ret, time) = measureTimedValue {
            part2(input, size)
        }
        check(time.inWholeMilliseconds < 1000L)
        return ret
    }

    val testInput = """
        5,4
        4,2
        4,5
        3,0
        2,1
        6,3
        2,4
        1,5
        0,6
        3,3
        2,6
        5,1
        1,2
        5,5
        2,5
        6,5
        1,4
        0,4
        6,4
        1,1
        6,1
        1,0
        0,5
        1,6
        2,0
    """.trimIndent().lines()

    check(timePart1(testInput, 12, 7 to 7) == 22L)
    check(timePart2(testInput, 7 to 7) == 6 to 1)

    // Read the input from the `src/Day18.txt` file.
    val input = readInput("Day18")
    timePart1(input, 1024, 71 to 71).println()
    timePart2(input, 71 to 71).println()
}
