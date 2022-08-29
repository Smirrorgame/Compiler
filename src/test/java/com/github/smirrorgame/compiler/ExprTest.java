package com.github.smirrorgame.compiler;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.github.smirrorgame.compiler.Expr.Binary;
import com.github.smirrorgame.compiler.Expr.Grouping;
import com.github.smirrorgame.compiler.Expr.Literal;
import com.github.smirrorgame.compiler.Expr.Unary;
import com.github.smirrorgame.compiler.Expr.Visitor;

class ExprTest {
	
	final Visitor<Integer> visitor = new Visitor<Integer>() {

		@Override
		public Integer visitBinaryExpr(Binary binary) {
			return 0;
		}

		@Override
		public Integer visitGroupingExpr(Grouping grouping) {
			return 1;
		}

		@Override
		public Integer visitLiteralExpr(Literal literal) {
			return 2;
		}

		@Override
		public Integer visitUnaryExpr(Unary unary) {
			return 3;
		}
	};

	@Test
	public void test_Binary() {
		Expr left = new Literal(20);
		Expr right = new Literal("Test");
		Token operator = new Token(TokenType.PLUS, "+", null, 1, 1);
		
		Binary b = new Binary(left, operator, right);
		assertEquals(left, b.left);
		assertEquals(right, b.right);
		assertEquals(operator, b.operator);
		
		int res = b.accept(visitor);
		assertEquals(0, res);
		
	}
	
	@Test
	public void test_Grouping() {
		Token operator = new Token(TokenType.PLUS, "+", null, 1, 1);
		Expr expression = new Binary(null, operator, null);
		Grouping g = new Grouping(expression);
		
		assertEquals(expression, g.expression);
		
		int res = g.accept(visitor);
		assertEquals(1, res);
	}
	
	@Test
	public void test_Literal() {
		int value = 40;		
		Literal l = new Literal(value);
		assertEquals(value, l.value);
		
		int res = l.accept(visitor);
		assertEquals(2, res);
	}	
	
	@Test
	public void test_Unary() {
		Expr right = new Literal(40);
		Token operator = new Token(TokenType.MINUS, "-", null, 1, 0);
		
		Unary u = new Unary(operator, right);
		assertEquals(right, u.right);
		assertEquals(operator, u.operator);
		
		int res = u.accept(visitor);
		assertEquals(3, res);
	}

}
