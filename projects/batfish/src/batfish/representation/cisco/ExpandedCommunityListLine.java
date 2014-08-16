package batfish.representation.cisco;

import java.io.Serializable;

import batfish.representation.LineAction;
import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class ExpandedCommunityListLine implements Serializable,
      RepresentationObject {

   private static final long serialVersionUID = 1L;

   private LineAction _action;
   private String _regex;

   public ExpandedCommunityListLine(LineAction action, String regex) {
      _action = action;
      _regex = regex;
   }

   public LineAction getAction() {
      return _action;
   }

   public String getRegex() {
      return _regex;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string);
         System.out.println("+ " + string + "._action:"
               + Util.objectToString(_action));
         System.out.println("+ " + string + "._regex:"
               + Util.objectToString(_regex));
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string);
         System.out.println("- " + string + "._action:"
               + Util.objectToString(_action));
         System.out.println("- " + string + "._regex:"
               + Util.objectToString(_regex));
         System.out.flush();
         return;
      }

      ExpandedCommunityListLine rhs = (ExpandedCommunityListLine) o;
      if (!Util.equalOrNull(_action, rhs._action)) {
         System.out.println("- " + string + "._action:"
               + Util.objectToString(_action));
         System.out.println("+ " + string + "._action:"
               + Util.objectToString(rhs._action));
      }
      if (!Util.equalOrNull(_regex, rhs._regex)) {
         System.out.println("- " + string + "._regex:"
               + Util.objectToString(_regex));
         System.out.println("+ " + string + "._regex:"
               + Util.objectToString(rhs._regex));
      }
      System.out.flush();
      return;
   }
}
