package com.github.skozlov.holdem4s

import com.github.skozlov.holdem4s.Rank._
import org.junit.Assert._
import org.junit.Test

class RankTest{
	@Test
	def testOrder(): Unit ={
		assertTrue(K < A)
		assertEquals(List(`2`, `3`, `4`, `5`, `6`, `7`, `8`, `9`, T, J, Q, K, A), Rank.values.toList.sorted)
	}

	@Test
	def testTo(): Unit ={
		assertEquals(List(A), A to A)
		assertEquals(List(Q, K, A), Q to A)
		try {
			A to K
			fail("No exception was thrown")
		} catch {
			case e: IllegalArgumentException => assertEquals("requirement failed: K is less than A", e.getMessage)
		}
	}

	@Test
	def testDownTo(): Unit ={
		assertEquals(List(A), A downTo A)
		assertEquals(List(A, K, Q), A downTo Q)
		try{
			K downTo A
			fail("No exception was thrown")
		} catch {
			case e: IllegalArgumentException => assertEquals("requirement failed: A is greater that K", e.getMessage)
		}
	}

	@Test
	def testMinusOther(): Unit ={
		assertEquals(0, A - A)
		assertEquals(12, A - `2`)
		assertEquals(-12, `2` - A)
	}
}