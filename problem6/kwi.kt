package study

/**
 * Created by codemaker88 on 2017-04-09.
 */
object Study6 {
    @JvmStatic fun main(args: Array<String>) {
        val target = (Math.random() * 100).toInt() + 1
        var start = System.nanoTime()
        println(find(1, 100, target))
        println("time : " + (System.nanoTime() - start))
    }

    fun find(min: Int, max: Int, target: Int): String {
        val result = (min + max) / 2
        if (min == target || max == target || target == result) return "$result!"
        else return "$result " + if (result > target) find(min, result, target) else find(result, max, target)
    }
}