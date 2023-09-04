package ac.id.poligon.pelanggar.report.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import ac.id.poligon.pelanggar.R;
import ac.id.poligon.pelanggar.beranda.activity.Home;
import ac.id.poligon.pelanggar.servis.API_SERVER;
import ac.id.poligon.pelanggar.servis.SharedPrefManager;

//import ac.id.poligon.pelanggaran.R;

public class Lapor extends AppCompatActivity {

    private int mYear, mMonth, mDay;
    private Spinner spinnerJenisPelanggaran,nispelanggar;
    private List<String> jenisPelanggaranList = new ArrayList<>();

    private HashMap<String, String> nisSiswaMap = new HashMap<>();

    private HashMap<String, String> jenisMap = new HashMap<>();


    private List<String> nisPelanggaran = new ArrayList<>();
    private EditText ket;

    private AppCompatButton btnTanggal,btnlapor;

    private String nis,jenis,tgl,keterangan,nisn,jenisMp;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lapor);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        ket = findViewById(R.id.teksketerangan);
        final EditText editTextTanggal = findViewById(R.id.tanggal);
        btnTanggal = findViewById(R.id.btntanggal);
        btnlapor = findViewById(R.id.btnlapor);

        spinnerJenisPelanggaran = findViewById(R.id.jenis);
        editTextTanggal.setEnabled(true);
        nispelanggar = findViewById(R.id.nis);

        System.out.println(nispelanggar);

        btnlapor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nis = nispelanggar.getSelectedItem().toString().trim();
                 nisn = nisSiswaMap.get(nis);
                jenis = spinnerJenisPelanggaran.getSelectedItem().toString().trim();
                 jenisMp = jenisMap.get(jenis);
                tgl = editTextTanggal.getText().toString().trim();
                keterangan = ket.getText().toString().trim();

                if (keterangan.equals("")){
                    ket.setError("harus diisi");
                } else if (tgl.equals("")) {
                    editTextTanggal.setError("harus di isi");
                } else {

                    try {
                        Post();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        });



        nispelanggar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nis = nispelanggar.getSelectedItem().toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ambil tanggal saat ini sebagai tanggal default
                final Calendar calendar = Calendar.getInstance();
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);

                // Tampilkan DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(Lapor.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Simpan tanggal yang dipilih
                                mYear = year;
                                mMonth = monthOfYear;
                                mDay = dayOfMonth;

                                // Format tanggal yang dipilih dan set ke EditText
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                                calendar.set(mYear, mMonth, mDay);
                                editTextTanggal.setText(sdf.format(calendar.getTime()));
                            }
                        }, mYear, mMonth, mDay);

                datePickerDialog.show();
            }
        });



        StringRequest stringRequest = new StringRequest(Request.Method.GET, API_SERVER.url_nis, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response JSON", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i <jsonArray.length(); i++){
                        JSONObject js = jsonArray.getJSONObject(i);

                        String nis = js.getString("nis");
                        String nama = js.getString("nama_siswa");
                        nisPelanggaran.add(nama);

                        nisSiswaMap.put(nama,nis);
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(Lapor.this, R.layout.spinner_item_layout, nisPelanggaran);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    adapter.notifyDataSetChanged();
                    nispelanggar.setAdapter(adapter);
                    Log.d("Data NIS", nisPelanggaran.toString());


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();

            }
        });
        requestQueue.add(stringRequest);


        StringRequest str = new StringRequest(Request.Method.GET, API_SERVER.url_jenis, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i <jsonArray.length(); i++){
                        JSONObject js = jsonArray.getJSONObject(i);

                        String id_jenis = String.valueOf(js.getInt("id_jenis_pelanggaran"));
                        String jp = js.getString("jenis_pelanggaran");
                        jenisPelanggaranList.add(jp);

                        jenisMap.put(jp,id_jenis);
                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Lapor.this, R.layout.spinner_item_layout, jenisPelanggaranList);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spinnerJenisPelanggaran.setAdapter(arrayAdapter);
                    arrayAdapter.notifyDataSetChanged();
                    Log.d("Data JEnis", jenisPelanggaranList.toString());


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();

            }
        });
        requestQueue.add(str);

    }

    private void Post() throws JSONException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("nis",nisn);
        jsonObject.put("iduser", SharedPrefManager.getInstance(getApplicationContext()).getKeyId());
        jsonObject.put("jenis",jenisMp);
        jsonObject.put("tanggal",tgl);
        jsonObject.put("keterangan",keterangan);

        System.out.println(jsonObject);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, API_SERVER.url_lapor, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response.getBoolean("status")){
                        Toast.makeText(getApplicationContext(),response.getString("message"),Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),"gagal",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);requestQueue.add(jsonObjectRequest);



    }

}