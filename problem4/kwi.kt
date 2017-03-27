package study

import java.util.*

/**
 * Created by kwi on 2017-03-27.
 */
object Study4 {
    @JvmStatic fun main(args: Array<String>) {
        solve().print()
    }

    class solve {

        fun print() {
            p4_1(4, 10)
            p4_2()
            p4_3(7, 31)
            p4_4(3)
        }

        fun p4_1(n: Int, m: Int) {
            println(IntRange(1, m)
                    .toList()
                    .sortedWith(Comparator { o1, o2 -> 1 - (Math.random() * 3).toInt() })
                    .subList(0, n)
                    .sorted())
        }

        fun p4_2() {
            val list = arrayListOf(arrayListOf('a', 'b', 'c'), arrayListOf('d', 'e'), arrayListOf('f', 'g', 'h'),
                    arrayListOf('c', 'd'), arrayListOf('i', 'j', 'k', 'l'), arrayListOf('m', 'n'), arrayListOf('o'))

            val sortedList = list.sortedWith(Comparator { o1, o2 ->
                if (o1.size != o2.size) o1.size.compareTo(o2.size) else o1.toString().compareTo(o2.toString())
            })

            println(sortedList)
        }

        fun p4_3(start: Int, end: Int) {
            val primeList = IntRange(start, end).filter {
                num ->
                IntProgression.fromClosedRange(2, Math.sqrt(num.toDouble()).toInt(), 1)
                        .filter { div -> num % div == 0 }
                        .none()
            }

            println(primeList)
        }

        fun p4_4(n: Int) {
            /*
            Gn : 그레이코드의 n번째 자리
            Bn : 이진코드의 n번째 자리
            G4 = B4 xor 0
            G3 = B3 xor B4
            G2 = B2 xor B3
            G1 = B1 xor B2
            */
            val grayList = IntRange(0, Math.pow(2.0, n.toDouble()).toInt() - 1).map {
                Integer.toBinaryString(it xor (it shr 1))
            }

            println(grayList)
        }
    }
}