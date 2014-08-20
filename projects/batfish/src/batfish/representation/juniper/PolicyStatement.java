package batfish.representation.juniper;

import java.util.Arrays;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class PolicyStatement implements RepresentationObject {
   private NavigableMap<Integer, PolicyStatementClause> _clauses;
   private String _mapName;

   public PolicyStatement(String name) {
      _mapName = name;
      _clauses = new TreeMap<Integer, PolicyStatementClause>();
   }

   public void addClause(PolicyStatementClause rmc) {
      _clauses.put(rmc.getSeqNum(), rmc);
   }

   public List<PolicyStatementClause> getClauseList() {
      return Arrays.asList(_clauses.values().toArray(
            new PolicyStatementClause[0]));
   }

   public NavigableMap<Integer, PolicyStatementClause> getClauseMap() {
      return _clauses;
   }

   public String getMapName() {
      return _mapName;
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
         System.out.println("+ " + string + "._mapName:"
               + Util.objectToString(_mapName));
         System.out.flush();
         return;
      }

      if (o == null) {         
         System.out.println("- " + string);
         Util.diffRepresentationMaps(_clauses, null, string + "._clauses");
         System.out.println("- " + string + "._mapName:"
               + Util.objectToString(_mapName));
         System.out.flush();
         return;
      }

      PolicyStatement rhs = (PolicyStatement) o;
      Util.diffRepresentationMaps(_clauses, rhs._clauses, string + "._clauses");
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
