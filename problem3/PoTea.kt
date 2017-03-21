/**
 * Created by potea on 2017-03-21.
 *
 * Problem 3
 */

import java.net.URL

const val MAX_EXPOSE = 1
const val MIN_EXPOSE = 0
const val LAST_LOTTO_GAME = 746 // 1 to 746

class LottoNumberSequence {
    var isInit = false
    var numbers = IntArray(46, { 0 })
    var weightNumbers = arrayOfNulls<Pair<Int, Int>>(44)

    infix fun print(expose: Int) {
        initNumbers()

        when (expose) {
            MAX_EXPOSE -> {
                println("MAX_EXPOSE")
                println(weightNumbers.sortedBy { it!!.second }.reversed().groupBy { it!!.second }.values)
                for (pair in weightNumbers.sortedBy { it!!.second }.reversed().subList(0, 7)) {
                    print("${pair!!.first}, ") // TODO: 위 values의 중첩을 풀어서 출력해야 하나. 시간관계상~
                }
                println("\n")
            }
            MIN_EXPOSE -> {
                println("MIN_EXPOSE")
                println(weightNumbers.sortedBy { it!!.second }.toList().groupBy { it!!.second }.values)
                for (pair in weightNumbers.sortedBy { it!!.second }.toList().subList(0, 7)) {
                    print("${pair!!.first}, ") // TODO: 위 values의 중첩을 풀어서 출력해야 하나. 시간관계상~
                }
                println("\n")
            }
        }
    }

    private fun initNumbers() {
        if (isInit)
            return

        for (n in 1..LAST_LOTTO_GAME) {
            getLottoData("http://www.nlotto.co.kr/common.do?method=getLottoNumber&drwNo=$n")
        }

        for (i in 1..44) weightNumbers[i-1] = Pair(i, numbers[i])

        isInit = true
    }

    // {"bnusNo":8,"firstWinamnt":1831451204,"totSellamnt":61846599000,"returnValue":"success","drwtNo3":17,"drwtNo2":13,"drwtNo1":5,"drwtNo6":36,"drwtNo5":28,"drwtNo4":23,"drwNoDate":"2015-04-04","drwNo":644,"firstPrzwnerCo":8}
    private fun getLottoData(url: String) {
        var weight = 1
        var nums = IntArray(7)
        val result = URL(url).readText()

        result.replace("\"", "").replace("{", "").replace("}","").split(",").toList().forEach {
            val sublist = it.split(":").toList()
            when (sublist[0]) {
                "firstPrzwnerCo" -> weight = if (sublist[1].toInt() == 0) 1 else sublist[1].toInt()
                "drwtNo1" -> nums[0] = sublist[1].toInt()
                "drwtNo2" -> nums[1] = sublist[1].toInt()
                "drwtNo3" -> nums[2] = sublist[1].toInt()
                "drwtNo4" -> nums[3] = sublist[1].toInt()
                "drwtNo5" -> nums[4] = sublist[1].toInt()
                "drwtNo6" -> nums[5] = sublist[1].toInt()
                "bnusNo" -> nums[6] = sublist[1].toInt()
            }
        }
        nums.forEach({ numbers[it] += weight })
    }
}

fun main(args: Array<String>) {
    val lottoNumberSequence : LottoNumberSequence = LottoNumberSequence()
    lottoNumberSequence print MAX_EXPOSE // 최다 노출 숫자형
    lottoNumberSequence print MIN_EXPOSE // 최소 노출 숫자형
}

/*
MAX_EXPOSE
[[(20, 881)], [(43, 866)], [(27, 859)], [(1, 854)], [(4, 852)], [(33, 843)], [(17, 835)], [(12, 827)], [(8, 826)], [(34, 825)], [(10, 824)], [(44, 817), (18, 817)], [(13, 796)], [(7, 788)], [(5, 776)], [(40, 774)], [(11, 772)], [(6, 767)], [(37, 745)], [(2, 739)], [(39, 734), (26, 734), (24, 734)], [(19, 723)], [(31, 720), (25, 720)], [(38, 716)], [(36, 710), (14, 710)], [(35, 709)], [(21, 702)], [(3, 694)], [(16, 682)], [(32, 664)], [(15, 659)], [(23, 643)], [(41, 641)], [(30, 637)], [(29, 619)], [(42, 614)], [(28, 604)], [(9, 586)], [(22, 572)]]
20, 43, 27, 1, 4, 33, 17,

MIN_EXPOSE
[[(22, 572)], [(9, 586)], [(28, 604)], [(42, 614)], [(29, 619)], [(30, 637)], [(41, 641)], [(23, 643)], [(15, 659)], [(32, 664)], [(16, 682)], [(3, 694)], [(21, 702)], [(35, 709)], [(14, 710), (36, 710)], [(38, 716)], [(25, 720), (31, 720)], [(19, 723)], [(24, 734), (26, 734), (39, 734)], [(2, 739)], [(37, 745)], [(6, 767)], [(11, 772)], [(40, 774)], [(5, 776)], [(7, 788)], [(13, 796)], [(18, 817), (44, 817)], [(10, 824)], [(34, 825)], [(8, 826)], [(12, 827)], [(17, 835)], [(33, 843)], [(4, 852)], [(1, 854)], [(27, 859)], [(43, 866)], [(20, 881)]]
22, 9, 28, 42, 29, 30, 41,
 */
