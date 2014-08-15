package batfish.representation.cisco;

import batfish.representation.OspfMetricType;
import batfish.representation.Protocol;
import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class OspfRedistributionPolicy extends RedistributionPolicy implements
      RepresentationObject {

   private static final long serialVersionUID = 1L;

   public static final String BGP_AS = "BGP_AS";
   public static final OspfMetricType DEFAULT_METRIC_TYPE = OspfMetricType.E2;
   public static final int DEFAULT_REDISTRIBUTE_CONNECTED_METRIC = 20;
   public static final int DEFAULT_REDISTRIBUTE_STATIC_METRIC = 20;

   private String _map;
   private Integer _metric;
   private OspfMetricType _metricType;
   private boolean _subnets;
   private Long _tag;

   public OspfRedistributionPolicy(Protocol sourceProtocol) {
      super(sourceProtocol, Protocol.OSPF);
   }

   public String getMap() {
      return _map;
   }

   public Integer getMetric() {
      return _metric;
   }

   public OspfMetricType getMetricType() {
      return _metricType;
   }

   public boolean getSubnets() {
      return _subnets;
   }

   public Long getTag() {
      return _tag;
   }

   public void setMap(String name) {
      _map = name;
   }

   public void setMetric(int metric) {
      _metric = metric;
   }

   public void setOspfMetricType(OspfMetricType type) {
      _metricType = type;
   }

   public void setSubnets(boolean b) {
      _subnets = b;
   }

   public void setTag(long tag) {
      _tag = tag;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      OspfRedistributionPolicy rhs = (OspfRedistributionPolicy) o;
      if (_map == null) {
         if (rhs.getMap() != null)
            return false;
         return _metric == rhs.getMetric()
               && _metricType == rhs.getMetricType()
               && _subnets == rhs.getSubnets() && _tag == rhs.getTag();
      }
      return _map.equals(rhs.getMap()) && _metric == rhs.getMetric()
            && _metricType == rhs.getMetricType()
            && _subnets == rhs.getSubnets() && _tag == rhs.getTag();
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string + "\n");
         System.out.println("+ " + string + "._map:" + Util.objectToString(_map) + "\n");
         System.out.println("+ " + string + "._metric:" + Util.objectToString(_metric) + "\n");
         System.out.println("+ " + string + "._metricType:" + Util.objectToString(_metricType) + "\n");
         System.out.println("+ " + string + "._subnets:" + _subnets + "\n");
         System.out.println("+ " + string + "._tag:" + Util.objectToString(_tag) + "\n");
         System.out.println("+ " + string + "._destinationProtocol:" + Util.objectToString(_destinationProtocol) + "\n");
         System.out.println("+ " + string + "._sourceProtocol:" + Util.objectToString(_sourceProtocol) + "\n");
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string + "\n");
         System.out.println("- " + string + "._map:" + Util.objectToString(_map) + "\n");
         System.out.println("- " + string + "._metric:" + Util.objectToString(_metric) + "\n");
         System.out.println("- " + string + "._metricType:" + Util.objectToString(_metricType) + "\n");
         System.out.println("- " + string + "._subnets:" + _subnets + "\n");
         System.out.println("- " + string + "._tag:" + Util.objectToString(_tag) + "\n");
         System.out.println("- " + string + "._destinationProtocol:" + Util.objectToString(_destinationProtocol) + "\n");
         System.out.println("- " + string + "._sourceProtocol:" + Util.objectToString(_sourceProtocol) + "\n");
         System.out.flush();
         return;
      }

      OspfRedistributionPolicy rhs = (OspfRedistributionPolicy) o;

      if (!Util.equalOrNull(_map, rhs._map)) {
         System.out.println("- " + string + "._map:" + Util.objectToString(_map) + "\n");
         System.out.println("+ " + string + "._map:" + Util.objectToString(rhs._map) + "\n");
      }
      if (!Util.equalOrNull(_metric, rhs._metric)) {
         System.out.println("- " + string + "._metric:" + Util.objectToString(_metric) + "\n");
         System.out.println("+ " + string + "._metric:" + Util.objectToString(rhs._metric) + "\n");
      }
      if (!Util.equalOrNull(_metricType, rhs._metricType)) {
         System.out.println("- " + string + "._metricType:" + Util.objectToString(_metricType) + "\n");
         System.out.println("+ " + string + "._metricType:" + Util.objectToString(rhs._metricType) + "\n");
      }
      if (_subnets != rhs._subnets) {
         System.out.println("- " + string + "._subnets:" + _subnets + "\n");
         System.out.println("+ " + string + "._subnets:" + rhs._subnets
               + "\n");
      }
      if (!Util.equalOrNull(_tag, rhs._tag)) {
         System.out.println("- " + string + "._tag:" + Util.objectToString(_tag) + "\n");
         System.out.println("+ " + string + "._tag:" + Util.objectToString(rhs._tag) + "\n");
      }
      if (!Util.equalOrNull(_destinationProtocol, rhs._destinationProtocol)) {
         System.out.println("- " + string + "._destinationProtocol:" + Util.objectToString(_destinationProtocol) + "\n");
         System.out.println("+ " + string + "._destinationProtocol:" + Util.objectToString(rhs._destinationProtocol) + "\n");
      }
      if (!Util.equalOrNull(_sourceProtocol, rhs._sourceProtocol)) {
         System.out.println("- " + string + "._sourceProtocol:" + Util.objectToString(_sourceProtocol) + "\n");
         System.out.println("+ " + string + "._sourceProtocol:" + Util.objectToString(rhs._sourceProtocol) + "\n");
      }
      System.out.flush();
      return;
   }
}
