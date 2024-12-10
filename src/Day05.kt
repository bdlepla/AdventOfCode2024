
fun main() {

    fun List<String>.parse():Pair<List<Pair<Int, Int>>, List<List<Int>>> {
        val ruleLines = this.takeWhile { it.isNotEmpty() }
        val rules = ruleLines.map {
            val (k, v) = it.split('|')
            k.toInt() to v.toInt()
        }

        val pages = this
            .drop(ruleLines.count()+1)
            .map{ line -> line.split(',').map{ it.toInt() } }

        return rules to pages
    }

    fun List<Int>.isValid(rules:List<Pair<Int, Int>>) = this.allTuples().all{ it in rules }

    fun List<Int>.isNotValid(rules:List<Pair<Int, Int>>) = !this.isValid(rules)

    fun List<Int>.middle() = this[(this.count()-1)/2]

    fun List<Int>.allBut(idx:Int):List<Int> = this.take(idx)+this.drop(idx+1)

    fun List<Int>.fix(rules:List<Pair<Int, Int>>):List<Int> =
        this
            .mapIndexed { idx, e-> e to this.allBut(idx).count{ (e to it) in rules } }
            .sortedBy { it.second }
            .map { it.first }

    fun part1(input: List<String>):Int {
        val (rules, pages) = input.parse()

        return pages
            .filter { it.isValid(rules) }
            .sumOf { it.middle() }
    }

    fun part2(input: List<String>):Int {
        val (rules, pages) = input.parse()
        return pages
            .filter { it.isNotValid(rules) }
            .map { it.fix(rules) }
            .sumOf { it.middle() }
    }

    val testInput = """
        47|53
        97|13
        97|61
        97|47
        75|29
        61|13
        75|53
        29|13
        97|29
        53|29
        61|53
        97|53
        61|29
        47|13
        75|47
        97|75
        47|61
        75|61
        47|29
        75|13
        53|13

        75,47,61,53,29
        97,61,53,29,13
        75,29,13
        75,97,47,61,53
        61,13,29
        97,13,75,29,47
    """.trimIndent().lines()
    check(part1(testInput) == 143)
    check(part2(testInput) == 123)

    // Read the input from the `src/Day05.txt` file.
    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
