


fun main() {

    fun Grid.isValidCoord(coord:Coord) =
        coord.first in 0..<this.count() && coord.second in 0..<this[0].count()

    fun Coord.getValidSurroundingCoords(input:Grid, directions:Iterable<Direction>):List<Pair<Coord, Direction>> =
        directions
            .map {
                val (row, col) = this
                when (it) {
                    Direction.North -> (row - 1 to col) to it
                    Direction.South -> (row + 1 to col) to it
                    Direction.East -> (row to col + 1) to it
                    Direction.West -> (row to col - 1) to it
                    Direction.NorthEast -> (row - 1 to col + 1) to it
                    Direction.NorthWest -> (row - 1 to col - 1) to it
                    Direction.SouthWest -> (row + 1 to col - 1) to it
                    Direction.SouthEast -> (row + 1 to col + 1) to it
                }
            }
            .filter{ input.isValidCoord(it.first) }

    fun Direction.opposite() =
        when (this) {
            Direction.NorthEast -> Direction.SouthWest
            Direction.SouthWest -> Direction.NorthEast
            Direction.NorthWest -> Direction.SouthEast
            Direction.SouthEast -> Direction.NorthWest
            else -> throw Exception("Not Used")
        }

    fun Grid.get(coord:Coord) = this[coord.first][coord.second]

    // find all letters on grid, regardless of any coord or direction
    fun Grid.findAll(letter:Char):List<Coord> {
        val ret = mutableListOf<Coord>()
        this.forEachIndexed{ row, line ->
            line.forEachIndexed{ col, c ->
                if (c == letter) ret.add(row to col)
            }
        }
        return ret
    }

    // find all letters surrounding a given coord, regardless of direction
    fun List<Coord>.findAll(letter:Char, input:Grid):List<Pair<Coord, Direction>>
    {
        return this.flatMap{ xCoord ->
            xCoord.getValidSurroundingCoords(input, Direction.entries)
                .filter{input.get(it.first) == letter}
        }
    }

    // find all chars surrounding a given coord in a given direction
    fun List<Pair<Coord, Direction>>.findAll(letter:Char, input:Grid):List<Pair<Coord, Direction>> {
        return this.flatMap { (mCoord, direction) ->
            mCoord.getValidSurroundingCoords(input, listOf(direction))
                .filter{ input.get(it.first) == letter }
        }
    }


    fun part1(input: Grid) = input
        .findAll('X')
        .findAll('M', input)
        .findAll('A', input)
        .findAll('S', input)
        .count()


    fun part2(input: Grid):Int {
        // find all 'A' letters
        // for each 'A' coord,
        //  look for 'M' in the corner directions: NW, NE, SW, SE surrounding 'A' coord
        //  look for 'S' in the corner directions also surrounding 'A' coord
        // the number of 'M" should be 2
        // the number of 'S' should also be 2
        // for each 'M', there should be an 'S' in the opposite direction
        val cornerDirections = listOf(Direction.NorthWest, Direction.NorthEast,
            Direction.SouthWest, Direction.SouthEast)
        return input.findAll('A')
            .count {
                val validSurroundingCoords = it.getValidSurroundingCoords(input, cornerDirections)
                val ms = validSurroundingCoords.filter{input.get(it.first) == 'M'}
                val ss = validSurroundingCoords.filter{input.get(it.first) == 'S'}
                ms.count() == 2 && ms.map{ it.second.opposite() }.minus(ss.map{ it.second }).count() == 0
            }
    }

    val testInput = """
        MMMSXXMASM
        MSAMXMSMSA
        AMXSXMAAMM
        MSAMASMSMX
        XMASAMXAMM
        XXAMMXXAMA
        SMSMSASXSS
        SAXAMASAAA
        MAMMMXMMMM
        MXMXAXMASX
    """.trimIndent().lines()
    check(part1(testInput) == 18)
    check(part2(testInput) == 9)

    // Read the input from the `src/Day04.txt` file.
    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
