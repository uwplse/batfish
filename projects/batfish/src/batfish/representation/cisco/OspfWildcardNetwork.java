package batfish.representation.cisco;

import java.io.Serializable;

import batfish.representation.Ip;
import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class OspfWildcardNetwork implements Comparable<OspfWildcardNetwork>,
      Serializable, RepresentationObject {

   private static final long serialVersionUID = 1L;

   private long _area;
   private int _hashCode;
   private Ip _prefix;
   private Ip _wildcard;

   public OspfWildcardNetwork(Ip prefix, Ip wildcard, long area) {
      _prefix = prefix;
      _wildcard = wildcard;
      _area = area;
      _hashCode = (prefix.networkString(_wildcard) + ":" + _area).hashCode();
   }

   @Override
   public int compareTo(OspfWildcardNetwork rhs) {
      int ret = _prefix.compareTo(rhs._prefix);
      if (ret == 0) {
         ret = _wildcard.compareTo(rhs._wildcard);
         if (ret == 0) {
            ret = Long.compare(_area, rhs._area);
         }
      }
      return ret;
   }

   @Override
   public boolean equals(Object o) {
      OspfWildcardNetwork rhs = (OspfWildcardNetwork) o;
      return _prefix.equals(rhs._prefix) && _wildcard.equals(rhs._wildcard)
            && _area == rhs._area;
   }

   public long getArea() {
      return _area;
   }

   public Ip getNetworkAddress() {
      return _prefix;
   }

   public Ip getWildcard() {
      return _wildcard;
   }

   @Override
   public int hashCode() {
      return _hashCode;
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
         System.out.println("+ " + string + "._area:" + _area + "\n");
         System.out.println("+ " + string + "._prefix:" + Util.objectToString(_prefix) + "\n");
         System.out.println("+ " + string + "._wildcard:" + Util.objectToString(_wildcard) + "\n");
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string + "\n");
         System.out.println("- " + string + "._area:" + _area + "\n");
         System.out.println("- " + string + "._prefix:" + Util.objectToString(_prefix) + "\n");
         System.out.println("- " + string + "._wildcard:" + Util.objectToString(_wildcard) + "\n");
         System.out.flush();
         return;
      }

      OspfWildcardNetwork rhs = (OspfWildcardNetwork) o;
      if (_area != rhs._area) {
         System.out.println("- " + string + "._area:" + _area + "\n");
         System.out.println("+ " + string + "._area:" + rhs._area
               + "\n");
      }
      if (!Util.equalOrNull(_prefix, rhs._prefix)) {
         System.out.println("- " + string + "._prefix:" + Util.objectToString(_prefix) + "\n");
         System.out.println("+ " + string + "._prefix:" + Util.objectToString(rhs._prefix) + "\n");
      }
      if (!Util.equalOrNull(_wildcard, rhs._wildcard)) {
         System.out.println("- " + string + "._wildcard:" + Util.objectToString(_wildcard) + "\n");
         System.out.println("+ " + string + "._wildcard:" + Util.objectToString(rhs._wildcard) + "\n");
      }
      System.out.flush();
      return;
   }

}
