import com.sun.deploy.util.StringUtils
import sun.management.snmp.jvmmib.EnumJvmClassesVerboseLevel
import java.util.*
import kotlin.properties.Delegates
import kotlin.reflect.KProperty
import javax.script.ScriptEngineManager
import javax.script.ScriptException
import kotlin.system.exitProcess


enum class StringFunction {
    SUM {
        override fun calculate(list: List<Int>): Int {
            return list.toIntArray().sum();
        }
    },
    AVERAGE {
        override fun calculate(list: List<Int>): Int {
            return list.toIntArray().average().toInt();
        }
    },
    MAX {
        override fun calculate(list: List<Int>): Int {
            return list.toIntArray().max()!!.toInt();
        }
    },
    MIN {
        override fun calculate(list: List<Int>): Int {
            return list.toIntArray().min()!!.toInt();
        }
    };

    abstract fun calculate(list: List<Int>): Int
}

class Cell(input: String, index: Char, function: StringFunction?, functionRange: CharRange?) {
    var express by Delegates.observable(input) {
        prop, old, new ->

        for (function in StringFunction.values()) {
            if (new.contains(function.name)) {
                var rangeList = listOf(new.get(new.indexOf(':') - 1), new.get(new.indexOf(':') + 1))
                this.function = function;
                this.functionRange = CharRange(rangeList.first(), rangeList.last());
                break
            }
        }

        Sheet.onChangeItem(this, old!!.toString(), new!!.toString())
    }
    var index = index

    var function = function
    var functionRange = functionRange
}

object Engine {
    val js = ScriptEngineManager().getEngineByName("JavaScript")!!
}

interface OnChangeItemListener {
    fun onChangeItem(cell: Cell, old: String, new: String)
}


object Sheet : OnChangeItemListener {
    var cellMap = HashMap<Char, Cell>()
    override fun onChangeItem(cell: Cell, old: String, new: String) {
        if (cell.function == null || !new.contains(":")) {
            for ((key, value) in cellMap) {
                if (value != cell && value.express.contains(cell.index) && value.function == null) {
                    var newExpress = value.express.replace(cell.index.toString(), new)
                    newExpress.filter(Char::isLetter).forEach {
                        newExpress = newExpress.replace(it.toString(), calculate(cellMap[it]!!.express).toString())
                    }
                    onChangeItem(value, value.express, newExpress)
                }
            }
        }

        if (cell.function == null || !new.contains(":")) {
            print(cell.index + "=>" + calculate(new).toString() + ", ")
        } else {
            val numbers: MutableList<Int> = mutableListOf()
            cell.functionRange?.forEach {
                numbers.add(calculate(cellMap[it]!!.express))
            }

            var result = cell.function?.calculate(numbers)
            onChangeItem(cell, cell.express, result.toString())
        }

    }

    fun calculate(express: String): Int {
        var resultExpress = express
        resultExpress.filter(Char::isLetter).forEach {
            resultExpress = resultExpress.replace(it.toString(), calculate(cellMap[it]!!.express).toString())
        }

        try {
            return resultExpress.toInt()
        } catch(e: Exception) {
            try {
                return (Engine.js.eval(resultExpress) as Double).toLong().toInt()
            } catch(e: Exception) {
                print(e.toString() + " - " + resultExpress)
                return 0;
            }
        }
    }

}

fun main(args: Array<String>) {
    val input = "100, 20, 3023, (A+E), 10, -30, (F+10), 0, 12345"
    //val input = "1, 2, 3, (A+E), 10, -30, (F+10), 0, 12345"

    var cellList = input.replace(" ", "").replace("(", "").replace(")", "").split(",")
    for ((index, value) in cellList.withIndex()) {

        var isFunction = false;
        for (function in StringFunction.values()) {
            if (value.contains(function.name)) {
                var rangeList = listOf(value.get(value.indexOf(':') - 1), value.get(value.indexOf(':') + 1))
                Sheet.cellMap.put('A' + index, Cell(value, 'A' + index, function, CharRange(rangeList.first(), rangeList.last())))
                isFunction = true;
                break
            }
        }
        if (!isFunction) {
            Sheet.cellMap.put('A' + index, Cell(value, 'A' + index, null, null))
        }
    }

//    Problem 1
    Sheet.cellMap.get('A')?.express = "10"
    print("\n")
    Sheet.cellMap.get('C')?.express = "(A*E)"
    print("\n")
    Sheet.cellMap.get('E')?.express = "100"
    print("\n")

//    Problem 2
    Sheet.cellMap.get('D')?.express = "=MIN(A:C)"
    Sheet.cellMap.get('D')?.express = "=MAX(A:C)"
    Sheet.cellMap.get('D')?.express = "=AVERAGE(A:C)"
    Sheet.cellMap.get('D')?.express = "=SUM(A:C)"
}

