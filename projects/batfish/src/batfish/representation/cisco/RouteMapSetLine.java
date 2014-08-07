package batfish.representation.cisco;

import java.io.Serializable;

import batfish.representation.Configuration;
import batfish.representation.PolicyMapSetLine;
import batfish.representation.RepresentationObject;

public abstract class RouteMapSetLine implements Serializable,
      RepresentationObject {

   private static final long serialVersionUID = 1L;

   public abstract PolicyMapSetLine toPolicyMapSetLine(Configuration c);

   public abstract RouteMapSetType getType();

}
