package batfish.representation.cisco;

import java.io.Serializable;

import batfish.representation.Ip;
import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class BgpNetwork implements Serializable, RepresentationObject {

   private static final long serialVersionUID = 1L;
   private Ip _networkAddress;
   private Ip _subnetMask;

   public BgpNetwork(Ip network, Ip subnet) {
      _networkAddress = network;
      _subnetMask = subnet;
   }

   @Override
   public boolean equals(Object o) {
      BgpNetwork rhs = (BgpNetwork) o;
      return _networkAddress.equals(rhs.getNetworkAddress())
            && _subnetMask.equals(rhs.getSubnetMask());
   }

   @Override
   public int hashCode() {
      return _networkAddress.hashCode() | _subnetMask.hashCode();
   }

   public Ip getNetworkAddress() {
      return _networkAddress;
   }

   public Ip getSubnetMask() {
      return _subnetMask;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string);
         System.out.println("+ " + string + "._networkAddress:"
               + Util.objectToString(_networkAddress));
         System.out.println("+ " + string + "._subnetMask:"
               + Util.objectToString(_subnetMask));
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string);
         System.out.println("- " + string + "._networkAddress:"
               + Util.objectToString(_networkAddress));
         System.out.println("- " + string + "._subnetMask:"
               + Util.objectToString(_subnetMask));
         System.out.flush();
         return;
      }

      BgpNetwork rhs = (BgpNetwork) o;
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

      System.out.flush();
      return;
   }

}
