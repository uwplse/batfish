package batfish.representation.cisco;

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

}
