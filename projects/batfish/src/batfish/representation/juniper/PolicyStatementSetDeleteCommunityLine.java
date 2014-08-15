package batfish.representation.juniper;

import batfish.representation.RepresentationObject;
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

      if (((PolicyStatementSetLine) o).getSetType() != SetType.DELETE_COMMUNITY) {
         ((RepresentationObject) o).diffRepresentation(null, string, true);
         diffRepresentation(null, string, false);
         return;
      }

      PolicyStatementSetDeleteCommunityLine rhs = (PolicyStatementSetDeleteCommunityLine) o;
      if (!Util.equalOrNull(_listName, rhs._listName)) {
         System.out.println("- " + string + "._listName:" + Util.objectToString(_listName) + "\n");
         System.out.println("+ " + string + "._listName:" + Util.objectToString(rhs._listName) + "\n");
      }
      System.out.flush();
      return;
   }
}
