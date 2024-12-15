import kotlin.math.log10
import kotlin.time.measureTimedValue

fun main() {

    fun ULong.numberOfDigits(): Int = (log10(toDouble())).toInt() + 1

    fun ULong.hasEvenNumberOfDigits(): Boolean = numberOfDigits() % 2 == 0

    fun ULong.split(): Pair<ULong, ULong> {
        val halfNumDigits = numberOfDigits() / 2
        val powersOfTens = Math.pow(10.toDouble(), halfNumDigits.toDouble()).toLong().toULong()
        val first = this / powersOfTens
        val second = this % powersOfTens
        return first to second
    }

    fun MutableMap<ULong, ULong>.add(num: ULong, count: ULong) {
        this[num] = getOrDefault(num, 0UL) + count
    }


    fun List<Pair<ULong, ULong>>.blink():List<Pair<ULong, ULong>> =
        fold(mutableMapOf<ULong, ULong>()) { memo, (num, count) ->

            if (num == 0UL) memo.add(1UL, count)
            else if (num.hasEvenNumberOfDigits())
                num.split().toList().map{memo.add(it, count)}
            else memo.add(num*2024UL, count)

            memo
        }.toList()

    fun List<String>.parse():List<Pair<ULong, ULong>> =
        this[0].split(' ').map{ it.toULong() to 1UL}

    fun List<String>.perform(repeatNum:Int):ULong =
        (0..<repeatNum).fold(this.parse()){ d, _ -> d.blink() }.sumOf { it.second }

    fun part1(input:List<String>):ULong = input.perform(25)

    fun part2(input:List<String>):ULong = input.perform(75)

    fun timePart1(input:List<String>):ULong {
        val (ret, time) = measureTimedValue {
            part1(input)
        }
        check(time.inWholeMilliseconds < 1000L)
        return ret
    }

    fun timePart2(input:List<String>):ULong {
        val (ret, time) = measureTimedValue {
            part2(input)
        }
        check(time.inWholeMilliseconds < 1000L)
        return ret
    }

    val testInput = """
        125 17
    """.trimIndent().lines()
    check(timePart1(testInput) == 55_312UL)
    check(timePart2(testInput) == 65_601_038_650_482UL)

//    // Read the input from the `src/Day11.txt` file.
    val input = readInput("Day11")
    timePart1(input).println()
    timePart2(input).println()
}
