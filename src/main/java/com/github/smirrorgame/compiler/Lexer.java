package com.github.smirrorgame.compiler;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
	
	private String src;
	private List<Token> tokens = new ArrayList<>();
	private int start = 0;
	  private int current = 0;
	  private int line = 1;
	
	
	public Lexer(String src) {
		this.src = src;
	}
	
	public List<Token> tokenize() {
		while(!isAtEnd()) {
			start = current;
			lexToken();
		}
		
		tokens.add(new Token(TokenType.EOF, "", null, line));
		return tokens;
	}

	private void lexToken() {
		char c = advance();
		switch (c) {
		case '(':
			addToken(TokenType.Left_PARENTHESIS);
			break;

		default:
			break;
		}
	}

	private char advance() {
		return src.charAt(current++);
	}

	private void addToken(TokenType type) {
		addToken(type, null);
	}
	
	private void addToken(TokenType type, Object literal) {
		String text = src.substring(start, current);
		tokens.add(new Token(type, text, literal, line));
	}

	private boolean isAtEnd() {
		return current >= src.length();
	}

}
