package com.github.smirrorgame.compiler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class LexerTest {
	
	@Test
	public void test_tokenize() {
		// TODO: add far more test cases
		System.out.println("Needs to be implemented further!");
		Lexer lexer = new Lexer("(");
		
		List<Token> tokens = lexer.tokenize();
		
		assertEquals(2, tokens.size());
		// Token (
		Token t = tokens.get(0);
		assertEquals(TokenType.Left_PARENTHESIS, t.type);
		assertEquals("(", t.lexeme);
		assertNull(t.literal);
		assertEquals(1, t.line);
		
		
		// Token EOF		
		t = tokens.get(1);
		assertEquals(TokenType.EOF, t.type);
		assertEquals("", t.lexeme);
		assertNull(t.literal);
		assertEquals(1, t.line);
		
	}

}
