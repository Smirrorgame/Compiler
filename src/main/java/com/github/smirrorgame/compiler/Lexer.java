package com.github.smirrorgame.compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer {
	
	private String src;
	private List<Token> tokens = new ArrayList<>();
	private int start = 0;
	private int current = 0;
	private int line = 1;
	private int col = 0;
	private static final Map<String, TokenType> reserved;

	static {
		reserved = new HashMap<>();
		reserved.put("and", TokenType.AND);
		reserved.put("class", TokenType.CLASS);
		reserved.put("else", TokenType.ELSE);
		reserved.put("false", TokenType.FALSE);
		reserved.put("for", TokenType.FOR);
		reserved.put("funct", TokenType.FUNCT);
		reserved.put("if", TokenType.IF);
		reserved.put("null", TokenType.NULL);
		reserved.put("or", TokenType.OR);
		reserved.put("print", TokenType.PRINT);
		reserved.put("return", TokenType.RETURN);
		reserved.put("super", TokenType.SUPER);
		reserved.put("this", TokenType.THIS);
		reserved.put("true", TokenType.TRUE);
		reserved.put("let", TokenType.LET);
		reserved.put("while", TokenType.WHILE);
	}
		
	public Lexer(String src) {
		this.src = src;
	}
	
	public List<Token> tokenize() {
		while(!isAtEnd()) {
			start = current;
			lexToken();
		}
		
		tokens.add(new Token(TokenType.EOF, "", null, line, col));
		return tokens;
	}

	private void lexToken() {
		char c = advance();
		switch (c) {
//			single character tokens
			case '(': addToken(TokenType.Left_PARENTHESIS); break;
			case ')': addToken(TokenType.RIGHT_PARENTHESIS); break;
			case '{': addToken(TokenType.LEFT_BRACE); break;
			case '}': addToken(TokenType.RIGHT_BRACE); break;
			case ',': addToken(TokenType.COMMA); break;
			case '.': addToken(TokenType.DOT); break;
			case '-': addToken(TokenType.MINUS); break;
			case '+': addToken(TokenType.PLUS); break;
			case ';': addToken(TokenType.SEMICOLON); break;
			case '*': addToken(TokenType.ASTERISK); break;
//			multi character tokens
			case '!':
				addToken(match('=') ? TokenType.NOT_EQUAL : TokenType.NOT);
		        break;
		    case '=':
		    	addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
		    	break;
		    case '<':
		    	addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
		    	break;
		    case '>':
		    	addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
		    	break;
		    case '/':
		    	if(match('/')) {
		    		while(peek() != '\n' && !isAtEnd()) advance();
		    	}else {
		    		addToken(TokenType.FORWARD_SLASH);
		    	}
//	    	ignore whitespaces
		    case ' ':
			case '\r':
			case '\t':
				break;
			case '\n':
				line++;
				col = 0;
				break;
//			literals and reserved keywords
			case '"':
				lexString();
				break;
			default:
				if(isDigit(c)) {
					lexNumber();
				}else if(isAlpha(c)) {
					lexIdentifier();
				}else {
					Error.error(line, current, "Unexpected Character: "+c);
				}
				break;
		}
	}

	private void lexIdentifier() {
		while(isAlphaNumeric(peek())) advance();
		String lexeme = src.substring(start, current);
		TokenType type = reserved.get(lexeme);
		if(type == null) type = TokenType.IDENTIFIER;
		addToken(type);
	}
	
	private boolean isAlphaNumeric(char c) {
		return isAlpha(c) || isDigit(c);
	}

	private boolean isAlpha(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
	}

	private void lexNumber() {
		while(isDigit(peek())) advance();
		
		if(peek() == '.' && isDigit(peekNext())) {
			advance();
			while(isDigit(peek())) advance();
		}
		double num = Double.parseDouble(src.substring(start, current));
		addToken(TokenType.Number, num);
	}

	private boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}

	private void lexString() {
		boolean hasError = false;
		while(peek() != '"' && !isAtEnd()) {
			if(peek() == '\n') {
				Error.error(line, current, "missing a double quote to terminate string.");
				hasError = true;
				line++;
				col = -1;
			}
			advance();
		}
		if(isAtEnd()) {
			Error.error(line, current, "Unterminated string.");
			return;
		}
		advance();
		if(hasError) return;
		String val = src.substring(start+1, current-1);
		addToken(TokenType.STRING, val);
	}

	private char peek() {
		if(isAtEnd()) return '\0';
		return src.charAt(current);
	}
	
	private char peekNext() {
		if(current + 1 >= src.length()) return '\0';
		return src.charAt(current + 1);
	}

	private boolean match(char expected) {
		if(isAtEnd()) return false;
		if(src.charAt(current) != expected) return false;
		col++;
		current++;
		return true;
	}

	private char advance() {
		++col;
		return src.charAt(current++);
	}

	private void addToken(TokenType type) {
		addToken(type, null);
	}
	
	private void addToken(TokenType type, Object literal) {
		String lexeme = src.substring(start, current);
		tokens.add(new Token(type, lexeme, literal, line, col - (current-start)));
	}

	private boolean isAtEnd() {
		return current >= src.length();
	}

}
