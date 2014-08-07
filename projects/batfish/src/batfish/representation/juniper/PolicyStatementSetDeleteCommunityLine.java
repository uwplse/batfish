package batfish.representation.juniper;

import batfish.util.Util;

public class PolicyStatementSetDeleteCommunityLine extends
      PolicyStatementSetLine {

   private String _listName;

   public PolicyStatementSetDeleteCommunityLine(String listName) {
      _listName = listName;
   }

   @Override
   public SetType getSetType() {
      return SetType.DELETE_COMMUNITY;
   }

   public String getListName() {
      return _listName;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      if (((PolicyStatementSetLine) o).getSetType() != SetType.DELETE_COMMUNITY) {
         return false;
      }

      PolicyStatementSetDeleteCommunityLine rhsLine = (PolicyStatementSetDeleteCommunityLine) o;
      return Util.equalOrNull(_listName, rhsLine._listName);
   }
}
