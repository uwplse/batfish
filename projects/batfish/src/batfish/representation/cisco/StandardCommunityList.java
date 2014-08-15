package batfish.representation.cisco;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import batfish.grammar.cisco.CiscoGrammar.Ip_community_list_standard_stanzaContext;
import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class StandardCommunityList implements Serializable,
      RepresentationObject {

   private static final long serialVersionUID = 1L;

   private transient Ip_community_list_standard_stanzaContext _context;
   private List<StandardCommunityListLine> _lines;
   private String _name;

   public StandardCommunityList(String name) {
      _name = name;
      _lines = new ArrayList<StandardCommunityListLine>();
   }

   public void addLine(StandardCommunityListLine line) {
      _lines.add(line);
   }

   public Ip_community_list_standard_stanzaContext getContext() {
      return _context;
   }

   public List<StandardCommunityListLine> getLines() {
      return _lines;
   }

   public String getName() {
      return _name;
   }

   public void setContext(Ip_community_list_standard_stanzaContext ctx) {
      _context = ctx;
   }

   public ExpandedCommunityList toExpandedCommunityList() {
      ExpandedCommunityList newList = new ExpandedCommunityList(_name);
      for (StandardCommunityListLine line : _lines) {
         List<Long> standardCommunities = line.getCommunities();
         String regex = "(";
         for (Long l : standardCommunities) {
            regex += batfish.util.Util.longToCommunity(l) + "|";
         }
         regex = regex.substring(0, regex.length() - 1) + ")";
         ExpandedCommunityListLine newLine = new ExpandedCommunityListLine(
               line.getAction(), regex);
         newList.addLine(newLine);
      }
      return newList;
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
         System.out.println("+ " + string + "._name:"
               + Util.objectToString(_name) + "\n");
         Util.diffRepresentationLists(null, _lines, string + "._lines");
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string + "\n");
         System.out.println("- " + string + "._name:"
               + Util.objectToString(_name) + "\n");
         Util.diffRepresentationLists(_lines, null, string + "._lines");
         System.out.flush();
         return;
      }

      StandardCommunityList rhs = (StandardCommunityList) o;
      if (!Util.equalOrNull(_name, rhs._name)) {
         System.out.println("- " + string + "._name:"
               + Util.objectToString(_name) + "\n");
         System.out.println("+ " + string + "._name:"
               + Util.objectToString(rhs._name) + "\n");
      }

      Util.diffRepresentationLists(_lines, rhs._lines, string + "._lines");

      System.out.flush();
      return;

   }

}
