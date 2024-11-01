package testcode

// Parsing Task

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Queue
import scala.compiletime.ops.string
import scala.collection.mutable.StringBuilder

sealed class Tree {

}

class ID (val value: String) extends Tree {
    // Checks for equality with another tree
    override def equals(x: Any): Boolean = x match {
        case that: ID => this.value == that.value
        case _ => false
    }

    // Returns string representation
    override def toString(): String = this.value
}

class Node (val children: ArrayBuffer[Tree]) extends Tree {
    // Appends given trees to the node's sequence of children
    def addChildren(newchilds: Tree*) = this.children.addAll(newchilds)

    // Checks for equality with another tree
    override def equals(x: Any): Boolean = x match {
        case that: Node => {
            if (that.children.length == this.children.length) {
                // Check that all children match
                var i = 0
                while (i < this.children.length) {
                    if (this.children(i) != that.children(i)) return false
                    i += 1
                }
                return true
            }
            return false
        }
        case _ => false
    }

    // Helper function to join string representations with a whitespace
    def stringCons(s: String, t: Tree) : String = {
        if (s == "") return t.toString()
        else return s ++ " " ++ t.toString()
    }

    // Returns string representation
    override def toString(): String = {
        "(" ++ this.children.foldLeft("")(stringCons) ++ ")"
    }
}

object Tree {
    // Turns input string into a buffer
    def parse(s: String) : Tree = {
        val s_seq : Seq[Char] = s
        val buffer = new Queue[Char]()
        buffer.addAll(s_seq)
        parseSeq(buffer)
    }

    // Initial function to parse the buffer, which can either represent Node or ID
    def parseSeq(buf: Queue[Char]) : Tree = {
        while (!buf.isEmpty) {
            val c = buf.front
            if (c.isLetterOrDigit) {
                val out = parseID(buf)
                // Ensure that the remainder of the buffer is whitespace only
                if (!onlyContainsWhitespace(buf)) throw new IllegalArgumentException("String represents tree containing more than one element at the top layer.")
                return out
            }
            else if (c == '(') {
                buf.dequeue() // Drop leading '('
                val out = parseNode(buf)
                // Ensure that the remainder of the buffer is whitespace only
                if (!onlyContainsWhitespace(buf)) throw new IllegalArgumentException("String represents tree containing more than one element at the top layer.")
                return out
            }
            else if (c.isWhitespace) buf.dequeue()
            else if (c == ')') {
                // Misplaced ')'
                throw new IllegalArgumentException("String contains ')' before '('")
            }
            else {
                // Invalid token
                throw new IllegalArgumentException("String contains invalid token '" ++ c.toString() ++ "'")
            }
        }
        throw new IllegalArgumentException("String cannot be empty")
        return null
    }

    // Parse and return a single ID
    def parseID(buf: Queue[Char]) : ID = {
        val s = new StringBuilder
        while (!buf.isEmpty && buf.head.isLetterOrDigit) s += buf.dequeue()
        return new ID(s.toString())
    }

    // Parse and return a single Node
    def parseNode(buf: Queue[Char]) : Node = {
        val children = new ArrayBuffer[Tree]()
        while (!buf.isEmpty) {
            val c = buf.head
            if (c == '(') { // Child Node
                buf.dequeue() // Drop leading '('
                children.addOne(parseNode(buf))
                if (!buf.isEmpty && !buf.head.isWhitespace && buf.head != ')') {
                    throw new IllegalArgumentException("Whitespace expected between two children of a Node")
                }
            }
            else if (c == ')') { // End of node
                buf.dequeue() // Drop ')'
                return new Node(children)
            }
            else if (c.isLetterOrDigit) { // Child ID
                children.addOne(parseID(buf))
                if (!buf.isEmpty && !buf.head.isWhitespace && buf.head != ')') {
                    throw new IllegalArgumentException("Whitespace expected between two children of a Node")
                }
            }
            else if (c.isWhitespace) buf.dequeue()
            else {
                // Invalid token
                throw new IllegalArgumentException("String contains invalid token '" ++ c.toString() ++ "'")
            }
        }
        // Buffer is empty before function returns, implying closing ')' is missing
        throw new IllegalArgumentException("Closing ')' for Node in parsed string expected but missing")
        return null
    }

    // Checks if the buffer only contains whitespace; dequeues checked whitespaces
    def onlyContainsWhitespace(buf: Queue[Char]) : Boolean = {
        while (!buf.isEmpty) {
            if (!buf.head.isWhitespace) return false
            buf.dequeue()
        }
        return true
    }

    // Returns copy of tree with subtrees equal to searchTree replaced by replacement
    def replace(tree: Tree, searchTree: Tree, replacement: Tree) : Tree = {
        val cpy = Tree.parse(tree.toString) // Create copy of tree
        return replace_rec(cpy, searchTree, replacement)
    }

    // Recursively checks tree for equality with searchTree for replacement
    def replace_rec(tree: Tree, searchTree: Tree, replacement: Tree) : Tree = tree match {
        case t: ID => {
            if (t == searchTree) return replacement
            else return t
        }
        case t: Node => {
            if (t == searchTree) return replacement
            else {
                // Checks and replaces each child of the Node
                for (i <- 0 until t.children.length) {
                    t.children(i) = replace_rec(t.children(i), searchTree, replacement)
                }
                return t
            }
        }
        case _ => throw IllegalArgumentException("Object to replace is not a valid member of Tree")
    }
}