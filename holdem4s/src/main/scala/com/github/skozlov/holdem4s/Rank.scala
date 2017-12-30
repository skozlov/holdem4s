package com.github.skozlov.holdem4s

/**
  * Card rank.
  * Ascending order of ranks: 2, 3, 4, 5, 6, 7, 8, 9, T, J, Q, K, A.
  * @param symbol `'2'`, `'3'`, etc.
  * @param priority rank with higher priority is higher. Priorities of any two adjacent ranks differ by `1`.
  */
sealed case class Rank private(symbol: Char, private val priority: Int) extends Ordered[Rank]{
	import Rank._

	PriorityToRank(priority) = this

	override def compare(that: Rank): Int = this.priority compare that.priority

	override val toString: String = "" + symbol

	/**
	  * Card which has this rank and suit of spades.
	  * @example
	  * import com.github.skozlov.holdem4s.Rank._
	  * val aceOfSpades: Card = A ♠
	  */
	lazy val ♠ = Card(this, Suit.♠)

	/**
	  * Card which has this rank and suit of hearts.
	  * @example
	  * import com.github.skozlov.holdem4s.Rank._
	  * val aceOfHearts: Card = A ♥
	  */
	lazy val ♥ = Card(this, Suit.♥)

	/**
	  * Card which has this rank and suit of diamonds.
	  * @example
	  * import com.github.skozlov.holdem4s.Rank._
	  * val aceOfDiamonds: Card = A ♦
	  */
	lazy val ♦ = Card(this, Suit.♦)

	/**
	  * Card which has this rank and suit of clubs.
	  * @example
	  * import com.github.skozlov.holdem4s.Rank._
	  * val aceOfClubs: Card = A ♣
	  */
	lazy val ♣ = Card(this, Suit.♣)

	/**
	  * Card which has this rank and suit of spades.
	  * @example
	  * import com.github.skozlov.holdem4s.Rank._
	  * val aceOfSpades: Card = A spades
	  */
	lazy val spades = Card(this, Suit.Spades)

	/**
	  * Card which has this rank and suit of hearts.
	  * @example
	  * import com.github.skozlov.holdem4s.Rank._
	  * val aceOfHearts: Card = A hearts
	  */
	lazy val hearts = Card(this, Suit.Hearts)

	/**
	  * Card which has this rank and suit of diamonds.
	  * @example
	  * import com.github.skozlov.holdem4s.Rank._
	  * val aceOfDiamonds: Card = A diamonds
	  */
	lazy val diamonds = Card(this, Suit.Diamonds)

	/**
	  * Card which has this rank and suit of clubs.
	  * @example
	  * import com.github.skozlov.holdem4s.Rank._
	  * val aceOfClubs: Card = A clubs
	  */
	lazy val clubs = Card(this, Suit.Clubs)

	/**
	  * Returns list of ranks from this rank (inclusive) to the specified rank (inclusive), in ascending order.
	  * @param upper max rank in list
	  * @return list of ranks
	  * @throws IllegalArgumentException if this rank is greater than `upper`
	  * @example
	  * import com.github.skozlov.holdem4s.Rank._
	  * val fromJackToAce: List[Rank] = J to A // J, Q, K, A
	  */
	def to(upper: Rank): List[Rank] = {
		require(upper >= this, s"$upper is less than $this")
		(this.priority to upper.priority).toList map PriorityToRank.apply
	}

	/**
	  * Returns list of ranks from this rank (inclusive) to the specified rank (inclusive), in descending order.
	  * @param lower min rank in list
	  * @return list of ranks
	  * @throws IllegalArgumentException if this rank is less than `lower`
	  * @example
	  * import com.github.skozlov.holdem4s.Rank._
	  * val fromAceToJack: List[Rank] = A downTo J // A, K, Q, J
	  */
	def downTo(lower: Rank): List[Rank] = {
		require(this >= lower, s"$lower is greater that $this")
		(this.priority to lower.priority by -1).toList map PriorityToRank.apply
	}

	/**
	  * Returns difference of this rank and `that`.
	  * If this rank is equal to `that`, returns `0`.
	  * If this rank is greater than `that`, returns `1` plus number of ranks between this one and `that`.
	  * If this rank is less than `that`, returns the same as if it were greater, but with minus sign.
	  * @param that another rank
	  * @return difference of this rank and `that`
	  * @example
	  * import com.github.skozlov.holdem4s.Rank._
	  * val a = A - K // 1
	  * val b = A - J // 3
	  * val c = J - A // -3
	  */
	def -(that: Rank): Int = this.priority - that.priority
}

object Rank {
	private val PriorityToRank: scala.collection.mutable.Map[Int, Rank] = scala.collection.mutable.HashMap()

	val `2` = Rank('2', 2)
	val Two: Rank = `2`

	val `3` = Rank('3', 3)
	val Three: Rank = `3`

	val `4` = Rank('4', 4)
	val Four: Rank = `4`

	val `5` = Rank('5', 5)
	val Five: Rank = `5`

	val `6` = Rank('6', 6)
	val Six: Rank = `6`

	val `7` = Rank('7', 7)
	val Seven: Rank = `7`

	val `8` = Rank('8', 8)
	val Eight: Rank = `8`

	val `9` = Rank('9', 9)
	val Nine: Rank = `9`

	val T = Rank('T', 10)
	val J = Rank('J', 11)
	val Q = Rank('Q', 12)
	val K = Rank('K', 13)
	val A = Rank('A', 14)

	val Ten: Rank = T
	val Jack: Rank = J
	val Queen: Rank = Q
	val King: Rank = K
	val Ace: Rank = A

	/**
	  * All ranks from 2 to A
	  */
	val RanksInAscendingOrder: List[Rank] = PriorityToRank.toList sortBy {_._1} map {_._2}

	/**
	  * All ranks from A to 2
	  */
	val RanksInDescendingOrder: List[Rank] = RanksInAscendingOrder.reverse

	/**
	  * Converts an integer from `2` to `9` (both inclusive) to the corresponding rank.
	  * @param number integer to convert to rank
	  * @return `Two` if `number == 2`, `Three` if `number == 3`, etc.
	  * @throws IllegalArgumentException if `number` is less than `2` or greater than `9`
	  */
	implicit def numberToRank(number: Int): Rank = {
		require((2 to 9) contains number, s"Invalid rank: $number")
		PriorityToRank(number)
	}
}