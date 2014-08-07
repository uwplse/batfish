package batfish.representation.cisco;

import batfish.representation.Configuration;
import batfish.representation.PolicyMapSetLine;
import batfish.representation.PolicyMapSetLocalPreferenceLine;

public class RouteMapSetLocalPreferenceLine extends RouteMapSetLine {

   private static final long serialVersionUID = 1L;

   private int _localPreference;

   public RouteMapSetLocalPreferenceLine(int localPreference) {
      _localPreference = localPreference;
   }

   public int getLocalPreference() {
      return _localPreference;
   }

   @Override
   public PolicyMapSetLine toPolicyMapSetLine(Configuration c) {
      return new PolicyMapSetLocalPreferenceLine(_localPreference);
   }

   @Override
   public RouteMapSetType getType() {
      return RouteMapSetType.LOCAL_PREFERENCE;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      if (((RouteMapSetLine) o).getType() != RouteMapSetType.LOCAL_PREFERENCE) {
         return false;
      }

      RouteMapSetLocalPreferenceLine rhsLine = (RouteMapSetLocalPreferenceLine) o;
      return getLocalPreference() == (rhsLine.getLocalPreference());
   }
}
