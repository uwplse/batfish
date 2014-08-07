package batfish.representation.juniper;

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

}
