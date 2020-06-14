package com.empresaurios.desarrollo.emulador;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vitom
 */
public class Emulador {

    private boolean luces;
    private boolean aire;
    private boolean cerraduras;
    private String objetivo;

    public Emulador() {
        this.aire = false;
        this.luces = false;
        this.cerraduras = false;
    }

    public void iniciar() {
        try {
            ServerSocket serverSocket = new ServerSocket(1440);
            Logger.getLogger(Emulador.class.getName()).log(Level.INFO, "Emulador iniciado...");
            while (true) {
                Socket socket = null;
                try {
                    socket = serverSocket.accept();
                    Logger.getLogger(Emulador.class.getName()).log(Level.INFO, "Un nuevo cliente esta conectado: " + socket);
                    
                    //Obtencion de las cadenas de entrada y salida
                    DataInputStream autinputServer = new DataInputStream((socket.getInputStream()));
                    DataOutputStream autoutputServer = new DataOutputStream(socket.getOutputStream());
                    boolean auto = autinputServer.readBoolean();
                    objetivo = autinputServer.readUTF();
                    if (auto) {
                        switch (objetivo) {
                            case "Luces":
                                autoutputServer.writeBoolean(luces);
                                break;
                            case "Aire":
                                autoutputServer.writeBoolean(aire);
                                break;
                            case "Cerraduras":
                                autoutputServer.writeBoolean(cerraduras);
                                break;
                            default:
                                break;
                        }
                    } else {
                        DataInputStream ordinputServer = new DataInputStream((socket.getInputStream()));
                        DataOutputStream ordoutputServer = new DataOutputStream(socket.getOutputStream());
                        boolean orden = ordinputServer.readBoolean();
                        switch (objetivo) {
                            case "LUCES":
                                luces = orden;
                                ObtenerEstadoLuces(luces);
                                ordoutputServer.writeBoolean(luces);
                                break;
                            case "AIRE":
                                aire = orden;
                                ObtenerEstadoAire(orden);
                                ordoutputServer.writeBoolean(aire);
                                break;
                            case "CERRADURAS":
                                cerraduras = orden;
                                ObtenerEstadoCerraduras(orden);
                                ordoutputServer.writeBoolean(cerraduras);
                                break;
                            default:
                                break;
                        }
                    }

                } catch (Exception e) {
                    socket.close();
                    e.printStackTrace();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Emulador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void ObtenerEstadoLuces(boolean orden){
        if(orden){
            Logger.getLogger(Emulador.class.getName()).log(Level.INFO, "LAS VELAS MODERNAS SE HAN DESFUNDIDO");
        }
        else{
            Logger.getLogger(Emulador.class.getName()).log(Level.INFO, "LAS VELAS MODERNAS SE HAN FUNDIDO");
        }
    }
    
    public void ObtenerEstadoAire(boolean orden){
        if(orden){
            Logger.getLogger(Emulador.class.getName()).log(Level.INFO, "EL ABANICO MODERNO SE HA DESROMPIDO");
        }
        else{
            Logger.getLogger(Emulador.class.getName()).log(Level.INFO, "EL ABANICO MODERNO SE HA ROMPIDO");
        }
    }
    
    public void ObtenerEstadoCerraduras(boolean orden){
        if(orden){
            Logger.getLogger(Emulador.class.getName()).log(Level.INFO, "LAS CHAPAS MODERNAS SE HAN DESATRANCADO");
        }
        else{
            Logger.getLogger(Emulador.class.getName()).log(Level.INFO, "LAS CHAPAS MODERNAS SE HAN ATRANCADO");
        }
    }

    public static void main(String[] args) {
        Emulador emulador = new Emulador();
        emulador.iniciar();
    }
    
    
}
