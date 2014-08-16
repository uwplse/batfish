package batfish.representation.juniper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import batfish.representation.LineAction;
import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class ExtendedAccessList implements RepresentationObject {
   private List<ExtendedAccessListLine> _lines;
   private List<ExtendedAccessListTerm> _terms;
   private String _id;

   public ExtendedAccessList(String id) {
      _id = id;
      _lines = new LinkedList<ExtendedAccessListLine>();
      _lines.add(new ExtendedAccessListLine(LineAction.REJECT, 0, "0.0.0.0",
            "255.255.255.255", "0.0.0.0", "255.255.255.255", null, null)); // TODO:
                                                                           // Stanley,
                                                                           // change
                                                                           // these
                                                                           // 'null's
                                                                           // so
                                                                           // destination
                                                                           // port
                                                                           // ranges
                                                                           // work
                                                                           // in
                                                                           // Juniper
      _terms = new ArrayList<ExtendedAccessListTerm>();
   }

   public String getId() {
      return _id;
   }

   public void addTerm(ExtendedAccessListTerm t) {
      _terms.add(t);
   }

   public void addLine(ExtendedAccessListLine all) {
      _lines.add(_lines.size() - 1, all);
   }

   public List<ExtendedAccessListLine> getLines() {
      return _lines;
   }

   public List<ExtendedAccessListTerm> getTerms() {
      return _terms;
   }

   @Override
   public String toString() {
      String output = super.toString() + "Identifier: " + _id;
      for (ExtendedAccessListLine line : _lines) {
         output += "\n" + line;
      }
      return output;
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
         System.out.println("+ " + string + "._id:" + Util.objectToString(_id));
         Util.diffRepresentationLists(null, _lines, string + "._lines");
         Util.diffRepresentationLists(null, _terms, string + "._terms");
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string);
         System.out.println("- " + string + "._id:" + Util.objectToString(_id));
         Util.diffRepresentationLists(_lines, null, string + "._lines");
         Util.diffRepresentationLists(_terms, null, string + "._terms");
         System.out.flush();
         return;
      }

      ExtendedAccessList rhs = (ExtendedAccessList) o;
      if (!Util.equalOrNull(_id, rhs._id)) {
         System.out.println("- " + string + "._id:" + Util.objectToString(_id));
         System.out.println("+ " + string + "._id:"
               + Util.objectToString(rhs._id));
      }

      Util.diffRepresentationLists(_lines, rhs._lines, string + "._lines");
      Util.diffRepresentationLists(_terms, rhs._terms, string + "._terms");

      System.out.flush();
      return;
   }
}
