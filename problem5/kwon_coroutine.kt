import kotlin.coroutines.experimental.buildSequence


fun ant(n: Int): Sequence<Int> {
    var result = sequenceOf(1)
    for (i in 2..n) {
        result = next(result)
    }
    return result
}

fun next(ns: Sequence<Int>): Sequence<Int> {
    return buildSequence<Int> {
        val it = ns.iterator()
        var prev = it.next()
        var count = 1
        while (it.hasNext()) {
            val c = it.next()
            if (prev == c)
                count++
            else {
                yield(count)
                yield(prev)
                prev = c
                count = 1
            }
        }
        yield(count)
        yield(prev)
    }
}


fun main(args: Array<String>) {
    var result_100 = ant(100)
    println(result_100.take(100).last())
    var result_1000 = ant(1000)
    println(result_1000.take(1000).last())

    //StackOverflowError
    var result_10000 = ant(5000)
    println(result_10000.take(5000).last())
}
