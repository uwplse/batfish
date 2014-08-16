package batfish.representation.juniper;

import java.util.List;

import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class PolicyStatementMatchIpAccessListLine extends
      PolicyStatementMatchLine {

   private List<String> _listNames;

   public PolicyStatementMatchIpAccessListLine(List<String> listNames) {
      _listNames = listNames;
   }

   @Override
   public MatchType getType() {
      return MatchType.IP_ACCESS_LIST;
   }

   public List<String> getListNames() {
      return _listNames;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      if (((PolicyStatementMatchLine) o).getType() != MatchType.IP_ACCESS_LIST) {
         return false;
      }

      PolicyStatementMatchIpAccessListLine rhsLine = (PolicyStatementMatchIpAccessListLine) o;
      return Util.cmpRepresentationLists(_listNames, rhsLine._listNames) == 0;
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string);
         Util.diffRepresentationLists(null, _listNames, string + "._listNames");
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string);
         Util.diffRepresentationLists(_listNames, null, string + "._listNames");
         System.out.flush();
         return;
      }

      if (((PolicyStatementMatchLine) o).getType() != MatchType.IP_ACCESS_LIST) {
         ((RepresentationObject) o).diffRepresentation(null, string, true);
         diffRepresentation(null, string, false);
         return;
      }

      PolicyStatementMatchIpAccessListLine rhs = (PolicyStatementMatchIpAccessListLine) o;
      Util.diffRepresentationLists(_listNames, rhs._listNames, string
            + "._listNames");
      System.out.flush();
      return;

   }

}
