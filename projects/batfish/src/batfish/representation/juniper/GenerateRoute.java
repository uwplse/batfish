package batfish.representation.juniper;

import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class GenerateRoute implements RepresentationObject {
   private String _prefix;
   private int _prefixLength;
   private String _policy;
   private int _preference;

   public GenerateRoute(String prefix, int prefixLength, String policy,
         int distance) {
      _prefix = prefix;
      _prefixLength = prefixLength;
      _policy = policy;
      _preference = distance;
   }

   public void setPolicy(String p) {
      _policy = p;
   }

   public String getPrefix() {
      return _prefix;
   }

   public int getPrefixLength() {
      return _prefixLength;
   }

   public String getPolicy() {
      return _policy;
   }

   public int getPreference() {
      return _preference;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      GenerateRoute rhs = (GenerateRoute) o;
      return Util.equalOrNull(_prefix, rhs._prefix)
            && _prefixLength == rhs._prefixLength
            && Util.equalOrNull(_policy, rhs._policy)
            && _preference == rhs._preference;
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string);
         System.out.println("+ " + string + "._prefix:"
               + Util.objectToString(_prefix));
         System.out.println("+ " + string + "._prefixLength:" + _prefixLength);
         System.out.println("+ " + string + "._policy:"
               + Util.objectToString(_policy));
         System.out.println("+ " + string + "._preference:" + _preference);
         return;
      }

      if (o == null) {
         System.out.println("- " + string);
         System.out.println("- " + string + "._prefix:"
               + Util.objectToString(_prefix));
         System.out.println("- " + string + "._prefixLength:" + _prefixLength);
         System.out.println("- " + string + "._policy:"
               + Util.objectToString(_policy));
         System.out.println("- " + string + "._preference:" + _preference);
         return;
      }

      GenerateRoute rhs = (GenerateRoute) o;
      if (!Util.equalOrNull(_prefix, rhs._prefix)) {
         System.out.println("- " + string + "._prefix:"
               + Util.objectToString(_prefix));
         System.out.println("+ " + string + "._prefix:"
               + Util.objectToString(rhs._prefix));
      }
      if (_prefixLength != rhs._prefixLength) {
         System.out.println("- " + string + "._prefixLength:" + _prefixLength);
         System.out.println("+ " + string + "._prefixLength:"
               + rhs._prefixLength);
      }
      if (!Util.equalOrNull(_policy, rhs._policy)) {
         System.out.println("- " + string + "._policy:"
               + Util.objectToString(_policy));
         System.out.println("+ " + string + "._policy:"
               + Util.objectToString(rhs._policy));
      }
      if (_preference != rhs._preference) {
         System.out.println("- " + string + "._preference:" + _preference);
         System.out.println("+ " + string + "._preference:" + rhs._preference);
      }
      System.out.flush();
      return;

   }
}
