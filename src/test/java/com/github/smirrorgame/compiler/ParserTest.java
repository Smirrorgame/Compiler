package com.github.smirrorgame.compiler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.github.smirrorgame.compiler.Expr.Binary;
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
	
	@Test
	public void test_factor() {
		
		
//		TESTING ASTERISK
		
		String src_asterisk = "3*2";
		setUp(src_asterisk);
		Expr res = p.parse();
		assertTrue(res instanceof Binary);
		Binary res_binary = (Binary) res;
		assertTrue(res_binary.left instanceof Literal);
		assertTrue(res_binary.right instanceof Literal);
		Token operator = new Token(TokenType.ASTERISK, "*", null, 1,1);
		assertEquals(operator, res_binary.operator);
		assertEquals(3.0, ((Literal) res_binary.left).value);
		assertEquals(2.0, ((Literal) res_binary.right).value);
		
		
		String src_asterisk_group= "(3)*2";
		setUp(src_asterisk_group);
		res = p.parse();
		assertTrue(res instanceof Binary);
		res_binary = (Binary) res;
		assertTrue(res_binary.left instanceof Grouping);
		assertTrue(res_binary.right instanceof Literal);
		operator = new Token(TokenType.ASTERISK, "*", null, 1,3);
		assertEquals(operator, res_binary.operator);
		assertEquals(2.0, ((Literal) res_binary.right).value);
		
		Grouping res_left_group = (Grouping) res_binary.left;
		assertTrue(res_left_group.expression instanceof Literal);
		assertEquals(3.0, ((Literal) res_left_group.expression).value);
		
		
		
//		TESTING FORWARD_SLASH
		String src_forwardslash= "1/4";
		setUp(src_forwardslash);
		res = p.parse();
		assertTrue(res instanceof Binary);
		res_binary = (Binary) res;
		assertTrue(res_binary.left instanceof Literal);
		assertTrue(res_binary.right instanceof Literal);
		operator = new Token(TokenType.FORWARD_SLASH, "/", null, 1,1);
		assertEquals(operator, res_binary.operator);
		assertEquals(1.0, ((Literal) res_binary.left).value);
		assertEquals(4.0, ((Literal) res_binary.right).value);
		
		String src_slash_group= "(1)/4";
		setUp(src_slash_group);
		res = p.parse();
		assertTrue(res instanceof Binary);
		res_binary = (Binary) res;
		assertTrue(res_binary.left instanceof Grouping);
		assertTrue(res_binary.right instanceof Literal);
		operator = new Token(TokenType.FORWARD_SLASH, "/", null, 1,3);
		assertEquals(operator, res_binary.operator);
		assertEquals(4.0, ((Literal) res_binary.right).value);
		
		res_left_group = (Grouping) res_binary.left;
		assertTrue(res_left_group.expression instanceof Literal);
		assertEquals(1.0, ((Literal) res_left_group.expression).value);
	}

	@Test
	public void test_term() {
		
//		TEST MINUS
		String src_minus = "3-2";
		setUp(src_minus);
		Expr res = p.parse();
		assertTrue(res instanceof Binary);
		Binary res_binary = (Binary) res;
		Token operator = new Token(TokenType.MINUS, "-", null, 1, 1);
		assertEquals(operator, res_binary.operator);
		assertTrue(res_binary.left instanceof Literal);
		assertTrue(res_binary.right instanceof Literal);
		
		assertEquals(3.0, ((Literal) res_binary.left).value);
		assertEquals(2.0, ((Literal) res_binary.right).value);
		
		
//		TEST MINUS GROUPING
		String src_minus_group = "3-(2)";
		setUp(src_minus_group);
		res = p.parse();
		assertTrue(res instanceof Binary);
		res_binary = (Binary) res;
		operator = new Token(TokenType.MINUS, "-", null, 1, 1);
		assertEquals(operator, res_binary.operator);
		assertTrue(res_binary.left instanceof Literal);
		assertTrue(res_binary.right instanceof Grouping);
		assertEquals(3.0, ((Literal) res_binary.left).value);
		Grouping res_binary_group = (Grouping) res_binary.right;
		assertTrue(res_binary_group.expression instanceof Literal);
		assertEquals(2.0, ((Literal) res_binary_group.expression).value);
		
		
//		TEST PLUS		
		String src_plus= "1+50";
		setUp(src_plus);
		res = p.parse();
		assertTrue(res instanceof Binary);
		res_binary = (Binary) res;
		operator = new Token(TokenType.PLUS, "+", null, 1, 1);
		assertEquals(operator, res_binary.operator);
		assertTrue(res_binary.left instanceof Literal);
		assertTrue(res_binary.right instanceof Literal);
		
		assertEquals(1.0, ((Literal) res_binary.left).value);
		assertEquals(50.0, ((Literal) res_binary.right).value);
		
//		TEST PLUS GROUPING
		String src_plus_group = "3+(2)";
		setUp(src_plus_group);
		res = p.parse();
		assertTrue(res instanceof Binary);
		res_binary = (Binary) res;
		operator = new Token(TokenType.PLUS, "+", null, 1, 1);
		assertEquals(operator, res_binary.operator);
		assertTrue(res_binary.left instanceof Literal);
		assertTrue(res_binary.right instanceof Grouping);
		assertEquals(3.0, ((Literal) res_binary.left).value);
		res_binary_group = (Grouping) res_binary.right;
		assertTrue(res_binary_group.expression instanceof Literal);
		assertEquals(2.0, ((Literal) res_binary_group.expression).value);
		
	}

	@Test
	public void test_comparison() {
		
//		TEST GREATER
		String src_greater = "2>1";
		setUp(src_greater);
		Expr res = p.parse();
		assertTrue(res instanceof Binary);
		Binary res_bin = (Binary) res;
		assertTrue(res_bin.left instanceof Literal);
		assertTrue(res_bin.right instanceof Literal);
		
		Token operator = new Token(TokenType.GREATER, ">", null, 1, 1);
		assertEquals(operator, res_bin.operator);
		assertEquals(2.0, ((Literal) res_bin.left).value);
		assertEquals(1.0, ((Literal) res_bin.right).value);
		
		
//		TEST GREATER_EQUAL
		String src_greaterequal = "10>=2";
		setUp(src_greaterequal);
		res = p.parse();
		assertTrue(res instanceof Binary);
		res_bin = (Binary) res;
		assertTrue(res_bin.left instanceof Literal);
		assertTrue(res_bin.right instanceof Literal);
		
		operator = new Token(TokenType.GREATER_EQUAL, ">=", null, 1, 2);
		assertEquals(operator, res_bin.operator);
		assertEquals(10.0, ((Literal) res_bin.left).value);
		assertEquals(2.0, ((Literal) res_bin.right).value);
		
//		TEST LESS
		String src_less= "1<20";
		setUp(src_less);
		res = p.parse();
		assertTrue(res instanceof Binary);
		res_bin = (Binary) res;
		assertTrue(res_bin.left instanceof Literal);
		assertTrue(res_bin.right instanceof Literal);
		
		operator = new Token(TokenType.LESS, "<", null, 1, 1);
		assertEquals(operator, res_bin.operator);
		assertEquals(1.0, ((Literal) res_bin.left).value);
		assertEquals(20.0, ((Literal) res_bin.right).value);
		
		
//		TEST LESS_EQUAL
		String src_lessequal = "1<=2";
		setUp(src_lessequal);
		res = p.parse();
		assertTrue(res instanceof Binary);
		res_bin = (Binary) res;
		assertTrue(res_bin.left instanceof Literal);
		assertTrue(res_bin.right instanceof Literal);
		
		operator = new Token(TokenType.LESS_EQUAL, "<=", null, 1, 1);
		assertEquals(operator, res_bin.operator);
		assertEquals(1.0, ((Literal) res_bin.left).value);
		assertEquals(2.0, ((Literal) res_bin.right).value);
		
	}

	@Test
	public void test_equality() {
		String src_not_equal = "\"13\" != 13";
		setUp(src_not_equal);
		Expr res = p.parse();
		
		assertTrue(res instanceof Binary);
		Binary res_bin = (Binary) res;
		assertTrue(res_bin.left instanceof Literal);
		assertTrue(res_bin.right instanceof Literal);
		
		Token operator = new Token(TokenType.NOT_EQUAL, "!=", null, 1, 5);
		assertEquals(operator, res_bin.operator);
		assertEquals("13", ((Literal) res_bin.left).value);
		assertEquals(13.0, ((Literal) res_bin.right).value);
		
		
		String src_equal = "13 == 13";
		setUp(src_equal);
		res = p.parse();
		
		assertTrue(res instanceof Binary);
		res_bin = (Binary) res;
		assertTrue(res_bin.left instanceof Literal);
		assertTrue(res_bin.right instanceof Literal);
		
		operator = new Token(TokenType.EQUAL_EQUAL, "==", null, 1, 3);
		assertEquals(operator, res_bin.operator);
		assertEquals(13.0, ((Literal) res_bin.left).value);
		assertEquals(13.0, ((Literal) res_bin.right).value);		
	}
	
}