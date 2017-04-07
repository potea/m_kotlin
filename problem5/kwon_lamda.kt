import java.awt.List
import java.io.File
import java.net.URL
import java.util.*
import kotlin.comparisons.compareBy
import kotlin.comparisons.then
import kotlin.comparisons.thenBy


fun group(groupAs : MutableList<Int>) : MutableList<MutableList<Int>> {
    var ass = arrayListOf<MutableList<Int>>()
    var g = arrayListOf<Int>()
    groupAs.forEach { value ->
        if (g.isEmpty() || !g[0].equals(value)) {
            g = arrayListOf<Int>()
            ass.add(g)
        }
        g.add(value)
    }
    return  ass
}

fun ant(n : Int) : MutableList<Int>{
    var result = mutableListOf(1)
    for( i in 2..n) {
        result = next_lamda(result)
    }
    return result
}

fun next_lamda(ns : MutableList<Int> ) : MutableList<Int>  {
    var group = group(ns)
    return group(ns).map { listOf(it.first(), it.size) }.flatten().toMutableList()
}


fun main(args: Array<String>) {
    print(ant(10))
}

