package com.github.skozlov.holdem4s

import com.github.skozlov.holdem4s.Rank._
import com.github.skozlov.holdem4s.Suit._
import org.junit.Assert._
import org.junit.Test

class CardTest{
	@Test
	def testConstruction(): Unit ={
		assertEquals(Card(A, Spades), A ♠)
		assertEquals(Card(A, Spades), A spades)
	}

	@Test
	def testToString(): Unit ={
		assertEquals("A♠", (A spades).toString)
	}
}