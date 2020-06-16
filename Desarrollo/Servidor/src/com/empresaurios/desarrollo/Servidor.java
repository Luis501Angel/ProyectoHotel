package com.empresaurios.desarrollo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Luis Angel
 */
public class Servidor {

    private Socket socket;
    private ServerSocket serverSocket;
    Scanner teclado = new Scanner(System.in);
    final String COMANDO_TERMINACION = "salir()";
    boolean auto;
    boolean estado;
    boolean orden;
    String objetivo = "";
    String habitacion = "";
    DataOutputStream bufferDeSalida = null;
    DataInputStream bufferDeEntrada = null;

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

    public void levantarConexionCliente(String ip, int puerto) {
        try {
            Socket socketEmulador = new Socket("192.168.1.68", 1444);
            DataOutputStream salidaEmulador = new DataOutputStream(socketEmulador.getOutputStream());
            DataInputStream entradaEmulador = new DataInputStream(socketEmulador.getInputStream());
            estado = entradaEmulador.readBoolean();
            System.out.println("Estado de las luces para el cliente: " + estado);
            enviarEstado(salidaEmulador);
            salidaEmulador.flush();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarTexto("Excepcion al levantar conexion: " + e.getMessage());
            System.exit(0);
        }
    }

    public void levantarConexionEmulador(String ip) {
        try {
            Socket socketCliente = new Socket(ip, 1441);

            DataOutputStream salidaCliente = new DataOutputStream(socketCliente.getOutputStream());
            DataInputStream entradaEmulador = new DataInputStream(socketCliente.getInputStream());

            mostrarTexto("Conectado al emulador: " + socketCliente.getInetAddress().getHostName() + " por el puerto 1441");
            enviar(salidaCliente);
            estado = entradaEmulador.readBoolean();
            System.out.println("Estado recibido por el emulador: " + estado);
            levantarConexionCliente("192.168.1.69", 1444); //IP del cliente
            salidaCliente.flush();
        } catch (Exception e) {
            mostrarTexto("Excepcion al levantar conexion: " + e.getMessage());
            System.exit(0);
        }
    }

    public void recibirDatos(DataInputStream entradaCliente, DataOutputStream salidaCliente) throws IOException {

        try {
            do {
                auto = entradaCliente.readBoolean();
                habitacion = (String) entradaCliente.readUTF();
                switch (habitacion) {
                    case "HB1":
                }
                objetivo = (String) entradaCliente.readUTF();
                System.out.println("El objetivo es: " + objetivo);
                if (auto) {
                    orden = entradaCliente.readBoolean();
                }
                levantarConexionEmulador("192.168.1.76");  //IP del emulador
                break;
            } while (!objetivo.equals(COMANDO_TERMINACION));
        } catch (IOException e) {
            socket.close();
            serverSocket.close();
        }
    }

    public void enviar(DataOutputStream bufferDeSalida) {
        try {
            bufferDeSalida.writeBoolean(auto);
            bufferDeSalida.writeUTF(objetivo);
            bufferDeSalida.writeBoolean(orden);
            bufferDeSalida.flush();
        } catch (IOException e) {
            mostrarTexto("Error al enviar: " + e.getMessage());
        }
    }

    public void enviarEstado(DataOutputStream bufferDeSalida) {
        try {
            bufferDeSalida.writeBoolean(estado);
            bufferDeSalida.flush();
        } catch (IOException e) {
            mostrarTexto("Error al enviar: " + e.getMessage());
        }
    }

    public static void mostrarTexto(String st) {
        System.out.println(st);
    }

    public void ejecutarConexion(Socket socketCliente) {
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        DataInputStream entradaCliente = new DataInputStream(socketCliente.getInputStream());
                        DataOutputStream salidaCliente = new DataOutputStream(socketCliente.getOutputStream());
                        recibirDatos(entradaCliente, salidaCliente);
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
