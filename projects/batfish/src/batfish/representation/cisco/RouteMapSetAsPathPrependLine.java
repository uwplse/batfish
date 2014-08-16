package batfish.representation.cisco;

import java.util.List;

import batfish.representation.Configuration;
import batfish.representation.PolicyMapSetAsPathPrependLine;
import batfish.representation.PolicyMapSetLine;
import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class RouteMapSetAsPathPrependLine extends RouteMapSetLine {

   private static final long serialVersionUID = 1L;

   private List<Integer> _asList;

   public RouteMapSetAsPathPrependLine(List<Integer> asList) {
      _asList = asList;
   }

   public List<Integer> getAsList() {
      return _asList;
   }

   @Override
   public PolicyMapSetLine toPolicyMapSetLine(Configuration c) {
      return new PolicyMapSetAsPathPrependLine(_asList);
   }

   @Override
   public RouteMapSetType getType() {
      return RouteMapSetType.AS_PATH_PREPEND;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      if (((RouteMapSetLine) o).getType() != RouteMapSetType.AS_PATH_PREPEND) {
         return false;
      }

      RouteMapSetAsPathPrependLine rhsLine = (RouteMapSetAsPathPrependLine) o;
      return getAsList().equals(rhsLine.getAsList());
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string);
         Util.diffRepresentationLists(null, _asList, string + "._asList");
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string);
         Util.diffRepresentationLists(_asList, null, string + "._asList");
         System.out.flush();
         return;
      }

      if (((RouteMapSetLine) o).getType() != RouteMapSetType.AS_PATH_PREPEND) {
         ((RepresentationObject) o).diffRepresentation(null, string, true);
         diffRepresentation(null, string, false);
         return;
      }

      RouteMapSetAsPathPrependLine rhs = (RouteMapSetAsPathPrependLine) o;

      Util.diffRepresentationLists(_asList, rhs._asList, string + "._asList");

      System.out.flush();
      return;
   }

}
