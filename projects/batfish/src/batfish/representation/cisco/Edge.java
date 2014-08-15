package batfish.representation.cisco;

import java.io.Serializable;

import batfish.representation.RepresentationObject;
import batfish.util.Util;

public class Edge implements Serializable, RepresentationObject {

   private static final long serialVersionUID = 1L;

   private String _host1;
   private String _host2;
   private String _int1;
   private String _int2;

   public Edge(String host1, String int1, String host2, String int2) {
      _host1 = host1;
      _host2 = host2;
      _int1 = int1;
      _int2 = int2;
   }

   public String getHost1() {
      return _host1;
   }

   public String getHost2() {
      return _host2;
   }

   public String getInt1() {
      return _int1;
   }

   public String getInt2() {
      return _int2;
   }

   @Override
   public boolean equalsRepresentation(Object o) {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public void diffRepresentation(Object o, String string, boolean reverse) {
      if (reverse) {
         System.out.println("+ " + string + "\n");
         System.out.println("+ " + string + "._host1:" + Util.objectToString(_host1) + "\n");
         System.out.println("+ " + string + "._host2:" + Util.objectToString(_host2) + "\n");
         System.out.println("+ " + string + "._int1:" + Util.objectToString(_int1) + "\n");
         System.out.println("+ " + string + "._int2:" + Util.objectToString(_int2) + "\n");
         System.out.flush();
         return;
      }

      if (o == null) {
         System.out.println("- " + string + "\n");
         System.out.println("- " + string + "._host1:" + Util.objectToString(_host1) + "\n");
         System.out.println("- " + string + "._host2:" + Util.objectToString(_host2) + "\n");
         System.out.println("- " + string + "._int1:" + Util.objectToString(_int1) + "\n");
         System.out.println("- " + string + "._int2:" + Util.objectToString(_int2) + "\n");
         System.out.flush();
         return;
      }

      Edge rhs = (Edge) o;
      if (!Util.equalOrNull(_host1, rhs._host1)) {
         System.out.println("- " + string + "._host1:" + Util.objectToString(_host1) + "\n");
         System.out.println("+ " + string + "._host1:" + Util.objectToString(rhs._host1) + "\n");
      }
      if (!Util.equalOrNull(_host2, rhs._host2)) {
         System.out.println("- " + string + "._host2:" + Util.objectToString(_host2) + "\n");
         System.out.println("+ " + string + "._host2:" + Util.objectToString(rhs._host2) + "\n");
      }
      if (!Util.equalOrNull(_int1, rhs._int1)) {
         System.out.println("- " + string + "._int1:" + Util.objectToString(_int1) + "\n");
         System.out.println("+ " + string + "._int1:" + Util.objectToString(rhs._int1) + "\n");
      }
      if (!Util.equalOrNull(_int2, rhs._int2)) {
         System.out.println("- " + string + "._int2:" + Util.objectToString(_int2) + "\n");
         System.out.println("+ " + string + "._int2:" + Util.objectToString(rhs._int2) + "\n");
      }
      
      System.out.flush();
      return;
   }

}
