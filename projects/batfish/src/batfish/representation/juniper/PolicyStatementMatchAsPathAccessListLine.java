package batfish.representation.juniper;

import batfish.representation.RepresentationObject;
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

      if (((PolicyStatementMatchLine) o).getType() != MatchType.AS_PATH_ACCESS_LIST) {
         ((RepresentationObject) o).diffRepresentation(null, string, true);
         diffRepresentation(null, string, false);
         return;
      }

      PolicyStatementMatchAsPathAccessListLine rhs = (PolicyStatementMatchAsPathAccessListLine) o;
      if (!Util.equalOrNull(_listName, rhs._listName)) {
         System.out.println("- " + string + "._listName:" + Util.objectToString(_listName) + "\n");
         System.out.println("+ " + string + "._listName:" + Util.objectToString(rhs._listName) + "\n");
      }
      System.out.flush();
      return;
   }

}
