package batfish.representation.juniper;

import batfish.util.Util;

public class PolicyStatementMatchAsPathAccessListLine extends
      PolicyStatementMatchLine {

   private String _listName;

   public PolicyStatementMatchAsPathAccessListLine(String listName) {
      _listName = listName;
   }

   @Override
   public MatchType getType() {
      return MatchType.AS_PATH_ACCESS_LIST;
   }

   public String getListName() {
      return _listName;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      if (((PolicyStatementMatchLine) o).getType() != MatchType.AS_PATH_ACCESS_LIST) {
         return false;
      }

      PolicyStatementMatchAsPathAccessListLine rhsLine = (PolicyStatementMatchAsPathAccessListLine) o;
      return Util.equalOrNull(_listName, rhsLine._listName);
   }

}
