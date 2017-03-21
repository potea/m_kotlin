package my.program

import java.io.File
import java.net.URL

object Study3 {

    infix fun Lotto.print(type: Lotto.ExposeType) {
        printList(findTargetList(type))
    }

    @JvmStatic fun main(args: Array<String>) {
        val lotto = Lotto()
        lotto.init()
        lotto print Lotto.ExposeType.MAX_EXPOSE
        lotto print Lotto.ExposeType.MIN_EXPOSE
    }

    class Lotto {

        val lottoUrl = "http://www.nlotto.co.kr/common.do?method=getLottoNumber"
        val lottoTurnParam = "&drwNo="
        val number = "drwtNo"
        val bonusNumber = "bnusNo"
        val winnerCountParam = "firstPrzwnerCo"
        val numberMap = hashMapOf<Int, Int>()

        val savedLottoFile = File("save")
        val revFile = File("rev")

        enum class ExposeType {
            MAX_EXPOSE,
            MIN_EXPOSE
        }

        fun init() {
            val curTurnNumber = getCurrentTurnNumber()

            if (!revFile.exists()) { //create
                saveLottoData(1, curTurnNumber)
            } else if (revFile.exists() && revFile.readText().toInt() != curTurnNumber) { //update
                saveLottoData(revFile.readText().toInt() + 1, curTurnNumber)
            }

            pareAllData()
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

        fun printList(list: List<Pair<Int, Int>>) {
            var str = list[0].first.toString()

            if (list.size > 7) {
                // 중복일때의 출력
                //find fixedPos
                var fixedPos = 0
                var count = 0
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
                remains.forEach { println(str + "," + it.first) }

            } else {
                for (i in 1..list.size - 1) {
                    str += "," + list[i].first
                }
                println(str)
            }
        }
    }
}
