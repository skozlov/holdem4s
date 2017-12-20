package com.github.skozlov.holdem4s

/**
  * Card rank.
  * Ascending order of ranks: 2, 3, 4, 5, 6, 7, 8, 9, 10, J, Q, K, A.
  */
object Rank extends Enumeration {
	type Rank = Value

	val `2` = Value("2")
	val Two: Rank = `2`

	val `3` = Value("3")
	val Three: Rank = `3`

	val `4` = Value("4")
	val Four: Rank = `4`

	val `5` = Value("5")
	val Five: Rank = `5`

	val `6` = Value("6")
	val Six: Rank = `6`

	val `7` = Value("7")
	val Seven: Rank = `7`

	val `8` = Value("8")
	val Eight: Rank = `8`

	val `9` = Value("9")
	val Nine: Rank = `9`

	val T = Value("T")
	val J = Value("J")
	val Q = Value("Q")
	val K = Value("K")
	val A = Value("A")

	val Ten: Rank = T
	val Jack: Rank = J
	val Queen: Rank = Q
	val King: Rank = K
	val Ace: Rank = A

	/**
	  * Useful implicit methods of [[com.github.skozlov.holdem4s.Rank]]
	  * @param rank rank
	  */
	implicit class Implicits(rank: Rank){
		/**
		  * Card which has this rank and suit of spades.
		  * @example
		  * import com.github.skozlov.holdem4s.Rank._
		  * val aceOfSpades: Card = A ♠
		  */
		lazy val ♠ = Card(rank, Suit.♠)

		/**
		  * Card which has this rank and suit of hearts.
		  * @example
		  * import com.github.skozlov.holdem4s.Rank._
		  * val aceOfHearts: Card = A ♥
		  */
		lazy val ♥ = Card(rank, Suit.♥)

		/**
		  * Card which has this rank and suit of diamonds.
		  * @example
		  * import com.github.skozlov.holdem4s.Rank._
		  * val aceOfDiamonds: Card = A ♦
		  */
		lazy val ♦ = Card(rank, Suit.♦)

		/**
		  * Card which has this rank and suit of clubs.
		  * @example
		  * import com.github.skozlov.holdem4s.Rank._
		  * val aceOfClubs: Card = A ♣
		  */
		lazy val ♣ = Card(rank, Suit.♣)

		/**
		  * Card which has this rank and suit of spades.
		  * @example
		  * import com.github.skozlov.holdem4s.Rank._
		  * val aceOfSpades: Card = A spades
		  */
		lazy val spades = Card(rank, Suit.Spades)

		/**
		  * Card which has this rank and suit of hearts.
		  * @example
		  * import com.github.skozlov.holdem4s.Rank._
		  * val aceOfHearts: Card = A hearts
		  */
		lazy val hearts = Card(rank, Suit.Hearts)

		/**
		  * Card which has this rank and suit of diamonds.
		  * @example
		  * import com.github.skozlov.holdem4s.Rank._
		  * val aceOfDiamonds: Card = A diamonds
		  */
		lazy val diamonds = Card(rank, Suit.Diamonds)

		/**
		  * Card which has this rank and suit of clubs.
		  * @example
		  * import com.github.skozlov.holdem4s.Rank._
		  * val aceOfClubs: Card = A clubs
		  */
		lazy val clubs = Card(rank, Suit.Clubs)

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
			require(rank >= lower, s"$lower is greater that $rank")
			(rank.id to lower.id by -1).toList map Rank.apply
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
		def -(that: Rank): Int = rank.id - that.id
	}
}