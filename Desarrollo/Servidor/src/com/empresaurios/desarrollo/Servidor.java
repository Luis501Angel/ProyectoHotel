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
    boolean orden;
    String objetivo = "";
    String habitacion = "";
    DataOutputStream bufferDeSalida = null;
    DataInputStream bufferDeEntrada = null;

    public void levantarConexion() {
        try {
            serverSocket = new ServerSocket(1444);

            Socket socketCliente = serverSocket.accept();
            
            ejecutarConexion(socketCliente);

            mostrarTexto("Conexion establecida con el cliente: " + socketCliente.getInetAddress().getHostName() + " en el puerto 1444");
        } catch (Exception e) {
            mostrarTexto("Error al levantar la conexion: " + e.getMessage());
            System.exit(0);
        }
    }

    public void levantarConexionEmulador(String ip) {
        try {
            Socket socketEmulador = new Socket(ip, 1440);

            DataOutputStream salidaEmulador = new DataOutputStream(socketEmulador.getOutputStream());
            
            mostrarTexto("Conectado al emulador: " + socketEmulador.getInetAddress().getHostName() + " por el puerto 1440");
            enviar(salidaEmulador);
            salidaEmulador.flush();
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
                levantarConexionEmulador("26.163.43.171");  //IP
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


    public static void main(String[] args) throws IOException {
        Servidor servidor = new Servidor();
        
        servidor.levantarConexion();
    }
}
