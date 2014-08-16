package batfish.representation.cisco;

import java.io.Serializable;

import batfish.representation.Ip;
import batfish.representation.LineAction;
import batfish.representation.RepresentationObject;
import batfish.util.Util;

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

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string);
         System.out.println("+ " + string + "._action:"
               + Util.objectToString(_action));
         System.out.println("+ " + string + "._ip:" + Util.objectToString(_ip));
         System.out.println("+ " + string + "._wildcard:"
               + Util.objectToString(_wildcard));
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string);
         System.out.println("- " + string + "._action:"
               + Util.objectToString(_action));
         System.out.println("- " + string + "._ip:" + Util.objectToString(_ip));
         System.out.println("- " + string + "._wildcard:"
               + Util.objectToString(_wildcard));
         System.out.flush();
         return;
      }

      StandardAccessListLine rhs = (StandardAccessListLine) o;
      if (!Util.equalOrNull(_action, rhs._action)) {
         System.out.println("- " + string + "._action:"
               + Util.objectToString(_action));
         System.out.println("+ " + string + "._action:"
               + Util.objectToString(rhs._action));
      }
      if (!Util.equalOrNull(_ip, rhs._ip)) {
         System.out.println("- " + string + "._ip:" + Util.objectToString(_ip));
         System.out.println("+ " + string + "._ip:"
               + Util.objectToString(rhs._ip));
      }
      if (!Util.equalOrNull(_wildcard, rhs._wildcard)) {
         System.out.println("- " + string + "._wildcard:"
               + Util.objectToString(_wildcard));
         System.out.println("+ " + string + "._wildcard:"
               + Util.objectToString(rhs._wildcard));
      }

      System.out.flush();
      return;
   }
}
