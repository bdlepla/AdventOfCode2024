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

fun <T>List<T>.allTuples():List<Pair<T, T>> =
    this.dropLast(1).flatMapIndexed{ idx, e ->
        this.drop(idx+1).map { e2 -> e to e2 }
    }

fun Grid.get(coord:Coord) = this[coord.first][coord.second]
fun Grid.isValid(coord:Coord) = coord.first in 0..<this.count() &&
        coord.second in 0..<this[0].count()