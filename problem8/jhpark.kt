package com.example.register.chapter8

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import org.jetbrains.anko.*
import java.net.URL
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        doAsync {
            val lastNumbers = initLastNumbers()

            activityUiThread  {
                (findViewById(R.id.lastNumber) as TextView).text = "마지막 당첨번호=${lastNumbers.toString()}"
            }
        }

        with((findViewById(R.id.random) as Button)) {
            onClick {
                alert(message = "랜덤 로또 번호 추첨", title = "문제 8") {
                    positiveButton("확인하기") { toast(randomLotto(7, 46)) }
                    cancelButton()
                }.show()
            }
        }
    }

    fun initLastNumbers(): MutableList<Int> {
        val api = "http://www.nlotto.co.kr/common.do?method=getLottoNumber&drwNo"
        val lastDrwNo = SimpleJson(URL(api).readText())["drwNo"]?.toInt()
        return Lotto(SimpleJson(URL("$api=$lastDrwNo").readText())).numbers
    }

    class SimpleJson(src: String) {
        val associated: Map<String, String>
                = src.replace("\"", "").replace("{","").replace("}","").split(",")
                .associateBy({ it.split(":")[0] }, { it.split(":")[1] })

        operator fun get(s: String): String? {
            return associated[s]
        }
    }

    class Lotto(src: SimpleJson) {
        val numbers = (1..6).map { src["drwtNo$it"]?.toInt() ?: 0 }.toMutableList()

        init {
            numbers.add(src["bnusNo"]?.toInt() ?: 0)
        }
    }

    fun randomLotto(count: Long, bound: Int): String
            = Random().ints(1, bound).distinct().limit(count).toArray().joinToString(transform = Int::toString)
}

