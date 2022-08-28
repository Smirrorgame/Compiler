package com.github.smirrorgame.compiler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.github.smirrorgame.compiler.Expr.Grouping;
import com.github.smirrorgame.compiler.Expr.Literal;
import com.github.smirrorgame.compiler.Expr.Unary;

class ParserTest {
	
	// AstPrinter only for debugging!
	//TODO: remove later when not used anymore
	AstPrinter printer = new AstPrinter();
	Parser p;
	Lexer l;
	List<Token> tokens;

	
	void setUp(String src) {
		l = new Lexer(src);
		tokens = l.tokenize();
		p = new Parser(tokens);
	}
	
	
	@Test
	public void test_primary() {
		
//		TEST PRIMARY TRUE
		String src_true = "true";
		setUp(src_true);
		Expr res = p.parse();
//		check if expr is of type Literal
		assertTrue(res instanceof Literal);
		Literal res_literal = (Literal) res;
//		check if value of literal is of type boolean
		assertTrue(res_literal.value instanceof Boolean);
//		check if value of literal is 'true' as expected
		assertEquals(true, res_literal.value);
		
//		TEST PRIMARY FALSE
		String src_false = "false";
		setUp(src_false);
		res = p.parse();
//		check if expr is of type Literal
		assertTrue(res instanceof Literal);
		res_literal = (Literal) res;
//		check if value of literal is of type boolean
		assertTrue(res_literal.value instanceof Boolean);
//		check if value of literal is 'false' as expected
		assertEquals(false, res_literal.value);
		
		
//		TEST PRIMARY NULL
		String src_null= "null";
		setUp(src_null);
		res = p.parse();
//		check if expr is of type Literal
		assertTrue(res instanceof Literal);
		res_literal = (Literal) res;
//		check if value of literal is null
		assertNull(res_literal.value);
		
//		TEST PRIMARY NUMBER
		String src_number= "10";
		setUp(src_number);
		res = p.parse();
//		check if expr is of type Literal
		assertTrue(res instanceof Literal);
		res_literal = (Literal) res;
//		check if value of literal is of type Double
		assertTrue(res_literal.value instanceof Double);
//		check if value of literal is '10.0' as expected
		assertEquals(10.0, res_literal.value);
		
		
//		TEST PRIMARY STRING
		String src_string= "\"string\"";
		setUp(src_string);
		res = p.parse();
//		check if expr is of type Literal
		assertTrue(res instanceof Literal);
		res_literal = (Literal) res;
//		check if value of literal is of type String
		assertTrue(res_literal.value instanceof String);
//		check if value of literal is 'string' as expected
		assertEquals("string", res_literal.value);
		
		
//		TEST PRIMARY PARENTHESIS
		String src_paren = "(1)";
		setUp(src_paren);
		res = p.parse();
//		check if expr is of type Grouping
		assertTrue(res instanceof Grouping);
		Grouping res_group = (Grouping) res;
//		check if expr inside group is of type Literal
		assertTrue(res_group.expression instanceof Literal);
//		check if value of literal is '1.0' as expected
		assertEquals(1.0, ((Literal) res_group.expression).value);
		
//		TEST PRIMARY PARENTHESIS EMPTY
//		getting error output and setting new error output stream
		ByteArrayOutputStream errors = new ByteArrayOutputStream();
		PrintStream original_err = System.err;
		System.setErr(new PrintStream(errors));

		String src_paren_empty = "()";
		setUp(src_paren_empty);
		res = p.parse();
//		check if expr is null
		assertNull(res);
		
//		checking errors
		String errorString = errors.toString();
		errorString = errorString.replace("\n", "").replace("\r", "");
		String exp_errors = "[Line 1, column 1] Error: Expected expression.";
		assertEquals(exp_errors, errorString);
		
		System.setErr(original_err);
		
//		TEST PRIMARY PARENTHESIS MISSING
//		getting error output and setting new error output stream
		errors = new ByteArrayOutputStream();
		original_err = System.err;
		System.setErr(new PrintStream(errors));

		String src_paren_missing= "(10";
		setUp(src_paren_missing);
		res = p.parse();
//		check if expr is null
		assertNull(res);
		
//		checking errors
		errorString = errors.toString();
		errorString = errorString.replace("\n", "").replace("\r", "");
		exp_errors = "[Line 1, column 3] Error: Expected ')' after expression.";
		assertEquals(exp_errors, errorString);
		
		System.setErr(original_err);
		
	}

	@Test
	public void test_unary() {
		
//		TEST UNARY NOT LITERAL
		String src_not_literal = "!true";
		setUp(src_not_literal);
		Expr res = p.parse();
//		check if Expr is of type unary
		assertTrue(res instanceof Unary);
		Unary res_unary = (Unary) res;
//		check if operator is as expected
		Token operator = new Token(TokenType.NOT, "!", null, 1, 0);
		assertEquals(operator, res_unary.operator);
//		check if right hand side expr is of type Literal
		assertTrue(res_unary.right instanceof Literal);
//		check if value of literal is of type boolean
		assertTrue(((Literal) res_unary.right).value instanceof Boolean);
//		check if value of literal is 'true' as expected
		assertEquals(true, ((Literal) res_unary.right).value);
		
//		TEST UNARY MINUS LITERAL
		String src_minus_literal = "-1";
		setUp(src_minus_literal);
		res = p.parse();
//		check if Expr is of type unary
		assertTrue(res instanceof Unary);
		res_unary = (Unary) res;
//		check if operator is as expected
		operator = new Token(TokenType.MINUS, "-", null, 1, 0);
		assertEquals(operator, res_unary.operator);
//		check if right hand side expr is of type Literal
		assertTrue(res_unary.right instanceof Literal);
//		check if value of literal is of type double
		assertTrue(((Literal) res_unary.right).value instanceof Double);
//		check if value of literal is 'true' as expected
		assertEquals(1.0, ((Literal) res_unary.right).value);
		
	
		//TEST UNARY NOT GROUPING
		String src_not_grouping = "!(false)";
		setUp(src_not_grouping);
		res = p.parse();

		//		check if Expr is of type unary
		assertTrue(res instanceof Unary);
		res_unary = (Unary) res;
		//		check if operator is as expected
		operator = new Token(TokenType.NOT, "!", null, 1, 0);
		assertEquals(operator, res_unary.operator);
		//		check if right hand side expr is of type Grouping
		assertTrue(res_unary.right instanceof Grouping);
		Grouping res_unary_group = (Grouping) res_unary.right;
		assertTrue(res_unary_group.expression instanceof Literal);
		//		check if value of literal is of type boolean
		assertTrue(((Literal) res_unary_group.expression).value instanceof Boolean);
		//		check if value of literal is 'false' as expected
		assertEquals(false, ((Literal) res_unary_group.expression).value);
		
//		TEST UNARY MINUS GROUPING
		String src_minus_grouping = "-(10)";
		setUp(src_minus_grouping);
		res = p.parse();
//		check if Expr is of type unary
		assertTrue(res instanceof Unary);
		res_unary = (Unary) res;
//		check if operator is as expected
		operator = new Token(TokenType.MINUS, "-", null, 1, 0);
		assertEquals(operator, res_unary.operator);
//		check if right hand side expr is of type Grouping
		assertTrue(res_unary.right instanceof Grouping);
		res_unary_group = (Grouping) res_unary.right;
		assertTrue(res_unary_group.expression instanceof Literal);
//		check if value of literal is of type double
		assertTrue(((Literal) res_unary_group.expression).value instanceof Double);
//		check if value of literal is '10.0' as expected
		assertEquals(10.0, ((Literal) res_unary_group.expression).value);
	}
}
