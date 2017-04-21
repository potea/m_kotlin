
fun josephus(n: Int, k: Int, t: Int) : Int {
    var nList = IntRange(1,n).toMutableList()
    var pos = -1
    var last = 0
    while (last != t) {
        pos += k
        pos %= nList.size
        last = nList[pos]
        nList.removeAt(pos)
        pos--
    }
    return n - nList.size
}

fun main(args: Array<String>) {
    println(josephus(7,3,2))
    println(josephus(14,2,13))

}
