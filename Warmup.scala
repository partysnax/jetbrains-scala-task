package testcode

// Warmup Task

class Warmup

object Warmup {

    /* The function f(n) is 2^n defined over the natural numbers.
     * Proof by induction:
     * Base case: 2^0 = 1 = f(0) by definition
     * Let f(n) = 2^n for some n >= 0.
     * f(n+1) = f(n) + f(n)
     *        = 2*f(n)
     *        = 2*(2^n)
     *        = 2^(n+1)
     * So by induction f(n) = 2^n
     */

    // Original recursive definiton
    def f(n: Integer) : Integer = {
        require (n>=0)
        if (n==0) 1
        else f(n-1) + f(n-1)
    }

    /* Time complexity:
     * Pseudopolynomial, i.e.
     * O(n) in the value of n
     * O(2^n) in the size of n
     * Since the function has a closed form, this can be done without recursion, as below
     */

    def f2(n: Integer) : Integer = {
        require (n>=0)
        1<<n
    }

    // f2 has constant time performance
}