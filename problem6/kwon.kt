import sun.reflect.generics.tree.Tree
import java.util.*
import kotlin.coroutines.experimental.buildSequence


fun ai(input: Int) {
    var min = 0
    var max = 100
    var guess = max / 2
    while (true) {
        var result = input.compareTo(guess)
        if (input === guess) {
            println("!")
            return
        }
        print("$guess ")
        if (result == -1) {
            max = guess
        } else {
            min = guess
        }
        guess = min + (max - min) / 2
    }
}

fun ai_recurive(min : Int, max : Int, guess : Int, input: Int) {
    if(guess == input) {
        println("!")
        return
    }
    print("$guess ")
    if(input.compareTo(guess) == -1)
        ai_recurive(min, guess, min + (guess - min) / 2, input)
    else
        ai_recurive(guess, max, guess + (max - guess) / 2, input)
}

fun main(args: Array<String>) {
    val min = 0
    val max = 100
    ai_recurive(min, max, max, 90)
    ai_recurive(min, max, max, 71)

//    ai(90)
//    ai(71)
}
