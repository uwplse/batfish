package batfish.representation.cisco;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import batfish.grammar.cisco.CiscoGrammar.Interface_stanzaContext;
import batfish.grammar.cisco.CiscoGrammar.Ip_address_if_stanzaContext;
import batfish.representation.Ip;
import batfish.representation.RepresentationObject;
import batfish.representation.SwitchportEncapsulationType;
import batfish.representation.SwitchportMode;
import batfish.util.SubRange;
import batfish.util.Util;

public class Interface implements Serializable, RepresentationObject {

   private static final double FAST_ETHERNET_BANDWIDTH = 100E6;

   private static final double GIGABIT_ETHERNET_BANDWIDTH = 1E9;

   /**
    * dirty hack: just chose a very large number
    */
   private static final double LOOPBACK_BANDWIDTH = 1E12;
   private static final long serialVersionUID = 1L;
   private static final double TEN_GIGABIT_ETHERNET_BANDWIDTH = 10E9;

   public static double getDefaultBandwidth(String name) {
      Double bandwidth = null;
      if (name.startsWith("FastEthernet")) {
         bandwidth = FAST_ETHERNET_BANDWIDTH;
      }
      else if (name.startsWith("GigabitEthernet")) {
         bandwidth = GIGABIT_ETHERNET_BANDWIDTH;
      }
      else if (name.startsWith("TenGigabitEthernet")) {
         bandwidth = TEN_GIGABIT_ETHERNET_BANDWIDTH;
      }
      else if (name.startsWith("Vlan")) {
         bandwidth = null;
      }
      else if (name.startsWith("Loopback")) {
         bandwidth = LOOPBACK_BANDWIDTH;
      }
      if (bandwidth == null) {
         bandwidth = 1.0;
      }
      return bandwidth;
   }

   private int _accessVlan;
   private boolean _active;
   private ArrayList<SubRange> _allowedVlans;
   private Integer _area;
   private Double _bandwidth;
   private transient Interface_stanzaContext _context;
   private String _description;
   private String _incomingFilter;
   private Ip _ip;
   private transient Ip_address_if_stanzaContext _ipAddressStanzaContext;
   private String _name;
   private int _nativeVlan;
   private Integer _ospfCost;
   private int _ospfDeadInterval;
   private int _ospfHelloMultiplier;
   private String _outgoingFilter;
   private String _routingPolicy;
   private Map<String, String> _secondaryIps;

   private Ip _subnet;

   private SwitchportMode _switchportMode;

   private SwitchportEncapsulationType _switchportTrunkEncapsulation;

   public Interface(String name) {
      _name = name;
      _area = null;
      _ip = null;
      _active = true;
      _nativeVlan = 1;
      _switchportMode = SwitchportMode.NONE;
      _allowedVlans = new ArrayList<SubRange>();
      _secondaryIps = new HashMap<String, String>();
      _ospfCost = null;
   }

   public void addAllowedRanges(List<SubRange> ranges) {
      _allowedVlans.addAll(ranges);
   }

   public int getAccessVlan() {
      return _accessVlan;
   }

   public boolean getActive() {
      return _active;
   }

   public List<SubRange> getAllowedVlans() {
      return _allowedVlans;
   }

   public Integer getArea() {
      return _area;
   }

   public Double getBandwidth() {
      return _bandwidth;
   }

   public Interface_stanzaContext getContext() {
      return _context;
   }

   public String getDescription() {
      return _description;
   }

   public String getIncomingFilter() {
      return _incomingFilter;
   }

   public Ip getIP() {
      return _ip;
   }

   public Ip_address_if_stanzaContext getIpAddressStanzaContext() {
      return _ipAddressStanzaContext;
   }

   public String getName() {
      return _name;
   }

   public int getNativeVlan() {
      return _nativeVlan;
   }

   public Integer getOspfCost() {
      return _ospfCost;
   }

   public int getOspfDeadInterval() {
      return _ospfDeadInterval;
   }

   public int getOspfHelloMultiplier() {
      return _ospfHelloMultiplier;
   }

   public String getOutgoingFilter() {
      return _outgoingFilter;
   }

   public String getRoutingPolicy() {
      return _routingPolicy;
   }

   public Map<String, String> getSecondaryIps() {
      return _secondaryIps;
   }

   public Ip getSubnetMask() {
      return _subnet;
   }

   public SwitchportMode getSwitchportMode() {
      return _switchportMode;
   }

   public SwitchportEncapsulationType getSwitchportTrunkEncapsulation() {
      return _switchportTrunkEncapsulation;
   }

   public void setAccessVlan(int vlan) {
      _accessVlan = vlan;
   }

   public void setActive(boolean active) {
      _active = active;
   }

   public void setBandwidth(Double bandwidth) {
      _bandwidth = bandwidth;
   }

   public void setContext(Interface_stanzaContext context) {
      _context = context;
   }

   public void setDescription(String description) {
      _description = description;
   }

   public void setIncomingFilter(String accessListName) {
      _incomingFilter = accessListName;
   }

