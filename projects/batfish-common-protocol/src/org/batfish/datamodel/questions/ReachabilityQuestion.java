package org.batfish.datamodel.questions;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.batfish.common.BatfishException;
import org.batfish.datamodel.ForwardingAction;
import org.batfish.datamodel.IcmpCode;
import org.batfish.datamodel.IcmpType;
import org.batfish.datamodel.Prefix;
import org.batfish.datamodel.SubRange;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ReachabilityQuestion extends Question {

   private static final String ACTIONS_VAR = "actions";

   private static final String DST_PORT_RANGE_VAR = "dstPortRange";

   private static final String DST_PREFIXES_VAR = "dstPrefixes";

   private static final String FINAL_NODE_REGEX_VAR = "finalNodeRegex";

   private static final String ICMP_CODE_VAR = "icmpCode";

   private static final String ICMP_TYPE_VAR = "icmpType";

   private static final String INGRESS_NODE_REGEX_VAR = "ingressNodeRegex";

   private static final String IP_PROTO_RANGE_VAR = "ipProtoRange";

   private static final String SRC_PORT_RANGE_VAR = "srcPortRange";

   private static final String SRC_PREFIXES_VAR = "srcPrefixes";

   private Set<ForwardingAction> _actions;

   private Set<SubRange> _dstPortRange;

   private Set<Prefix> _dstPrefixes;

   private String _finalNodeRegex;

   private int _icmpCode;

   private int _icmpType;

   private String _ingressNodeRegex;

   private Set<SubRange> _ipProtocolRange;

   private Set<SubRange> _srcPortRange;

   private Set<Prefix> _srcPrefixes;

   // private int _tcpFlags;

   public ReachabilityQuestion() {
      super(QuestionType.REACHABILITY);
      _actions = EnumSet.of(ForwardingAction.ACCEPT);
      _dstPortRange = new TreeSet<SubRange>();
      _dstPrefixes = new TreeSet<Prefix>();
      _finalNodeRegex = ".*";
      _icmpCode = IcmpCode.UNSET;
      _icmpType = IcmpType.UNSET;
      _ingressNodeRegex = ".*";
      _ipProtocolRange = new TreeSet<SubRange>();
      _srcPortRange = new TreeSet<SubRange>();
      _srcPrefixes = new TreeSet<Prefix>();
   }

   @JsonProperty(ACTIONS_VAR)
   public Set<ForwardingAction> getActions() {
      return _actions;
   }

   @Override
   public boolean getDataPlane() {
      return true;
   }

   @Override
   public boolean getDifferential() {
      return false;
   }

   @JsonProperty(DST_PORT_RANGE_VAR)
   public Set<SubRange> getDstPortRange() {
      return _dstPortRange;
   }

   @JsonProperty(DST_PREFIXES_VAR)
   public Set<Prefix> getDstPrefixes() {
      return _dstPrefixes;
   }

   @JsonProperty(FINAL_NODE_REGEX_VAR)
   public String getFinalNodeRegex() {
      return _finalNodeRegex;
   }

   @JsonProperty(ICMP_CODE_VAR)
   public int getIcmpCode() {
      return _icmpCode;
   }

   @JsonProperty(ICMP_TYPE_VAR)
   public int getIcmpType() {
      return _icmpType;
   }

   @JsonProperty(INGRESS_NODE_REGEX_VAR)
   public String getIngressNodeRegex() {
      return _ingressNodeRegex;
   }

   @JsonProperty(IP_PROTO_RANGE_VAR)
   public Set<SubRange> getIpProtocolRange() {
      return _ipProtocolRange;
   }

   @JsonProperty(SRC_PORT_RANGE_VAR)
   public Set<SubRange> getSrcPortRange() {
      return _srcPortRange;
   }

   @JsonProperty(SRC_PREFIXES_VAR)
   public Set<Prefix> getSrcPrefixes() {
      return _srcPrefixes;
   }

   @Override
   public boolean getTraffic() {
      return true;
   }

   public void setActions(Set<ForwardingAction> actionSet) {
      _actions = actionSet;
   }

   public void setDstPortRange(Set<SubRange> rangeSet) {
      _dstPortRange = rangeSet;
   }

   public void setDstPrefixes(Set<Prefix> prefixSet) {
      _dstPrefixes = prefixSet;
   }

   public void setFinalNodeRegex(String regex) {
      _finalNodeRegex = regex;
   }

   public void setIcmpCode(int icmpCode) {
      _icmpCode = icmpCode;
   }

   public void setIcmpType(int icmpType) {
      _icmpType = icmpType;
   }

   public void setIngressNodeRegex(String regex) {
      _ingressNodeRegex = regex;
   }

   public void setIpProtocolRange(Set<SubRange> rangeSet) {
      _ipProtocolRange = rangeSet;
   }

   @Override
   public void setJsonParameters(JSONObject parameters) {
      super.setJsonParameters(parameters);

      Iterator<?> paramKeys = parameters.keys();

      while (paramKeys.hasNext()) {
         String paramKey = (String) paramKeys.next();

         try {
            switch (paramKey) {
            case ACTIONS_VAR:
               setActions(new ObjectMapper().<Set<ForwardingAction>> readValue(
                     parameters.getString(paramKey),
                     new TypeReference<Set<ForwardingAction>>() {
                     }));
               break;
            case DST_PORT_RANGE_VAR:
               setDstPortRange(new ObjectMapper().<Set<SubRange>> readValue(
                     parameters.getString(paramKey),
                     new TypeReference<Set<SubRange>>() {
                     }));
               break;
            case DST_PREFIXES_VAR:
               setDstPrefixes(new ObjectMapper().<Set<Prefix>> readValue(
                     parameters.getString(paramKey),
                     new TypeReference<Set<Prefix>>() {
                     }));
               break;
            case FINAL_NODE_REGEX_VAR:
               setFinalNodeRegex(parameters.getString(paramKey));
               break;
            case ICMP_CODE_VAR:
               setIcmpCode(parameters.getInt(paramKey));
               break;
            case ICMP_TYPE_VAR:
               setIcmpType(parameters.getInt(paramKey));
               break;
            case INGRESS_NODE_REGEX_VAR:
               setIngressNodeRegex(parameters.getString(paramKey));
               break;
            case IP_PROTO_RANGE_VAR:
               setIpProtocolRange(new ObjectMapper().<Set<SubRange>> readValue(
                     parameters.getString(paramKey),
                     new TypeReference<Set<SubRange>>() {
                     }));
               break;
            case SRC_PORT_RANGE_VAR:
               setSrcPortRange(new ObjectMapper().<Set<SubRange>> readValue(
                     parameters.getString(paramKey),
                     new TypeReference<Set<SubRange>>() {
                     }));
               break;
            case SRC_PREFIXES_VAR:
               setSrcPrefixes(new ObjectMapper().<Set<Prefix>> readValue(
                     parameters.getString(paramKey),
                     new TypeReference<Set<Prefix>>() {
                     }));
               break;
            default:
               throw new BatfishException(
                     "Unknown key in ReachabilityQuestion: " + paramKey);
            }
         }
         catch (JSONException | IOException e) {
            throw new BatfishException("JSONException in parameters", e);
         }
      }
   }

   public void setSrcPortRange(Set<SubRange> rangeSet) {
      _srcPortRange = rangeSet;
   }

   public void setSrcPrefixes(Set<Prefix> prefixSet) {
      _srcPrefixes = prefixSet;
   }

}
