package batfish.representation.juniper;

import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class PolicyStatementSetAdditiveCommunityLine extends
      PolicyStatementSetLine {

   private String _communities;

   public PolicyStatementSetAdditiveCommunityLine(String communities) {
      _communities = communities;
   }

   @Override
   public SetType getSetType() {
      return SetType.ADDITIVE_COMMUNITY;
   }

   public String getCommunities() {
      return _communities;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      if (((PolicyStatementSetLine) o).getSetType() != SetType.ADDITIVE_COMMUNITY) {
         return false;
      }

      PolicyStatementSetAdditiveCommunityLine rhsLine = (PolicyStatementSetAdditiveCommunityLine) o;
      return Util.equalOrNull(_communities, rhsLine._communities);
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string);
         System.out.println("+ " + string + "._communities:"
               + Util.objectToString(_communities));
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string);
         System.out.println("- " + string + "._communities:"
               + Util.objectToString(_communities));
         System.out.flush();
         return;
      }

      if (((PolicyStatementSetLine) o).getSetType() != SetType.ADDITIVE_COMMUNITY) {
         ((RepresentationObject) o).diffRepresentation(null, string, true);
         diffRepresentation(null, string, false);
         return;
      }

      PolicyStatementSetAdditiveCommunityLine rhs = (PolicyStatementSetAdditiveCommunityLine) o;
      if (!Util.equalOrNull(_communities, rhs._communities)) {
         System.out.println("- " + string + "._communities:"
               + Util.objectToString(_communities));
         System.out.println("+ " + string + "._communities:"
               + Util.objectToString(rhs._communities));
      }
      System.out.flush();
      return;
   }

}
