## Files
- Warmup.scala is for the warmup task
- Tree.scala is for the tree parsing task
- Main.scala is a Scalatest suite testing code for both tasks

## Scalatest output
```
Run starting. Expected test count is: 19
Main:
f
- should be 2^n
- should reject invalid inputs
f2
- should be 2^n
- should reject invalid inputs
Converting tree to string
- should work on a single empty node
- should work on a single ID
- should work on trees with multiple layers
String parsing
- should work on a single ID
- should work on a single node
- should work on trees with multiple layers
- should fail on strings without Node nor ID
- should fail when string represents multiple objects
- should fail when the string has mismatched parentheses
- should fail when the string contains illegal characters
- should fail when children of a node are not separated by whitespace
Replacing subtrees
- should work on proper subtrees
- should work on the tree itself
- should not modify the original tree
- should not further replace a replacement
Run completed in 221 milliseconds.
Total number of tests run: 19
Suites: completed 1, aborted 0
Tests: succeeded 19, failed 0, canceled 0, ignored 0, pending 0
All tests passed.
```