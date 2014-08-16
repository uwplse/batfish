package batfish.representation.juniper;

import java.util.List;

import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class ASPathAccessList implements RepresentationObject {
   private List<ASPathAccessListLine> _lines;
   private String _name;

   public ASPathAccessList(String name, List<ASPathAccessListLine> lines) {
      _lines = lines;
      _name = name;
   }

   public List<ASPathAccessListLine> getLines() {
      return _lines;
   }

   public String getName() {
      return _name;
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

      ASPathAccessList rhs = (ASPathAccessList) o;
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
