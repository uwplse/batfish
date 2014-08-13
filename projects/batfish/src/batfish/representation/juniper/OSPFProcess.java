package batfish.representation.juniper;

import java.util.ArrayList;
import java.util.List;

import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class OSPFProcess implements RepresentationObject {
   private int _pid;
   private ArrayList<OSPFNetwork> _networks;
   private String _routerId;
   private double _referenceBandwidth;
   private List<String> _exportPolicyStatements;

   public OSPFProcess(int procnum) {
      _pid = procnum;
      _referenceBandwidth = 0;
      _networks = new ArrayList<OSPFNetwork>();
      _exportPolicyStatements = new ArrayList<String>();
   }

   public double getReferenceBandwidth() {
      return _referenceBandwidth;
   }

   public void setReferenceBandwidth(double referenceBandwidth) {
      _referenceBandwidth = referenceBandwidth;
   }

   public int getPid() {
      return _pid;
   }

   public List<OSPFNetwork> getNetworks() {
      return _networks;
   }

   public void addNetwork(String networkAddress, String subnetMask, int area) {
      _networks.add(new OSPFNetwork(networkAddress, subnetMask, area));

   }

   public void addNetworkByInterface(String inf, int area) {
      _networks.add(new OSPFNetwork(inf, area));
   }

   public void setRouterId(String id) {
      _routerId = id;
   }

   public String getRouterId() {
      return _routerId;
   }

   public void addExportPolicyStatements(List<String> ps) {
      _exportPolicyStatements.addAll(ps);
   }

   public List<String> getExportPolicyStatements() {
      return _exportPolicyStatements;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      OSPFProcess rhs = (OSPFProcess) o;
      return _pid == rhs._pid
            && (Util.cmpRepresentationLists(_networks, rhs._networks) == 0)
            && Util.equalOrNull(_routerId, rhs._routerId)
            && _referenceBandwidth == rhs._referenceBandwidth
            && (Util.cmpRepresentationLists(_exportPolicyStatements,
                  rhs._exportPolicyStatements) == 0);
   }

}
