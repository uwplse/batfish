package batfish.representation.cisco;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import batfish.grammar.cisco.CiscoGrammar.Ip_as_path_access_list_stanzaContext;
import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class IpAsPathAccessList implements Serializable,RepresentationObject {

   private static final long serialVersionUID = 1L;

   private transient Ip_as_path_access_list_stanzaContext _context;
   private List<IpAsPathAccessListLine> _lines;
   private String _name;

   public IpAsPathAccessList(String name) {
      _name = name;
      _lines = new ArrayList<IpAsPathAccessListLine>();
   }

   public void addLine(IpAsPathAccessListLine line) {
      _lines.add(line);
   }

   public Ip_as_path_access_list_stanzaContext getContext() {
      return _context;
   }

   public List<IpAsPathAccessListLine> getLines() {
      return _lines;
   }

   public String getName() {
      return _name;
   }

   public void setContext(Ip_as_path_access_list_stanzaContext ctx) {
      _context = ctx;
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
         System.out.println("+ " + string + "._name:" + Util.objectToString(_name)
               + "\n");
         Util.diffRepresentationLists(null, _lines, string + "._lines");
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string + "\n");
         System.out.println("- " + string + "._name:" + Util.objectToString(_name)
               + "\n");
         Util.diffRepresentationLists(_lines, null, string + "._lines");
         System.out.flush();
         return;
      }

      IpAsPathAccessList rhs = (IpAsPathAccessList) o;
      if (!Util.equalOrNull(_name, rhs._name)) {
         System.out.println("- " + string + "._name:" + Util.objectToString(_name)
               + "\n");
         System.out.println("+ " + string + "._name:"
               + Util.objectToString(rhs._name) + "\n");
      }

      Util.diffRepresentationLists(_lines, rhs._lines, string + "._lines");

      System.out.flush();
      return;
   }

}
