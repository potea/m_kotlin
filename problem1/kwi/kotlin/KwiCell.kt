package com.codemaker.reactivesample.kotlin

import com.codemaker.reactivesample.kotlin.KwiCells.ERROR_MSG
import java.util.*

/**
 * Created by kwi on 2017-03-05.
 */
class KwiCell : CellObserver, CellObservable() {

    var id: Int = 0
    var listener: UpdateListener? = null
    var expression: String = ""
    var isError: Boolean = false
    var isExpression: Boolean = false
    val expressionList = ArrayList<Char>()

    fun setOnUpdateListener(listener: UpdateListener) {
        this.listener = listener
    }

    fun cal(): String {

        if(isError) {
            return ERROR_MSG
        }

        if(isExpression) {//치환
            var temp = ""
            for(char in expression) {
                if(KwiCells.getCellByLabel(char) != null) {
                    temp += KwiCells.getCellByLabel(char)?.cal()
                } else {
                    temp += char
                }
            }

            return temp
        }

        return expression
    }

    fun getLabel(): Char {
        return 'A' + id
    }

    fun setCellText(text: String) {
        isError = !parse(text)
        expression = text
        onUpdated()
        notifyUpdate()
    }

    fun parse(text: String): Boolean {

        if (text.startsWith('(')) {

            clearExpressionList()

            var temp = text.trim('(', ')', ' ')
            temp.replace(" ", "")

            for(char in temp) {
                if(KwiCells.getCellByLabel(char) != null) {
                    if(checkCycle(KwiCells.getCellByLabel(char)!!)) {
                        clearExpressionList()
                        return false
                    }
                    expressionList.add(char)
                    KwiCells.getCellByLabel(char)?.register(this)
                }
            }

            isExpression = true
            return true
        }

        isExpression = false
        return true
    }

    fun clearExpressionList() {
        for(char in expressionList) {
            KwiCells.getCellByLabel(char)?.unregister(this)
        }
        expressionList.clear()
    }

    fun checkCycle(target: KwiCell): Boolean {
        return target == this || isRegistered(target)
    }

    override fun onUpdated() {
        listener?.onUpdated(this)
    }
}