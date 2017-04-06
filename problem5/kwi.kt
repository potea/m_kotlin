package study

import java.util.*
import kotlin.coroutines.experimental.buildSequence

/**
 * Created by codemaker88 on 2017-04-01.
 */
object Study5 {
    @JvmStatic fun main(args: Array<String>) {
        val solve = 개미수열()
        solve.solve1(10)
        solve.solve2(100)
    }

    class 개미수열 {

        fun solve1(n: Int) {
            var antSequence = arrayListOf("1")

            (1..n - 1).forEach {

                val nextAntSequence = ArrayList<String>()
                var curChar = " "
                var count = 0

                antSequence.forEach {
                    if (curChar == it) {
                        count++
                    } else {
                        if (curChar != " ") {
                            nextAntSequence.add(curChar)
                            nextAntSequence.add("$count")
                        }
                        curChar = it
                        count = 1
                    }
                }
                nextAntSequence.add(curChar)
                nextAntSequence.add("$count")

                antSequence = nextAntSequence
            }

            println(antSequence)
        }

        fun solve2(n: Int) {
            val sequenceMap = hashMapOf<Int, Sequence<String>>()
            fun nextAntSequence(i: Int): Sequence<String> {
                return buildSequence {
                    var curChar = " "
                    var count = 0

                    sequenceMap[i]?.forEach {
                        if (curChar == it) {
                            count++
                        } else {
                            if (curChar != " ") {
                                yield(curChar)
                                yield("$count")
                            }
                            curChar = it
                            count = 1
                        }
                    }
                    if (curChar != " ") {
                        yield(curChar)
                        yield("$count")
                    }

                }
            }

            val antSeq = buildSequence {
                var index = 0
                sequenceMap.put(index, sequenceOf("1"))
                yield(sequenceMap[0])

                while (index < n - 1) {
                    val nextAntBuilder = nextAntSequence(index)
                    sequenceMap.put(++index, nextAntBuilder)
                    yield(nextAntBuilder)
                }
            }

            antSeq.last()?.forEach(::print)//숫자 하나씩 출력
            //println(antSeq.last()?.toList())//수열 전체 출력
        }
    }
}
