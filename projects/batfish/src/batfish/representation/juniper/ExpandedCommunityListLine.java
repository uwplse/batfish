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
}
