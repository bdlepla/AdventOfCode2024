import kotlin.time.measureTimedValue

private typealias Region = List<Coord>
fun main() {

    fun getDirections():List<Coord> =
        listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)
    fun Coord.perpindicular() = Coord(this.second, this.first)


    fun Coord.neighbors(): List<Coord> {
        val (row, col) = this
        return listOf(
            row-1 to col,
            row+1 to col,
            row to col-1,
            row to col+1
        )
    }

    fun Coord.isTouching(other:Coord):Boolean = this in other.neighbors()

    fun Coord.isInRegion(region:List<Coord>):Boolean = region.any { it.isTouching(this) }

    fun List<Coord>.createRegions():List<Region> {
        val ret = mutableListOf<MutableList<Coord>>()

        for (coord in this) {
            val allMatchingRegions = ret.filter {coord.isInRegion(it) }
            if (allMatchingRegions.isEmpty()) {
                ret.add(mutableListOf(coord))
            } else {
                val first = allMatchingRegions.first()
                first.add(coord)
                allMatchingRegions.drop(1).forEach {
                    first.addAll(it)
                    it.clear()
                }
            }
        }

        return ret.filter{it.isNotEmpty()}
    }

    fun Region.area():ULong = this.count().toULong()

    operator fun Coord.plus(other:Coord) =
        Coord(this.first+other.first, this.second+other.second)

    fun Region.perimeter():ULong =
        // perimeter for each coord is 4 - number of neighbors in region
        this.sumOf {4 - (it.neighbors().intersect(this).count())}.toULong()

    fun Region.sides():ULong {
        val ret = mutableSetOf<Pair<Coord, Coord>>()
        forEach{coord ->
            for (dir in getDirections()) {
                if (this.contains(coord+dir)) continue

                val perp = dir.perpindicular()
                var cur = Coord(coord.first, coord.second)
                while (this.contains(cur+perp) && !this.contains(cur+dir))
                    cur += perp

                ret.add(cur to dir)
            }
        }
        return ret.count().toULong()
    }


    fun Region.calculatePrice():ULong =
        this.area() * this.perimeter()

    fun Region.calculateBulkPrice():ULong =
        this.area() * this.sides()

    fun List<String>.parse():List<Region> {
        val input = this
        val coordsByPlant = mutableMapOf<Char, MutableList<Coord>>()
        input.forEachIndexed { row, line ->
            line.forEachIndexed { col, c ->
                coordsByPlant.getOrPut(c){mutableListOf()}.add(row to col)
            }
        }

        return coordsByPlant.values.toList()
            .flatMap { it.createRegions() }
    }


    fun part1(input:List<String>):ULong =
        input
            // first, group plant to coord:
            .parse()
            // next, group each plants to their regions
            .sumOf { it.calculatePrice() }



    fun part2(input:List<String>):ULong =
        input.parse().sumOf{ it.calculateBulkPrice() }

    fun timePart1(input:List<String>):ULong {
        val (ret, time) = measureTimedValue {
            part1(input)
        }
        //println("Time of part1 = $time")
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
        RRRRIICCFF
        RRRRIICCCF
        VVRRRCCFFF
        VVRCCCJFFF
        VVVVCJJCFE
        VVIVCCJJEE
        VVIIICJJEE
        MIIIIIJJEE
        MIIISIJEEE
        MMMISSJEEE
    """.trimIndent().lines()
    check(timePart1(testInput) == 1930UL)
    check(timePart2(testInput) == 1206UL)

//    // Read the input from the `src/Day12.txt` file.
    val input = readInput("Day12")
    timePart1(input).println()
    timePart2(input).println()
}
