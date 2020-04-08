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
} 