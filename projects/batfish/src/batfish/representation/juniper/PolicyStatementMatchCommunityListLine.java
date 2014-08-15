package batfish.representation.juniper;

import batfish.representation.RepresentationObject;
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

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string + "\n");
         System.out.println("+ " + string + "._listName:" + Util.objectToString(_listName) + "\n");
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string + "\n");
         System.out.println("- " + string + "._listName:" + Util.objectToString(_listName) + "\n");
         System.out.flush();
         return;
      }

      if (((PolicyStatementMatchLine) o).getType() != MatchType.COMMUNITY_LIST) {
         ((RepresentationObject) o).diffRepresentation(null, string, true);
         diffRepresentation(null, string, false);
         return;
      }

      PolicyStatementMatchCommunityListLine rhs = (PolicyStatementMatchCommunityListLine) o;
      if (!Util.equalOrNull(_listName, rhs._listName)) {
         System.out.println("- " + string + "._listName:" + Util.objectToString(_listName) + "\n");
         System.out.println("+ " + string + "._listName:" + Util.objectToString(rhs._listName) + "\n");
      }
      System.out.flush();
      return;
   }

}
