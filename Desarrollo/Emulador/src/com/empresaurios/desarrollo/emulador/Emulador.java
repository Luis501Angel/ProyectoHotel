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
                                System.out.println("Las foquitos se han modificado");
                                ordoutputServer.writeBoolean(luces);
                                break;
                            case "AIRE":
                                aire = orden;
                                System.out.println("Las chiflones se han modificado");
                                ordoutputServer.writeBoolean(aire);
                                break;
                            case "CERRADURAS":
                                cerraduras = orden;
                                System.out.println("Las chapas se han modificado");
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

    public static void main(String[] args) {
        Emulador emulador = new Emulador();
        emulador.iniciar();
    }
}
