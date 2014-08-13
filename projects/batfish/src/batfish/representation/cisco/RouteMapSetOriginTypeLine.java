package batfish.representation.cisco;

import batfish.representation.Configuration;
import batfish.representation.OriginType;
import batfish.representation.PolicyMapSetLine;
import batfish.representation.PolicyMapSetOriginTypeLine;
import batfish.util.Util;

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
   public PolicyMapSetLine toPolicyMapSetLine(Configuration c) {
      return new PolicyMapSetOriginTypeLine(_originType);
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      if (((RouteMapSetLine) o).getType() != RouteMapSetType.ORIGIN_TYPE) {
         return false;
      }

      RouteMapSetOriginTypeLine rhsLine = (RouteMapSetOriginTypeLine) o;
      return Util.equalOrNull(_asNum, rhsLine._asNum)
            && Util.equalOrNull(_originType, rhsLine._originType);
   }

}
