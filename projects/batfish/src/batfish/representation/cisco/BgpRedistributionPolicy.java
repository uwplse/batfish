package batfish.representation.cisco;

import java.io.Serializable;

import batfish.representation.Protocol;
import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class BgpRedistributionPolicy extends RedistributionPolicy implements
      Serializable, RepresentationObject {

   public static final String OSPF_PROCESS_NUMBER = "OSPF_PROCESS_NUMBER";

   private static final long serialVersionUID = 1L;

   private String _map;
   private Integer _metric;

   public BgpRedistributionPolicy(Protocol sourceProtocol) {
      super(sourceProtocol, Protocol.BGP);
   }

   public String getMap() {
      return _map;
   }

   public Integer getMetric() {
      return _metric;
   }

   public void setMap(String name) {
      _map = name;
   }

   public void setMetric(int metric) {
      _metric = metric;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string + "\n");
;
         System.out.println("+ " + string + "._map:" + Util.objectToString(_map) + "\n");
         System.out.println("+ " + string + "._metric:" + Util.objectToString(_metric) + "\n");
         System.out.println("+ " + string + "._destinationProtocol:" + Util.objectToString(_destinationProtocol) + "\n");
         System.out.println("+ " + string + "._sourceProtocol:" + Util.objectToString(_sourceProtocol) + "\n");
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string + "\n");
         System.out.println("- " + string + "._map:" + Util.objectToString(_map) + "\n");
         System.out.println("- " + string + "._metric:" + Util.objectToString(_metric) + "\n");
         System.out.println("- " + string + "._destinationProtocol:" + Util.objectToString(_destinationProtocol) + "\n");
         System.out.println("- " + string + "._sourceProtocol:" + Util.objectToString(_sourceProtocol) + "\n");
         System.out.flush();
         return;
      }

      BgpRedistributionPolicy rhs = (BgpRedistributionPolicy) o;
      if (!Util.equalOrNull(_map, rhs._map)) {
         System.out.println("- " + string + "._map:" + Util.objectToString(_map) + "\n");
         System.out.println("+ " + string + "._map:" + Util.objectToString(rhs._map) + "\n");
      }
      if (!Util.equalOrNull(_metric, rhs._metric)) {
         System.out.println("- " + string + "._metric:" + Util.objectToString(_metric) + "\n");
         System.out.println("+ " + string + "._metric:" + Util.objectToString(rhs._metric) + "\n");
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
