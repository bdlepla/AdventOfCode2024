
fun main() {


    fun parse(input:String):List<Long> {
        var id = 0L
        return input.flatMapIndexed { idx, c ->
            if (idx and 1 == 1) {
                (0..<c.toString().toInt()).map { -1L }
            }
            else {
                val num = id
                id++
                (0..<c.toString().toInt()).map { num }
            }
        }
    }

    fun part1(input:Grid):Long {
        val data = parse(input[0]).toMutableList()
        var startingIdx = data.indexOfFirst { it == -1L }
        var endingIdx = data.count()-1
        while (startingIdx < endingIdx) {
            val b = data[endingIdx]
            if (b != -1L) {
                data[startingIdx] = b
                data[endingIdx] = -1
                startingIdx = data.indexOfFirst { it == -1L }
            }
            endingIdx--
        }

        return data.mapIndexed{idx, e -> if (e == -1L) 0 else idx * e }.sum()
    }

    data class MultiBlock(var id: Long, var numBlocks:Int)

    fun List<Long>.groupToMultiBlock():List<MultiBlock> =
        this
            .drop(1)
            .fold(mutableListOf(MultiBlock(first(), 1))) { acc, e ->
                val last = acc.last()
                if (last.id == e) last.numBlocks++
                else acc.add(MultiBlock(e, 1))
                acc
            }



    fun part2(input:Grid):Long {
        val data  = parse(input[0]).groupToMultiBlock().toMutableList()
        var endIdx = data.count()-1
        while (endIdx > 0) {
            val blockToMove = data[endIdx]
            if (blockToMove.id == -1L)  {
                endIdx--
                continue
            }

            val sizeToFind = blockToMove.numBlocks
            val startIdx = data.indexOfFirst { it.id == -1L && it.numBlocks >= sizeToFind }
            if (startIdx == -1 || startIdx > endIdx) {
                endIdx--
                continue
            }

            val blockToAccept = data[startIdx]
            blockToAccept.id = blockToMove.id
            blockToMove.id = -1
            val newEmptyBlockSize = blockToAccept.numBlocks - sizeToFind
            if (newEmptyBlockSize > 0) {
                blockToAccept.numBlocks = sizeToFind
                data.add(startIdx+1, MultiBlock(-1L, newEmptyBlockSize))
                endIdx++
            }

            endIdx--
        }

        return data.fold(0L to 0){ (acc, idx), e ->
            val newAcc = acc + if (e.id == -1L) 0
            else { (0..<e.numBlocks).sumOf { (idx+it)*e.id } }
            newAcc to idx+e.numBlocks
        }.first
    }

    val testInput = """
        2333133121414131402
    """.trimIndent().lines()
    check(part1(testInput) == 1928L)
    check(part2(testInput) == 2858L)

    // Read the input from the `src/Day09.txt` file.
    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
