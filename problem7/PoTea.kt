/**
 * Created by potea on 2017-04-23.
 *
 * Problem 7
 */

fun MutableList<Int>.getJosephus(index: Int): Int {
    val circle = (index - 1) % this.size
    val result = this[circle]
    this.removeAt(circle)
    return result
}

fun Josephus(n: Int, k: Int, t:Int): Int {
    var list = (1..n).toMutableList()
    var count = 0
    for (x in 1..n) {
        if (list.getJosephus(x * k - count++) == t)
            return count
    }
    return 0
}

fun main(args: Array<String>) {
    println(Josephus(7, 3, 2))
}
