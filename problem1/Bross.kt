package exam_1

import javax.script.ScriptEngineManager
import kotlin.properties.Delegates


/**
 * Created by bross on 2017. 3. 2..
 */

interface ReactiveValueChangedListener {
    fun onValueChanged(key: Char, newValue: Double)
}

class PrintingReactiveValueChangedListener : ReactiveValueChangedListener {
    override fun onValueChanged(key: Char, newValue: Double) = print("$key => $newValue\t")
}

class ReactiveValue(val map: Map<Char, ReactiveValue>) {

    var listener: ReactiveValueChangedListener? = PrintingReactiveValueChangedListener()

    var referenceSet: Set<ReactiveValue> = HashSet()


    var extension: String by Delegates.observable("") { _, old, new ->
        val oldRefList = old.toCharArray().asList().stream().filter { it -> it != ('(') && it != ')' }.filter { it -> it in 'A'..'Z' }.toArray().asList()
        val newRefList = new.toCharArray().asList().stream().filter { it -> it != ('(') && it != ')' }.filter { it -> it in 'A'..'Z' }.toArray().asList()
        printExtension(new)
        oldRefList.filter { it -> !newRefList.contains(it) }.forEach { it -> removeRef(it as Char) }
        calculation()

        if (result != Double.POSITIVE_INFINITY) {
            listener?.onValueChanged((map.values.indexOf(this) + 'A'.toInt()).toChar(), result)
        }
        referenceSet.forEach {
            it ->
            val key: Char = map.values.indexOf(it).toChar() + 'A'.toInt()
            val newValue = it.calculation()
            print("$key => $newValue\t")
        }
        println()
    }

    var result: Double = Double.NaN

    fun calculation(): Double {
        if (extension.contains('(')) {
            val refList = extension.toCharArray().asList().stream().filter { it -> it != ('(') && it != ')' }.filter { it -> it in 'A'..'Z' }.toArray().asList()

            var numberExtension = extension
            refList.forEach { it ->
                run {
                    addRef(it as Char)
                    numberExtension = numberExtension.replace(it.toString(), cellValue(it).toString())
                }
            }
            try {
                result = ScriptEngineManager().getEngineByName("JavaScript").eval(numberExtension) as Double
                if (result == Double.POSITIVE_INFINITY) {
                    println("$extension => #err")
                }
            } catch (e: java.lang.NumberFormatException) {
                println("$extension => #err")
            }
        } else {

            try {
                result = extension.toDouble()
            } catch (e: java.lang.NumberFormatException) {
                println("$extension => #err")
            }
        }


        return result
    }

    fun cellValue(cell: Char): Double? {
        return map[cell]?.result
    }

    fun addRef(reactiveValue: ReactiveValue) {
        referenceSet += reactiveValue
    }

    fun addRef(cell: Char) {
        getCell(cell).addRef(this)
    }

    fun removeRef(reactiveValue: ReactiveValue) {
        referenceSet -= reactiveValue
    }

    fun getCell(cell: Char): ReactiveValue {
        return map[cell] as ReactiveValue
    }

    fun removeRef(cell: Char) {
        getCell(cell).removeRef(this)
    }

    fun printExtension(newExtension: String) {
        val key = (map.values.indexOf(this) + 'A'.toInt()).toChar()
        print("$key => $newExtension\t")
    }


}

fun main(args: Array<String>) {


    val reactiveValueMap = HashMap<Char, ReactiveValue>()

    for (x in 1..9) {
        reactiveValueMap.put(reactiveValueMap.size.toChar() + 'A'.toInt(),
                ReactiveValue(reactiveValueMap))
    }

//    100, 20, 3023, (A+E), 10, -30, (D+10), 0, 12345
    reactiveValueMap['A']?.extension = "100"
    reactiveValueMap['B']?.extension = "20"
    reactiveValueMap['C']?.extension = "3023"
    reactiveValueMap['D']?.extension = "(A+E)"
    reactiveValueMap['E']?.extension = "10"
    reactiveValueMap['F']?.extension = "-30"
    reactiveValueMap['G']?.extension = "(D+10)"
    reactiveValueMap['H']?.extension = "0"
    reactiveValueMap['I']?.extension = "12345"
    reactiveValueMap['I']?.extension = "(12345/0)"


}
