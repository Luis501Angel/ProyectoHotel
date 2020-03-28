package prototipo;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
/**
 *
 * @author vitom
 */
public class ObjetoRemoto extends UnicastRemoteObject implements IterfazRemota {
    public ObjetoRemoto( String name ) throws RemoteException {
    try {
      Naming.rebind( name, this );
    }
    catch( Exception e ) {
      System.out.println( e );
    }
  }

  @Override
  public void Saludo() {
    System.out.println("Conexion Establecida");
  }
}
