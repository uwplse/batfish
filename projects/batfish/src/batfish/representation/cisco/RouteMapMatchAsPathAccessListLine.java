package batfish.representation.cisco;

import java.util.Set;

import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class RouteMapMatchAsPathAccessListLine extends RouteMapMatchLine {

   private static final long serialVersionUID = 1L;

   private Set<String> _listNames;

   public RouteMapMatchAsPathAccessListLine(Set<String> names) {
      _listNames = names;
   }

   public Set<String> getListNames() {
      return _listNames;
   }

   @Override
   public RouteMapMatchType getType() {
      return RouteMapMatchType.AS_PATH_ACCESS_LIST;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      if (((RouteMapMatchLine) o).getType() != RouteMapMatchType.AS_PATH_ACCESS_LIST) {
         return false;
      }

      RouteMapMatchAsPathAccessListLine rhAccessListLine = (RouteMapMatchAsPathAccessListLine) o;
      return getListNames().equals(rhAccessListLine.getListNames());
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string + "\n");
         Util.diffRepresentationSets(null, _listNames, string + "._listNames");
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string + "\n");
         Util.diffRepresentationSets(_listNames, null, string + "._listNames");
         System.out.flush();
         return;
      }

      if (((RouteMapMatchLine) o).getType() != RouteMapMatchType.AS_PATH_ACCESS_LIST) {
         ((RepresentationObject) o).diffRepresentation(null, string, true);
         diffRepresentation(null, string, false);
         return;
      }

      RouteMapMatchAsPathAccessListLine rhs = (RouteMapMatchAsPathAccessListLine) o;
      Util.diffRepresentationSets(_listNames, rhs._listNames, string
            + "._listNames");

      System.out.flush();
      return;
   }

}
