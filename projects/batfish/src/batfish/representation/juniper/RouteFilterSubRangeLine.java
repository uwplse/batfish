package batfish.representation.juniper;

import batfish.representation.RepresentationObject;
import batfish.util.SubRange;
import batfish.util.Util;

public class RouteFilterSubRangeLine extends RouteFilterLine {
   // IP Prefix
   private String _prefix;

   private int _prefixLength;

   private SubRange _lengthRange;

   public RouteFilterSubRangeLine(String prefix, int prefixLength,
         SubRange lengthRange) {
      _prefix = prefix;
      _prefixLength = prefixLength;
      _lengthRange = lengthRange;
   }

   public String getPrefix() {
      return _prefix;
   }

   public int getPrefixLength() {
      return _prefixLength;
   }

   public SubRange getLengthRange() {
      return _lengthRange;
   }

   @Override
   public RouteFilterLineType getType() {
      return RouteFilterLineType.SUBRANGE;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      if (((RouteFilterLine) o).getType() != RouteFilterLineType.SUBRANGE) {
         return false;
      }

      RouteFilterSubRangeLine rhsLine = (RouteFilterSubRangeLine) o;
      return Util.equalOrNull(_prefix, rhsLine._prefix)
            && _prefixLength == rhsLine._prefixLength
            && Util.equalOrNull(_lengthRange, rhsLine._lengthRange);
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string);
         System.out.println("+ " + string + "._prefix:"
               + Util.objectToString(_prefix));
         System.out.println("+ " + string + "._prefixLength:" + _prefixLength);
         System.out.println("+ " + string + "._lengthRange:"
               + Util.objectToString(_lengthRange));
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string);
         System.out.println("- " + string + "._prefix:"
               + Util.objectToString(_prefix));
         System.out.println("- " + string + "._prefixLength:" + _prefixLength);
         System.out.println("- " + string + "._lengthRange:"
               + Util.objectToString(_lengthRange));
         System.out.flush();
         return;
      }

      if (((RouteFilterLine) o).getType() != RouteFilterLineType.SUBRANGE) {
         ((RepresentationObject) o).diffRepresentation(null, string, true);
         diffRepresentation(null, string, false);
         return;
      }

      RouteFilterSubRangeLine rhs = (RouteFilterSubRangeLine) o;
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
      if (!Util.equalOrNull(_lengthRange, rhs._lengthRange)) {
         System.out.println("- " + string + "._lengthRange:"
               + Util.objectToString(_lengthRange));
         System.out.println("+ " + string + "._lengthRange:"
               + Util.objectToString(rhs._lengthRange));
      }
      System.out.flush();
      return;
   }

}
