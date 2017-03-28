import java.awt.List
import java.io.File
import java.net.URL
import java.util.*
import kotlin.comparisons.compareBy
import kotlin.comparisons.then
import kotlin.comparisons.thenBy

fun lotto(length: Int, max: Int = 10) {
    var result = IntRange(1, max).toList()
    Collections.shuffle(result, Random())
    println(result.subList(0, length))
}

fun com(input: MutableList<MutableList<Char>>) {
//    input.forEach {
//        it.sort()
//    }
    //so
    input.sortWith(Comparator { l, r ->
        if (l.size == r.size) {
            l.sort()
            r.sort()
//            l.sorted().get(0).compareTo(r.sorted().get(0))
            l.toString().compareTo(r.toString())
        } else
            Integer.compare(l.size, r.size)
    })
    println(input)
}

fun isPrime(number: Int): Boolean {
    return (IntRange(2, number - 1).filter { (number % it) == 0 }).isEmpty()
}


fun main(args: Array<String>) {
    lotto(4, 10)

    var input = mutableListOf(
            mutableListOf('e', 'd'),
            mutableListOf('f', 'g', 'h'),
            mutableListOf('a', 'b', 'c'),
            mutableListOf('d', 'e'),
            mutableListOf('i', 'j', 'k', 'l'),
            mutableListOf('m', 'n'),
            mutableListOf('o')
    )

    com(input)
    println(IntRange(7, 31).filter { isPrime(it) })
}

