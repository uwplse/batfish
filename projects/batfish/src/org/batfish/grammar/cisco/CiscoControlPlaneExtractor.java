package org.batfish.grammar.cisco;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.batfish.grammar.BatfishCombinedParser;
import org.batfish.grammar.ControlPlaneExtractor;
import org.batfish.grammar.cisco.CiscoParser.Extended_access_list_tailContext;
import org.batfish.grammar.cisco.CiscoParser.*;
import org.batfish.common.BatfishException;
import org.batfish.datamodel.DscpType;
import org.batfish.datamodel.IcmpCode;
import org.batfish.datamodel.IcmpType;
import org.batfish.datamodel.Ip;
import org.batfish.datamodel.Ip6;
import org.batfish.datamodel.IpProtocol;
import org.batfish.datamodel.IpWildcard;
import org.batfish.datamodel.IsisInterfaceMode;
import org.batfish.datamodel.IsisLevel;
import org.batfish.datamodel.IsoAddress;
import org.batfish.datamodel.LineAction;
import org.batfish.datamodel.NamedPort;
import org.batfish.datamodel.OriginType;
import org.batfish.datamodel.OspfMetricType;
import org.batfish.datamodel.Prefix;
import org.batfish.datamodel.Prefix6;
import org.batfish.datamodel.RoutingProtocol;
import org.batfish.datamodel.SubRange;
import org.batfish.datamodel.SwitchportEncapsulationType;
import org.batfish.datamodel.SwitchportMode;
import org.batfish.datamodel.TcpFlags;
import org.batfish.main.RedFlagBatfishException;
import org.batfish.main.Warnings;
import org.batfish.representation.VendorConfiguration;
import org.batfish.representation.cisco.BgpAggregateNetwork;
import org.batfish.representation.cisco.BgpPeerGroup;
import org.batfish.representation.cisco.BgpProcess;
import org.batfish.representation.cisco.BgpRedistributionPolicy;
import org.batfish.representation.cisco.CiscoConfiguration;
import org.batfish.representation.cisco.CiscoVendorConfiguration;
import org.batfish.representation.cisco.DynamicBgpPeerGroup;
import org.batfish.representation.cisco.ExpandedCommunityList;
import org.batfish.representation.cisco.ExpandedCommunityListLine;
import org.batfish.representation.cisco.ExtendedAccessList;
import org.batfish.representation.cisco.ExtendedAccessListLine;
import org.batfish.representation.cisco.Interface;
import org.batfish.representation.cisco.IpAsPathAccessList;
import org.batfish.representation.cisco.IpAsPathAccessListLine;
import org.batfish.representation.cisco.IpBgpPeerGroup;
import org.batfish.representation.cisco.Ipv6BgpPeerGroup;
import org.batfish.representation.cisco.IsisProcess;
import org.batfish.representation.cisco.IsisRedistributionPolicy;
import org.batfish.representation.cisco.MasterBgpPeerGroup;
import org.batfish.representation.cisco.NamedBgpPeerGroup;
import org.batfish.representation.cisco.OspfProcess;
import org.batfish.representation.cisco.OspfRedistributionPolicy;
import org.batfish.representation.cisco.OspfWildcardNetwork;
import org.batfish.representation.cisco.PrefixList;
import org.batfish.representation.cisco.PrefixListLine;
import org.batfish.representation.cisco.RouteMap;
import org.batfish.representation.cisco.RouteMapClause;
import org.batfish.representation.cisco.RouteMapMatchAsPathAccessListLine;
import org.batfish.representation.cisco.RouteMapMatchCommunityListLine;
import org.batfish.representation.cisco.RouteMapMatchIpAccessListLine;
import org.batfish.representation.cisco.RouteMapMatchIpPrefixListLine;
import org.batfish.representation.cisco.RouteMapMatchTagLine;
import org.batfish.representation.cisco.RouteMapSetAdditiveCommunityLine;
import org.batfish.representation.cisco.RouteMapSetAdditiveCommunityListLine;
import org.batfish.representation.cisco.RouteMapSetAsPathPrependLine;
import org.batfish.representation.cisco.RouteMapSetCommunityLine;
import org.batfish.representation.cisco.RouteMapSetCommunityListLine;
import org.batfish.representation.cisco.RouteMapSetCommunityNoneLine;
import org.batfish.representation.cisco.RouteMapSetDeleteCommunityLine;
import org.batfish.representation.cisco.RouteMapSetLine;
import org.batfish.representation.cisco.RouteMapSetLocalPreferenceLine;
import org.batfish.representation.cisco.RouteMapSetMetricLine;
import org.batfish.representation.cisco.RouteMapSetNextHopLine;
import org.batfish.representation.cisco.RouteMapSetOriginTypeLine;
import org.batfish.representation.cisco.RoutePolicy;
import org.batfish.representation.cisco.RoutePolicyApplyStatement;
import org.batfish.representation.cisco.RoutePolicyBoolean;
import org.batfish.representation.cisco.RoutePolicyBooleanAnd;
import org.batfish.representation.cisco.RoutePolicyBooleanCommunityMatchesAny;
import org.batfish.representation.cisco.RoutePolicyBooleanCommunityMatchesEvery;
import org.batfish.representation.cisco.RoutePolicyBooleanDestination;
import org.batfish.representation.cisco.RoutePolicyBooleanNot;
import org.batfish.representation.cisco.RoutePolicyBooleanOr;
import org.batfish.representation.cisco.RoutePolicyBooleanRIBHasRoute;
import org.batfish.representation.cisco.RoutePolicyCommunitySet;
import org.batfish.representation.cisco.RoutePolicyCommunitySetName;
import org.batfish.representation.cisco.RoutePolicyCommunitySetNumber;
import org.batfish.representation.cisco.RoutePolicyDeleteAllStatement;
import org.batfish.representation.cisco.RoutePolicyDeleteCommunityStatement;
import org.batfish.representation.cisco.RoutePolicyDispositionStatement;
import org.batfish.representation.cisco.RoutePolicyDispositionType;
import org.batfish.representation.cisco.RoutePolicyElseBlock;
import org.batfish.representation.cisco.RoutePolicyElseIfBlock;
import org.batfish.representation.cisco.RoutePolicyIfStatement;
import org.batfish.representation.cisco.RoutePolicyNextHop;
import org.batfish.representation.cisco.RoutePolicyNextHopIP;
import org.batfish.representation.cisco.RoutePolicyNextHopIP6;
import org.batfish.representation.cisco.RoutePolicyNextHopPeerAddress;
import org.batfish.representation.cisco.RoutePolicyNextHopSelf;
import org.batfish.representation.cisco.RoutePolicyPrefixSet;
import org.batfish.representation.cisco.RoutePolicyPrefixSetIp;
import org.batfish.representation.cisco.RoutePolicyPrefixSetIpV6;
import org.batfish.representation.cisco.RoutePolicyPrefixSetName;
import org.batfish.representation.cisco.RoutePolicyPrefixSetNumber;
import org.batfish.representation.cisco.RoutePolicyPrefixSetNumberV6;
import org.batfish.representation.cisco.RoutePolicySetCommunity;
import org.batfish.representation.cisco.RoutePolicySetLocalPref;
import org.batfish.representation.cisco.RoutePolicySetMED;
import org.batfish.representation.cisco.RoutePolicySetNextHop;
import org.batfish.representation.cisco.RoutePolicyStatement;
import org.batfish.representation.cisco.StandardAccessList;
import org.batfish.representation.cisco.StandardAccessListLine;
import org.batfish.representation.cisco.StandardCommunityList;
import org.batfish.representation.cisco.StandardCommunityListLine;
import org.batfish.representation.cisco.StaticRoute;

