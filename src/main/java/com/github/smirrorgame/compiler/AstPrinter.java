package com.github.smirrorgame.compiler;

import com.github.smirrorgame.compiler.Expr.Binary;
import com.github.smirrorgame.compiler.Expr.Grouping;
import com.github.smirrorgame.compiler.Expr.Literal;
import com.github.smirrorgame.compiler.Expr.Unary;
import com.github.smirrorgame.compiler.Expr.Visitor;

public class AstPrinter implements Visitor<String> {

	
	public String print(Expr expr) {
		return expr.accept(this);
	}
	
	@Override
	public String visitBinaryExpr(Binary binary) {
		return "(" + binary.left.accept(this) + " " + binary.operator.lexeme + " " + binary.right.accept(this) + ")";
	}

	@Override
	public String visitGroupingExpr(Grouping grouping) {
		return "(" +  grouping.expression.accept(this) + ")";
	}

	@Override
	public String visitLiteralExpr(Literal literal) {
		if(literal.value == null) return "null";
		if(literal.value instanceof String) return "\"" + literal.value.toString() + "\"";
		return literal.value.toString();
	}

	@Override
	public String visitUnaryExpr(Unary unary) {
		return "(" + unary.operator.lexeme + unary.right.accept(this) + ")";
	}

}
