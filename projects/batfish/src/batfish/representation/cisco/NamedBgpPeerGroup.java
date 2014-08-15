package batfish.representation.cisco;

import java.util.LinkedHashSet;
import java.util.Set;

import batfish.representation.Ip;
import batfish.util.Util;

public class NamedBgpPeerGroup extends BgpPeerGroup {

   public static final NamedBgpPeerGroup DEFAULT_INSTANCE = new NamedBgpPeerGroup(
         null);

   private static final long serialVersionUID = 1L;

   private String _name;
   private Set<Ip> _neighborAddresses;

   public NamedBgpPeerGroup(String name) {
      _neighborAddresses = new LinkedHashSet<Ip>();
      _routeReflectorClient = false;
      _defaultOriginate = false;
      _sendCommunity = false;
      _name = name;
   }

   public void addNeighborAddress(Ip address) {
      _neighborAddresses.add(address);
   }

   @Override
   public String getName() {
      return _name;
   }

   public Set<Ip> getNeighborAddresses() {
      return _neighborAddresses;
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string + "\n");
         System.out.println("+ " + string + "._name:" + Util.objectToString(_name) + "\n");
         Util.diffRepresentationSets(null, _neighborAddresses, string + "._neighborAddresses");
         
         System.out.println("+ " + string + "._clusterId:" + Util.objectToString(_clusterId) + "\n");
         System.out.println("+ " + string + "._defaultOriginate:" + Util.objectToString(_defaultOriginate) + "\n");
         System.out.println("+ " + string + "._defaultOriginateMap:" + Util.objectToString(_defaultOriginateMap) + "\n");
         System.out.println("+ " + string + "._inboundPrefixList:" + Util.objectToString(_inboundPrefixList) + "\n");
         System.out.println("+ " + string + "._inboundRouteMap:" + Util.objectToString(_inboundRouteMap) + "\n");
         System.out.println("+ " + string + "._outboundPrefixList:" + Util.objectToString(_outboundPrefixList) + "\n");
         System.out.println("+ " + string + "._outboundRouteMap:" + Util.objectToString(_outboundRouteMap) + "\n");
         System.out.println("+ " + string + "._remoteAS:" + Util.objectToString(_remoteAS) + "\n");
         System.out.println("+ " + string + "._routeReflectorClient:" + Util.objectToString(_routeReflectorClient) + "\n");
         System.out.println("+ " + string + "._sendCommunity:" + Util.objectToString(_sendCommunity) + "\n");
         System.out.println("+ " + string + "._updateSource:" + Util.objectToString(_updateSource) + "\n");
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string + "\n");
         System.out.println("- " + string + "._name:" + Util.objectToString(_name) + "\n");
         Util.diffRepresentationSets(_neighborAddresses, null, string + "._neighborAddresses");
         
         System.out.println("- " + string + "._clusterId:" + Util.objectToString(_clusterId) + "\n");
         System.out.println("- " + string + "._defaultOriginate:" + Util.objectToString(_defaultOriginate) + "\n");
         System.out.println("- " + string + "._defaultOriginateMap:" + Util.objectToString(_defaultOriginateMap) + "\n");
         System.out.println("- " + string + "._inboundPrefixList:" + Util.objectToString(_inboundPrefixList) + "\n");
         System.out.println("- " + string + "._inboundRouteMap:" + Util.objectToString(_inboundRouteMap) + "\n");
         System.out.println("- " + string + "._outboundPrefixList:" + Util.objectToString(_outboundPrefixList) + "\n");
         System.out.println("- " + string + "._outboundRouteMap:" + Util.objectToString(_outboundRouteMap) + "\n");
         System.out.println("- " + string + "._remoteAS:" + Util.objectToString(_remoteAS) + "\n");
         System.out.println("- " + string + "._routeReflectorClient:" + Util.objectToString(_routeReflectorClient) + "\n");
         System.out.println("- " + string + "._sendCommunity:" + Util.objectToString(_sendCommunity) + "\n");
         System.out.println("- " + string + "._updateSource:" + Util.objectToString(_updateSource) + "\n");
         System.out.flush();
         return;
      }

      NamedBgpPeerGroup rhs = (NamedBgpPeerGroup) o;
      if (!Util.equalOrNull(_name, rhs._name)) {
         System.out.println("- " + string + "._name:" + Util.objectToString(_name) + "\n");
         System.out.println("+ " + string + "._name:" + Util.objectToString(rhs._name) + "\n");
      }
      Util.diffRepresentationSets(_neighborAddresses, rhs._neighborAddresses, string + "._neighborAddresses");
      
      if (!Util.equalOrNull(_clusterId, rhs._clusterId)) {
         System.out.println("- " + string + "._clusterId:" + Util.objectToString(_clusterId) + "\n");
         System.out.println("+ " + string + "._clusterId:" + Util.objectToString(rhs._clusterId) + "\n");
      }
      if (!Util.equalOrNull(_defaultOriginate, rhs._defaultOriginate)) {
         System.out.println("- " + string + "._defaultOriginate:" + Util.objectToString(_defaultOriginate) + "\n");
         System.out.println("+ " + string + "._defaultOriginate:" + Util.objectToString(rhs._defaultOriginate) + "\n");
      }
      if (!Util.equalOrNull(_defaultOriginateMap, rhs._defaultOriginateMap)) {
         System.out.println("- " + string + "._defaultOriginateMap:" + Util.objectToString(_defaultOriginateMap) + "\n");
         System.out.println("+ " + string + "._defaultOriginateMap:" + Util.objectToString(rhs._defaultOriginateMap) + "\n");
      }
      if (!Util.equalOrNull(_inboundPrefixList, rhs._inboundPrefixList)) {
         System.out.println("- " + string + "._inboundPrefixList:" + Util.objectToString(_inboundPrefixList) + "\n");
         System.out.println("+ " + string + "._inboundPrefixList:" + Util.objectToString(rhs._inboundPrefixList) + "\n");
      }
      if (!Util.equalOrNull(_inboundRouteMap, rhs._inboundRouteMap)) {
         System.out.println("- " + string + "._inboundRouteMap:" + Util.objectToString(_inboundRouteMap) + "\n");
         System.out.println("+ " + string + "._inboundRouteMap:" + Util.objectToString(rhs._inboundRouteMap) + "\n");
      }
      if (!Util.equalOrNull(_outboundPrefixList, rhs._outboundPrefixList)) {
         System.out.println("- " + string + "._outboundPrefixList:" + Util.objectToString(_outboundPrefixList) + "\n");
         System.out.println("+ " + string + "._outboundPrefixList:" + Util.objectToString(rhs._outboundPrefixList) + "\n");
      }
      if (!Util.equalOrNull(_outboundRouteMap, rhs._outboundRouteMap)) {
         System.out.println("- " + string + "._outboundRouteMap:" + Util.objectToString(_outboundRouteMap) + "\n");
         System.out.println("+ " + string + "._outboundRouteMap:" + Util.objectToString(rhs._outboundRouteMap) + "\n");
      }
      if (!Util.equalOrNull(_remoteAS, rhs._remoteAS)) {
         System.out.println("- " + string + "._remoteAS:" + Util.objectToString(_remoteAS) + "\n");
         System.out.println("+ " + string + "._remoteAS:" + Util.objectToString(rhs._remoteAS) + "\n");
      }
      if (!Util.equalOrNull(_routeReflectorClient, rhs._routeReflectorClient)) {
         System.out.println("- " + string + "._routeReflectorClient:" + Util.objectToString(_routeReflectorClient) + "\n");
         System.out.println("+ " + string + "._routeReflectorClient:" + Util.objectToString(rhs._routeReflectorClient) + "\n");
      }
      if (!Util.equalOrNull(_sendCommunity, rhs._sendCommunity)) {
         System.out.println("- " + string + "._sendCommunity:" + Util.objectToString(_sendCommunity) + "\n");
         System.out.println("+ " + string + "._sendCommunity:" + Util.objectToString(rhs._sendCommunity) + "\n");
      }
      if (!Util.equalOrNull(_updateSource, rhs._updateSource)) {
         System.out.println("- " + string + "._updateSource:" + Util.objectToString(_updateSource) + "\n");
         System.out.println("+ " + string + "._updateSource:" + Util.objectToString(rhs._updateSource) + "\n");
      }
      System.out.flush();
      return;
   }

}
