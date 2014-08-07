package batfish.representation.juniper;

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

}
