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
    private DataInputStream bufferDeEntrada = null;
    private DataOutputStream bufferDeSalida = null;
    Scanner teclado = new Scanner(System.in);
    final String COMANDO_TERMINACION = "salir()";
    
    public void levantarConexion(){
        try{
            serverSocket = new ServerSocket(1445);
            mostrarTexto("Esperando conexion en el puerto 1444...");
            socket = serverSocket.accept();
            mostrarTexto("Conexion establecida con: " + socket.getInetAddress().getHostName());
        } catch(Exception e){
            mostrarTexto("Error al levantar la conexion: " + e.getMessage());
            System.exit(0);
        }
    }
    
    public void levantarConexionEmulador(String ip){
        try{
            socket = new Socket(ip, 1444);
            mostrarTexto("Conectado al emulador: " + socket.getInetAddress().getHostName());
        } catch (Exception e){
            mostrarTexto("Excepcion al levantar conexion: " + e.getMessage());
            System.exit(0);
        }
    }
    
    public void flujos(){
        try{
            bufferDeEntrada = new DataInputStream(socket.getInputStream());
            bufferDeSalida = new DataOutputStream(socket.getOutputStream());
            bufferDeSalida.flush();
        } catch(Exception e){
            mostrarTexto("Error en la apertura de flujos");
        }
    }
    
    public void recibirDatos(){
        String aparato = "";
        try{
            do{
                aparato = (String)bufferDeEntrada.readUTF();
                mostrarTexto("Componente: " + aparato);
            } while(!aparato.equals(COMANDO_TERMINACION));
        } catch(IOException e){
            cerrarConexion();
        }
    }
    
    public void enviar(String aparato){
        try{
            bufferDeSalida.writeUTF(aparato);
            bufferDeSalida.flush();
        } catch(IOException e){
            mostrarTexto("Error al enviar: " + e.getMessage());
        }
    }
    
    public static void mostrarTexto(String st){
        System.out.println(st);
    }
    
    public void escribirDatos(){
        String entrada = "";
        while(true){
            System.out.println("Enviando componente...");
            entrada = teclado.nextLine();
            if(entrada.length() > 0)
                enviar(entrada);
        }
    }
    
    public void cerrarConexion(){
        try{
            bufferDeEntrada.close();
            bufferDeSalida.close();
            socket.close();
        } catch (IOException e){
            mostrarTexto("Exception al cerrar la conexion: " + e.getMessage());
        } finally {
            mostrarTexto("Sesion finalizada");
            System.exit(0);
        }
    }
    
    public void ejecutarConexion(){
        Thread hilo = new Thread(new Runnable(){
            @Override
            public void run(){
                while(true){
                    try{
                        levantarConexion();
                        flujos();
                        recibirDatos();
                    } finally{
                        cerrarConexion();
                    }
                }
            }
        });
        hilo.start();
    }
    
     public void ejecutarConexionEmulador(){
        Thread hilo = new Thread(new Runnable(){
            @Override
            public void run(){
                while(true){
                    try{
                        levantarConexionEmulador("192.168.1.76");
                        flujos();
                        escribirDatos();
                    } finally{
                        cerrarConexion();
                    }
                }
            }
        });
        hilo.start();
    }
    
    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        
        servidor.ejecutarConexion();
        servidor.ejecutarConexionEmulador();
    }
}
