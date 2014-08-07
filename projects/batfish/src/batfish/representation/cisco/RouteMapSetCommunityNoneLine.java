package batfish.representation.cisco;

import batfish.representation.Configuration;
import batfish.representation.PolicyMapSetCommunityNoneLine;
import batfish.representation.PolicyMapSetLine;

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

}
