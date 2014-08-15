package batfish.representation.cisco;

import java.util.List;

import batfish.representation.Configuration;
import batfish.representation.PolicyMapSetCommunityLine;
import batfish.representation.PolicyMapSetLine;
import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class RouteMapSetCommunityLine extends RouteMapSetLine {

   private static final long serialVersionUID = 1L;

   private List<Long> _communities;

   public RouteMapSetCommunityLine(List<Long> communities) {
      _communities = communities;
   }

   public List<Long> getCommunities() {
      return _communities;
   }

   @Override
   public PolicyMapSetLine toPolicyMapSetLine(Configuration c) {
      return new PolicyMapSetCommunityLine(_communities);
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      if (((RouteMapSetLine) o).getType() != RouteMapSetType.COMMUNITY) {
         return false;
      }

      RouteMapSetCommunityLine rhsLine = (RouteMapSetCommunityLine) o;
      return getCommunities().equals(rhsLine.getCommunities());
   }

   @Override
   public RouteMapSetType getType() {
      return RouteMapSetType.COMMUNITY;
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string + "\n");
         Util.diffRepresentationLists(null, _communities, string
               + "._communities");
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string + "\n");
         Util.diffRepresentationLists(_communities, null, string
               + "._communities");
         System.out.flush();
         return;
      }

      if (((RouteMapSetLine) o).getType() != RouteMapSetType.COMMUNITY) {
         ((RepresentationObject) o).diffRepresentation(null, string, true);
         diffRepresentation(null, string, false);
         return;
      }

      RouteMapSetCommunityLine rhs = (RouteMapSetCommunityLine) o;

      Util.diffRepresentationLists(_communities, rhs._communities, string
            + "._communities");

      System.out.flush();
      return;
   }

}
