package batfish.representation.juniper;

import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class RouteFilterThroughLine extends RouteFilterLine {
   // IP Prefix
   private String _prefix;

   // Range for prefix-length to be matched
   private int _prefixLength;

   // Another IP Prefix if the match type includes a range of prefix
   private String _secondPrefix;

   private int _secondPrefixLength;

   public RouteFilterThroughLine(String prefix, int prefixLength,
         String secondPrefix, int secondPrefixLength) {
      _prefix = prefix;
      _prefixLength = prefixLength;
      _secondPrefix = secondPrefix;
      _secondPrefixLength = secondPrefixLength;
   }

   public String getPrefix() {
      return _prefix;
   }

   public int getPrefixLength() {
      return _prefixLength;
   }

   public String getSecondPrefix() {
      return _secondPrefix;
   }

   public int getSecondPrefixLength() {
      return _secondPrefixLength;
   }

   @Override
   public RouteFilterLineType getType() {
      return RouteFilterLineType.THROUGH;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      if (((RouteFilterLine) o).getType() != RouteFilterLineType.THROUGH) {
         return false;
      }

      RouteFilterThroughLine rhsLine = (RouteFilterThroughLine) o;
      return Util.equalOrNull(_prefix, rhsLine._prefix)
            && _prefixLength == rhsLine._prefixLength
            && Util.equalOrNull(_secondPrefix, rhsLine._secondPrefix)
            && _secondPrefixLength == rhsLine._secondPrefixLength;

   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string);
         System.out.println("+ " + string + "._prefix:"
               + Util.objectToString(_prefix));
         System.out.println("+ " + string + "._prefixLength:" + _prefixLength);
         System.out.println("+ " + string + "._secondPrefix:"
               + Util.objectToString(_secondPrefix));
         System.out.println("+ " + string + "._secondPrefixLength:"
               + _secondPrefixLength);
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string);
         System.out.println("- " + string + "._prefix:"
               + Util.objectToString(_prefix));
         System.out.println("- " + string + "._prefixLength:" + _prefixLength);
         System.out.println("- " + string + "._secondPrefix:"
               + Util.objectToString(_secondPrefix));
         System.out.println("- " + string + "._secondPrefixLength:"
               + _secondPrefixLength);
         System.out.flush();
         return;
      }

      if (((RouteFilterLine) o).getType() != RouteFilterLineType.THROUGH) {
         ((RepresentationObject) o).diffRepresentation(null, string, true);
         diffRepresentation(null, string, false);
         return;
      }

      RouteFilterThroughLine rhs = (RouteFilterThroughLine) o;
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
      if (!Util.equalOrNull(_secondPrefix, rhs._secondPrefix)) {
         System.out.println("- " + string + "._secondPrefix:"
               + Util.objectToString(_secondPrefix));
         System.out.println("+ " + string + "._secondPrefix:"
               + Util.objectToString(rhs._secondPrefix));
      }
      if (_secondPrefixLength != rhs._secondPrefixLength) {
         System.out.println("- " + string + "._secondPrefixLength:"
               + _secondPrefixLength);
         System.out.println("+ " + string + "._secondPrefixLength:"
               + rhs._secondPrefixLength);
      }
      System.out.flush();
      return;
   }

}
