package batfish.representation.cisco;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import batfish.representation.Ip;
import batfish.representation.Protocol;
import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class BgpProcess implements Serializable, RepresentationObject {

   private static final long serialVersionUID = 1L;

   private Set<Ip> _activatedNeighbors;
   private Map<BgpNetwork, Boolean> _aggregateNetworks;
   private Map<String, BgpPeerGroup> _allPeerGroups;
   private Ip _clusterId;
   private int _defaultMetric;
   private boolean _defaultNeighborActivate;
   private Map<Ip, IpBgpPeerGroup> _ipPeerGroups;
   private Map<String, NamedBgpPeerGroup> _namedPeerGroups;
   private Set<BgpNetwork> _networks;
   private int _pid;
   private Map<Protocol, BgpRedistributionPolicy> _redistributionPolicies;
   private Ip _routerId;
   private Set<String> _shutdownNeighbors;

   public BgpProcess(int procnum) {
      _pid = procnum;
      _allPeerGroups = new HashMap<String, BgpPeerGroup>();
      _namedPeerGroups = new HashMap<String, NamedBgpPeerGroup>();
      _ipPeerGroups = new HashMap<Ip, IpBgpPeerGroup>();
      _networks = new LinkedHashSet<BgpNetwork>();
      _activatedNeighbors = new LinkedHashSet<Ip>();
      _defaultNeighborActivate = true;
      _aggregateNetworks = new HashMap<BgpNetwork, Boolean>();
      _shutdownNeighbors = new LinkedHashSet<String>();
      _clusterId = null;
      _redistributionPolicies = new EnumMap<Protocol, BgpRedistributionPolicy>(
            Protocol.class);
   }

   public void addActivatedNeighbor(Ip address) {
      if (!(_shutdownNeighbors.contains(address))) {
         _activatedNeighbors.add(address);
      }
   }

   public void addDefaultOriginateNeighbor(String neighbor, String routeMapName) {
      BgpPeerGroup pg = _allPeerGroups.get(neighbor);
      pg.setDefaultOriginate(true);
      pg.setDefaultOriginateMap(routeMapName);
   }

   public void addDefaultOriginateNeighbors(
         Map<String, String> defaultOriginateNeighbors) {
      for (String neighbor : defaultOriginateNeighbors.keySet()) {
         String routeMapName = defaultOriginateNeighbors.get(neighbor);
         addDefaultOriginateNeighbor(neighbor, routeMapName);
      }
   }

   public void addIpPeerGroup(Ip ip) {
      IpBgpPeerGroup pg = new IpBgpPeerGroup(ip);
      if (_defaultNeighborActivate) {
         addActivatedNeighbor(ip);
      }
      _ipPeerGroups.put(ip, pg);
      _allPeerGroups.put(ip.toString(), pg);
   }

   public void addNamedPeerGroup(String name) {
      NamedBgpPeerGroup pg = new NamedBgpPeerGroup(name);
      _namedPeerGroups.put(name, pg);
      _allPeerGroups.put(name, pg);
   }

   public void addPeerGroupInboundPrefixList(String peerGroupName,
         String listName) {
      BgpPeerGroup pg = _allPeerGroups.get(peerGroupName);
      if (pg != null) {
         pg.setInboundPrefixList(listName);
      }
      else {
         throw new Error("Peer group: \"" + peerGroupName
               + "\" does not exist!");
      }
   }

   public void addPeerGroupInboundRouteMap(String peerGroupName, String mapName) {
      BgpPeerGroup pg = _allPeerGroups.get(peerGroupName);
      if (pg != null) {
         pg.setInboundRouteMap(mapName);
      }
      else {
         throw new Error("Peer group: \"" + peerGroupName
               + "\" does not exist!");
      }
   }

   public void addPeerGroupMember(Ip address, String namedPeerGroupName) {
      NamedBgpPeerGroup namedPeerGroup = _namedPeerGroups
            .get(namedPeerGroupName);
      if (namedPeerGroup != null) {
         namedPeerGroup.addNeighborAddress(address);
         IpBgpPeerGroup ipPeerGroup = _ipPeerGroups.get(address);
         if (ipPeerGroup == null) {
            addIpPeerGroup(address);
            ipPeerGroup = _ipPeerGroups.get(address);
         }
         ipPeerGroup.setGroupName(namedPeerGroupName);
      }
      else {
         throw new Error("Peer group: \"" + namedPeerGroupName
               + "\" does not exist!");
      }
   }

   public void addPeerGroupOutboundPrefixList(String peerGroupName,
         String listName) {
      BgpPeerGroup pg = _allPeerGroups.get(peerGroupName);
      if (pg != null) {
         pg.setOutboundPrefixList(listName);
      }
      else {
         throw new Error("Peer group: \"" + peerGroupName
               + "\" does not exist!");
      }
   }

   public void addPeerGroupOutboundRouteMap(String peerGroupName, String mapName) {
      BgpPeerGroup pg = _allPeerGroups.get(peerGroupName);
      if (pg != null) {
         pg.setOutboundRouteMap(mapName);
      }
      else {
         throw new Error("Peer group: \"" + peerGroupName
               + "\" does not exist!");
      }
   }

   public void addPeerGroupRouteReflectorClient(String peerGroupName) {
      BgpPeerGroup pg = _allPeerGroups.get(peerGroupName);
      if (pg != null) {
         pg.setRouteReflectorClient();
      }
      else {
         throw new Error("Peer group: \"" + peerGroupName
               + "\" does not exist!");
      }
   }

   public void addPeerGroupRouteReflectorClients(List<String> rrcPeerGroups) {
      for (String peerGroupName : rrcPeerGroups) {
         addPeerGroupRouteReflectorClient(peerGroupName);
      }
   }

   public void addSendCommunityPeerGroup(String peerGroupName) {
      BgpPeerGroup pg = _allPeerGroups.get(peerGroupName);
      if (pg != null) {
         pg.setSendCommunity(true);
      }
      else {
         throw new Error("Peer group: \"" + peerGroupName
               + "\" does not exist!");
      }
   }

   public void addShutDownNeighbor(String peerGroupName) {
      _shutdownNeighbors.add(peerGroupName);
   }

   public Set<Ip> getActivatedNeighbors() {
      return _activatedNeighbors;
   }

   public Map<BgpNetwork, Boolean> getAggregateNetworks() {
      return _aggregateNetworks;
   }

   public Map<String, BgpPeerGroup> getAllPeerGroups() {
      return _allPeerGroups;
   }

   public Ip getClusterId() {
      return _clusterId;
   }

   public int getDefaultMetric() {
      return _defaultMetric;
   }

   public boolean getDefaultNeighborActivate() {
      return _defaultNeighborActivate;
   }

   public Map<Ip, IpBgpPeerGroup> getIpPeerGroups() {
      return _ipPeerGroups;
   }

   public Map<String, NamedBgpPeerGroup> getNamedPeerGroups() {
      return _namedPeerGroups;
   }

   public Set<BgpNetwork> getNetworks() {
      return _networks;
   }

   public BgpPeerGroup getPeerGroup(String name) {
      return _allPeerGroups.get(name);
   }

   public int getPid() {
      return _pid;
   }

   public Map<Protocol, BgpRedistributionPolicy> getRedistributionPolicies() {
      return _redistributionPolicies;
   }

   public Ip getRouterId() {
      return _routerId;
   }

   public Set<String> getShutdownNeighbors() {
      return _shutdownNeighbors;
   }

   public void setClusterId(Ip clusterId) {
      _clusterId = clusterId;
   }

   public void setDefaultMetric(int defaultMetric) {
      _defaultMetric = defaultMetric;
   }

   public void setPeerGroupUpdateSource(String peerGroupName, String source) {
      BgpPeerGroup pg = _allPeerGroups.get(peerGroupName);
      if (pg != null) {
         pg.setUpdateSource(source);
      }
      else {
         throw new Error("Peer group: \"" + peerGroupName
               + "\" does not exist!");
      }
   }

   public void setRouterId(Ip routerId) {
      _routerId = routerId;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string);
         Util.diffRepresentationSets(null, _activatedNeighbors, string
               + "._activatedNeighbors");
         Util.diffRepresentationMaps(null, _aggregateNetworks, string
               + "._aggregateNetworks");
         System.out.println("+ " + string + "._clusterId:"
               + Util.objectToString(_clusterId));
         System.out
               .println("+ " + string + "._defaultMetric:" + _defaultMetric);
         System.out.println("+ " + string + "._defaultNeighborActivate:"
               + _defaultNeighborActivate);
         Util.diffRepresentationMaps(null, _ipPeerGroups, string
               + "._ipPeerGroups");
         Util.diffRepresentationMaps(null, _namedPeerGroups, string
               + "._namedPeerGroups");
         Util.diffRepresentationSets(null, _networks, string + "._networks");
         System.out.println("+ " + string + "._pid:" + _pid);
         Util.diffRepresentationMaps(null, _redistributionPolicies, string
               + "._redistributionPolicies");
         System.out.println("+ " + string + "._routerId:"
               + Util.objectToString(_routerId));
         Util.diffRepresentationSets(null, _shutdownNeighbors, string
               + "._shutdownNeighbors");
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string);
         Util.diffRepresentationSets(_activatedNeighbors, null, string
               + "._activatedNeighbors");
         Util.diffRepresentationMaps(_aggregateNetworks, null, string
               + "._aggregateNetworks");
         System.out.println("- " + string + "._clusterId:"
               + Util.objectToString(_clusterId));
         System.out
               .println("- " + string + "._defaultMetric:" + _defaultMetric);
         System.out.println("- " + string + "._defaultNeighborActivate:"
               + _defaultNeighborActivate);
         Util.diffRepresentationMaps(_ipPeerGroups, null, string
               + "._ipPeerGroups");
         Util.diffRepresentationMaps(_namedPeerGroups, null, string
               + "._namedPeerGroups");
         Util.diffRepresentationSets(_networks, null, string + "._networks");
         System.out.println("- " + string + "._pid:" + _pid);
         Util.diffRepresentationMaps(_redistributionPolicies, null, string
               + "._redistributionPolicies");
         System.out.println("- " + string + "._routerId:"
               + Util.objectToString(_routerId));
         Util.diffRepresentationSets(_shutdownNeighbors, null, string
               + "._shutdownNeighbors");
         System.out.flush();
         return;
      }

      BgpProcess rhs = (BgpProcess) o;
      Util.diffRepresentationSets(_activatedNeighbors, rhs._activatedNeighbors,
            string + "._activatedNeighbors");
      Util.diffRepresentationMaps(_aggregateNetworks, rhs._aggregateNetworks,
            string + "._aggregateNetworks");
      if (!Util.equalOrNull(_clusterId, rhs._clusterId)) {
         System.out.println("- " + string + "._clusterId:"
               + Util.objectToString(_clusterId));
         System.out.println("+ " + string + "._clusterId:"
               + Util.objectToString(rhs._clusterId));
      }
      if (_defaultMetric != rhs._defaultMetric) {
         System.out
               .println("- " + string + "._defaultMetric:" + _defaultMetric);
         System.out.println("+ " + string + "._defaultMetric:"
               + rhs._defaultMetric);
      }
      if (_defaultNeighborActivate != rhs._defaultNeighborActivate) {
         System.out.println("- " + string + "._defaultNeighborActivate:"
               + _defaultNeighborActivate);
         System.out.println("+ " + string + "._defaultNeighborActivate:"
               + rhs._defaultNeighborActivate);
      }
      Util.diffRepresentationMaps(_ipPeerGroups, rhs._ipPeerGroups, string
            + "._ipPeerGroups");
      Util.diffRepresentationMaps(_namedPeerGroups, rhs._namedPeerGroups,
            string + "._namedPeerGroups");
      Util.diffRepresentationSets(_networks, rhs._networks, string
            + "._networks");
      if (_pid != rhs._pid) {
         System.out.println("- " + string + "._pid:" + _pid);
         System.out.println("+ " + string + "._pid:" + rhs._pid);
      }
      Util.diffRepresentationMaps(_redistributionPolicies,
            rhs._redistributionPolicies, string + "._redistributionPolicies");
      if (!Util.equalOrNull(_routerId, rhs._routerId)) {
         System.out.println("- " + string + "._routerId:"
               + Util.objectToString(_routerId));
         System.out.println("+ " + string + "._routerId:"
               + Util.objectToString(rhs._routerId));
      }
      Util.diffRepresentationSets(_shutdownNeighbors, rhs._shutdownNeighbors,
            string + "._shutdownNeighbors");
      System.out.flush();
      return;
   }

}
