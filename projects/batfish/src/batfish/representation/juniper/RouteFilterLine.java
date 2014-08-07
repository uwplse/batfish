package batfish.representation.juniper;

import batfish.representation.RepresentationObject;

/**
 * 
 * A data structure used in RouteFilter to store prefix and prefix-length
 * 
 */

public abstract class RouteFilterLine implements RepresentationObject {
   public abstract RouteFilterLineType getType();

}
