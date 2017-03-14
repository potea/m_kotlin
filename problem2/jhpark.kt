import javax.script.ScriptEngineManager
import javax.script.ScriptException

// Reactive System
interface Reaction {
    fun react()
}

object SimpleReactiveSystem {
    val reactMap = HashMap<Reaction, MutableList<Reaction>>()

    fun observeOn(target : Reaction, observer: Reaction) {
        reactMap[target] = reactMap[target] ?: ArrayList<Reaction>()
        reactMap[target]!!.add(observer)
    }

    fun propagate(target: Reaction) {
        reactMap[target]?.toList()?.forEach {
            it.react()
            propagate(it)
        }
    }
}
// End of Reactive System

// SpreadSheet
object Engine {
    val js = ScriptEngineManager().getEngineByName("JavaScript")!!

    enum class Func {
        SUM {
            override fun eval(l: Char, r: Char): Int? = cells(l, r)?.sum()
        }, AVERAGE {
            override fun eval(l: Char, r: Char): Int? = cells(l, r)?.average()?.toInt()
        }, MAX {
            override fun eval(l: Char, r: Char): Int? = cells(l, r)?.max()
        }, MIN { 
            override fun eval(l: Char, r: Char): Int? = cells(l, r)?.min()
        };

        abstract fun eval(l: Char, r: Char): Int?

        protected fun cells(l: Char, r: Char): List<Int>? {
            return Sheet.row?.subList(l - 'A', r - 'A')?.map(Cell::calc)
        }
    }
}

object Sheet {
    var row : List<Cell>? = null

    fun getLetter(cell: Cell): Char {
        return 'A' + row!!.indexOf(cell)
    }

    fun getCell(letter: Char): Cell {
        return row!![letter - 'A']
    }
}

abstract class Cell(var value: String) {
    var evaluated = ""

    protected abstract fun onParsed(cell: Cell)

    override fun toString(): String {
        return Sheet.getLetter(this) + "=>" + evaluated
    }

    fun evaluate() {
        try {
            evaluated = calc().toString()
        } catch (ignored: ArithmeticException) {
            evaluated = "#err"
        }
    }

    fun calc(): Int {
        if (value.startsWith("=")) {
            return parse(value.substring(1))
        } else {
            return value.toInt()
        }
    }

    private fun parse(value: String):Int {
        var result = replaceFunc(value)

        result.filter(Char::isLetter).forEach {
            result = result.replace(it.toString(), Sheet.getCell(it).calc().toString())

            onParsed(Sheet.getCell(it))
        }

        try {
            return Engine.js.eval(result) as Int
        } catch (e: ScriptException) {
            throw ArithmeticException()
        }
    }

    private fun replaceFunc(value: String): String {
        var result = value

        Engine.Func.values()
            .filter { value.contains(it.name) }
            .map { value.substring(value.indexOf(it.name), value.indexOf(")") + 1) }
            .forEach { funcExp ->
                result = result.replace(funcExp, Engine.Func.values()
                    .filter { funcExp.contains(it.name) }
                    .map { it.eval(funcExp[funcExp.indexOf(":") - 1], funcExp[funcExp.indexOf(":") + 1]) }
                    .toString()).replace("[","").replace("]","")
            }

        return result
    }
}
// End of SpreadSheet

// Application
object App {
    val log = HashSet<ReactiveCell>()

    class ReactiveCell(arg: String) : Cell(arg), Reaction {
        override fun onParsed(cell: Cell) {
            SimpleReactiveSystem.observeOn(cell as Reaction, this)
        }

        override fun react() {
            if (log.add(this)) {
                evaluate()
            }
        }
    }

    fun Sheet.replaceCell(command: String): Cell {
        with(parseUserInput(command) as Reaction) {
            SimpleReactiveSystem.propagate(this)
            return this as Cell
        }
    }

    private fun Sheet.parseUserInput(command: String): Cell {
        val split = command.split("=>")

        with(getCell(split[0][0])) {
            value = split[1]
            evaluate()
            return this
        }
    }
}
// End of Application

// Test
private fun App.printResult(input: List<String>) {
    Sheet.row = input[0].split(",").map { App.ReactiveCell(it) }
    Sheet.row!!.forEach(Cell::evaluate)

    println(Sheet.row)

    input.listIterator(1).forEach { command ->
        App.log.clear()

        val outputLog =
            command + " : " + Sheet.replaceCell(command).toString() + ", " +
            App.log.map { Sheet.getLetter(it) + "=>" + it.evaluated }

        println(outputLog.replace("[","").replace("]",""))
    }
}

fun main(args : Array<String>) {
    App.printResult("""
        |100, 20, 3023, =(A+E), 10, -30, =D+10, 0, 12345, =SUM(A:C)
        |A=>10
        |C=>=(A*E)
        |E=>100
        """.replace(" ", "").trimMargin().lines())
}
