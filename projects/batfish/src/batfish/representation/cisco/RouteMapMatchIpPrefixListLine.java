package batfish.representation.cisco;

import java.util.Set;

import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class RouteMapMatchIpPrefixListLine extends RouteMapMatchLine {

   private static final long serialVersionUID = 1L;

   private Set<String> _listNames;

   public RouteMapMatchIpPrefixListLine(Set<String> names) {
      _listNames = names;
   }

   public Set<String> getListNames() {
      return _listNames;
   }

   @Override
   public RouteMapMatchType getType() {
      return RouteMapMatchType.IP_PREFIX_LIST;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      if (((RouteMapMatchLine) o).getType() != RouteMapMatchType.IP_PREFIX_LIST) {
         return false;
      }

      RouteMapMatchIpPrefixListLine rhsLine = (RouteMapMatchIpPrefixListLine) o;
      return getListNames().equals(rhsLine.getListNames());
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string);
         Util.diffRepresentationSets(null, _listNames, string + "._listNames");
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string);
         Util.diffRepresentationSets(_listNames, null, string + "._listNames");
         System.out.flush();
         return;
      }

      if (((RouteMapMatchLine) o).getType() != RouteMapMatchType.IP_PREFIX_LIST) {
         ((RepresentationObject) o).diffRepresentation(null, string, true);
         diffRepresentation(null, string, false);
         return;
      }

      RouteMapMatchIpPrefixListLine rhs = (RouteMapMatchIpPrefixListLine) o;
      Util.diffRepresentationSets(_listNames, rhs._listNames, string
            + "._listNames");

      System.out.flush();
      return;
   }

}
