package com.dnieln7.thread.task;

import com.dnieln7.thread.data.Cuarto;

public class LucesTask implements Runnable {

    private Cuarto cuarto;
    private boolean encender;

    public LucesTask(Cuarto cuarto, boolean encender) {
        this.cuarto = cuarto;
        this.encender = encender;
    }

    @Override
    public void run() {
        cuarto.getLuces().keySet().forEach(luz -> {
            cuarto.getLuces().replace(luz, encender);
            System.out.println("Cuarto: " + cuarto.getId()
                    + " en el piso: " + cuarto.getPiso()
                    + " Luz: " + luz
                    + " ha cambiado a " + (cuarto.getLuces().get(luz) ? "Encendida" : "Apagada"));
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignored) {
            }
        });
    }
}
