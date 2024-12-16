import kotlin.time.measureTimedValue

fun main() {

    data class ButtonPrizeInfo(
        val btnA:Pair<Long, Long>,
        val btnB:Pair<Long, Long>,
        val prize:Pair<Long, Long>)


    fun List<String>.parse(part2:Boolean):List<ButtonPrizeInfo> {
        return this.chunked(4)
            .map{
                val (_, axs, ays) = it[0].split(':', ',')
                val (_, ax) = axs.split('+')
                val (_, ay) = ays.split('+')
                val (_, bxs, bys) = it[1].split(':', ',')
                val (_, bx) = bxs.split('+')
                val (_, by) = bys.split('+')
                val (_, pxs, pys) = it[2].split(':', ',')
                var (_, px) = pxs.split('=')
                var (_, py) = pys.split('=')
                var npx = px.toLong()
                var npy = py.toLong()
                if (part2) {
                    npx += 10000000000000
                    npy += 10000000000000
                }

                ButtonPrizeInfo(
                    ax.toLong() to ay.toLong(),
                    bx.toLong() to by.toLong(),
                    npx to npy
                )
            }
    }

    fun ButtonPrizeInfo.play():Long {
        val numA = (prize.first*btnB.second-prize.second*btnB.first)/
                (btnA.first*btnB.second-btnA.second*btnB.first)
        val numB = (prize.first - numA*btnA.first)/btnB.first
        val solution = numA*btnA.first + numB*btnB.first to
            numA*btnA.second+numB*btnB.second
        return (if (solution==prize) 3L*numA + numB else 0L)
    }

    fun part1(input:List<String>):Long {
        return input
            .parse(false)
            .sumOf {it.play()}
    }

    fun part2(input:List<String>):Long {
        return input
            .parse(true)
            .sumOf {it.play()}
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
        Button A: X+94, Y+34
        Button B: X+22, Y+67
        Prize: X=8400, Y=5400
        
        Button A: X+26, Y+66
        Button B: X+67, Y+21
        Prize: X=12748, Y=12176
        
        Button A: X+17, Y+86
        Button B: X+84, Y+37
        Prize: X=7870, Y=6450
        
        Button A: X+69, Y+23
        Button B: X+27, Y+71
        Prize: X=18641, Y=10279
    """.trimIndent().lines()
    check(timePart1(testInput) == 480L)
    check(timePart2(testInput) == 875318608908L)

    // Read the input from the `src/Day13.txt` file.
    val input = readInput("Day13")
    timePart1(input).println()
    timePart2(input).println()
}
