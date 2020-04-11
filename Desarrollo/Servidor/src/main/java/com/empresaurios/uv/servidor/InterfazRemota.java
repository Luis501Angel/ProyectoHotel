package com.empresaurios.uv.servidor;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfazRemota extends Remote {

    /**
     * Envia un saludo.
     * @return Retorna el saludo
     * @throws RemoteException
     */
    public void Saludo(String saludo) throws RemoteException;

    /**
     * Cambia el estado booleano del cuarto 1
     */
    public void interactuar_cuarto1() throws RemoteException;

    /**
     * Cambia el estado booleano del cuarto 2
     */
    public void interactuar_cuarto2() throws RemoteException;

    /**
     * Cambia el estado booleano del cuarto 2
     */
    public void interactuar_cuarto3() throws RemoteException;

    /**
     * Devuelve el estado de todos los cuartos
     * @return Retorna un arreglo de booleanos, en donde cada espacio en el arreglo corresponde a un cuarto
     */
    public Boolean[] monitorear_luces() throws RemoteException;
} 