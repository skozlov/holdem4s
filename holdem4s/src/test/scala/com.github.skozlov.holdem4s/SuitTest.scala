package com.github.skozlov.holdem4s

import com.github.skozlov.holdem4s.Suit._
import org.junit.Assert._
import org.junit.Test

class SuitTest{
	@Test
	def testToString(): Unit ={
		assertEquals("♠", `♠`.toString)
		assertEquals("♠", Spades.toString)
	}

	@Test
	def testAlias(): Unit ={
		assertEquals(`♠`, Spades)
	}
}