/**
 * Created by potea on 2017-04-13.
 *
 * Problem 6
 */

fun guess(number: Int) {
    fun guess(list: List<Int>) {
        val check = list.size / 2
        print(list[check].toString() + if (list[check] == number) "!\n" else " ")

        if (list[check] < number) guess(list.drop(check))
        else if (list[check] > number) guess(list.dropLast(check))
    }

    guess((1..100).toList())
}

fun main(args: Array<String>) {
    guess(90)
    guess(71)
    guess(10)
}
