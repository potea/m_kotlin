package com.bross.lottorecommendsystem

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.bross.lottorecommendsystem.db.DAO
import com.bross.lottorecommendsystem.db.Lotto
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.getAs
import io.realm.Realm
import kotlinx.android.synthetic.main.content_main.*
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.FileWriter
import java.net.URL
import java.util.*
import kotlin.collections.HashMap


fun String.removeBrackets(): String {
    return replace("[", "").replace("]", "")
}


class MainActivity : AppCompatActivity() {

    val url = "http://www.nlotto.co.kr/common.do?method=getLottoNumber&drwNo"
    var lastLottoCount = 0
    var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DAO.init(applicationContext)
        FileCache.init("$cacheDir/lotto_number_cache.dat", this)
        visitCount()
        showLastLottoNumber()
        randomButtonClick()
        asyncTask(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (dialog?.isShowing as Boolean) {
            dialog?.dismiss()
        }
        DAO.close()
    }


//    fun clickEventSetting() {
//        button_db_search.setOnClickListener {
//            val number = edit_text_input_number.text.toString().toInt()
//            edit_text_input_number.setText(DAO.select(number).toString())
//
//        }
//        button_db_insert.setOnClickListener {
//            //            val number = edit_text_input_number.text.toString().toInt()
////            lottoNumberInsert(number)
//        }
//    }

    fun visitCount() {
        val PREFERENCE_FILE_NAME = "com.bross.lottorecommendsystem.prefs"
        val VALUE_NAME = "VISIT_COUNT"
        val sharedPreferences: SharedPreferences? = getSharedPreferences(PREFERENCE_FILE_NAME, 0)
        val visitCnt = sharedPreferences?.getInt(VALUE_NAME, 0)?.plus(1)
        val editor = sharedPreferences?.edit()
        editor?.putInt(VALUE_NAME, visitCnt!!)
        editor?.apply()
        text_view_visit_count.text = visitCnt.toString()
    }


    fun parseLottoNumber(json: String): List<Int> {

        Log.i("kyk", json)
        var numberList: List<Int> = ArrayList()
        val jsonObject = JSONObject(json)
        val keyString = "drwtNo" // json 파싱
        for (n in 1..6) {
            numberList += jsonObject.get(keyString + n).toString().toInt()
        }
        numberList += jsonObject.get("bnusNo").toString().toInt()
        lastLottoCount = jsonObject.get("drwNo").toString().toInt()
        return numberList
    }

    fun parsePossible(json: String): Boolean {
        try {
            val jsonObject = JSONObject(json)

            return jsonObject.get("returnValue").equals("success")
        } catch (e: Exception) {
            return false
        }
    }

    fun parseLottoWinCount(json: String): Int {
        val jsonObject = JSONObject(json)
        return jsonObject.get("firstPrzwnerCo").toString().toInt()
    }

    fun parseLottoCount(json: String): Int {
        val jsonObject = JSONObject(json)
        return jsonObject.get("drwNo").toString().toInt()
    }


    fun showLastLottoNumber() {
        url.httpGet().responseString { request, response, result ->
            when (result) {
                is Result.Failure -> {
                    text_view_last_lotto_number.text = result.getAs()
                }
                is Result.Success -> {
                    val (data, error) = result
                    if (parsePossible(data!!)) {
                        text_view_last_lotto_number.text = parseLottoNumber(data!!).toString()
                    }
                }
            }
        }
    }


    fun randomButtonClick() {
        fun onClick() {
            fun <T : Comparable<T>> shuffle(items: MutableList<T>): List<T> {
                val rg: Random = Random()
                for (i in 0..items.size - 1) {
                    val randomPosition = rg.nextInt(items.size)
                    val tmp: T = items[i]
                    items[i] = items[randomPosition]
                    items[randomPosition] = tmp
                }
                return items
            }

            fun lotto(count: Int, maxNumber: Int): String {
                val range = (1..maxNumber).toList()
                val shuffled = shuffle(range as MutableList<Int>)
                return shuffled.subList(0, count).toString()
            }
            text_view_random_number.text = lotto(4, 10)
        }
        button_random_number.setOnClickListener {
            onClick()
        }
        onClick()
    }

    fun lottoSequence() {
        Realm.init(this)
        val realm = Realm.getDefaultInstance()
        val lottoNumbers = realm.where(Lotto::class.java).lessThan("count", lastLottoCount).findAll()

    }


    fun lottoNumberInsert(number: Int) {
        val requestUrl = "$url=$number"

        requestUrl.httpGet().responseString { request, response, result ->
            when (result) {
                is Result.Failure -> {
                    val (data, error) = result
                }
                is Result.Success -> {
                    val (data, error) = result
                    if (parsePossible(data!!)) {
                        val numberList = parseLottoNumber(data!!)
                        val winCount = parseLottoWinCount(data!!)
                        val lottoCount = parseLottoCount(data!!)
                        Log.i("kyk", "lottoCount $lottoCount, win count : $winCount, numberList : ${numberList.toString()}")

                        DAO.insertAsyncTask(lottoCount, winCount, numberList)

                    }
                }
            }
        }
    }


