package batfish.representation.cisco;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import batfish.grammar.cisco.CiscoGrammar.Route_map_stanzaContext;
import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class RouteMap implements Serializable, RepresentationObject {

   private static final long serialVersionUID = 1L;

   private Map<Integer, RouteMapClause> _clauses;
   private transient Route_map_stanzaContext _context;
   private boolean _ignore;
   private String _mapName;

   public RouteMap(String name) {
      _mapName = name;
      _clauses = new TreeMap<Integer, RouteMapClause>();
      _ignore = false;
   }

   public Map<Integer, RouteMapClause> getClauses() {
      return _clauses;
   }

   public Route_map_stanzaContext getContext() {
      return _context;
   }

   public boolean getIgnore() {
      return _ignore;
   }

   public String getMapName() {
      return _mapName;
   }

   public void setContext(Route_map_stanzaContext ctx) {
      _context = ctx;
   }

   public void setIgnore(boolean b) {
      _ignore = b;
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
         Util.diffRepresentationMaps(null, _clauses, string + "._clauses");
         System.out.println("+ " + string + "._ignore:" + _ignore);
         System.out.println("+ " + string + "._mapName:"
               + Util.objectToString(_mapName));
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string);
         Util.diffRepresentationMaps(_clauses, null, string + "._clauses");
         System.out.println("- " + string + "._ignore:" + _ignore);
         System.out.println("- " + string + "._mapName:"
               + Util.objectToString(_mapName));
         System.out.flush();
         return;
      }

      RouteMap rhs = (RouteMap) o;

      Util.diffRepresentationMaps(_clauses, rhs._clauses, string + "._clauses");

      if (_ignore != rhs._ignore) {
         System.out.println("- " + string + "._ignore:" + _ignore);
         System.out.println("+ " + string + "._ignore:" + rhs._ignore);
      }

      if (!Util.equalOrNull(_mapName, rhs._mapName)) {
         System.out.println("- " + string + "._mapName:"
               + Util.objectToString(_mapName));
         System.out.println("+ " + string + "._mapName:"
               + Util.objectToString(rhs._mapName));
      }

      System.out.flush();
      return;
   }

}
