package batfish.representation.juniper;

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
}
