package batfish.representation.cisco;

import java.io.Serializable;

import batfish.representation.Ip;
import batfish.representation.LineAction;
import batfish.representation.RepresentationObject;

public class StandardAccessListLine implements Serializable,
      RepresentationObject {

   private static final long serialVersionUID = 1L;

   private LineAction _action;
   private Ip _ip;
   private Ip _wildcard;

   public StandardAccessListLine(LineAction action, Ip ip, Ip wildcard) {
      _action = action;
      _ip = ip;
      _wildcard = wildcard;
   }

   public LineAction getAction() {
      return _action;
   }

   public Ip getIP() {
      return _ip;
   }

   public Ip getWildcard() {
      return _wildcard;
   }

   public ExtendedAccessListLine toExtendedAccessListLine() {
      return new ExtendedAccessListLine(_action, 0, _ip, _wildcard, new Ip(0l),
            new Ip(0xFFFFFFFFl), null, null);
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      StandardAccessListLine rhs = (StandardAccessListLine) o;
      if (_action == null && rhs.getAction() != null)
         return false;
      if (_ip == null && rhs.getIP() != null)
         return false;
      if (_wildcard == null && rhs.getWildcard() != null)
         return false;
      return (_action.equals(rhs.getAction()) && _ip.equals(rhs.getIP()) && _wildcard
            .equals(rhs.getWildcard()));

   }
}
