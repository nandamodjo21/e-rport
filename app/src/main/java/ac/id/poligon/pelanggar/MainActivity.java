package ac.id.poligon.pelanggar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import ac.id.poligon.pelanggar.beranda.activity.Home;
import ac.id.poligon.pelanggar.login.activity.Login;
import ac.id.poligon.pelanggar.servis.SharedPrefManager;
//import ac.id.poligon.pelanggaran.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {

        @Override
        public void run(){
            if (SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()) {
                startActivity(new Intent(getApplicationContext(), Home.class));
                finish();
            }
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        }
        }, 2500L);
    }
}