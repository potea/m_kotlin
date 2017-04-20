import java.util.concurrent.LinkedBlockingQueue

fun Josephus(n: Int, k:Int, t:Int): Int {
    val q = LinkedBlockingQueue<Int>(List(n, { it + 1 }))
    do { (0..k - 2).forEach { q.put(q.poll()) } } while (q.poll() != t)
    return n - q.size
}

fun main(args: Array<String>) {
    println(Josephus(7, 3, 2))
}
