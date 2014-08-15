package batfish.representation.cisco;

import batfish.representation.Configuration;
import batfish.representation.PolicyMapSetCommunityNoneLine;
import batfish.representation.PolicyMapSetLine;
import batfish.representation.RepresentationObject;

public class RouteMapSetCommunityNoneLine extends RouteMapSetLine {

   private static final long serialVersionUID = 1L;

   @Override
   public PolicyMapSetLine toPolicyMapSetLine(Configuration c) {
      return new PolicyMapSetCommunityNoneLine();
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      if (o == null)
         return false;
      if (((RouteMapSetLine) o).getType() != RouteMapSetType.COMMUNITY_NONE) {
         return false;
      }

      return true;
   }

   @Override
   public RouteMapSetType getType() {
      return RouteMapSetType.COMMUNITY_NONE;
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string + "\n");
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string + "\n");
         System.out.flush();
         return;
      }

      if (((RouteMapSetLine) o).getType() != RouteMapSetType.COMMUNITY_NONE) {
         ((RepresentationObject) o).diffRepresentation(null, string, true);
         diffRepresentation(null, string, false);
         return;
      }

      System.out.flush();
      return;

   }

}
