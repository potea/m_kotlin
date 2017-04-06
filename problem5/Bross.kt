package exam_5

import kotlin.coroutines.experimental.buildSequence

/**
 * Created by bross on 2017. 4. 1..
 */

fun exam1() {

    fun lookAndSay(count: Int): String {
        if (count == 1) {
            return "1"
        } else {
            var prevStr = lookAndSay(count - 1)
            var firstChar = prevStr[0]
            var cnt = 0
            var result = ""
            prevStr.forEach { it ->
                if (it == firstChar) cnt++
                else {
                    result += ("$firstChar$cnt")
                    firstChar = it
                    cnt = 1
                }
            }
            result += ("$firstChar$cnt")
            return result
        }
    }

    println(lookAndSay(10))
}

fun exam2() {

    fun lookAndSay(count: Int): List<Int> {
        tailrec fun lookAndSay(list: List<Int>): List<Int> {
            val count = list.takeWhile { it == list.first() }.size
            val remainList = list.drop(count)
            if (remainList.isEmpty()) {
                return listOf(list.first(), count)
            } else {
                var newList = ArrayList<Int>()
                newList.add(list.first())
                newList.add(count)
                newList.addAll(lookAndSay(remainList))
                return newList
            }
        }
        if (count == 1) {
            return listOf(1)
        } else {
            return lookAndSay(lookAndSay(count - 1))
        }
    }

    println(lookAndSay(10))
}


var lookAndSay = buildSequence {

    fun lookAndSay(list: List<Int>): List<Int> {
        val count = list.takeWhile { it == list.first() }.size
        val remainList = list.drop(count)
        if (remainList.isEmpty()) {
            return listOf(count, list.first())
        } else {
            var newList = ArrayList<Int>()
            newList.add(list.first())
            newList.add(count)
            newList.addAll(lookAndSay(remainList))
            return newList
        }
    }

    var list = lookAndSay(listOf(1))

    while (true) {
        yield(list)
        list = lookAndSay(list)
    }

}


fun exam3() {
    println(lookAndSay.take(100).last())
}


fun exam4() {

    fun Sequence<Int>.lookAndSay(): Sequence<Int> = buildSequence {
        var list = this@lookAndSay.toMutableList()
        var count = list.takeWhile { it == list.first() }.count()
        var remainList = list.drop(count)

        yield(list.first())
        yield(count)

        while (remainList.isNotEmpty()) {
            yield(remainList.first())
            count = remainList.takeWhile { it == remainList.first() }.count()
            yield(count)
            remainList = remainList.drop(count)
            if (count == 0) break
        }
    }

    var sequence: Sequence<Int> = sequenceOf(1)
    return buildSequence {
        while (true) {
            yield(sequence)
            sequence = sequence.lookAndSay()
        }
    }.take(100).last().forEach(::print)
}


fun main(args: Array<String>) {
    exam1()
    exam2()
//    exam3()
    exam4()
}
