package testcode

import org.scalatest._
import flatspec._
import matchers._
import scala.collection.mutable.ArrayBuffer

// Code testing done here

class Main extends AnyFlatSpec with should.Matchers {

    // Warmup task
    
    "f" should "be 2^n" in {
        (Warmup.f(0)) should be (1)
        (Warmup.f(1)) should be (2)
        (Warmup.f(20)) should be (1<<20)
    }

    it should "reject invalid inputs" in {
        assertThrows[java.lang.IllegalArgumentException] {
            Warmup.f(-2)
        }
    }

    "f2" should "be 2^n" in {
        (Warmup.f2(0)) should be (1)
        (Warmup.f2(1)) should be (2)
        (Warmup.f2(50)) should be (1<<50)
    }

    it should "reject invalid inputs" in {
        assertThrows[java.lang.IllegalArgumentException] {
            Warmup.f2(-1)
        }
    }

    // Tree parsing task

    "Converting tree to string" should "work on a single empty node" in {
        val n1 = new Node(new ArrayBuffer[Tree]())
        (n1.toString) should be ("()")
    }

    it should "work on a single ID" in {
        val t1 = new ID("abcde")
        (t1.toString) should be ("abcde")
    }

    it should "work on trees with multiple layers" in {
        val t1 = new ID("1")
        val t2 = new ID("2")
        val t3 = new ID("3")
        val t4 = new ID("4")
        val t5 = new ID("5")
        val n1, n2, n3, n4, n5 = new Node(new ArrayBuffer[Tree]())
        n2.addChildren(n1)
        n3.addChildren(t3, t4)
        n4.addChildren(t2, n2, n3)
        n5.addChildren(t1, n4, t5)
        (n5.toString) should be ("(1 (2 (()) (3 4)) 5)")
    }

    "String parsing" should "work on a single ID" in {
        val t = Tree.parse("20j2f2n")
        (t.toString) should be ("20j2f2n")

        val t2 = Tree.parse("       24t   ")
        (t2.toString) should be ("24t")
    }
    
    it should "work on a single node" in {
        val t = Tree.parse("()")
        (t.toString) should be ("()")

        val t2 = Tree.parse("   (    )  ")
        (t2.toString) should be ("()")
    }

    it should "work on trees with multiple layers" in {
        val t = Tree.parse("  (  1  ()  2   3  4 (() 5) 6 )")
        (t.toString) should be ("(1 () 2 3 4 (() 5) 6)")
    }

    it should "fail on strings without Node nor ID" in {
        assertThrows[IllegalArgumentException] {
            val t = Tree.parse("")
        }

        assertThrows[IllegalArgumentException] {
            val t = Tree.parse("      ")
        }
    }

    it should "fail when string represents multiple objects" in {
        assertThrows[IllegalArgumentException] {
            val t = Tree.parse(" 9asdfj   ajo rjt ")
        }

        assertThrows[IllegalArgumentException] {
            val t = Tree.parse("   (  ()  d   g )()")
        }

        assertThrows[IllegalArgumentException] {
            val t = Tree.parse("(())a")
        }
    }

    it should "fail when the string has mismatched parentheses" in {
        assertThrows[IllegalArgumentException] {
            val t = Tree.parse(")")
        }

        assertThrows[IllegalArgumentException] {
            val t = Tree.parse("(  (a  ()   a )")
        }

        assertThrows[IllegalArgumentException] {
            val t = Tree.parse("(  aw  aber  (  wa ()  (e) ) (r) ) )")
        }
    }

    it should "fail when the string contains illegal characters" in {
        assertThrows[IllegalArgumentException] {
            val t = Tree.parse("_")
        }

        assertThrows[IllegalArgumentException] {
            val t = Tree.parse("awjr-erj")
        }

        assertThrows[IllegalArgumentException] {
            val t = Tree.parse("(  ()  4 $  ())")
        }
    }

    it should "fail when children of a node are not separated by whitespace" in {
        assertThrows[IllegalArgumentException] {
            val t = Tree.parse("(a  b())")
        }

        assertThrows[IllegalArgumentException] {
            val t = Tree.parse("(f  t  ()() )")
        }

        assertThrows[IllegalArgumentException] {
            val t = Tree.parse("((1)2 3)")
        }
    }

    "Replacing subtrees" should "work on proper subtrees" in {
        val t = Tree.parse("(  a (b  (c))  d  (c))")
        val sub = Tree.parse("(c)")
        val rep = Tree.parse("k")
        val u = Tree.replace(t, sub, rep)
        (u.toString) should be ("(a (b k) d k)")
    }

    it should "work on the tree itself" in {
        val t1 = Tree.parse("( a (b) (c))")
        val t2 = Tree.parse("(a (b)  (c))")
        val rep = Tree.parse("()")
        val u1 = Tree.replace(t1, t1, rep)
        val u2 = Tree.replace(t1, t2, rep)
        (u1.toString) should be ("()")
        (u2.toString) should be ("()")
    }

    it should "not modify the original tree" in {
        val t1 = Tree.parse("k")
        val t2 = Tree.parse("((k) (1 (k)))")
        val sub = Tree.parse("k")
        val rep = Tree.parse("(ab cd)")
        val u1 = Tree.replace(t1, sub, rep)
        val u2 = Tree.replace(t2, sub, rep)
        (t1.toString) should be ("k")
        (t2.toString) should be ("((k) (1 (k)))")
    }

    it should "not further replace a replacement" in {
        val t = Tree.parse("(0 (0 (0 1)))")
        val sub = Tree.parse("(0 1)")
        val rep = Tree.parse("1")
        val u = Tree.replace(t, sub, rep)
        (u.toString) should be ("(0 (0 1))")
    }
}