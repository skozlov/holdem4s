package com.github.skozlov.holdem4s

import com.github.skozlov.holdem4s.Combination._
import com.github.skozlov.holdem4s.Rank._

import scala.annotation.tailrec

/**
  * Like [[com.github.skozlov.holdem4s.Hand]], but contains only data relevant for comparison of hands.
  * At most 5 cards can participate in comparison.
  * All kinds of hands:
  * <ul>
  *     <li>[[com.github.skozlov.holdem4s.StraightFlush]]</li>
  *     <li>[[com.github.skozlov.holdem4s.FourOfAKind]]</li>
  *     <li>[[com.github.skozlov.holdem4s.FullHouse]]</li>
  *     <li>[[com.github.skozlov.holdem4s.Flush]]</li>
  *     <li>[[com.github.skozlov.holdem4s.Straight]]</li>
  *     <li>[[com.github.skozlov.holdem4s.ThreeOfAKind]]</li>
  *     <li>[[com.github.skozlov.holdem4s.TwoPair]]</li>
  *     <li>[[com.github.skozlov.holdem4s.Pair]]</li>
  *     <li>[[com.github.skozlov.holdem4s.HighCard]]</li>
  * </ul>
 *
  * @example
  * <ul>
  *     <li>Queen-high straight flush (QJT98)</li>
  *     <li>Four kings and ace (KKKKA)</li>
  * </ul>
  */
sealed trait Combination extends Ordered[Combination]{
	/**
	  * For internal usage only
	  */
	protected[holdem4s] def kindRank: Int

	/**
	  * For internal usage only
	  */
	protected[holdem4s] def ranksOrdered: List[Rank]

	/**
	  * Compares this hand with `that`.
	  * Comparison rules:
	  * <ul>
	  *     <li>Straight flush is greater than any other kind of hand.</li>
	  *     <li>Straight flush with greater rank is greater. See [[com.github.skozlov.holdem4s.StraightFlush.rank]].</li>
	  *     <li>Four of a kind is less than any straight flush and greater than any full house.</li>
	  *     <li>Four of a kind with greater rank is greater. See [[com.github.skozlov.holdem4s.FourOfAKind.rank]].</li>
	  *     <li>Among 2 fours of a kind of the same rank, the one with greater extra rank is greater. See [[com.github.skozlov.holdem4s.FourOfAKind.kickerRank]].</li>
	  *     <li>Full house is less than any four of a kind and greater than any flush.</li>
	  *     <li>Full house with greater rank of included three of a kind is greater. See [[com.github.skozlov.holdem4s.FullHouse.threeRank]].</li>
	  *     <li>Among 2 full houses containing three of a kind of the same rank, the one containing pair with greater rank is greater. See [[com.github.skozlov.holdem4s.FullHouse.pairRank]].</li>
	  *     <li>Flush is less than any full house and greater than any straight.</li>
	  *     <li>Flushes are compared by their ranks in descending order: first, the highest ranks are compared, and the three with higher one is higher, otherwise 2nd ranks are compared, etc.</li>
	  *     <li>Straight is less than any flush and greater than any three of a kind.</li>
	  *     <li>Straight with greater rank is greater. See [[com.github.skozlov.holdem4s.Straight.rank]].</li>
	  *     <li>Three of a kind is less than any straight and greater than any two pair.</li>
	  *     <li>Threes of a kind are compared by their ranks in descending order: first, the ranks of threes ([[com.github.skozlov.holdem4s.ThreeOfAKind.rank]]) are compared, and the three with higher one is higher, otherwise highest extra ranks are compared, etc.</li>
	  *     <li>Two pair is less than any three of a kind and greater than any pair.</li>
	  *     <li>Two pair with greater rank of high pair is greater. See [[com.github.skozlov.holdem4s.TwoPair.highPairRank]].</li>
	  *     <li>Among 2 two pairs with the same rank of high pair, the one with greater rank of low pair is greater. See [[com.github.skozlov.holdem4s.TwoPair.lowPairRank]].</li>
	  *     <li>Among 2 two pairs containing the same pairs, the one with greater extra rank is greater. See [[com.github.skozlov.holdem4s.TwoPair.kickerRank]].</li>
	  *     <li>Pair is less than any two pair and greater than high card.</li>
	  *     <li>Pair with greater rank is greater. See [[com.github.skozlov.holdem4s.Pair.rank]].</li>
	  *     <li>Pairs with the same rank are compared by their extra ranks in descending order: first, the highest ones are compared, and the pair with higher one is higher, otherwise 2nd ranks are compared, etc. See [[com.github.skozlov.holdem4s.Pair.kickerRanks]].</li>
	  *     <li>High card is less than any other kind of hand.</li>
	  *     <li>High cards are compared by their ranks in descending order: first, the highest ones are compared, and the hand with higher one is higher, otherwise 2nd ranks are compared, etc.</li>
	  *     <li>If none of the rules above states that hand `x` is less or greater than hand `y`, the hand containing more cards is greater.</li>
	  *     <li>If none of the rules above states that hand `x` is less or greater than hand `y`, `x` and `y` are equivalent.</li>
	  * </ul>
 *
	  * @param that hand to compare `this` with
	  * @return a positive number if `this` is greater than `that`, a negative number if `this` is less than `that`, `0` otherwise
	  */
	override def compare(that: Combination): Int = {
		val kindResult = this.kindRank compare that.kindRank
		if (kindResult != 0) kindResult
		else {
			@tailrec
			def compare(ranks1: List[Rank], ranks2: List[Rank]): Int = {
				if(ranks1.isEmpty || ranks2.isEmpty) ranks1.size compare ranks2.size
				else {
					val headResult = ranks1.head compare ranks2.head
					if(headResult == 0) compare(ranks1.tail, ranks2.tail)
					else headResult
				}
			}

			compare(this.ranksOrdered, that.ranksOrdered)
		}
	}
}

