package batfish.representation.cisco;

import java.util.Set;

import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class RouteMapMatchTagLine extends RouteMapMatchLine {

   private static final long serialVersionUID = 1L;

   private Set<Integer> _tags;

   public RouteMapMatchTagLine(Set<Integer> tags) {
      _tags = tags;
   }

   public Set<Integer> getTags() {
      return _tags;
   }

   @Override
   public RouteMapMatchType getType() {
      return RouteMapMatchType.TAG;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      if (((RouteMapMatchLine) o).getType() != RouteMapMatchType.TAG) {
         return false;
      }

      RouteMapMatchTagLine rhsLine = (RouteMapMatchTagLine) o;
      return getTags().equals(rhsLine.getTags());
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string);
         Util.diffRepresentationSets(null, _tags, string + "._tags");
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string);
         Util.diffRepresentationSets(_tags, null, string + "._tags");
         System.out.flush();
         return;
      }

      if (((RouteMapMatchLine) o).getType() != RouteMapMatchType.TAG) {
         ((RepresentationObject) o).diffRepresentation(null, string, true);
         diffRepresentation(null, string, false);
         return;
      }

      RouteMapMatchTagLine rhs = (RouteMapMatchTagLine) o;

      Util.diffRepresentationSets(_tags, rhs._tags, string + "._tags");

      System.out.flush();
      return;

   }
}
