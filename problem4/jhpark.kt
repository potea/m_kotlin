import java.util.*

fun lotto(count: Long, bound: Int): String
        = Random().ints(1, bound).distinct().limit(count).toArray().joinToString(transform = Int::toString)

fun sort(input: String) :String {
    fun subLists(input: String): Sequence<MatchResult> = "\\[(.*?)\\]".toRegex().findAll(input)
    fun Sequence<MatchResult>.filterLetters() = map { it.value.toList().filter(Char::isLetter) }
    fun parseList(input: String) : List<List<Char>> = subLists(input).filterLetters().toList()

    return parseList(input).sortedBy { it.size }.toString()
}

fun primes(min:Int, max:Int): List<Int> {
    fun Int.sqrt() = Math.sqrt(this.toDouble()).toInt()
    fun Int.isPrime() = this > 1 && (2..sqrt()).find { this % it == 0 } == null

    return (min..max).filter(Int::isPrime)
}

fun grayCode(n:Int): String {
    fun Int.pow(exp:Int) = Math.pow(this.toDouble(), exp.toDouble()).toInt()

    fun grayCode(k:Int, n:Int): String = 
        if (n == 1) k.toString() 
        else if (k < 2.pow(n - 1)) "0" + grayCode(k, n - 1) 
        else  "1" + grayCode(2.pow(n) - k - 1, n - 1)

    return List(2.pow(n), { k -> grayCode(k, n) }).toString()
}

fun main(args : Array<String>) {
    println(lotto(4, 10))
    println(sort("['a', 'b', 'c'], ['d', 'e'],['f', 'g', 'h'], ['d', 'e'], ['i', 'j', 'k', 'l'], ['m', 'n'], ['o']"))
    println(primes(7, 31))
    println(grayCode(3))
}
