package com.github.skozlov.holdem4s

/**
  * Card suit: spades (♠), hearts (♥), diamonds (♦) or clubs (♣)
  */
object Suit extends Enumeration{
	type Suit = Value

	val ♠ = Value("♠")
	val ♥ = Value("♥")
	val ♦ = Value("♦")
	val ♣ = Value("♣")

	val Spades: Suit = ♠
	val Hearts: Suit = ♥
	val Diamonds: Suit = ♦
	val Clubs: Suit = ♣
}