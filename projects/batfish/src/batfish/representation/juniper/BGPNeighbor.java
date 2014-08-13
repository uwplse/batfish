package batfish.representation.juniper;

import java.util.ArrayList;
import java.util.List;

import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class BGPNeighbor implements RepresentationObject {
   private String _ip;
   private Integer _remoteAS;
   private Integer _localAS;
   private List<String> _inboundPolicyStatementName;
   private List<String> _outboundPolicyStatementName;

   // not used at this moment
   private String _updateSource;

   public BGPNeighbor(String ip) {
      _ip = ip;
      _inboundPolicyStatementName = new ArrayList<String>();
      _outboundPolicyStatementName = new ArrayList<String>();
      _updateSource = null;
   }

   public void setRemoteAS(int remoteAS) {
      _remoteAS = remoteAS;
   }

   public void setLocalAS(int localAS) {
      _localAS = localAS;
   }

   public void setInboundPolicyStatement(List<String> name) {
      _inboundPolicyStatementName.addAll(name);
   }

   public void setOutboundPolicyStatement(List<String> name) {
      _outboundPolicyStatementName.addAll(name);
   }

   public Integer getRemoteAS() {
      return _remoteAS;
   }

   public Integer getLocalAS() {
      return _localAS;
   }

   public String getIP() {
      return _ip;
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

   public void setUpdateSource(String updateSource) {
      _updateSource = updateSource;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      BGPNeighbor rhs = (BGPNeighbor) o;
      return Util.equalOrNull(_ip, rhs._ip)
            && Util.equalOrNull(_remoteAS, rhs._remoteAS)
            && Util.equalOrNull(_localAS, rhs._localAS)
            && Util.cmpRepresentationLists(_inboundPolicyStatementName,
                  rhs._inboundPolicyStatementName) == 0
            && Util.equalOrNull(_outboundPolicyStatementName,
                  rhs._outboundPolicyStatementName);
   }

}