object Combination{
	/**
	  * Returns [[com.github.skozlov.holdem4s.Combination]] corresponding to the given [[com.github.skozlov.holdem4s.Hand]].
	  * Several alternatives can exist for the given hand, the greatest one of them will be returned.
 *
	  * @param hand hand
	  * @return [[com.github.skozlov.holdem4s.Combination]] corresponding to the given [[com.github.skozlov.holdem4s.Hand]]
	  */
	def apply(hand: Hand): Combination = {
		case class RankGroup(size: Int, rank: Rank) extends Ordered[RankGroup] {
			override def compare(that: RankGroup): Int = {
				val sizeResult = this.size compare that.size
				if (sizeResult == 0) this.rank compare that.rank
				else sizeResult
			}
		}

		val byRank: List[RankGroup] = {
			(hand.cards groupBy {_.rank} map {case (rank, cards) => RankGroup(cards.size, rank)}).toList.sorted.reverse
		}

		if (byRank.head.size == 4) {
			/*
			It cannot be a straight flush,
			because straight flush contains 5 distinct ranks,
			while a hand containing 4 cards with the same rank can contain at most 4 distinct ranks.
			 */
			FourOfAKind(byRank.head.rank, (byRank.tail map {_.rank}).sorted.lastOption)
		}
		else flushLike(hand.cards) match {
			case Some(ranks) => straightLike(ranks) match {
				case Some(orderedRanks) => StraightFlush(orderedRanks.head)
				case None =>
					/*
					It cannot be a full house,
					because hand containing at least 5 suited cards
					cannot contain both three of a kind and pair at the same time.
					 */
					val flushRanks = ranks.toList.sorted.reverse
					//noinspection ZeroIndexToHead
					Flush(flushRanks(0), flushRanks(1), flushRanks(2), flushRanks(3), flushRanks(4))
			}
			case None =>
				byRank match {
					case RankGroup(3, threeRank) :: RankGroup(x, pairRank) :: _ if x >= 2 => FullHouse(threeRank, pairRank)
					case _ =>
						straightLike(hand.cards map {_.rank}) match {
							case Some(orderedRanks) => Straight(orderedRanks.head)
							case None => byRank match {
								case RankGroup(3, rank) :: rest => ThreeOfAKind(rank, rest take 2 map {_.rank})
								case RankGroup(2, highPairRank) :: RankGroup(2, lowPairRank) :: rest =>
									TwoPair(highPairRank, lowPairRank, (rest map {_.rank}).sorted.lastOption)
								case RankGroup(2, rank) :: rest => Pair(rank, rest take 3 map {_.rank})
								case _ => HighCard(byRank take 5 map {_.rank})
							}
						}
				}
		}
	}

