package batfish.representation.cisco;

import batfish.representation.Ip;
import batfish.util.Util;

public class IpBgpPeerGroup extends BgpPeerGroup {

   private static final long serialVersionUID = 1L;

   private String _groupName;
   private Ip _ip;

   public IpBgpPeerGroup(Ip ip) {
      _ip = ip;
   }

   public String getGroupName() {
      return _groupName;
   }

   public Ip getIp() {
      return _ip;
   }

   @Override
   public String getName() {
      return _ip.toString();
   }

   public void setGroupName(String name) {
      _groupName = name;
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string);
         System.out.println("+ " + string + "._groupName:"
               + Util.objectToString(_groupName));
         System.out.println("+ " + string + "._ip:" + Util.objectToString(_ip));

         System.out.println("+ " + string + "._clusterId:"
               + Util.objectToString(_clusterId));
         System.out.println("+ " + string + "._defaultOriginate:"
               + Util.objectToString(_defaultOriginate));
         System.out.println("+ " + string + "._defaultOriginateMap:"
               + Util.objectToString(_defaultOriginateMap));
         System.out.println("+ " + string + "._inboundPrefixList:"
               + Util.objectToString(_inboundPrefixList));
         System.out.println("+ " + string + "._inboundRouteMap:"
               + Util.objectToString(_inboundRouteMap));
         System.out.println("+ " + string + "._outboundPrefixList:"
               + Util.objectToString(_outboundPrefixList));
         System.out.println("+ " + string + "._outboundRouteMap:"
               + Util.objectToString(_outboundRouteMap));
         System.out.println("+ " + string + "._remoteAS:"
               + Util.objectToString(_remoteAS));
         System.out.println("+ " + string + "._routeReflectorClient:"
               + Util.objectToString(_routeReflectorClient));
         System.out.println("+ " + string + "._sendCommunity:"
               + Util.objectToString(_sendCommunity));
         System.out.println("+ " + string + "._updateSource:"
               + Util.objectToString(_updateSource));
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string);
         System.out.println("- " + string + "._groupName:"
               + Util.objectToString(_groupName));
         System.out.println("- " + string + "._ip:" + Util.objectToString(_ip));

         System.out.println("- " + string + "._clusterId:"
               + Util.objectToString(_clusterId));
         System.out.println("- " + string + "._defaultOriginate:"
               + Util.objectToString(_defaultOriginate));
         System.out.println("- " + string + "._defaultOriginateMap:"
               + Util.objectToString(_defaultOriginateMap));
         System.out.println("- " + string + "._inboundPrefixList:"
               + Util.objectToString(_inboundPrefixList));
         System.out.println("- " + string + "._inboundRouteMap:"
               + Util.objectToString(_inboundRouteMap));
         System.out.println("- " + string + "._outboundPrefixList:"
               + Util.objectToString(_outboundPrefixList));
         System.out.println("- " + string + "._outboundRouteMap:"
               + Util.objectToString(_outboundRouteMap));
         System.out.println("- " + string + "._remoteAS:"
               + Util.objectToString(_remoteAS));
         System.out.println("- " + string + "._routeReflectorClient:"
               + Util.objectToString(_routeReflectorClient));
         System.out.println("- " + string + "._sendCommunity:"
               + Util.objectToString(_sendCommunity));
         System.out.println("- " + string + "._updateSource:"
               + Util.objectToString(_updateSource));
         System.out.flush();
         return;
      }

      IpBgpPeerGroup rhs = (IpBgpPeerGroup) o;
      if (!Util.equalOrNull(_groupName, rhs._groupName)) {
         System.out.println("- " + string + "._groupName:"
               + Util.objectToString(_groupName));
         System.out.println("+ " + string + "._groupName:"
               + Util.objectToString(rhs._groupName));
      }
      if (!Util.equalOrNull(_ip, rhs._ip)) {
         System.out.println("- " + string + "._ip:" + Util.objectToString(_ip));
         System.out.println("+ " + string + "._ip:"
               + Util.objectToString(rhs._ip));
      }

      if (!Util.equalOrNull(_clusterId, rhs._clusterId)) {
         System.out.println("- " + string + "._clusterId:"
               + Util.objectToString(_clusterId));
         System.out.println("+ " + string + "._clusterId:"
               + Util.objectToString(rhs._clusterId));
      }
      if (!Util.equalOrNull(_defaultOriginate, rhs._defaultOriginate)) {
         System.out.println("- " + string + "._defaultOriginate:"
               + Util.objectToString(_defaultOriginate));
         System.out.println("+ " + string + "._defaultOriginate:"
               + Util.objectToString(rhs._defaultOriginate));
      }
      if (!Util.equalOrNull(_defaultOriginateMap, rhs._defaultOriginateMap)) {
         System.out.println("- " + string + "._defaultOriginateMap:"
               + Util.objectToString(_defaultOriginateMap));
         System.out.println("+ " + string + "._defaultOriginateMap:"
               + Util.objectToString(rhs._defaultOriginateMap));
      }
      if (!Util.equalOrNull(_inboundPrefixList, rhs._inboundPrefixList)) {
         System.out.println("- " + string + "._inboundPrefixList:"
               + Util.objectToString(_inboundPrefixList));
         System.out.println("+ " + string + "._inboundPrefixList:"
               + Util.objectToString(rhs._inboundPrefixList));
      }
      if (!Util.equalOrNull(_inboundRouteMap, rhs._inboundRouteMap)) {
         System.out.println("- " + string + "._inboundRouteMap:"
               + Util.objectToString(_inboundRouteMap));
         System.out.println("+ " + string + "._inboundRouteMap:"
               + Util.objectToString(rhs._inboundRouteMap));
      }
      if (!Util.equalOrNull(_outboundPrefixList, rhs._outboundPrefixList)) {
         System.out.println("- " + string + "._outboundPrefixList:"
               + Util.objectToString(_outboundPrefixList));
         System.out.println("+ " + string + "._outboundPrefixList:"
               + Util.objectToString(rhs._outboundPrefixList));
      }
      if (!Util.equalOrNull(_outboundRouteMap, rhs._outboundRouteMap)) {
         System.out.println("- " + string + "._outboundRouteMap:"
               + Util.objectToString(_outboundRouteMap));
         System.out.println("+ " + string + "._outboundRouteMap:"
               + Util.objectToString(rhs._outboundRouteMap));
      }
      if (!Util.equalOrNull(_remoteAS, rhs._remoteAS)) {
         System.out.println("- " + string + "._remoteAS:"
               + Util.objectToString(_remoteAS));
         System.out.println("+ " + string + "._remoteAS:"
               + Util.objectToString(rhs._remoteAS));
      }
      if (!Util.equalOrNull(_routeReflectorClient, rhs._routeReflectorClient)) {
         System.out.println("- " + string + "._routeReflectorClient:"
               + Util.objectToString(_routeReflectorClient));
         System.out.println("+ " + string + "._routeReflectorClient:"
               + Util.objectToString(rhs._routeReflectorClient));
      }
      if (!Util.equalOrNull(_sendCommunity, rhs._sendCommunity)) {
         System.out.println("- " + string + "._sendCommunity:"
               + Util.objectToString(_sendCommunity));
         System.out.println("+ " + string + "._sendCommunity:"
               + Util.objectToString(rhs._sendCommunity));
      }
      if (!Util.equalOrNull(_updateSource, rhs._updateSource)) {
         System.out.println("- " + string + "._updateSource:"
               + Util.objectToString(_updateSource));
         System.out.println("+ " + string + "._updateSource:"
               + Util.objectToString(rhs._updateSource));
      }
      System.out.flush();
      return;
   }

}
