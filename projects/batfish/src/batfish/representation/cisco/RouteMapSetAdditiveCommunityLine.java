package batfish.representation.cisco;

import java.util.List;

import batfish.representation.Configuration;
import batfish.representation.PolicyMapSetAddCommunityLine;
import batfish.representation.PolicyMapSetLine;

public class RouteMapSetAdditiveCommunityLine extends RouteMapSetLine {

   private List<Long> _communities;

   public RouteMapSetAdditiveCommunityLine(List<Long> communities) {
      _communities = communities;
   }

   public List<Long> getCommunities() {
      return _communities;
   }

   @Override
   public PolicyMapSetLine toPolicyMapSetLine(Configuration c) {
      return new PolicyMapSetAddCommunityLine(_communities);
   }
   
}