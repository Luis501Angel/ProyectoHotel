package prototipo;
import java.rmi.*;
import java.rmi.registry.*;
/**
 *
 * @author vitom
 */
public class Cliente {
    public static void main(String[] args) {
    try {
      IterfazRemota InterR = (IterfazRemota)Naming.lookup("rmi://localhost/EJEMPLO");
      System.out.println("Estableciendo conexcion entre Cliente y Servidor...");
      InterR.Saludo();
    }
    catch( Exception e ) {
      System.out.println( e );
    }
  }
}
