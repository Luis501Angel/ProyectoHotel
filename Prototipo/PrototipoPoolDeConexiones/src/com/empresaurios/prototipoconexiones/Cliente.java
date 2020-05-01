package com.empresaurios.prototipoconexiones;

/**
 *
 * @author Luis Angel
 */

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {
    
    public static void main(String[] args) throws IOException{
        try{
            Scanner scn = new Scanner(System.in);
            
            //Se ingresa la ip del servidor
            InetAddress ip = InetAddress.getByName("localhost");
            
            //Se establece la conexion con el servidor en el puerto 5056
            Socket s = new Socket(ip, 5056);
            
            //Se obtiene los datos de entrada y salida
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            
            //Se crea un loop para intercambiar informacion entre el cliente y el manejador del cliente
            while(true){
                System.out.println(dis.readUTF());
                String toSend = scn.nextLine();
                dos.writeUTF(toSend);
                
                //Si el cliente envia Exit, se cierra la conexion y se rompe el loop
                if(toSend.equals("Exit")){
                    System.out.println("Cerrando esta conexion: " + s);
                    s.close();
                    System.out.println("Conexion cerrada");
                    break;
                }
                
                //Se imprime el date o time para el cliente
                String received = dis.readUTF();
                System.out.println(received);
            }
            
            //Cerrando los recuros
            scn.close();
            dis.close();
            dos.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
}
