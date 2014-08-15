package batfish.representation.juniper;

import batfish.representation.LineAction;
import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class ExpandedCommunityListLine implements RepresentationObject {
   private String _regex;
   private LineAction _action;

   public ExpandedCommunityListLine(LineAction action, String regex) {
      _action = action;
      _regex = regex;
   }

   public String getRegex() {
      return _regex;
   }

   public LineAction getAction() {
      return _action;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      ExpandedCommunityListLine rhs = (ExpandedCommunityListLine) o;
      return Util.equalOrNull(_action, rhs._action)
            && Util.equalOrNull(_regex, rhs._regex);
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string + "\n");
         System.out.println("+ " + string + "._action:" + Util.objectToString(_action) + "\n");
         System.out.println("+ " + string + "._regex:" + Util.objectToString(_regex) + "\n");
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string + "\n");
         System.out.println("- " + string + "._action:" + Util.objectToString(_action) + "\n");
         System.out.println("- " + string + "._regex:" + Util.objectToString(_regex) + "\n");
         System.out.flush();
         return;
      }

      ExpandedCommunityListLine rhs = (ExpandedCommunityListLine) o;
      if (!Util.equalOrNull(_action, rhs._action)) {
         System.out.println("- " + string + "._action:" + Util.objectToString(_action) + "\n");
         System.out.println("+ " + string + "._action:" + Util.objectToString(rhs._action) + "\n");
      }
      if (!Util.equalOrNull(_regex, rhs._regex)) {
         System.out.println("- " + string + "._regex:" + Util.objectToString(_regex) + "\n");
         System.out.println("+ " + string + "._regex:" + Util.objectToString(rhs._regex) + "\n");
      }
      System.out.flush();
      return;
   }
}
