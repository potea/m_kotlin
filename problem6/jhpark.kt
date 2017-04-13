fun main(args: Array<String>) {
    fun guess(i: Int, range:IntRange) {
        fun check(range: IntRange) {
            val c = (range.last + range.first) / 2
            print(c.toString() + if (c == i) "!" else " ")

            if (c > i) check(range.first..c)
            else if (c < i) check(c..range.last)
        }
        check(range)
    }
    guess(readLine()?.toInt()!!, 1..100)
}
