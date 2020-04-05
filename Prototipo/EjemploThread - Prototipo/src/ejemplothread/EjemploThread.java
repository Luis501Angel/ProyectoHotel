package ejemplothread;

/**
 *
 * @author Luis Angel
 */
import java.time.Duration;
import java.time.Instant;

public class EjemploThread {

    private static final Instant INICIO = Instant.now();

    public static void main(String[] args) {

        Runnable tarea = () -> {
            try {
                Log("Empieza la tarea");
                Thread.sleep(5000);
                Log("Termina la tarea");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Thread hilo = new Thread(tarea);

        hilo.start();

        try {
            Log("Se empieza a esperar el hilo");
            hilo.join(3000);
            Log("Se termina de esperar al hilo");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void Log(Object mensaje) {
        System.out.println(String.format("%s [%s] %s",
                Duration.between(INICIO, Instant.now()), Thread.currentThread().getName(), mensaje.toString()));
    }
}

/* ThreadRunnable Output
	PT0.062S [main] Se empieza a esperar al hilo
	PT0.062S [Thread-0] Empieza la tarea
	PT3.125S [main] Se termina de esperar al hilo
	PT5.128S [Thread-0] Termina la tarea
*/
