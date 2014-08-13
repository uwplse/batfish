package batfish.representation.juniper;

import java.util.List;

import batfish.representation.LineAction;
import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class PolicyStatementClause implements RepresentationObject {

   private String _mapName;
   private List<PolicyStatementMatchLine> _matchList;
   private List<PolicyStatementSetLine> _setList;
   private int _seqNum;
   private LineAction _type;
   private String _clauseName;

   public PolicyStatementClause(LineAction type, String name, int num,
         List<PolicyStatementMatchLine> mlist,
         List<PolicyStatementSetLine> slist) {
      _type = type;
      _mapName = name;
      _seqNum = num;
      _matchList = mlist;
      _setList = slist;
   }

   public void setClauseName(String n) {
      _clauseName = n;
   }

   public void addMatchLines(List<PolicyStatementMatchLine> m) {
      _matchList.addAll(m);
   }

   public void addSetLines(List<PolicyStatementSetLine> s) {
      _setList.addAll(s);
   }

   public void setAction(LineAction a) {
      _type = a;
   }

   public String getClauseName() {
      return _clauseName;
   }

   public LineAction getAction() {
      return _type;
   }

   public String getMapName() {
      return _mapName;
   }

   public List<PolicyStatementMatchLine> getMatchList() {
      return _matchList;
   }

   public List<PolicyStatementSetLine> getSetList() {
      return _setList;
   }

   public int getSeqNum() {
      return _seqNum;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      PolicyStatementClause rhs = (PolicyStatementClause) o;
      return Util.equalOrNull(_mapName, rhs._mapName)
            && Util.cmpRepresentationLists(_matchList, rhs._matchList) == 0
            && Util.cmpRepresentationLists(_setList, rhs._setList) == 0
            && _seqNum == rhs._seqNum && Util.equalOrNull(_type, rhs._type)
            && Util.equalOrNull(_clauseName, rhs._clauseName);
   }

}
