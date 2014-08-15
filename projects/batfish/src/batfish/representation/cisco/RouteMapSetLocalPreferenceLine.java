package batfish.representation.cisco;

import batfish.representation.Configuration;
import batfish.representation.PolicyMapSetLine;
import batfish.representation.PolicyMapSetLocalPreferenceLine;
import batfish.representation.RepresentationObject;

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

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string + "\n");
         System.out.println("+ " + string + "._localPreference:"
               + _localPreference + "\n");
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string + "\n");
         System.out.println("- " + string + "._localPreference:"
               + _localPreference + "\n");
         System.out.flush();
         return;
      }

      if (((RouteMapSetLine) o).getType() != RouteMapSetType.LOCAL_PREFERENCE) {
         ((RepresentationObject) o).diffRepresentation(null, string, true);
         diffRepresentation(null, string, false);
         return;
      }

      RouteMapSetLocalPreferenceLine rhs = (RouteMapSetLocalPreferenceLine) o;
      if (_localPreference != rhs._localPreference) {
         System.out.println("- " + string + "._localPreference:"
               + _localPreference + "\n");
         System.out.println("+ " + string + "._localPreference:"
               + rhs._localPreference + "\n");
      }
      System.out.flush();
      return;
   }
}
