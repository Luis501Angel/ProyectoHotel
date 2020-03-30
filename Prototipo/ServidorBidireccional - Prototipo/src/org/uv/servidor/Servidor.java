//Protipo de un servidor bidireccional
package org.uv.servidor;

/**
 *
 * @author Luis Angel
 */

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Servidor {
    
    private Socket socket;
    private ServerSocket serverSocket;
    private DataInputStream bufferDeEntrada = null;
    private DataOutputStream bufferDeSalida = null;
    Scanner teclado = new Scanner(System.in);
    final String COMANDO_TERMINACION = "salir()";
    
    public void levantarConexion(int puerto){
        try{
            serverSocket = new ServerSocket(puerto);
            mostrarTexto("Esperando conexion entrante en el puerto " + String.valueOf(puerto) + " ...");
            socket = serverSocket.accept();
            mostrarTexto("Conexion establecida con: " + socket.getInetAddress().getHostName() + "\n\n\n");
        } catch(Exception e){
            mostrarTexto("Error en levantarConexion(): " + e.getMessage());
            System.exit(0);
        }
    }
    public void flujos(){
        try{
            bufferDeEntrada = new DataInputStream(socket.getInputStream());
            bufferDeSalida = new DataOutputStream(socket.getOutputStream());
            bufferDeSalida.flush();
        } catch (Exception e){
            mostrarTexto("Error en la apertura de flujos");
        }
    }
    
    public void recibirDatos(){
        String st = "";
        try{
            do{
                st = (String) bufferDeEntrada.readUTF();
                mostrarTexto("\n[Cliente] -> " + st);
                System.out.println("\n[Usted] -> ");
            } while(!st.equals(COMANDO_TERMINACION));
        } catch (IOException e){
            cerrarConexion();
        }
    }
    
    public void enviar(String s){
        try{
            bufferDeSalida.writeUTF(s);
            bufferDeSalida.flush();
        } catch (IOException e){
            mostrarTexto("Error en enviar(): " + e.getMessage());
        }
    }  
    
    public static void mostrarTexto(String s){
        System.out.println(s);
    }
    
    public void escribirDatos(){
        while(true){
            System.out.println("[Usted] -> ");
            enviar(teclado.nextLine());
        }
    }
    
    public void cerrarConexion(){
        try{
            bufferDeEntrada.close();
            bufferDeSalida.close();
            socket.close();
        } catch (IOException e){
            mostrarTexto("Excepcion en cerrarConexion(): " + e.getMessage());
        } finally {
            mostrarTexto("Conversacion finalizada...");
            System.exit(0);
        }
    }
    
    public void ejecutarConexion(int puerto){
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try{
                        levantarConexion(puerto);
                        flujos();
                        recibirDatos();
                    } finally {
                        cerrarConexion();
                    }
                }
            }
        });
        hilo.start();
    }
    
    public static void main(String[] args) throws IOException {
        Servidor servidor = new Servidor();
        Scanner sc = new Scanner(System.in);
        
        mostrarTexto("Ingresa el puerto [5050 por defecto]: ");
        String puerto = sc.nextLine();
        
        if(puerto.length() <= 0) 
            puerto = "5050";
        servidor.ejecutarConexion(Integer.parseInt(puerto));
        servidor.escribirDatos();
    }
}
