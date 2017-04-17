package study

import kotlin.coroutines.experimental.buildSequence

/**
 * Created by codemaker88 on 2017-04-15.
 */

object Study7 {
    @JvmStatic fun main(args: Array<String>) {
        JosephusProblem(7, 3, 2)
        JosephusProblem(3, 3, 1)
        JosephusProblem(1000, 1000, 2)
    }

    fun JosephusProblem(n: Int, k: Int, t: Int) {
        println(buildSequence {
            val list = ArrayList<Int>((1..n).toList())
            var index = 0
            while (list.isNotEmpty()) {
                index += k - 1
                index %= list.size
                yield(list.removeAt(index))
            }
        }.toList().indexOf(t) + 1)
    }
}
