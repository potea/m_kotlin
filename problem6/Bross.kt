package exam_6

import java.util.*

/**
 * Created by bross on 2017. 4. 10..
 */

val MAX: Int = 100
val MIN: Int = 1

fun <T : Comparable<T>> List<T?>.binarySearch(element: T?, fromIndex: Int = 0, toIndex: Int = size): String {
    rangeCheck(size, fromIndex, toIndex)

    var low = fromIndex
    var high = toIndex - 1

    var result = ""

    while (low <= high) {
        val mid = (low + high).ushr(1) // safe from overflows
        val midVal = get(mid)
        val cmp = compareValues(midVal, element)

        result += "$mid "

        if (cmp < 0) {
            low = mid + 1
        } else if (cmp > 0) {
            high = mid - 1
        } else
            break
    }
    return result // key not found
}

private fun rangeCheck(size: Int, fromIndex: Int, toIndex: Int) {
    when {
        fromIndex > toIndex -> throw IllegalArgumentException("fromIndex ($fromIndex) is greater than toIndex ($toIndex).")
        fromIndex < 0 -> throw IndexOutOfBoundsException("fromIndex ($fromIndex) is less than zero.")
        toIndex > size -> throw IndexOutOfBoundsException("toIndex ($toIndex) is greater than size ($size).")
    }
}


fun guessMyNumber(n: Int) {
    println("output : ${(MIN..MAX).toList().binarySearch(n)}!")
}

fun inputNumber(): Int {
    val scanner = Scanner(System.`in`)
    print("1 보다 크거나 같고, 100보다 작거나 같은 정수 [그외 범위시 종료] : ")
    return scanner.nextInt()
}

fun main(args: Array<String>) {
    val text = """
                |*****    Guess My Number       *****
                |
                |
                |*****    간단한 인공지능 게임       *****
                |*****    플레이어가 1 ~ 100 사이의  *****
                |*****    정수를 입력합니다.         *****
                |*****    그러면 컴퓨터는            *****
                |*****    플레이어가 입력한          *****
                |*****    숫자를 맞춥니다.          *****
                |
                |
                """.trimMargin()
    print(text)
    var value = inputNumber()
    while (value in MIN..MAX) {
        guessMyNumber(value)
        value = inputNumber()
    }
}
