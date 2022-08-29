package com.github.smirrorgame.compiler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.junit.jupiter.api.Test;

public class LexerTest {
	
	private Lexer lexer;
	
	public void setUpLexer(String src) {
		lexer = new Lexer(src);
	}
	
	@Test
	public void test_singleCharacterTokens() {
		String src = "(){},.\n-+;*/";
		setUpLexer(src);
		Token[] expected = {
				new Token(TokenType.Left_PARENTHESIS, "(", null, 1,0),
				new Token(TokenType.RIGHT_PARENTHESIS, ")", null, 1,1),
				new Token(TokenType.LEFT_BRACE, "{", null, 1,2),
				new Token(TokenType.RIGHT_BRACE, "}", null, 1,3),
				new Token(TokenType.COMMA, ",", null, 1,4),
				new Token(TokenType.DOT, ".", null, 1,5),
				new Token(TokenType.MINUS, "-", null, 2,0),
				new Token(TokenType.PLUS, "+", null, 2,1),
				new Token(TokenType.SEMICOLON, ";", null, 2,2),
				new Token(TokenType.ASTERISK, "*", null, 2,3),
				new Token(TokenType.FORWARD_SLASH, "/", null, 2,4),
				new Token(TokenType.EOF, "", null, 2,5),
		};
		List<Token> tokens = lexer.tokenize();
		
		for(int i=0;i<expected.length;i++) {
			Token tok = tokens.get(i);
			Token exp = expected[i];
			assertEquals(exp, tok);
		}
	}
	
	@Test
	public void test_multiCharacterTokens() {
		String src = "! != = == > >= < <=";
		setUpLexer(src);
		Token[] expected = {
				new Token(TokenType.NOT, "!", null, 1,0),
				new Token(TokenType.NOT_EQUAL, "!=", null, 1,2),
				new Token(TokenType.EQUAL, "=", null, 1,5),
				new Token(TokenType.EQUAL_EQUAL, "==", null, 1,7),
				new Token(TokenType.GREATER, ">", null, 1,10),
				new Token(TokenType.GREATER_EQUAL, ">=", null, 1,12),
				new Token(TokenType.LESS, "<", null, 1,15),
				new Token(TokenType.LESS_EQUAL, "<=", null, 1,17),
				new Token(TokenType.EOF, "", null, 1,19),
		};
		List<Token> tokens = lexer.tokenize();
		
		for(int i=0;i<expected.length;i++) {
			Token tok = tokens.get(i);
			Token exp = expected[i];
			assertEquals(exp, tok);
		}
	}
	
	@Test
	public void test_literals() {
		String src = "let x = \"Test\"\nlet y = 133.7";
		setUpLexer(src);
		Token[] expected = {
				new Token(TokenType.LET, "let", null, 1,0),
				new Token(TokenType.IDENTIFIER, "x", null, 1,4),
				new Token(TokenType.EQUAL, "=", null, 1,6),
				new Token(TokenType.STRING, "\"Test\"", "Test", 1,8),
				new Token(TokenType.LET, "let", null, 2,0),
				new Token(TokenType.IDENTIFIER, "y", null, 2,4),
				new Token(TokenType.EQUAL, "=", null, 2,6),
				new Token(TokenType.Number, "133.7", 133.7, 2,8),
				new Token(TokenType.EOF, "", null, 2,13),
		};
		List<Token> tokens = lexer.tokenize();		
		
		for(int i=0;i<expected.length;i++) {
			Token actual = tokens.get(i);
			Token exp = expected[i];
			assertEquals(exp, actual);
		}
	}
	
