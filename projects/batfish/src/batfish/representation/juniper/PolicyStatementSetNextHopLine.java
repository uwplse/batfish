package batfish.representation.juniper;

import java.util.List;

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

}
