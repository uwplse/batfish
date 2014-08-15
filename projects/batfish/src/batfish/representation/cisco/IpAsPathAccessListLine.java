package batfish.representation.cisco;

import java.io.Serializable;

import batfish.representation.LineAction;
import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class IpAsPathAccessListLine implements Serializable,
      RepresentationObject {

   private static final long serialVersionUID = 1L;

   private LineAction _action;
   private String _regex;

   public IpAsPathAccessListLine(LineAction action, String regex) {
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
      IpAsPathAccessListLine rhs = (IpAsPathAccessListLine) o;
      if (_action == null && rhs.getAction() != null)
         return false;
      if (_regex == null && rhs.getRegex() != null)
         return false;
      return (_action.equals(rhs.getAction()) && _regex.equals(rhs.getRegex()));

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

      IpAsPathAccessListLine rhs = (IpAsPathAccessListLine) o;
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
