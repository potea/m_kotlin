import java.net.URL
import java.util.*
import java.util.LinkedList

class SimpleJson(src: String) {
    val associated: Map<String, String>
            = src.replace("\"", "").replace("{","").replace("}","").split(",")
                 .associateBy({ it.split(":")[0] }, { it.split(":")[1] })

    operator fun get(s: String): String? {
        return associated[s]
    }
}

class Lotto(src: SimpleJson) {
    val numbers = (1..6).map { src["drwtNo$it"]?.toInt() }.toMutableList()
    init { 
        numbers.add(src["bnusNo"]?.toInt()!!) 
    }
    
    var winners : Int = src["winners"]!!.toInt()
        get() = if (winners != 0) winners else 1
}

fun <T> permutations(lists: List<List<T>>?): List<List<T>> {
    fun <T> permutations(original: List<List<T>>, res: MutableList<List<T>>, 
                         depth: Int, current: List<T>): MutableList<List<T>> {
        if (depth < original.size) {
            original[depth].forEach { permutations(original, res, depth + 1, current.toMutableList() + it) }
        } else {
            res.add(current)
        }
        return res
    }
    return permutations(lists!!, mutableListOf<List<T>>(), 0, LinkedList<T>())
}

fun main(args : Array<String>) {
    data class Result(val number: Int, val weight: Int)

    fun makeResult(target: List<MutableMap.MutableEntry<Int, MutableList<Pair<Int, Int>>>>)
            = target.subList(0, 7).map { it.key to it.value.map { it.first } }.map { it.second }
    
    val api = "http://www.nlotto.co.kr/common.do?method=getLottoNumber&drwNo"
    val lastDrwNo = SimpleJson(URL(api).readText())["drwNo"]?.toInt()

    val entries = (1..lastDrwNo!!)
            .map { drwNo -> Lotto(SimpleJson(URL("$api=$drwNo").readText())) }
            .flatMap { lotto -> lotto.numbers.map { Result(it!! , lotto.winners) } }
            .groupBy { it.number }.map { it.key to it.value.sumBy { it.weight } }
            .groupByTo(TreeMap()) { it.second }.entries

    permutations(makeResult(entries.reversed())).forEach(::println)
    permutations(makeResult(entries.toList())).forEach(::println)
}
