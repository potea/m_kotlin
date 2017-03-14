package com.codemaker.reactivesample.kotlin

import com.codemaker.reactivesample.kotlin.expression.Expression
import java.util.*

/**
 * Created by kwi on 2017-03-05.
 */
class KwiCell : CellObserver, CellObservable() {

    var id: Int = 0
    var listener: UpdateListener? = null
    val registeredObserverList = ArrayList<Char>()
    var exp: Expression? = null

    fun setOnUpdateListener(listener: UpdateListener) {
        this.listener = listener
    }

    fun calc(): String {
        return Calculator.calc(exp)
    }

    fun getLabel(): Char {
        return 'A' + id
    }

    fun getExpression(): String? {
        return exp?.expString
    }

    fun setCellText(text: String) {
        clearRegisteredList()
        exp = Parser.parseAndRegister(text, this)
        onUpdated()
        notifyUpdate()
    }

    fun clearRegisteredList() {
        for (char in registeredObserverList) {
            KwiSeet.getCellByLabel(char)?.unregister(this)
        }
        registeredObserverList.clear()
    }

    fun addRegisteredList(char: Char) {
        registeredObserverList.add(char)
        KwiSeet.getCellByLabel(char)?.register(this)

    }

    fun checkCycle(target: KwiCell): Boolean {
        return target == this
    }

    override fun onUpdated() {
        listener?.onUpdated(this)
    }
}