package batfish.representation.cisco;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import batfish.grammar.cisco.CiscoGrammar.Route_map_tailContext;
import batfish.representation.LineAction;
import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class RouteMapClause implements Serializable, RepresentationObject {

   private static final long serialVersionUID = 1L;

   private LineAction _action;
   private transient Route_map_tailContext _context;
   private boolean _ignore;
   private String _mapName;
   private List<RouteMapMatchLine> _matchList;
   private int _seqNum;
   private List<RouteMapSetLine> _setList;

   public RouteMapClause(LineAction action, String name, int num) {
      _action = action;
      _mapName = name;
      _seqNum = num;
      _matchList = new ArrayList<RouteMapMatchLine>();
      _setList = new ArrayList<RouteMapSetLine>();
   }

   public void addMatchLine(RouteMapMatchLine line) {
      _matchList.add(line);
   }

   public void addSetLine(RouteMapSetLine line) {
      _setList.add(line);
   }

   public LineAction getAction() {
      return _action;
   }

   public Route_map_tailContext getContext() {
      return _context;
   }

   public boolean getIgnore() {
      return _ignore;
   }

   public String getMapName() {
      return _mapName;
   }

   public List<RouteMapMatchLine> getMatchList() {
      return _matchList;
   }

   public int getSeqNum() {
      return _seqNum;
   }

   public List<RouteMapSetLine> getSetList() {
      return _setList;
   }

   public void setContext(Route_map_tailContext ctx) {
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
         System.out.println("+ " + string + "._action:"
               + Util.objectToString(_action));
         System.out.println("+ " + string + "._ignore:" + _ignore);
         System.out.println("+ " + string + "._mapName:"
               + Util.objectToString(_mapName));
         Util.diffRepresentationLists(null, _matchList, string + "._matchList");
         System.out.println("+ " + string + "._seqNum:" + _seqNum);
         Util.diffRepresentationLists(null, _setList, string + "._setList");
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string);
         System.out.println("- " + string + "._action:"
               + Util.objectToString(_action));
         System.out.println("- " + string + "._ignore:" + _ignore);
         System.out.println("- " + string + "._mapName:"
               + Util.objectToString(_mapName));
         Util.diffRepresentationLists(_matchList, null, string + "._matchList");
         System.out.println("- " + string + "._seqNum:" + _seqNum);
         Util.diffRepresentationLists(_setList, null, string + "._setList");
         System.out.flush();
         return;
      }

      RouteMapClause rhs = (RouteMapClause) o;

      if (!Util.equalOrNull(_action, rhs._action)) {
         System.out.println("- " + string + "._action:"
               + Util.objectToString(_action));
         System.out.println("+ " + string + "._action:"
               + Util.objectToString(rhs._action));
      }

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

      Util.diffRepresentationLists(_matchList, rhs._matchList, string
            + "._matchList");

      if (_seqNum != rhs._seqNum) {
         System.out.println("- " + string + "._seqNum:" + _seqNum);
         System.out.println("+ " + string + "._seqNum:" + rhs._seqNum);
      }

      Util.diffRepresentationLists(_setList, rhs._setList, string + "._setList");

      System.out.flush();
      return;

   }

}
