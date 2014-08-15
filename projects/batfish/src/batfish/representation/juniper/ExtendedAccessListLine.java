package batfish.representation.juniper;

import java.util.Collections;
import java.util.List;

import batfish.representation.LineAction;
import batfish.representation.RepresentationObject;
import batfish.util.SubRange;
import batfish.util.Util;

public class ExtendedAccessListLine implements RepresentationObject {

   private LineAction _ala;
   private String _dstIp;
   private List<SubRange> _dstPortRanges;
   private String _dstWildcard;
   private String _id;
   private int _protocol;
   private String _srcIp;
   private List<SubRange> _srcPortRanges;
   private String _srcWildcard;

   public ExtendedAccessListLine(LineAction ala, int protocol, String srcIp,
         String srcWildcard, String dstIp, String dstWildcard,
         List<SubRange> srcPortRanges, List<SubRange> dstPortRanges) {
      _ala = ala;
      _protocol = protocol;
      _srcIp = srcIp;
      _srcWildcard = srcWildcard;
      _dstIp = dstIp;
      _dstWildcard = dstWildcard;
      _srcPortRanges = srcPortRanges;
      _dstPortRanges = dstPortRanges;
      if (srcPortRanges == null
            && (Util.getProtocolName(protocol).equals("tcp") || Util
                  .getProtocolName(protocol).equals("udp"))) {
         srcPortRanges = Collections.singletonList(new SubRange(0, 65535));
      }
      if (dstPortRanges == null) {
         dstPortRanges = Collections.singletonList(new SubRange(0, 65535));
      }
   }

   public String getDestinationIP() {
      return _dstIp;
   }

   public String getDestinationWildcard() {
      return _dstWildcard;
   }

   public List<SubRange> getDstPortRanges() {
      return _dstPortRanges;
   }

   public String getID() {
      return _id;
   }

   public LineAction getLineAction() {
      return _ala;
   }

   public int getProtocol() {
      return _protocol;
   }

   public String getSourceIP() {
      return _srcIp;
   }

   public String getSourceWildcard() {
      return _srcWildcard;
   }

   public List<SubRange> getSrcPortRanges() {
      return _srcPortRanges;
   }

   public void setDestinationIP(String d) {
      _dstIp = d;
   }

   public void setDestinationWildcard(String d) {
      _dstWildcard = d;
   }

   public void setID(String i) {
      _id = i;
   }

   public void setLineAction(LineAction la) {
      _ala = la;
   }

   public void setPortRange(List<SubRange> sr) {
      _srcPortRanges = sr;
   }

   public void setProtocol(int p) {
      _protocol = p;
   }

   public void setSourceIP(String s) {
      _srcIp = s;
   }

   public void setSourceWildcard(String s) {
      _srcWildcard = s;
   }

