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

            Socket socketCliente = serverSocket.accept();
            Socket socketEmulador = serverSocket.accept();
            
            ejecutarConexion(socketCliente);
            ejecutarConexionEmulador(socketEmulador);
        } catch (Exception e) {
            mostrarTexto("Error al levantar la conexion: " + e.getMessage());
            System.exit(0);
        }
    }

    public void levantarConexionEmulador(String ip) {
        try {
            Socket socketEmulador = new Socket(ip, 1441);

            DataOutputStream salidaEmulador = new DataOutputStream(socketEmulador.getOutputStream());
            DataInputStream entradaEmulador = new DataInputStream(socketEmulador.getInputStream());
            
            boolean estado = entradaEmulador.readBoolean();
            enviarEstado(estado);
            salidaEmulador.flush();
        } catch (Exception e) {
            mostrarTexto("Excepcion al levantar conexion: " + e.getMessage());
            System.exit(0);
        }
    }
    
    public void levantarConexionCliente(String ip) {
        try {
            Socket socketCliente = new Socket(ip, 1444);

            DataOutputStream salidaCliente = new DataOutputStream(socketCliente.getOutputStream());
            
            mostrarTexto("Conectado al cliente: " + socketCliente.getInetAddress().getHostName() + " por el puerto 1440");
            enviar(salidaCliente);
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
                
                switch(habitacion) {
                    case "HB1":
                        
                }
                
                objetivo = (String) entradaCliente.readUTF();
                if (auto) {
                    orden = entradaCliente.readBoolean();
                }
                levantarConexionEmulador("localhost");  //IP del emulador
                System.out.println(objetivo);
            } while (!objetivo.equals(COMANDO_TERMINACION));
        } catch (IOException e) {
            socket.close();
            serverSocket.close();
        }
    }
    
     public void recibirDatosEmulador(DataInputStream entradaEmulador, DataOutputStream salidaEmulador) throws IOException {
        try {
            do {
                estado = entradaEmulador.readBoolean();
                levantarConexionEmulador("localhost");  //IP del cliente
                System.out.println(objetivo);
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
    
    public void enviarEstado(boolean estado) {
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
                CICLO:
                while (true) {
                    try {
                        
                        DataInputStream entradaCliente = new DataInputStream(socketCliente.getInputStream());
                        DataOutputStream salidaCliente = new DataOutputStream(socketCliente.getOutputStream());
                        
                        recibirDatos(entradaCliente, salidaCliente);
                        break CICLO;
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println("Conexion terminada");
                    }
                }
            }
        });
        hilo.start();
    }
    
    public void ejecutarConexionEmulador(Socket socketEmulador) {
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                CICLO:
                while (true) {
                    try {
                        
                        DataInputStream entradaEmulador = new DataInputStream(socketEmulador.getInputStream());
                        DataOutputStream salidaEmulador = new DataOutputStream(socketEmulador.getOutputStream());
                        
                        recibirDatosEmulador(entradaEmulador, salidaEmulador);
                        break CICLO;
                    } catch (Exception e) {
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
