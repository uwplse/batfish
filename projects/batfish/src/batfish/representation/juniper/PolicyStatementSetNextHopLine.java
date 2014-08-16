package batfish.representation.juniper;

import java.util.List;

import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class PolicyStatementSetNextHopLine extends PolicyStatementSetLine {

   private List<String> _nextHops;

   public PolicyStatementSetNextHopLine(List<String> nextHops) {
      _nextHops = nextHops;
   }

   @Override
   public SetType getSetType() {
      return SetType.NEXT_HOP;
   }

   public List<String> getNextHops() {
      return _nextHops;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      if (((PolicyStatementSetLine) o).getSetType() != SetType.NEXT_HOP) {
         return false;
      }

      PolicyStatementSetNextHopLine rhsLine = (PolicyStatementSetNextHopLine) o;
      return Util.cmpRepresentationLists(_nextHops, rhsLine._nextHops) == 0;
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string);
         Util.diffRepresentationLists(null, _nextHops, string + "._nextHops");
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string);
         Util.diffRepresentationLists(_nextHops, null, string + "._nextHops");
         System.out.flush();
         return;
      }

      if (((PolicyStatementSetLine) o).getSetType() != SetType.NEXT_HOP) {
         ((RepresentationObject) o).diffRepresentation(null, string, true);
         diffRepresentation(null, string, false);
         return;
      }

      PolicyStatementSetNextHopLine rhs = (PolicyStatementSetNextHopLine) o;
      Util.diffRepresentationLists(_nextHops, rhs._nextHops, string
            + "._nextHops");
      System.out.flush();
      return;
   }

}
