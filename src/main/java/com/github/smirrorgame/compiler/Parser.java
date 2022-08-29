package com.github.smirrorgame.compiler;

import java.util.List;

import com.github.smirrorgame.compiler.Expr.Binary;
import com.github.smirrorgame.compiler.Expr.Literal;
import com.github.smirrorgame.compiler.Expr.Unary;
import com.github.smirrorgame.compiler.Expr.Grouping;
	

public class Parser {
	
	private final List<Token> tokens;
	private int current = 0;
	
	Parser(List<Token> tokens) {
		this.tokens = tokens;
	}
	
	Expr parse() {
		try {
			return expression();
		} catch (Error e) {
			return null;
		}
	}
	
	private Token peek() {
		return tokens.get(current);
	}
	
	private Token previous() {
		return tokens.get(current - 1);
	}
	
	private boolean isAtEnd()  {
		return peek().type == TokenType.EOF;
	}
	
	private boolean checkTokenType(TokenType type) {
		if (isAtEnd()) return false;
		return peek().type == type;
	}
	
	private Token advance() {
		if(!isAtEnd()) current++;
		return previous();
	}
	
	private boolean matchesAnyTokenType(TokenType...tokentypes) {
		for (TokenType type : tokentypes) {
			if(checkTokenType(type)) {
				advance();
				return true;
			}
		}
		return false;
	}
	
	private Token consume(TokenType type, String errorMsg) {
		if(checkTokenType(type)) return advance();
		
		throw Error.error(peek(), errorMsg);
	}
	
	private Expr expression() {
		return equality();
	}
	
	private Expr equality() {
		Expr expr = comparison();
		while(matchesAnyTokenType(TokenType.NOT_EQUAL, TokenType.EQUAL_EQUAL)) {
			Token operator = previous();
			Expr right = comparison();
			expr = new Binary(expr, operator, right);
		}
		
		return expr;
	}
	
	private Expr comparison() {
		Expr expr = term();
		
		while(matchesAnyTokenType(TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL)) {
			Token operator = previous();
			Expr right = term();
			expr = new Binary(expr, operator, right);
		}
		return expr;
	}
	
	private Expr term() {
		Expr expr = factor();
		
		while(matchesAnyTokenType(TokenType.MINUS, TokenType.PLUS)) {
			Token operator = previous();
			Expr right = factor();
			expr = new Binary(expr, operator, right);
		}
		return expr;
	}
	
	private Expr factor() {
		Expr expr = unary();
		
		while(matchesAnyTokenType(TokenType.ASTERISK, TokenType.FORWARD_SLASH)) {
			Token operator = previous();
			Expr right = unary();
			expr = new Binary(expr, operator, right);
		}
		return expr;
	}
	
	private Expr unary() {
		if(matchesAnyTokenType(TokenType.NOT, TokenType.MINUS)) {
			Token operator = previous();
			Expr right = unary();
			return new Unary(operator, right);
		}
		
		return primary();
	}
	
	private Expr primary() {

		if(matchesAnyTokenType(TokenType.TRUE)) return new Literal(true);
		if(matchesAnyTokenType(TokenType.FALSE)) return new Literal(false);
		if(matchesAnyTokenType(TokenType.NULL)) return new Literal(null);
		
		if(matchesAnyTokenType(TokenType.Number, TokenType.STRING)) {
			return new Literal(previous().literal);
		}
		
		if(matchesAnyTokenType(TokenType.Left_PARENTHESIS)) {
			Expr expr = expression();
			consume(TokenType.RIGHT_PARENTHESIS, "Expected ')' after expression.");
			return new Grouping(expr);
		}

		throw Error.error(peek(), "Expected expression.");
	}
	
}
