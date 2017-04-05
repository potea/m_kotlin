import kotlin.coroutines.experimental.buildSequence

fun String.lookAndSay(): String {
    val encoded = StringBuilder()
    "(([0-9])\\2*)".toRegex().findAll(this).forEach { 
        encoded.append(it.value.length).append(it.value[0]) 
    }
    return encoded.toString()
}

fun ant1(limit: Int): String {
    var sequence = "1"
    (1..limit - 1).forEach { 
        sequence = sequence.lookAndSay() 
    }
    return sequence
}

fun ant2(limit: Int): String {
    var sequence = "1"
    return buildSequence {
        while (true) {
            yield(sequence)
            sequence = sequence.lookAndSay()
        }
    }.take(limit).joinToString()
}

fun main(args : Array<String>) {
    println(ant1(10))
    println(ant2(10))
}
