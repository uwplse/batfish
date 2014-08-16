package batfish.representation.juniper;

import batfish.representation.RepresentationObject;
import batfish.representation.juniper.MatchType;
import batfish.representation.juniper.PolicyStatementMatchLine;
import batfish.util.Util;

public class PolicyStatementMatchNeighborLine extends PolicyStatementMatchLine {

   private String _neighborIp;

   public PolicyStatementMatchNeighborLine(String neighborIP) {
      _neighborIp = neighborIP;
   }

   @Override
   public MatchType getType() {
      return MatchType.NEIGHBOR;
   }

   public String getNeighborIp() {
      return _neighborIp;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      if (((PolicyStatementMatchLine) o).getType() != MatchType.NEIGHBOR) {
         return false;
      }

      PolicyStatementMatchNeighborLine rhsLine = (PolicyStatementMatchNeighborLine) o;
      return Util.equalOrNull(_neighborIp, rhsLine._neighborIp);
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string);
         System.out.println("+ " + string + "._neighborIp:"
               + Util.objectToString(_neighborIp));
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string);
         System.out.println("- " + string + "._neighborIp:"
               + Util.objectToString(_neighborIp));
         System.out.flush();
         return;
      }

      if (((PolicyStatementMatchLine) o).getType() != MatchType.NEIGHBOR) {
         ((RepresentationObject) o).diffRepresentation(null, string, true);
         diffRepresentation(null, string, false);
         return;
      }

      PolicyStatementMatchNeighborLine rhs = (PolicyStatementMatchNeighborLine) o;
      if (!Util.equalOrNull(_neighborIp, rhs._neighborIp)) {
         System.out.println("- " + string + "._neighborIp:"
               + Util.objectToString(_neighborIp));
         System.out.println("+ " + string + "._neighborIp:"
               + Util.objectToString(rhs._neighborIp));
      }
      System.out.flush();
      return;
   }
}
