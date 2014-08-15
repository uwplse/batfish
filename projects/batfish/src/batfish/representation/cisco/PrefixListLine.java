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

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string + "\n");
         System.out.println("+ " + string + "._action:"
               + Util.objectToString(_action) + "\n");
         System.out.println("+ " + string + "._lengthRange:"
               + Util.objectToString(_lengthRange) + "\n");
         System.out.println("+ " + string + "._prefix:"
               + Util.objectToString(_prefix) + "\n");
         System.out.println("+ " + string + "._prefixLength:" + _prefixLength + "\n");
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string + "\n");
         System.out.println("- " + string + "._action:"
               + Util.objectToString(_action) + "\n");
         System.out.println("- " + string + "._lengthRange:"
               + Util.objectToString(_lengthRange) + "\n");
         System.out.println("- " + string + "._prefix:"
               + Util.objectToString(_prefix) + "\n");
         System.out.println("- " + string + "._prefixLength:" + _prefixLength + "\n");
         System.out.flush();
         return;
      }

      PrefixListLine rhs = (PrefixListLine) o;
      if (!Util.equalOrNull(_action, rhs._action)) {
         System.out.println("- " + string + "._action:"
               + Util.objectToString(_action) + "\n");
         System.out.println("+ " + string + "._action:"
               + Util.objectToString(rhs._action) + "\n");
      }
      if (!Util.equalOrNull(_lengthRange, rhs._lengthRange)) {
         System.out.println("- " + string + "._lengthRange:"
               + Util.objectToString(_lengthRange) + "\n");
         System.out.println("+ " + string + "._lengthRange:"
               + Util.objectToString(rhs._lengthRange) + "\n");
      }
      if (!Util.equalOrNull(_prefix, rhs._prefix)) {
         System.out.println("- " + string + "._prefix:"
               + Util.objectToString(_prefix) + "\n");
         System.out.println("+ " + string + "._prefix:"
               + Util.objectToString(rhs._prefix) + "\n");
      }
      if (_prefixLength != rhs._prefixLength) {
         System.out.println("- " + string + "._prefixLength:" + _prefixLength + "\n");
         System.out.println("+ " + string + "._prefixLength:" + rhs._prefixLength
               + "\n");
      }
      System.out.flush();
      return;
   }

}
