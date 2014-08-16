package batfish.representation.cisco;

import java.io.Serializable;
import java.util.List;

import batfish.representation.LineAction;
import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class StandardCommunityListLine implements Serializable,
      RepresentationObject {

   private static final long serialVersionUID = 1L;

   private LineAction _action;
   private List<Long> _communities;

   public StandardCommunityListLine(LineAction action, List<Long> communities) {
      _action = action;
      _communities = communities;
   }

   public LineAction getAction() {
      return _action;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      StandardCommunityListLine rhs = (StandardCommunityListLine) o;
      if (_action == null && rhs.getAction() != null)
         return false;
      if (_communities == null && rhs.getCommunities() != null)
         return false;
      return (_action.equals(rhs.getAction()) && _communities.equals(rhs
            .getCommunities()));

   }

   public List<Long> getCommunities() {
      return _communities;
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string);
         System.out.println("+ " + string + "._action:"
               + Util.objectToString(_action));
         Util.diffRepresentationLists(null, _communities, string
               + "._communities");
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string);
         System.out.println("- " + string + "._action:"
               + Util.objectToString(_action));
         Util.diffRepresentationLists(_communities, null, string
               + "._communities");
         System.out.flush();
         return;
      }

      StandardCommunityListLine rhs = (StandardCommunityListLine) o;
      if (!Util.equalOrNull(_action, rhs._action)) {
         System.out.println("- " + string + "._action:"
               + Util.objectToString(_action));
         System.out.println("+ " + string + "._action:"
               + Util.objectToString(rhs._action));
      }

      Util.diffRepresentationLists(_communities, rhs._communities, string
            + "._communities");

      System.out.flush();
      return;

   }

}
