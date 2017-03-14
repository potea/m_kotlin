/**
 * Created by potea on 2017-03-07.
 *
 * 문제2: 문제1 풀이에 함수 처리 추가
 */

import javax.script.ScriptEngineManager
import javax.script.ScriptException

object Engine {
    val js = ScriptEngineManager().getEngineByName("JavaScript")!!
}

enum class Func {
    SUM {
        override fun calculation(array: IntArray): Int {
            return array.sum()
        }
    },
    AVERAGE {
        override fun calculation(array: IntArray): Int {
            return array.average().toInt()
        }
    },
    MAX {
        override fun calculation(array: IntArray): Int {
            return array.max()!!.toInt()
        }
    },
    MIN {
        override fun calculation(array: IntArray): Int {
            return array.min()!!.toInt()
        }
    };

    abstract fun calculation(array: IntArray): Int
}

open class ReactCell {
    val reactSet = mutableSetOf<Char>()

    fun connect(name: Char) {
        reactSet.add(name)
    }

    fun disconnect(name: Char) {
        reactSet.remove(name)
    }

    fun propagation() {
        reactSet.forEach {
            Sheet.updateCell(it)
        }
    }
}

class Cell(var name: Char, var cell: String) : ReactCell() {

    fun initialize() {
        if (isExpression()) {
            val list = getCellNames();
            list.forEach {
                Sheet.connectCell(it, name)
            }
        }
    }

    fun change(data: String) {
        if (isExpression()) {
            val list = getCellNames();
            list.forEach {
                Sheet.disconnectCell(it, name)
            }
        }

        // change cell
        cell = data

        initialize()
        calculation()
    }

    fun calculation() {
        println("$name=>${getResult()}")
        propagation()
    }

    fun getResult(): String {
        try {
            return getValue().toString()
        } catch (ignored: ArithmeticException) {
            return "#err"
        }
    }

    private fun getValue(): Int {
        if (!isExpression()) {
            return cell.toInt()
        }
        else {
            var result = cell.substring(1)
            if (cell.contains(":")) {
                val list = getCellNames()
                var values = IntArray(list.size)
                for (index in list.indices) {
                    val exp = Sheet.getResult(list[index])
                    if (exp == "#err") {
                        throw ArithmeticException()
                    }
                    else {
                        values[index] = exp.toInt()
                    }
                }
                for (func in Func.values()) {
                    if (cell.contains(func.name)) {
                        return func.calculation(values)
                    }
                }
            }
            else {
                cell.filter(Char::isLetter).forEach {
                    result = result.replace(it.toString(), Sheet.getResult(it))
                }

                try {
                    return Engine.js.eval(result) as Int
                } catch (e: ScriptException) {
                    throw ArithmeticException()
                }
            }
        }
        throw ArithmeticException()
    }

    private fun isExpression(): Boolean {
        return cell.startsWith("=")
    }

    private fun getCellNames(): List<Char> {
        var result = cell.substring(1)

        for (func in Func.values()) {
            if (cell.contains(func.name)) {
                result = result.replace(func.name, "").replace("(", "").replace(")", "")
                return CharProgression.fromClosedRange(result[0], result[2], 1).toList()
            }
        }

        return result.filter(Char::isLetter).toList()
    }
}

object Sheet {
    val sheet = hashMapOf<Char, Cell>()

    fun connectCell(name: Char, observer: Char) {
        sheet[name]?.connect(observer)
    }

    fun disconnectCell(name: Char, observer: Char) {
        sheet[name]?.disconnect(observer)
    }

    fun updateCell(name: Char) {
        sheet[name]?.calculation()
    }

    fun getResult(name: Char): String {
        return sheet[name]?.getResult() ?: "#err"
    }

    fun changeCell(name: Char, data: String) {
        if (sheet.containsKey(name)) {
            sheet[name]?.change(data)
        }
        else
        {
            sheet[name] = Cell(name, data)
        }
    }

    fun calculation(data: List<String>) {
        // 초기값 추가
        val cells = data[0].split(",").toList()
        for (i in cells.indices) {
            changeCell('A' + i, cells[i])
        }
        // 초기값 초기화
        for (pair in sheet.toList()) {
            pair.second.initialize()
        }

        // 변경값 설정
        data.listIterator(1).forEach{
            println("$it :")
            val parts = it.split("=>").toList()
            changeCell(parts[0][0], parts[1])
        }
    }
}

fun main(args: Array<String>) {
    val data = """
        |100, 20, 3023, =(A+E), 10, -30, =(D+10), 0, 12345
        |A=>10
        |C=>=(A*E)
        |E=>100
        |H=>=SUM(B:E)
        |H=>=AVERAGE(B:E)
        |H=>=MAX(B:E)
        |H=>=MIN(B:E)
        """.replace(" ", "").trimMargin().lines()

        Sheet.calculation(data)
}