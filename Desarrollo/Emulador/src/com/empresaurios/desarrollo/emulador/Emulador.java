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
            ServerSocket serverSocket = new ServerSocket(1441);
            Logger.getLogger(Emulador.class.getName()).log(Level.INFO, "Emulador iniciado...");
            while (true) {
                Socket socket = null;
                try {
                    socket = serverSocket.accept();
                    Logger.getLogger(Emulador.class.getName()).log(Level.INFO, "Conexion establecida con: ", socket.getInetAddress().getHostName());

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
                                ordoutputServer.writeBoolean(luces);
                                ObtenerEstadoLuces(luces);
                                break;
                            case "AIRE":
                                aire = orden;
                                ordoutputServer.writeBoolean(aire);
                                ObtenerEstadoAire(aire);
                                break;
                            case "CERRADURAS":
                                cerraduras = orden;
                                ordoutputServer.writeBoolean(cerraduras);
                                ObtenerEstadoCerraduras(cerraduras);
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

    public void ObtenerEstadoLuces(boolean luces) {
        if (luces) {
            Logger.getLogger(Emulador.class.getName()).log(Level.INFO, "LUCES ENCENDIDAS");
        } else {
            Logger.getLogger(Emulador.class.getName()).log(Level.INFO, "LUCES APAGADAS");
        }
    }

    public void ObtenerEstadoAire(boolean aire) {
        if (aire) {
            Logger.getLogger(Emulador.class.getName()).log(Level.INFO, "AIRE ACONDICIONADO ACTIVADO");
        } else {
            Logger.getLogger(Emulador.class.getName()).log(Level.INFO, "AIRE ACONDICIONADO DESACTIVADO");
        }
    }

    public void ObtenerEstadoCerraduras(boolean cerraduras) {
        if (cerraduras) {
            Logger.getLogger(Emulador.class.getName()).log(Level.INFO, "CERRADURAS BLOQUEADAS");
        } else {
            Logger.getLogger(Emulador.class.getName()).log(Level.INFO, "CERRADURAS DESBLOQUEADAS");
        }
    }

    public static void main(String[] args) {
        Emulador emulador = new Emulador();
        emulador.iniciar();
    }

}
