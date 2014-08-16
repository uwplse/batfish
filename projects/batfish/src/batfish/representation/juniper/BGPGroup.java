package batfish.representation.juniper;

import java.util.ArrayList;
import java.util.List;

import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class BGPGroup implements RepresentationObject {
   private Long _clusterId;
   private String _name;
   private Integer _localAS;
   private boolean _routeReflectorClient;
   private List<BGPNeighbor> _neighbors;
   private boolean _isExternal;

   // not used at this moment
   private Integer _remoteAS;
   private List<String> _inboundPolicyStatementName;
   private List<String> _outboundPolicyStatementName;
   private String _updateSource;

   public BGPGroup(String name) {
      _name = name;
      _routeReflectorClient = false;
      _inboundPolicyStatementName = null;
      _outboundPolicyStatementName = null;
      _clusterId = null;
      _updateSource = null;
      _neighbors = new ArrayList<BGPNeighbor>();
      _isExternal = true;
   }

   public boolean getRouteReflectorClient() {
      return _routeReflectorClient;
   }

   public void setRemoteAS(int remoteAS) {
      _remoteAS = remoteAS;
   }

   public void setLocalAS(int localAS) {
      _localAS = localAS;
   }

   public void addNeighbor(BGPNeighbor n) {
      _neighbors.add(n);
   }

   public void setIsExternal(boolean e) {
      _isExternal = e;
   }

   public void setInboundPolicyStatement(List<String> name) {
      _inboundPolicyStatementName = name;
   }

   public void setOutboundPolicyStatement(List<String> name) {
      _outboundPolicyStatementName = name;
   }

   public Long getClusterId() {
      return _clusterId;
   }

   public boolean getIsExternal() {
      return _isExternal;
   }

   public Integer getRemoteAS() {
      return _remoteAS;
   }

   public Integer getLocalAS() {
      return _localAS;
   }

   public String getName() {
      return _name;
   }

   public List<String> getInboundPolicyStatement() {
      return _inboundPolicyStatementName;
   }

   public List<String> getOutboundPolicyStatement() {
      return _outboundPolicyStatementName;
   }

   public String getUpdateSource() {
      return _updateSource;
   }

   public List<BGPNeighbor> getNeighbors() {
      return _neighbors;
   }

   public void setRouteReflectorClient() {
      _routeReflectorClient = true;
   }

   public void setClusterId(Long clusterId) {
      _clusterId = clusterId;
   }

   public void setUpdateSource(String updateSource) {
      _updateSource = updateSource;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      BGPGroup rhs = (BGPGroup) o;
      return Util.equalOrNull(_clusterId, rhs._clusterId)
            && Util.equalOrNull(_name, rhs._name)
            && Util.equalOrNull(_localAS, rhs._localAS)
            && _routeReflectorClient == rhs._routeReflectorClient
            && (Util.cmpRepresentationLists(_neighbors, rhs._neighbors) == 0)
            && _isExternal == rhs._isExternal;
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string);
         System.out.println("+ " + string + "._clusterId:"
               + Util.objectToString(_clusterId));
         System.out.println("+ " + string + "._name:"
               + Util.objectToString(_name));
         System.out.println("+ " + string + "._localAS:"
               + Util.objectToString(_localAS));
         System.out.println("+ " + string + "._routeReflectorClient:"
               + _routeReflectorClient);
         Util.diffRepresentationLists(null, _neighbors, string + "._neighbors");
         System.out.println("+ " + string + "._isExternal:" + _isExternal);
         return;
      }

      if (o == null) {
         System.out.println("- " + string);
         System.out.println("- " + string + "._clusterId:"
               + Util.objectToString(_clusterId));
         System.out.println("- " + string + "._name:"
               + Util.objectToString(_name));
         System.out.println("- " + string + "._localAS:"
               + Util.objectToString(_localAS));
         System.out.println("- " + string + "._routeReflectorClient:"
               + _routeReflectorClient);
         Util.diffRepresentationLists(_neighbors, null, string + "._neighbors");
         System.out.println("- " + string + "._isExternal:" + _isExternal);
         return;
      }

      BGPGroup rhs = (BGPGroup) o;
      if (!Util.equalOrNull(_clusterId, rhs._clusterId)) {
         System.out.println("- " + string + "._clusterId:"
               + Util.objectToString(_clusterId));
         System.out.println("+ " + string + "._clusterId:"
               + Util.objectToString(rhs._clusterId));
      }
      if (!Util.equalOrNull(_name, rhs._name)) {
         System.out.println("- " + string + "._name:"
               + Util.objectToString(_name));
         System.out.println("+ " + string + "._name:"
               + Util.objectToString(rhs._name));
      }
      if (!Util.equalOrNull(_localAS, rhs._localAS)) {
         System.out.println("- " + string + "._localAS:"
               + Util.objectToString(_localAS));
         System.out.println("+ " + string + "._localAS:"
               + Util.objectToString(rhs._localAS));
      }
      if (!Util.equalOrNull(_routeReflectorClient, rhs._routeReflectorClient)) {
         System.out.println("- " + string + "._routeReflectorClient:"
               + Util.objectToString(_routeReflectorClient));
         System.out.println("+ " + string + "._routeReflectorClient:"
               + Util.objectToString(rhs._routeReflectorClient));
      }

      Util.diffRepresentationLists(_neighbors, rhs._neighbors, string
            + "._neighbors");

      if (_isExternal != rhs._isExternal) {
         System.out.println("- " + string + "._isExternal:" + _isExternal);
         System.out.println("+ " + string + "._isExternal:" + rhs._isExternal);
      }
      System.out.flush();
      return;
   }

}
