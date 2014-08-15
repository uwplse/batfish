package batfish.representation.cisco;

import java.util.List;

import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class RouteMapMatchProtocolLine extends RouteMapMatchLine {

   private static final long serialVersionUID = 1L;

   private List<String> _protocol;

   public RouteMapMatchProtocolLine(List<String> protocol) {
      _protocol = protocol;
   }

   public List<String> getProtocl() {
      return _protocol;
   }

   @Override
   public RouteMapMatchType getType() {
      return RouteMapMatchType.PROTOCOL;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      if (((RouteMapMatchLine) o).getType() != RouteMapMatchType.PROTOCOL) {
         return false;
      }

      RouteMapMatchProtocolLine rhsLine = (RouteMapMatchProtocolLine) o;
      return getProtocl().equals(rhsLine.getProtocl());
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string + "\n");
         Util.diffRepresentationLists(null, _protocol, string + "._protocol");
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string + "\n");
         Util.diffRepresentationLists(_protocol, null, string + "._protocol");
         System.out.flush();
         return;
      }

      if (((RouteMapMatchLine) o).getType() != RouteMapMatchType.PROTOCOL) {
         ((RepresentationObject) o).diffRepresentation(null, string, true);
         diffRepresentation(null, string, false);
         return;
      }

      RouteMapMatchProtocolLine rhs = (RouteMapMatchProtocolLine) o;

      Util.diffRepresentationLists(_protocol, rhs._protocol, string
            + "._protocol");

      System.out.flush();
      return;

   }
}
