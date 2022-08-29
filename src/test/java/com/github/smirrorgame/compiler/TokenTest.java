package com.github.smirrorgame.compiler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class TokenTest {

	@Test
	public void test_Token() {
		
		Token t = new Token(TokenType.AND, "and", null, 0, 0);
		assertEquals(TokenType.AND, t.type);
		assertEquals("and", t.lexeme);
		assertNull(t.literal);
		assertEquals(0, t.line);
		assertEquals(0, t.col);
	}
	
	@Test
	public void test_toString() {
		Token t = new Token(TokenType.AND, "and", null, 0, 0);
		assertEquals("AND: \"and\" null at 0:0", t.toString());
	}
	
	@Test
	public void test_equals() {
		/*
		 * TESTS:
		 * 
		 * - Must be of same class						X

		 * - literal must be same:						X
		 * 		- null vs !null -> not same				X
		 * 		- !null vs null -> not same				X
		 * 		- null vs null  -> same					X
		 * 		- any vs any	-> same					X

		 * - TokenType must be same						X
		 * 
		 * - Lexeme must be equal						X
		 * 
		 * - line must be same							O
		 * 
		 * - col must be same							O
		 * 
		 */
		
		
		Token actual;		
		Token expected;		
		Token unexpected;	
		
//		checking literal -> null vs !null and null vs !null
		unexpected = 	new Token(TokenType.STRING, "\"the string\"", "the string", 1, 0);
		actual = 		new Token(TokenType.STRING, "\"the string\"", null, 1, 0);
		assertNotEquals(unexpected, actual);
		assertNotEquals(actual, unexpected);
		
//		checking literal -> 'the string' vs 'string' and 'string vs 'the string'
		unexpected = 	new Token(TokenType.STRING, "\"the string\"", "the string", 1, 0);
		actual = 		new Token(TokenType.STRING, "\"the string\"", "string", 1, 0);
		assertNotEquals(unexpected, actual);
		assertNotEquals(actual, unexpected);
		
//		checking literal -> 'null' vs 'null'
		unexpected = 	new Token(TokenType.PLUS, "+", null, 1, 0);
		actual = 		new Token(TokenType.PLUS, "+", null, 1, 0);
		assertEquals(unexpected, actual);
		assertEquals(actual, unexpected);
		
//		checking TokenType -> LET vs STRING and STRING vs LET
		unexpected = 	new Token(TokenType.LET, "let", null, 1, 0);
		actual = 		new Token(TokenType.STRING, "\the string\"", "the string", 1, 0);
		assertNotEquals(unexpected, actual);
		assertNotEquals(actual, unexpected);

//		checking lexeme -> 'let' vs 'let' and 'lt' vs 'let'
		expected = 		new Token(TokenType.LET, "let", null, 1,0);
		unexpected = 	new Token(TokenType.LET, "lt", null, 1,0);
		actual = 		new Token(TokenType.LET, "let", null, 1,0);
		assertEquals(expected, actual);
		assertEquals(actual, expected);
		assertNotEquals(actual, unexpected);
		assertNotEquals(unexpected, actual);
		
//		checking class -> Token vs TokenType.LET
		assertNotEquals(actual, TokenType.LET);
		
		
//		checking line -> 1 vs 1 and 1 vs 2
		expected = 		new Token(TokenType.LET, "let", null, 1, 0);
		unexpected = 	new Token(TokenType.LET, "let", null, 2, 0);
		actual = 		new Token(TokenType.LET, "let", null, 1, 0);
		assertEquals(expected, actual);
		assertEquals(actual, expected);
		assertNotEquals(actual, unexpected);
		assertNotEquals(unexpected, actual);
		
//		checking col -> 0 vs 0 and 0 vs 2
		expected = 		new Token(TokenType.LET, "let", null, 1, 0);
		unexpected = 	new Token(TokenType.LET, "let", null, 1, 2);
		actual = 		new Token(TokenType.LET, "let", null, 1, 0);
		assertEquals(expected, actual);
		assertEquals(actual, expected);
		assertNotEquals(actual, unexpected);
		assertNotEquals(unexpected, actual);
	}
	
	
}
