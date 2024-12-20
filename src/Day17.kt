import kotlin.time.measureTimedValue

fun main() {

    class CPU(regA:Long, regB:Long, regC:Long) {
        private var registerA = regA
        private var registerB = regB
        private var registerC = regC

        private fun Long.combo():Long =
            when (this) {
                in (0L..3L) -> this
                4L -> registerA
                5L -> registerB
                6L -> registerC
                else -> throw Exception("$this is not supported in combo")
            }

        fun run(program:List<Long>):List<Long> {
            val ret = mutableListOf<Long>()
            var instructionPointer = 0
            while (instructionPointer in 0L..< program.count().toLong()) {
                val opcode = program[instructionPointer]
                val operand = program[instructionPointer+1]
                instructionPointer += 2
                when (opcode) {
                    0L -> registerA = registerA shr operand.combo().toInt()
                    1L -> registerB = registerB xor operand
                    2L -> registerB = operand.combo() and 7
                    3L -> if (registerA != 0L) instructionPointer = operand.toInt()
                    4L -> registerB = registerB xor registerC
                    5L -> ret.add(operand.combo() and 7)
                    6L -> registerB = registerA shr operand.combo().toInt()
                    7L -> registerC = registerA shr operand.combo().toInt()
                    else -> throw Exception("Unknown opcode $opcode in run")
                }
            }
            return ret
        }
    }

    fun List<String>.parse():Pair<CPU, List<Long>> {
        val (regA, regB, regC) =  take(3)
            .map { it.split(':') }
            .map { (_, s) -> s.trim().toLong()}

        val (_, listOfNum) = this[4].split(':')
        val program = listOfNum.trim().split(',').map{it.toLong()}

        return CPU(regA, regB, regC) to program
    }

    fun part1(input:List<String>):String {
        val (cpu, program) = input.parse()
        return cpu.run(program).joinToString(",")
    }

    fun findAMatchingOutput(program: List<Long>, target: List<Long>): Long {
        var aStart = if (target.size == 1) 0
                else 8 * findAMatchingOutput(program, target.drop(1))

        while(target !=  CPU(aStart, 0, 0).run(program)) {
            aStart++
        }

        return aStart
    }

    fun part2(input:List<String>):Long {
        val (_, program) = input.parse()
        return findAMatchingOutput(program, program)
    }

    fun timePart1(input:List<String>):String {
        val (ret, time) = measureTimedValue {
            part1(input)
        }
        check(time.inWholeMilliseconds < 1000L)
        return ret
    }

    fun timePart1b(input:List<String>):String {
        val (ret, time) = measureTimedValue {
            part1(input)
        }
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
        Register A: 729
        Register B: 0
        Register C: 0
        
        Program: 0,1,5,4,3,0
    """.trimIndent().lines()

    val testInput2 = """
        Register A: 117440
        Register B: 0
        Register C: 0

        Program: 0,3,5,4,3,0
    """.trimIndent().lines()

    check(timePart1(testInput) == "4,6,3,5,6,3,5,2,1,0")
    check(timePart1b(testInput2) == "0,3,5,4,3,0")
    check(timePart2(testInput2) == 117440L)

    // Read the input from the `src/Day17.txt` file.
    val input = readInput("Day17")
    timePart1(input).println()
    timePart2(input).println()
}
