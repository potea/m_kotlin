package com.codemaker.reactivesample.kotlin

/**
 * Created by kwi on 2017-03-07.
 */
object KwiSeet {
    const val SIZE: Int = 26
    val kwiCellMap = hashMapOf<Char, KwiCell>()

    init {
        for (i in 0..SIZE - 1) {
            var kwiCel: KwiCell = KwiCell()
            kwiCel.id = i
            kwiCellMap.put(kwiCel.getLabel(), kwiCel)
        }
    }

    fun getCellByLabel(label: Char): KwiCell? {
        try {
            return kwiCellMap.getValue(label)
        } catch (e: NoSuchElementException) {
            return null
        }
    }

    fun getCellById(id: Int): KwiCell? {
        return getCellByLabel('A' + id)
    }

    fun getSize(): Int {
        return kwiCellMap.size
    }
}