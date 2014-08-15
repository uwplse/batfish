package batfish.representation.cisco;

import java.util.Set;

import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class RouteMapMatchCommunityListLine extends RouteMapMatchLine {

   private static final long serialVersionUID = 1L;

   private Set<String> _listNames;

   public RouteMapMatchCommunityListLine(Set<String> names) {
      _listNames = names;
   }

   public Set<String> getListNames() {
      return _listNames;
   }

   @Override
   public RouteMapMatchType getType() {
      return RouteMapMatchType.COMMUNITY_LIST;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      if (((RouteMapMatchLine) o).getType() != RouteMapMatchType.COMMUNITY_LIST) {
         return false;
      }

      RouteMapMatchCommunityListLine rhsLine = (RouteMapMatchCommunityListLine) o;
      return getListNames().equals(rhsLine.getListNames());
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

      if (((RouteMapMatchLine) o).getType() != RouteMapMatchType.COMMUNITY_LIST) {
         ((RepresentationObject) o).diffRepresentation(null, string, true);
         diffRepresentation(null, string, false);
         return;
      }

      RouteMapMatchCommunityListLine rhs = (RouteMapMatchCommunityListLine) o;
      Util.diffRepresentationSets(_listNames, rhs._listNames, string
            + "._listNames");

      System.out.flush();
      return;

   }

}
