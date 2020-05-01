package com.empresaurios.prototipoconexiones;

/**
 *
 * @author Luis Angel
 */

import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;

public class Server {
    
    public static void main(String[] args) throws IOException { 
        //Se crea un sevidor en el puerto 5056
        ServerSocket serverSocket = new ServerSocket(5056);  
        
        //Se crea un loop infinito para obterner la respuesta del cliente
        while(true){
            Socket s = null;  
            try {
                
                // El objeto socket se prepara para recibir respuestas de clientes
                s = serverSocket.accept();
                System.out.println("Un nuevo cliente esta conectado: " + s);
                
                //Obtencion de las cadenas de entrada y salida
                DataInputStream dis = new DataInputStream((s.getInputStream()));
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                
                System.out.println("Asignando un hilo nuevo para este cliente");
                
                //Se crea un nuevo hilo
                Thread t = new ClientHandler(s, dis, dos);
                
                //Se invoca el metodo para iniciar el hilo
                t.start();
            } catch (Exception e){
                s.close();
                e.printStackTrace();
            }
        }
    }
}
    
    //Clase ClienteHandler
    class ClientHandler extends Thread{
        DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
        final DataInputStream dis;
        final DataOutputStream dos;
        final Socket s;
        
        //Constructor
        public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos){
            this.s = s;
            this.dis = dis;
            this.dos = dos;
        }
        
        @Override
        public void run(){
            String received;
            String toreturn;
            while(true){
                try{
                    //Le pregunta al usuario que desea hacer
                    dos.writeUTF("¿Que desea ver? [Date | Time]...\n" +
                                 "Escriba Exit para terminar la conexion");
                    
                    //Recibe la respuesta del cliente
                    received = dis.readUTF();
                    
                    if(received.equals("Exit")){
                        System.out.println("Cliente " + this.s + " quiere salir...");
                        System.out.println("Cerrando su conexion...");
                        this.s.close();
                        System.out.println("Conexion cerrada");
                        break;
                    }
                    //Creando el objeto Date
                    Date date = new Date();
                    
                    //Muestra el formato de salida dependiendo de lo que escogio el Cliente
                    switch(received){
                        case "Date":
                            toreturn = fordate.format(date);
                            dos.writeUTF(toreturn);
                            break;
                        case "Time":
                            toreturn = fortime.format(date);
                            dos.writeUTF(toreturn);
                            break;
                        default:
                            dos.writeUTF("Entrada invalida");
                            break;
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
            
            try{
                //Cerrando los recursos
                this.dis.close();
                this.dos.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
