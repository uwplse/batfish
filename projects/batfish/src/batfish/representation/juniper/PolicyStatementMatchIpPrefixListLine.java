package batfish.representation.juniper;

import java.util.List;

import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class PolicyStatementMatchIpPrefixListLine extends
      PolicyStatementMatchLine {

   private List<String> _listNames;

   public PolicyStatementMatchIpPrefixListLine(List<String> listNames) {
      _listNames = listNames;
   }

   @Override
   public MatchType getType() {
      return MatchType.ROUTE_FILTER;
   }

   public List<String> getListNames() {
      return _listNames;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      if (((PolicyStatementMatchLine) o).getType() != MatchType.ROUTE_FILTER) {
         return false;
      }

      PolicyStatementMatchIpPrefixListLine rhsLine = (PolicyStatementMatchIpPrefixListLine) o;
      return Util.cmpRepresentationLists(_listNames, rhsLine._listNames) == 0;
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string + "\n");
         Util.diffRepresentationLists(null, _listNames, string + "._listNames");
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string + "\n");
         Util.diffRepresentationLists(_listNames, null, string + "._listNames");
         System.out.flush();
         return;
      }

      if (((PolicyStatementMatchLine) o).getType() != MatchType.ROUTE_FILTER) {
         ((RepresentationObject) o).diffRepresentation(null, string, true);
         diffRepresentation(null, string, false);
         return;
      }

      PolicyStatementMatchIpPrefixListLine rhs = (PolicyStatementMatchIpPrefixListLine) o;
      Util.diffRepresentationLists(_listNames, rhs._listNames, string + "._listNames");
      System.out.flush();
      return;
   }

}
