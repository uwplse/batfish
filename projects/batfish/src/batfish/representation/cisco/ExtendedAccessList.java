package batfish.representation.cisco;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import batfish.grammar.cisco.CiscoGrammar.Extended_access_list_stanzaContext;
import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class ExtendedAccessList implements Serializable, RepresentationObject {

   private static final long serialVersionUID = 1L;

   private transient Extended_access_list_stanzaContext _context;
   private String _id;
   private List<ExtendedAccessListLine> _lines;

   public ExtendedAccessList(String id) {
      _id = id;
      _lines = new ArrayList<ExtendedAccessListLine>();
      // _lines.add(new ExtendedAccessListLine(LineAction.REJECT, 0,
      // "0.0.0.0", "255.255.255.255", "0.0.0.0", "255.255.255.255", null));
   }

   public void addLine(ExtendedAccessListLine all) {
      _lines.add(all);
   }

   public Extended_access_list_stanzaContext getContext() {
      return _context;
   }

   public String getId() {
      return _id;
   }

   public List<ExtendedAccessListLine> getLines() {
      return _lines;
   }

   public void setContext(Extended_access_list_stanzaContext ctx) {
      _context = ctx;
   }

   @Override
   public String toString() {
      String output = super.toString() + "\n" + "Identifier: " + _id;
      for (ExtendedAccessListLine line : _lines) {
         output += "\n" + line;
      }
      return output;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      ExtendedAccessList rhs = (ExtendedAccessList) o;
      return Util.equalOrNull(_id, rhs._id)
            && Util.cmpRepresentationLists(_lines, rhs._lines) == 0;
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string + "\n");
         System.out.println("+ " + string + "._id:" + Util.objectToString(_id)
               + "\n");
         Util.diffRepresentationLists(null, _lines, string + "._lines");
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string + "\n");
         System.out.println("- " + string + "._id:" + Util.objectToString(_id)
               + "\n");
         Util.diffRepresentationLists(_lines, null, string + "._lines");
         System.out.flush();
         return;
      }

      ExtendedAccessList rhs = (ExtendedAccessList) o;
      if (!Util.equalOrNull(_id, rhs._id)) {
         System.out.println("- " + string + "._id:" + Util.objectToString(_id)
               + "\n");
         System.out.println("+ " + string + "._id:"
               + Util.objectToString(rhs._id) + "\n");
      }

      Util.diffRepresentationLists(_lines, rhs._lines, string + "._lines");

      System.out.flush();
      return;
   }
}
