package batfish.util;

import java.io.Serializable;

import batfish.representation.RepresentationObject;

public class SubRange implements Serializable, RepresentationObject {

   private static final long serialVersionUID = 1L;

   private int _end;
   private int _start;

   public SubRange(int start, int end) {
      _start = start;
      _end = end;
   }

   public int getEnd() {
      return _end;
   }

   public int getStart() {
      return _start;
   }

   @Override
   public String toString() {
      return "[" + _start + "," + _end + "]";
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      SubRange rhs = (SubRange) o;
      return (_start == rhs.getStart() && _end == rhs.getEnd());
   }
}
