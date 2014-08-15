package batfish.representation.cisco;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import batfish.grammar.cisco.CiscoGrammar.Ip_community_list_expanded_stanzaContext;
import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class ExpandedCommunityList implements Serializable, RepresentationObject {

   private static final long serialVersionUID = 1L;

   private transient Ip_community_list_expanded_stanzaContext _context;
   private List<ExpandedCommunityListLine> _lines;
   private String _name;

   public ExpandedCommunityList(String name) {
      _name = name;
      _lines = new ArrayList<ExpandedCommunityListLine>();
   }

   public void addLine(ExpandedCommunityListLine line) {
      _lines.add(line);
   }

   public Ip_community_list_expanded_stanzaContext getContext() {
      return _context;
   }

   public List<ExpandedCommunityListLine> getLines() {
      return _lines;
   }

   public String getName() {
      return _name;
   }

   public void setContext(Ip_community_list_expanded_stanzaContext ctx) {
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

      ExpandedCommunityList rhs = (ExpandedCommunityList) o;
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
