package batfish.representation.cisco;

import java.util.Set;

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

}
