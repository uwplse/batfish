package org.batfish.representation.juniper;

import org.batfish.common.BatfishException;
import org.batfish.datamodel.LineAction;
import org.batfish.datamodel.Prefix;
import org.batfish.datamodel.RouteFilterList;
import org.batfish.datamodel.SubRange;

public class RouteFilterLineLonger extends RouteFilterLine {

   /**
    *
    */
   private static final long serialVersionUID = 1L;

   public RouteFilterLineLonger(Prefix prefix) {
      super(prefix);
   }

   @Override
   public void applyTo(RouteFilterList rfl) {
      int prefixLength = _prefix.getPrefixLength();
      if (prefixLength >= 32) {
         throw new BatfishException(
               "Route filter prefix length cannot be 'longer' than 32");
      }
      org.batfish.datamodel.RouteFilterLine line = new org.batfish.datamodel.RouteFilterLine(
            LineAction.ACCEPT, _prefix, new SubRange(prefixLength + 1, 32));
      rfl.addLine(line);
   }

   @Override
   public boolean equals(Object o) {
      if (!this.getClass().equals(o.getClass())) {
         return false;
      }
      else {
         RouteFilterLineLonger rhs = (RouteFilterLineLonger) o;
         return _prefix.equals(rhs._prefix);
      }
   }

   @Override
   public int hashCode() {
      return _prefix.hashCode();
   }

}