	/**
	  * Throws [[IllegalArgumentException]] unless the given ranks are distinct and arranged in descending order.
	  * @param ranks ranks to check
	  */
	def requireDecreasing(ranks: List[Rank]): Unit ={
		if(ranks.size >= 2){
			for(i <- 0 to (ranks.size - 2)){
				val left = ranks(i)
				val right = ranks(i+1)
				require(left != right, "Duplicated rank: " + left)
				require(left > right, s"Wrong order of ranks: $right must be before $left")
			}
		}
	}

	/**
	  * Tests whether the given set contains at least 5 suited cards
	  * @param cards cards to search for suited ones
	  * @return if the given set contains at least 5 suited cards, returns [[scala.Some]] containing all those cards, otherwise returns [[scala.None]]
	  */
	def flushLike(cards: Set[Card]): Option[Set[Rank]] = {
		if(cards.size < 5) None
		else {
			val flushLike: Iterable[Rank] = for {
				singleSuitCards <- (cards groupBy {_.suit}).values
				if singleSuitCards.size >= 5
				card <- singleSuitCards
			} yield card.rank
			if (flushLike.isEmpty) None else Some(flushLike.toSet)
		}
	}

	/**
	  * Tests whether the given set contains at least 5 ranks in a sequence or `5432A`.
	  * @param ranks ranks to search for straight-like sequence
	  * @return if the given set contains at least 5 ranks in a sequence, returns [[scala.Some]] containing all of those ranks in descending order; otherwise if it contains `5`, `4`, `3`, `2` and `A`, returns `Some(List(5, 4, 3, 2, A))`; otherwise returns [[scala.None]]
	  */
	def straightLike(ranks: Set[Rank]): Option[List[Rank]] = {
		if(ranks.size < 5) None
		else {
			val sortedRanks = ranks.toList.sorted.reverse

			@tailrec
			def continuousStraightLike(ranks: List[Rank]): Option[List[Rank]] = {
				if(ranks.size < 5) None
				else if(ranks.head - ranks(4) == 4) Some(ranks.head downTo ranks(4))
				else continuousStraightLike(ranks.tail)
			}

			continuousStraightLike(sortedRanks) orElse {
				if(sortedRanks.head == A && sortedRanks(sortedRanks.size - 4) == `5`) Some((`5` downTo `2`) :+ A)
				else None
			}
		}
	}
}

/**
  * Five cards in a sequence, all in the same suit.
  * `5432A`, all in the same suit, is also a straight flush.
  * Ace-high straight flush (`AKQJT`) is called a royal flush.
  * @param rank the highest rank among cards in a sequence, `5` for `5432A`.
  */
case class StraightFlush(rank: Rank) extends Combination {
	require(rank >= 5, s"$rank-high straight flush does not exist")

	/**
	  * If this straight flush is a royal flush.
	  */
	lazy val royal: Boolean = rank == A

	override protected[holdem4s] final val kindRank = 8

	override protected[holdem4s] final lazy val ranksOrdered = List(rank)

	/**
	  * Name of this straight flush.
	  * @example
	  * println(StraightFlush(`5`)) // 5-high straight flush
	  * println(StraightFlush(A)) // Royal flush
	  */
	override lazy val toString: String = if(royal) "Royal flush" else s"$rank-high straight flush"
}

object StraightFlush{
	/**
	  * Extracts rank of straight flush
	  * @param combination possible straight flush
	  * @return if the given combination is a straight flush, returns [[scala.Some]] containing its rank,
	  *         [[scala.None]] otherwise
	  */
	def unapply(combination: Combination): Option[Rank] = combination match {
		case straightFlush: StraightFlush => Some(straightFlush.rank)
		case _ => None
	}

