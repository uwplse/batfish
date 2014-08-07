package batfish.representation.juniper;

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

}
