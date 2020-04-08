package com.empresaurios.uv.servidor;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
/**
 *
 * @author Empresaurios
 */
public class ObjetoRemoto extends UnicastRemoteObject implements InterfazRemota {
  
    public ObjetoRemoto( String name ) throws RemoteException {
    try {
      Naming.rebind( name, this );
    }
    catch( Exception e ) {
      System.out.println( e );
    }
  }

  @Override
  public void Saludo(String saludo) {
    System.out.println(saludo);
  }
}