package exam_3

import com.oracle.javafx.jmx.json.JSONFactory
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.FileWriter
import java.io.StringReader
import java.net.URL

/**
 * Created by bross on 2017. 3. 15..
 */

// Common
private fun String.removeBrackets(): String {
    return replace("[", "").replace("]", "")
}


//lotto api entity class
data class LottoNumber
(var json: String = "", var numberList: List<Int> = ArrayList(), var winCount: Int = 0, var date: String = "") {
    override fun toString(): String = numberList.toString().removeBrackets()
    fun parse(str: String) {
        val jsonReader = JSONFactory.instance().makeReader(StringReader(str)) // json factory를 이용하여 json reader  생성
        val jsonDocument = jsonReader.build() // json document 생성
        val keyString = "drwtNo" // json 파싱
        for (n in 1..6) {
            numberList += jsonDocument.`object`().get(keyString + n).toString().toInt()
        }
        numberList += jsonDocument.`object`().get("bnusNo").toString().toInt()
        winCount = jsonDocument.`object`().get("firstPrzwnerCo").toString().toInt()
        date = jsonDocument.`object`().get("drwNoDate").toString()
    }

}

// url parser
object URLParser {
    val urlString = "http://www.nlotto.co.kr/common.do?method=getLottoNumber&drwNo="
    var url: URL? = null
    fun parse(count: Int): LottoNumber {
        url = URL(urlString + count)
        return LottoNumber(url!!.readText())
    }
}

object FileCache { // file cache
    val FILE_NAME = "lotto_number_cache.dat"
    fun write(str: String) {
        val fileWriter = FileWriter(FILE_NAME, true)
        fileWriter.append(str + "\n")
        fileWriter.close()
    }

    fun readAll():List<String>? {
        var strList : List<String>? = null
        try {
            val fileReader = FileReader(FILE_NAME)
            strList= fileReader.readLines()
            fileReader.close()
        }
        catch (e: FileNotFoundException)
        {
            e.printStackTrace()
        }

        return strList

    }
}

//lotto count entity
data class LottoCount(val number: Int, var count: Int = 0) : Comparable<LottoCount> {
    override fun compareTo(other: LottoCount): Int = compareValuesBy(this, other, { it.count })

    override fun toString(): String {
        val number = this.number + 1
        return "$number"
    }

    fun inc() = this.count.inc()
    fun add(cnt : Int){
        count += cnt
    }
}

class LottoSequence(count: Int = 746) {
    val LOTTO_MAX_COUNT = 45 // max lotto number 45
    var lottoMap: MutableMap<Int, LottoNumber> = HashMap() // 회차별 lotto map
    var lottoCntList: Array<LottoCount> = emptyArray() // lotto count list // heap 

    init {

        FileCache.readAll()?.forEachIndexed { index, s ->
            val lottoNumber = LottoNumber()
            lottoNumber.parse(s)
            lottoMap.put(index, lottoNumber)
        }

        val start = lottoMap.keys.max()?:1 // key의 max값 부터 시작 하거나 값이 없을경우 1부터 시작 
        for (n in start..count) {
            lottoMap[n] ?: urlRequest(n) // lottoMap에 값이 없을경우 url request 호출
        }
        sequenceCounting()
    }

    fun urlRequest(n: Int): Boolean {  
        try {
            val lottoNumber = URLParser.parse(n)
            FileCache.write(lottoNumber.json)
            lottoMap.put(n, lottoNumber)
        }
        catch (e:NumberFormatException){
            e.printStackTrace()
            return false
        }
        return true
    }

    fun sequenceCounting() { // 수열 카운트 
        for (n in 1..LOTTO_MAX_COUNT ) {
            lottoCntList += LottoCount(n)
        }
        lottoMap.values.forEach({ (_, numberList, winCount) ->
            numberList.forEach { number ->
                if (winCount <= 1) {
                    lottoCntList[number - 1].inc()
                } else {
                    lottoCntList[number - 1].add(winCount)
                }
            }
        })
    }


    fun print(printMenu : Type){
        when(printMenu)
        {
            Type.MIN_EXPOSE-> println("MIN : " + lottoCntList.sortedArray().toList().subList(0, 6).toString().removeBrackets()) // convert min heap print
            Type.MAX_EXPOSE-> println("MAX : " + lottoCntList.sortedArray().reversed().subList(0, 6).toString().removeBrackets()) // conver max heap print
        }
    }
}

enum class Type{
    MIN_EXPOSE,MAX_EXPOSE
}


infix fun LottoSequence.print(menu : Type){
    this.print(menu)
}

fun main(args: Array<String>) {

    val lottoSequence = LottoSequence()

    lottoSequence print Type.MAX_EXPOSE

    lottoSequence print Type.MIN_EXPOSE

}
