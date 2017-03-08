/**
 * Created by potea on 2017-03-07.
 *
 * 기본 설계 이후, 문법 적용에 시간이 오래 걸려 마무리 못함
 */

package problem1.potea

interface DataHandler {
    fun getCellResult(name: Char): Double
}

open class BaseCell(name: Char) {
    var handler: DataHandler? = null
    var hasResult: Boolean = false

    open fun printCell() {
        print("$name=>")
    }
}

class ValueCell(name: Char, result: Double) : BaseCell(name) {
    init {
        hasResult = true;
    }

    override fun printCell() {
        super.printCell()
        print("$result")
    }
}

class ExpressionCell(name: Char, exp: String) : BaseCell(name) {

    fun calc(): Double {

        // exp parse 진행
        // lValue, rValue를 문자와 숫자로 구분
        // lValue: Char, opValue: Char, rValue: Char

        // 둘다 문자일 경우 각 키 값을 이용해서 값을 찾음
        val lResult: Double = handler?.getCellResult(lValue)
        val rResult: Double = handler?.getCellResult(rValue)

        // 연산자 연산
        hasResult = true
        return when(opValue) {
            '+' -> lResult + rResult
            '*' -> lResult * rResult
            '-' -> lResult - rResult
            '/' -> {
                if (rResult != 0.0)
                    lResult / rResult
                else {
                    hasResult = false
                    0.0
                }
            }
            else -> {
                hasResult = false
                0.0
            }
        }
    }

    fun getResult(): String {
        if (handler != null)
            return "#err"

        val result = calc()

        if (!hasResult)
            return "#err"

        return result.toString()
    }

    override fun printCell() {
        super.printCell()
        print(getResult())
    }
}


class Cell {
    // 타입에 따른 BaseCell 값을 저장하고,
    // 자신의 셀을 참조하는 다른 셀에 전파하는 기능을 갖는다.
    // Observer pattern 적용 부분

    var cellValue: BaseCell? = null

    fun calc() {}
    fun setPropagation()
}

class Sheet : DataHandler{
    // Sheet에서 셀 값의 추가, Observer의 등록 및 삭제 관리 진행
    var cells = MutableMap<Char, Cell>

    override fun getCellResult(name: Char): Double {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        /* val left= when(lcell) {
            is ValueCell -> lcell.result
            is ExpressionCell -> lcell.calc()
        }*/
    }
}


fun main(args: Array<String>) {
    // 기본 값 파싱 및 Sheet에 등록
    // 변경 값을 Sheet를 통해 적용: Observer 관리,
    // 변경 값에 대해 Cell은 자신을 참조하는 셀에 전파


    val persons = listOf(Person("Alice"),
            Person("Bob", age = 29))

    val oldest = persons.maxBy { it.age ?: 0 }
    println("The oldest is: $oldest")
}