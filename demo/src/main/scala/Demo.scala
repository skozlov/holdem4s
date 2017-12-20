object Demo extends App{
	import com.github.skozlov.holdem4s._
	import Suit._
	import Rank._

	val suits: List[Suit] = List(♠, ♥, ♦, ♣)
	println(suits mkString " ") //♠ ♥ ♦ ♣

	println(List(♠, ♥, ♦, ♣) == List(Spades, Hearts, Diamonds, Clubs)) //true

	val ranks: List[Rank] = List(A, K, Q, J, T, `9`, `8`, `7`, `6`, `5`, `4`, `3`, `2`)
	println(ranks mkString " ") //A K Q J T 9 8 7 6 5 4 3 2

	println(A > K) //true

	val unorderedRanks = scala.util.Random.shuffle(ranks)
	println(unorderedRanks mkString " ") //"Q 8 2 J 7 4 K 9 3 A 5 T 6" or in another order
	println(unorderedRanks.sorted mkString " ") //2 3 4 5 6 7 8 9 T J Q K A

	println(List(A, K, Q, J, T, `9`, `8`, `7`, `6`, `5`, `4`, `3`, `2`)
		== List(Ace, King, Queen, Jack, Ten, Nine, Eight, Seven, Six, Five, Four, Three, Two)) //true

	val aceSpades: Card = A♠

	println(aceSpades.rank) //A
	println(aceSpades.suit) //♠
	println(aceSpades) // A♠

	println((Ace spades) == (A♠)) //true

	println((K♦) < (A♠)) // true

	val shortestHand: Hand = Hand(A♠)
	val longestHand = Hand(K♥, Q♥, J♥, T♥, `9`♥, A♣, A♠)

	println(Hand(A♠, A♥, A♦, A♣, K♠, K♥, K♦) < Hand(A♥, K♥, Q♥, J♥, T♥)) //true
	val combination: Combination = Hand(A ♠, A ♥, A ♦, A ♣, K ♠, K ♥, K ♦).toCombination
	println(combination) //Quad As with K kicker
	println(Hand(A♥, K♥, Q♥, J♥, T♥).toCombination) //Royal flush

	println(Hand(A♠, A♥, A♦, K♣, Q♠, J♥) > Hand(A♠, A♥, A♦, K♣)) //true
	println(Hand(A♠, A♥, A♦, Q♠, J♥) < Hand(A♠, A♥, A♦, K♣)) //true

	println(Hand(A♠, A♥, A♦, K♣, Q♠, J♥).toCombination == Hand(A♠, A♥, A♦, K♣, Q♠).toCombination) //true
	println(!(
		Hand(A♠, A♥, A♦, K♣, Q♠, J♥) > Hand(A♠, A♥, A♦, K♣, Q♠)
		|| Hand(A♠, A♥, A♦, K♣, Q♠, J♥) < Hand(A♠, A♥, A♦, K♣, Q♠)
		)) //true

	Hand(`2`♠, `2`♥, A♠, K♠, Q♠, J♥, `9`♥) match {
		case Pair(`2`, highestKicker :: _) =>
			println(s"A pair of 2s with $highestKicker kicker and so on") //A pair of 2s with A kicker and so on
	}
}