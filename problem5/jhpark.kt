import kotlin.coroutines.experimental.buildSequence

fun ant1(limit: Int) {
    fun String.lookAndSay(): String {
        val encoded = StringBuilder()
        "(([0-9])\\2*)".toRegex().findAll(this).forEach { encoded.append(it.value[0]).append(it.value.length) }
        return encoded.toString()
    }

    var sequence = "1"
    (1..limit - 1).forEach { sequence = sequence.lookAndSay() }
    println(sequence)
}

fun ant2(limit: Int) {
    fun Sequence<String>.lookAndSay(): Sequence<String> {
        return buildSequence {
            var last = '1'
            var count = 0
            
            this@lookAndSay.forEach {
                it.forEach {
                    if (it != last) {
                        yield("$last$count")
                        
                        count = 1
                        last = it
                    } else {
                        count++
                    }
                }
            }
            
            yield("$last$count")
        }
    }

    var sequence:Sequence<String> = sequenceOf("1")
    return buildSequence {
               while (true) {
                   yield(sequence)
                   sequence = sequence.lookAndSay()
               }
           }.take(limit).last().forEach(::print)
}

fun main(args : Array<String>) {
    ant1(10)
    ant2(100)
}
