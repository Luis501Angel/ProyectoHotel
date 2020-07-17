package com.empresaurios.desarrollo.emulador;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kronoz
 */
public class Emulador {

    private boolean luces;
    private boolean aire;
    private boolean cerraduras;

    public Emulador() {
        this.aire = false;
        this.luces = false;
        this.cerraduras = false;
    }

    public void iniciar() {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingresar puerto: ");
        int puerto = scanner.nextInt();

        try {
            Logger.getLogger(Emulador.class.getName()).log(Level.INFO, "Iniciando Emulador...");

            ServerSocket serverSocket = new ServerSocket(puerto);

            Logger.getLogger(Emulador.class.getName()).log(Level.INFO, "...Emulador Iniciado...");
            Logger.getLogger(Emulador.class.getName()).log(Level.INFO, "Puerto: {0}", puerto);

            while (true) {
                Socket clienteSocket = null;

                try {
                    //Conexion con el servidor
                    clienteSocket = serverSocket.accept();

                    String ipServidor = clienteSocket.getInetAddress().getHostAddress();
                    Logger.getLogger(Emulador.class.getName()).log(Level.INFO, "Conexion establecida con: ", ipServidor);

                    //Obtencion de las cadenas de entrada y salida del servidor
                    DataInputStream autinputServer = new DataInputStream((clienteSocket.getInputStream()));
                    DataOutputStream autoutputServer = new DataOutputStream(clienteSocket.getOutputStream());

                    boolean auto = autinputServer.readBoolean();
                    String objetivo = autinputServer.readUTF();

                    //Detección de modo auto/objetivo
                    if (auto) {
                        switch (objetivo) {
                            case "LUCES":
                                autoutputServer.writeBoolean(luces);
                                ObtenerEstadoLuces(luces);
                                break;
                            case "AIRE":
                                autoutputServer.writeBoolean(aire);
                                ObtenerEstadoAire(aire);
                                break;
                            case "CERRADURAS":
                                autoutputServer.writeBoolean(cerraduras);
                                ObtenerEstadoCerraduras(cerraduras);
                                break;
                            default:
                                break;
                        }
                    } else {
                        DataInputStream ordinputServer = new DataInputStream((clienteSocket.getInputStream()));
                        DataOutputStream ordoutputServer = new DataOutputStream(clienteSocket.getOutputStream());

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
                } catch (IOException e) {
                    Logger.getLogger(Emulador.class.getName()).log(Level.SEVERE, null, e);
                    clienteSocket.close();
                }
            }
        } catch (IOException e) {
            Logger.getLogger(Emulador.class.getName()).log(Level.SEVERE, null, e);
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
            Logger.getLogger(Emulador.class.getName()).log(Level.INFO, "CERRADURAS ABIERTAS");
        } else {
            Logger.getLogger(Emulador.class.getName()).log(Level.INFO, "CERRADURAS CERRADAS");
        }
    }

    public static void main(String[] args) {
        Emulador emulador = new Emulador();
        emulador.iniciar();
    }
}
