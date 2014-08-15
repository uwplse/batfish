package batfish.representation.cisco;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import batfish.grammar.cisco.CiscoGrammar.Cisco_configurationContext;
import batfish.grammar.cisco.CiscoGrammar.Router_bgp_stanzaContext;
import batfish.grammar.cisco.CiscoGrammar.Router_ospf_stanzaContext;
import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class CiscoConfiguration implements Serializable, RepresentationObject {

   private static final long serialVersionUID = 1L;

   protected final Map<String, IpAsPathAccessList> _asPathAccessLists;
   protected BgpProcess _bgpProcess;
   protected transient Router_bgp_stanzaContext _bgpProcessContext;
   protected transient Cisco_configurationContext _context;
   protected final Map<String, ExpandedCommunityList> _expandedCommunityLists;
   protected final Map<String, ExtendedAccessList> _extendedAccessLists;
   protected String _hostname;
   protected final Map<String, Interface> _interfaces;
   protected OspfProcess _ospfProcess;
   protected transient Router_ospf_stanzaContext _ospfProcessContext;
   protected final Map<String, PrefixList> _prefixLists;
   protected final Map<String, RouteMap> _routeMaps;
   protected final Map<String, StandardAccessList> _standardAccessLists;
   protected final Map<String, StandardCommunityList> _standardCommunityLists;
   protected final Map<String, StaticRoute> _staticRoutes;

   public CiscoConfiguration() {
      _asPathAccessLists = new HashMap<String, IpAsPathAccessList>();
      _expandedCommunityLists = new HashMap<String, ExpandedCommunityList>();
      _extendedAccessLists = new HashMap<String, ExtendedAccessList>();
      _interfaces = new HashMap<String, Interface>();
      _prefixLists = new HashMap<String, PrefixList>();
      _routeMaps = new HashMap<String, RouteMap>();
      _standardAccessLists = new HashMap<String, StandardAccessList>();
      _standardCommunityLists = new HashMap<String, StandardCommunityList>();
      _staticRoutes = new HashMap<String, StaticRoute>();
   }

   public Map<String, IpAsPathAccessList> getAsPathAccessLists() {
      return _asPathAccessLists;
   }

   public final BgpProcess getBgpProcess() {
      return _bgpProcess;
   }

   public final Router_bgp_stanzaContext getBgpProcessContext() {
      return _bgpProcessContext;
   }

   public final Cisco_configurationContext getContext() {
      return _context;
   }

   public final Map<String, ExpandedCommunityList> getExpandedCommunityLists() {
      return _expandedCommunityLists;
   }

   public final Map<String, ExtendedAccessList> getExtendedAcls() {
      return _extendedAccessLists;
   }

   public final String getHostname() {
      return _hostname;
   }

   public final Map<String, Interface> getInterfaces() {
      return _interfaces;
   }

   public final OspfProcess getOspfProcess() {
      return _ospfProcess;
   }

   public final Router_ospf_stanzaContext getOspfProcessContext() {
      return _ospfProcessContext;
   }

   public final Map<String, PrefixList> getPrefixLists() {
      return _prefixLists;
   }

   public final Map<String, RouteMap> getRouteMaps() {
      return _routeMaps;
   }

   public final Map<String, StandardAccessList> getStandardAcls() {
      return _standardAccessLists;
   }

   public final Map<String, StandardCommunityList> getStandardCommunityLists() {
      return _standardCommunityLists;
   }

   public final Map<String, StaticRoute> getStaticRoutes() {
      return _staticRoutes;
   }

   public final void setBgpProcess(BgpProcess bgpProcess,
         Router_bgp_stanzaContext context) {
      _bgpProcess = bgpProcess;
      _bgpProcessContext = context;
   }

   public final void setContext(Cisco_configurationContext ctx) {
      _context = ctx;
   }

   public final void setHostname(String hostname) {
      _hostname = hostname;
   }

   public final void setOspfProcess(OspfProcess proc,
         Router_ospf_stanzaContext ctx) {
      _ospfProcess = proc;
      _ospfProcessContext = ctx;
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
         Util.diffRepresentationMaps(null, _asPathAccessLists, string
               + "._asPathAccessLists");
         if (_bgpProcess != null) {
            _bgpProcess.diffRepresentation(null, string + "._bgpProcess", true);
         }
         Util.diffRepresentationMaps(null, _expandedCommunityLists, string
               + "._expandedCommunityLists");
         Util.diffRepresentationMaps(null, _extendedAccessLists, string
               + "._extendedAccessLists");
         Util.diffRepresentationMaps(null, _interfaces, string + "._interfaces");
         if (_ospfProcess != null) {
            _ospfProcess.diffRepresentation(null, string + "._ospfProcess",
                  true);
         }
         Util.diffRepresentationMaps(null, _prefixLists, string
               + "._prefixLists");
         Util.diffRepresentationMaps(null, _routeMaps, string + "._routeMaps");
         Util.diffRepresentationMaps(null, _standardAccessLists, string
               + "._standardAccessLists");
         Util.diffRepresentationMaps(null, _standardCommunityLists, string
               + "._standardCommunityLists");
         Util.diffRepresentationMaps(null, _staticRoutes, string
               + "._staticRoutes");
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string + "\n");
         Util.diffRepresentationMaps(_asPathAccessLists, null, string
               + "._asPathAccessLists");
         if (_bgpProcess != null) {
            _bgpProcess
                  .diffRepresentation(null, string + "._bgpProcess", false);
         }
         Util.diffRepresentationMaps(_expandedCommunityLists, null, string
               + "._expandedCommunityLists");
         Util.diffRepresentationMaps(_extendedAccessLists, null, string
               + "._extendedAccessLists");
         Util.diffRepresentationMaps(_interfaces, null, string + "._interfaces");
         if (_ospfProcess != null) {
            _ospfProcess.diffRepresentation(null, string + "._ospfProcess",
                  false);
         }
         Util.diffRepresentationMaps(_prefixLists, null, string
               + "._prefixLists");
         Util.diffRepresentationMaps(_routeMaps, null, string + "._routeMaps");
         Util.diffRepresentationMaps(_standardAccessLists, null, string
               + "._standardAccessLists");
         Util.diffRepresentationMaps(_standardCommunityLists, null, string
               + "._standardCommunityLists");
         Util.diffRepresentationMaps(_staticRoutes, null, string
               + "._staticRoutes");
         System.out.flush();
         return;
      }

      CiscoConfiguration rhs = (CiscoConfiguration) o;
      Util.diffRepresentationMaps(_asPathAccessLists, rhs._asPathAccessLists,
            string + "._asPathAccessLists");
      if (_bgpProcess != null) {
         _bgpProcess.diffRepresentation(rhs._bgpProcess, string
               + "._bgpProcess", false);
      }
      else if (rhs._bgpProcess != null) {
         rhs._bgpProcess
               .diffRepresentation(null, string + "._bgpProcess", true);
      }
      Util.diffRepresentationMaps(_expandedCommunityLists,
            rhs._expandedCommunityLists, string + "._expandedCommunityLists");
      Util.diffRepresentationMaps(_extendedAccessLists,
            rhs._extendedAccessLists, string + "._extendedAccessLists");
      Util.diffRepresentationMaps(_interfaces, rhs._interfaces, string
            + "._interfaces");
      if (_ospfProcess != null) {
         _ospfProcess.diffRepresentation(rhs._ospfProcess, string
               + "._ospfProcess", false);
      }
      else if(rhs._ospfProcess!= null){
         rhs._ospfProcess.diffRepresentation(null, string
               + "._ospfProcess", true);
      }
      Util.diffRepresentationMaps(_prefixLists, rhs._prefixLists, string
            + "._prefixLists");
      Util.diffRepresentationMaps(_routeMaps, rhs._routeMaps, string
            + "._routeMaps");
      Util.diffRepresentationMaps(_standardAccessLists,
            rhs._standardAccessLists, string + "._standardAccessLists");
      Util.diffRepresentationMaps(_standardCommunityLists,
            rhs._standardCommunityLists, string + "._standardCommunityLists");
      Util.diffRepresentationMaps(_staticRoutes, rhs._staticRoutes, string
            + "._staticRoutes");

      System.out.flush();
      return;
   }

}