   @Override
   public String toString() {
      String protocolName = Util.getProtocolName(_protocol);
      return "[Action:"
            + _ala
            + ", Protocol:"
            + (protocolName != null ? protocolName + "(" + _protocol + ")"
                  : _protocol) + ", SourceIp:" + _srcIp + ", SourceWildcard:"
            + _srcWildcard + ", DestinationIp:" + _dstIp
            + ", DestinationWildcard:" + _dstWildcard + ", PortRange:"
            + _srcPortRanges + "]";
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      ExtendedAccessListLine rhs = (ExtendedAccessListLine) o;
      return Util.equalOrNull(_ala, rhs._ala)
            && Util.equalOrNull(_dstIp, rhs._dstIp)
            && Util.cmpRepresentationLists(_dstPortRanges, rhs._dstPortRanges) == 0
            && Util.equalOrNull(_dstWildcard, rhs._dstWildcard)
            && Util.equalOrNull(_id, rhs._id)
            && _protocol == rhs._protocol
            && Util.equalOrNull(_srcIp, rhs._srcIp)
            && Util.cmpRepresentationLists(_srcPortRanges, rhs._srcPortRanges) == 0
            && Util.equalOrNull(_srcWildcard, rhs._srcWildcard);
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string + "\n");
         System.out.println("+ " + string + "._ala:" + Util.objectToString(_ala) + "\n");
         System.out.println("+ " + string + "._dstIp:" + Util.objectToString(_dstIp) + "\n");
         Util.diffRepresentationLists(null, _dstPortRanges, string + "._dstPortRanges");
         System.out.println("+ " + string + "._dstWildcard:" + Util.objectToString(_dstWildcard) + "\n");
         System.out.println("+ " + string + "._id:" + Util.objectToString(_id) + "\n");
         System.out.println("+ " + string + "._protocol:" + _protocol + "\n");
         System.out.println("+ " + string + "._srcIp:" + Util.objectToString(_srcIp) + "\n");
         Util.diffRepresentationLists(null, _srcPortRanges, string + "._srcPortRanges");
         System.out.println("+ " + string + "._srcWildcard:" + Util.objectToString(_srcWildcard) + "\n");
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string + "\n");
         System.out.println("- " + string + "._ala:" + Util.objectToString(_ala) + "\n");
         System.out.println("- " + string + "._dstIp:" + Util.objectToString(_dstIp) + "\n");
         Util.diffRepresentationLists(_dstPortRanges, null, string + "._dstPortRanges");

         System.out.println("- " + string + "._dstWildcard:" + Util.objectToString(_dstWildcard) + "\n");
         System.out.println("- " + string + "._id:" + Util.objectToString(_id) + "\n");
         System.out.println("- " + string + "._protocol:" + _protocol + "\n");
         System.out.println("- " + string + "._srcIp:" + Util.objectToString(_srcIp) + "\n");
         Util.diffRepresentationLists(_srcPortRanges, null, string + "._srcPortRanges");
         System.out.println("- " + string + "._srcWildcard:" + Util.objectToString(_srcWildcard) + "\n");
         System.out.flush();
         return;
      }

      ExtendedAccessListLine rhs = (ExtendedAccessListLine) o;

      if (!Util.equalOrNull(_ala, rhs._ala)) {
         System.out.println("- " + string + "._ala:" + Util.objectToString(_ala) + "\n");
         System.out.println("+ " + string + "._ala:" + Util.objectToString(rhs._ala) + "\n");
      }
      if (!Util.equalOrNull(_dstIp, rhs._dstIp)) {
         System.out.println("- " + string + "._dstIp:" + Util.objectToString(_dstIp) + "\n");
         System.out.println("+ " + string + "._dstIp:" + Util.objectToString(rhs._dstIp) + "\n");
      }
      Util.diffRepresentationLists(_dstPortRanges, rhs._dstPortRanges, string + "._dstPortRanges");
      if (!Util.equalOrNull(_dstWildcard, rhs._dstWildcard)) {
         System.out.println("- " + string + "._dstWildcard:" + Util.objectToString(_dstWildcard) + "\n");
         System.out.println("+ " + string + "._dstWildcard:" + Util.objectToString(rhs._dstWildcard) + "\n");
      }
      if (!Util.equalOrNull(_id, rhs._id)) {
         System.out.println("- " + string + "._id:" + Util.objectToString(_id) + "\n");
         System.out.println("+ " + string + "._id:" + Util.objectToString(rhs._id) + "\n");
      }
      if (_protocol != rhs._protocol) {
         System.out.println("- " + string + "._protocol:" + _protocol + "\n");
         System.out.println("+ " + string + "._protocol:" + rhs._protocol + "\n");
      }
      if (!Util.equalOrNull(_srcIp, rhs._srcIp)) {
         System.out.println("- " + string + "._srcIp:" + Util.objectToString(_srcIp) + "\n");
         System.out.println("+ " + string + "._srcIp:" + Util.objectToString(rhs._srcIp) + "\n");
      }
      Util.diffRepresentationLists(_srcPortRanges, rhs._srcPortRanges, string + "._srcPortRanges");
      if (!Util.equalOrNull(_srcWildcard, rhs._srcWildcard)) {
         System.out.println("- " + string + "._srcWildcard:" + Util.objectToString(_srcWildcard) + "\n");
         System.out.println("+ " + string + "._srcWildcard:" + Util.objectToString(rhs._srcWildcard) + "\n");
      }
      
      System.out.flush();
      return;
   }

}
