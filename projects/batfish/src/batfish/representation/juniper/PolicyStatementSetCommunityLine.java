package batfish.representation.juniper;

import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class PolicyStatementSetCommunityLine extends PolicyStatementSetLine {

   private String _community;

   public PolicyStatementSetCommunityLine(String c) {
      _community = c;
   }

   @Override
   public SetType getSetType() {
      return SetType.COMMUNITY;
   }

   public String getCommunities() {
      return _community;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      if (((PolicyStatementSetLine) o).getSetType() != SetType.COMMUNITY) {
         return false;
      }

      PolicyStatementSetCommunityLine rhsLine = (PolicyStatementSetCommunityLine) o;
      return Util.equalOrNull(_community, rhsLine._community);
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string + "\n");
         System.out.println("+ " + string + "._community:" + Util.objectToString(_community) + "\n");
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string + "\n");
         System.out.println("- " + string + "._community:" + Util.objectToString(_community) + "\n");
         System.out.flush();
         return;
      }

      if (((PolicyStatementSetLine) o).getSetType() != SetType.COMMUNITY) {
         ((RepresentationObject) o).diffRepresentation(null, string, true);
         diffRepresentation(null, string, false);
         return;
      }

      PolicyStatementSetCommunityLine rhs = (PolicyStatementSetCommunityLine) o;
      if (!Util.equalOrNull(_community, rhs._community)) {
         System.out.println("- " + string + "._community:" + Util.objectToString(_community) + "\n");
         System.out.println("+ " + string + "._community:" + Util.objectToString(rhs._community) + "\n");
      }
      System.out.flush();
      return;
   }

}
