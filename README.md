# Holdem4s

Scala library for recognition and comparison of [Texas holdem](https://en.wikipedia.org/wiki/Texas_hold_%27em) hands.

## Code sample

```scala
import com.github.skozlov.holdem4s._
import Suit._
import Rank._

Hand(2♠, 2♥, A♠, K♠, Q♠, J♥, 9♥) match {
	case Pair(`2`, highestKicker :: _) =>
		println(s"A pair of Twos with ${highestKicker.name} kicker and so on") //A pair of Twos with Ace kicker and so on
}
```

## Quick start guide

It is assumed in the code samples below that the following imports are included:

```scala
import com.github.skozlov.holdem4s._
import Suit._
import Rank._
```

### Suits

There is a constant for each of 4 suits, named after the corresponding well-known unicode character:

```scala
val suits: List[Suit] = List(♠, ♥, ♦, ♣)
println(suits mkString " ") //♠ ♥ ♦ ♣
```

They have aliases coinciding with their English names:

```scala
println(List(♠, ♥, ♦, ♣) == List(Spades, Hearts, Diamonds, Clubs)) //true
```

### Ranks

There are intuitive constants for all 13 ranks:

```scala
val ranks: List[Rank] = List(A, K, Q, J, T, 9, 8, 7, 6, 5, 4, 3, 2)
println(ranks mkString " ") //A K Q J T 9 8 7 6 5 4 3 2
```

Ranks can be compared:

```scala
println(A > K) //true
```

... and sorted:

```scala
val unorderedRanks = scala.util.Random.shuffle(ranks)
println(unorderedRanks mkString " ") //"Q 8 2 J 7 4 K 9 3 A 5 T 6" or in another order
println(unorderedRanks.sorted mkString " ") //2 3 4 5 6 7 8 9 T J Q K A
```

They also have aliases:

```scala
require(List[Rank](A, K, Q, J, T, 9, 8, 7, 6, 5, 4, 3, 2)
	== List(Ace, King, Queen, Jack, Ten, Nine, Eight, Seven, Six, Five, Four, Three, Two))
```

### Cards

Creating a card is pretty easy:

```scala
val aceSpades: Card = A♠
```

`Card` is a case class with arguments `rank` and `suit`, so it has properties of the same names:

```scala
println(aceSpades.rank) //A
println(aceSpades.suit) //♠
```

But its own `toString()` is overloaded:

```scala
println(aceSpades) // A♠
```

Of course, you can use aliases of ranks and suits:

```scala
println((Ace spades) == (A♠)) //true
```

Class `Card` extends `Ordered[Card]`. Cards are ordered by ranks:

```scala
println((K♦) < (A♠)) // true
```

### Hands and Combinations

A hand is a set of cards available to the player.
It can include up to 7 cards: 2 in the pocket and 3 to 5 on the board.

We support hands consisting of any number of cards from 1 to 7 (both inclusive).
An unusual number of cards (1, 3 or 4) in the hand can be used, for instance, in assumptions.

So the shortest hand contains just 1 card:

```scala
val shortestHand: Hand = Hand(A♠)
```

... and the longest one consists of 7 cards:

```scala
val longestHand = Hand(K♥, Q♥, J♥, T♥, 9♥, A♣, A♠)
```

Hands can be compared with each other:

```scala
println(Hand(A♠, A♥, A♦, A♣, K♠, K♥, K♦) < Hand(A♥, K♥, Q♥, J♥, T♥)) //true
```

`Hand(A♠, A♥, A♦, A♣, K♠, K♥, K♦)` is longer and contains cards of higher ranks,
but `Hand(A♥, K♥, Q♥, J♥, T♥)` beats it,
because the rank of a hand is determined by the highest 5-card _combination_ it contains:

```scala
val combination: Combination = Hand(A ♠, A ♥, A ♦, A ♣, K ♠, K ♥, K ♦).toCombination
println(combination) //Quad Aces with King kicker
println(Hand(A♥, K♥, Q♥, J♥, T♥).toCombination) //Royal flush
```

Note that `toString()` of `Combination` returns the human-readable name of the combination.

How hands of different lengths are compared?
As we saw above, `Hand(A♥, K♥, Q♥, J♥, T♥)` beats `Hand(A♠, A♥, A♦, A♣, K♠, K♥, K♦)`,
because royal flush always beats four of a kind.
But what if both hands are, for example, threes of a kind?
Then the general rule is applied: _all alse being equal_, the longer combination beats the shorter one:

```scala
println(Hand(A♠, A♥, A♦, K♣, Q♠, J♥) > Hand(A♠, A♥, A♦, K♣)) //true
```

In this example, we have 2 "Three aces" with King as the highest kicker, 
but `Hand(A♠, A♥, A♦, K♣, Q♠, J♥)` has 2nd kicker, while `Hand(A♠, A♥, A♦, K♣)` has only one.

Another example:

```scala
println(Hand(A♠, A♥, A♦, Q♠, J♥) < Hand(A♠, A♥, A♦, K♣)) //true
```

Again, we have 2 "Three aces", but the 1st kickers are different, so the condition "all alse being equal" does not hold.

Remember that a combination cannot contain more than 5 cards, so the following hands are considered equivalent:

```scala
println(Hand(A♠, A♥, A♦, K♣, Q♠, J♥).toCombination == Hand(A♠, A♥, A♦, K♣, Q♠).toCombination) //true
println(!(
	Hand(A♠, A♥, A♦, K♣, Q♠, J♥) > Hand(A♠, A♥, A♦, K♣, Q♠)
	|| Hand(A♠, A♥, A♦, K♣, Q♠, J♥) < Hand(A♠, A♥, A♦, K♣, Q♠)
	)) //true
```

Finally, pattern matching:

```scala
Hand(2♠, 2♥, A♠, K♠, Q♠, J♥, 9♥) match {
	case Pair(`2`, highestKicker :: _) =>
		println(s"A pair of Twos with ${highestKicker.name} kicker and so on") //A pair of Twos with Ace kicker and so on
}
```