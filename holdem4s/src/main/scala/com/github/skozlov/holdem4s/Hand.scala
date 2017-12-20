package com.github.skozlov.holdem4s

/**
  * Set of cards a player has.
  * Can include up to 2 pocket cards and up to 5 board cards.
  * Actually, a player always has exactly 2 pocket cards and 0 or 3 to 5 board cards,
  * but [[com.github.skozlov.holdem4s.Hand]] supports 1 to 7 cards (inclusive) to be able to be used when some cards are not known etc.
  * @param cards cards that player has, at least 1 and at most 7
  */
case class Hand(cards: Set[Card]) extends Ordered[Hand]{
	require((1 to 7) contains cards.size, "A hand must contain at least 1 and at most 7 cards")

	/**
	  * Compares this hand with `that`.
	  * See [[com.github.skozlov.holdem4s.Combination.compare]] for details of comparison rules.
 *
	  * @param that hand to compare `this` with
	  * @return a positive number if `this` is greater than `that`, a negative number if `this` is less than `that`, `0` otherwise
	  */
	override def compare(that: Hand): Int = Combination(this) compare Combination(that)

	/**
	  * [[com.github.skozlov.holdem4s.Combination]] corresponding to this hand
	  */
	lazy val toCombination: Combination = Combination(this)
}

object Hand{
	/**
	  * Creates hand consisting of the provided cards
	  * @param card1 card
	  * @param otherCards cards
	  * @return hand
	  */
	def apply(card1: Card, otherCards: Card*): Hand = Hand(otherCards.toSet + card1)
}