package id.sch.smktelkom_mlg.project.xiirpl208182838.aplikasipariwisatamalang3;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import model.Hotel;

public class DetailActivity extends AppCompatActivity {
    String url = "http://192.168.160.1//pariwisatamalang.php";
    TextView tvDeskripsi;
    ListView lvtempat;
    private JSONObject jObject;
    private String xResult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Hotel hotel = (Hotel) getIntent().getSerializableExtra(MainActivity.HOTEL);
        setTitle(hotel.judul);
        ImageView ivFoto = (ImageView) findViewById(R.id.imageFoto);
        ivFoto.setImageURI(Uri.parse(hotel.foto));
        TextView tvDeskripsi = (TextView) findViewById(R.id.tvDeskripsi);
        tvDeskripsi.setText(hotel.deskripsi + "\n\n" + hotel.detail);
        TextView tvLokasi = (TextView) findViewById(R.id.tvLokasi);
        tvLokasi.setText(hotel.lokasi);
//        getPariwisatamalang();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

//    void getPariwisatamalang(){
//        RequestQueue requestqueue = Volley.newRequestQueue(this);
//        JsonObjectRequest jsonobjectrequest=new JsonObjectRequest(Request.Method.GET, url,null, new
//                Response.Listener<JSONObject>(){
//                    @Override
//                    public  void onResponse(JSONObject response) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(response.toString());
//                            JSONArray jsonArray = jsonObject.getJSONArray("pariwisatamalang");
//                            String[] tempat = new String[jsonArray.length()];
//                            for(int i=0;i<jsonArray.length();i++){
//                                tempat[i]=jsonArray.getJSONObject(i).getString("tempat");
//                            }
//                            tvDeskripsi.setText(new ArrayAdapter<Object>(getBaseContext(),R.layout.activity_detail, tempat));
//
//                        }catch (JSONException e){
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d(getClass().getName(),error.getMessage());
//            }
//
//                }
//        );
//
//    }
}
