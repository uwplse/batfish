package batfish.representation.cisco;

import java.util.List;

import batfish.representation.Configuration;
import batfish.representation.PolicyMapSetLine;

public class RouteMapSetAsPathLine extends RouteMapSetLine {

   private static final long serialVersionUID = 1L;

   // private List<Integer> _asList;

   public RouteMapSetAsPathLine(List<Integer> asList) {
      // _asList = asList;
   }

   @Override
   public PolicyMapSetLine toPolicyMapSetLine(Configuration c) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public RouteMapSetType getType() {
      return RouteMapSetType.AS_PATH;
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      // TODO Auto-generated method stub

   }

}
