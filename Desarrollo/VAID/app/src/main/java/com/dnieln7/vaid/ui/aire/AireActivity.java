package com.dnieln7.vaid.ui.aire;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.dnieln7.vaid.R;
import com.dnieln7.vaid.ui.luces.LucesActivity;
import com.dnieln7.vaid.utils.Directorio;
import com.dnieln7.vaid.utils.Printer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutionException;

public class AireActivity extends AppCompatActivity {

    private static final String OBJETIVO = "AIRE";

    private boolean aireHb1;
    private boolean aireHb2;

    private CardView hb1;
    private CardView hb2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aire);

        hb1 = findViewById(R.id.aire_1);
        hb2 = findViewById(R.id.aire_2);

        Toolbar toolbar = findViewById(R.id.aire_appbar).findViewById(R.id.toolbar);
        toolbar.setTitle("Aire acondicionado");
        toolbar.setNavigationIcon(R.drawable.ic_less_arrow_left);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    public void interactuarHb1(View view) {
        try {
            aireHb1 = new InteractuarTask(
                    Directorio.IP_SERVIDOR,
                    Directorio.PUERTO_SERVIDOR,
                    "HB1",
                    OBJETIVO,
                    !aireHb1
            ).execute().get();
        }
        catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            Printer.snackBar(view, "Ha ocurrido un error");
            Printer.logError(LucesActivity.class.getName(), e);
        }

        cambiarEstado(hb1, aireHb1);
    }

    public void interactuarHb2(View view) {
        try {
            aireHb2 = new InteractuarTask(
                    Directorio.IP_SERVIDOR,
                    Directorio.PUERTO_SERVIDOR,
                    "HB2",
                    OBJETIVO,
                    !aireHb2
            ).execute().get();
        }
        catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            Printer.snackBar(view, "Ha ocurrido un error");
            Printer.logError(LucesActivity.class.getName(), e);
        }

        cambiarEstado(hb2, aireHb2);
    }

    private static void cambiarEstado(CardView habitacion, boolean orden) {
        if (orden) {
            habitacion.setCardBackgroundColor(Directorio.AIRE_ON);
        }
        else {
            habitacion.setCardBackgroundColor(Directorio.AIRE_OFF);
        }
    }

    static class InteractuarTask extends AsyncTask<Void, Void, Boolean> {

        private final String ip;
        private final int puerto;

        private final String habitacion;
        private final String objetivo;
        private final boolean orden;

        InteractuarTask(String ip, int puerto, String habitacion, String objetivo, boolean orden) {
            this.ip = ip;
            this.puerto = puerto;
            this.habitacion = habitacion;
            this.objetivo = objetivo;
            this.orden = orden;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean estado = false;

            try (Socket cliente = new Socket()) {
                cliente.connect(new InetSocketAddress(ip, puerto), 5000);

                if(cliente.isConnected()) {
                    DataInputStream entrada = new DataInputStream(cliente.getInputStream());
                    DataOutputStream salida = new DataOutputStream(cliente.getOutputStream());

                    salida.writeBoolean(false);
                    salida.writeUTF(habitacion);
                    salida.writeUTF(objetivo);
                    salida.writeBoolean(orden);
                    estado = entrada.readBoolean();
                }
                else {
                    throw new SocketTimeoutException();
                }
            }
            catch (IOException e) {
                Printer.logError(InteractuarTask.class.getName(), e);
            }

            return estado;
        }
    }
}
