package batfish.representation.juniper;

import java.util.List;

import batfish.representation.RepresentationObject;
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
      return Util.cmpRepresentationLists(_protocol, rhsLine._protocol) == 0;
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string + "\n");
         Util.diffRepresentationLists(null, _protocol, string + "._protocol");
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string + "\n");
         Util.diffRepresentationLists(_protocol, null, string + "._protocol");
         System.out.flush();
         return;
      }

      if (((PolicyStatementMatchLine) o).getType() != MatchType.PROTOCOL) {
         ((RepresentationObject) o).diffRepresentation(null, string, true);
         diffRepresentation(null, string, false);
         return;
      }

      PolicyStatementMatchProtocolLine rhs = (PolicyStatementMatchProtocolLine) o;
      Util.diffRepresentationLists(_protocol, rhs._protocol, string + "._protocol");
      System.out.flush();
      return;
   }

}
