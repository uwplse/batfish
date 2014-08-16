package batfish.representation.juniper;

import java.util.ArrayList;
import java.util.List;

import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class ExpandedCommunityList implements RepresentationObject {

   private String _name;
   private List<ExpandedCommunityListLine> _lines;

   public ExpandedCommunityList(String name) {
      _name = name;
      _lines = new ArrayList<ExpandedCommunityListLine>();
   }

   public String getName() {
      return _name;
   }

   public void addLine(ExpandedCommunityListLine line) {
      _lines.add(line);
   }

   public List<ExpandedCommunityListLine> getLines() {
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

      ExpandedCommunityList rhs = (ExpandedCommunityList) o;
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
