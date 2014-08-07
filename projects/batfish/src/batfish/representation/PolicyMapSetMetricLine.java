package batfish.representation;

public class PolicyMapSetMetricLine extends PolicyMapSetLine {

   private static final long serialVersionUID = 1L;

   private int _metric;

   public PolicyMapSetMetricLine(int metric) {
      _metric = metric;
   }

   @Override
   public boolean equals(Object o) {
      if (((PolicyMapSetLine) o).getType() != PolicyMapSetType.METRIC)
         return false;
      PolicyMapSetMetricLine rhsLine = (PolicyMapSetMetricLine) o;
      return getMetric() == rhsLine.getMetric();
   }

   public int getMetric() {
      return _metric;
   }

   @Override
   public PolicyMapSetType getType() {
      return PolicyMapSetType.METRIC;
   }

}