	/**
	  * Extracts rank of straight flush
	  * @param hand possible straight flush
	  * @return if the given hand is a straight flush, returns [[scala.Some]] containing its rank,
	  *         [[scala.None]] otherwise
	  */
	def unapply(hand: Hand): Option[Rank] = if(hand == null) None else unapply(hand.toCombination)
}

/**
  * Royal flush, or ace-high straight flush.
  */
object RoyalFlush extends StraightFlush(A)

/**
  * Four of a kind: a hand containing 4 cards of the same rank.
  * @param rank common rank of 4 cards
  * @param kickerRank rank of another card, if any
  */
case class FourOfAKind(rank: Rank, kickerRank: Option[Rank] = None) extends Combination {
	require(!(kickerRank contains rank), "Duplicated rank: " + kickerRank)

	override protected[holdem4s] final val kindRank = 7

	override protected[holdem4s] final lazy val ranksOrdered: List[Rank] = List(rank) ++ kickerRank.toList

	/**
	  * Name of this four of a kind.
	  * @example
	  * println(Four(A)) // Quad As
	  * println(Four(A, K)) // Quad As with K kicker
	  */
	override lazy val toString: String = {
		val mainPart = s"Quad ${rank}s"
		val extraPart = kickerRank map {rank => s" with $rank kicker"} getOrElse ""
		mainPart + extraPart
	}
}

object FourOfAKind{
	/**
	  * Creates four of a kind
	  * @param rank common rank of 4 cards
	  * @param extraRank rank of another card
	  * @return four of a kind
	  */
	def apply(rank: Rank, extraRank: Rank): FourOfAKind = FourOfAKind(rank, Some(extraRank))

	/**
	  * Extracts parameters of four of a kind
	  * @param combination possible four of a kind
	  * @return if the given combination is a four of a kind, returns [[scala.Some]] containing its parameters,
	  *         [[scala.None]] otherwise
	  */
	def unapply(combination: Combination): Option[(Rank, Option[Rank])] = combination match {
		case four: FourOfAKind => Some(four.rank, four.kickerRank)
		case _ => None
	}

	/**
	  * Extracts parameters of four of a kind
	  * @param hand possible four of a kind
	  * @return if the given hand is a four of a kind, returns [[scala.Some]] containing its parameters,
	  *         [[scala.None]] otherwise
	  */
	def unapply(hand: Hand): Option[(Rank, Option[Rank])] = if(hand == null) None else unapply(hand.toCombination)
}

/**
  * Full house: a hand containing 3 cards of the same rank and 2 cards of another rank.
  * @param threeRank common rank of 3 cards
  * @param pairRank common rank of 2 cards
  */
case class FullHouse(threeRank: Rank, pairRank: Rank) extends Combination{
	require(threeRank != pairRank, "Duplicated rank: " + threeRank)

	override protected[holdem4s] final val kindRank = 6

	override protected[holdem4s] final lazy val ranksOrdered = List(threeRank, pairRank)

	/**
	  * Name of this full house.
	  * @example println(FullHouse(`5`, J)) // 5s full of Js
	  */
	override lazy val toString: String = s"${threeRank}s full of ${pairRank}s"
}

object FullHouse{
	/**
	  * Extracts parameters of full house
	  * @param combination possible full house
	  * @return if the given combination is a full house, returns [[scala.Some]] containing its parameters,
	  *         [[scala.None]] otherwise
	  */
	def unapply(combination: Combination): Option[(Rank, Rank)] = combination match {
		case fh: FullHouse => Some(fh.threeRank, fh.pairRank)
		case _ => None
	}

	/**
	  * Extracts parameters of full house
	  * @param hand possible full house
	  * @return if the given hand is a full house, returns [[scala.Some]] containing its parameters,
	  *         [[scala.None]] otherwise
	  */
	def unapply(hand: Hand): Option[(Rank, Rank)] = if(hand == null) None else unapply(hand.toCombination)
}

/**
  * Flush: a hand consisting of 5 cards of the same suit, but not in a sequence.
  * @param rank1 highest rank
  * @param rank2 2nd rank
  * @param rank3 3rd rank
  * @param rank4 4th rank
  * @param rank5 lowest rank
  */
