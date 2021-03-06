package org.batfish.representation.cisco;

import org.batfish.datamodel.Configuration;
import org.batfish.datamodel.OriginType;
import org.batfish.datamodel.PolicyMapSetLine;
import org.batfish.datamodel.PolicyMapSetOriginTypeLine;
import org.batfish.main.Warnings;

public class RouteMapSetOriginTypeLine extends RouteMapSetLine {

   private static final long serialVersionUID = 1L;

   private Integer _asNum;
   private OriginType _originType;

   public RouteMapSetOriginTypeLine(OriginType originType, Integer asNum) {
      _originType = originType;
      _asNum = asNum;
   }

   public Integer getAsNum() {
      return _asNum;
   }

   public OriginType getOriginType() {
      return _originType;
   }

   @Override
   public RouteMapSetType getType() {
      return RouteMapSetType.ORIGIN_TYPE;
   }

   @Override
   public PolicyMapSetLine toPolicyMapSetLine(CiscoConfiguration v,
         Configuration c, Warnings w) {
      return new PolicyMapSetOriginTypeLine(_originType);
   }

}
