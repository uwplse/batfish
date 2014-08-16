package batfish.representation.juniper;

import java.util.ArrayList;
import java.util.List;

import batfish.representation.RepresentationObject;
import batfish.representation.SwitchportEncapsulationType;
import batfish.representation.SwitchportMode;
import batfish.util.SubRange;
import batfish.util.Util;

public class Interface implements RepresentationObject {

   private int _accessVlan;
   private boolean _active;
   private ArrayList<SubRange> _allowedVlans;
   private Double _bandwidth;
   private String _incomingFilter;
   private String _ip;
   private String _name;
   private int _nativeVlan;
   private Integer _ospfCost;
   private int _ospfDeadInterval;
   private int _ospfHelloMultiplier;
   private String _outgoingFilter;
   private String _subnet;
   private SwitchportMode _switchportMode;
   private SwitchportEncapsulationType _switchportTrunkEncapsulation;

   public Interface(String name) {
      _name = name;
      _ip = "";
      _active = true;
      _nativeVlan = 1;
      _switchportMode = SwitchportMode.NONE;
      _allowedVlans = new ArrayList<SubRange>();
      _ospfCost = null;
   }

   public void setBandwidth(Double bandwidth) {
      _bandwidth = bandwidth;
   }

   public double getBandwidth() {
      return _bandwidth;
   }

   public String getName() {
      return _name;
   }

   public boolean getActive() {
      return _active;
   }

   public void setActive(boolean active) {
      _active = active;
   }

   public String getIP() {
      return _ip;
   }

   public int getAccessVlan() {
      return _accessVlan;
   }

   public int getNativeVlan() {
      return _nativeVlan;
   }

   public void setNativeVlan(int vlan) {
      _nativeVlan = vlan;
   }

   public int getOSPFDeadInterval() {
      return _ospfDeadInterval;
   }

   public void setOSPFDeadInterval(int seconds) {
      _ospfDeadInterval = seconds;
   }

   public int getOSPFHelloMultiplier() {
      return _ospfHelloMultiplier;
   }

   public void setOSPFHelloMultiplier(int multiplier) {
      _ospfHelloMultiplier = multiplier;
   }

   public void setIP(String ip) {
      _ip = ip;
   }

   public String getSubnetMask() {
      return _subnet;
   }

   public void setSubnetMask(String subnet) {
      _subnet = subnet;
   }

   public void setAccessVlan(int vlan) {
      _accessVlan = vlan;
   }

   public SwitchportMode getSwitchportMode() {
      return _switchportMode;
   }

   public void setSwitchportMode(SwitchportMode switchportMode) {
      _switchportMode = switchportMode;
   }

   public List<SubRange> getAllowedVlans() {
      return _allowedVlans;
   }

   public void addAllowedRanges(List<SubRange> ranges) {
      _allowedVlans.addAll(ranges);
   }

   public SwitchportEncapsulationType getSwitchportTrunkEncapsulation() {
      return _switchportTrunkEncapsulation;
   }

   public void setSwitchportTrunkEncapsulation(
         SwitchportEncapsulationType encapsulation) {
      _switchportTrunkEncapsulation = encapsulation;
   }

   public void setOspfCost(int defaultOspfCost) {
      _ospfCost = defaultOspfCost;
   }

   public Integer getOspfCost() {
      return _ospfCost;
   }

   public String getOutgoingFilter() {
      return _outgoingFilter;
   }

   public void setOutgoingFilter(String accessListName) {
      _outgoingFilter = accessListName;
   }

   public void setIncomingFilter(String accessListName) {
      _incomingFilter = accessListName;
   }