case class Flush(rank1: Rank, rank2: Rank, rank3: Rank, rank4: Rank, rank5: Rank) extends Combination{
	override protected[holdem4s] final val kindRank = 5

	override final val ranksOrdered: List[Rank] = List(rank1, rank2, rank3, rank4, rank5)

	requireDecreasing(ranksOrdered)
	require(straightLike(ranksOrdered.toSet).isEmpty, "This is a straight flush")

	/**
	  * Name of this flush.
	  * @example println(Flush(List(A, K, Q, J, `9`))) // Flush (A K Q J 9)
	  */
	override lazy val toString: String = s"Flush (${ranksOrdered.mkString(" ")})"
}

object Flush{
	/**
	  * Extracts parameters of flush
	  * @param combination possible flush
	  * @return if the given combination is a flush, returns [[scala.Some]] containing its parameters,
	  *         [[scala.None]] otherwise
	  */
	def unapply(combination: Combination): Option[(Rank, Rank, Rank, Rank, Rank)] = combination match {
		case f: Flush => Some(f.rank1, f.rank2, f.rank3, f.rank4, f.rank5)
		case _ => None
	}

	/**
	  * Extracts parameters of flush
	  * @param hand possible flush
	  * @return if the given hand is a flush, returns [[scala.Some]] containing its parameters,
	  *         [[scala.None]] otherwise
	  */
	def unapply(hand: Hand): Option[(Rank, Rank, Rank, Rank, Rank)] = {
		if(hand == null) None else unapply(hand.toCombination)
	}
}

/**
  * Straight: a hand consisting of 5 cards in a sequence (or `5`, `4`, `3`, `2` and `A`), but not in the same suit.
  * @param rank the highest rank among cards in a sequence, `5` for `5432A`.
  */
case class Straight(rank: Rank) extends Combination{
	require(rank >= `5`, s"$rank-high straight does not exist")

	override protected[holdem4s] final val kindRank = 4

	override protected[holdem4s] final lazy val ranksOrdered = List(rank)

	/**
	  * Name of this straight.
	  * @example
	  * println(Straight(A)) // A-high straight
	  */
	override lazy val toString: String = s"$rank-high straight"
}

object Straight{
	/**
	  * Extracts parameters of straight
	  * @param combination possible straight
	  * @return if the given combination is a straight, returns [[scala.Some]] containing its parameters,
	  *         [[scala.None]] otherwise
	  */
	def unapply(combination: Combination): Option[Rank] = combination match {
		case s: Straight => Some(s.rank)
		case _ => None
	}

	/**
	  * Extracts parameters of straight
	  * @param hand possible straight
	  * @return if the given hand is a straight, returns [[scala.Some]] containing its parameters,
	  *         [[scala.None]] otherwise
	  */
	def unapply(hand: Hand): Option[Rank] = if(hand == null) None else unapply(hand.toCombination)
}

/**
  * Three of a kind: a hand consisting of 3 cards of the same rank and at most 2 other unmatched cards
  * @param rank common rank of 3 cards
  * @param kickerRanks ranks of kickers (in descending order)
  */
case class ThreeOfAKind(rank: Rank, kickerRanks: List[Rank]) extends Combination{
	require(kickerRanks.size <= 2, s"A three of a kind cannot contain more than 2 kickers, but found ${kickerRanks.size}")
	require(!(kickerRanks contains rank), "Duplicated rank: " + rank)
	requireDecreasing(kickerRanks)

	override protected[holdem4s] final val kindRank = 3

	override protected[holdem4s] final lazy val ranksOrdered: List[Rank] = rank :: kickerRanks

	/**
	  * Name of this three of a kind.
	  * @example
	  * println(Three(A)) // Three As
	  * println(Three(A, K)) // Three As with K kicker
	  * println(Three(A, K, Q)) // // Three As with K and Q kickers
	  */
	override lazy val toString: String = {
		val mainPart = s"Three ${rank}s"
		val extraPart = kickerRanks match {
			case Nil => ""
			case List(kickerRank) => s" with $kickerRank kicker"
			case List(kicker1, kicker2) => s" with $kicker1 and $kicker2 kickers"
		}
		mainPart + extraPart
	}
}

