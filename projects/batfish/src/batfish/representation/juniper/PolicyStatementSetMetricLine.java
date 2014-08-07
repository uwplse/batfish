package batfish.representation.juniper;

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

}
