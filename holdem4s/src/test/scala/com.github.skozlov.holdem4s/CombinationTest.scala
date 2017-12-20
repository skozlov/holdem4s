package com.github.skozlov.holdem4s

import com.github.skozlov.holdem4s.Rank._
import org.junit.Assert._
import org.junit.Test

import scala.annotation.tailrec
import scala.util.Random

class CombinationTest{
	@Test
	def testCompare(): Unit ={
		val handsDescending = List(
			RoyalFlush, StraightFlush(`5`),
			FourOfAKind(A, K), FourOfAKind(A, `2`), FourOfAKind(A), FourOfAKind(K, A), FourOfAKind(`2`),
			FullHouse(A, K), FullHouse(A, `2`), FullHouse(K, A), FullHouse(`2`, `3`),
			Flush(A, K, Q, J, `9`), Flush(A, K, Q, J, `2`), Flush(A, K, Q, T, `9`), Flush(A, K, Q, `3`, `2`),
			Flush(A, K, J, T, `9`), Flush(A, K, `4`, `3`, `2`), Flush(A, Q, J, T, `9`), Flush(A, `6`, `4`, `3`, `2`),
			Flush(K, Q, J, T, `8`), Flush(`7`, `5`, `4`, `3`, `2`),
			Straight(A), Straight(`5`),
			ThreeOfAKind(A, K, Q), ThreeOfAKind(A, K, `2`), ThreeOfAKind(A, K), ThreeOfAKind(A, Q, J), ThreeOfAKind(A, `3`, `2`), ThreeOfAKind(A, `3`),
			ThreeOfAKind(A, `2`), ThreeOfAKind(A), ThreeOfAKind(K, Q, J), ThreeOfAKind(`2`),
			TwoPair(A, K, Q), TwoPair(A, K, `2`), TwoPair(A, K), TwoPair(A, Q, J), TwoPair(A, `2`, `3`),
			TwoPair(A, `2`), TwoPair(K, Q, A), TwoPair(`3`, `2`),
			Pair(A, K, Q, J), Pair(A, K, Q, `2`), Pair(A, K, Q), Pair(A, K, J, T), Pair(A, K, `2`), Pair(A, K),
			Pair(A, Q, J, T), Pair(A, `2`), Pair(A), Pair(K, Q, J, T), Pair(`2`),
			HighCard(A, K, Q, J, `9`), HighCard(A, K, Q, J, `2`), HighCard(A, K, Q, T, `9`),
			HighCard(A, K, Q, `3`, `2`), HighCard(A, K, J, T, `9`), HighCard(A, K, `4`, `3`, `2`),
			HighCard(A, Q, J, T, `9`), HighCard(A, `6`, `4`, `3`, `2`), HighCard(K, Q, J, T, `8`),
			HighCard(`7`, `5`, `4`, `3`, `2`)
		)

		@tailrec
		def test(handsDescending: List[Combination]): Unit ={
			val first = handsDescending.head
			assertEquals(0, first compare first)
			if (handsDescending.size >= 2){
				val second = handsDescending(1)
				val firstCompareSecond = first compare second
				assertTrue(
					s"Expected positive integer when comparing $first with $second, but found $firstCompareSecond",
					firstCompareSecond > 0
				)
				val secondCompareFirst = second compare first
				assertTrue(
					s"Expected negative integer when comparing $second with $first, but found $secondCompareFirst",
					secondCompareFirst < 0
				)
				test(handsDescending.tail)
			}
		}

		test(handsDescending)
	}

	@Test
	def testApply(): Unit ={
		def compare(expected: Combination, cards: Card*): Unit = {
			assertEquals(expected, Combination(Hand(Random.shuffle(cards).toSet)))
		}

		compare(StraightFlush(K), K♥, Q♥, J♥, T♥, `9`♥, A♣, A♠)
		compare(StraightFlush(K), K♥, Q♥, J♥, T♥, `9`♥)
		compare(StraightFlush(`5`), `5`♥, `4`♥, `3`♥, `2`♥, A♥, K♥, `6`♠)
		compare(StraightFlush(`5`), `5`♥, `4`♥, `3`♥, `2`♥, A♥)
		compare(FourOfAKind(K, A), K♠, K♥, K♦, K♣, A♠, Q♥, Q♦)
		compare(FourOfAKind(K, A), K♠, K♥, K♦, K♣, A♠)
		compare(FourOfAKind(K), K♠, K♥, K♦, K♣)
		compare(FullHouse(A, K), A♠, A♥, A♦, K♠, K♥, K♦, Q♠)
		compare(FullHouse(A, K), A♠, A♥, A♦, K♠, K♥)
		compare(FullHouse(K, A), K♠, K♥, K♦, A♠, A♥, Q♦, Q♠)
		compare(FullHouse(K, A), K♠, K♥, K♦, A♠, A♥)
		compare(Flush(K, Q, J, T, `8`), K♥, Q♥, J♥, T♥, `8`♥, A♠, A♦)
		compare(Flush(K, Q, J, T, `8`), K♥, Q♥, J♥, T♥, `8`♥)
		compare(Straight(Q), Q♥, J♥, T♥, `9`♥, `8`♦, A♦, A♠)
		compare(Straight(Q), Q♥, J♥, T♥, `9`♥, `8`♦)
		compare(Straight(`5`), `5`♥, `4`♥, `3`♥, `2`♥, A♠, A♦, A♣)
		compare(Straight(`5`), `5`♥, `4`♥, `3`♥, `2`♥, A♠)
		compare(ThreeOfAKind(K, A, Q), K♠, K♥, K♦, A♠, Q♠, J♠, `9`♣)
		compare(ThreeOfAKind(K, A, Q), K♠, K♥, K♦, A♠, Q♠)
		compare(ThreeOfAKind(K, A), K♠, K♥, K♦, A♠)
		compare(ThreeOfAKind(K), K♠, K♥, K♦)
		compare(TwoPair(K, Q, A), K♠, K♥, Q♠, Q♥, A♠, J♠, J♣)
		compare(TwoPair(K, Q, A), K♠, K♥, Q♠, Q♥, A♠)
		compare(TwoPair(K, Q), K♠, K♥, Q♠, Q♥)
		compare(Pair(`2`, A, K, Q), `2`♠, `2`♥, A♠, K♠, Q♠, J♥, `9`♥)
		compare(Pair(`2`, A, K, Q), `2`♠, `2`♥, A♠, K♠, Q♠)
		compare(Pair(`2`, A, K), `2`♠, `2`♥, A♠, K♠)
		compare(Pair(`2`, A), `2`♠, `2`♥, A♠)
		compare(Pair(`2`), `2`♠, `2`♥)
		compare(HighCard(A, K, Q, J, `9`), A♠, K♠, Q♠, J♠, `9`♥, `8`♥, `7`♥)
		compare(HighCard(A, K, Q, J, `9`), A♠, K♠, Q♠, J♠, `9`♥)
		compare(HighCard(A, K, Q, J), A♠, K♠, Q♠, J♠)
		compare(HighCard(A, K, Q), A♠, K♠, Q♠)
		compare(HighCard(A, K), A♠, K♠)
		compare(HighCard(A), A♠)
	}
}