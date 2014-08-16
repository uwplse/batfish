package batfish.representation.cisco;

import batfish.representation.Configuration;
import batfish.representation.PolicyMapSetLine;
import batfish.representation.PolicyMapSetMetricLine;
import batfish.representation.RepresentationObject;

public class RouteMapSetMetricLine extends RouteMapSetLine {

   private static final long serialVersionUID = 1L;

   private int _metric;

   public RouteMapSetMetricLine(int metric) {
      _metric = metric;
   }

   public int getMetric() {
      return _metric;
   }

   @Override
   public PolicyMapSetLine toPolicyMapSetLine(Configuration c) {
      return new PolicyMapSetMetricLine(_metric);
   }

   @Override
   public RouteMapSetType getType() {
      return RouteMapSetType.METRIC;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      if (((RouteMapSetLine) o).getType() != RouteMapSetType.METRIC) {
         return false;
      }

      RouteMapSetMetricLine rhsLine = (RouteMapSetMetricLine) o;
      return getMetric() == (rhsLine.getMetric());
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string);
         System.out.println("+ " + string + "._metric:" + _metric);
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string);
         System.out.println("- " + string + "._metric:" + _metric);
         System.out.flush();
         return;
      }

      if (((RouteMapSetLine) o).getType() != RouteMapSetType.METRIC) {
         ((RepresentationObject) o).diffRepresentation(null, string, true);
         diffRepresentation(null, string, false);
         return;
      }

      RouteMapSetMetricLine rhs = (RouteMapSetMetricLine) o;
      if (_metric != rhs._metric) {
         System.out.println("- " + string + "._metric:" + _metric);
         System.out.println("+ " + string + "._metric:" + rhs._metric);
      }
      System.out.flush();
      return;
   }
}
