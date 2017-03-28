package exam_4

import java.util.*
import java.util.Arrays.asList

/**
 * Created by bross on 2017. 3. 27..
 */

fun exam1() {
    fun <T : Comparable<T>> shuffle(items: MutableList<T>): List<T> {
        val rg: Random = Random()
        for (i in 0..items.size - 1) {
            val randomPosition = rg.nextInt(items.size)
            val tmp: T = items[i]
            items[i] = items[randomPosition]
            items[randomPosition] = tmp
        }
        return items
    }

    fun lotto(count: Int, maxNumber: Int) {
        val range = (1..maxNumber).toList()
        val shuffled = shuffle(range as MutableList<Int>)
        println(shuffled.subList(0, count))
    }

    lotto(4, 10)
}

fun exam2() {

    class CharList constructor(str: String) : Comparable<CharList> {

        override fun compareTo(other: CharList): Int {
            val value = list.count().compareTo(other.list.count())
            if (value == 0) {
                return list.toString().compareTo(other.list.toString())
            }
            return value
        }

        override fun toString(): String {
            return list.toString() + " "
        }

        var list: List<Char> = str.toList().filter(Char::isLetter)

    }


    val input = asList(CharList("a,b,c"), CharList("d,e"), CharList("f,g,h"),
            CharList("d,e"), CharList("i,j,k,l"), CharList("m,n"), CharList("o"))
    input.sorted().forEach(::print)
}

fun exam3() {
    val start = 7
    val end = 31
    fun Int.isPrime() = this > 1 && (2..(this / 2)).all { this % it != 0 }
    val list = (start..end).filter(Int::isPrime)
    println(list)
}

fun exam4() {

    fun GrayCode(bits: Int): List<String>
            = if (bits == 0) listOf("")
    else {
        val codes = GrayCode(bits - 1)
        codes.map { "0" + it } + codes.asReversed().map { "1" + it }
    }

    (1..4).forEach { print(GrayCode(it)) }

}

fun main(args: Array<String>) {
    exam1()
    exam2()
    exam3()
    exam4()
}
