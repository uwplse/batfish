package batfish.representation.juniper;

import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class PolicyStatementSetLocalPreferenceLine extends
      PolicyStatementSetLine {

   private int _localPreference;

   public PolicyStatementSetLocalPreferenceLine(int localPreference) {
      _localPreference = localPreference;
   }

   @Override
   public SetType getSetType() {
      return SetType.LOCAL_PREFERENCE;
   }

   public int getLocalPreference() {
      return _localPreference;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      if (((PolicyStatementSetLine) o).getSetType() != SetType.LOCAL_PREFERENCE) {
         return false;
      }

      PolicyStatementSetLocalPreferenceLine rhsLine = (PolicyStatementSetLocalPreferenceLine) o;
      return Util.equalOrNull(_localPreference, rhsLine._localPreference);
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string);
         System.out.println("+ " + string + "._localPreference:"
               + _localPreference);
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string);
         System.out.println("- " + string + "._localPreference:"
               + _localPreference);
         System.out.flush();
         return;
      }

      if (((PolicyStatementSetLine) o).getSetType() != SetType.LOCAL_PREFERENCE) {
         ((RepresentationObject) o).diffRepresentation(null, string, true);
         diffRepresentation(null, string, false);
         return;
      }

      PolicyStatementSetLocalPreferenceLine rhs = (PolicyStatementSetLocalPreferenceLine) o;
      if (_localPreference != rhs._localPreference) {
         System.out.println("- " + string + "._localPreference:"
               + _localPreference);
         System.out.println("+ " + string + "._localPreference:"
               + rhs._localPreference);
      }
      System.out.flush();
      return;
   }

}
