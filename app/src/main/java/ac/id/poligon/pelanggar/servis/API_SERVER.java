package ac.id.poligon.pelanggar.servis;

import android.annotation.SuppressLint;
import android.content.Context;

public class API_SERVER {
    @SuppressLint("StaticFieldLeak")
    public static Context mct;

    public static final String root_url = "http://d126-182-1-137-183.ngrok-free.app/pelanggaran/api/auth";
    public static final String url_login = root_url + "/index";
    public static final String url_lapor = root_url + "/lapor";
    public static final String url_jenis = root_url + "/jenis";
    public static final String url_nis = root_url + "/siswa";
}
