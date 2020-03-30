//Prototipo de un servidor bidireccional

package org.uv.servidor;

/**
 *
 * @author Luis Angel
 */

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Cliente {
    private Socket socket;
    private DataInputStream bufferDeEntrada = null;
    private DataOutputStream bufferDeSalida = null;
    Scanner teclado = new Scanner(System.in);
    final String COMANDO_TERMINACION = "salir()";
    
    public void levantarConexion(String ip, int puerto){
        try{
            socket = new Socket(ip, puerto);
            mostrarTexto("Conectado a: " + socket.getInetAddress().getHostName());
        } catch (Exception e){
            mostrarTexto("Excepcion al levantar conexion: " + e.getMessage());
            System.exit(0);
        }
    }
    
    public static void mostrarTexto(String s){
        System.out.println(s);
    }
    
    public void abrirFlujos(){
        try{
            bufferDeEntrada = new DataInputStream(socket.getInputStream());
            bufferDeSalida = new DataOutputStream(socket.getOutputStream());
            bufferDeSalida.flush();
        } catch (IOException e){
            mostrarTexto("Error en la apertura de flujos");
        }
    }
    
    public void enviar(String s){
        try{
            bufferDeSalida.writeUTF(s);
            bufferDeSalida.flush();
        } catch (IOException e){
            mostrarTexto("IOException on enviar");
        }
    }
    
    public void cerrarConexion(){
        try{
            bufferDeEntrada.close();
            bufferDeSalida.close();
            socket.close();
            mostrarTexto("Conexion terminada");
        } catch (IOException e){
            mostrarTexto("IOException en cerrarConexion()");
        } finally {
            System.exit(0);
        }
    }
    
    public void ejecutarConexion(String ip, int puerto){
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    levantarConexion(ip, puerto);
                    abrirFlujos();
                    recibirDatos();
                } finally {
                    cerrarConexion();
                }
            }
        });
        hilo.start();
    }
    
    public void recibirDatos(){
        String st = "";
        try{
            do{
                st = (String) bufferDeEntrada.readUTF();
                mostrarTexto("\n[Servidor] -> " + st);
                System.out.println("\n[Usted] -> ");
            } while (!st.equals(COMANDO_TERMINACION));
        } catch (IOException e){
            System.out.println("Error al recibir datos ...");
        }
    }
    
    public void escribirDatos(){
        String entrada = "";
        while(true){
            System.out.println("[Usted] -> ");
            entrada = teclado.nextLine();
            if(entrada.length() > 0)
                enviar(entrada);
        }
    }
    
    public static void main(String[] args){
        Cliente cliente = new Cliente();
        Scanner sc = new Scanner(System.in);
        mostrarTexto("Ingresa la IP del servidor: ");
        String ip = sc.nextLine();
        mostrarTexto("Puerto: [5050 por defecto] ");
        String puerto = sc.nextLine();
        if(puerto.length() <= 0)
            puerto = "5050";
        
        cliente.ejecutarConexion(ip, Integer.parseInt(puerto));
        cliente.escribirDatos();
    }
}