   public String getIncomingFilter() {
      return _incomingFilter;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      Interface rhs = (Interface) o;
      return _accessVlan == rhs._accessVlan
            && _active == rhs._active
            && (Util.cmpRepresentationLists(_allowedVlans, rhs._allowedVlans) == 0)
            && Util.equalOrNull(_bandwidth, rhs._bandwidth)
            && Util.equalOrNull(_incomingFilter, rhs._incomingFilter)
            && Util.equalOrNull(_ip, rhs._ip)
            && Util.equalOrNull(_name, rhs._name)
            && _nativeVlan == rhs._nativeVlan
            && Util.equalOrNull(_ospfCost, rhs._ospfCost)
            && _ospfDeadInterval == rhs._ospfDeadInterval
            && _ospfHelloMultiplier == rhs._ospfHelloMultiplier
            && Util.equalOrNull(_outgoingFilter, rhs._outgoingFilter)
            && Util.equalOrNull(_subnet, rhs._subnet)
            && Util.equalOrNull(_switchportMode, rhs._switchportMode)
            && Util.equalOrNull(_switchportTrunkEncapsulation,
                  rhs._switchportTrunkEncapsulation);
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string);
         System.out.println("+ " + string + "._accessVlan:" + _accessVlan);
         System.out.println("+ " + string + "._active:" + _active);
         Util.diffRepresentationLists(null, _allowedVlans, string
               + "._allowedVlans");
         System.out.println("+ " + string + "._bandwidth:"
               + Util.objectToString(_bandwidth));
         System.out.println("+ " + string + "._incomingFilter:"
               + Util.objectToString(_incomingFilter));
         System.out.println("+ " + string + "._ip:" + Util.objectToString(_ip));
         System.out.println("+ " + string + "._name:"
               + Util.objectToString(_name));
         System.out.println("+ " + string + "._nativeVlan:" + _nativeVlan);
         System.out.println("+ " + string + "._ospfCost:"
               + Util.objectToString(_ospfCost));
         System.out.println("+ " + string + "._ospfDeadInterval:"
               + _ospfDeadInterval);
         System.out.println("+ " + string + "._ospfHelloMultiplier:"
               + _ospfHelloMultiplier);
         System.out.println("+ " + string + "._outgoingFilter:"
               + Util.objectToString(_outgoingFilter));
         System.out.println("+ " + string + "._subnet:"
               + Util.objectToString(_subnet));
         System.out.println("+ " + string + "._switchportMode:"
               + Util.objectToString(_switchportMode));
         System.out.println("+ " + string + "._switchportTrunkEncapsulation:"
               + Util.objectToString(_switchportTrunkEncapsulation));
         return;
      }

      if (o == null) {
         System.out.println("- " + string);
         System.out.println("- " + string + "._accessVlan:" + _accessVlan);
         System.out.println("- " + string + "._active:" + _active);
         Util.diffRepresentationLists(_allowedVlans, null, string
               + "._allowedVlans");
         System.out.println("- " + string + "._bandwidth:"
               + Util.objectToString(_bandwidth));
         System.out.println("- " + string + "._incomingFilter:"
               + Util.objectToString(_incomingFilter));
         System.out.println("- " + string + "._ip:" + Util.objectToString(_ip));
         System.out.println("- " + string + "._name:"
               + Util.objectToString(_name));
         System.out.println("- " + string + "._nativeVlan:" + _nativeVlan);
         System.out.println("- " + string + "._ospfCost:"
               + Util.objectToString(_ospfCost));
         System.out.println("- " + string + "._ospfDeadInterval:"
               + _ospfDeadInterval);
         System.out.println("- " + string + "._ospfHelloMultiplier:"
               + _ospfHelloMultiplier);
         System.out.println("- " + string + "._outgoingFilter:"
               + Util.objectToString(_outgoingFilter));
         System.out.println("- " + string + "._subnet:"
               + Util.objectToString(_subnet));
         System.out.println("- " + string + "._switchportMode:"
               + Util.objectToString(_switchportMode));
         System.out.println("- " + string + "._switchportTrunkEncapsulation:"
               + Util.objectToString(_switchportTrunkEncapsulation));
         return;
      }

      Interface rhs = (Interface) o;

      if (_accessVlan != rhs._accessVlan) {
         System.out.println("- " + string + "._accessVlan:" + _accessVlan);
         System.out.println("+ " + string + "._accessVlan:" + rhs._accessVlan);
      }

      if (_active != rhs._active) {
         System.out.println("- " + string + "._active:" + _active);
         System.out.println("+ " + string + "._active:" + rhs._active);
      }

      Util.diffRepresentationLists(_allowedVlans, rhs._allowedVlans, string
            + "._allowedVlans");

      if (!Util.equalOrNull(_bandwidth, rhs._bandwidth)) {
         System.out.println("- " + string + "._bandwidth:"
               + Util.objectToString(_bandwidth));
         System.out.println("+ " + string + "._bandwidth:"
               + Util.objectToString(rhs._bandwidth));
      }

      if (!Util.equalOrNull(_incomingFilter, rhs._incomingFilter)) {
         System.out.println("- " + string + "._incomingFilter:"
               + Util.objectToString(_incomingFilter));
         System.out.println("+ " + string + "._incomingFilter:"
               + Util.objectToString(rhs._incomingFilter));
      }

      if (!Util.equalOrNull(_ip, rhs._ip)) {
         System.out.println("- " + string + "._ip:" + Util.objectToString(_ip));
         System.out.println("+ " + string + "._ip:"
               + Util.objectToString(rhs._ip));
      }

      if (!Util.equalOrNull(_name, rhs._name)) {
         System.out.println("- " + string + "._name:"
               + Util.objectToString(_name));
         System.out.println("+ " + string + "._name:"
               + Util.objectToString(rhs._name));
      }

      if (_nativeVlan != rhs._nativeVlan) {
         System.out.println("- " + string + "._nativeVlan:" + _nativeVlan);
         System.out.println("+ " + string + "._nativeVlan:" + rhs._nativeVlan);
      }

      if (!Util.equalOrNull(_ospfCost, rhs._ospfCost)) {
         System.out.println("- " + string + "._ospfCost:"
               + Util.objectToString(_ospfCost));
         System.out.println("+ " + string + "._ospfCost:"
               + Util.objectToString(rhs._ospfCost));
      }

      if (_ospfDeadInterval != rhs._ospfDeadInterval) {
         System.out.println("- " + string + "._ospfDeadInterval:"
               + _ospfDeadInterval);
         System.out.println("+ " + string + "._ospfDeadInterval:"
               + rhs._ospfDeadInterval);
      }

      if (_ospfHelloMultiplier != rhs._ospfHelloMultiplier) {
         System.out.println("- " + string + "._ospfHelloMultiplier:"
               + _ospfHelloMultiplier);
         System.out.println("+ " + string + "._ospfHelloMultiplier:"
               + rhs._ospfHelloMultiplier);
      }

      if (!Util.equalOrNull(_outgoingFilter, rhs._outgoingFilter)) {
         System.out.println("- " + string + "._outgoingFilter:"
               + Util.objectToString(_outgoingFilter));
         System.out.println("+ " + string + "._outgoingFilter:"
               + Util.objectToString(rhs._outgoingFilter));
      }

      if (!Util.equalOrNull(_subnet, rhs._subnet)) {
         System.out.println("- " + string + "._subnet:"
               + Util.objectToString(_subnet));
         System.out.println("+ " + string + "._subnet:"
               + Util.objectToString(rhs._subnet));
      }

      if (!Util.equalOrNull(_switchportMode, rhs._switchportMode)) {
         System.out.println("- " + string + "._switchportMode:"
               + Util.objectToString(_switchportMode));
         System.out.println("+ " + string + "._switchportMode:"
               + Util.objectToString(rhs._switchportMode));
      }

      if (!Util.equalOrNull(_switchportTrunkEncapsulation,
            rhs._switchportTrunkEncapsulation)) {
         System.out.println("- " + string + "._switchportTrunkEncapsulation:"
               + Util.objectToString(_switchportTrunkEncapsulation));
         System.out.println("+ " + string + "._switchportTrunkEncapsulation:"
               + Util.objectToString(rhs._switchportTrunkEncapsulation));
      }

      System.out.flush();
      return;
   }
}
