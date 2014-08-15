package batfish.representation.juniper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class BGPProcess implements RepresentationObject {

   private int _pid;
   private String _routerID;
   private Map<String, BGPGroup> _peerGroups;
   private ArrayList<String> _activatedNeighbors;

   public BGPProcess(int procnum) {
      _pid = procnum;
      _peerGroups = new HashMap<String, BGPGroup>();
      _activatedNeighbors = new ArrayList<String>();
   }

   public int getPid() {
      return _pid;
   }

   public String getRouterID() {
      return _routerID;
   }

   public Map<String, BGPGroup> getAllPeerGroups() {
      return _peerGroups;
   }

   public BGPGroup getPeerGroup(String name) {
      return _peerGroups.get(name);
   }

   public void addPeerGroup(BGPGroup peerGroup) {
      _peerGroups.put(peerGroup.getName(), peerGroup);
   }

   public List<String> getActivatedNeighbors() {
      return _activatedNeighbors;
   }

   public void addActivatedNeighbor(String address) {
      _activatedNeighbors.add(address);
   }

   public void addActivatedNeighbors(List<String> addresses) {
      _activatedNeighbors.addAll(addresses);
   }

   public void setAsNum(int as) {
      _pid = as;
   }

   public void setRouterID(String id) {
      _routerID = id;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      BGPProcess rhs = (BGPProcess) o;
      return _pid == rhs._pid
            && Util.equalOrNull(_routerID, rhs._routerID)
            && Util.cmpRepresentationMaps(_peerGroups, rhs._peerGroups) == 0
            && Util.cmpRepresentationLists(_activatedNeighbors,
                  rhs._activatedNeighbors) == 0;
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string + "\n");
         System.out.println("+ " + string + "._pid:" + _pid + "\n");
         System.out.println("+ " + string + "._routerID:"
               + Util.objectToString(_routerID) + "\n");
         Util.diffRepresentationMaps(null, _peerGroups, string + "._peerGroups");
         Util.diffRepresentationLists(null, _activatedNeighbors, string
               + "._activatedNeighbors");
         return;
      }

      if (o == null) {
         System.out.println("- " + string + "\n");
         System.out.println("- " + string + "._pid:" + _pid + "\n");
         System.out.println("- " + string + "._routerID:"
               + Util.objectToString(_routerID) + "\n");
         Util.diffRepresentationMaps(_peerGroups, null, string + "._peerGroups");
         Util.diffRepresentationLists(_activatedNeighbors, null, string
               + "._activatedNeighbors");
         return;
      }

      BGPProcess rhs = (BGPProcess) o;

      if (_pid != rhs._pid) {
         System.out.println("- " + string + "._pid:" + _pid + "\n");
         System.out.println("+ " + string + "._pid:" + rhs._pid + "\n");
      }

      if (!Util.equalOrNull(_routerID, rhs._routerID)) {
         System.out.println("- " + string + "._routerID:"
               + Util.objectToString(_routerID) + "\n");
         System.out.println("+ " + string + "._routerID:"
               + Util.objectToString(rhs._routerID) + "\n");
      }

      Util.diffRepresentationMaps(_peerGroups, rhs._peerGroups, string
            + "._peerGroups");
      Util.diffRepresentationLists(_activatedNeighbors,
            rhs._activatedNeighbors, string + "._activatedNeighbors");

      System.out.flush();
      return;
   }

}
