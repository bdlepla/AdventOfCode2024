
typealias GuardInfo = Pair<Coord, Direction>
fun main() {

    fun Direction.turnRight():Direction =
        when (this) {
            Direction.North -> Direction.East
            Direction.East -> Direction.South
            Direction.South -> Direction.West
            Direction.West -> Direction.North
            else -> throw Exception("Not used here")
        }

    fun Grid.findGuard():GuardInfo {
        this.forEachIndexed{row, line ->
            line.forEachIndexed{col, c ->
                if (c == '^') return (row to col) to Direction.North
            }
        }
        throw Exception("Could not find guard")
    }

    fun Grid.isValid(coord:Coord):Boolean =
        coord.first in 0..<this.count() && coord.second in 0..<this[0].count()

    fun Grid.isNotValid(coord:Coord) = !isValid(coord)

    fun Grid.get(coord:Coord) = this[coord.first][coord.second]

    fun Grid.isBarrier(coord:Coord) = get(coord) == '#'
    fun Grid.isNotBarrier(coord:Coord) = !isBarrier(coord)


    fun GuardInfo.move(grid:Grid):GuardInfo {
        val (pos, dir) = this
        val (row, col) = pos
        val newPos = when (dir) {
            Direction.North -> row-1 to col
            Direction.South -> row+1 to col
            Direction.West -> row to col-1
            Direction.East -> row to col+1
            else -> throw Exception("$dir is not used in move")
        }
        if (grid.isNotValid(newPos) || grid.isNotBarrier(newPos)) return newPos to dir
        return (pos to dir.turnRight()).move(grid)
    }

    fun GuardInfo.countAllPositions(grid:Grid):Int {
        var guardPosition = this
        val retSet = mutableSetOf(guardPosition.first);
        guardPosition = guardPosition.move(grid)
        while (grid.isValid(guardPosition.first)) {
            retSet.add(guardPosition.first)
            guardPosition = guardPosition.move(grid)
        }
        return retSet.count()
    }

    fun GuardInfo.move2(grid:Grid, placedBarrier:Coord):GuardInfo {
        val (pos, dir) = this
        val (row, col) = pos
        val newPos = when (dir) {
            Direction.North -> row-1 to col
            Direction.South -> row+1 to col
            Direction.West -> row to col-1
            Direction.East -> row to col+1
            else -> throw Exception("$dir is not used in move2")
        }
        if (newPos != placedBarrier && (grid.isNotValid(newPos) || grid.isNotBarrier(newPos))) {
            return newPos to dir
        }
        return (pos to dir.turnRight()).move2(grid, placedBarrier)
    }

    // returns true if found a loop, false if not
    fun Grid.isLoop(barrierCoord:Coord, guardInfo:GuardInfo):Boolean {
        var guardPosition = guardInfo
        val retSet = mutableSetOf(guardPosition);
        guardPosition = guardPosition.move2(this, barrierCoord)
        while (this.isValid(guardPosition.first)) {
            if (!retSet.add(guardPosition)) return true
            guardPosition = guardPosition.move2(this, barrierCoord)
        }
        return false
    }

    fun part1(input:Grid):Int = input.findGuard().countAllPositions(input)

    fun part2(input:Grid):Int {
        // for each position that is not a barrier or starting position
        //      add a barrier to the position to the original grid
        //      move the guard until
        //          we've seen the pos and direction before (return as true)
        //          the guard position is off the map (return as false)
        // count all the trues
        return input.flatMapIndexed {row, line ->
            line.mapIndexed { col, c ->
                (c == '.') && input.isLoop(row to col, input.findGuard())
            }
        }.count{ it }
    }

    val testInput = """
        ....#.....
        .........#
        ..........
        ..#.......
        .......#..
        ..........
        .#..^.....
        ........#.
        #.........
        ......#...
    """.trimIndent().lines()
    check(part1(testInput) == 41)
    check(part2(testInput) == 6)

    // Read the input from the `src/Day06.txt` file.
    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
