package exam_2

import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import kotlin.properties.Delegates


/**
 * Created by bross on 2017. 3. 2..
 */

// Script engine  - single ton
class Engine private constructor() {
    val engine: ScriptEngine = ScriptEngineManager().getEngineByName("JavaScript")
    fun eval(extension: String): Int {
        return engine.eval(extension) as Int
    }

    private object Holder {
        val INSTANCE = Engine()
    }

    companion object {
        val instance: Engine by lazy { Holder.INSTANCE }
    }
}

//Cell , key , sheet 참조
class Cell(val key: Char, val sheet: Sheet) {
    var expression: String by Delegates.observable("") { _, old, new -> // 표현식에 observer를 설정

        if (old.contains(":")) { // old가 범위형식의 함수식이였으면 기존에 참조하고있던 셀에서 현재 셀을 지운다
            val leftCell: Char = old.getOrNull(old.indexOf('(') + 1)!!
            val rightCell: Char = old.getOrNull(old.indexOf(')') - 1)!!
            for (x in leftCell..rightCell) {
                sheet.getCell(x).propagationList -= key
            }
        } else { // 제거된 셀 목록에서 현재셀 참조를 제거
            var oldRefList = old.toCharArray().filter(Char::isLetter)
            val newRefList = new.toCharArray().filter(Char::isLetter)
            oldRefList -= newRefList
            oldRefList.forEach { sheet.getCell(it).propagationList -= it }
        }

        printExtension() // 현재 표현식을 출력
        calc()  // 계산
        when (isError) {
            false -> print(" => $value") // 값이 있을경우
            true -> print(" => #err ")  // 에러일 경우
        }
        propagationList.forEach { // 현재셀을 참조하고있는 셀에 전파
            it ->
            val cell = sheet.getCell(it)
            cell.calc()
            cell.isError = this.isError // 현재셀이 에러이면 전파셀도 전부 에러로 판단
            cell.printKeyValue() // 셀과 값 출력
        }
        println()
    }

    var value: Int = 0
    var propagationList: Set<Char> = HashSet()
    var isError: Boolean = false

    fun calc() {
        isError = false
        try {
            value = expression.toInt() // int형으로 변환 시도
        } catch (e: NumberFormatException) { // int형으로 변환이안되면 식이거나 함수
            if (expression.contains(':')) { // : 을 포함하고있으면 함수
                runFunction()   // 함수 식 실행
            } else {
                runCalc()       // 계산식 실행
            }
        }
    }


    fun runFunction() { // 함수이름 추출, 범위
        val functionName = expression.substring(0, expression.indexOf('(')).toUpperCase().replace("=", "")
        val startCell: Char = expression.getOrNull(expression.indexOf('(') + 1)!!
        val endCell: Char = expression.getOrNull(expression.indexOf(')') - 1)!!

        var range: List<Char> = ArrayList() // 범위 배열식으로 변환
        (startCell..endCell).forEach { range += it }
        var rangeValue: List<Int> = ArrayList() // 범위에 자신을 propagation list에 등록
        range.forEach {
            sheet.getCell(it).propagationList += (key)
            rangeValue += sheet.getCellValue(it)
        }

        //함수명에 맡는 식 실행
        when (functionName) {
            "SUM" -> value = rangeValue.sum()
            "AVERAGE" -> value = rangeValue.average().toInt()
            "MIN" -> value = rangeValue.min()!!
            "MAX" -> value = rangeValue.max()!!
        }
    }

    fun runCalc() { // 시트 참조식을 숫자로 변환후 계산 -> 계산실패시 error 처리
        var numberExpression: String = expression.replace("=", "")
        numberExpression.filter(Char::isLetter).forEach {
            numberExpression = numberExpression.replace(it.toString(), sheet.getCellValue(it).toString())
        }
        try {
            value = Engine.instance.eval(numberExpression)
        } catch (e: ClassCastException) {
            isError = true
        }

    }

    fun printKeyValue() { // value print 함수
        if (isError) {
            print(" $key => #err")
        } else {
            print(" $key => $value")
        }
    }

    fun printExtension() { // 표현식 출력
        print("$key => $expression")
    }
}

// Sheet
class Sheet {
    var cells: List<Cell> = ArrayList() // 한 시트는 여러개의 cell을 가지고 있음
    fun getCellValue(key: Char): Int {  // key값으로 cell을 가져옴
        return cells[key - 'A'].value
    }

    fun getCell(key: Char): Cell {
        return cells[key - 'A']
    }

    init {  // 시트를 생성시에 A-Z까지 셀을 만듬
        for (x in 'A'..'Z') {
            cells += Cell(x, this)
        }
    }

}


fun main(args: Array<String>) {


    val sheet : Sheet = Sheet()


    sheet.getCell('A').expression = "10"
    sheet.getCell('B').expression = "20"
    sheet.getCell('C').expression = "50"
    sheet.getCell('D').expression = "A+C"
    sheet.getCell('E').expression = "=SUM(A:C)"
    sheet.getCell('F').expression = "=MIN(A:C)"
    sheet.getCell('G').expression = "=MAX(A:D)"
    sheet.getCell('H').expression = "=AVERAGE(A:C)"
    sheet.getCell('D').expression = "10"
    sheet.getCell('C').expression = "100"
    sheet.getCell('C').expression = "10/0"


}
