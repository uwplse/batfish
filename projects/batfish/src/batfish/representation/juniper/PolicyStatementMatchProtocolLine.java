package batfish.representation.juniper;

import java.util.List;

import batfish.representation.juniper.MatchType;
import batfish.representation.juniper.PolicyStatementMatchLine;
import batfish.util.Util;

public class PolicyStatementMatchProtocolLine extends PolicyStatementMatchLine {

   private List<String> _protocol;

   public PolicyStatementMatchProtocolLine(List<String> protocol) {
      _protocol = protocol;
   }

   @Override
   public MatchType getType() {
      return MatchType.PROTOCOL;
   }

   public List<String> getProtocl() {
      return _protocol;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      if (((PolicyStatementMatchLine) o).getType() != MatchType.PROTOCOL) {
         return false;
      }

      PolicyStatementMatchProtocolLine rhsLine = (PolicyStatementMatchProtocolLine) o;
      return Util.sameRepresentationLists(_protocol, rhsLine._protocol);
   }

}
