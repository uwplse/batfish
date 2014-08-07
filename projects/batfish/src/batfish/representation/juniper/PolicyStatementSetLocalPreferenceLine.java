package batfish.representation.juniper;

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

}
