package org.batfish.representation.cisco;

import java.util.List;

import org.batfish.datamodel.Configuration;
import org.batfish.datamodel.PolicyMapSetAddCommunityLine;
import org.batfish.datamodel.PolicyMapSetLine;
import org.batfish.main.Warnings;

public class RouteMapSetAdditiveCommunityLine extends RouteMapSetLine {

   private static final long serialVersionUID = 1L;

   private List<Long> _communities;

   public RouteMapSetAdditiveCommunityLine(List<Long> communities) {
      _communities = communities;
   }

   public List<Long> getCommunities() {
      return _communities;
   }

   @Override
   public RouteMapSetType getType() {
      return RouteMapSetType.ADDITIVE_COMMUNITY;
   }

   @Override
   public PolicyMapSetLine toPolicyMapSetLine(CiscoConfiguration v,
         Configuration c, Warnings w) {
      return new PolicyMapSetAddCommunityLine(_communities);
   }

}
