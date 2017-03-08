import com.sun.deploy.util.StringUtils
import java.util.*
import kotlin.properties.Delegates
import kotlin.reflect.KProperty


class Cell(index: Int, input: String, callback: OnChangeItemListener) {
    var callback = callback
    var index = index;
    var intValue by Delegates.observable(convertInt(input)) {
        prop, old, new ->
        callback.onChangeItem(index, old!!.toInt(), new!!.toInt())
    }
    var fixed = (intValue is Int)

    var firstExpressionIndex = convertFirstIndex(input)
    var secondExpressionIndex = convertSecondIndex(input)
    var expressionOperator = getOperator(input)
    fun convertFirstIndex(str: String): Int {
        if (fixed) {
            return -1;
        }
        return str.toCharArray().get(0).toInt() - 'A'.toInt();
    }

    fun getOperator(str: String): String {
        if (fixed) {
            return "";
        }
        return str.toCharArray().get(1).toString();
    }


    fun convertSecondIndex(str: String): Int {
        if (fixed) {
            return -1;
        }
        return str.toCharArray().get(2).toInt() - 'A'.toInt();
    }
}

interface OnChangeItemListener {
    fun onChangeItem(index: Int, old: Int, new: Int)
}

fun convertInt(str: String): Int? {
    try {
        return str.toInt()
    } catch(e: NumberFormatException) {
        return null;
    }
}

class Sheet() : OnChangeItemListener {

    lateinit var cellArray: Array<Cell?>
    var parseV1Index: Int = 0
    var parseV1Value: Int = 0

    override fun onChangeItem(index: Int, old: Int, new: Int) {
        for ((index, value) in cellArray.withIndex()) {
            if (cellArray[index]?.firstExpressionIndex === index) {

            } else if (cellArray[index]?.secondExpressionIndex === index) {

            }
        }
    }

    fun cal(str: String) {
        //calculate cell
    }
}

fun main(args: Array<String>) {
    val input = "100, 20, 3023, (A+E), 10, -30, (D+10), 0, 12345"

    var cellList = input.replace(" ", "").replace("(", "").replace(")", "").split(",")
    var cellArray = arrayOfNulls<Cell>(cellList.size)

    val sheet = Sheet()

    for ((index, value) in cellList.withIndex()) {
        cellArray[index] = Cell(index, value, sheet)
    }
    sheet.cellArray = cellArray


    val v1 = "A=>10"
    var parseV1 = v1.split("=>").toTypedArray();
    var parseV1Index = parseV1[0].toCharArray().get(0).toInt() - 'A'.toInt();
    var parseV1Value = parseV1[1].toInt();

    sheet.cal(v1)
}

