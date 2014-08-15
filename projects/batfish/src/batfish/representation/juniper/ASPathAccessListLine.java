package batfish.representation.juniper;

import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class ASPathAccessListLine implements RepresentationObject{

   private String _regex;

   public ASPathAccessListLine(String regex) {
      _regex = regex;
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
         System.out.println("+ " + string + "\n");
         System.out.println("+ " + string + "._regex:" + Util.objectToString(_regex) + "\n");
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string + "\n");
         System.out.println("- " + string + "._regex:" + Util.objectToString(_regex) + "\n");
         System.out.flush();
         return;
      }

      ASPathAccessListLine rhs = (ASPathAccessListLine) o;
      if (!Util.equalOrNull(_regex, rhs._regex)) {
         System.out.println("- " + string + "._regex:" + Util.objectToString(_regex) + "\n");
         System.out.println("+ " + string + "._regex:" + Util.objectToString(rhs._regex) + "\n");
      }
      
      System.out.flush();
      return;
   }

}
