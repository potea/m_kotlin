package exam_7

/**
 * Created by bross on 2017. 4. 18..
 */


class Node<T>(value: T) {
    var value: T = value
    var next: Node<T>? = null
    var previous: Node<T>? = null
}

class LinkedList<T> {

    private var head: Node<T>? = null

    var isEmpty: Boolean = head == null

    fun first(): Node<T>? = head

    fun last(): Node<T>? {
        var node = head
        if (node != null) {
            while (node?.next != null) {
                node = node?.next
            }
            return node
        } else {
            return null
        }
    }

    fun count(): Int {
        var node = head
        if (node != null) {
            var counter = 1
            while (node?.next != null) {
                node = node?.next
                counter += 1
            }
            return counter
        } else {
            return 0
        }
    }

    fun nodeAtIndex(index: Int): Node<T>? {
        if (index >= 0) {
            var node = head
            var i = index
            while (node != null) {
                if (i == 0) return node
                i -= 1
                node = node.next
            }
        }
        return null
    }

    fun append(value: T) {
        var newNode = Node(value)

        var lastNode = this.last()
        if (lastNode != null) {
            newNode.previous = lastNode
            lastNode.next = newNode
        } else {
            head = newNode
        }
    }

    fun removeAll() {
        head = null
    }

    fun removeNode(node: Node<T>): T {
        val prev = node.previous
        val next = node.next

        if (prev != null) {
            prev.next = next
        } else {
            head = next
        }
        next?.previous = prev

        node.previous = null
        node.next = null

        return node.value
    }

    fun removeLast(): T? {
        val last = this.last()
        if (last != null) {
            return removeNode(last)
        } else {
            return null
        }
    }

    fun removeAtIndex(index: Int): T? {
        val node = nodeAtIndex(index)
        if (node != null) {
            return removeNode(node)
        } else {
            return null
        }
    }

    override fun toString(): String {
        var s = "["
        var node = head
        while (node != null) {
            s += "${node.value}"
            node = node.next
            if (node != null) {
                s += ", "
            }
        }
        return s + "]"
    }
}


fun josephus(max: Int, cycle: Int, peek: Int):Int {

    fun josephus(max: Int, cycle: Int): MutableList<Int> {

        var result = mutableListOf<Int>()
        var list = LinkedList<Int>()
        (1..max).forEach(list::append)

        list.last()?.next = list.first()

        var node = list.first()
        var cnt = 1
        while (node != null) {
            if (cnt % cycle == 0) {
                result.add(node.value)
                val temp = node
                node = node.next
                list.removeNode(temp)

            } else {
                node = node.next
            }
            cnt++
        }

        return result
    }


    return josephus(max, cycle).filterIndexed { index, it ->
        if (it == peek) {
            println(index+1)
        }
        it == peek
    }.first()
}

fun main(args: Array<String>) {
    josephus(7, 1000, 2)
}
