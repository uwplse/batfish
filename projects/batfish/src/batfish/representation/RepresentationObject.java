package batfish.representation;

public interface RepresentationObject {

   boolean equalsRepresentation(Object o);
   
   void diffRepresentation(Object o, String string, boolean reverse);

}
