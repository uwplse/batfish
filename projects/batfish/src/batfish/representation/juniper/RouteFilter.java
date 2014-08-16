package batfish.representation.juniper;

import java.util.ArrayList;
import java.util.List;

import batfish.representation.RepresentationObject;
import batfish.util.Util;

/**
 * 
 * A data structure that represents a list of prefix with their prefix-length to
 * be matched Used for route filter and prefix list in Juniper JunOS Used for
 * prefix list in Cisco IOS
 * 
 */

public class RouteFilter implements RepresentationObject {
   // Name of the filter
   private String _name;

   // List of lines that stores the prefix
   private List<RouteFilterLine> _lines;

   public RouteFilter(String n) {
      _name = n;
      _lines = new ArrayList<RouteFilterLine>();
   }

   public void addLine(RouteFilterLine r) {
      _lines.add(r);
   }

   public void addLines(List<RouteFilterLine> r) {
      _lines.addAll(r);
   }

   public String getName() {
      return _name;
   }

   public List<RouteFilterLine> getLines() {
      return _lines;
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
         System.out.println("+ " + string + "._name:"
               + Util.objectToString(_name));
         Util.diffRepresentationLists(null, _lines, string + "._lines");
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string);
         System.out.println("- " + string + "._name:"
               + Util.objectToString(_name));
         Util.diffRepresentationLists(_lines, null, string + "._lines");
         System.out.flush();
         return;
      }

      RouteFilter rhs = (RouteFilter) o;
      if (!Util.equalOrNull(_name, rhs._name)) {
         System.out.println("- " + string + "._name:"
               + Util.objectToString(_name));
         System.out.println("+ " + string + "._name:"
               + Util.objectToString(rhs._name));
      }

      Util.diffRepresentationLists(_lines, rhs._lines, string + "._lines");

      System.out.flush();
      return;
   }

}
