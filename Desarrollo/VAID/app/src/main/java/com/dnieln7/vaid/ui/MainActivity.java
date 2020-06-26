package com.dnieln7.vaid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dnieln7.vaid.R;
import com.dnieln7.vaid.utils.Printer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.main_appbar).findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
    }

    public void luces(View view) {
        startActivity(new Intent(this, LucesActivity.class));
    }

    public void cerradura(View view) {
        Printer.toast(this, "Cerraduras");
    }

    public void aire(View view) {
        Printer.toast(this, "Aire");
    }

    public void about(View view) {
        startActivity(new Intent(this, AboutActivity.class));
    }
}
