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

}
