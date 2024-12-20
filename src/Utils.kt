import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

typealias Coord = Pair<Int, Int>  // Row (Y), Col (X)
typealias Grid = List<String>


enum class Direction {
    North,
    NorthEast,
    East,
    SouthEast,
    South,
    SouthWest,
    West,
    NorthWest
}

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readText().trim().lines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

/**
 * Returns true if all elements in the collection match.
 */
fun <T>Iterable<T>.unanimous(): Boolean = this.all{it == this.first()}
fun <T>Iterable<T>.allUnique():Boolean = this.count() == this.toSet().count()

fun <T>List<T>.allTuples():List<Pair<T, T>> =
    this.dropLast(1).flatMapIndexed{ idx, e ->
        this.drop(idx+1).map { e2 -> e to e2 }
    }

fun Grid.get(coord:Coord) = this[coord.first][coord.second]
fun Grid.isValid(coord:Coord) = coord.first in 0..<this.count() &&
        coord.second in 0..<this[0].count()

internal data class PQState<T>(val data:T, val step:Int)

// after adding data with a cost,
// the pop will return the data with the least cost
class PriorityQueue<T> {
    internal val data = mutableListOf<PQState<T>>()
    fun add(element:T, cost:Int) = add(PQState(element, cost))
    private fun add(element:PQState<T>) {
        val idx = data.binarySearch { element.step - it.step }
        val insertIdx = if (idx < 0) (idx+1)*-1 else idx
        data.add(insertIdx, element)
    }
    fun pop():Pair<T, Int>{
        val ret = data.removeLast()
        return ret.data to ret.step
    }

    fun isEmpty() = data.isEmpty()
    fun isNotEmpty() = !isEmpty()
}

fun Int.raisedToPower(num:Int):Int = (0..<num).fold(1){a, _ -> a * this}
