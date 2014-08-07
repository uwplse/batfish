package batfish.representation.cisco;

import java.io.Serializable;

import batfish.representation.LineAction;
import batfish.representation.RepresentationObject;

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
}