public class CiscoControlPlaneExtractor extends CiscoParserBaseListener
      implements ControlPlaneExtractor {

   private static final Map<String, String> CISCO_INTERFACE_PREFIXES = getCiscoInterfacePrefixes();

   private static final int DEFAULT_STATIC_ROUTE_DISTANCE = 1;

   private static final Interface DUMMY_INTERFACE = new Interface("dummy");

   private static final String F_ALLOWAS_IN_NUMBER = "bgp -  allowas-in with number - ignored and effectively infinite for now";

   private static final String F_BGP_AUTO_SUMMARY = "bgp - auto-summary";

   private static final String F_BGP_INHERIT_PEER_OTHER = "bgp - inherit peer - inheritance not implemented for this peer type";

   private static final String F_BGP_INHERIT_PEER_SESSION_OTHER = "bgp - inherit peer-session - inheritance not implemented for this peer type";

   private static final String F_BGP_MAXIMUM_PEERS = "bgp - maximum-peers";

   private static final String F_BGP_NEIGHBOR_DISTRIBUTE_LIST = "bgp - neighbor distribute-list";

   private static final String F_BGP_NETWORK_ROUTE_MAP = "bgp - network with route-map";

   private static final String F_BGP_NEXT_HOP_SELF = "bgp - (no) next-hop-self";

   private static final String F_BGP_REDISTRIBUTE_AGGREGATE = "bgp - redistribute aggregate";

   private static final String F_FRAGMENTS = "acl fragments";

   private static final String F_INTERFACE_MULTIPOINT = "interface multipoint";

   private static final String F_IP_DEFAULT_GATEWAY = "ip default-gateway";

   private static final String F_IP_ROUTE_VRF = "ip route vrf / vrf - ip route";

   private static final String F_IPV6 = "ipv6 - other";

   private static final String F_OSPF_AREA_NSSA = "ospf - not-so-stubby areas";

   private static final String F_OSPF_MAXIMUM_PATHS = "ospf - maximum-paths";

   private static final String F_OSPF_REDISTRIBUTE_RIP = "ospf - redistribute rip";

   private static final String F_OSPF_VRF = "router ospf vrf";

   private static final String F_RIP = "rip";

   private static final String F_ROUTE_MAP_SET_METRIC_TYPE = "route-map - set metric-type";

   private static final String F_SWITCHING_MODE = "switching-mode";

   private static final String F_TTL = "acl ttl eq number";

   private static final String NXOS_MANAGEMENT_INTERFACE_PREFIX = "mgmt";

   public static LineAction getAccessListAction(Access_list_actionContext ctx) {
      if (ctx.PERMIT() != null) {
         return LineAction.ACCEPT;
      }
      else if (ctx.DENY() != null) {
         return LineAction.REJECT;
      }
      else {
         throw new BatfishException("bad LineAction");
      }
   }

   private static String getCanonicalInterfaceNamePrefix(String prefix) {
      for (Entry<String, String> e : CISCO_INTERFACE_PREFIXES.entrySet()) {
         String matchPrefix = e.getKey();
         String canonicalPrefix = e.getValue();
         if (matchPrefix.toLowerCase().startsWith(prefix.toLowerCase())) {
            return canonicalPrefix;
         }
      }
      throw new BatfishException("Invalid interface name prefix: \"" + prefix
            + "\"");
   }

   private static Map<String, String> getCiscoInterfacePrefixes() {
      Map<String, String> prefixes = new LinkedHashMap<String, String>();
      prefixes.put("Async", "Async");
      prefixes.put("ATM", "ATM");
      prefixes.put("Bundle-Ether", "Bundle-Ether");
      prefixes.put("cmp-mgmt", "cmp-mgmt");
      prefixes.put("Dialer", "Dialer");
      prefixes.put("Embedded-Service-Engine", "Embedded-Service-Engine");
      prefixes.put("Ethernet", "Ethernet");
      prefixes.put("FastEthernet", "FastEthernet");
      prefixes.put("fe", "FastEthernet");
      prefixes.put("fortyGigE", "FortyGigabitEthernet");
      prefixes.put("GigabitEthernet", "GigabitEthernet");
      prefixes.put("ge", "GigabitEthernet");
      prefixes.put("GMPLS", "GMPLS");
      prefixes.put("HundredGigE", "HundredGigE");
      prefixes.put("ip", "ip");
      prefixes.put("Group-Async", "Group-Async");
      prefixes.put("Loopback", "Loopback");
      prefixes.put("Management", "Management");
      prefixes.put("ManagementEthernet", "ManagementEthernet");
      prefixes.put("mgmt", NXOS_MANAGEMENT_INTERFACE_PREFIX);
      prefixes.put("MgmtEth", "ManagementEthernet");
      prefixes.put("Null", "Null");
      prefixes.put("Port-channel", "Port-channel");
      prefixes.put("POS", "POS");
      prefixes.put("Serial", "Serial");
      prefixes.put("TenGigabitEthernet", "TenGigabitEthernet");
      prefixes.put("TenGigE", "TenGigE");
      prefixes.put("te", "TenGigabitEthernet");
      prefixes.put("trunk", "trunk");
      prefixes.put("Tunnel", "Tunnel");
      prefixes.put("tunnel-te", "tunnel-te");
      prefixes.put("Virtual-Template", "Virtual-Template");
      prefixes.put("Vlan", "Vlan");
      return prefixes;
   }

   public static Ip getIp(Access_list_ip_rangeContext ctx) {
      if (ctx.ip != null) {
         return toIp(ctx.ip);
      }
      else if (ctx.prefix != null) {
         return getPrefixIp(ctx.prefix);
      }
      else {
         return new Ip(0l);
      }
   }

   public static NamedPort getNamedPort(PortContext ctx) {
      if (ctx.AOL() != null) {
         return NamedPort.AOL;
      }
      else if (ctx.BGP() != null) {
         return NamedPort.BGP;
      }
      else if (ctx.BIFF() != null) {
         return NamedPort.BIFFudp_OR_EXECtcp;
      }
      else if (ctx.BOOTPC() != null) {
         return NamedPort.BOOTPC;
      }
      else if (ctx.BOOTPS() != null) {
         return NamedPort.BOOTPS_OR_DHCP;
      }
      else if (ctx.CHARGEN() != null) {
         return NamedPort.CHARGEN;
      }
      else if (ctx.CITRIX_ICA() != null) {
         return NamedPort.CITRIX_ICA;
      }
      else if (ctx.CMD() != null) {
         return NamedPort.CMDtcp_OR_SYSLOGudp;
      }
      else if (ctx.CTIQBE() != null) {
         return NamedPort.CTIQBE;
      }
      else if (ctx.DAYTIME() != null) {
         return NamedPort.DAYTIME;
      }
      else if (ctx.DISCARD() != null) {
         return NamedPort.DISCARD;
      }
      else if (ctx.DNSIX() != null) {
         return NamedPort.DNSIX;
      }
      else if (ctx.DOMAIN() != null) {
         return NamedPort.DOMAIN;
      }
      else if (ctx.ECHO() != null) {
         return NamedPort.ECHO;
      }
      else if (ctx.EXEC() != null) {
         return NamedPort.BIFFudp_OR_EXECtcp;
      }
      else if (ctx.FINGER() != null) {
         return NamedPort.FINGER;
      }
      else if (ctx.FTP() != null) {
         return NamedPort.FTP;
      }
      else if (ctx.FTP_DATA() != null) {
         return NamedPort.FTP_DATA;
      }
      else if (ctx.GOPHER() != null) {
         return NamedPort.GOPHER;
      }
      else if (ctx.H323() != null) {
         return NamedPort.H323;
      }
      else if (ctx.HTTPS() != null) {
         return NamedPort.HTTPS;
      }
      else if (ctx.HOSTNAME() != null) {
         return NamedPort.HOSTNAME;
      }
      else if (ctx.IDENT() != null) {
         return NamedPort.IDENT;
      }
      else if (ctx.IMAP4() != null) {
         return NamedPort.IMAP;
      }
      else if (ctx.IRC() != null) {
         return NamedPort.IRC;
      }
      else if (ctx.ISAKMP() != null) {
         return NamedPort.ISAKMP;
      }
      else if (ctx.KERBEROS() != null) {
         return NamedPort.KERBEROS;
      }
      else if (ctx.KLOGIN() != null) {
         return NamedPort.KLOGIN;
      }
      else if (ctx.KSHELL() != null) {
         return NamedPort.KSHELL;
      }
      else if (ctx.LDAP() != null) {
         return NamedPort.LDAP;
      }
      else if (ctx.LDAPS() != null) {
         return NamedPort.LDAPS;
      }
      else if (ctx.LPD() != null) {
         return NamedPort.LPD;
      }
      else if (ctx.LOGIN() != null) {
         return NamedPort.LOGINtcp_OR_WHOudp;
      }
      else if (ctx.LOTUSNOTES() != null) {
         return NamedPort.LOTUSNOTES;
      }
      else if (ctx.MLAG() != null) {
         return NamedPort.MLAG;
      }
      else if (ctx.MOBILE_IP() != null) {
         return NamedPort.MOBILE_IP_AGENT;
      }
      else if (ctx.NAMESERVER() != null) {
         return NamedPort.NAMESERVER;
      }
      else if (ctx.NETBIOS_DGM() != null) {
         return NamedPort.NETBIOS_DGM;
      }
      else if (ctx.NETBIOS_NS() != null) {
         return NamedPort.NETBIOS_NS;
      }
      else if (ctx.NETBIOS_SS() != null) {
         return NamedPort.NETBIOS_SSN;
      }
      else if (ctx.NETBIOS_SSN() != null) {
         return NamedPort.NETBIOS_SSN;
      }
      else if (ctx.NNTP() != null) {
         return NamedPort.NNTP;
      }
      else if (ctx.NON500_ISAKMP() != null) {
         return NamedPort.NON500_ISAKMP;
      }
      else if (ctx.NTP() != null) {
         return NamedPort.NTP;
      }
      else if (ctx.PCANYWHERE_DATA() != null) {
         return NamedPort.PCANYWHERE_DATA;
      }
      else if (ctx.PCANYWHERE_STATUS() != null) {
         return NamedPort.PCANYWHERE_STATUS;
      }
      else if (ctx.PIM_AUTO_RP() != null) {
         return NamedPort.PIM_AUTO_RP;
      }
      else if (ctx.POP2() != null) {
         return NamedPort.POP2;
      }
      else if (ctx.POP3() != null) {
         return NamedPort.POP3;
      }
      else if (ctx.PPTP() != null) {
         return NamedPort.PPTP;
      }
      else if (ctx.RADIUS() != null) {
         return NamedPort.RADIUS_CISCO;
      }
      else if (ctx.RADIUS_ACCT() != null) {
         return NamedPort.RADIUS_ACCT_CISCO;
      }
      else if (ctx.RIP() != null) {
         return NamedPort.RIP;
      }
      else if (ctx.SECUREID_UDP() != null) {
         return NamedPort.SECUREID_UDP;
      }
      else if (ctx.SMTP() != null) {
         return NamedPort.SMTP;
      }
      else if (ctx.SNMP() != null) {
         return NamedPort.SNMP;
      }
      else if (ctx.SNMPTRAP() != null) {
         return NamedPort.SNMPTRAP;
      }
      else if (ctx.SQLNET() != null) {
         return NamedPort.SQLNET;
      }
      else if (ctx.SSH() != null) {
         return NamedPort.SSH;
      }
      else if (ctx.SUNRPC() != null) {
         return NamedPort.SUNRPC;
      }
      else if (ctx.SYSLOG() != null) {
         return NamedPort.CMDtcp_OR_SYSLOGudp;
      }
      else if (ctx.TACACS() != null) {
         return NamedPort.TACACS;
      }
      else if (ctx.TACACS_DS() != null) {
         return NamedPort.TACACS_DS;
      }
      else if (ctx.TALK() != null) {
         return NamedPort.TALK;
      }
      else if (ctx.TELNET() != null) {
         return NamedPort.TELNET;
      }
      else if (ctx.TFTP() != null) {
         return NamedPort.TFTP;
      }
      else if (ctx.TIME() != null) {
         return NamedPort.TIME;
      }
      else if (ctx.UUCP() != null) {
         return NamedPort.UUCP;
      }
      else if (ctx.WHO() != null) {
         return NamedPort.LOGINtcp_OR_WHOudp;
      }
      else if (ctx.WHOIS() != null) {
         return NamedPort.WHOIS;
      }
      else if (ctx.WWW() != null) {
         return NamedPort.HTTP;
      }
      else if (ctx.XDMCP() != null) {
         return NamedPort.XDMCP;
      }
      else {
         throw new BatfishException("missing port-number mapping for port: \""
               + ctx.getText() + "\"");
      }
   }

   public static int getPortNumber(PortContext ctx) {
      if (ctx.DEC() != null) {
         return toInteger(ctx.DEC());
      }
      else {
         NamedPort namedPort = getNamedPort(ctx);
         return namedPort.number();
      }
   }

   private static List<SubRange> getPortRanges(Port_specifierContext ps) {
      List<SubRange> ranges = new ArrayList<SubRange>();
      if (ps.EQ() != null) {
         for (PortContext pc : ps.args) {
            int port = getPortNumber(pc);
            ranges.add(new SubRange(port, port));
         }
      }
      else if (ps.GT() != null) {
         int port = getPortNumber(ps.arg);
         ranges.add(new SubRange(port + 1, 65535));
      }
      else if (ps.NEQ() != null) {
         int port = getPortNumber(ps.arg);
         SubRange beforeRange = new SubRange(0, port - 1);
         SubRange afterRange = new SubRange(port + 1, 65535);
         ranges.add(beforeRange);
         ranges.add(afterRange);
      }
      else if (ps.LT() != null) {
         int port = getPortNumber(ps.arg);
         ranges.add(new SubRange(0, port - 1));
      }
      else if (ps.RANGE() != null) {
         int lowPort = getPortNumber(ps.arg1);
         int highPort = getPortNumber(ps.arg2);
         ranges.add(new SubRange(lowPort, highPort));
      }
      else {
         throw new BatfishException("bad port range");
      }
      return ranges;
   }

   public static Ip getPrefixIp(Token ipPrefixToken) {
      if (ipPrefixToken.getType() != CiscoLexer.IP_PREFIX) {
         throw new BatfishException(
               "attempted to get prefix length from non-IP_PREFIX token: "
                     + ipPrefixToken.getType() + " with text: \""
                     + ipPrefixToken.getText() + "\"");
      }
      String text = ipPrefixToken.getText();
      String[] parts = text.split("/");
      String prefixIpStr = parts[0];
      Ip prefixIp = new Ip(prefixIpStr);
      return prefixIp;
   }

   public static int getPrefixLength(Token ipPrefixToken) {
      if (ipPrefixToken.getType() != CiscoLexer.IP_PREFIX) {
         throw new BatfishException(
               "attempted to get prefix length from non-IP_PREFIX token: "
                     + ipPrefixToken.getType());
      }
      String text = ipPrefixToken.getText();
      String[] parts = text.split("/");
      String prefixLengthStr = parts[1];
      int prefixLength = Integer.parseInt(prefixLengthStr);
      return prefixLength;
   }

   public static Ip getWildcard(Access_list_ip_rangeContext ctx) {
      if (ctx.wildcard != null) {
         return toIp(ctx.wildcard);
      }
      else if (ctx.ANY() != null || ctx.address_group != null) {
         return new Ip(0xFFFFFFFFl);
      }
      else if (ctx.HOST() != null) {
         return new Ip(0l);
      }
      else if (ctx.prefix != null) {
         int pfxLength = getPrefixLength(ctx.prefix);
         long ipAsLong = 0xFFFFFFFFl >>> pfxLength;
         return new Ip(ipAsLong);
      }
      else if (ctx.ip != null) {
         // basically same as host
         return new Ip(0l);
      }
      else {
         throw new BatfishException("bad extended ip access list ip range");
      }
   }

   private static int toDscpType(Dscp_typeContext ctx) {
      int val;
      if (ctx.DEC() != null) {
         val = toInteger(ctx.DEC());
      }
      else if (ctx.AF11() != null) {
         val = DscpType.AF11.number();
      }
      else if (ctx.AF12() != null) {
         val = DscpType.AF12.number();
      }
      else if (ctx.AF13() != null) {
         val = DscpType.AF13.number();
      }
      else if (ctx.AF21() != null) {
         val = DscpType.AF21.number();
      }
      else if (ctx.AF22() != null) {
         val = DscpType.AF22.number();
      }
      else if (ctx.AF23() != null) {
         val = DscpType.AF23.number();
      }
      else if (ctx.AF31() != null) {
         val = DscpType.AF31.number();
      }
      else if (ctx.AF32() != null) {
         val = DscpType.AF32.number();
      }
      else if (ctx.AF33() != null) {
         val = DscpType.AF33.number();
      }
      else if (ctx.AF41() != null) {
         val = DscpType.AF41.number();
      }
      else if (ctx.AF42() != null) {
         val = DscpType.AF42.number();
      }
      else if (ctx.AF43() != null) {
         val = DscpType.AF43.number();
      }
      else if (ctx.CS1() != null) {
         val = DscpType.CS1.number();
      }
      else if (ctx.CS2() != null) {
         val = DscpType.CS2.number();
      }
      else if (ctx.CS3() != null) {
         val = DscpType.CS3.number();
      }
      else if (ctx.CS4() != null) {
         val = DscpType.CS4.number();
      }
      else if (ctx.CS5() != null) {
         val = DscpType.CS5.number();
      }
      else if (ctx.CS6() != null) {
         val = DscpType.CS6.number();
      }
      else if (ctx.CS7() != null) {
         val = DscpType.CS7.number();
      }
      else if (ctx.DEFAULT() != null) {
         val = DscpType.DEFAULT.number();
      }
      else if (ctx.EF() != null) {
         val = DscpType.EF.number();
      }
      else {
         throw new BatfishException("Unhandled dscp type: \"" + ctx.getText()
               + "\"");
      }
      return val;
   }

   public static int toInteger(TerminalNode t) {
      return Integer.parseInt(t.getText());
   }

   public static int toInteger(Token t) {
      return Integer.parseInt(t.getText());
   }

   private static String toInterfaceName(Interface_nameContext ctx) {
      String canonicalNamePrefix = getCanonicalInterfaceNamePrefix(ctx.name_prefix_alpha
            .getText());
      String name = canonicalNamePrefix;
      for (Token part : ctx.name_middle_parts) {
         name += part.getText();
      }
      if (ctx.range().range_list.size() != 1) {
         throw new RedFlagBatfishException(
               "got interface range where single interface was expected: \""
                     + ctx.getText() + "\"");
      }
      name += ctx.range().getText();
      return name;
   }

   public static Ip toIp(TerminalNode t) {
      return new Ip(t.getText());
   }

   public static Ip toIp(Token t) {
      return new Ip(t.getText());
   }

   public static Ip6 toIp6(TerminalNode t) {
      return new Ip6(t.getText());
   }

   public static Ip6 toIp6(Token t) {
      return new Ip6(t.getText());
   }

   public static IpProtocol toIpProtocol(ProtocolContext ctx) {
      if (ctx.DEC() != null) {
         int num = toInteger(ctx.DEC());
         return IpProtocol.fromNumber(num);
      }
      else if (ctx.AHP() != null) {
         return IpProtocol.AHP;
      }
      else if (ctx.EIGRP() != null) {
         return IpProtocol.EIGRP;
      }
      else if (ctx.ESP() != null) {
         return IpProtocol.ESP;
      }
      else if (ctx.GRE() != null) {
         return IpProtocol.GRE;
      }
      else if (ctx.ICMP() != null) {
         return IpProtocol.ICMP;
      }
      else if (ctx.IGMP() != null) {
         return IpProtocol.IGMP;
      }
      else if (ctx.IP() != null) {
         return IpProtocol.IP;
      }
      else if (ctx.IPINIP() != null) {
         return IpProtocol.IPINIP;
      }
      else if (ctx.IPV4() != null) {
         return IpProtocol.IP;
      }
      else if (ctx.IPV6() != null) {
         return IpProtocol.IPv6;
      }
      else if (ctx.OSPF() != null) {
         return IpProtocol.OSPF;
      }
      else if (ctx.PIM() != null) {
         return IpProtocol.PIM;
      }
      else if (ctx.SCTP() != null) {
         return IpProtocol.SCTP;
      }
      else if (ctx.TCP() != null) {
         return IpProtocol.TCP;
      }
      else if (ctx.UDP() != null) {
         return IpProtocol.UDP;
      }
      else if (ctx.VRRP() != null) {
         return IpProtocol.VRRP;
      }
      else {
         throw new BatfishException("missing token-protocol mapping");
      }
   }

   public static long toLong(CommunityContext ctx) {
      switch (ctx.com.getType()) {
      case CiscoLexer.COMMUNITY_NUMBER:
         String numberText = ctx.com.getText();
         String[] parts = numberText.split(":");
         String leftStr = parts[0];
         String rightStr = parts[1];
         long left = Long.parseLong(leftStr);
         long right = Long.parseLong(rightStr);
         return (left << 16) | right;

      case CiscoLexer.DEC:
         return toLong(ctx.com);

      case CiscoLexer.INTERNET:
         return 0l;

      case CiscoLexer.LOCAL_AS:
         return 0xFFFFFF03l;

      case CiscoLexer.NO_ADVERTISE:
         return 0xFFFFFF02l;

      case CiscoLexer.NO_EXPORT:
         return 0xFFFFFF01l;

      default:
         throw new BatfishException("bad community");
      }
   }

   public static long toLong(TerminalNode t) {
      return Long.parseLong(t.getText());
   }

   public static long toLong(Token t) {
      return Long.parseLong(t.getText());
   }

   public static List<SubRange> toRange(RangeContext ctx) {
      List<SubRange> range = new ArrayList<SubRange>();
      for (SubrangeContext sc : ctx.range_list) {
         SubRange sr = toSubRange(sc);
         range.add(sr);
      }
      return range;
   }

   private static SubRange toSubrange(As_path_regex_rangeContext ctx) {
      if (ctx.DEC() != null) {
         int as = toInteger(ctx.DEC());
         return new SubRange(as, as);
      }
      else if (ctx.PERIOD() != null) {
         return new SubRange(0, 65535);
      }
      else {
         throw new BatfishException("Invalid as path regex range");
      }
   }

   public static SubRange toSubRange(SubrangeContext ctx) {
      int low = toInteger(ctx.low);
      if (ctx.DASH() != null) {
         int high = toInteger(ctx.high);
         return new SubRange(low, high);
      }
      else {
         return new SubRange(low, low);
      }
   }

   private CiscoConfiguration _configuration;

   private IpAsPathAccessList _currentAsPathAcl;

   private DynamicBgpPeerGroup _currentDynamicPeerGroup;

   private ExpandedCommunityList _currentExpandedCommunityList;

   private ExtendedAccessList _currentExtendedAcl;

   private List<Interface> _currentInterfaces;

   private IpBgpPeerGroup _currentIpPeerGroup;

   private Ipv6BgpPeerGroup _currentIpv6PeerGroup;

   private Interface _currentIsisInterface;

   private IsisProcess _currentIsisProcess;

   private NamedBgpPeerGroup _currentNamedPeerGroup;

   private OspfProcess _currentOspfProcess;

   private BgpPeerGroup _currentPeerGroup;

   private NamedBgpPeerGroup _currentPeerSession;

   private PrefixList _currentPrefixList;

   private RouteMap _currentRouteMap;

   private RouteMapClause _currentRouteMapClause;

   private RoutePolicy _currentRoutePolicy;

   private StandardAccessList _currentStandardAcl;

   private StandardCommunityList _currentStandardCommunityList;

   private String _currentVrf;

   private BgpPeerGroup _dummyPeerGroup;

   private final BatfishCombinedParser<?, ?> _parser;

   private List<BgpPeerGroup> _peerGroupStack;

   private final String _text;

   private final Set<String> _unimplementedFeatures;

   private final boolean _unrecognizedAsRedFlag;

   private CiscoVendorConfiguration _vendorConfiguration;

   private final Warnings _w;

   public CiscoControlPlaneExtractor(String text,
         BatfishCombinedParser<?, ?> parser, Warnings warnings,
         boolean unrecognizedAsRedFlag) {
      _text = text;
      _parser = parser;
      _unimplementedFeatures = new TreeSet<String>();
      _w = warnings;
      _peerGroupStack = new ArrayList<BgpPeerGroup>();
      _unrecognizedAsRedFlag = unrecognizedAsRedFlag;
   }

   private void addInterface(String vrf, double bandwidth, String name) {
      Interface newInterface = _configuration.getInterfaces().get(name);
      if (newInterface == null) {
         newInterface = new Interface(name);
         _configuration.getInterfaces().put(name, newInterface);
      }
      else {
         _w.pedantic("Interface: \"" + name + "\" altered more than once");
      }
      _currentInterfaces.add(newInterface);
      newInterface.setBandwidth(bandwidth);
      newInterface.setVrf(vrf);
   }

   @Override
   public void enterAddress_family_header(Address_family_headerContext ctx) {
      if (ctx.VPNV4() != null || ctx.VPNV6() != null || ctx.IPV6() != null
            || ctx.MDT() != null || ctx.MULTICAST() != null
            || ctx.VRF() != null) {
         pushPeer(_dummyPeerGroup);
      }
      else {
         pushPeer(_currentPeerGroup);
      }
   }

   @Override
   public void enterCisco_configuration(Cisco_configurationContext ctx) {
      _vendorConfiguration = new CiscoVendorConfiguration(
            _unimplementedFeatures);
      _configuration = _vendorConfiguration;
      _currentVrf = CiscoConfiguration.MASTER_VRF_NAME;
   }

   @Override
   public void enterDescription_if_stanza(Description_if_stanzaContext ctx) {
      Token descriptionToken = ctx.description_line().text;
      String description = descriptionToken != null ? descriptionToken
            .getText().trim() : "";
      for (Interface currentInterface : _currentInterfaces) {
         currentInterface.setDescription(description);
      }
   }

   @Override
   public void enterExtended_access_list_stanza(
         Extended_access_list_stanzaContext ctx) {
      boolean ipv6 = (ctx.IPV6() != null);

      if (ipv6) {
         todo(ctx, F_IPV6);
      }

      String name;
      if (ctx.name != null) {
         name = ctx.name.getText();
      }
      else if (ctx.num != null) {
         name = ctx.num.getText();
      }
      else {
         throw new BatfishException("Could not determine acl name");
      }
      _currentExtendedAcl = _configuration.getExtendedAcls().get(name);
      if (_currentExtendedAcl == null) {
         _currentExtendedAcl = new ExtendedAccessList(name);
         _currentExtendedAcl.setIpv6(ipv6);
         _configuration.getExtendedAcls().put(name, _currentExtendedAcl);
      }
   }

   @Override
   public void enterInterface_is_stanza(Interface_is_stanzaContext ctx) {
      String ifaceName = ctx.iname.getText();
      _currentIsisInterface = _configuration.getInterfaces().get(ifaceName);
      if (_currentIsisInterface == null) {
         _w.redFlag("IS-IS process references nonexistent interface: \""
               + ifaceName + "\"");
         _currentIsisInterface = DUMMY_INTERFACE;
      }
      _currentIsisInterface.setIsisInterfaceMode(IsisInterfaceMode.ACTIVE);
   }

   @Override
   public void enterInterface_stanza(Interface_stanzaContext ctx) {
      String nameAlpha = ctx.iname.name_prefix_alpha.getText();
      String canonicalNamePrefix = getCanonicalInterfaceNamePrefix(nameAlpha);
      String vrf = canonicalNamePrefix.equals(NXOS_MANAGEMENT_INTERFACE_PREFIX) ? CiscoConfiguration.MANAGEMENT_VRF_NAME
            : CiscoConfiguration.MASTER_VRF_NAME;
      double bandwidth = Interface.getDefaultBandwidth(canonicalNamePrefix);
      String namePrefix = canonicalNamePrefix;
      for (Token part : ctx.iname.name_middle_parts) {
         namePrefix += part.getText();
      }
      _currentInterfaces = new ArrayList<Interface>();
      if (ctx.iname.range() != null) {
         List<SubRange> ranges = toRange(ctx.iname.range());
         for (SubRange range : ranges) {
            for (int i = range.getStart(); i <= range.getEnd(); i++) {
               String name = namePrefix + i;
               addInterface(vrf, bandwidth, name);
            }
         }
      }
      else {
         String name = namePrefix;
         addInterface(vrf, bandwidth, name);
      }
      if (ctx.MULTIPOINT() != null) {
         todo(ctx, F_INTERFACE_MULTIPOINT);
      }
   }

   @Override
   public void enterIp_as_path_access_list_stanza(
         Ip_as_path_access_list_stanzaContext ctx) {
      String name = ctx.name.getText();
      _currentAsPathAcl = _configuration.getAsPathAccessLists().get(name);
      if (_currentAsPathAcl == null) {
         _currentAsPathAcl = new IpAsPathAccessList(name);
         _configuration.getAsPathAccessLists().put(name, _currentAsPathAcl);
      }
   }

   @Override
   public void enterIp_community_list_expanded_stanza(
         Ip_community_list_expanded_stanzaContext ctx) {
      String name;
      if (ctx.num != null) {
         name = ctx.num.getText();
      }
      else if (ctx.name != null) {
         name = ctx.name.getText();
      }
      else {
         throw new BatfishException("Invalid community-list name");
      }
      _currentExpandedCommunityList = _configuration
            .getExpandedCommunityLists().get(name);
      if (_currentExpandedCommunityList == null) {
         _currentExpandedCommunityList = new ExpandedCommunityList(name);
         _configuration.getExpandedCommunityLists().put(name,
               _currentExpandedCommunityList);
      }
   }

   @Override
   public void enterIp_community_list_standard_stanza(
         Ip_community_list_standard_stanzaContext ctx) {
      String name;
      if (ctx.num != null) {
         name = ctx.num.getText();
      }
      else if (ctx.name != null) {
         name = ctx.name.getText();
      }
      else {
         throw new BatfishException("Invalid standard community-list name");
      }
      _currentStandardCommunityList = _configuration
            .getStandardCommunityLists().get(name);
      if (_currentStandardCommunityList == null) {
         _currentStandardCommunityList = new StandardCommunityList(name);
         _configuration.getStandardCommunityLists().put(name,
               _currentStandardCommunityList);
      }
   }

   @Override
   public void enterIp_prefix_list_stanza(Ip_prefix_list_stanzaContext ctx) {
      String name = ctx.name.getText();
      boolean isIpv6 = (ctx.IPV6() != null);
      if (isIpv6) {
         _currentPrefixList = null;
         todo(ctx, F_IPV6);
         return;
      }
      else {
         _currentPrefixList = _configuration.getPrefixLists().get(name);
         if (_currentPrefixList == null) {
            _currentPrefixList = new PrefixList(name);
            _configuration.getPrefixLists().put(name, _currentPrefixList);
         }
      }
   }

   @Override
   public void enterIp_route_stanza(Ip_route_stanzaContext ctx) {
      if (ctx.vrf != null) {
         _currentVrf = ctx.vrf.getText();
      }
   }

   @Override
   public void enterIs_type_is_stanza(Is_type_is_stanzaContext ctx) {
      IsisProcess proc = _configuration.getIsisProcess();
      if (ctx.LEVEL_1() != null) {
         proc.setLevel(IsisLevel.LEVEL_1);
      }
      else if (ctx.LEVEL_2_ONLY() != null || ctx.LEVEL_2() != null) {
         proc.setLevel(IsisLevel.LEVEL_2);
      }
      else {
         throw new BatfishException("Unsupported is-type");
      }
   }

   @Override
   public void enterNeighbor_group_rb_stanza(Neighbor_group_rb_stanzaContext ctx) {
      BgpProcess proc = _configuration.getBgpProcesses().get(_currentVrf);
      String name = ctx.name.getText();
      _currentNamedPeerGroup = proc.getNamedPeerGroups().get(name);
      if (_currentNamedPeerGroup == null) {
         proc.addNamedPeerGroup(name);
         _currentNamedPeerGroup = proc.getNamedPeerGroups().get(name);
      }
      pushPeer(_currentNamedPeerGroup);
   }

   @Override
   public void enterNeighbor_rb_stanza(Neighbor_rb_stanzaContext ctx) {
      // do no further processing for unsupported address families / containers
      if (_currentPeerGroup == _dummyPeerGroup) {
         pushPeer(_dummyPeerGroup);
         return;
      }
      BgpProcess proc = _configuration.getBgpProcesses().get(_currentVrf);
      // we must create peer group if it does not exist and this is a remote_as
      // declaration
      boolean create = ctx.remote_as_bgp_tail() != null
            || ctx.inherit_peer_session_bgp_tail() != null;
      if (ctx.ip != null) {
         Ip ip = toIp(ctx.ip);
         _currentIpPeerGroup = proc.getIpPeerGroups().get(ip);
         if (_currentIpPeerGroup == null) {
            if (create) {
               proc.addIpPeerGroup(ip);
               _currentIpPeerGroup = proc.getIpPeerGroups().get(ip);
               pushPeer(_currentIpPeerGroup);
            }
            else {
               String message = "reference to undeclared peer group: \""
                     + ip.toString() + "\"";
               _w.redFlag(message);
               pushPeer(_dummyPeerGroup);
            }
         }
         else {
            pushPeer(_currentIpPeerGroup);
         }
      }
      else if (ctx.ip6 != null) {
         todo(ctx, F_IPV6);
         _currentIpv6PeerGroup = Ipv6BgpPeerGroup.INSTANCE;
         pushPeer(_currentIpv6PeerGroup);
      }
      else if (ctx.peergroup != null) {
         String name = ctx.peergroup.getText();
         _currentNamedPeerGroup = proc.getNamedPeerGroups().get(name);
         if (_currentNamedPeerGroup == null) {
            if (create) {
               proc.addNamedPeerGroup(name);
               _currentNamedPeerGroup = proc.getNamedPeerGroups().get(name);
            }
            else {
               throw new BatfishException(
                     "reference to undeclared peer group: \"" + name + "\"");
            }
         }
         pushPeer(_currentNamedPeerGroup);
      }
      else {
         throw new BatfishException("unknown neighbor type");
      }
   }

   @Override
   public void enterNet_is_stanza(Net_is_stanzaContext ctx) {
      IsisProcess proc = _configuration.getIsisProcess();
      IsoAddress isoAddress = new IsoAddress(ctx.ISO_ADDRESS().getText());
      proc.setNetAddress(isoAddress);
   }

   @Override
   public void enterNexus_neighbor_rb_stanza(Nexus_neighbor_rb_stanzaContext ctx) {
      // do no further processing for unsupported address families / containers
      if (_currentPeerGroup == _dummyPeerGroup) {
         pushPeer(_dummyPeerGroup);
         return;
      }
      if (ctx.ipv6_address != null || ctx.ipv6_prefix != null) {
         todo(ctx, F_IPV6);
         _currentIpv6PeerGroup = Ipv6BgpPeerGroup.INSTANCE;
         pushPeer(_currentIpv6PeerGroup);
         return;
      }
      BgpProcess proc = _configuration.getBgpProcesses().get(_currentVrf);
      if (ctx.ip_address != null) {
         Ip ip = toIp(ctx.ip_address);
         _currentIpPeerGroup = proc.getIpPeerGroups().get(ip);
         if (_currentIpPeerGroup == null) {
            proc.addIpPeerGroup(ip);
            _currentIpPeerGroup = proc.getIpPeerGroups().get(ip);
         }
         pushPeer(_currentIpPeerGroup);
      }
      else if (ctx.ip_prefix != null) {
         Ip ip = getPrefixIp(ctx.ip_prefix);
         int prefixLength = getPrefixLength(ctx.ip_prefix);
         Prefix prefix = new Prefix(ip, prefixLength);
         _currentDynamicPeerGroup = proc.getDynamicPeerGroups().get(prefix);
         if (_currentDynamicPeerGroup == null) {
            _currentDynamicPeerGroup = proc.addDynamicPeerGroup(prefix);
         }
         pushPeer(_currentDynamicPeerGroup);
      }
      if (ctx.REMOTE_AS() != null) {
         int remoteAs = toInteger(ctx.asnum);
         _currentPeerGroup.setRemoteAs(remoteAs);
      }
      // TODO: verify if this is correct for nexus
      _currentPeerGroup.setActive(true);
      _currentPeerGroup.setShutdown(false);
   }

   @Override
   public void enterNexus_vrf_rb_stanza(Nexus_vrf_rb_stanzaContext ctx) {
      _currentVrf = ctx.name.getText();
      // BgpProcess masterProc =
      // _configuration.getBgpProcesses().get(BgpProcess.MASTER_VRF_NAME);
      BgpProcess proc = new BgpProcess(0); // TODO: fix vrf bgp process number
      _configuration.getBgpProcesses().put(_currentVrf, proc);
   }

   @Override
   public void enterRoute_map_stanza(Route_map_stanzaContext ctx) {
      String name = ctx.name.getText();
      _currentRouteMap = _configuration.getRouteMaps().get(name);
      if (_currentRouteMap == null) {
         _currentRouteMap = new RouteMap(name);
         _configuration.getRouteMaps().put(name, _currentRouteMap);
      }
      int num = toInteger(ctx.num);
      LineAction action = getAccessListAction(ctx.rmt);
      _currentRouteMapClause = _currentRouteMap.getClauses().get(num);
      if (_currentRouteMapClause == null) {
         _currentRouteMapClause = new RouteMapClause(action,
               _currentRouteMap.getName(), num);
         _currentRouteMap.getClauses().put(num, _currentRouteMapClause);
      }
      else {
         _w.redFlag("Route map '" + _currentRouteMap.getName()
               + "' already contains clause numbered '" + num
               + "'. Duplicate clause will be merged with original clause.");
      }
   }

   @Override
   public void enterRoute_policy_stanza(Route_policy_stanzaContext ctx) {
      String name = ctx.name.getText();
      _currentRoutePolicy = _configuration.getRoutePolicies().get(name);
      if (_currentRoutePolicy == null) {
         _currentRoutePolicy = new RoutePolicy(name);
         _configuration.getRoutePolicies().put(name, _currentRoutePolicy);
      }

      List<RoutePolicyStatement> stmts = _currentRoutePolicy.getStatements();

      stmts.addAll(toRoutePolicyStatementList(ctx.route_policy_tail().stanzas));
   }

   @Override
   public void enterRouter_bgp_stanza(Router_bgp_stanzaContext ctx) {
      int procNum = toInteger(ctx.procnum);
      BgpProcess proc = new BgpProcess(procNum);
      _configuration.getBgpProcesses().put(_currentVrf, proc);
      _dummyPeerGroup = new MasterBgpPeerGroup();
      pushPeer(proc.getMasterBgpPeerGroup());
   }

   @Override
   public void enterRouter_isis_stanza(Router_isis_stanzaContext ctx) {
      _currentIsisProcess = new IsisProcess();
      _currentIsisProcess.setLevel(IsisLevel.LEVEL_1_2);
      _configuration.setIsisProcess(_currentIsisProcess);
   }

   @Override
   public void enterRouter_ospf_stanza(Router_ospf_stanzaContext ctx) {
      int procNum = toInteger(ctx.procnum);
      _currentOspfProcess = new OspfProcess(procNum);
      if (ctx.vrf != null) {
         todo(ctx, F_OSPF_VRF);
      }
      else {
         _configuration.setOspfProcess(_currentOspfProcess);
      }
   }

   @Override
   public void enterRouter_rip_stanza(Router_rip_stanzaContext ctx) {
      todo(ctx, F_RIP);
   }

   @Override
   public void enterStandard_access_list_stanza(
         Standard_access_list_stanzaContext ctx) {
      String name;
      if (ctx.name != null) {
         name = ctx.name.getText();
      }
      else if (ctx.num != null) {
         name = ctx.num.getText();
      }
      else {
         throw new BatfishException("Invalid standard access-list name");
      }
      _currentStandardAcl = _configuration.getStandardAcls().get(name);
      if (_currentStandardAcl == null) {
         _currentStandardAcl = new StandardAccessList(name);
         _configuration.getStandardAcls().put(name, _currentStandardAcl);
      }
   }

   @Override
   public void enterTemplate_peer_rb_stanza(Template_peer_rb_stanzaContext ctx) {
      String name = ctx.name.getText();
      BgpProcess proc = _configuration.getBgpProcesses().get(_currentVrf);
      _currentNamedPeerGroup = proc.getNamedPeerGroups().get(name);
      if (_currentNamedPeerGroup == null) {
         proc.addNamedPeerGroup(name);
         _currentNamedPeerGroup = proc.getNamedPeerGroups().get(name);
      }
      pushPeer(_currentNamedPeerGroup);
   }

   @Override
   public void enterTemplate_peer_session_rb_stanza(
         Template_peer_session_rb_stanzaContext ctx) {
      String name = ctx.name.getText();
      BgpProcess proc = _configuration.getBgpProcesses().get(_currentVrf);
      _currentPeerSession = proc.getPeerSessions().get(name);
      if (_currentPeerSession == null) {
         proc.addPeerSession(name);
         _currentPeerSession = proc.getPeerSessions().get(name);
      }
      pushPeer(_currentPeerSession);
   }

   @Override
   public void enterVrf_context_stanza(Vrf_context_stanzaContext ctx) {
      _currentVrf = ctx.name.getText();
   }

   @Override
   public void exitActivate_bgp_tail(Activate_bgp_tailContext ctx) {
      if (_currentPeerGroup == null) {
         return;
      }
      BgpProcess proc = _configuration.getBgpProcesses().get(_currentVrf);
      if (_currentPeerGroup != proc.getMasterBgpPeerGroup()) {
         _currentPeerGroup.setActive(true);
      }
      else {
         throw new BatfishException(
               "no peer or peer group to activate in this context");
      }
   }

   @Override
   public void exitAddress_family_rb_stanza(Address_family_rb_stanzaContext ctx) {
      popPeer();
   }

   @Override
   public void exitAggregate_address_rb_stanza(
         Aggregate_address_rb_stanzaContext ctx) {
      BgpProcess proc = _configuration.getBgpProcesses().get(_currentVrf);
      if (_currentPeerGroup == proc.getMasterBgpPeerGroup()) {
         boolean summaryOnly = ctx.summary_only != null;
         boolean asSet = ctx.as_set != null;
         if (ctx.network != null || ctx.prefix != null) {
            // ipv4
            Prefix prefix;
            if (ctx.network != null) {
               Ip network = toIp(ctx.network);
               Ip subnet = toIp(ctx.subnet);
               int prefixLength = subnet.numSubnetBits();
               prefix = new Prefix(network, prefixLength);
            }
            else {
               // ctx.prefix != null
               prefix = new Prefix(ctx.prefix.getText());
            }
            BgpAggregateNetwork net = new BgpAggregateNetwork(prefix);
            net.setAsSet(asSet);
            net.setSummaryOnly(summaryOnly);
            if (ctx.mapname != null) {
               String mapName = ctx.mapname.getText();
               net.setAttributeMap(mapName);
            }
            proc.getAggregateNetworks().put(prefix, net);
         }
         else if (ctx.ipv6_prefix != null) {
            todo(ctx, F_IPV6);
         }
      }
      else if (_currentIpPeerGroup != null || _currentNamedPeerGroup != null) {
         throw new BatfishException(
               "unexpected occurrence in peer group/neighbor context");

      }
   }

   @Override
   public void exitAllowas_in_bgp_tail(Allowas_in_bgp_tailContext ctx) {
      _currentPeerGroup.setAllowAsIn(true);
      if (ctx.num != null) {
         todo(ctx, F_ALLOWAS_IN_NUMBER);
      }
   }

   @Override
   public void exitAlways_compare_med_rb_stanza(
         Always_compare_med_rb_stanzaContext ctx) {
      BgpProcess proc = _configuration.getBgpProcesses().get(_currentVrf);
      proc.setAlwaysCompareMed(true);
   }

   @Override
   public void exitArea_nssa_ro_stanza(Area_nssa_ro_stanzaContext ctx) {
      OspfProcess proc = _currentOspfProcess;
      int area = (ctx.area_int != null) ? toInteger(ctx.area_int) : (int) toIp(
            ctx.area_ip).asLong();
      boolean noSummary = ctx.NO_SUMMARY() != null;
      boolean defaultOriginate = ctx.DEFAULT_INFORMATION_ORIGINATE() != null;
      if (defaultOriginate) {
         todo(ctx, F_OSPF_AREA_NSSA);
      }
      proc.getNssas().put(area, noSummary);
   }

   @Override
   public void exitAuto_summary_bgp_tail(Auto_summary_bgp_tailContext ctx) {
      todo(ctx, F_BGP_AUTO_SUMMARY);
   }

   @Override
   public void exitBgp_advertise_inactive_rb_stanza(
         Bgp_advertise_inactive_rb_stanzaContext ctx) {
      _currentPeerGroup.setAdvertiseInactive(true);
   }

   @Override
   public void exitBgp_listen_range_rb_stanza(
         Bgp_listen_range_rb_stanzaContext ctx) {
      String name = ctx.name.getText();
      BgpProcess proc = _configuration.getBgpProcesses().get(_currentVrf);
      if (ctx.IP_PREFIX() != null) {
         Ip ip = getPrefixIp(ctx.IP_PREFIX().getSymbol());
         int prefixLength = getPrefixLength(ctx.IP_PREFIX().getSymbol());
         Prefix prefix = new Prefix(ip, prefixLength);
         DynamicBgpPeerGroup pg = proc.addDynamicPeerGroup(prefix);
         NamedBgpPeerGroup namedGroup = proc.getNamedPeerGroups().get(name);
         if (namedGroup == null) {
            proc.addNamedPeerGroup(name);
            namedGroup = proc.getNamedPeerGroups().get(name);
         }
         namedGroup.addNeighborPrefix(prefix);
         if (ctx.as != null) {
            int remoteAs = toInteger(ctx.as);
            pg.setRemoteAs(remoteAs);
         }
      }
      else if (ctx.IPV6_PREFIX() != null) {
         todo(ctx, F_IPV6);
      }
   }

   @Override
   public void exitCluster_id_bgp_tail(Cluster_id_bgp_tailContext ctx) {
      Ip clusterId = null;
      if (ctx.DEC() != null) {
         long ipAsLong = Long.parseLong(ctx.DEC().getText());
         clusterId = new Ip(ipAsLong);
      }
      else if (ctx.IP_ADDRESS() != null) {
         clusterId = toIp(ctx.IP_ADDRESS());
      }
      _currentPeerGroup.setClusterId(clusterId);
   }

   @Override
   public void exitCmm_access_group(Cmm_access_groupContext ctx) {
      String name;
      if (ctx.name != null) {
         name = ctx.name.getText();
      }
      else {
         name = ctx.num.getText();
      }
      _configuration.getClassMapAccessGroups().add(name);
   }

   @Override
   public void exitCp_ip_access_group(Cp_ip_access_groupContext ctx) {
      String name = ctx.name.getText();
      _configuration.getControlPlaneAccessGroups().add(name);
   }

   @Override
   public void exitDefault_information_ro_stanza(
         Default_information_ro_stanzaContext ctx) {
      OspfProcess proc = _currentOspfProcess;
      proc.setDefaultInformationOriginate(true);
      boolean always = ctx.ALWAYS().size() > 0;
      proc.setDefaultInformationOriginateAlways(always);
      if (ctx.metric != null) {
         int metric = toInteger(ctx.metric);
         proc.setDefaultInformationMetric(metric);
      }
      if (ctx.metric_type != null) {
         int metricTypeInt = toInteger(ctx.metric_type);
         OspfMetricType metricType = OspfMetricType.fromInteger(metricTypeInt);
         proc.setDefaultInformationMetricType(metricType);
      }
      if (ctx.map != null) {
         proc.setDefaultInformationOriginateMap(ctx.map.getText());
      }
   }

   @Override
   public void exitDefault_metric_bgp_tail(Default_metric_bgp_tailContext ctx) {
      int metric = toInteger(ctx.metric);
      _currentPeerGroup.setDefaultMetric(metric);
   }

   @Override
   public void exitDefault_originate_bgp_tail(
         Default_originate_bgp_tailContext ctx) {
      String mapName = ctx.map != null ? ctx.map.getText() : null;
      if (_currentIpv6PeerGroup != null) {
         todo(ctx, F_IPV6);
         return;
      }
      else {
         _currentPeerGroup.setDefaultOriginate(true);
         _currentPeerGroup.setDefaultOriginateMap(mapName);
      }
   }

   @Override
   public void exitDefault_shutdown_bgp_tail(
         Default_shutdown_bgp_tailContext ctx) {
      _currentPeerGroup.setShutdown(true);
   }

   @Override
   public void exitDescription_bgp_tail(Description_bgp_tailContext ctx) {
      String description = ctx.description_line().text.getText().trim();
      _currentPeerGroup.setDescription(description);
   }

   @Override
   public void exitDisable_peer_as_check_bgp_tail(
         Disable_peer_as_check_bgp_tailContext ctx) {
      _currentPeerGroup.setDisablePeerAsCheck(true);
   }

   @Override
   public void exitDistribute_list_bgp_tail(Distribute_list_bgp_tailContext ctx) {
      todo(ctx, F_BGP_NEIGHBOR_DISTRIBUTE_LIST);
   }

   @Override
   public void exitEbgp_multihop_bgp_tail(Ebgp_multihop_bgp_tailContext ctx) {
      _currentPeerGroup.setEbgpMultihop(true);
   }

   @Override
   public void exitExtended_access_list_stanza(
         Extended_access_list_stanzaContext ctx) {
      _currentExtendedAcl = null;
   }

   @Override
   public void exitExtended_access_list_tail(
         Extended_access_list_tailContext ctx) {

      if (_currentExtendedAcl.getIpv6()) {
         return;
      }
      LineAction action = getAccessListAction(ctx.ala);
      IpProtocol protocol = toIpProtocol(ctx.prot);
      switch (protocol) {
      case IPv6:
      case IPv6_Frag:
      case IPv6_ICMP:
      case IPv6_NoNxt:
      case IPv6_Opts:
      case IPv6_Route:
         _currentExtendedAcl.setIpv6(true);
         // $CASES-OMITTED$
      default:
         break;
      }
      Ip srcIp = getIp(ctx.srcipr);
      Ip srcWildcard = getWildcard(ctx.srcipr);
      Ip dstIp = getIp(ctx.dstipr);
      Ip dstWildcard = getWildcard(ctx.dstipr);
      String srcAddressGroup = getAddressGroup(ctx.srcipr);
      String dstAddressGroup = getAddressGroup(ctx.dstipr);
      List<SubRange> srcPortRanges = ctx.alps_src != null ? getPortRanges(ctx.alps_src)
            : Collections.<SubRange> emptyList();
      List<SubRange> dstPortRanges = ctx.alps_dst != null ? getPortRanges(ctx.alps_dst)
            : Collections.<SubRange> emptyList();
      Integer icmpType = null;
      Integer icmpCode = null;
      List<TcpFlags> tcpFlags = new ArrayList<TcpFlags>();
      Set<Integer> dscps = new TreeSet<Integer>();
      Set<Integer> ecns = new TreeSet<Integer>();
      for (Extended_access_list_additional_featureContext feature : ctx.features) {
         if (feature.ACK() != null) {
            TcpFlags alt = new TcpFlags();
            alt.setUseAck(true);
            alt.setAck(true);
            tcpFlags.add(alt);
         }
         if (feature.DSCP() != null) {
            int dscpType = toDscpType(feature.dscp_type());
            dscps.add(dscpType);
         }
         if (feature.ECHO_REPLY() != null) {
            icmpType = IcmpType.ECHO_REPLY;
            icmpCode = IcmpCode.ECHO_REPLY;
         }
         if (feature.ECHO() != null) {
            icmpType = IcmpType.ECHO_REQUEST;
            icmpCode = IcmpCode.ECHO_REQUEST;
         }
         if (feature.ECN() != null) {
            int ecn = toInteger(feature.ecn);
            ecns.add(ecn);
         }
         if (feature.ESTABLISHED() != null) {
            // must contain ACK or RST
            TcpFlags alt1 = new TcpFlags();
            TcpFlags alt2 = new TcpFlags();
            alt1.setUseAck(true);
            alt1.setAck(true);
            alt2.setUseRst(true);
            alt2.setRst(true);
            tcpFlags.add(alt1);
            tcpFlags.add(alt2);
         }
         if (feature.FRAGMENTS() != null) {
            todo(ctx, F_FRAGMENTS);
         }
         if (feature.HOST_UNKNOWN() != null) {
            icmpType = IcmpType.DESTINATION_UNREACHABLE;
            icmpCode = IcmpCode.DESTINATION_HOST_UNKNOWN;
         }
         if (feature.HOST_UNREACHABLE() != null) {
            icmpType = IcmpType.DESTINATION_UNREACHABLE;
            icmpCode = IcmpCode.DESTINATION_HOST_UNREACHABLE;
         }
         if (feature.NETWORK_UNKNOWN() != null) {
            icmpType = IcmpType.DESTINATION_UNREACHABLE;
            icmpCode = IcmpCode.DESTINATION_NETWORK_UNKNOWN;
         }
         if (feature.NET_UNREACHABLE() != null) {
            icmpType = IcmpType.DESTINATION_UNREACHABLE;
            icmpCode = IcmpCode.DESTINATION_NETWORK_UNREACHABLE;
         }
         if (feature.PACKET_TOO_BIG() != null) {
            icmpType = IcmpType.DESTINATION_UNREACHABLE;
            icmpCode = IcmpCode.PACKET_TOO_BIG;
         }
         if (feature.PARAMETER_PROBLEM() != null) {
            icmpType = IcmpType.PARAMETER_PROBLEM;
         }
         if (feature.PORT_UNREACHABLE() != null) {
            icmpType = IcmpType.DESTINATION_UNREACHABLE;
            icmpCode = IcmpCode.DESTINATION_PORT_UNREACHABLE;
         }
         if (feature.REDIRECT() != null) {
            icmpType = IcmpType.REDIRECT_MESSAGE;
         }
         if (feature.RST() != null) {
            TcpFlags alt = new TcpFlags();
            alt.setUseRst(true);
            alt.setRst(true);
            tcpFlags.add(alt);
         }
         if (feature.SOURCE_QUENCH() != null) {
            icmpType = IcmpType.SOURCE_QUENCH;
            icmpCode = IcmpCode.SOURCE_QUENCH;
         }
         if (feature.TIME_EXCEEDED() != null) {
            icmpType = IcmpType.TIME_EXCEEDED;
         }
         if (feature.TTL() != null) {
            todo(ctx, F_TTL);
         }
         if (feature.TTL_EXCEEDED() != null) {
            icmpType = IcmpType.TIME_EXCEEDED;
            icmpCode = IcmpCode.TTL_EXCEEDED;
         }
         if (feature.TRACEROUTE() != null) {
            icmpType = IcmpType.TRACEROUTE;
            icmpCode = IcmpCode.TRACEROUTE;
         }
         if (feature.UNREACHABLE() != null) {
            icmpType = IcmpType.DESTINATION_UNREACHABLE;
         }
      }
      String name;
      if (ctx.num != null) {
         name = ctx.num.getText();
      }
      else {
         name = getFullText(ctx).trim();
      }
      ExtendedAccessListLine line = new ExtendedAccessListLine(name, action,
            protocol, new IpWildcard(srcIp, srcWildcard), srcAddressGroup,
            new IpWildcard(dstIp, dstWildcard), dstAddressGroup, srcPortRanges,
            dstPortRanges, dscps, ecns, icmpType, icmpCode, tcpFlags);
      _currentExtendedAcl.addLine(line);
   }

   @Override
   public void exitHostname_stanza(Hostname_stanzaContext ctx) {
      String hostname = ctx.name.getText();
      _configuration.setHostname(hostname);
   }

   @Override
   public void exitInherit_peer_session_bgp_tail(
         Inherit_peer_session_bgp_tailContext ctx) {
      BgpProcess proc = _configuration.getBgpProcesses().get(_currentVrf);
      String groupName = ctx.name.getText();
      if (_currentIpPeerGroup != null) {
         _currentIpPeerGroup.setGroupName(groupName);
      }
      else if (_currentNamedPeerGroup != null) {
         _currentNamedPeerGroup.setPeerSession(groupName);
      }
      else if (_currentPeerGroup == proc.getMasterBgpPeerGroup()) {
         throw new BatfishException("Invalid peer context for inheritance");
      }
      else {
         todo(ctx, F_BGP_INHERIT_PEER_SESSION_OTHER);
      }
   }

   @Override
   public void exitInterface_is_stanza(Interface_is_stanzaContext ctx) {
      _currentIsisInterface = null;
   }

   @Override
   public void exitInterface_stanza(Interface_stanzaContext ctx) {
      _currentInterfaces = null;
   }

   @Override
   public void exitIp_access_group_if_stanza(
         Ip_access_group_if_stanzaContext ctx) {
      String name = ctx.name.getText();
      if (ctx.IN() != null || ctx.INGRESS() != null) {
         for (Interface currentInterface : _currentInterfaces) {
            currentInterface.setIncomingFilter(name);
         }
      }
      else if (ctx.OUT() != null || ctx.EGRESS() != null) {
         for (Interface currentInterface : _currentInterfaces) {
            currentInterface.setOutgoingFilter(name);
         }
      }
      else {
         throw new BatfishException("bad direction");
      }
   }

   @Override
   public void exitIp_address_if_stanza(Ip_address_if_stanzaContext ctx) {
      Prefix prefix;
      if (ctx.prefix != null) {
         prefix = new Prefix(ctx.prefix.getText());
      }
      else {
         Ip address = new Ip(ctx.ip.getText());
         Ip mask = new Ip(ctx.subnet.getText());
         prefix = new Prefix(address, mask);
      }
      for (Interface currentInterface : _currentInterfaces) {
         currentInterface.setPrefix(prefix);
      }
   }

   @Override
   public void exitIp_address_secondary_if_stanza(
         Ip_address_secondary_if_stanzaContext ctx) {
      Ip address;
      Ip mask;
      Prefix prefix;
      if (ctx.prefix != null) {
         prefix = new Prefix(ctx.prefix.getText());
      }
      else {
         address = new Ip(ctx.ip.getText());
         mask = new Ip(ctx.subnet.getText());
         prefix = new Prefix(address, mask.numSubnetBits());
      }
      for (Interface currentInterface : _currentInterfaces) {
         currentInterface.getSecondaryPrefixes().add(prefix);
      }
   }

   @Override
   public void exitIp_as_path_access_list_stanza(
         Ip_as_path_access_list_stanzaContext ctx) {
      _currentAsPathAcl = null;
   }

   @Override
   public void exitIp_as_path_access_list_tail(
         Ip_as_path_access_list_tailContext ctx) {
      LineAction action = getAccessListAction(ctx.action);
      As_path_regexContext asPath = ctx.as_path_regex();
      if (asPath == null) {
         // not an as-path we can use right now
         return;
      }
      IpAsPathAccessListLine line = new IpAsPathAccessListLine(action);
      boolean atBeginning = asPath.CARAT() != null;
      boolean matchEmpty = asPath.ranges.size() == asPath.ASTERISK().size();
      line.setAtBeginning(atBeginning);
      line.setMatchEmpty(matchEmpty);
      switch (asPath.ranges.size()) {
      case 0:
         break;

      case 2:
         As_path_regex_rangeContext range2ctx = asPath.ranges.get(1);
         SubRange asRange2 = toSubrange(range2ctx);
         line.setAs2Range(asRange2);
      case 1:
         As_path_regex_rangeContext range1ctx = asPath.ranges.get(0);
         SubRange asRange1 = toSubrange(range1ctx);
         line.setAs1Range(asRange1);
         break;

      default:
         _w.redFlag("Do not currently support more than two AS'es in Cisco as-path regexes");
      }
      _currentAsPathAcl.addLine(line);
   }

   @Override
   public void exitIp_community_list_expanded_stanza(
         Ip_community_list_expanded_stanzaContext ctx) {
      _currentExpandedCommunityList = null;
   }

   @Override
   public void exitIp_community_list_expanded_tail(
         Ip_community_list_expanded_tailContext ctx) {
      LineAction action = getAccessListAction(ctx.ala);
      String regex = "";
      for (Token remainder : ctx.remainder) {
         regex += remainder.getText();
      }
      ExpandedCommunityListLine line = new ExpandedCommunityListLine(action,
            regex);
      _currentExpandedCommunityList.addLine(line);
   }

   @Override
   public void exitIp_community_list_standard_stanza(
         Ip_community_list_standard_stanzaContext ctx) {
      _currentStandardCommunityList = null;
   }

   @Override
   public void exitIp_community_list_standard_tail(
         Ip_community_list_standard_tailContext ctx) {
      LineAction action = getAccessListAction(ctx.ala);
      List<Long> communities = new ArrayList<Long>();
      for (CommunityContext communityCtx : ctx.communities) {
         long community = toLong(communityCtx);
         communities.add(community);
      }
      StandardCommunityListLine line = new StandardCommunityListLine(action,
            communities);
      _currentStandardCommunityList.getLines().add(line);
   }

   @Override
   public void exitIp_default_gateway_stanza(
         Ip_default_gateway_stanzaContext ctx) {
      todo(ctx, F_IP_DEFAULT_GATEWAY);
   }

   @Override
   public void exitIp_ospf_cost_if_stanza(Ip_ospf_cost_if_stanzaContext ctx) {
      int cost = toInteger(ctx.cost);
      for (Interface currentInterface : _currentInterfaces) {
         currentInterface.setOspfCost(cost);
      }
   }

   @Override
   public void exitIp_ospf_dead_interval_if_stanza(
         Ip_ospf_dead_interval_if_stanzaContext ctx) {
      int seconds = toInteger(ctx.seconds);
      for (Interface currentInterface : _currentInterfaces) {
         currentInterface.setOspfDeadInterval(seconds);
         currentInterface.setOspfHelloMultiplier(0);
      }
   }

   @Override
   public void exitIp_ospf_dead_interval_minimal_if_stanza(
         Ip_ospf_dead_interval_minimal_if_stanzaContext ctx) {
      int multiplier = toInteger(ctx.mult);
      for (Interface currentInterface : _currentInterfaces) {
         currentInterface.setOspfDeadInterval(1);
         currentInterface.setOspfHelloMultiplier(multiplier);
      }
   }

   @Override
   public void exitIp_policy_if_stanza(Ip_policy_if_stanzaContext ctx) {
      String policyName = ctx.name.getText();
      for (Interface currentInterface : _currentInterfaces) {
         currentInterface.setRoutingPolicy(policyName);
      }
   }

   @Override
   public void exitIp_prefix_list_stanza(Ip_prefix_list_stanzaContext ctx) {
      _currentPrefixList = null;
   }

   @Override
   public void exitIp_prefix_list_tail(Ip_prefix_list_tailContext ctx) {
      boolean ipv6 = ctx.ipv6_prefix != null;
      LineAction action = getAccessListAction(ctx.action);
      Prefix prefix = null;
      Prefix6 prefix6 = null;
      int prefixLength;
      if (ipv6) {
         prefix6 = new Prefix6(ctx.ipv6_prefix.getText());
         prefixLength = prefix6.getPrefixLength();
      }
      else {
         prefix = new Prefix(ctx.prefix.getText());
         prefixLength = prefix.getPrefixLength();
      }
      int minLen = prefixLength;
      int maxLen = prefixLength;
      if (ctx.minpl != null) {
         minLen = toInteger(ctx.minpl);
         maxLen = ipv6 ? 128 : 32;
      }
      if (ctx.maxpl != null) {
         maxLen = toInteger(ctx.maxpl);
      }
      if (ctx.eqpl != null) {
         minLen = toInteger(ctx.eqpl);
         maxLen = toInteger(ctx.eqpl);
      }
      SubRange lengthRange = new SubRange(minLen, maxLen);
      if (ipv6) {
         todo(ctx, F_IPV6);
      }
      else {
         PrefixListLine line = new PrefixListLine(action, prefix, lengthRange);
         _currentPrefixList.addLine(line);
      }
   }

   @Override
   public void exitIp_route_stanza(Ip_route_stanzaContext ctx) {
      if (ctx.vrf != null) {
         _currentVrf = CiscoConfiguration.MASTER_VRF_NAME;
      }
   }

   @Override
   public void exitIp_route_tail(Ip_route_tailContext ctx) {
      if (!_currentVrf.equals(CiscoConfiguration.MASTER_VRF_NAME)) {
         todo(ctx, F_IP_ROUTE_VRF);
         return;
      }
      Prefix prefix;
      if (ctx.prefix != null) {
         prefix = new Prefix(ctx.prefix.getText());
      }
      else {
         Ip address = toIp(ctx.address);
         Ip mask = toIp(ctx.mask);
         int prefixLength = mask.numSubnetBits();
         prefix = new Prefix(address, prefixLength);
      }
      Ip nextHopIp = null;
      String nextHopInterface = null;
      int distance = DEFAULT_STATIC_ROUTE_DISTANCE;
      Integer tag = null;
      Integer track = null;
      boolean permanent = ctx.perm != null;
      if (ctx.nexthopip != null) {
         nextHopIp = toIp(ctx.nexthopip);
      }
      else if (ctx.nexthopprefix != null) {
         nextHopIp = getPrefixIp(ctx.nexthopprefix);
      }
      if (ctx.nexthopint != null) {
         nextHopInterface = ctx.nexthopint.getText();
      }
      if (ctx.distance != null) {
         distance = toInteger(ctx.distance);
      }
      if (ctx.tag != null) {
         tag = toInteger(ctx.tag);
      }
      if (ctx.track != null) {
         track = toInteger(ctx.track);
      }
      StaticRoute route = new StaticRoute(prefix, nextHopIp, nextHopInterface,
            distance, tag, track, permanent);
      _configuration.getStaticRoutes().add(route);
   }

   @Override
   public void exitIp_route_vrfc_stanza(Ip_route_vrfc_stanzaContext ctx) {
      todo(ctx, F_IP_ROUTE_VRF);
   }

   @Override
   public void exitIp_router_isis_if_stanza(Ip_router_isis_if_stanzaContext ctx) {
      for (Interface iface : _currentInterfaces) {
         iface.setIsisInterfaceMode(IsisInterfaceMode.ACTIVE);
      }
   }

   @Override
   public void exitIsis_metric_if_stanza(Isis_metric_if_stanzaContext ctx) {
      int metric = toInteger(ctx.metric);
      for (Interface iface : _currentInterfaces) {
         iface.setIsisCost(metric);
      }
   }

   @Override
   public void exitL_access_class(L_access_classContext ctx) {
      String name = ctx.name.getText();
      _configuration.getLineAccessClassLists().add(name);
   }

   @Override
   public void exitMatch_as_path_access_list_rm_stanza(
         Match_as_path_access_list_rm_stanzaContext ctx) {
      Set<String> names = new TreeSet<String>();
      for (VariableContext name : ctx.name_list) {
         names.add(name.getText());
      }
      RouteMapMatchAsPathAccessListLine line = new RouteMapMatchAsPathAccessListLine(
            names);
      _currentRouteMapClause.addMatchLine(line);
   }

   @Override
   public void exitMatch_community_list_rm_stanza(
         Match_community_list_rm_stanzaContext ctx) {
      Set<String> names = new TreeSet<String>();
      for (VariableContext name : ctx.name_list) {
         names.add(name.getText());
      }
      RouteMapMatchCommunityListLine line = new RouteMapMatchCommunityListLine(
            names);
      _currentRouteMapClause.addMatchLine(line);
   }

   @Override
   public void exitMatch_ip_access_list_rm_stanza(
         Match_ip_access_list_rm_stanzaContext ctx) {
      Set<String> names = new TreeSet<String>();
      for (Token t : ctx.name_list) {
         names.add(t.getText());
      }
      RouteMapMatchIpAccessListLine line = new RouteMapMatchIpAccessListLine(
            names);
      _currentRouteMapClause.addMatchLine(line);
   }

   @Override
   public void exitMatch_ip_prefix_list_rm_stanza(
         Match_ip_prefix_list_rm_stanzaContext ctx) {
      Set<String> names = new TreeSet<String>();
      for (Token t : ctx.name_list) {
         names.add(t.getText());
      }
      RouteMapMatchIpPrefixListLine line = new RouteMapMatchIpPrefixListLine(
            names);
      _currentRouteMapClause.addMatchLine(line);
   }

   @Override
   public void exitMatch_ipv6_rm_stanza(Match_ipv6_rm_stanzaContext ctx) {
      _currentRouteMap.setIpv6(true);
   }

   @Override
   public void exitMatch_tag_rm_stanza(Match_tag_rm_stanzaContext ctx) {
      Set<Integer> tags = new TreeSet<Integer>();
      for (Token t : ctx.tag_list) {
         tags.add(toInteger(t));
      }
      RouteMapMatchTagLine line = new RouteMapMatchTagLine(tags);
      _currentRouteMapClause.addMatchLine(line);
   }

   @Override
   public void exitMaximum_paths_ro_stanza(Maximum_paths_ro_stanzaContext ctx) {
      todo(ctx, F_OSPF_MAXIMUM_PATHS);
      /*
       * Note that this is very difficult to enforce, and may not help the
       * analysis without major changes
       */
   }

   @Override
   public void exitMaximum_peers_bgp_tail(Maximum_peers_bgp_tailContext ctx) {
      todo(ctx, F_BGP_MAXIMUM_PEERS);
   }

   @Override
   public void exitMgmt_ip_access_group(Mgmt_ip_access_groupContext ctx) {
      String name = ctx.name.getText();
      _configuration.getManagementAccessGroups().add(name);
   }

   @Override
   public void exitNeighbor_group_rb_stanza(Neighbor_group_rb_stanzaContext ctx) {
      _currentIpPeerGroup = null;
      _currentIpv6PeerGroup = null;
      _currentNamedPeerGroup = null;
      popPeer();
   }

   @Override
   public void exitNeighbor_rb_stanza(Neighbor_rb_stanzaContext ctx) {
      _currentDynamicPeerGroup = null;
      _currentIpPeerGroup = null;
      _currentIpv6PeerGroup = null;
      _currentNamedPeerGroup = null;
      popPeer();
   }

   @Override
   public void exitNetwork_bgp_tail(Network_bgp_tailContext ctx) {
      if (ctx.mapname != null) {
         todo(ctx, F_BGP_NETWORK_ROUTE_MAP);
      }
      else {
         Prefix prefix;

         if (ctx.prefix != null) {
            prefix = new Prefix(ctx.prefix.getText());
         }
         else {
            Ip address = toIp(ctx.ip);
            Ip mask = (ctx.mask != null) ? toIp(ctx.mask) : address
                  .getClassMask();
            int prefixLength = mask.numSubnetBits();
            prefix = new Prefix(address, prefixLength);
         }
         _configuration.getBgpProcesses().get(_currentVrf).getNetworks()
               .add(prefix);
      }
   }

   @Override
   public void exitNetwork_ro_stanza(Network_ro_stanzaContext ctx) {
      Ip address;
      Ip wildcard;
      if (ctx.prefix != null) {
         Prefix prefix = new Prefix(ctx.prefix.getText());
         address = prefix.getAddress();
         wildcard = prefix.getPrefixWildcard();
      }
      else {
         address = toIp(ctx.ip);
         wildcard = toIp(ctx.wildcard);
      }
      long area;
      if (ctx.area_int != null) {
         area = toLong(ctx.area_int);
      }
      else if (ctx.area_ip != null) {
         area = toIp(ctx.area_ip).asLong();
      }
      else {
         throw new BatfishException("bad area");
      }
      OspfWildcardNetwork network = new OspfWildcardNetwork(address, wildcard,
            area);
      _currentOspfProcess.getWildcardNetworks().add(network);
   }

   @Override
   public void exitNext_hop_self_bgp_tail(Next_hop_self_bgp_tailContext ctx) {
      todo(ctx, F_BGP_NEXT_HOP_SELF);
      // note that this rule matches "no next-hop-self"
   }

   @Override
   public void exitNexus_neighbor_address_family(
         Nexus_neighbor_address_familyContext ctx) {
      popPeer();
   }

   @Override
   public void exitNexus_neighbor_inherit(Nexus_neighbor_inheritContext ctx) {
      BgpProcess proc = _configuration.getBgpProcesses().get(_currentVrf);
      String groupName = ctx.name.getText();
      if (_currentIpPeerGroup != null) {
         _currentIpPeerGroup.setGroupName(groupName);
      }
      else if (_currentDynamicPeerGroup != null) {
         _currentDynamicPeerGroup.setGroupName(groupName);
      }
      else if (_currentPeerGroup == proc.getMasterBgpPeerGroup()) {
         throw new BatfishException("Invalid peer context for inheritance");
      }
      else {
         todo(ctx, F_BGP_INHERIT_PEER_OTHER);
      }
   }

   @Override
   public void exitNexus_neighbor_rb_stanza(Nexus_neighbor_rb_stanzaContext ctx) {
      _currentDynamicPeerGroup = null;
      _currentIpPeerGroup = null;
      _currentIpv6PeerGroup = null;
      _currentNamedPeerGroup = null;
      popPeer();
   }

   @Override
   public void exitNexus_vrf_rb_stanza(Nexus_vrf_rb_stanzaContext ctx) {
      _currentVrf = CiscoConfiguration.MASTER_VRF_NAME;
   }

   @Override
   public void exitNo_neighbor_activate_rb_stanza(
         No_neighbor_activate_rb_stanzaContext ctx) {
      BgpProcess proc = _configuration.getBgpProcesses().get(_currentVrf);
      if (ctx.ip != null) {
         Ip ip = toIp(ctx.ip);
         IpBgpPeerGroup pg = proc.getIpPeerGroups().get(ip);
         if (pg == null) {
            String message = "reference to undefined ip peer group: "
                  + ip.toString();
            _w.redFlag(message);
         }
         else {
            pg.setActive(false);
         }
      }
      else if (ctx.ip6 != null) {
         todo(ctx, F_IPV6);
      }
      else if (ctx.peergroup != null) {
         String pgName = ctx.peergroup.getText();
         NamedBgpPeerGroup npg = proc.getNamedPeerGroups().get(pgName);
         npg.setActive(false);
         for (IpBgpPeerGroup ipg : proc.getIpPeerGroups().values()) {
            String currentGroupName = ipg.getGroupName();
            if (currentGroupName != null && currentGroupName.equals(pgName)) {
               ipg.setActive(false);
            }
         }
      }
   }

   @Override
   public void exitNo_neighbor_shutdown_rb_stanza(
         No_neighbor_shutdown_rb_stanzaContext ctx) {
      BgpProcess proc = _configuration.getBgpProcesses().get(_currentVrf);
      if (ctx.ip != null) {
         Ip ip = toIp(ctx.ip);
         IpBgpPeerGroup pg = proc.getIpPeerGroups().get(ip);
         // TODO: see if it is always ok to set active on 'no shutdown'
         if (pg == null) {
            String message = "reference to undefined ip peer group: "
                  + ip.toString();
            _w.redFlag(message);
         }
         else {
            pg.setActive(true);
            pg.setShutdown(false);
         }
      }
      else if (ctx.ip6 != null) {
         todo(ctx, F_IPV6);
      }
      else if (ctx.peergroup != null) {
         _w.redFlag("'no shutdown' of  peer group unsupported");
      }
   }

   @Override
   public void exitNo_redistribute_connected_rb_stanza(
         No_redistribute_connected_rb_stanzaContext ctx) {
      BgpProcess proc = _configuration.getBgpProcesses().get(_currentVrf);
      if (_currentPeerGroup == proc.getMasterBgpPeerGroup()) {
         RoutingProtocol sourceProtocol = RoutingProtocol.CONNECTED;
         proc.getRedistributionPolicies().remove(sourceProtocol);
      }
      else if (_currentIpPeerGroup != null || _currentNamedPeerGroup != null) {
         throw new BatfishException(
               "do not currently handle per-neighbor redistribution policies");
      }
   }

   @Override
   public void exitNo_shutdown_rb_stanza(No_shutdown_rb_stanzaContext ctx) {
      // TODO: see if it is always ok to set active on 'no shutdown'
      _currentPeerGroup.setShutdown(false);
      _currentPeerGroup.setActive(true);
   }

   @Override
   public void exitNtp_access_group(Ntp_access_groupContext ctx) {
      String name = ctx.name.getText();
      _configuration.getNtpAccessGroups().add(name);
   }

   @Override
   public void exitNull_as_path_regex(Null_as_path_regexContext ctx) {
      _w.redFlag("as-path regexes this complicated are not supported yet");
   }

   @Override
   public void exitPassive_iis_stanza(Passive_iis_stanzaContext ctx) {
      _currentIsisInterface.setIsisInterfaceMode(IsisInterfaceMode.PASSIVE);
   }

   @Override
   public void exitPassive_interface_default_ro_stanza(
         Passive_interface_default_ro_stanzaContext ctx) {
      _currentOspfProcess.setPassiveInterfaceDefault(true);
   }

   @Override
   public void exitPassive_interface_is_stanza(
         Passive_interface_is_stanzaContext ctx) {
      String ifaceName = ctx.name.getText();
      _configuration.getInterfaces().get(ifaceName)
            .setIsisInterfaceMode(IsisInterfaceMode.PASSIVE);
   }

   @Override
   public void exitPassive_interface_ro_stanza(
         Passive_interface_ro_stanzaContext ctx) {
      boolean passive = ctx.NO() == null;
      String iname = ctx.i.getText();
      OspfProcess proc = _currentOspfProcess;
      if (passive) {
         proc.getInterfaceBlacklist().add(iname);
      }
      else {
         proc.getInterfaceWhitelist().add(iname);
      }
   }

   @Override
   public void exitPeer_group_assignment_rb_stanza(
         Peer_group_assignment_rb_stanzaContext ctx) {
      if (ctx.address != null) {
         Ip address = toIp(ctx.address);
         String peerGroupName = ctx.name.getText();
         _configuration.getBgpProcesses().get(_currentVrf)
               .addPeerGroupMember(address, peerGroupName);
      }
      else if (ctx.address6 != null) {
         todo(ctx, F_IPV6);
      }
   }

   @Override
   public void exitPeer_group_creation_rb_stanza(
         Peer_group_creation_rb_stanzaContext ctx) {
      String name = ctx.name.getText();
      BgpProcess proc = _configuration.getBgpProcesses().get(_currentVrf);
      if (proc.getNamedPeerGroups().get(name) == null) {
         proc.addNamedPeerGroup(name);
         if (ctx.PASSIVE() != null) {
            // dell: won't otherwise specify activation so just activate here
            NamedBgpPeerGroup npg = proc.getNamedPeerGroups().get(name);
            npg.setActive(true);
         }
      }
   }

   @Override
   public void exitPeer_sa_filter(Peer_sa_filterContext ctx) {
      String name = ctx.name.getText();
      _configuration.getMsdpPeerSaLists().add(name);
   }

   @Override
   public void exitPim_accept_register(Pim_accept_registerContext ctx) {
      String name = ctx.name.getText();
      if (ctx.LIST() != null) {
         _configuration.getPimAcls().add(name);
      }
      else if (ctx.ROUTE_MAP() != null) {
         _configuration.getPimRouteMaps().add(name);
      }
   }

   @Override
   public void exitPim_accept_rp(Pim_accept_rpContext ctx) {
      String name = ctx.name.getText();
      _configuration.getPimAcls().add(name);
   }

   @Override
   public void exitPim_rp_address(Pim_rp_addressContext ctx) {
      if (ctx.name != null) {
         String name = ctx.name.getText();
         _configuration.getPimAcls().add(name);
      }
   }

   @Override
   public void exitPim_rp_announce_filter(Pim_rp_announce_filterContext ctx) {
      String name = ctx.name.getText();
      _configuration.getPimAcls().add(name);
   }

   @Override
   public void exitPim_rp_candidate(Pim_rp_candidateContext ctx) {
      if (ctx.name != null) {
         String name = ctx.name.getText();
         _configuration.getPimAcls().add(name);
      }
   }

   @Override
   public void exitPim_send_rp_announce(Pim_send_rp_announceContext ctx) {
      if (ctx.name != null) {
         String name = ctx.name.getText();
         _configuration.getPimAcls().add(name);
      }
   }

   @Override
   public void exitPim_spt_threshold(Pim_spt_thresholdContext ctx) {
      if (ctx.name != null) {
         String name = ctx.name.getText();
         _configuration.getPimAcls().add(name);
      }
   }

   @Override
   public void exitPim_ssm(Pim_ssmContext ctx) {
      if (ctx.name != null) {
         String name = ctx.name.getText();
         _configuration.getPimAcls().add(name);
      }
   }

   @Override
   public void exitPrefix_list_bgp_tail(Prefix_list_bgp_tailContext ctx) {
      if (_currentIpv6PeerGroup != null) {
         todo(ctx, F_IPV6);
      }
      else {
         String listName = ctx.list_name.getText();
         if (ctx.IN() != null) {
            _currentPeerGroup.setInboundPrefixList(listName);
         }
         else if (ctx.OUT() != null) {
            _currentPeerGroup.setOutboundPrefixList(listName);
         }
         else {
            throw new BatfishException("bad direction");
         }
      }
   }

   @Override
   public void exitRedistribute_aggregate_bgp_tail(
         Redistribute_aggregate_bgp_tailContext ctx) {
      todo(ctx, F_BGP_REDISTRIBUTE_AGGREGATE);
   }

   @Override
   public void exitRedistribute_bgp_ro_stanza(
         Redistribute_bgp_ro_stanzaContext ctx) {
      OspfProcess proc = _currentOspfProcess;
      RoutingProtocol sourceProtocol = RoutingProtocol.BGP;
      OspfRedistributionPolicy r = new OspfRedistributionPolicy(sourceProtocol);
      proc.getRedistributionPolicies().put(sourceProtocol, r);
      int as = toInteger(ctx.as);
      r.getSpecialAttributes().put(OspfRedistributionPolicy.BGP_AS, as);
      if (ctx.metric != null) {
         int metric = toInteger(ctx.metric);
         r.setMetric(metric);
      }
      if (ctx.map != null) {
         String map = ctx.map.getText();
         r.setMap(map);
      }
      if (ctx.type != null) {
         int typeInt = toInteger(ctx.type);
         OspfMetricType type = OspfMetricType.fromInteger(typeInt);
         r.setOspfMetricType(type);
      }
      else {
         r.setOspfMetricType(OspfRedistributionPolicy.DEFAULT_METRIC_TYPE);
      }
      if (ctx.tag != null) {
         long tag = toLong(ctx.tag);
         r.setTag(tag);
      }
      r.setSubnets(ctx.subnets != null);
   }

   @Override
   public void exitRedistribute_connected_bgp_tail(
         Redistribute_connected_bgp_tailContext ctx) {
      BgpProcess proc = _configuration.getBgpProcesses().get(_currentVrf);
      if (_currentPeerGroup == proc.getMasterBgpPeerGroup()) {
         RoutingProtocol sourceProtocol = RoutingProtocol.CONNECTED;
         BgpRedistributionPolicy r = new BgpRedistributionPolicy(sourceProtocol);
         proc.getRedistributionPolicies().put(sourceProtocol, r);
         if (ctx.metric != null) {
            int metric = toInteger(ctx.metric);
            r.setMetric(metric);
         }
         if (ctx.map != null) {
            String map = ctx.map.getText();
            r.setMap(map);
         }
      }
      else if (_currentIpPeerGroup != null || _currentNamedPeerGroup != null) {
         throw new BatfishException(
               "do not currently handle per-neighbor redistribution policies");
      }
   }

   @Override
   public void exitRedistribute_connected_is_stanza(
         Redistribute_connected_is_stanzaContext ctx) {
      IsisProcess proc = _configuration.getIsisProcess();
      RoutingProtocol sourceProtocol = RoutingProtocol.CONNECTED;
      IsisRedistributionPolicy r = new IsisRedistributionPolicy(sourceProtocol);
      proc.getRedistributionPolicies().put(sourceProtocol, r);
      if (ctx.metric != null) {
         int metric = toInteger(ctx.metric);
         r.setMetric(metric);
      }
      if (ctx.map != null) {
         String map = ctx.map.getText();
         r.setMap(map);
      }
      if (ctx.LEVEL_1() != null) {
         r.setLevel(IsisLevel.LEVEL_1);
      }
      else if (ctx.LEVEL_2() != null) {
         r.setLevel(IsisLevel.LEVEL_2);
      }
      else if (ctx.LEVEL_1_2() != null) {
         r.setLevel(IsisLevel.LEVEL_1_2);
      }
      else {
         r.setLevel(IsisRedistributionPolicy.DEFAULT_LEVEL);
      }
   }

   @Override
   public void exitRedistribute_connected_ro_stanza(
         Redistribute_connected_ro_stanzaContext ctx) {
      OspfProcess proc = _currentOspfProcess;
      RoutingProtocol sourceProtocol = RoutingProtocol.CONNECTED;
      OspfRedistributionPolicy r = new OspfRedistributionPolicy(sourceProtocol);
      proc.getRedistributionPolicies().put(sourceProtocol, r);
      if (ctx.metric != null) {
         int metric = toInteger(ctx.metric);
         r.setMetric(metric);
      }
      if (ctx.map != null) {
         String map = ctx.map.getText();
         r.setMap(map);
      }
      if (ctx.type != null) {
         int typeInt = toInteger(ctx.type);
         OspfMetricType type = OspfMetricType.fromInteger(typeInt);
         r.setOspfMetricType(type);
      }
      else {
         r.setOspfMetricType(OspfRedistributionPolicy.DEFAULT_METRIC_TYPE);
      }
      if (ctx.tag != null) {
         long tag = toLong(ctx.tag);
         r.setTag(tag);
      }
      r.setSubnets(ctx.subnets != null);
   }

   @Override
   public void exitRedistribute_ospf_bgp_tail(
         Redistribute_ospf_bgp_tailContext ctx) {
      BgpProcess proc = _configuration.getBgpProcesses().get(_currentVrf);
      if (_currentPeerGroup == proc.getMasterBgpPeerGroup()) {
         RoutingProtocol sourceProtocol = RoutingProtocol.OSPF;
         BgpRedistributionPolicy r = new BgpRedistributionPolicy(sourceProtocol);
         proc.getRedistributionPolicies().put(sourceProtocol, r);
         if (ctx.metric != null) {
            int metric = toInteger(ctx.metric);
            r.setMetric(metric);
         }
         if (ctx.map != null) {
            String map = ctx.map.getText();
            r.setMap(map);
         }
         int procNum = toInteger(ctx.procnum);
         r.getSpecialAttributes().put(
               BgpRedistributionPolicy.OSPF_PROCESS_NUMBER, procNum);
      }
      else if (_currentIpPeerGroup != null || _currentNamedPeerGroup != null) {
         throw new BatfishException(
               "do not currently handle per-neighbor redistribution policies");
      }
   }

   @Override
   public void exitRedistribute_rip_ro_stanza(
         Redistribute_rip_ro_stanzaContext ctx) {
      todo(ctx, F_OSPF_REDISTRIBUTE_RIP);
   }

   @Override
   public void exitRedistribute_static_bgp_tail(
         Redistribute_static_bgp_tailContext ctx) {
      BgpProcess proc = _configuration.getBgpProcesses().get(_currentVrf);
      if (_currentPeerGroup == proc.getMasterBgpPeerGroup()) {
         RoutingProtocol sourceProtocol = RoutingProtocol.STATIC;
         BgpRedistributionPolicy r = new BgpRedistributionPolicy(sourceProtocol);
         proc.getRedistributionPolicies().put(sourceProtocol, r);
         if (ctx.metric != null) {
            int metric = toInteger(ctx.metric);
            r.setMetric(metric);
         }
         if (ctx.map != null) {
            String map = ctx.map.getText();
            r.setMap(map);
         }
      }
      else if (_currentIpPeerGroup != null || _currentNamedPeerGroup != null) {
         throw new BatfishException(
               "do not currently handle per-neighbor redistribution policies");
      }
   }

   @Override
   public void exitRedistribute_static_is_stanza(
         Redistribute_static_is_stanzaContext ctx) {
      IsisProcess proc = _configuration.getIsisProcess();
      RoutingProtocol sourceProtocol = RoutingProtocol.STATIC;
      IsisRedistributionPolicy r = new IsisRedistributionPolicy(sourceProtocol);
      proc.getRedistributionPolicies().put(sourceProtocol, r);
      if (ctx.metric != null) {
         int metric = toInteger(ctx.metric);
         r.setMetric(metric);
      }
      if (ctx.map != null) {
         String map = ctx.map.getText();
         r.setMap(map);
      }
      if (ctx.LEVEL_1() != null) {
         r.setLevel(IsisLevel.LEVEL_1);
      }
      else if (ctx.LEVEL_2() != null) {
         r.setLevel(IsisLevel.LEVEL_2);
      }
      else if (ctx.LEVEL_1_2() != null) {
         r.setLevel(IsisLevel.LEVEL_1_2);
      }
      else {
         r.setLevel(IsisRedistributionPolicy.DEFAULT_LEVEL);
      }
   }

   @Override
   public void exitRedistribute_static_ro_stanza(
         Redistribute_static_ro_stanzaContext ctx) {
      OspfProcess proc = _currentOspfProcess;
      RoutingProtocol sourceProtocol = RoutingProtocol.STATIC;
      OspfRedistributionPolicy r = new OspfRedistributionPolicy(sourceProtocol);
      proc.getRedistributionPolicies().put(sourceProtocol, r);
      if (ctx.metric != null) {
         int metric = toInteger(ctx.metric);
         r.setMetric(metric);
      }
      if (ctx.map != null) {
         String map = ctx.map.getText();
         r.setMap(map);
      }
      if (ctx.type != null) {
         int typeInt = toInteger(ctx.type);
         OspfMetricType type = OspfMetricType.fromInteger(typeInt);
         r.setOspfMetricType(type);
      }
      else {
         r.setOspfMetricType(OspfRedistributionPolicy.DEFAULT_METRIC_TYPE);
      }
      if (ctx.tag != null) {
         long tag = toLong(ctx.tag);
         r.setTag(tag);
      }
      r.setSubnets(ctx.subnets != null);
   }

   @Override
   public void exitRemote_as_bgp_tail(Remote_as_bgp_tailContext ctx) {
      BgpProcess proc = _configuration.getBgpProcesses().get(_currentVrf);
      int as = toInteger(ctx.as);
      if (_currentPeerGroup != proc.getMasterBgpPeerGroup()) {
         _currentPeerGroup.setRemoteAs(as);
      }
      else {
         throw new BatfishException("no peer or peer group in context");
      }
   }

   @Override
   public void exitRemove_private_as_bgp_tail(
         Remove_private_as_bgp_tailContext ctx) {
      _currentPeerGroup.setRemovePrivateAs(true);
   }

   @Override
   public void exitRoute_map_bgp_tail(Route_map_bgp_tailContext ctx) {
      if (_currentPeerGroup == null) {
         return;
      }
      String mapName = ctx.name.getText();
      if (ctx.IN() != null) {
         _currentPeerGroup.setInboundRouteMap(mapName);
      }
      else if (ctx.OUT() != null) {
         _currentPeerGroup.setOutboundRouteMap(mapName);
      }
      else {
         throw new BatfishException("bad direction");
      }
   }

   @Override
   public void exitRoute_map_stanza(Route_map_stanzaContext ctx) {
      _currentRouteMap = null;
      _currentRouteMapClause = null;
   }

   @Override
   public void exitRoute_policy_stanza(Route_policy_stanzaContext ctx) {
      _currentRoutePolicy = null;
   }

   @Override
   public void exitRoute_reflector_client_bgp_tail(
         Route_reflector_client_bgp_tailContext ctx) {
      _currentPeerGroup.setRouteReflectorClient(true);
   }

   @Override
   public void exitRouter_bgp_stanza(Router_bgp_stanzaContext ctx) {
      popPeer();
   }

   @Override
   public void exitRouter_id_bgp_tail(Router_id_bgp_tailContext ctx) {
      Ip routerId = toIp(ctx.routerid);
      _configuration.getBgpProcesses().get(_currentVrf).setRouterId(routerId);
   }

   @Override
   public void exitRouter_id_ro_stanza(Router_id_ro_stanzaContext ctx) {
      Ip routerId = toIp(ctx.ip);
      _currentOspfProcess.setRouterId(routerId);
   }

   @Override
   public void exitRouter_isis_stanza(Router_isis_stanzaContext ctx) {
      _currentIsisProcess = null;
   }

   @Override
   public void exitRouter_ospf_stanza(Router_ospf_stanzaContext ctx) {
      _currentOspfProcess.computeNetworks(_configuration.getInterfaces()
            .values());
      _currentOspfProcess = null;
   }

   @Override
   public void exitSend_community_bgp_tail(Send_community_bgp_tailContext ctx) {
      _currentPeerGroup.setSendCommunity(true);
   }

   @Override
   public void exitSet_as_path_prepend_rm_stanza(
         Set_as_path_prepend_rm_stanzaContext ctx) {
      List<Integer> asList = new ArrayList<Integer>();
      for (Token t : ctx.as_list) {
         int as = toInteger(t);
         asList.add(as);
      }
      RouteMapSetAsPathPrependLine line = new RouteMapSetAsPathPrependLine(
            asList);
      _currentRouteMapClause.addSetLine(line);
   }

   @Override
   public void exitSet_comm_list_delete_rm_stanza(
         Set_comm_list_delete_rm_stanzaContext ctx) {
      String name = ctx.name.getText();
      RouteMapSetDeleteCommunityLine line = new RouteMapSetDeleteCommunityLine(
            name);
      _currentRouteMapClause.addSetLine(line);
   }

   @Override
   public void exitSet_community_additive_rm_stanza(
         Set_community_additive_rm_stanzaContext ctx) {
      List<Long> commList = new ArrayList<Long>();
      for (CommunityContext c : ctx.communities) {
         long community = toLong(c);
         commList.add(community);
      }
      RouteMapSetAdditiveCommunityLine line = new RouteMapSetAdditiveCommunityLine(
            commList);
      _currentRouteMapClause.addSetLine(line);
   }

   @Override
   public void exitSet_community_list_additive_rm_stanza(
         Set_community_list_additive_rm_stanzaContext ctx) {
      Set<String> communityLists = new LinkedHashSet<String>();
      for (VariableContext comm_list : ctx.comm_lists) {
         String communityList = comm_list.getText();
         communityLists.add(communityList);
      }
      RouteMapSetAdditiveCommunityListLine line = new RouteMapSetAdditiveCommunityListLine(
            communityLists);
      _currentRouteMapClause.addSetLine(line);
   }

   @Override
   public void exitSet_community_list_rm_stanza(
         Set_community_list_rm_stanzaContext ctx) {
      Set<String> communityLists = new LinkedHashSet<String>();
      for (VariableContext comm_list : ctx.comm_lists) {
         String communityList = comm_list.getText();
         communityLists.add(communityList);
      }
      RouteMapSetCommunityListLine line = new RouteMapSetCommunityListLine(
            communityLists);
      _currentRouteMapClause.addSetLine(line);
   }

   @Override
   public void exitSet_community_none_rm_stanza(
         Set_community_none_rm_stanzaContext ctx) {
      RouteMapSetCommunityNoneLine line = new RouteMapSetCommunityNoneLine();
      _currentRouteMapClause.addSetLine(line);
   }

   @Override
   public void exitSet_community_rm_stanza(Set_community_rm_stanzaContext ctx) {
      List<Long> commList = new ArrayList<Long>();
      for (CommunityContext c : ctx.communities) {
         long community = toLong(c);
         commList.add(community);
      }
      RouteMapSetCommunityLine line = new RouteMapSetCommunityLine(commList);
      _currentRouteMapClause.addSetLine(line);
   }

   @Override
   public void exitSet_ipv6_rm_stanza(Set_ipv6_rm_stanzaContext ctx) {
      _currentRouteMap.setIpv6(true);
   }

   @Override
   public void exitSet_local_preference_rm_stanza(
         Set_local_preference_rm_stanzaContext ctx) {
      int localPreference = toInteger(ctx.pref);
      RouteMapSetLocalPreferenceLine line = new RouteMapSetLocalPreferenceLine(
            localPreference);
      _currentRouteMapClause.addSetLine(line);
   }

   @Override
   public void exitSet_metric_rm_stanza(Set_metric_rm_stanzaContext ctx) {
      int metric = toInteger(ctx.metric);
      RouteMapSetMetricLine line = new RouteMapSetMetricLine(metric);
      _currentRouteMapClause.addSetLine(line);
   }

   @Override
   public void exitSet_metric_type_rm_stanza(
         Set_metric_type_rm_stanzaContext ctx) {
      todo(ctx, F_ROUTE_MAP_SET_METRIC_TYPE);
   }

   @Override
   public void exitSet_next_hop_rm_stanza(Set_next_hop_rm_stanzaContext ctx) {
      Set<Ip> nextHops = new TreeSet<Ip>();
      for (Token t : ctx.nexthop_list) {
         Ip nextHop = toIp(t);
         nextHops.add(nextHop);
      }
      RouteMapSetNextHopLine line = new RouteMapSetNextHopLine(nextHops);
      _currentRouteMapClause.addSetLine(line);
   }

   @Override
   public void exitSet_origin_rm_stanza(Set_origin_rm_stanzaContext ctx) {
      OriginType originType;
      Integer asNum = null;
      if (ctx.IGP() != null) {
         originType = OriginType.IGP;
      }
      else if (ctx.INCOMPLETE() != null) {
         originType = OriginType.INCOMPLETE;
      }
      else if (ctx.as != null) {
         asNum = toInteger(ctx.as);
         originType = OriginType.EGP;
      }
      else {
         throw new BatfishException("bad origin type");
      }
      RouteMapSetLine line = new RouteMapSetOriginTypeLine(originType, asNum);
      _currentRouteMapClause.addSetLine(line);
   }

   @Override
   public void exitShutdown_bgp_tail(Shutdown_bgp_tailContext ctx) {
      if (_currentPeerGroup == null) {
         return;
      }
      _currentPeerGroup.setShutdown(true);
   }

   @Override
   public void exitShutdown_if_stanza(Shutdown_if_stanzaContext ctx) {
      if (ctx.NO() == null) {
         for (Interface currentInterface : _currentInterfaces) {
            currentInterface.setActive(false);
         }
      }
   }

   @Override
   public void exitStandard_access_list_stanza(
         Standard_access_list_stanzaContext ctx) {
      _currentStandardAcl = null;
   }

   @Override
   public void exitStandard_access_list_tail(
         Standard_access_list_tailContext ctx) {

      if (_currentStandardAcl.isIpV6()) {
         return;
      }

      LineAction action = getAccessListAction(ctx.ala);
      Ip srcIp = getIp(ctx.ipr);
      Ip srcWildcard = getWildcard(ctx.ipr);
      Set<Integer> dscps = new TreeSet<Integer>();
      Set<Integer> ecns = new TreeSet<Integer>();
      for (Standard_access_list_additional_featureContext feature : ctx.features) {
         if (feature.DSCP() != null) {
            int dscpType = toDscpType(feature.dscp_type());
            dscps.add(dscpType);
         }
         else if (feature.ECN() != null) {
            int ecn = toInteger(feature.ecn);
            ecns.add(ecn);
         }
      }
      String name;
      if (ctx.num != null) {
         name = ctx.num.getText();
      }
      else {
         name = getFullText(ctx).trim();
      }
      StandardAccessListLine line = new StandardAccessListLine(name, action,
            new IpWildcard(srcIp, srcWildcard), dscps, ecns);
      _currentStandardAcl.addLine(line);
   }

   @Override
   public void exitSubnet_bgp_tail(Subnet_bgp_tailContext ctx) {
      BgpProcess proc = _configuration.getBgpProcesses().get(_currentVrf);
      if (ctx.IP_PREFIX() != null) {
         Ip ip = getPrefixIp(ctx.IP_PREFIX().getSymbol());
         int prefixLength = getPrefixLength(ctx.IP_PREFIX().getSymbol());
         Prefix prefix = new Prefix(ip, prefixLength);
         NamedBgpPeerGroup namedGroup = _currentNamedPeerGroup;
         namedGroup.addNeighborPrefix(prefix);
         DynamicBgpPeerGroup pg = proc.addDynamicPeerGroup(prefix);
         pg.setGroupName(namedGroup.getName());
      }
      else if (ctx.IPV6_PREFIX() != null) {
         todo(ctx, F_IPV6);
      }
   }

   @Override
   public void exitSummary_address_is_stanza(
         Summary_address_is_stanzaContext ctx) {
      Ip ip = toIp(ctx.ip);
      Ip mask = toIp(ctx.mask);
      Prefix prefix = new Prefix(ip, mask);
      RoutingProtocol sourceProtocol = RoutingProtocol.ISIS_L1;
      IsisRedistributionPolicy r = new IsisRedistributionPolicy(sourceProtocol);
      r.setSummaryPrefix(prefix);
      _currentIsisProcess.getRedistributionPolicies().put(sourceProtocol, r);
      if (ctx.metric != null) {
         int metric = toInteger(ctx.metric);
         r.setMetric(metric);
      }
      if (!ctx.LEVEL_1().isEmpty()) {
         r.setLevel(IsisLevel.LEVEL_1);
      }
      else if (!ctx.LEVEL_2().isEmpty()) {
         r.setLevel(IsisLevel.LEVEL_2);
      }
      else if (!ctx.LEVEL_1_2().isEmpty()) {
         r.setLevel(IsisLevel.LEVEL_1_2);
      }
      else {
         r.setLevel(IsisRedistributionPolicy.DEFAULT_LEVEL);
      }
   }

   @Override
   public void exitSwitching_mode_stanza(Switching_mode_stanzaContext ctx) {
      todo(ctx, F_SWITCHING_MODE);
   }

   @Override
   public void exitSwitchport_access_if_stanza(
         Switchport_access_if_stanzaContext ctx) {
      if (ctx.vlan != null) {
         int vlan = toInteger(ctx.vlan);
         for (Interface currentInterface : _currentInterfaces) {
            currentInterface.setSwitchportMode(SwitchportMode.ACCESS);
            currentInterface.setAccessVlan(vlan);
         }
      }
      else {
         for (Interface currentInterface : _currentInterfaces) {
            currentInterface.setSwitchportMode(SwitchportMode.ACCESS);
            currentInterface.setSwitchportAccessDynamic(true);
         }
      }
   }

   @Override
   public void exitSwitchport_mode_access_stanza(
         Switchport_mode_access_stanzaContext ctx) {
      for (Interface currentInterface : _currentInterfaces) {
         currentInterface.setSwitchportMode(SwitchportMode.ACCESS);
      }
   }

   @Override
   public void exitSwitchport_mode_dynamic_auto_stanza(
         Switchport_mode_dynamic_auto_stanzaContext ctx) {
      for (Interface currentInterface : _currentInterfaces) {
         currentInterface.setSwitchportMode(SwitchportMode.DYNAMIC_AUTO);
      }
   }

   @Override
   public void exitSwitchport_mode_dynamic_desirable_stanza(
         Switchport_mode_dynamic_desirable_stanzaContext ctx) {
      for (Interface currentInterface : _currentInterfaces) {
         currentInterface.setSwitchportMode(SwitchportMode.DYNAMIC_DESIRABLE);
      }
   }

   @Override
   public void exitSwitchport_mode_trunk_stanza(
         Switchport_mode_trunk_stanzaContext ctx) {
      for (Interface currentInterface : _currentInterfaces) {
         currentInterface.setSwitchportMode(SwitchportMode.TRUNK);
      }
   }

   @Override
   public void exitSwitchport_trunk_allowed_if_stanza(
         Switchport_trunk_allowed_if_stanzaContext ctx) {
      List<SubRange> ranges = toRange(ctx.r);
      for (Interface currentInterface : _currentInterfaces) {
         currentInterface.addAllowedRanges(ranges);
      }
   }

   @Override
   public void exitSwitchport_trunk_encapsulation_if_stanza(
         Switchport_trunk_encapsulation_if_stanzaContext ctx) {
      SwitchportEncapsulationType type = toEncapsulation(ctx.e);
      for (Interface currentInterface : _currentInterfaces) {
         currentInterface.setSwitchportTrunkEncapsulation(type);
      }
   }

   @Override
   public void exitSwitchport_trunk_native_if_stanza(
         Switchport_trunk_native_if_stanzaContext ctx) {
      int vlan = toInteger(ctx.vlan);
      for (Interface currentInterface : _currentInterfaces) {
         currentInterface.setNativeVlan(vlan);
      }
   }

   @Override
   public void exitTemplate_peer_address_family(
         Template_peer_address_familyContext ctx) {
      popPeer();
   }

   @Override
   public void exitTemplate_peer_rb_stanza(Template_peer_rb_stanzaContext ctx) {
      _currentIpPeerGroup = null;
      _currentIpv6PeerGroup = null;
      _currentNamedPeerGroup = null;
      popPeer();
   }

   @Override
   public void exitTemplate_peer_session_rb_stanza(
         Template_peer_session_rb_stanzaContext ctx) {
      _currentIpPeerGroup = null;
      _currentIpv6PeerGroup = null;
      _currentNamedPeerGroup = null;
      _currentPeerSession = null;
      popPeer();
   }

   @Override
   public void exitUnrecognized_line(Unrecognized_lineContext ctx) {
      String line = _text.substring(ctx.start.getStartIndex(),
            ctx.stop.getStopIndex());
      String msg = String.format("Line %d unrecognized: %s",
            ctx.start.getLine(), line);
      if (_unrecognizedAsRedFlag) {
         _w.redFlag(msg);
      }
      else {
         _parser.getParserErrorListener().syntaxError(ctx, ctx.getStart(),
               ctx.getStart().getLine(),
               ctx.getStart().getCharPositionInLine(), msg);
         throw new BatfishException(msg);
      }
   }

   @Override
   public void exitUpdate_source_bgp_tail(Update_source_bgp_tailContext ctx) {
      if (_currentPeerGroup == null) {
         return;
      }
      else if (_currentIpv6PeerGroup != null) {
         todo(ctx, F_IPV6);
      }
      else {
         String source = toInterfaceName(ctx.source);
         _currentPeerGroup.setUpdateSource(source);
      }
   }

   @Override
   public void exitUse_neighbor_group_bgp_tail(
         Use_neighbor_group_bgp_tailContext ctx) {
      String groupName = ctx.name.getText();
      if (_currentIpPeerGroup != null) {
         _currentIpPeerGroup.setGroupName(groupName);
      }
      else if (_currentIpv6PeerGroup != null) {
         todo(ctx, F_IPV6);
      }
      else {
         throw new BatfishException("Unexpected context for use neighbor group");
      }
   }

   @Override
   public void exitVrf_context_stanza(Vrf_context_stanzaContext ctx) {
      _currentVrf = CiscoConfiguration.MASTER_VRF_NAME;
   }

   @Override
   public void exitVrf_forwarding_if_stanza(Vrf_forwarding_if_stanzaContext ctx) {
      String name = ctx.name.getText();
      for (Interface currentInterface : _currentInterfaces) {
         currentInterface.setVrf(name);
         currentInterface.setPrefix(null);
      }
   }

   @Override
   public void exitVrf_member_if_stanza(Vrf_member_if_stanzaContext ctx) {
      String name = ctx.name.getText();
      for (Interface currentInterface : _currentInterfaces) {
         currentInterface.setVrf(name);
      }
   }

   private String getAddressGroup(Access_list_ip_rangeContext ctx) {
      if (ctx.address_group != null) {
         return ctx.address_group.getText();
      }
      else {
         return null;
      }
   }

   public CiscoConfiguration getConfiguration() {
      return _configuration;
   }

   private String getFullText(ParserRuleContext ctx) {
      int start = ctx.getStart().getStartIndex();
      int end = ctx.getStop().getStopIndex();
      String text = _text.substring(start, end + 1);
      return text;
   }

   public String getText() {
      return _text;
   }

   @Override
   public Set<String> getUnimplementedFeatures() {
      return _unimplementedFeatures;
   }

   @Override
   public VendorConfiguration getVendorConfiguration() {
      return _vendorConfiguration;
   }

   private void popPeer() {
      int index = _peerGroupStack.size() - 1;
      _currentPeerGroup = _peerGroupStack.get(index);
      _peerGroupStack.remove(index);
   }

   @Override
   public void processParseTree(ParserRuleContext tree) {
      ParseTreeWalker walker = new ParseTreeWalker();
      walker.walk(this, tree);
   }

   private void pushPeer(BgpPeerGroup pg) {
      _peerGroupStack.add(_currentPeerGroup);
      _currentPeerGroup = pg;
   }

   private void todo(ParserRuleContext ctx, String feature) {
      _w.todo(ctx, feature, _parser, _text);
      _unimplementedFeatures.add("Cisco: " + feature);
   }

   public SwitchportEncapsulationType toEncapsulation(
         Switchport_trunk_encapsulationContext ctx) {
      if (ctx.DOT1Q() != null) {
         return SwitchportEncapsulationType.DOT1Q;
      }
      else if (ctx.ISL() != null) {
         return SwitchportEncapsulationType.ISL;
      }
      else if (ctx.NEGOTIATE() != null) {
         return SwitchportEncapsulationType.NEGOTIATE;
      }
      else {
         throw new BatfishException("bad encapsulation");
      }
   }

   public RoutePolicyBoolean toRoutePolicyBoolean(
         Boolean_and_rp_stanzaContext ctxt) {
      if (ctxt.AND() == null) {
         return toRoutePolicyBoolean(ctxt.boolean_not_rp_stanza());
      }
      else {
         return new RoutePolicyBooleanAnd(
               toRoutePolicyBoolean(ctxt.boolean_and_rp_stanza()),
               toRoutePolicyBoolean(ctxt.boolean_not_rp_stanza()));
      }
   }

   public RoutePolicyBoolean toRoutePolicyBoolean(
         Boolean_not_rp_stanzaContext ctxt) {
      if (ctxt.NOT() == null) {
         return toRoutePolicyBoolean(ctxt.boolean_simple_rp_stanza());
      }
      else {
         return new RoutePolicyBooleanNot(
               toRoutePolicyBoolean(ctxt.boolean_simple_rp_stanza()));
      }
   }

   public RoutePolicyBoolean toRoutePolicyBoolean(Boolean_rp_stanzaContext ctxt) {
      if (ctxt.OR() == null) {
         return toRoutePolicyBoolean(ctxt.boolean_and_rp_stanza());
      }
      else {
         return new RoutePolicyBooleanOr(
               toRoutePolicyBoolean(ctxt.boolean_rp_stanza()),
               toRoutePolicyBoolean(ctxt.boolean_and_rp_stanza()));
      }
   }

   public RoutePolicyBoolean toRoutePolicyBoolean(
         Boolean_simple_rp_stanzaContext ctxt) {
      Boolean_rp_stanzaContext bctxt = ctxt.boolean_rp_stanza();
      if (bctxt != null) {
         return toRoutePolicyBoolean(bctxt);
      }

      Boolean_community_matches_any_rp_stanzaContext mactxt = ctxt
            .boolean_community_matches_any_rp_stanza();
      if (mactxt != null) {
         return new RoutePolicyBooleanCommunityMatchesAny(
               toRoutePolicyCommunitySet(mactxt.rp_community_set()));
      }

      Boolean_community_matches_every_rp_stanzaContext mectxt = ctxt
            .boolean_community_matches_every_rp_stanza();
      if (mectxt != null) {
         return new RoutePolicyBooleanCommunityMatchesEvery(
               toRoutePolicyCommunitySet(mectxt.rp_community_set()));
      }

      Boolean_destination_rp_stanzaContext dctxt = ctxt
            .boolean_destination_rp_stanza();
      if (dctxt != null) {
         return new RoutePolicyBooleanDestination(
               toRoutePolicyPrefixSet(dctxt.rp_prefix_set()));
      }

      Boolean_rib_has_route_rp_stanzaContext rctxt = ctxt
            .boolean_rib_has_route_rp_stanza();
      if (rctxt != null) {
         return new RoutePolicyBooleanRIBHasRoute(
               toRoutePolicyPrefixSet(rctxt.rp_prefix_set()));
      }

      return null;

   }

   public RoutePolicyCommunitySet toRoutePolicyCommunitySet(
         Rp_community_setContext ctxt) {
      if (ctxt.name != null) {
         return new RoutePolicyCommunitySetName(ctxt.name.getText());
      }
      else {
         return new RoutePolicyCommunitySetNumber(ctxt.COMMUNITY_NUMBER()
               .getText());
      }
   }

   public RoutePolicyElseBlock toRoutePolicyElseBlock(Else_rp_stanzaContext ctxt) {
      List<RoutePolicyStatement> stmts = toRoutePolicyStatementList(ctxt
            .rp_stanza());
      return new RoutePolicyElseBlock(stmts);

   }

   public RoutePolicyElseIfBlock toRoutePolicyElseIfBlock(
         Elseif_rp_stanzaContext ctxt) {
      RoutePolicyBoolean b = toRoutePolicyBoolean(ctxt.boolean_rp_stanza());
      List<RoutePolicyStatement> stmts = toRoutePolicyStatementList(ctxt
            .rp_stanza());
      return new RoutePolicyElseIfBlock(b, stmts);

   }

   public RoutePolicyPrefixSet toRoutePolicyPrefixSet(Rp_prefix_setContext ctxt) {
      if (ctxt.name != null) {
         return new RoutePolicyPrefixSetName(ctxt.name.getText());
      }
      else {
         Prefix_set_elemContext pctxt = ctxt.prefix_set_elem();

         Integer lower = null;
         Integer upper = null;
         if (pctxt.minpl != null) {
            lower = new Integer(toInteger(pctxt.minpl));
         }
         if (pctxt.maxpl != null) {
            upper = new Integer(toInteger(pctxt.maxpl));
         }
         if (pctxt.eqpl != null) {
            lower = new Integer(toInteger(pctxt.eqpl));
            upper = new Integer(lower);
         }

         if (pctxt.ipa != null) {
            return new RoutePolicyPrefixSetIp(toIp(pctxt.ipa), lower, upper);
         }
         if (pctxt.prefix != null) {
            return new RoutePolicyPrefixSetNumber(new Prefix(
                  pctxt.prefix.getText()), lower, upper);
         }
         if (pctxt.ipv6a != null) {
            return new RoutePolicyPrefixSetIpV6(toIp6(pctxt.ipv6a), lower,
                  upper);
         }
         if (pctxt.ipv6_prefix != null) {
            return new RoutePolicyPrefixSetNumberV6(new Prefix6(
                  pctxt.ipv6_prefix.getText()), lower, upper);
         }

         return null;
      }
   }

   public RoutePolicyStatement toRoutePolicyStatement(
         Apply_rp_stanzaContext ctxt) {
      return new RoutePolicyApplyStatement(ctxt.name.getText());
   }

   public RoutePolicyStatement toRoutePolicyStatement(
         Delete_rp_stanzaContext ctxt) {
      if (ctxt.ALL() != null) {
         return new RoutePolicyDeleteAllStatement();
      }
      else {
         boolean negated = (ctxt.NOT() != null);
         return new RoutePolicyDeleteCommunityStatement(negated,
               toRoutePolicyCommunitySet(ctxt.rp_community_set()));
      }
   }

   public RoutePolicyStatement toRoutePolicyStatement(
         Disposition_rp_stanzaContext ctxt) {
      RoutePolicyDispositionType t = null;
      if (ctxt.DONE() != null) {
         t = RoutePolicyDispositionType.DONE;
      }
      else if (ctxt.DROP() != null) {
         t = RoutePolicyDispositionType.DROP;
      }
      else if (ctxt.PASS() != null) {
         t = RoutePolicyDispositionType.PASS;
      }
      return new RoutePolicyDispositionStatement(t);
   }

   public RoutePolicyStatement toRoutePolicyStatement(If_rp_stanzaContext ctxt) {
      RoutePolicyBoolean b = toRoutePolicyBoolean(ctxt.boolean_rp_stanza());
      List<RoutePolicyStatement> stmts = toRoutePolicyStatementList(ctxt
            .rp_stanza());
      List<RoutePolicyElseIfBlock> elseIfs = new ArrayList<RoutePolicyElseIfBlock>();
      for (Elseif_rp_stanzaContext ectxt : ctxt.elseif_rp_stanza()) {
         elseIfs.add(toRoutePolicyElseIfBlock(ectxt));
      }
      RoutePolicyElseBlock els = null;
      Else_rp_stanzaContext elctxt = ctxt.else_rp_stanza();
      if (elctxt != null) {
         els = toRoutePolicyElseBlock(elctxt);
      }

      return new RoutePolicyIfStatement(b, stmts, elseIfs, els);

   }

   public RoutePolicyStatement toRoutePolicyStatement(Rp_stanzaContext ctxt) {
      Apply_rp_stanzaContext actxt = ctxt.apply_rp_stanza();
      if (actxt != null) {
         return toRoutePolicyStatement(actxt);
      }

      Delete_rp_stanzaContext dctxt = ctxt.delete_rp_stanza();
      if (dctxt != null) {
         return toRoutePolicyStatement(dctxt);
      }

      Disposition_rp_stanzaContext pctxt = ctxt.disposition_rp_stanza();
      if (pctxt != null) {
         return toRoutePolicyStatement(pctxt);
      }

      If_rp_stanzaContext ictxt = ctxt.if_rp_stanza();
      if (ictxt != null) {
         return toRoutePolicyStatement(ictxt);
      }

      Set_rp_stanzaContext sctxt = ctxt.set_rp_stanza();
      if (sctxt != null) {
         return toRoutePolicyStatement(sctxt);
      }

      return null;
   }

   public RoutePolicyStatement toRoutePolicyStatement(
         Set_community_rp_stanzaContext ctxt) {
      RoutePolicyCommunitySet cset = toRoutePolicyCommunitySet(ctxt
            .rp_community_set());
      boolean additive = (ctxt.ADDITIVE() != null);
      return new RoutePolicySetCommunity(cset, additive);
   }

   public RoutePolicyStatement toRoutePolicyStatement(
         Set_local_preference_rp_stanzaContext ctxt) {
      return new RoutePolicySetLocalPref(toInteger(ctxt.pref));
   }

   public RoutePolicyStatement toRoutePolicyStatement(
         Set_med_rp_stanzaContext ctxt) {
      return new RoutePolicySetMED(toInteger(ctxt.med));
   }

   public RoutePolicyStatement toRoutePolicyStatement(
         Set_next_hop_rp_stanzaContext ctxt) {
      RoutePolicyNextHop hop = null;
      if (ctxt.IP_ADDRESS() != null) {
         hop = new RoutePolicyNextHopIP(toIp(ctxt.IP_ADDRESS()));
      }
      else if (ctxt.IPV6_ADDRESS() != null) {
         hop = new RoutePolicyNextHopIP6(toIp6(ctxt.IPV6_ADDRESS()));
      }
      else if (ctxt.PEER_ADDRESS() != null) {
         hop = new RoutePolicyNextHopPeerAddress();
      }
      else if (ctxt.SELF() != null) {
         hop = new RoutePolicyNextHopSelf();
      }

      boolean dest_vrf = (ctxt.DESTINATION_VRF() != null);
      return new RoutePolicySetNextHop(hop, dest_vrf);

   }

   public RoutePolicyStatement toRoutePolicyStatement(Set_rp_stanzaContext ctxt) {
      Set_community_rp_stanzaContext cctxt = ctxt.set_community_rp_stanza();
      if (cctxt != null) {
         return toRoutePolicyStatement(cctxt);
      }

      Set_local_preference_rp_stanzaContext lpctxt = ctxt
            .set_local_preference_rp_stanza();
      if (lpctxt != null) {
         return toRoutePolicyStatement(lpctxt);
      }

      Set_med_rp_stanzaContext mctxt = ctxt.set_med_rp_stanza();
      if (mctxt != null) {
         return toRoutePolicyStatement(mctxt);
      }

      Set_next_hop_rp_stanzaContext hctxt = ctxt.set_next_hop_rp_stanza();
      if (hctxt != null) {
         return toRoutePolicyStatement(hctxt);
      }

      return null;
   }

   public List<RoutePolicyStatement> toRoutePolicyStatementList(
         List<Rp_stanzaContext> ctxts) {
      List<RoutePolicyStatement> stmts = new ArrayList<RoutePolicyStatement>();
      for (Rp_stanzaContext ctxt : ctxts) {
         RoutePolicyStatement stmt = toRoutePolicyStatement(ctxt);
         if (stmt != null) {
            stmts.add(stmt);
         }
      }
      return stmts;
   }

}
