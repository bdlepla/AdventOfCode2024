import kotlin.math.abs
import kotlin.time.measureTimedValue

fun main() {

    data class Parsed(val robot:Coord, val boxes:MutableList<Coord>, val walls:List<Coord>, val moves:List<Char>, val mapSize:Pair<Int, Int>)
    data class Parsed2(val robot:Coord, val boxes:MutableList<Pair<Coord, Char>>, val walls:List<Coord>, val moves:List<Char>, val mapSize:Pair<Int, Int>)

    fun List<String>.parse():Parsed {
        val topPart = takeWhile { it.isNotEmpty() }
        val map = topPart.flatMapIndexed { row, line ->
            line.mapIndexed { col, c -> c to (row to col) }
        }
        val robot = map.first { it.first == '@' }.second
        val boxes = map.filter { it.first == 'O' }.map { it.second }.toMutableList()
        val walls = map.filter { it.first == '#' }.map { it.second }
        val moves = drop(topPart.count()+1).joinToString("").toList()
        val mapSize = topPart.count() to topPart[0].count()
        return Parsed(robot, boxes, walls, moves, mapSize)
    }

    fun List<String>.parse2():Parsed2 {
        val topPart = takeWhile { it.isNotEmpty() }
        val robots = mutableListOf<Coord>()
        val boxes = mutableListOf<Pair<Coord, Char>>()
        val walls = mutableListOf<Coord>()
        topPart.forEachIndexed { row, line ->
            line.forEachIndexed { col, c ->
                when (c) {
                    '@' -> robots.add(row to col*2)
                    '#' -> {
                        walls.add(row to col*2)
                        walls.add(row to (col*2)+1)
                    }
                    'O' -> {
                        boxes.add((row to col*2) to '[')
                        boxes.add((row to (col*2)+1) to ']')
                    }
                }
            }
        }
        val robot = robots.first()
        val moves = drop(topPart.count()+1).joinToString("").toList()
        val mapSize = topPart.count() to topPart[0].count()*2
        return Parsed2(robot, boxes, walls, moves, mapSize)
    }

    fun Coord.move(move:Char):Coord {
        val (row, col) = this
        return when (move) {
            '<' -> row to col-1
            '>' -> row to col+1
            'v' -> row+1 to col
            '^' -> row-1 to col
            else -> throw Exception("Unknown move char '$move'")
        }
    }

    tailrec fun findEnd(point:Coord, originalPoint:Coord, move:Char, boxes:List<Coord>, walls:List<Coord>):Coord {
        val nextPoint = point.move(move)
        if (nextPoint in walls) return originalPoint
        if (nextPoint !in boxes) return nextPoint
        return findEnd(nextPoint, originalPoint, move, boxes, walls)
    }

    fun Coord.move(move:Char, boxes:MutableList<Coord>, walls:List<Coord>):Coord {
        val end = findEnd(this, this, move, boxes, walls )
        if (this == end) return this // robot could not move
        val dist = abs(end.second - this.second) + abs(end.first - this.first)
        // dist == 1 is no boxes moved;
        val dest = this.move(move)
        if (dist != 1) {
            // moving boxes; only need to move box at dest to space at end
            boxes.remove(dest)
            boxes.add(end)
        }
        return dest
    }

    fun canMoveInto(point:Coord, move:Char, boxes:MutableList<Pair<Coord, Char>>, walls:List<Coord>):Boolean {
        if (point in walls) return false
        val boxesAtPoint = boxes.filter { it.first == point }
        if (boxesAtPoint.isEmpty()) return true
        if (move == '>' || move == '<') return canMoveInto(point.move(move), move, boxes, walls)
        val (left, right) = if (boxesAtPoint[0].second == '[') {
            val nextPointForLeft = point.move(move)
            val nextPointForRight = (point.first to point.second+1).move(move)
            nextPointForLeft to nextPointForRight
        } else {
            val nextPointForLeft = (point.first to point.second-1).move(move)
            val nextPointForRight = point.move(move)
            nextPointForLeft to nextPointForRight
        }
        return canMoveInto(left, move, boxes, walls) &&
                canMoveInto(right, move, boxes, walls)
    }

    fun moveInto(point:Coord, move:Char, boxes:MutableList<Pair<Coord, Char>>) {
        val boxesAtPoint = boxes.filter { it.first == point }
        if (boxesAtPoint.isEmpty()) return
        val nextPoint = point.move(move)
        val box = boxesAtPoint.first()
        val boxChar = box.second
        if (move == '<' || move == '>') {
            moveInto(nextPoint, move, boxes)
            boxes.remove(box)
            boxes.add(nextPoint to boxChar)
        } else {
            val (next, current) = if (boxChar == '[') {
                val currentRight = point.first to point.second+1
                val nextPointForLeft = point.move(move)
                val nextPointForRight = currentRight.move(move)
                (nextPointForLeft to nextPointForRight) to (point to currentRight)
            } else {
                val currentLeft = point.first to point.second-1
                val nextPointForLeft = currentLeft.move(move)
                val nextPointForRight = point.move(move)
                (nextPointForLeft to nextPointForRight) to (currentLeft to point)
            }
            val (nextLeft, nextRight) = next
            moveInto(nextLeft, move, boxes)
            moveInto(nextRight, move, boxes)
            val (currLeft, currRight) = current
            boxes.remove(currLeft to '[')
            boxes.remove(currRight to ']')
            boxes.add(nextLeft to '[')
            boxes.add(nextRight to ']')
        }
    }


    fun Coord.move2(move:Char, boxes:MutableList<Pair<Coord, Char>>, walls:List<Coord>):Coord {
        val nextPoint = this.move(move)
        return if (canMoveInto(nextPoint, move, boxes, walls)) {
            moveInto(nextPoint, move, boxes)
            nextPoint
        }
        else this
    }

    fun part1(input:List<String>):Long {
        val (robot, boxes, walls, moves, mapSize) = input.parse()
        var curr = robot
        for (i in 0..< moves.count()) {
            curr = curr.move(moves[i], boxes, walls)

            //if (i == 5) break
        }
//        println("")
//        for (row in 0..<mapSize.first) {
//            for (col in 0..<mapSize.second) {
//                val coord = row to col
//                val whatToPrint = when {
//                    coord in walls -> '#'
//                    coord in boxes -> 'O'
//                    coord == curr -> '@'
//                    else -> '.'
//                }
//                print(whatToPrint)
//            }
//            println("")
//        }



        return boxes.sumOf{it.first*100L + it.second}
    }

    fun part2(input:List<String>):Long {
        val (robot, boxes, walls, moves, mapSize) = input.parse2()
        var curr = robot
        for (i in 0..< moves.count()) {
            curr = curr.move2(moves[i], boxes, walls)

            //if (i == 10) break
        }
//        println("")
//        for (row in 0..<mapSize.first) {
//            for (col in 0..<mapSize.second) {
//                val coord = row to col
//                val whatToPrint = when {
//                    coord in walls -> '#'
//                    coord in boxes.map{it.first} -> boxes.first{it.first == coord}.second
//                    coord == curr -> '@'
//                    else -> '.'
//                }
//                print(whatToPrint)
//            }
//            println("")
//        }



        return boxes
            .filter{it.second == '['}
            .map{it.first}
            .sumOf{it.first*100L + it.second}
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
        ##########
        #..O..O.O#
        #......O.#
        #.OO..O.O#
        #..O@..O.#
        #O#..O...#
        #O..O..O.#
        #.OO.O.OO#
        #....O...#
        ##########

        <vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
        vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
        ><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
        <<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
        ^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
        ^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
        >^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
        <><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
        ^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
        v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
    """.trimIndent().lines()
    check(timePart1(testInput) == 10092L)
    check(timePart2(testInput) == 9021L)

    // Read the input from the `src/Day15.txt` file.
    val input = readInput("Day15")
    timePart1(input).println()
    timePart2(input).println()
}
