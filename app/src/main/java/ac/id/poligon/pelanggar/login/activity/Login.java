package ac.id.poligon.pelanggar.login.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import ac.id.poligon.pelanggar.R;
import ac.id.poligon.pelanggar.beranda.activity.Home;
import ac.id.poligon.pelanggar.servis.API_SERVER;
import ac.id.poligon.pelanggar.servis.SharedPrefManager;


public class Login extends AppCompatActivity {

    private TextInputLayout t_user,t_pass;

    private AppCompatButton btnlogin;

    private String username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        t_user = findViewById(R.id.textuser);
        t_pass = findViewById(R.id.textPass);

        btnlogin = findViewById(R.id.btn_login);
        TextInputEditText e_user = (TextInputEditText) t_user.getEditText();
        TextInputEditText e_pass = (TextInputEditText) t_pass.getEditText();

if (SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()){
    startActivity(new Intent(getApplicationContext(),Home.class));
    Toast.makeText(getApplicationContext(),"berhasil login",Toast.LENGTH_SHORT).show();
    finish();
}

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = e_user.getText().toString().trim();
                password = e_pass.getText().toString().trim();

                if (username.equals("")){
                    e_user.setError("user harus diisi");
                } else if (password.equals("")) {
                    e_pass.setError("password harus di isi");
                } else {
                    try {

                        loginAccess();
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }

            }
        });

    }

    private void loginAccess() throws JSONException{

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username",username);
        jsonObject.put("password",password);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, API_SERVER.url_login, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject data = response.getJSONObject("data");
                    String pesan = response.getString("message");

                    if (response.getInt("status")== 200){
                        SharedPrefManager.getInstance(getApplicationContext())
                                .session(data.getString("iduser")
                                        ,data.getString("nip")
                                        ,data.getString("guru")
                                        ,data.getString("username")
                                        ,data.getString("role"));
                        Toast.makeText(getApplicationContext(),pesan,Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Home.class));
                    } else {
                        Toast.makeText(getApplicationContext(),pesan,Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),"gagal login! hubungi admin",Toast.LENGTH_SHORT).show();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);requestQueue.add(jsonObjectRequest);
    }
}
