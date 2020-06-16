package com.empresaurios.desarrollo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luis Angel
 */
public class Servidor {

    // Objetos del servidor
    private ServerSocket serverSocket;

    // Objetos del cliente
    boolean auto = false;
    boolean estado = false;
    boolean orden = false;
    String objetivo = "";
    String habitacion = "";

    DataInputStream entradaCliente = null;
    DataOutputStream salidaCliente = null;

    public void levantarConexion() {
        try {
            serverSocket = new ServerSocket(1444);
            while (true) {
                Socket socketCliente = serverSocket.accept();
                System.out.println("Conexion establecida con el cliente: " + socketCliente.getInetAddress().getHostName());
                ejecutarConexion(socketCliente);
            }
        } catch (IOException e) {
            mostrarTexto("Error al levantar la conexion: " + e.getMessage());
            System.exit(0);
        }
    }

    public void levantarConexionEmulador(String ip) {
        try {
            Socket socketEmulador = new Socket(ip, 1441);

            DataInputStream entradaEmulador = new DataInputStream(socketEmulador.getInputStream());
            DataOutputStream salidaEmulador = new DataOutputStream(socketEmulador.getOutputStream());

            mostrarTexto("Conectado al emulador: " + socketEmulador.getInetAddress().getHostName() + " por el puerto 1441");

            salidaEmulador.writeBoolean(auto);
            salidaEmulador.writeUTF(objetivo);
            salidaEmulador.writeBoolean(orden);

            estado = entradaEmulador.readBoolean();

        } catch (Exception e) {
            mostrarTexto("Excepcion al levantar conexion: " + e.getMessage());
            System.exit(0);
        }
    }

    public void recibirDatosCliente(DataInputStream entradaCliente, DataOutputStream salidaCliente) throws IOException {

        try {
            auto = entradaCliente.readBoolean();
            habitacion = (String) entradaCliente.readUTF();

            switch (habitacion) {
                case "HB1":
                // SI es HB1 usa ip ...
                // Sies hb2 usa ip2...
            }
            objetivo = (String) entradaCliente.readUTF();

            System.out.println("El objetivo es: " + objetivo);

            if (!auto) {
                orden = entradaCliente.readBoolean();
            }

            levantarConexionEmulador("192.168.1.76");  //IP del emulador
        } catch (IOException e) {
            serverSocket.close();
        }
    }

    public static void mostrarTexto(String mensaje) {
        Logger.getLogger(Servidor.class.getName()).log(Level.INFO, mensaje);
    }

    public void ejecutarConexion(Socket socketCliente) {
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        DataInputStream entradaCliente = new DataInputStream(socketCliente.getInputStream());
                        DataOutputStream salidaCliente = new DataOutputStream(socketCliente.getOutputStream());

                        recibirDatosCliente(entradaCliente, salidaCliente);
                        salidaCliente.writeBoolean(estado);

                        break;
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Conexion terminada");
                    }
                }
            }
        });
        hilo.start();
    }

    public static void main(String[] args) throws IOException {
        Servidor servidor = new Servidor();

        servidor.levantarConexion();
    }
}
