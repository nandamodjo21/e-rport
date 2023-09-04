package ac.id.poligon.pelanggar.beranda.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import ac.id.poligon.pelanggar.R;
import ac.id.poligon.pelanggar.login.activity.Login;
import ac.id.poligon.pelanggar.report.activity.Lapor;
import ac.id.poligon.pelanggar.servis.SharedPrefManager;


public class Home extends AppCompatActivity {

    private AppCompatButton logout,lapor;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        logout = findViewById(R.id.btnlogout);
        lapor = findViewById(R.id.btnlapor);

        lapor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Lapor.class));
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefManager.getInstance(getApplicationContext()).logout();
                startActivity(new Intent(getApplicationContext(), Login.class));
                Toast.makeText(getApplicationContext(),"logout berhasil!",Toast.LENGTH_SHORT).show();
                finish();
            }
        });


    }
}
