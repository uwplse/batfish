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
            && Util.cmpRepresentationLists(_outboundPolicyStatementName,
                  rhs._outboundPolicyStatementName) == 0;
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string);
         System.out.println("+ " + string + "._ip:" + Util.objectToString(_ip));
         System.out.println("+ " + string + "._remoteAS:"
               + Util.objectToString(_remoteAS));
         System.out.println("+ " + string + "._localAS:"
               + Util.objectToString(_localAS));
         Util.diffRepresentationLists(null, _inboundPolicyStatementName, string
               + "._inboundPolicyStatementName");
         Util.diffRepresentationLists(null, _outboundPolicyStatementName,
               string + "._outboundPolicyStatementName");
         return;
      }

      if (o == null) {
         System.out.println("- " + string);
         System.out.println("- " + string + "._ip:" + Util.objectToString(_ip));
         System.out.println("- " + string + "._remoteAS:"
               + Util.objectToString(_remoteAS));
         System.out.println("- " + string + "._localAS:"
               + Util.objectToString(_localAS));
         Util.diffRepresentationLists(_inboundPolicyStatementName, null, string
               + "._inboundPolicyStatementName");
         Util.diffRepresentationLists(_outboundPolicyStatementName, null,
               string + "._outboundPolicyStatementName");
         return;
      }

      BGPNeighbor rhs = (BGPNeighbor) o;
      if (!Util.equalOrNull(_ip, rhs._ip)) {
         System.out.println("- " + string + "._ip:" + Util.objectToString(_ip));
         System.out.println("+ " + string + "._ip:"
               + Util.objectToString(rhs._ip));
      }
      if (!Util.equalOrNull(_remoteAS, rhs._remoteAS)) {
         System.out.println("- " + string + "._remoteAS:"
               + Util.objectToString(_remoteAS));
         System.out.println("+ " + string + "._remoteAS:"
               + Util.objectToString(rhs._remoteAS));
      }
      if (!Util.equalOrNull(_localAS, rhs._localAS)) {
         System.out.println("- " + string + "._localAS:"
               + Util.objectToString(_localAS));
         System.out.println("+ " + string + "._localAS:"
               + Util.objectToString(rhs._localAS));
      }

      Util.diffRepresentationLists(_inboundPolicyStatementName,
            rhs._inboundPolicyStatementName, string
                  + "._inboundPolicyStatementName");
      Util.diffRepresentationLists(_outboundPolicyStatementName,
            rhs._outboundPolicyStatementName, string
                  + "._outboundPolicyStatementName");

      System.out.flush();
      return;
   }

}
