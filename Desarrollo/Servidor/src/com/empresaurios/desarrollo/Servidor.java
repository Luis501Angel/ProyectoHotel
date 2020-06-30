package com.empresaurios.desarrollo;

import java.awt.TextArea;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

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
    String ipCliente = "";

    DataInputStream entradaCliente = null;
    DataOutputStream salidaCliente = null;

    static JFrame igServer;
    static TextArea logTextArea;

    String COMANDO_TERMINACION = "salir()";

    public void levantarConexion() {
        try {
            serverSocket = new ServerSocket(1444);
            agregarTextArea("El servidor se ha iniciado");

            while (true) {
                Socket socketCliente = serverSocket.accept();
                ipCliente = socketCliente.getInetAddress().getHostAddress();

                agregarTextArea("Conexion establecida con el cliente: " + ipCliente);

                ejecutarConexion(socketCliente);
            }
        } catch (IOException e) {
            agregarTextArea("Error al levantar la conexion: " + e.getMessage());
        }
    }

    public void levantarConexionEmulador(String ip) {
        try {
            while (true) {
                Socket socketEmulador = new Socket(ip, 1441);

                DataInputStream entradaEmulador = new DataInputStream(socketEmulador.getInputStream());
                DataOutputStream salidaEmulador = new DataOutputStream(socketEmulador.getOutputStream());

                agregarTextArea("Conectado al emulador: " + socketEmulador.getInetAddress().getHostName() + " por el puerto 1441");

                salidaEmulador.writeBoolean(auto);
                salidaEmulador.writeUTF(objetivo);
                salidaEmulador.writeBoolean(orden);

                estado = entradaEmulador.readBoolean();
                agregarTextArea("Estado recibido del emulador: " + estado);
                salidaEmulador.flush();
                break;
            }
        } catch (Exception e) {
            agregarTextArea("Excepcion al levantar conexion: " + e.getMessage());
        }
    }

    public void recibirDatosCliente(DataInputStream entradaCliente, DataOutputStream salidaCliente) throws IOException {

        try {
            while (true) {
                auto = entradaCliente.readBoolean();
                habitacion = (String) entradaCliente.readUTF();

                switch (habitacion) {
                    case "HB1":
                        levantarConexionEmulador("26.163.43.171");  //IP del emulador de la habitacion 1
                        objetivo = (String) entradaCliente.readUTF();
                        agregarTextArea("El objetivo es: " + objetivo + " en la habitacion: " + habitacion);
                        if (!auto) {
                            orden = entradaCliente.readBoolean();
                        }
                        break;
                    case "HB2":
                        levantarConexionEmulador("26.163.43.171");  //IP del emulador de la habitacion 1
                        objetivo = (String) entradaCliente.readUTF();
                        agregarTextArea("El objetivo es: " + objetivo + " en la habitacion: " + habitacion);
                        if (!auto) {
                            orden = entradaCliente.readBoolean();
                        }
                        break;
                }

                break;
            }
        } catch (IOException e) {
            serverSocket.close();
        }
    }

    public static void agregarTextArea(String mensaje) {
        logTextArea.append(new Date() + "     " + mensaje + "\n");
    }

    public void ejecutarConexion(Socket socketCliente) throws IOException {
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DataInputStream entradaCliente = new DataInputStream(socketCliente.getInputStream());
                    DataOutputStream salidaCliente = new DataOutputStream(socketCliente.getOutputStream());

                    recibirDatosCliente(entradaCliente, salidaCliente);
                    salidaCliente.writeBoolean(estado);
                    agregarTextArea("Estado enviado al cliente: " + estado);
                    socketCliente.close();
                } catch (IOException e) {
                    agregarTextArea(e.getMessage());
                    agregarTextArea("Conexion terminada");

                }
            }
        });

        hilo.start();
    }

    public static void main(String[] args) throws IOException {
        //Frame
        igServer = new JFrame();
        igServer.setTitle("Servidor");
        igServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        igServer.setVisible(true);
        igServer.setBounds(100, 100, 600, 400);

        logTextArea = new TextArea();
        igServer.add(logTextArea);

        //Servidor
        Servidor servidor = new Servidor();
        servidor.levantarConexion();
    }
}
