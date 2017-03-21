import java.io.File
import java.net.URL
import java.util.*

fun parsingMap(json: String): HashMap<String, String> {
    var totalMap = HashMap<String, String>()
    var value = json.substring(1, json.length - 1)
    value.split(",").filter {
        Regex("firstPrzwnerCo|drwtNo|bnusNo").containsMatchIn(it)
    }.forEach {
        var keyValue = it.replace("\"", "").split(":"); totalMap.put(keyValue[0], keyValue[1])
    }

    return totalMap
}

fun loadTotalLotto(range : IntRange): List<String> {
//    var result = File("""D:\study\Lotto\save.txt""").readLines()

    val result = ArrayList<String>()
    range.forEach {
        result.add(URL("http://www.nlotto.co.kr/common.do?method=getLottoNumber&drwNo=" + it).readText())
    }


    return result
}

fun String.splitKeeping(str: String): List<String> {
    return this.split(str).flatMap { listOf(it, str) }.dropLast(1).filterNot { it.isEmpty() }
}

fun String.splitKeeping(vararg strs: String): List<String> {
    var res = listOf(this)
    strs.forEach { str ->
        res = res.flatMap { it.splitKeeping(str) }
    }
    return res
}


/*
firstPrzwnerCo
drwtNo1 .. drwtNo6, bnusNo
*/

fun main(args: Array<String>) {

    var totalLotto = loadTotalLotto(1.rangeTo(746))

    var lottoArray = IntArray(45)
    totalLotto.forEach {
        var parsedMap = parsingMap(it)
        var przwnerCoText = parsedMap["firstPrzwnerCo"];
        if (przwnerCoText != null) {
            var przwnerCo = przwnerCoText.toInt()
            if(przwnerCo == 0) {
                przwnerCo = 1;
            }
            parsedMap.remove("firstPrzwnerCo")
            parsedMap.values.forEach {
                lottoArray[it.toInt() - 1] += przwnerCo
            }
        }
    }

    var maxLotto = lottoArray.sortedArray()

    lottoArray.filter { it >= maxLotto[6] }.flatMap { listOf(it) }

    val maxIndexLotto = ArrayList<Int>()
    lottoArray.forEachIndexed { index, value ->
        if(value >= maxLotto[38]) {
            maxIndexLotto.add(index + 1)
        }
    }
    println("최다 노출 기준\n" + maxIndexLotto)

    val minIndexLotto = ArrayList<Int>()
    lottoArray.forEachIndexed { index, value ->
        if(value <= maxLotto[6]) {
            minIndexLotto.add(index + 1)
        }
    }
    println("최소 노출 기준\n" + minIndexLotto)
}

