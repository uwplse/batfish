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

}
