package batfish.representation.cisco;

import batfish.representation.CommunityList;
import batfish.representation.Configuration;
import batfish.representation.PolicyMapSetDeleteCommunityLine;
import batfish.representation.PolicyMapSetLine;
import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class RouteMapSetDeleteCommunityLine extends RouteMapSetLine {

   private static final long serialVersionUID = 1L;

   private String _listName;

   public RouteMapSetDeleteCommunityLine(String listName) {
      _listName = listName;
   }

   public String getListName() {
      return _listName;
   }

   @Override
   public PolicyMapSetLine toPolicyMapSetLine(Configuration c) {
      CommunityList dcList = c.getCommunityLists().get(_listName);
      return new PolicyMapSetDeleteCommunityLine(dcList);
   }

   @Override
   public RouteMapSetType getType() {
      return RouteMapSetType.DELETE_COMMUNITY;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      if (((RouteMapSetLine) o).getType() != RouteMapSetType.DELETE_COMMUNITY) {
         return false;
      }

      RouteMapSetDeleteCommunityLine rhsLine = (RouteMapSetDeleteCommunityLine) o;
      return getListName().equals(rhsLine.getListName());
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string);
         System.out.println("+ " + string + "._listName:"
               + Util.objectToString(_listName));
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string);
         System.out.println("- " + string + "._listName:"
               + Util.objectToString(_listName));
         System.out.flush();
         return;
      }

      if (((RouteMapSetLine) o).getType() != RouteMapSetType.DELETE_COMMUNITY) {
         ((RepresentationObject) o).diffRepresentation(null, string, true);
         diffRepresentation(null, string, false);
         return;
      }

      RouteMapSetDeleteCommunityLine rhs = (RouteMapSetDeleteCommunityLine) o;
      if (!Util.equalOrNull(_listName, rhs._listName)) {
         System.out.println("- " + string + "._listName:"
               + Util.objectToString(_listName));
         System.out.println("+ " + string + "._listName:"
               + Util.objectToString(rhs._listName));
      }

      System.out.flush();
      return;
   }
}
