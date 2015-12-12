package org.batfish.question.boolean_expr.string;

import org.batfish.question.Environment;
import org.batfish.question.boolean_expr.BaseBooleanExpr;
import org.batfish.question.string_expr.StringExpr;

public final class StringEqExpr extends BaseBooleanExpr {

   private final StringExpr _lhs;

   private final StringExpr _rhs;

   public StringEqExpr(StringExpr lhs, StringExpr rhs) {
      _lhs = lhs;
      _rhs = rhs;
   }

   @Override
   public Boolean evaluate(Environment env) {
      String lhs = _lhs.evaluate(env);
      String rhs = _rhs.evaluate(env);
      return lhs.equals(rhs);
   }

}