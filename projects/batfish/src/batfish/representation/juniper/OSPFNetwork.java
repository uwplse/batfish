package batfish.representation.juniper;

import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class OSPFNetwork implements RepresentationObject {

   private String _networkAddress;
   private String _subnetMask;
   private String _interface;
   private int _area;

   public OSPFNetwork(String networkAddress, String subnetMask, int area) {
      _networkAddress = networkAddress;
      _subnetMask = subnetMask;
      _area = area;
   }

   public OSPFNetwork(String inf, int area) {
      _interface = inf;
      _area = area;
   }

   public String getNetworkAddress() {
      return _networkAddress;
   }

   public String getSubnetMask() {
      return _subnetMask;
   }

   public int getArea() {
      return _area;
   }

   public String getInterface() {
      return _interface;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      OSPFNetwork rhs = (OSPFNetwork) o;
      return Util.equalOrNull(_networkAddress, rhs._networkAddress)
            && Util.equalOrNull(_subnetMask, rhs._subnetMask)
            && _area == rhs._area
            && Util.equalOrNull(_interface, rhs._interface);
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string);
         System.out.println("+ " + string + "._networkAddress:"
               + Util.objectToString(_networkAddress));
         System.out.println("+ " + string + "._subnetMask:"
               + Util.objectToString(_subnetMask));
         System.out.println("+ " + string + "._interface:"
               + Util.objectToString(_interface));
         System.out.println("+ " + string + "._area:" + _area);
         return;
      }

      if (o == null) {
         System.out.println("- " + string);
         System.out.println("- " + string + "._networkAddress:"
               + Util.objectToString(_networkAddress));
         System.out.println("- " + string + "._subnetMask:"
               + Util.objectToString(_subnetMask));
         System.out.println("- " + string + "._interface:"
               + Util.objectToString(_interface));
         System.out.println("- " + string + "._area:" + _area);
         return;
      }

      OSPFNetwork rhs = (OSPFNetwork) o;
      if (!Util.equalOrNull(_networkAddress, rhs._networkAddress)) {
         System.out.println("- " + string + "._networkAddress:"
               + Util.objectToString(_networkAddress));
         System.out.println("+ " + string + "._networkAddress:"
               + Util.objectToString(rhs._networkAddress));
      }
      if (!Util.equalOrNull(_subnetMask, rhs._subnetMask)) {
         System.out.println("- " + string + "._subnetMask:"
               + Util.objectToString(_subnetMask));
         System.out.println("+ " + string + "._subnetMask:"
               + Util.objectToString(rhs._subnetMask));
      }
      if (!Util.equalOrNull(_interface, rhs._interface)) {
         System.out.println("- " + string + "._interface:"
               + Util.objectToString(_interface));
         System.out.println("+ " + string + "._interface:"
               + Util.objectToString(rhs._interface));
      }
      if (_area != rhs._area) {
         System.out.println("- " + string + "._area:" + _area);
         System.out.println("+ " + string + "._area:" + rhs._area);
      }
      System.out.flush();
      return;
   }

}