    object FileCache {
        var FILE_NAME: String? = null

        fun init(fileName: String, activity: Activity) {
            FILE_NAME = fileName
            copyAssets(activity)
        }

        fun exists(): Boolean {
            val file = File(FILE_NAME)
            return file.exists()
        }

        fun copyAssets(activity: Activity) {
            if (!exists()) {
                val reader = activity?.assets?.open("lotto_number_cache.dat")?.bufferedReader()
                reader?.readText()?.let { write(it) }
            }
        }

        fun write(str: String) {
            val fileWriter = FileWriter(FILE_NAME, true)
            fileWriter.append(str + "\n")
            fileWriter.close()
        }

        fun readAll(): List<String>? {
            var strList: List<String>? = null
            try {
                val fileReader = FileReader(FILE_NAME)
                strList = fileReader.readLines()
                fileReader.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

            return strList

        }
    }

    //lotto api entity class
    data class LottoNumber
    (var json: String = "", var numberList: List<Int> = ArrayList(), var winCount: Int = 0, var date: String = "") {
        override fun toString(): String = numberList.toString().removeBrackets()
        fun parse(str: String) {
            Log.i("kyk","parse "+str)
            val json = JSONObject(str)
            val keyString = "drwtNo" // json 파싱
            for (n in 1..6) {
                numberList += json.get(keyString + n).toString().toInt()
            }
            numberList += json.get("bnusNo").toString().toInt()
            winCount = json.get("firstPrzwnerCo").toString().toInt()
            date = json.get("drwNoDate").toString()
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
        fun add(cnt: Int) {
            count += cnt
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

    class LottoSequence(count: Int) {
        val LOTTO_MAX_COUNT = 45 // max lotto number 45
        var lottoMap: MutableMap<Int, LottoNumber> = HashMap() // 회차별 lotto map
        var lottoCntList: Array<LottoCount> = emptyArray() // lotto count list // heap


        init {

            FileCache.readAll()?.forEachIndexed { index, s ->
                val lottoNumber = LottoNumber()
                if(!s.isEmpty()) {
                    lottoNumber.parse(s)
                    lottoMap.put(index, lottoNumber)
                }
            }

            val start = lottoMap.keys.max() ?: 1 // key의 max값 부터 시작 하거나 값이 없을경우 1부터 시작
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
            } catch (e: NumberFormatException) {
                e.printStackTrace()
                return false
            }
            return true
        }

        fun sequenceCounting() { // 수열 카운트
            for (n in 1..LOTTO_MAX_COUNT) {
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


        fun getLottoSequence(menu: Type): String {
            when (menu) {
                Type.MIN_EXPOSE -> return lottoCntList.sortedArray().toList().subList(0, 7).toString().removeBrackets() // convert min heap print
                Type.MAX_EXPOSE -> return lottoCntList.sortedArray().reversed().subList(0, 7).toString().removeBrackets() // conver max heap print
            }
            return ""
        }

    }

    enum class Type {
        MIN_EXPOSE, MAX_EXPOSE
    }

    data class InsertLotto(val winCount: Int, val numberList: List<Int>)

    fun asyncTask(activity: Activity) {
        object : AsyncTask<Void, Void, Boolean>() {

            var MIN_EXPOSE: String = ""
            var MAX_EXPOSE: String = ""
            var lastCount: Int = 0


            fun getLastCount() {
                url.httpGet().responseString { request, response, result ->
                    when (result) {
                        is Result.Failure -> {
                        }
                        is Result.Success -> {
                            val (data, error) = result
                            if (parsePossible(data!!)) {
                                lastCount = parseLottoCount(data!!)
                            }
                        }
                    }
                }

            }

            override fun onPreExecute() {
                super.onPreExecute()
                dialog = ProgressDialog(activity)
                getLastCount()
                dialog?.show()
            }

            override fun doInBackground(vararg params: Void): Boolean? {

                val lottoSequence = LottoSequence(lastCount)

                MIN_EXPOSE = lottoSequence.getLottoSequence(Type.MIN_EXPOSE)
                MAX_EXPOSE = lottoSequence.getLottoSequence(Type.MAX_EXPOSE)

                return true
            }

            override fun onPostExecute(result: Boolean) {
                super.onPostExecute(result)
                text_view_lotto_sequence_max.setText(MAX_EXPOSE)
                text_view_lotto_sequence_min.setText(MIN_EXPOSE)
                dialog?.dismiss()

            }
        }.execute()
    }

}
