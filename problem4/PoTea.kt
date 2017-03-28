/**
 * Created by potea on 2017-03-27.
 *
 * Problem 4
 */

import java.util.*


fun lotto(count: Int, limit: Int) {
    var rangeList = IntRange(1, limit).toMutableList()
    for (i in 1 until count) print("${rangeList.removeAt(Random().nextInt(rangeList.size))}, ")
    println("${rangeList.removeAt(Random().nextInt(rangeList.size))}")
}

fun sort(list: List<List<Char>>) {
    println(list.sortedWith( Comparator { o1, o2 ->  if (o1.size != o2.size) o1.size.compareTo(o2.size) else o1.toString().compareTo(o2.toString()) }))
}

fun prime(start: Int, end: Int) {
    fun isPrime(num: Int): Boolean = (num > 1 && (2..Math.sqrt(num.toDouble()).toInt()).find { num % it == 0 } == null)
    println(IntRange(start, end).filter{ isPrime(it) })
}

fun graycode(length: Int) {
    var codetable = mutableMapOf<Int, List<String>>()
    codetable[0] = listOf("")

    for (i in 1..length) {
        codetable.put(i, codetable[i-1]?.toList()!!.map { it -> "0" + it }.toList()
                + codetable[i-1]?.reversed()!!.map { it -> "1" + it }.toList())
    }
    println(codetable[length])
}


fun main(args: Array<String>) {
    lotto(7, 45)
    sort(listOf(listOf('a', 'b', 'c'), listOf('d', 'e'), listOf('f', 'g', 'h'), listOf('d', 'e'), listOf('i', 'j', 'k', 'l'), listOf('m', 'n'), listOf('o')))
    prime(7, 31)
    graycode(3)
}
