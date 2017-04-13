package study

/**
 * Created by codemaker88 on 2017-04-09.
 */
object Study6 {
    @JvmStatic fun main(args: Array<String>) {
        var start = System.nanoTime()
        (1..100).forEach { println(find(1, 100, it)) }
        println("time : " + (System.nanoTime() - start))
    }

    fun find(min: Int, max: Int, target: Int): String {
        val mid = (min + max) / 2
        if (min == target || max == target || mid == target) return "$target!"
        else return "$mid " + if (mid > target) find(min + 1, mid - 1, target) else find(mid + 1, max - 1, target)
    }
}