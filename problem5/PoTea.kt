/**
 * Created by potea on 2017-03-31.
 *
 * Problem 5
 */

import kotlin.coroutines.experimental.buildSequence

fun ant1(count: Int) {
    fun lookAndSay(list: List<Int>): List<Int> {
        val count = list.takeWhile { it == list[0] }.size
        val sublist = list.drop(count)

        return when(sublist.size) {
            0 -> listOf(list[0], count)
            else -> listOf(list[0], count) + lookAndSay(sublist)
        }
    }

    var list: List<Int> = listOf(1)

    for (x in 1 until count) {
        list = lookAndSay(list)
    }

    list.forEach(::print)
    println()
}

fun ant2(count: Int) {
    fun lookAndSay(sequence: Sequence<String>): Sequence<String> {
        return buildSequence {
            var head = '1'
            var count = 0

            sequence.forEach {
                for (c in it) {
                    if (c != head) {
                        yield("$head$count")
                        count = 1
                        head = c
                    } else {
                        count++
                    }
                }
            }
            yield("$head$count")
        }
    }

    var sequence: Sequence<String> = sequenceOf("1")
    val antSeq = buildSequence {
        while (true) {
            yield(sequence)
            sequence = lookAndSay(sequence)
        }
    }

    antSeq.take(count).last().forEach(::print)
    println()
}

fun main(args: Array<String>) {
    ant1(10)
    ant2(10)
    ant2(100)
}

