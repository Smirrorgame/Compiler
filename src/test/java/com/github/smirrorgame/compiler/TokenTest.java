package com.github.smirrorgame.compiler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class TokenTest {

	@Test
	public void test_Token() {
		
		Token t = new Token(TokenType.AND, "and", null, 0);
		assertEquals(TokenType.AND, t.type);
		assertEquals("and", t.lexeme);
		assertNull(t.literal);
		assertEquals(0, t.line);
		// TODO: add far more test cases
		System.out.println("Needs to be implemented further!");
	}
	
	@Test
	public void test_toString() {
		Token t = new Token(TokenType.AND, "and", null, 0);
		assertEquals("AND: and null", t.toString());
	}
	
	@Test
	public void test_equals() {
		Token expected = new Token(TokenType.STRING, "\"the string\"", "the string", 1);
		Token actual = new Token(TokenType.STRING, "\"the string\"", "the string", 1);
		Token unexpected = new Token(TokenType.STRING, "\"the string\"", "the string", 1);
		assertEquals(expected, actual);
		actual = new Token(TokenType.STRING, "\the string\"", null, 1);
		assertNotEquals(unexpected, actual);
		assertNotEquals(actual, unexpected);
		
		unexpected = new Token(TokenType.LET, "let", null, 1);
		actual = new Token(TokenType.STRING, "\the string\"", "the string", 1);
		assertNotEquals(unexpected, actual);
		assertNotEquals(actual, unexpected);

		expected = new Token(TokenType.LET, "let", null, 1);
		actual = new Token(TokenType.LET, "let", null, 1);
		assertEquals(expected, actual);
		
		assertNotEquals(actual, TokenType.LET);
	}
	
	
}