object ThreeOfAKind{
	/**
	  * Creates three of a kind.
	  * @param rank common rank of 3 cards
	  * @param kickerRanks ranks of kickers (in descending order)
	  * @return created three of a kind
	  */
	def apply(rank: Rank, kickerRanks: Rank*): ThreeOfAKind = ThreeOfAKind(rank, kickerRanks.toList)

	/**
	  * Extracts parameters of three of a kind
	  * @param combination possible three of a kind
	  * @return if the given combination is a three of a kind, returns [[scala.Some]] containing its parameters,
	  *         [[scala.None]] otherwise
	  */
	def unapply(combination: Combination): Option[(Rank, List[Rank])] = combination match {
		case t: ThreeOfAKind => Some(t.rank, t.kickerRanks)
		case _ => None
	}

	/**
	  * Extracts parameters of three of a kind
	  * @param hand possible three of a kind
	  * @return if the given hand is a three of a kind, returns [[scala.Some]] containing its parameters,
	  *         [[scala.None]] otherwise
	  */
	def unapply(hand: Hand): Option[(Rank, List[Rank])] = if(hand == null) None else unapply(hand.toCombination)
}

/**
  * Two pair: a hand consisting of 2 cards of the same rank and another 2 cards with the same rank
  * (but of a different rank from the first two cards).
  * The remaining card (if any) is an unmatched card.
  * @param highPairRank rank of higher pair
  * @param lowPairRank rank of lower pair
  * @param kickerRank rank of kicker, if any
  */
case class TwoPair(highPairRank: Rank, lowPairRank: Rank, kickerRank: Option[Rank] = None) extends Combination{
	private val pairRanks = List(highPairRank, lowPairRank)
	require((pairRanks intersect kickerRank.toList).isEmpty, "Duplicated rank: " + kickerRank.get)
	requireDecreasing(pairRanks)

	override protected[holdem4s] final val kindRank = 2

	override protected[holdem4s] final lazy val ranksOrdered: List[Rank] = pairRanks ++ kickerRank.toList

	/**
	  * Name of this two pair.
	  * @example println(TwoPair(A, K)) // As and Ks
	  * @example println(TwoPair(A, K, Q)) // As and Ks with Q kicker
	  */
	override lazy val toString: String = {
		val mainPart = s"${highPairRank}s and ${lowPairRank}s"
		val extraPart = kickerRank map {rank => s" with $rank kicker"} getOrElse ""
		mainPart + extraPart
	}
}

object TwoPair{
	/**
	  * Creates two pair.
	  * @param highPairRank rank of higher pair
	  * @param lowPairRank rank of lower pair
	  * @param kickerRank rank of kicker
	  * @return created two pair
	  */
	def apply(highPairRank: Rank, lowPairRank: Rank, kickerRank: Rank): TwoPair = {
		TwoPair(highPairRank, lowPairRank, Some(kickerRank))
	}

	/**
	  * Extracts parameters of two pair
	  * @param combination possible two pair
	  * @return if the given combination is a two pair, returns [[scala.Some]] containing its parameters,
	  *         [[scala.None]] otherwise
	  */
	def unapply(combination: Combination): Option[(Rank, Rank, Option[Rank])] = combination match {
		case t: TwoPair => Some(t.highPairRank, t.lowPairRank, t.kickerRank)
		case _ => None
	}

	/**
	  * Extracts parameters of two pair
	  * @param hand possible two pair
	  * @return if the given hand is a two pair, returns [[scala.Some]] containing its parameters,
	  *         [[scala.None]] otherwise
	  */
	def unapply(hand: Hand): Option[(Rank, Rank, Option[Rank])] = if(hand == null) None else unapply(hand.toCombination)
}

/**
  * One pair: a hand consisting of 2 cards of the same rank and up to 3 cards of other distinct ranks.
  * @param rank common rank of 2 cards
  * @param kickerRanks ranks of other cards, in descending order, up to 3
  */
