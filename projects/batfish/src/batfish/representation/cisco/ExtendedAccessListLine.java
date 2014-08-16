package batfish.representation.cisco;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import batfish.representation.Ip;
import batfish.representation.LineAction;
import batfish.representation.RepresentationObject;
import batfish.util.SubRange;
import batfish.util.Util;

public class ExtendedAccessListLine implements Serializable,
      RepresentationObject {

   private static final long serialVersionUID = 1L;

   private LineAction _action;
   private Ip _dstIp;
   private List<SubRange> _dstPortRanges;
   private Ip _dstWildcard;
   private int _protocol;
   private Ip _srcIp;
   private List<SubRange> _srcPortRanges;
   private Ip _srcWildcard;

   public ExtendedAccessListLine(LineAction action, int protocol, Ip srcIp,
         Ip srcWildcard, Ip dstIp, Ip dstWildcard,
         List<SubRange> srcPortRanges, List<SubRange> dstPortRanges) {
      _action = action;
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
      if (dstPortRanges == null
            && (Util.getProtocolName(protocol).equals("tcp") || Util
                  .getProtocolName(protocol).equals("udp"))) {
         dstPortRanges = Collections.singletonList(new SubRange(0, 65535));
      }
   }

   public LineAction getAction() {
      return _action;
   }

   public Ip getDestinationIP() {
      return _dstIp;
   }

   public Ip getDestinationWildcard() {
      return _dstWildcard;
   }

   public List<SubRange> getDstPortRange() {
      return _dstPortRanges;
   }

   public int getProtocol() {
      return _protocol;
   }

   public Ip getSourceIP() {
      return _srcIp;
   }

   public Ip getSourceWildcard() {
      return _srcWildcard;
   }

   public List<SubRange> getSrcPortRanges() {
      return _srcPortRanges;
   }

   @Override
   public String toString() {
      String protocolName = Util.getProtocolName(_protocol);
      return "[Action:"
            + _action
            + ", Protocol:"
            + (protocolName != null ? protocolName + "(" + _protocol + ")"
                  : _protocol) + ", SourceIp:" + _srcIp + ", SourceWildcard:"
            + _srcWildcard + ", DestinationIp:" + _dstIp
            + ", DestinationWildcard:" + _dstWildcard + ", PortRange:"
            + _srcPortRanges + "]";
   }

   /*
    * private LineAction _action; private Ip _dstIp; private List<SubRange>
    * _dstPortRanges; private Ip _dstWildcard; private int _protocol; private Ip
    * _srcIp; private List<SubRange> _srcPortRanges; private Ip _srcWildcard;
    */
   @Override
   public boolean equalsRepresentation(Object o) {
      ExtendedAccessListLine rhs = (ExtendedAccessListLine) o;
      return Util.equalOrNull(_action, rhs._action)
            && Util.equalOrNull(_dstIp, rhs._dstIp)
            && Util.cmpRepresentationLists(_dstPortRanges, rhs._dstPortRanges) == 0
            && Util.equalOrNull(_dstWildcard, rhs._dstWildcard)
            && _protocol == rhs._protocol
            && Util.equalOrNull(_srcIp, rhs._srcIp)
            && Util.cmpRepresentationLists(_srcPortRanges, rhs._srcPortRanges) == 0
            && Util.equalOrNull(_srcWildcard, rhs._srcWildcard);
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string);
         System.out.println("+ " + string + "._action:"
               + Util.objectToString(_action));
         System.out.println("+ " + string + "._dstIp:"
               + Util.objectToString(_dstIp));
         Util.diffRepresentationLists(null, _dstPortRanges, string
               + "._dstPortRanges");
         System.out.println("+ " + string + "._dstWildcard:"
               + Util.objectToString(_dstWildcard));
         System.out.println("+ " + string + "._protocol:" + _protocol);
         System.out.println("+ " + string + "._srcIp:"
               + Util.objectToString(_srcIp));
         Util.diffRepresentationLists(null, _srcPortRanges, string
               + "._srcPortRanges");
         System.out.println("+ " + string + "._srcWildcard:"
               + Util.objectToString(_srcWildcard));
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string);
         System.out.println("- " + string + "._action:"
               + Util.objectToString(_action));
         System.out.println("- " + string + "._dstIp:"
               + Util.objectToString(_dstIp));
         Util.diffRepresentationLists(_dstPortRanges, null, string
               + "._dstPortRanges");

         System.out.println("- " + string + "._dstWildcard:"
               + Util.objectToString(_dstWildcard));
         System.out.println("- " + string + "._protocol:" + _protocol);
         System.out.println("- " + string + "._srcIp:"
               + Util.objectToString(_srcIp));
         Util.diffRepresentationLists(_srcPortRanges, null, string
               + "._srcPortRanges");
         System.out.println("- " + string + "._srcWildcard:"
               + Util.objectToString(_srcWildcard));
         System.out.flush();
         return;
      }

      ExtendedAccessListLine rhs = (ExtendedAccessListLine) o;

      if (!Util.equalOrNull(_action, rhs._action)) {
         System.out.println("- " + string + "._action:"
               + Util.objectToString(_action));
         System.out.println("+ " + string + "._action:"
               + Util.objectToString(rhs._action));
      }
      if (!Util.equalOrNull(_dstIp, rhs._dstIp)) {
         System.out.println("- " + string + "._dstIp:"
               + Util.objectToString(_dstIp));
         System.out.println("+ " + string + "._dstIp:"
               + Util.objectToString(rhs._dstIp));
      }
      Util.diffRepresentationLists(_dstPortRanges, rhs._dstPortRanges, string
            + "._dstPortRanges");
      if (!Util.equalOrNull(_dstWildcard, rhs._dstWildcard)) {
         System.out.println("- " + string + "._dstWildcard:"
               + Util.objectToString(_dstWildcard));
         System.out.println("+ " + string + "._dstWildcard:"
               + Util.objectToString(rhs._dstWildcard));
      }
      if (_protocol != rhs._protocol) {
         System.out.println("- " + string + "._protocol:" + _protocol);
         System.out.println("+ " + string + "._protocol:" + rhs._protocol);
      }
      if (!Util.equalOrNull(_srcIp, rhs._srcIp)) {
         System.out.println("- " + string + "._srcIp:"
               + Util.objectToString(_srcIp));
         System.out.println("+ " + string + "._srcIp:"
               + Util.objectToString(rhs._srcIp));
      }
      Util.diffRepresentationLists(_srcPortRanges, rhs._srcPortRanges, string
            + "._srcPortRanges");
      if (!Util.equalOrNull(_srcWildcard, rhs._srcWildcard)) {
         System.out.println("- " + string + "._srcWildcard:"
               + Util.objectToString(_srcWildcard));
         System.out.println("+ " + string + "._srcWildcard:"
               + Util.objectToString(rhs._srcWildcard));
      }

      System.out.flush();
      return;
   }
}
