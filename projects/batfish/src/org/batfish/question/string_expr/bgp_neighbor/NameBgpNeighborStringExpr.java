package org.batfish.question.string_expr.bgp_neighbor;

import org.batfish.question.Environment;
import org.batfish.question.bgp_neighbor_expr.BgpNeighborExpr;
import org.batfish.representation.BgpNeighbor;

public class NameBgpNeighborStringExpr extends BgpNeighborStringExpr {

   public NameBgpNeighborStringExpr(BgpNeighborExpr caller) {
      super(caller);
   }

   @Override
   public String evaluate(Environment environment) {
      BgpNeighbor caller = _caller.evaluate(environment);
      return caller.getPrefix().toString();
   }

}