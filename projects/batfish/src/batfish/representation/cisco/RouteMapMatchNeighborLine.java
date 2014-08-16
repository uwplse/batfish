package batfish.representation.cisco;

import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class RouteMapMatchNeighborLine extends RouteMapMatchLine {

   private static final long serialVersionUID = 1L;

   private String _neighborIp;

   public RouteMapMatchNeighborLine(String neighborIP) {
      _neighborIp = neighborIP;
   }

   public String getNeighborIp() {
      return _neighborIp;
   }

   @Override
   public RouteMapMatchType getType() {
      return RouteMapMatchType.NEIGHBOR;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      if (((RouteMapMatchLine) o).getType() != RouteMapMatchType.NEIGHBOR) {
         return false;
      }

      RouteMapMatchNeighborLine rhsLine = (RouteMapMatchNeighborLine) o;
      return getNeighborIp().equals(rhsLine.getNeighborIp());
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string);
         System.out.println("+ " + string + "._neighborIp:"
               + Util.objectToString(_neighborIp));
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string);
         System.out.println("- " + string + "._neighborIp:"
               + Util.objectToString(_neighborIp));
         System.out.flush();
         return;
      }

      if (((RouteMapMatchLine) o).getType() != RouteMapMatchType.NEIGHBOR) {
         ((RepresentationObject) o).diffRepresentation(null, string, true);
         diffRepresentation(null, string, false);
         return;
      }

      RouteMapMatchNeighborLine rhs = (RouteMapMatchNeighborLine) o;

      if (!Util.equalOrNull(_neighborIp, rhs._neighborIp)) {
         System.out.println("- " + string + "._neighborIp:"
               + Util.objectToString(_neighborIp));
         System.out.println("+ " + string + "._neighborIp:"
               + Util.objectToString(rhs._neighborIp));
      }

      System.out.flush();
      return;

   }

}