case class Pair(rank: Rank, kickerRanks: List[Rank]) extends Combination{
	require(kickerRanks.size <= 3, s"A pair cannot contain more than 3 kickers, but found ${kickerRanks.size}")
	require(!(kickerRanks contains rank), "Duplicated rank: " + rank)
	requireDecreasing(kickerRanks)

	override protected[holdem4s] final val kindRank = 1

	override protected[holdem4s] final lazy val ranksOrdered: List[Rank] = rank :: kickerRanks

	/**
	  * Name of this pair.
	  * @example println(Pair(A)) // A pair of As
	  * @example println(Pair(A, K)) // A pair of As with K kicker
	  * @example println(Pair(A, K, Q)) // A pair of As with K and Q kickers
	  */
	override lazy val toString: String = {
		val mainPart = s"A pair of ${rank}s"
		val extraPart = kickerRanks match {
			case Nil => ""
			case List(kickerRank) => s" with $kickerRank kicker"
			case _ => s" with ${kickerRanks mkString " and "} kickers"
		}
		mainPart + extraPart
	}
}

object Pair{
	/**
	  * Creates pair.
	  * @param rank common rank of 2 cards
	  * @param kickerRanks ranks of other cards, in descending order, up to 3
	  * @return created pair
	  */
	def apply(rank: Rank, kickerRanks: Rank*): Pair = Pair(rank, kickerRanks.toList)

	/**
	  * Extracts parameters of pair
	  * @param combination possible pair
	  * @return if the given combination is a pair, returns [[scala.Some]] containing its parameters,
	  *         [[scala.None]] otherwise
	  */
	def unapply(combination: Combination): Option[(Rank, List[Rank])] = combination match {
		case p: Pair => Some(p.rank, p.kickerRanks)
		case _ => None
	}

	/**
	  * Extracts parameters of pair
	  * @param hand possible pair
	  * @return if the given hand is a pair, returns [[scala.Some]] containing its parameters,
	  *         [[scala.None]] otherwise
	  */
	def unapply(hand: Hand): Option[(Rank, List[Rank])] = if(hand == null) None else unapply(hand.toCombination)
}

/**
  * High card: a hand which contains none of those subsets:
  * <ul>
  *     <li>5 cards of the same suit</li>
  *     <li>5 cards in rank sequence</li>
  *     <li>2 or more cards of the same rank</li>
  * </ul>
  * @param ranks ranks of cards, in decreasing order, 1 to 5 (inclusive)
  */
case class HighCard(ranks: List[Rank]) extends Combination{
	require(
		(1 to 5) contains ranks.size,
		"A combination must contain at least 1 and at most 5 cards, but found" + ranks.size)
	requireDecreasing(ranks)
	require(straightLike(ranks.toSet).isEmpty, ranks.mkString(" ") + " is a straight")

	override protected[holdem4s] final val kindRank = 0

	override protected[holdem4s] final lazy val ranksOrdered: List[Rank] = ranks

	/**
	  * Name of this high card
	  * @example HighCard(A) // A-high
	  * @example HighCard(A, K) // A-high with K kicker
	  * @example HighCard(A, K, Q) // A-high with K and Q kickers
	  */
	override lazy val toString: String = {
		val mainPart = s"${ranks.head}-high"
		val kickerPart = ranks.tail match {
			case Nil => ""
			case List(kicker) => s" with $kicker kicker"
			case kickers => s" with ${kickers mkString " and "} kickers"
		}
		mainPart + kickerPart
	}
}

object HighCard{
	/**
	  * Creates high card
	  * @param ranks ranks of cards, in decreasing order, 1 to 5 (inclusive)
	  * @return created high card
	  */
	def apply(ranks: Rank*): HighCard = HighCard(ranks.toList)

	/**
	  * Extracts parameters of high card
	  * @param combination possible high card
	  * @return if the given combination is a high card, returns [[scala.Some]] containing its parameters,
	  *         [[scala.None]] otherwise
	  */
	def unapply(combination: Combination): Option[List[Rank]] = combination match {
		case h: HighCard => Some(h.ranks)
		case _ => None
	}

	/**
	  * Extracts parameters of high card
	  * @param hand possible high card
	  * @return if the given hand is a high card, returns [[scala.Some]] containing its parameters,
	  *         [[scala.None]] otherwise
	  */
	def unapply(hand: Hand): Option[List[Rank]] = if(hand == null) None else unapply(hand.toCombination)
}