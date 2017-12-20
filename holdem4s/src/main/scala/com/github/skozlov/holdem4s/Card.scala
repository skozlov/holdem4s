package com.github.skozlov.holdem4s

import com.github.skozlov.holdem4s.Rank.Rank
import com.github.skozlov.holdem4s.Suit.Suit

/**
  * Playing card
  * @param rank [[com.github.skozlov.holdem4s.Rank]] of the card
  * @param suit [[com.github.skozlov.holdem4s.Suit]] of the card
  */
case class Card(rank: Rank, suit: Suit) extends Ordered[Card] {
	/**
	  * Compares `this` with `that`.
	  * Actually compares ranks.
	  * @param that card to compare `this` with
	  * @return `0` if `this.rank == that.rank`, a positive number if `this.rank > that.rank`, a negative number if `this.rank < that.rank`
	  */
	override def compare(that: Card): Int = this.rank compare that.rank

	/**
	  * String representation of this card.
	  * Contains 2 characters: rank and suit.
	  * @example val s = (A spades).toString // "A♠"
	  */
	override lazy val toString: String = s"$rank$suit"
}