package batfish.representation.cisco;

import batfish.representation.Configuration;
import batfish.representation.OriginType;
import batfish.representation.PolicyMapSetLine;
import batfish.representation.PolicyMapSetOriginTypeLine;
import batfish.representation.RepresentationObject;
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

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string + "\n");
         System.out.println("+ " + string + "._asNum:"
               + Util.objectToString(_asNum) + "\n");
         System.out.println("+ " + string + "._originType:"
               + Util.objectToString(_originType) + "\n");
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string + "\n");
         System.out.println("- " + string + "._asNum:"
               + Util.objectToString(_asNum) + "\n");
         System.out.println("- " + string + "._originType:"
               + Util.objectToString(_originType) + "\n");
         System.out.flush();
         return;
      }

      if (((RouteMapSetLine) o).getType() != RouteMapSetType.ORIGIN_TYPE) {
         ((RepresentationObject) o).diffRepresentation(null, string, true);
         diffRepresentation(null, string, false);
         return;
      }

      RouteMapSetOriginTypeLine rhs = (RouteMapSetOriginTypeLine) o;
      if (!Util.equalOrNull(_asNum, rhs._asNum)) {
         System.out.println("- " + string + "._asNum:"
               + Util.objectToString(_asNum) + "\n");
         System.out.println("+ " + string + "._asNum:"
               + Util.objectToString(rhs._asNum) + "\n");
      }
      if (!Util.equalOrNull(_originType, rhs._originType)) {
         System.out.println("- " + string + "._originType:"
               + Util.objectToString(_originType) + "\n");
         System.out.println("+ " + string + "._originType:"
               + Util.objectToString(rhs._originType) + "\n");
      }

      System.out.flush();
      return;
   }

}
