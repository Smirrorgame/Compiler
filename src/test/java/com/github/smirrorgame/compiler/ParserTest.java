package com.github.smirrorgame.compiler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class ParserTest {
	
	Parser p;

	void setUp(List<Token> tokens) {
		p = new Parser(tokens);
	}

	@Test
	public void test_equality() {
		List<Token> tokens = new ArrayList<Token>();
		tokens.add(new Token(TokenType.Number, "10", 10.0, 1, 0));
		tokens.add(new Token(TokenType.EQUAL_EQUAL, "==", null, 1, 3));
		tokens.add(new Token(TokenType.STRING, "\"10\"", "10", 1, 6));
		tokens.add(new Token(TokenType.EOF, "", null, 1, 10));
		
		setUp(tokens);
		Expr res = p.parse();
		System.out.println(res);
		
		AstPrinter printer = new AstPrinter();
		System.out.println(printer.print(res));
		
		
		
	}

}
