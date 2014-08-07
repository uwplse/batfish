package batfish.representation.cisco;

import java.io.Serializable;

import batfish.representation.Ip;
import batfish.representation.LineAction;
import batfish.representation.RepresentationObject;
import batfish.util.SubRange;
import batfish.util.Util;

public class PrefixListLine implements Serializable, RepresentationObject {

   private static final long serialVersionUID = 1L;

   private LineAction _action;

   private SubRange _lengthRange;

   private Ip _prefix;

   private int _prefixLength;

   public PrefixListLine(LineAction action, Ip prefix, int prefixLength,
         SubRange lengthRange) {
      _action = action;
      _prefix = prefix;
      _prefixLength = prefixLength;
      _lengthRange = lengthRange;
   }

   public LineAction getAction() {
      return _action;
   }

   public SubRange getLengthRange() {
      return _lengthRange;
   }

   public Ip getPrefix() {
      return _prefix;
   }

   public int getPrefixLength() {
      return _prefixLength;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      PrefixListLine rhs = (PrefixListLine) o;
      return Util.equalOrNull(_action, rhs._action)
            && Util.equalOrNull(_lengthRange, rhs._lengthRange)
            && Util.equalOrNull(_prefix, rhs._prefix)
            && Util.equalOrNull(_prefixLength, rhs._prefixLength);
   }

}
