package batfish.representation.juniper;

import java.util.List;

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
      return Util.sameRepresentationLists(_listNames, rhsLine._listNames);
   }

}
