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

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string);
         System.out.println("+ " + string + "._pid:" + _pid);
         Util.diffRepresentationLists(null, _networks, string + "._networks");
         System.out.println("+ " + string + "._routerId:"
               + Util.objectToString(_routerId));
         System.out.println("+ " + string + "._referenceBandwidth:"
               + _referenceBandwidth);
         Util.diffRepresentationLists(null, _exportPolicyStatements, string
               + "._exportPolicyStatements");
         return;
      }

      if (o == null) {
         System.out.println("- " + string);
         System.out.println("- " + string + "._pid:" + _pid);
         Util.diffRepresentationLists(_networks, null, string + "._networks");
         System.out.println("- " + string + "._routerId:"
               + Util.objectToString(_routerId));
         System.out.println("- " + string + "._referenceBandwidth:"
               + _referenceBandwidth);
         Util.diffRepresentationLists(_exportPolicyStatements, null, string
               + "._exportPolicyStatements");
         return;
      }

      OSPFProcess rhs = (OSPFProcess) o;
      if (_pid != rhs._pid) {
         System.out.println("- " + string + "._pid:" + _pid);
         System.out.println("+ " + string + "._pid:" + rhs._pid);
      }
      Util.diffRepresentationLists(_networks, rhs._networks, string
            + "._networks");
      if (!Util.equalOrNull(_routerId, rhs._routerId)) {
         System.out.println("- " + string + "._routerId:"
               + Util.objectToString(_routerId));
         System.out.println("+ " + string + "._routerId:"
               + Util.objectToString(rhs._routerId));
      }
      if (!Util.equalOrNull(_referenceBandwidth, rhs._referenceBandwidth)) {
         System.out.println("- " + string + "._referenceBandwidth:"
               + Util.objectToString(_referenceBandwidth));
         System.out.println("+ " + string + "._referenceBandwidth:"
               + Util.objectToString(rhs._referenceBandwidth));
      }
      Util.diffRepresentationLists(_exportPolicyStatements,
            rhs._exportPolicyStatements, string + "._exportPolicyStatements");
      System.out.flush();
      return;
   }

}
