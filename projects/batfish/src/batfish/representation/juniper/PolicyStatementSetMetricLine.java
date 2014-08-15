package batfish.representation.juniper;

import batfish.representation.RepresentationObject;

public class PolicyStatementSetMetricLine extends PolicyStatementSetLine {

   private int _metric;

   public PolicyStatementSetMetricLine(int metric) {
      _metric = metric;
   }

   @Override
   public SetType getSetType() {
      return SetType.METRIC;
   }

   public int getMetric() {
      return _metric;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      if (((PolicyStatementSetLine) o).getSetType() != SetType.METRIC) {
         return false;
      }

      PolicyStatementSetMetricLine rhsLine = (PolicyStatementSetMetricLine) o;
      return _metric == rhsLine._metric;
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string + "\n");
         System.out.println("+ " + string + "._metric:" + _metric + "\n");
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string + "\n");
         System.out.println("- " + string + "._metric:" + _metric + "\n");
         System.out.flush();
         return;
      }

      if (((PolicyStatementSetLine) o).getSetType() != SetType.METRIC) {
         ((RepresentationObject) o).diffRepresentation(null, string, true);
         diffRepresentation(null, string, false);
         return;
      }

      PolicyStatementSetMetricLine rhs = (PolicyStatementSetMetricLine) o;
      if (_metric != rhs._metric) {
         System.out.println("- " + string + "._metric:" + _metric + "\n");
         System.out.println("+ " + string + "._metric:" + rhs._metric + "\n");
      }
      System.out.flush();
      return;
   }

}
