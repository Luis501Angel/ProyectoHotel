package com.dnieln7.thread;

import com.dnieln7.thread.data.Cuarto;
import com.dnieln7.thread.data.mock.CuartosMock;
import com.dnieln7.thread.task.LucesTask;
import com.dnieln7.thread.task.ObtenerCuartoByIdTask;
import com.dnieln7.thread.task.ObtenerCuartosTask;

import javax.swing.*;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    // Método main representa el hilo principal
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        // Clase para simular una base de datos
        CuartosMock mock = new CuartosMock();

        // Tarea para obtener cuartos
        ObtenerCuartosTask dataTask = new ObtenerCuartosTask(mock);
        // La tarea se definira cuando el usuario digite el id del cuarto
        ObtenerCuartoByIdTask dataByIdTask;
        // Se crea una nueva pool de hilos con 2 hilos
        ExecutorService dataService = Executors.newFixedThreadPool(2);

        // La tarea se definira cuando el usuario defina si quiere apagar o encender la luz
        LucesTask lucesTask;
        // Se crea una nueva pool con el numero de cuartos disponibles como hilos
        ExecutorService luzService = Executors.newFixedThreadPool(10);

        CICLO:
        while (true) {

            int opcion = Integer.parseInt(JOptionPane.showInputDialog("Digite una opcion\n1.- Listar Cuartos \n2.- Encender/Apagar luces \n3.- Salir"));

            switch (opcion) {
                case 1:
                    // Se comienza la tarea, utlizando get se espera hasta que esta este completa
                    dataService.submit(dataTask).get().forEach(System.out::println);
                    break;
                case 2:

                    String idCuarto = JOptionPane.showInputDialog("Id del cuarto");
                    int opcionLuces = JOptionPane.showConfirmDialog(
                            null,
                            "¿Encender las luces?",
                            "",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );
                    boolean encender = opcionLuces == 0;

                    // Se Crea la tarea
                    dataByIdTask = new ObtenerCuartoByIdTask(mock, idCuarto);

                    // Se comienza la tarea, utlizando get se espera hasta que esta este completa
                    Cuarto cuarto = dataService.submit(dataByIdTask).get();

                    //Se comienza la tarea en segundo plano para encender las luces
                    lucesTask = new LucesTask(cuarto, encender);
                    // Al no usar get se ejecutara en segundo plano
                    luzService.submit(lucesTask);

                    break;
                case 3:
                    break CICLO;
                default:
                    JOptionPane.showMessageDialog(null, "No válido");
            }
        }
    }
}

