package batfish.representation.cisco;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import batfish.grammar.cisco.CiscoGrammar.Ip_prefix_list_stanzaContext;
import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class PrefixList implements Serializable, RepresentationObject{

   private static final long serialVersionUID = 1L;

   private transient Ip_prefix_list_stanzaContext _context;

   // List of lines that stores the prefix
   private List<PrefixListLine> _lines;

   // Name of the filter
   private String _name;

   public PrefixList(String n) {
      _name = n;
      _lines = new ArrayList<PrefixListLine>();
   }

   public void addLine(PrefixListLine r) {
      _lines.add(r);
   }

   public void addLines(List<PrefixListLine> r) {
      _lines.addAll(r);
   }

   public Ip_prefix_list_stanzaContext getContext() {
      return _context;
   }

   public List<PrefixListLine> getLines() {
      return _lines;
   }

   public String getName() {
      return _name;
   }

   public void setContext(Ip_prefix_list_stanzaContext ctx) {
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

      PrefixList rhs = (PrefixList) o;
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
