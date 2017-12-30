object Demo extends App{
	import com.github.skozlov.holdem4s._
	import Suit._
	import Rank._

	val suits: List[Suit] = List(♠, ♥, ♦, ♣)
	require((suits mkString " ") == "♠ ♥ ♦ ♣")

	require(List(♠, ♥, ♦, ♣) == List(Spades, Hearts, Diamonds, Clubs))

	val ranks: List[Rank] = List(A, K, Q, J, T, `9`, `8`, `7`, `6`, `5`, `4`, `3`, `2`)
	require((ranks mkString " ") == "A K Q J T 9 8 7 6 5 4 3 2")

	require(A > K)

	val unorderedRanks = scala.util.Random.shuffle(ranks)
	println(unorderedRanks mkString " ") //"Q 8 2 J 7 4 K 9 3 A 5 T 6" or in another order
	require((unorderedRanks.sorted mkString " ") == "2 3 4 5 6 7 8 9 T J Q K A")

	require(List(A, K, Q, J, T, `9`, `8`, `7`, `6`, `5`, `4`, `3`, `2`)
		== List(Ace, King, Queen, Jack, Ten, Nine, Eight, Seven, Six, Five, Four, Three, Two))

	val aceSpades: Card = A♠

	require(aceSpades.rank == A)
	require(aceSpades.suit == ♠)
	require(aceSpades.toString == "A♠")

	require((Ace spades) == (A♠))

	require((K♦) < (A♠))

	val shortestHand: Hand = Hand(A♠)
	val longestHand = Hand(K♥, Q♥, J♥, T♥, `9`♥, A♣, A♠)

	require(Hand(A♠, A♥, A♦, A♣, K♠, K♥, K♦) < Hand(A♥, K♥, Q♥, J♥, T♥))
	val combination: Combination = Hand(A ♠, A ♥, A ♦, A ♣, K ♠, K ♥, K ♦).toCombination
	require(combination.toString == "Quad As with K kicker")
	require(Hand(A♥, K♥, Q♥, J♥, T♥).toCombination.toString == "Royal flush")

	require(Hand(A♠, A♥, A♦, K♣, Q♠, J♥) > Hand(A♠, A♥, A♦, K♣))
	require(Hand(A♠, A♥, A♦, Q♠, J♥) < Hand(A♠, A♥, A♦, K♣))

	require(Hand(A♠, A♥, A♦, K♣, Q♠, J♥).toCombination == Hand(A♠, A♥, A♦, K♣, Q♠).toCombination)
	require(!(
		Hand(A♠, A♥, A♦, K♣, Q♠, J♥) > Hand(A♠, A♥, A♦, K♣, Q♠)
		|| Hand(A♠, A♥, A♦, K♣, Q♠, J♥) < Hand(A♠, A♥, A♦, K♣, Q♠)
		))

	Hand(`2`♠, `2`♥, A♠, K♠, Q♠, J♥, `9`♥) match {
		case Pair(`2`, highestKicker :: _) =>
			require(s"A pair of 2s with $highestKicker kicker and so on" == "A pair of 2s with A kicker and so on")
	}
}