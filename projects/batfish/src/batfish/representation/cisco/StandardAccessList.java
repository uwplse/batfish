package batfish.representation.cisco;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import batfish.grammar.cisco.CiscoGrammar.Standard_access_list_stanzaContext;
import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class StandardAccessList implements Serializable, RepresentationObject {

   private static final long serialVersionUID = 1L;

   private transient Standard_access_list_stanzaContext _context;
   private String _id;
   private List<StandardAccessListLine> _lines;

   public StandardAccessList(String id) {
      _id = id;
      _lines = new ArrayList<StandardAccessListLine>();
      // _lines.add(new StandardAccessListLine(LineAction.REJECT, "0.0.0.0",
      // "255.255.255.255"));
   }

   public void addLine(StandardAccessListLine all) {
      _lines.add(all);
   }

   public Standard_access_list_stanzaContext getContext() {
      return _context;
   }

   public String getId() {
      return _id;
   }

   public List<StandardAccessListLine> getLines() {
      return _lines;
   }

   public void setContext(Standard_access_list_stanzaContext ctx) {
      _context = ctx;
   }

   public ExtendedAccessList toExtendedAccessList() {
      ExtendedAccessList eal = new ExtendedAccessList(_id);
      eal.getLines().clear();
      for (StandardAccessListLine sall : _lines) {
         eal.addLine(sall.toExtendedAccessListLine());
      }
      return eal;
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

      StandardAccessList rhs = (StandardAccessList) o;
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
