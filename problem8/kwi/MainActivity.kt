package com.codemaker.lottomaker

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.codemaker.lottomaker.kotlin.Lotto

class MainActivity : AppCompatActivity() {
    var lotto: Lotto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val revFile = getFileStreamPath("revFile")
        val saveFile = getFileStreamPath("saveFile")
        lotto = Lotto(revFile, saveFile)
        LottoAsyncTask().execute()
    }

    fun updateUi() {
        (findViewById(R.id.text_view_last_lotto) as TextView).text = lotto?.getLastWinnerLotto()
        (findViewById(R.id.text_view_recommend_max_lotto) as TextView).text = lotto?.getRecommendLotto(Lotto.ExposeType.MAX_EXPOSE)
        (findViewById(R.id.text_view_recommend_min_lotto) as TextView).text = lotto?.getRecommendLotto(Lotto.ExposeType.MIN_EXPOSE)
        (findViewById(R.id.text_view_random_lotto) as TextView).text = lotto?.getRandomLotto()
    }

    inner class LottoAsyncTask : AsyncTask<Void, Void, Boolean>() {

        override fun onPreExecute() {
            super.onPreExecute()
            findViewById(R.id.progress).visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: Void?): Boolean? {
            return lotto?.init()
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)
            findViewById(R.id.progress).visibility = View.GONE
            updateUi()
        }
    }
}