   public void setIp(Ip ip) {
      _ip = ip;
   }

   public void setIpAddressStanzaContext(Ip_address_if_stanzaContext ctx) {
      _ipAddressStanzaContext = ctx;
   }

   public void setNativeVlan(int vlan) {
      _nativeVlan = vlan;
   }

   public void setOspfCost(int ospfCost) {
      _ospfCost = ospfCost;
   }

   public void setOSPFDeadInterval(int seconds) {
      _ospfDeadInterval = seconds;
   }

   public void setOSPFHelloMultiplier(int multiplier) {
      _ospfHelloMultiplier = multiplier;
   }

   public void setOutgoingFilter(String accessListName) {
      _outgoingFilter = accessListName;
   }

   public void setRoutingPolicy(String routingPolicy) {
      _routingPolicy = routingPolicy;
   }

   public void setSubnetMask(Ip subnet) {
      _subnet = subnet;
   }

   public void setSwitchportMode(SwitchportMode switchportMode) {
      _switchportMode = switchportMode;
   }

   public void setSwitchportTrunkEncapsulation(
         SwitchportEncapsulationType encapsulation) {
      _switchportTrunkEncapsulation = encapsulation;
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
         System.out.println("+ " + string + "._accessVlan:" + _accessVlan + "\n");
         System.out.println("+ " + string + "._active:" + _active + "\n");
         Util.diffRepresentationLists(null, _allowedVlans, string + "._allowedVlans");
         System.out.println("+ " + string + "._area:" + Util.objectToString(_area) + "\n");
         System.out.println("+ " + string + "._bandwidth:" + Util.objectToString(_bandwidth) + "\n");
         System.out.println("+ " + string + "._incomingFilter:" + Util.objectToString(_incomingFilter) + "\n");
         System.out.println("+ " + string + "._ip:" + Util.objectToString(_ip) + "\n");
         System.out.println("+ " + string + "._name:" + Util.objectToString(_name) + "\n");
         System.out.println("+ " + string + "._nativeVlan:" + _nativeVlan + "\n");
         System.out.println("+ " + string + "._ospfCost:" + Util.objectToString(_ospfCost) + "\n");
         System.out.println("+ " + string + "._ospfDeadInterval:" + _ospfDeadInterval + "\n");
         System.out.println("+ " + string + "._ospfHelloMultiplier:" + _ospfHelloMultiplier + "\n");
         System.out.println("+ " + string + "._outgoingFilter:" + Util.objectToString(_outgoingFilter) + "\n");
         System.out.println("+ " + string + "._routingPolicy:" + Util.objectToString(_routingPolicy) + "\n");
         Util.diffRepresentationMaps(null, _secondaryIps, string + "._secondaryIps");
         System.out.println("+ " + string + "._subnet:" + Util.objectToString(_subnet) + "\n");
         System.out.println("+ " + string + "._switchportMode:" + Util.objectToString(_switchportMode) + "\n");
         System.out.println("+ " + string + "._switchportTrunkEncapsulation:" + Util.objectToString(_switchportTrunkEncapsulation) + "\n");
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string + "\n");
         System.out.println("- " + string + "._accessVlan:" + _accessVlan + "\n");
         System.out.println("- " + string + "._active:" + _active + "\n");
         Util.diffRepresentationLists(_allowedVlans, null, string + "._allowedVlans");
         System.out.println("- " + string + "._area:" + Util.objectToString(_area) + "\n");
         System.out.println("- " + string + "._bandwidth:" + Util.objectToString(_bandwidth) + "\n");
         System.out.println("- " + string + "._incomingFilter:" + Util.objectToString(_incomingFilter) + "\n");
         System.out.println("- " + string + "._ip:" + Util.objectToString(_ip) + "\n");
         System.out.println("- " + string + "._name:" + Util.objectToString(_name) + "\n");
         System.out.println("- " + string + "._nativeVlan:" + _nativeVlan + "\n");
         System.out.println("- " + string + "._ospfCost:" + Util.objectToString(_ospfCost) + "\n");
         System.out.println("- " + string + "._ospfDeadInterval:" + _ospfDeadInterval + "\n");
         System.out.println("- " + string + "._ospfHelloMultiplier:" + _ospfHelloMultiplier + "\n");
         System.out.println("- " + string + "._outgoingFilter:" + Util.objectToString(_outgoingFilter) + "\n");
         System.out.println("- " + string + "._routingPolicy:" + Util.objectToString(_routingPolicy) + "\n");
         Util.diffRepresentationMaps(_secondaryIps, null, string + "._secondaryIps");
         System.out.println("- " + string + "._subnet:" + Util.objectToString(_subnet) + "\n");
         System.out.println("- " + string + "._switchportMode:" + Util.objectToString(_switchportMode) + "\n");
         System.out.println("- " + string + "._switchportTrunkEncapsulation:" + Util.objectToString(_switchportTrunkEncapsulation) + "\n");
         System.out.flush();
         return;
      }

      Interface rhs = (Interface) o;
      if (_accessVlan != rhs._accessVlan) {
         System.out.println("- " + string + "._accessVlan:" + _accessVlan + "\n");
         System.out.println("+ " + string + "._accessVlan:" + rhs._accessVlan + "\n");
      }
      if (_active != rhs._active) {
         System.out.println("- " + string + "._active:" + _active + "\n");
         System.out.println("+ " + string + "._active:" + rhs._active
               + "\n");
      }
      Util.diffRepresentationLists(_allowedVlans, rhs._allowedVlans, string + "._allowedVlans");
      if (!Util.equalOrNull(_area, rhs._area)) {
         System.out.println("- " + string + "._area:" + Util.objectToString(_area) + "\n");
         System.out.println("+ " + string + "._area:" + Util.objectToString(rhs._area) + "\n");
      }
      if (!Util.equalOrNull(_bandwidth, rhs._bandwidth)) {
         System.out.println("- " + string + "._bandwidth:" + Util.objectToString(_bandwidth) + "\n");
         System.out.println("+ " + string + "._bandwidth:" + Util.objectToString(rhs._bandwidth) + "\n");
      }
      if (!Util.equalOrNull(_incomingFilter, rhs._incomingFilter)) {
         System.out.println("- " + string + "._incomingFilter:" + Util.objectToString(_incomingFilter) + "\n");
         System.out.println("+ " + string + "._incomingFilter:" + Util.objectToString(rhs._incomingFilter) + "\n");
      }
      if (!Util.equalOrNull(_ip, rhs._ip)) {
         System.out.println("- " + string + "._ip:" + Util.objectToString(_ip) + "\n");
         System.out.println("+ " + string + "._ip:" + Util.objectToString(rhs._ip) + "\n");
      }
      if (!Util.equalOrNull(_name, rhs._name)) {
         System.out.println("- " + string + "._name:" + Util.objectToString(_name) + "\n");
         System.out.println("+ " + string + "._name:" + Util.objectToString(rhs._name) + "\n");
      }
      if (_nativeVlan != rhs._nativeVlan) {
         System.out.println("- " + string + "._nativeVlan:" + _nativeVlan + "\n");
         System.out.println("+ " + string + "._nativeVlan:" + rhs._nativeVlan + "\n");
      }
      if (!Util.equalOrNull(_ospfCost, rhs._ospfCost)) {
         System.out.println("- " + string + "._ospfCost:" + Util.objectToString(_ospfCost) + "\n");
         System.out.println("+ " + string + "._ospfCost:" + Util.objectToString(rhs._ospfCost) + "\n");
      }
      if (_ospfDeadInterval != rhs._ospfDeadInterval) {
         System.out.println("- " + string + "._ospfDeadInterval:" + _ospfDeadInterval + "\n");
         System.out.println("+ " + string + "._ospfDeadInterval:" + rhs._ospfDeadInterval + "\n");
      }
      if (_ospfHelloMultiplier != rhs._ospfHelloMultiplier) {
         System.out.println("- " + string + "._ospfHelloMultiplier:" + _ospfHelloMultiplier + "\n");
         System.out.println("+ " + string + "._ospfHelloMultiplier:" + rhs._ospfHelloMultiplier + "\n");
      }
      if (!Util.equalOrNull(_outgoingFilter, rhs._outgoingFilter)) {
         System.out.println("- " + string + "._outgoingFilter:" + Util.objectToString(_outgoingFilter) + "\n");
         System.out.println("+ " + string + "._outgoingFilter:" + Util.objectToString(rhs._outgoingFilter) + "\n");
      }
      if (!Util.equalOrNull(_routingPolicy, rhs._routingPolicy)) {
         System.out.println("- " + string + "._routingPolicy:" + Util.objectToString(_routingPolicy) + "\n");
         System.out.println("+ " + string + "._routingPolicy:" + Util.objectToString(rhs._routingPolicy) + "\n");
      }
      Util.diffRepresentationMaps(_secondaryIps, rhs._secondaryIps, string + "._secondaryIps");
      if (!Util.equalOrNull(_subnet, rhs._subnet)) {
         System.out.println("- " + string + "._subnet:" + Util.objectToString(_subnet) + "\n");
         System.out.println("+ " + string + "._subnet:" + Util.objectToString(rhs._subnet) + "\n");
      }
      if (!Util.equalOrNull(_switchportMode, rhs._switchportMode)) {
         System.out.println("- " + string + "._switchportMode:" + Util.objectToString(_switchportMode) + "\n");
         System.out.println("+ " + string + "._switchportMode:" + Util.objectToString(rhs._switchportMode) + "\n");
      }
      if (!Util.equalOrNull(_switchportTrunkEncapsulation, rhs._switchportTrunkEncapsulation)) {
         System.out.println("- " + string + "._switchportTrunkEncapsulation:" + Util.objectToString(_switchportTrunkEncapsulation) + "\n");
         System.out.println("+ " + string + "._switchportTrunkEncapsulation:" + Util.objectToString(rhs._switchportTrunkEncapsulation) + "\n");
      }
      
      System.out.flush();
      return;
   }

}
