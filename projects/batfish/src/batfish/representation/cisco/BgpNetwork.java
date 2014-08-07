package batfish.representation.cisco;

import java.io.Serializable;

import batfish.representation.Ip;
import batfish.representation.RepresentationObject;

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
      return equals(o);
   }

}
