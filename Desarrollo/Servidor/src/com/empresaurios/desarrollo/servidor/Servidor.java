package com.empresaurios.desarrollo.servidor;

import java.awt.TextArea;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import javax.swing.JFrame;

/**
 *
 * @author Luis Angel
 */
public class Servidor {

    // Objetos del servidor
    private ServerSocket serverSocket;

    // Objetos del cliente
    private boolean auto = false;
    private boolean estado = false;
    private boolean orden = false;

    private String objetivoCliente = "";
    private String habitacionCliente = "";
    private String ipCliente = "";

    // Directorio
    private static final String HB1_IP = "26.45.17.136";
    private static final String HB2_IP = "26.45.17.136";
    private static final int HB1_PUERTO = 1440;
    private static final int HB2_PUERTO = 1441;

    // GUI
    private static JFrame serverFrame;
    private static TextArea logTextArea;
    private static final String COMANDO_TERMINACION = "salir()";

    public void levantarConexion() {
        try {
            serverSocket = new ServerSocket(1444);
            agregarTextArea("El servidor se ha iniciado");

            while (true) {
                Socket socketCliente = serverSocket.accept();
                ipCliente = socketCliente.getInetAddress().getHostAddress();

                agregarTextArea("Iniciando Servidor...");
                agregarTextArea("...Servidor Iniciado...");

                agregarTextArea("Conexion establecida con el cliente: " + ipCliente);
                recibirCliente(socketCliente);
            }
        } catch (IOException error) {
            agregarTextArea("Error al levantar la conexion");
            agregarTextArea(error.getMessage());
        }
    }

    public void interactuarEmulador(String ip, int puerto) {
        try (Socket socketEmulador = new Socket(ip, puerto)) {
            DataInputStream entradaEmulador = new DataInputStream(socketEmulador.getInputStream());
            DataOutputStream salidaEmulador = new DataOutputStream(socketEmulador.getOutputStream());

            agregarTextArea("Conectado al emulador: " + socketEmulador.getInetAddress().getHostName());

            salidaEmulador.writeBoolean(false);
            salidaEmulador.writeUTF(objetivoCliente);
            salidaEmulador.writeBoolean(orden);

            estado = entradaEmulador.readBoolean();

            agregarTextArea("Estado recibido del emulador: " + estado);

            salidaEmulador.flush();
        } catch (IOException e) {
            agregarTextArea("Excepcion al levantar conexion: " + e.getMessage());
        }
    }

    public void escucharEmulador(String ip, int puerto) {
        try (Socket socketEmulador = new Socket(ip, puerto)) {
            DataInputStream entradaEmulador = new DataInputStream(socketEmulador.getInputStream());
            DataOutputStream salidaEmulador = new DataOutputStream(socketEmulador.getOutputStream());

            agregarTextArea("Conectado al emulador: " + socketEmulador.getInetAddress().getHostName());

            salidaEmulador.writeBoolean(true);
            salidaEmulador.writeUTF(objetivoCliente);

            estado = entradaEmulador.readBoolean();

            agregarTextArea("Estado recibido del emulador: " + estado);

            salidaEmulador.flush();
        } catch (IOException e) {
            agregarTextArea("Excepcion al levantar conexion: " + e.getMessage());
        }
    }

    public void recibirDatosCliente(DataInputStream entradaCliente, DataOutputStream salidaCliente) throws IOException {
        auto = entradaCliente.readBoolean();
        habitacionCliente = entradaCliente.readUTF();
        objetivoCliente = entradaCliente.readUTF();

        if (auto) {
            agregarTextArea("Modo automatico... Habitación: " + habitacionCliente);

            if (habitacionCliente.equals("HB1")) {
                escucharEmulador(HB1_IP, HB1_PUERTO);
            } else if (habitacionCliente.equals("HB2")) {
                escucharEmulador(HB2_IP, HB2_PUERTO);
            }

        } else {
            agregarTextArea("El objetivo es: " + objetivoCliente + " en la habitacion: " + habitacionCliente);

            orden = entradaCliente.readBoolean();

            if (habitacionCliente.equals("HB1")) {
                interactuarEmulador(HB1_IP, HB1_PUERTO);
            } else if (habitacionCliente.equals("HB2")) {
                interactuarEmulador(HB2_IP, HB2_PUERTO);
            }
        }
    }

    public static void agregarTextArea(String mensaje) {
        logTextArea.append(new Date() + "     " + mensaje + "\n");
    }

    public void recibirCliente(Socket socketCliente) {
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
                } catch (IOException error) {
                    agregarTextArea("ha ocurrido un error");
                    agregarTextArea(error.getMessage());
                    agregarTextArea("Conexion terminada");
                }
            }
        });

        hilo.start();
    }

    public static void main(String[] args) throws IOException {
        //Frame
        serverFrame = new JFrame();
        serverFrame.setTitle("Servidor");
        serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        serverFrame.setVisible(true);
        serverFrame.setBounds(100, 100, 600, 400);

        logTextArea = new TextArea();
        serverFrame.add(logTextArea);

        //Servidor
        Servidor servidor = new Servidor();
        servidor.levantarConexion();
    }
}
