package prototipo;
import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 *
 * @author vitom
 */
public interface IterfazRemota  extends Remote {
  void Saludo() throws RemoteException;    
}
