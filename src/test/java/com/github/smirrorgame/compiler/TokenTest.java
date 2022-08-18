package com.github.smirrorgame.compiler;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
	
	
}
