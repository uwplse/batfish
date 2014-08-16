package batfish.representation.cisco;

import java.io.Serializable;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import batfish.representation.Ip;
import batfish.representation.OspfMetricType;
import batfish.representation.Protocol;
import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class OspfProcess implements Serializable, RepresentationObject {

   private static final int DEFAULT_DEFAULT_INFORMATION_METRIC = 1;

   private static final OspfMetricType DEFAULT_DEFAULT_INFORMATION_METRIC_TYPE = OspfMetricType.E2;

   /**
    * bits per second
    */
   private static final double DEFAULT_REFERENCE_BANDWIDTH = 1E9;

   private static final long serialVersionUID = 1L;

   private int _defaultInformationMetric;
   private OspfMetricType _defaultInformationMetricType;
   private boolean _defaultInformationOriginate;
   private boolean _defaultInformationOriginateAlways;
   private String _defaultInformationOriginateMap;
   private Set<String> _interfaceBlacklist;
   private Set<String> _interfaceWhitelist;
   private Set<OspfNetwork> _networks;
   private Map<Integer, Boolean> _nssas;
   private boolean _passiveInterfaceDefault;
   private int _pid;
   private Map<Protocol, OspfRedistributionPolicy> _redistributionPolicies;
   private double _referenceBandwidth;
   private Ip _routerId;
   private Set<OspfWildcardNetwork> _wildcardNetworks;

   public OspfProcess(int procnum) {
      _pid = procnum;
      _referenceBandwidth = DEFAULT_REFERENCE_BANDWIDTH;
      _networks = new TreeSet<OspfNetwork>();
      _defaultInformationOriginate = false;
      _defaultInformationOriginateAlways = false;
      _defaultInformationOriginateMap = null;
      _defaultInformationMetric = DEFAULT_DEFAULT_INFORMATION_METRIC;
      _defaultInformationMetricType = DEFAULT_DEFAULT_INFORMATION_METRIC_TYPE;
      _passiveInterfaceDefault = false;
      _nssas = new HashMap<Integer, Boolean>();
      _interfaceBlacklist = new HashSet<String>();
      _interfaceWhitelist = new HashSet<String>();
      _wildcardNetworks = new TreeSet<OspfWildcardNetwork>();
      _redistributionPolicies = new EnumMap<Protocol, OspfRedistributionPolicy>(
            Protocol.class);
   }

   public void computeNetworks(Collection<Interface> interfaces) {
      for (Interface i : interfaces) {
         String iname = i.getName();
         if (_interfaceBlacklist.contains(iname)
               || (_passiveInterfaceDefault && !_interfaceWhitelist
                     .contains(iname))) {
            continue;
         }
         Ip intIp = i.getIP();
         if (intIp == null) {
            continue;
         }
         for (OspfWildcardNetwork wn : _wildcardNetworks) {
            Ip wildcardAsMask = wn.getWildcard().inverted();
            Ip intWildcardNetwork = intIp.getNetworkAddress(wildcardAsMask);
            Ip wildcardNetwork = wn.getNetworkAddress();
            Ip maskedWildcardNetwork = wildcardNetwork
                  .getNetworkAddress(wildcardAsMask);
            if (maskedWildcardNetwork.equals(intWildcardNetwork)) {
               Ip intSubnetMask = i.getSubnetMask();
               Ip intNetwork = intIp.getNetworkAddress(intSubnetMask);
               _networks.add(new OspfNetwork(intNetwork, intSubnetMask, wn
                     .getArea()));
               break;
            }
         }
      }
   }

   public int getDefaultInformationMetric() {
      return _defaultInformationMetric;
   }

   public OspfMetricType getDefaultInformationMetricType() {
      return _defaultInformationMetricType;
   }

   public boolean getDefaultInformationOriginate() {
      return _defaultInformationOriginate;
   }

   public boolean getDefaultInformationOriginateAlways() {
      return _defaultInformationOriginateAlways;
   }

   public String getDefaultInformationOriginateMap() {
      return _defaultInformationOriginateMap;
   }

   public Set<String> getInterfaceBlacklist() {
      return _interfaceBlacklist;
   }

   public Set<String> getInterfaceWhitelist() {
      return _interfaceWhitelist;
   }

   public Set<OspfNetwork> getNetworks() {
      return _networks;
   }

   public Map<Integer, Boolean> getNssas() {
      return _nssas;
   }

   public int getPid() {
      return _pid;
   }

   public Map<Protocol, OspfRedistributionPolicy> getRedistributionPolicies() {
      return _redistributionPolicies;
   }

   public double getReferenceBandwidth() {
      return _referenceBandwidth;
   }

   public Ip getRouterId() {
      return _routerId;
   }

   public Set<OspfWildcardNetwork> getWildcardNetworks() {
      return _wildcardNetworks;
   }

   public void setDefaultInformationMetric(int metric) {
      _defaultInformationMetric = metric;
   }

   public void setDefaultInformationMetricType(OspfMetricType metricType) {
      _defaultInformationMetricType = metricType;
   }

   public void setDefaultInformationOriginate(boolean b) {
      _defaultInformationOriginate = b;
   }

   public void setDefaultInformationOriginateAlways(boolean b) {
      _defaultInformationOriginateAlways = b;
   }

   public void setDefaultInformationOriginateMap(String name) {
      _defaultInformationOriginateMap = name;
   }

   public void setPassiveInterfaceDefault(boolean b) {
      _passiveInterfaceDefault = b;
   }

   public void setReferenceBandwidth(double referenceBandwidth) {
      _referenceBandwidth = referenceBandwidth;
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
         System.out.println("+ " + string + "._defaultInformationMetric:"
               + _defaultInformationMetric);
         System.out.println("+ " + string + "._defaultInformationMetricType:"
               + Util.objectToString(_defaultInformationMetricType));
         System.out.println("+ " + string + "._defaultInformationOriginate:"
               + _defaultInformationOriginate);
         System.out.println("+ " + string
               + "._defaultInformationOriginateAlways:"
               + _defaultInformationOriginateAlways);
         System.out.println("+ " + string + "._defaultInformationOriginateMap:"
               + Util.objectToString(_defaultInformationOriginateMap));
         Util.diffRepresentationSets(null, _interfaceBlacklist, string
               + "._interfaceBlacklist");
         Util.diffRepresentationSets(null, _interfaceWhitelist, string
               + "._interfaceWhitelist");
         Util.diffRepresentationSets(null, _networks, string + "._networks");
         Util.diffRepresentationMaps(null, _nssas, string + "._nssas");
         System.out.println("+ " + string + "._passiveInterfaceDefault:"
               + _passiveInterfaceDefault);
         System.out.println("+ " + string + "._pid:" + _pid);
         Util.diffRepresentationMaps(null, _redistributionPolicies, string
               + "._redistributionPolicies");
         System.out.println("+ " + string + "._referenceBandwidth:"
               + _referenceBandwidth);
         System.out.println("+ " + string + "._routerId:"
               + Util.objectToString(_routerId));
         Util.diffRepresentationSets(null, _wildcardNetworks, string
               + "._wildcardNetworks");
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string);
         System.out.println("- " + string + "._defaultInformationMetric:"
               + _defaultInformationMetric);
         System.out.println("- " + string + "._defaultInformationMetricType:"
               + Util.objectToString(_defaultInformationMetricType));
         System.out.println("- " + string + "._defaultInformationOriginate:"
               + _defaultInformationOriginate);
         System.out.println("- " + string
               + "._defaultInformationOriginateAlways:"
               + _defaultInformationOriginateAlways);
         System.out.println("- " + string + "._defaultInformationOriginateMap:"
               + Util.objectToString(_defaultInformationOriginateMap));
         Util.diffRepresentationSets(_interfaceBlacklist, null, string
               + "._interfaceBlacklist");
         Util.diffRepresentationSets(_interfaceWhitelist, null, string
               + "._interfaceWhitelist");
         Util.diffRepresentationSets(_networks, null, string + "._networks");
         Util.diffRepresentationMaps(_nssas, null, string + "._nssas");
         System.out.println("- " + string + "._passiveInterfaceDefault:"
               + _passiveInterfaceDefault);
         System.out.println("- " + string + "._pid:" + _pid);
         Util.diffRepresentationMaps(_redistributionPolicies, null, string
               + "._redistributionPolicies");
         System.out.println("- " + string + "._referenceBandwidth:"
               + _referenceBandwidth);
         System.out.println("- " + string + "._routerId:"
               + Util.objectToString(_routerId));
         Util.diffRepresentationSets(_wildcardNetworks, null, string
               + "._wildcardNetworks");
         System.out.flush();
         return;
      }

      OspfProcess rhs = (OspfProcess) o;
      if (_defaultInformationMetric != rhs._defaultInformationMetric) {
         System.out.println("- " + string + "._defaultInformationMetric:"
               + _defaultInformationMetric);
         System.out.println("+ " + string + "._defaultInformationMetric:"
               + rhs._defaultInformationMetric);
      }
      if (!Util.equalOrNull(_defaultInformationMetricType,
            rhs._defaultInformationMetricType)) {
         System.out.println("- " + string + "._defaultInformationMetricType:"
               + Util.objectToString(_defaultInformationMetricType));
         System.out.println("+ " + string + "._defaultInformationMetricType:"
               + Util.objectToString(rhs._defaultInformationMetricType));
      }
      if (_defaultInformationOriginate != rhs._defaultInformationOriginate) {
         System.out.println("- " + string + "._defaultInformationOriginate:"
               + _defaultInformationOriginate);
         System.out.println("+ " + string + "._defaultInformationOriginate:"
               + rhs._defaultInformationOriginate);
      }
      if (_defaultInformationOriginateAlways != rhs._defaultInformationOriginateAlways) {
         System.out.println("- " + string
               + "._defaultInformationOriginateAlways:"
               + _defaultInformationOriginateAlways);
         System.out.println("+ " + string
               + "._defaultInformationOriginateAlways:"
               + rhs._defaultInformationOriginateAlways);
      }
      if (!Util.equalOrNull(_defaultInformationOriginateMap,
            rhs._defaultInformationOriginateMap)) {
         System.out.println("- " + string + "._defaultInformationOriginateMap:"
               + Util.objectToString(_defaultInformationOriginateMap));
         System.out.println("+ " + string + "._defaultInformationOriginateMap:"
               + Util.objectToString(rhs._defaultInformationOriginateMap));
      }
      Util.diffRepresentationSets(_interfaceBlacklist, rhs._interfaceBlacklist,
            string + "._interfaceBlacklist");
      Util.diffRepresentationSets(_interfaceWhitelist, rhs._interfaceWhitelist,
            string + "._interfaceWhitelist");
      Util.diffRepresentationSets(_networks, rhs._networks, string
            + "._networks");
      Util.diffRepresentationMaps(_nssas, rhs._nssas, string + "._nssas");
      if (_passiveInterfaceDefault != rhs._passiveInterfaceDefault) {
         System.out.println("- " + string + "._passiveInterfaceDefault:"
               + _passiveInterfaceDefault);
         System.out.println("+ " + string + "._passiveInterfaceDefault:"
               + rhs._passiveInterfaceDefault);
      }
      if (_pid != rhs._pid) {
         System.out.println("- " + string + "._pid:" + _pid);
         System.out.println("+ " + string + "._pid:" + rhs._pid);
      }
      Util.diffRepresentationMaps(_redistributionPolicies,
            rhs._redistributionPolicies, string + "._redistributionPolicies");
      if (_referenceBandwidth != rhs._referenceBandwidth) {
         System.out.println("- " + string + "._referenceBandwidth:"
               + _referenceBandwidth);
         System.out.println("+ " + string + "._referenceBandwidth:"
               + rhs._referenceBandwidth);
      }
      if (!Util.equalOrNull(_routerId, rhs._routerId)) {
         System.out.println("- " + string + "._routerId:"
               + Util.objectToString(_routerId));
         System.out.println("+ " + string + "._routerId:"
               + Util.objectToString(rhs._routerId));
      }
      Util.diffRepresentationSets(_wildcardNetworks, rhs._wildcardNetworks,
            string + "._wildcardNetworks");
      System.out.flush();
      return;
   }

}
