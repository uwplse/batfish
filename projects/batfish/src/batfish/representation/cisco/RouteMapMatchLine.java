package batfish.representation.cisco;

import java.io.Serializable;

import batfish.representation.RepresentationObject;

public abstract class RouteMapMatchLine implements Serializable,
      RepresentationObject {

   private static final long serialVersionUID = 1L;

   public abstract RouteMapMatchType getType();

}
