package org.batfish.datamodel.questions;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;

import org.batfish.common.BatfishException;
import org.batfish.datamodel.NodeType;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NodesQuestion extends Question {

   private static final String NODE_REGEX_VAR = "nodeRegex";

   private static final String NODE_TYPE_VAR = "nodeType";

   private String _nodeRegex;

   private Set<NodeType> _nodeType;

   public NodesQuestion() {
      super(QuestionType.NODES);
      _nodeType = EnumSet.of(NodeType.ANY);
      _nodeRegex = ".*";
   }

   @Override
   public boolean getDataPlane() {
      return false;
   }

   @Override
   public boolean getDifferential() {
      return false;
   }

   @JsonProperty(NODE_REGEX_VAR)
   public String getNodeRegex() {
      return _nodeRegex;
   }

   @JsonProperty(NODE_TYPE_VAR)
   public Set<NodeType> getNodeTypes() {
      return _nodeType;
   }

   @Override
   public boolean getTraffic() {
      return false;
   }

   @Override
   public void setJsonParameters(JSONObject parameters) {
      super.setJsonParameters(parameters);
      Iterator<?> paramKeys = parameters.keys();
      while (paramKeys.hasNext()) {
         String paramKey = (String) paramKeys.next();
         try {
            switch (paramKey) {
            case NODE_REGEX_VAR:
               setNodeRegex(parameters.getString(paramKey));
               break;
            case NODE_TYPE_VAR:
               setNodeTypes(new ObjectMapper().<Set<NodeType>> readValue(
                     parameters.getString(paramKey),
                     new TypeReference<Set<NodeType>>() {
                     }));
               break;
            default:
               throw new BatfishException("Unknown key in "
                     + getClass().getSimpleName() + ": " + paramKey);
            }
         }
         catch (JSONException | IOException e) {
            throw new BatfishException("JSONException in parameters", e);
         }
      }
   }

   public void setNodeRegex(String regex) {
      _nodeRegex = regex;
   }

   public void setNodeTypes(Set<NodeType> nType) {
      _nodeType = nType;
   }

}
