package com.codemaker.lottomaker.kotlin

import java.io.File
import java.net.URL

/**
 * Created by kwi on 2017-05-07.
 */
class Lotto(val revFile: File, val savedLottoFile: File) {
    val lottoUrl = "http://www.nlotto.co.kr/common.do?method=getLottoNumber"
    val lottoTurnParam = "&drwNo="
    val number = "drwtNo"
    val bonusNumber = "bnusNo"
    val winnerCountParam = "firstPrzwnerCo"
    val numberMap = hashMapOf<Int, Int>()

    enum class ExposeType {
        MAX_EXPOSE,
        MIN_EXPOSE
    }

    fun init(): Boolean {
        val curTurnNumber = getCurrentTurnNumber()

        if (!revFile.exists()) { //create
            saveLottoData(1, curTurnNumber)
        } else if (revFile.exists() && revFile.readText().toInt() != curTurnNumber) { //update
            saveLottoData(revFile.readText().toInt() + 1, curTurnNumber)
        }

        pareAllData()
        return true
    }

    fun getCurrentTurnNumber(): Int {
        val connection = URL(lottoUrl).openConnection()
        val responseData = connection.inputStream.bufferedReader().readText()
        val jsonData = responseData.filter { it != '{' && it != '}' }.split(",")
        val curTurnNumber = jsonData.filter { it.contains("drwNo") && !it.contains("drwNoDate") }[0].split(":")[1].toInt()

        return curTurnNumber
    }

    fun saveLottoData(startNum: Int, endNum: Int) {
        (startNum..endNum)
                .map { URL(lottoUrl + lottoTurnParam + it).openConnection() }
                .map { it.inputStream.bufferedReader().readText() }
                .forEach { savedLottoFile.appendText(it + "\n") }

        revFile.writeText(endNum.toString())
    }

    fun pareAllData() {
        savedLottoFile.readLines().forEach {
            val jsonData = it.filter { it != '{' && it != '}' }.split(",")
            val winnerCount = jsonData.filter { it.contains(winnerCountParam) }[0].split(":")[1].toInt()
            jsonData.filter { it.contains(number) || it.contains(bonusNumber) }
                    .map { it.split(":")[1] } //numberList
                    .forEach {

                        if (numberMap[it.toInt()] == null) {
                            numberMap[it.toInt()] = if (winnerCount > 0) winnerCount else 1
                        } else {
                            numberMap[it.toInt()] = numberMap[it.toInt()]!!.toInt() + if (winnerCount > 0) winnerCount else 1
                        }
                    }
        }
    }

    fun findTargetList(type: ExposeType): List<Pair<Int, Int>> {
        val list = if (type == ExposeType.MAX_EXPOSE) {
            numberMap.toList().sortedByDescending { it.second }
        } else {
            numberMap.toList().sortedBy { it.second }
        }

        val findList = arrayListOf<Pair<Int, Int>>()
        var i = 0
        while (findList.size < 7) {
            val temp = list.filter { it.second == list[i].second }
            findList.addAll(temp)
            i += temp.size
        }

        return findList
    }

    fun getPrintList(list: List<Pair<Int, Int>>): String {
        var str = list[0].first.toString()

        if (list.size > 7) {
            // 중복일때의 출력
            //find fixedPos
            var fixedPos = 0
            var count = 0
            var subList = ""
            while (fixedPos < 5) {
                val temp = list.filter { it.second == list[fixedPos].second }

                if (count + temp.size > 6) {
                    break
                }
                fixedPos += temp.size
                count += temp.size
            }

            for (i in 1..fixedPos - 1) {
                str += "," + list[i].first
            }

            val remains = list.subList(fixedPos, list.size)
            remains.forEach {
                subList += str + "," + it.first + "\n"
            }
            return str
        } else {
            for (i in 1..list.size - 1) {
                str += "," + list[i].first
            }
            return str
        }
    }

    fun getRecommendLotto(type: ExposeType): String {
        return getPrintList(findTargetList(type))
    }

    fun getLastWinnerLotto(): String {
        var lastWinnerLotto = ""
        val jsonData = savedLottoFile.readLines().last().filter { it != '{' && it != '}' }.split(",")
        lastWinnerLotto = jsonData.filter { it.contains(number) || it.contains(bonusNumber) }
                .map { it.split(":")[1] } //numberList
                .toString()

        return lastWinnerLotto
    }

    fun getRandomLotto(): String {
        return getRandom(7, 45)
    }

    fun getRandom(n: Int, m: Int): String {
        return IntRange(1, m)
                .toList()
                .sortedWith(Comparator { o1, o2 -> 1 - (Math.random() * 3).toInt() })
                .subList(0, n)
                .toString()
    }
}