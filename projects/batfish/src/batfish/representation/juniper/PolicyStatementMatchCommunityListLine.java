package batfish.representation.juniper;

import batfish.util.Util;

public class PolicyStatementMatchCommunityListLine extends
      PolicyStatementMatchLine {

   private String _listName;

   public PolicyStatementMatchCommunityListLine(String listName) {
      _listName = listName;
   }

   @Override
   public MatchType getType() {
      return MatchType.COMMUNITY_LIST;
   }

   public String getListName() {
      return _listName;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      if (((PolicyStatementMatchLine) o).getType() != MatchType.COMMUNITY_LIST) {
         return false;
      }

      PolicyStatementMatchCommunityListLine rhsLine = (PolicyStatementMatchCommunityListLine) o;
      return Util.equalOrNull(_listName, rhsLine._listName);
   }

}
