import kotlin.time.measureTimedValue

fun main() {
    //                       a list of Pair of InitialCoord(x,y) to Velocity (dx, dy)
    fun List<String>.parse():List<Pair<Pair<Long, Long>, Pair<Long, Long>>> =
        // line like p=2,0 v=2,-1
        // split by space, then split by equal, take second then split by comma
        this
            .map { line ->
                val (pPart, vPart) = line.split(' ')
                val (_,x,y) = pPart.split('=', ',')
                val (_,vx,vy) = vPart.split('=', ',')
                (x.toLong() to y.toLong()) to (vx.toLong() to vy.toLong())
            }


    fun part1(input:List<String>, lobbySize:Pair<Long, Long>):Long {
        val robotPos = input
            .parse()
            .map {
                val (pos, vel) = it
                val (x, y) = pos
                val (vx, vy) = vel
                val finalX = (((x + vx * 100L) % lobbySize.first) + lobbySize.first) % lobbySize.first // a modulus, not a remainder
                val finalY = (((y + vy * 100L) % lobbySize.second) + lobbySize.second) % lobbySize.second // ditto
                finalX to finalY
            }

        val midX = lobbySize.first / 2L
        val midY = lobbySize.second / 2L

        val quad1 = robotPos.count { it.first < midX && it.second < midY }.toLong()
        val quad2 = robotPos.count { it.first > midX && it.second < midY }.toLong()
        val quad3 = robotPos.count { it.first < midX && it.second > midY }.toLong()
        val quad4 = robotPos.count { it.first > midX && it.second > midY }.toLong()
       // println("$quad1 $quad2 $quad3 $quad4")
        return quad1 * quad2 * quad3 * quad4
    }

    fun part2(input:List<String>, lobbySize:Pair<Long, Long>):Long {
        var ret = 0L
        val data = input.parse()
        while (true) {
            ret++

            val robotPos = data
                .map {
                    val (pos, vel) = it
                    val (x, y) = pos
                    val (vx, vy) = vel
                    val finalX = (((x + vx * ret) % lobbySize.first) + lobbySize.first) % lobbySize.first // a modulus, not a remainder
                    val finalY = (((y + vy * ret) % lobbySize.second) + lobbySize.second) % lobbySize.second // ditto
                    finalX to finalY
                }
            if (robotPos.allUnique()) return ret
        }
    }

    fun timePart1(input:List<String>, lobbySize:Pair<Long, Long>):Long {
        val (ret, time) = measureTimedValue {
            part1(input, lobbySize)
        }
        //println("Time of part1 = $time")
        check(time.inWholeMilliseconds < 1000L)
        return ret
    }

    fun timePart2(input:List<String>, lobbySize:Pair<Long, Long>):Long {
        val (ret, time) = measureTimedValue {
            part2(input, lobbySize)
        }
        check(time.inWholeMilliseconds < 1000L)
        return ret
    }

    val testInput = """
        p=0,4 v=3,-3
        p=6,3 v=-1,-3
        p=10,3 v=-1,2
        p=2,0 v=2,-1
        p=0,0 v=1,3
        p=3,0 v=-2,-2
        p=7,6 v=-1,-3
        p=3,0 v=-1,-2
        p=9,3 v=2,3
        p=7,3 v=-1,2
        p=2,4 v=2,-3
        p=9,5 v=-3,-3
    """.trimIndent().lines()
    check(timePart1(testInput, 11L to 7L) == 12L)
    check(timePart2(testInput, 11L to 7L) == 1L)

//    // Read the input from the `src/Day14.txt` file.
    val input = readInput("Day14")
    timePart1(input, 101L to 103L).println()
    timePart2(input, 101L to 103L).println()
}