	@Test
	public void test_reservedKeywords() {
		String src = "and class else false funct for if null or print return super this true let while";
		setUpLexer(src);
		Token[] expected = {
				new Token(TokenType.AND, "and", null, 1,0),
				new Token(TokenType.CLASS, "class", null, 1,4),
				new Token(TokenType.ELSE, "else", null, 1,10),
				new Token(TokenType.FALSE, "false", null, 1,15),
				new Token(TokenType.FUNCT, "funct", null, 1,21),
				new Token(TokenType.FOR, "for", null, 1,27),
				new Token(TokenType.IF, "if", null, 1,31),
				new Token(TokenType.NULL, "null", null, 1,34),
				new Token(TokenType.OR, "or", null, 1, 39),
				new Token(TokenType.PRINT, "print", null, 1, 42),
				new Token(TokenType.RETURN, "return", null, 1, 48),
				new Token(TokenType.SUPER, "super", null, 1,55),
				new Token(TokenType.THIS, "this", null, 1,61),
				new Token(TokenType.TRUE, "true", null, 1,66),
				new Token(TokenType.LET, "let", null, 1,71),
				new Token(TokenType.WHILE, "while", null, 1,75),
				new Token(TokenType.EOF, "", null, 1, 80),
		};
		List<Token> tokens = lexer.tokenize();
		
		for(int i=0;i<expected.length;i++) {
			Token tok = tokens.get(i);
			Token exp = expected[i];
			assertEquals(exp, tok);
		}
	}
	
	@Test
	public void test_Comment_and_unexpected() {
		// getting error output and setting new error output stream
		ByteArrayOutputStream errors = new ByteArrayOutputStream();
		PrintStream original_err = System.err;
		System.setErr(new PrintStream(errors));
		
		String src = "//This is a comment\n"
				+ "$\n"
				+ "//a comment reaching the end";
		setUpLexer(src);
		
		List<Token> tokens = lexer.tokenize();
		assertEquals(1, tokens.size());
		Token eof_tok = tokens.get(0);
		Token exp = new Token(TokenType.EOF, "", null, 3,28);
		assertEquals(exp, eof_tok);
		
		String errorString = errors.toString();
		errorString = errorString.replace("\n", "").replace("\r", "");
		assertEquals("[Line 2, column 21] Error: Unexpected Character: $", errorString);
		
		// resetting original error output stream
		System.setErr(original_err);
		
	}

	@Test
	public void test_strings() {
		// getting error output and setting new error output stream
		ByteArrayOutputStream errors = new ByteArrayOutputStream();
		PrintStream original_err = System.err;
		System.setErr(new PrintStream(errors));
		
		String src = "\"This is a\nmultiline string\"\"This is an unterminated string";
		setUpLexer(src);
		
		// checking EOF token
		List<Token> tokens = lexer.tokenize();
		Token eof_tok = tokens.get(0);
		//TODO: Token colon is 49 but should be 48 because "multiline string\"\"This is an unterminated string" has length 48
		Token exp = new Token(TokenType.EOF, "", null, 2, 48);
		assertEquals(1, tokens.size());
		assertEquals(exp, eof_tok);
		
		// checking errors
		String errorString = errors.toString();
		errorString = errorString.replace("\n", "").replace("\r", "");
		String exp_errors = "[Line 1, column 10] Error: missing a double quote to terminate string."
				+ "[Line 2, column 59] Error: Unterminated string.";
		assertEquals(exp_errors, errorString);
		
		// resetting original error output stream
		System.setErr(original_err);
	}
	
	@Test
	public void test_numbers() {
		String src = "1337.42\n1337.";
		setUpLexer(src);
		
		List<Token> tokens = lexer.tokenize();
				
		assertEquals(4, tokens.size());

		Token dec_tok = tokens.get(0);
		Token int_tok = tokens.get(1);
		Token dot_tok = tokens.get(2);
		Token eof_tok = tokens.get(3);
		
		Token exp_dec = new Token(TokenType.Number, "1337.42", 1337.42, 1,0);
		Token exp_int = new Token(TokenType.Number, "1337", 1337.0, 2, 0);
		Token exp_dot = new Token(TokenType.DOT, ".", null, 2,4);
		Token exp_eof = new Token(TokenType.EOF, "", null, 2,5);
		assertEquals(exp_dec, dec_tok);
		assertEquals(exp_int, int_tok);
		assertEquals(exp_dot, dot_tok);
		assertEquals(exp_eof, eof_tok);
	}
}
