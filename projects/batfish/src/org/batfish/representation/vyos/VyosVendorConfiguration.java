package org.batfish.representation.vyos;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.batfish.common.BatfishException;
import org.batfish.common.VendorConversionException;
import org.batfish.datamodel.Configuration;
import org.batfish.datamodel.ConfigurationFormat;
import org.batfish.datamodel.IkeGateway;
import org.batfish.datamodel.IkePolicy;
import org.batfish.datamodel.Ip;
import org.batfish.datamodel.IpsecPolicy;
import org.batfish.datamodel.IpsecProposal;
import org.batfish.datamodel.IpsecProtocol;
import org.batfish.datamodel.IpsecVpn;
import org.batfish.datamodel.LineAction;
import org.batfish.datamodel.PolicyMap;
import org.batfish.datamodel.PolicyMapAction;
import org.batfish.datamodel.PolicyMapClause;
import org.batfish.datamodel.Prefix;
import org.batfish.datamodel.RouteFilterLine;
import org.batfish.datamodel.RouteFilterList;
import org.batfish.main.Warnings;
import org.batfish.representation.VendorConfiguration;

public class VyosVendorConfiguration extends VyosConfiguration implements
      VendorConfiguration {

   /**
    *
    */
   private static final long serialVersionUID = 1L;

   private Configuration _c;

   private ConfigurationFormat _format;

   private transient Map<Ip, org.batfish.datamodel.Interface> _ipToInterfaceMap;

   private transient Set<String> _unimplementedFeatures;

   private transient Warnings _w;

   private void convertInterfaces() {
      for (Entry<String, Interface> e : _interfaces.entrySet()) {
         String name = e.getKey();
         Interface iface = e.getValue();
         org.batfish.datamodel.Interface newIface = toInterface(iface);
         _c.getInterfaces().put(name, newIface);
      }
   }

   private void convertPrefixLists() {
      for (Entry<String, PrefixList> e : _prefixLists.entrySet()) {
         String name = e.getKey();
         PrefixList prefixList = e.getValue();
         RouteFilterList routeFilterList = toRouteFilterList(prefixList);
         _c.getRouteFilterLists().put(name, routeFilterList);
      }
   }

   private void convertRouteMaps() {
      for (Entry<String, RouteMap> e : _routeMaps.entrySet()) {
         String name = e.getKey();
         RouteMap routeMap = e.getValue();
         PolicyMap policyMap = toPolicyMap(routeMap);
         _c.getPolicyMaps().put(name, policyMap);
      }
   }

   private void convertVpns() {
      for (Entry<Ip, IpsecPeer> ipsecPeerEntry : _ipsecPeers.entrySet()) {
         // create ipsecvpn and ikegateway to correspond roughly to vyos ipsec
         // site-to-site peer
         Ip peerAddress = ipsecPeerEntry.getKey();
         IpsecPeer ipsecPeer = ipsecPeerEntry.getValue();
         String newIpsecVpnName = peerAddress.toString();
         String newIkeGatewayName = newIpsecVpnName;
         IpsecVpn newIpsecVpn = new IpsecVpn(newIpsecVpnName, _c);
         _c.getIpsecVpns().put(newIpsecVpnName, newIpsecVpn);
         IkeGateway newIkeGateway = new IkeGateway(newIkeGatewayName);
         _c.getIkeGateways().put(newIkeGatewayName, newIkeGateway);
         newIpsecVpn.setGateway(newIkeGateway);
         newIkeGateway.setLocalId(ipsecPeer.getAuthenticationId());
         newIkeGateway.setRemoteId(ipsecPeer.getAuthenticationRemoteId());
         newIkeGateway.setAddress(peerAddress);
         Ip localAddress = ipsecPeer.getLocalAddress();
         org.batfish.datamodel.Interface externalInterface = _ipToInterfaceMap
               .get(localAddress);
         if (externalInterface == null) {
            _w.redFlag("Could not determine external interface for vpn \""
                  + newIpsecVpnName + "\" from local-address: "
                  + localAddress.toString());
         }
         else {
            newIkeGateway.setExternalInterface(externalInterface);
         }

         // bind interface
         String bindInterfaceName = ipsecPeer.getBindInterface();
         org.batfish.datamodel.Interface newBindInterface = _c.getInterfaces()
               .get(bindInterfaceName);
         if (newBindInterface != null) {
            Interface bindInterface = _interfaces.get(bindInterfaceName);
            bindInterface.getReferers().put(
                  ipsecPeer,
                  "bind interface for site-to-site peer \"" + newIpsecVpnName
                        + "\"");
            newIpsecVpn.setBindInterface(newBindInterface);
         }
         else {
            _w.redFlag("Reference to undefined bind-interface: \""
                  + bindInterfaceName + "\"");
         }

         // convert the referenced ike group
         String ikeGroupName = ipsecPeer.getIkeGroup();
         IkeGroup ikeGroup = _ikeGroups.get(ikeGroupName);
         if (ikeGroup == null) {
            _w.redFlag("Reference to undefined ike-group: \"" + ikeGroupName
                  + "\"");
         }
         else {
            ikeGroup.getReferers().put(
                  ipsecPeer,
                  "ike group for site-to-site peer: \"" + newIpsecVpnName
                        + "\"");
            IkePolicy newIkePolicy = new IkePolicy(ikeGroupName);
            _c.getIkePolicies().put(ikeGroupName, newIkePolicy);
            newIkeGateway.setIkePolicy(newIkePolicy);
            newIkePolicy.setPreSharedKeyHash(ipsecPeer
                  .getAuthenticationPreSharedSecretHash());

            // convert contained ike proposals
            for (Entry<Integer, IkeProposal> ikeProposalEntry : ikeGroup
                  .getProposals().entrySet()) {
               String newIkeProposalName = ikeGroupName + ":"
                     + Integer.toString(ikeProposalEntry.getKey());
               IkeProposal ikeProposal = ikeProposalEntry.getValue();
               org.batfish.datamodel.IkeProposal newIkeProposal = new org.batfish.datamodel.IkeProposal(
                     newIkeProposalName);
               _c.getIkeProposals().put(newIkeProposalName, newIkeProposal);
               newIkePolicy.getProposals().put(newIkeProposalName,
                     newIkeProposal);
               newIkeProposal.setDiffieHellmanGroup(ikeProposal.getDhGroup());
               newIkeProposal.setEncryptionAlgorithm(ikeProposal
                     .getEncryptionAlgorithm());
               newIkeProposal.setLifetimeSeconds(ikeGroup.getLifetimeSeconds());
               newIkeProposal.setAuthenticationAlgorithm(ikeProposal
                     .getHashAlgorithm().toIkeAuthenticationAlgorithm());
               newIkeProposal.setAuthenticationMethod(ipsecPeer
                     .getAuthenticationMode());
            }
         }

         // convert the referenced esp group
         String espGroupName = ipsecPeer.getEspGroup();
         EspGroup espGroup = _espGroups.get(espGroupName);
         if (espGroup == null) {
            _w.redFlag("Reference to undefined esp-group: \"" + espGroupName
                  + "\"");
         }
         else {
            espGroup.getReferers().put(
                  ipsecPeer,
                  "esp-group for ipsec site-to-site peer: \"" + newIpsecVpnName
                        + "\"");
            IpsecPolicy newIpsecPolicy = new IpsecPolicy(espGroupName);
            _c.getIpsecPolicies().put(espGroupName, newIpsecPolicy);
            newIpsecVpn.setIpsecPolicy(newIpsecPolicy);
            if (espGroup.getPfsSource() == null) {
               espGroup.setPfsSource(PfsSource.IKE_GROUP);
            }
            switch (espGroup.getPfsSource()) {
            case DISABLED:
               break;

            case ESP_GROUP:
               newIpsecPolicy.setPfsKeyGroup(espGroup.getPfsDhGroup());
               break;

            case IKE_GROUP:
               newIpsecPolicy.setPfsKeyGroupDynamicIke(true);
               break;

            default:
               throw new BatfishException("Invalid pfs source");
            }

            // convert contained esp proposals
            for (Entry<Integer, EspProposal> espProposalEntry : espGroup
                  .getProposals().entrySet()) {
               String newIpsecProposalName = espGroupName + ":"
                     + Integer.toString(espProposalEntry.getKey());
               EspProposal espProposal = espProposalEntry.getValue();
               IpsecProposal newIpsecProposal = new IpsecProposal(
                     newIpsecProposalName);
               _c.getIpsecProposals().put(newIpsecProposalName,
                     newIpsecProposal);
               newIpsecPolicy.getProposals().put(newIpsecProposalName,
                     newIpsecProposal);
               newIpsecProposal.setAuthenticationAlgorithm(espProposal
                     .getHashAlgorithm().toIpsecAuthenticationAlgorithm());
               newIpsecProposal.setEncryptionAlgorithm(espProposal
                     .getEncryptionAlgorithm());
               newIpsecProposal.setLifetimeSeconds(espGroup
                     .getLifetimeSeconds());
               newIpsecProposal.setProtocol(IpsecProtocol.ESP);
            }
         }
      }

   }

   @Override
   public Set<String> getUnimplementedFeatures() {
      return _unimplementedFeatures;
   }

   @Override
   public Warnings getWarnings() {
      return _w;
   }

   @Override
   public void setVendor(ConfigurationFormat format) {
      _format = format;
   }

   private org.batfish.datamodel.Interface toInterface(Interface iface) {
      String name = iface.getName();
      org.batfish.datamodel.Interface newIface = new org.batfish.datamodel.Interface(
            name, _c);
      newIface.setActive(true); // TODO: may have to change
      newIface.setBandwidth(iface.getBandwidth());
      newIface.setDescription(iface.getDescription());
      Prefix prefix = iface.getPrefix();
      if (prefix != null) {
         newIface.setPrefix(iface.getPrefix());
      }
      newIface.getAllPrefixes().addAll(iface.getAllPrefixes());
      for (Prefix p : newIface.getAllPrefixes()) {
         _ipToInterfaceMap.put(p.getAddress(), newIface);
      }
      return newIface;
   }

   private PolicyMap toPolicyMap(RouteMap routeMap) {
      String name = routeMap.getName();
      PolicyMap policyMap = new PolicyMap(name);
      for (Entry<Integer, RouteMapRule> e : routeMap.getRules().entrySet()) {
         String ruleName = Integer.toString(e.getKey());
         RouteMapRule rule = e.getValue();
         PolicyMapClause clause = new PolicyMapClause();
         clause.setName(ruleName);
         clause.setAction(PolicyMapAction.fromLineAction(rule.getAction()));
         policyMap.getClauses().add(clause);
         for (RouteMapMatch match : rule.getMatches()) {
            match.applyTo(_c, policyMap, clause, _w);
         }
      }
      return policyMap;
   }

   private RouteFilterList toRouteFilterList(PrefixList prefixList) {
      String name = prefixList.getName();
      RouteFilterList newList = new RouteFilterList(name);
      for (PrefixListRule rule : prefixList.getRules().values()) {
         RouteFilterLine newLine = new RouteFilterLine(rule.getAction(),
               rule.getPrefix(), rule.getLengthRange());
         newList.getLines().add(newLine);
      }
      return newList;
   }

   @Override
   public Configuration toVendorIndependentConfiguration(Warnings warnings)
         throws VendorConversionException {
      _ipToInterfaceMap = new HashMap<Ip, org.batfish.datamodel.Interface>();
      _w = warnings;
      _c = new Configuration(_hostname);
      _c.setVendor(_format);
      _c.setDefaultCrossZoneAction(LineAction.ACCEPT);
      _c.setDefaultInboundAction(LineAction.ACCEPT);

      convertPrefixLists();
      convertRouteMaps();
      convertInterfaces();
      convertVpns();
      warnAndDisableUnreferencedVtiInterfaces();

      return _c;
   }

   private void warnAndDisableUnreferencedVtiInterfaces() {
      for (Entry<String, Interface> ifaceEntry : _interfaces.entrySet()) {
         Interface iface = ifaceEntry.getValue();
         if (iface.getType() == InterfaceType.VTI && iface.isUnused()) {
            String name = ifaceEntry.getKey();
            _c.getInterfaces().remove(name);
            _w.redFlag("Disabling unused VTI interface: \"" + name + "\"");
         }
      }
   }

}