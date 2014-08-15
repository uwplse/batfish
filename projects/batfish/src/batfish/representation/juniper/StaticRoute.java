package batfish.representation.juniper;

import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class StaticRoute implements RepresentationObject {
   private int _distance;
   private String _mask;
   private String _nextHopInterface;
   private String _nextHopIp;
   private String _prefix;
   private int _tag;

   public StaticRoute(String prefix, String mask, String nextHopIp,
         String nextHopInterface, int distance, int tag) {
      _prefix = prefix;
      _mask = mask;
      _nextHopIp = nextHopIp;
      _nextHopInterface = nextHopInterface;
      _distance = distance;
      _tag = tag;
   }

   public int getDistance() {
      return _distance;
   }

   public String getMask() {
      return _mask;
   }

   public String getNextHopInterface() {
      return _nextHopInterface;
   }

   public String getNextHopIp() {
      return _nextHopIp;
   }

   public String getPrefix() {
      return _prefix;
   }

   public int getTag() {
      return _tag;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      StaticRoute rhs = (StaticRoute) o;
      return _distance == rhs._distance && Util.equalOrNull(_mask, rhs._mask)
            && Util.equalOrNull(_nextHopInterface, rhs._nextHopInterface)
            && Util.equalOrNull(_nextHopIp, rhs._nextHopIp)
            && Util.equalOrNull(_prefix, rhs._prefix) && _tag == rhs._tag;
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string + "\n");
         System.out.println("+ " + string + "._distance:" + _distance + "\n");
         System.out.println("+ " + string + "._mask:"
               + Util.objectToString(_mask) + "\n");
         System.out.println("+ " + string + "._nextHopInterface:"
               + Util.objectToString(_nextHopInterface) + "\n");
         System.out.println("+ " + string + "._nextHopIp:"
               + Util.objectToString(_nextHopIp) + "\n");
         System.out.println("+ " + string + "._prefix:"
               + Util.objectToString(_prefix) + "\n");
         System.out.println("+ " + string + "._tag:" + _tag + "\n");
         return;
      }

      if (o == null) {
         System.out.println("- " + string + "\n");
         System.out.println("- " + string + "._distance:" + _distance + "\n");
         System.out.println("- " + string + "._mask:"
               + Util.objectToString(_mask) + "\n");
         System.out.println("- " + string + "._nextHopInterface:"
               + Util.objectToString(_nextHopInterface) + "\n");
         System.out.println("- " + string + "._nextHopIp:"
               + Util.objectToString(_nextHopIp) + "\n");
         System.out.println("- " + string + "._prefix:"
               + Util.objectToString(_prefix) + "\n");
         System.out.println("- " + string + "._tag:" + _tag + "\n");
         return;
      }

      StaticRoute rhs = (StaticRoute) o;
      if (_distance != rhs._distance) {
         System.out.println("- " + string + "._distance:" + _distance + "\n");
         System.out.println("+ " + string + "._distance:" + rhs._distance
               + "\n");
      }
      if (!Util.equalOrNull(_mask, rhs._mask)) {
         System.out.println("- " + string + "._mask:"
               + Util.objectToString(_mask) + "\n");
         System.out.println("+ " + string + "._mask:"
               + Util.objectToString(rhs._mask) + "\n");
      }
      if (!Util.equalOrNull(_nextHopInterface, rhs._nextHopInterface)) {
         System.out.println("- " + string + "._nextHopInterface:"
               + Util.objectToString(_nextHopInterface) + "\n");
         System.out.println("+ " + string + "._nextHopInterface:"
               + Util.objectToString(rhs._nextHopInterface) + "\n");
      }
      if (!Util.equalOrNull(_nextHopIp, rhs._nextHopIp)) {
         System.out.println("- " + string + "._nextHopIp:"
               + Util.objectToString(_nextHopIp) + "\n");
         System.out.println("+ " + string + "._nextHopIp:"
               + Util.objectToString(rhs._nextHopIp) + "\n");
      }
      if (!Util.equalOrNull(_prefix, rhs._prefix)) {
         System.out.println("- " + string + "._prefix:"
               + Util.objectToString(_prefix) + "\n");
         System.out.println("+ " + string + "._prefix:"
               + Util.objectToString(rhs._prefix) + "\n");
      }
      if (_tag != rhs._tag) {
         System.out.println("- " + string + "._tag:" + _tag + "\n");
         System.out.println("+ " + string + "._tag:" + rhs._tag + "\n");
      }
      System.out.flush();
      return;

   }

}
