package batfish.representation.juniper;

import java.util.List;

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
      return Util.sameRepresentationLists(_listNames, rhsLine._listNames);
   }

}
