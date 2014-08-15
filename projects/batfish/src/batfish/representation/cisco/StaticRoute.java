package batfish.representation.cisco;

import java.io.Serializable;

import batfish.representation.Ip;
import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class StaticRoute implements Serializable, RepresentationObject {

   private static final long serialVersionUID = 1L;

   private int _distance;
   private Ip _mask;
   private String _nextHopInterface;
   private Ip _nextHopIp;
   private boolean _permanent;
   private Ip _prefix;
   private Integer _tag;
   private Integer _track;

   public StaticRoute(Ip prefix, Ip mask, Ip nextHopIp,
         String nextHopInterface, int distance, Integer tag, Integer track,
         boolean permanent) {
      _prefix = prefix;
      _mask = mask;
      _nextHopIp = nextHopIp;
      _nextHopInterface = nextHopInterface;
      _distance = distance;
      _tag = tag;
      _track = track;
      _permanent = permanent;
   }

   public int getDistance() {
      return _distance;
   }

   public Ip getMask() {
      return _mask;
   }

   public String getNextHopInterface() {
      return _nextHopInterface;
   }

   public Ip getNextHopIp() {
      return _nextHopIp;
   }

   public boolean getPermanent() {
      return _permanent;
   }

   public Ip getPrefix() {
      return _prefix;
   }

   public Integer getTag() {
      return _tag;
   }

   public Integer getTrack() {
      return _track;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      // TODO Auto-generated method stub
      return false;
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
         System.out.println("+ " + string + "._permanent:" + _permanent + "\n");
         System.out.println("+ " + string + "._prefix:"
               + Util.objectToString(_prefix) + "\n");
         System.out.println("+ " + string + "._tag:"
               + Util.objectToString(_tag) + "\n");
         System.out.println("+ " + string + "._track:"
               + Util.objectToString(_track) + "\n");
         System.out.flush();
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
         System.out.println("- " + string + "._permanent:" + _permanent + "\n");
         System.out.println("- " + string + "._prefix:"
               + Util.objectToString(_prefix) + "\n");
         System.out.println("- " + string + "._tag:"
               + Util.objectToString(_tag) + "\n");
         System.out.println("- " + string + "._track:"
               + Util.objectToString(_track) + "\n");
         System.out.flush();
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
      if (_permanent != rhs._permanent) {
         System.out.println("- " + string + "._permanent:" + _permanent + "\n");
         System.out.println("+ " + string + "._permanent:" + rhs._permanent
               + "\n");
      }
      if (!Util.equalOrNull(_prefix, rhs._prefix)) {
         System.out.println("- " + string + "._prefix:"
               + Util.objectToString(_prefix) + "\n");
         System.out.println("+ " + string + "._prefix:"
               + Util.objectToString(rhs._prefix) + "\n");
      }
      if (!Util.equalOrNull(_tag, rhs._tag)) {
         System.out.println("- " + string + "._tag:"
               + Util.objectToString(_tag) + "\n");
         System.out.println("+ " + string + "._tag:"
               + Util.objectToString(rhs._tag) + "\n");
      }
      if (!Util.equalOrNull(_track, rhs._track)) {
         System.out.println("- " + string + "._track:"
               + Util.objectToString(_track) + "\n");
         System.out.println("+ " + string + "._track:"
               + Util.objectToString(rhs._track) + "\n");
      }
      System.out.flush();
      return;
   }

}
