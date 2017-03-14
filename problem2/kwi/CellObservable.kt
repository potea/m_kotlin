package com.codemaker.reactivesample.kotlin

import java.util.*

/**
 * Created by kwi on 2017-03-07.
 */
open class CellObservable {
    val ObserverList = ArrayList<CellObserver>()

    fun register(observer: CellObserver): Boolean {
        if (!ObserverList.contains(observer)) {
            return ObserverList.add(observer)
        }
        return false
    }

    fun unregister(observer: CellObserver): Boolean {
        return ObserverList.remove(observer)
    }

    fun notifyUpdate() {
        for (observer in ObserverList) {
            observer.onUpdated()
        }
    }

    fun isRegistered(observer: CellObserver): Boolean {
        return ObserverList.contains(observer)
    }
}