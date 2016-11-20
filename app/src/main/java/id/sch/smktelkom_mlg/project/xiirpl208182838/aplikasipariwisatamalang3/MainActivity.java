package id.sch.smktelkom_mlg.project.xiirpl208182838.aplikasipariwisatamalang3;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import adapter.HotelAdapter;
import model.Hotel;

public class MainActivity extends AppCompatActivity implements HotelAdapter.IHotelAdapter {
    public static final String HOTEL = "hotel";
    public static final int REQUEST_CODE_ADD = 88;
    private static final String TAG_TEMPAT = "tempat";
    private static final String TAG_ID = "id_tempat";
    private static final String TAG_TEMPAT2 = "tempat";
    private static final String TAG_DESKRIPSI = "deskrispi";
    private static final String TAG_LOKASI = "lokasi";
    ArrayList<Hotel> mList = new ArrayList<>();
    ArrayList<Hotel> mListAll = new ArrayList<>();
    boolean isFiltered;
    ArrayList<Integer> mListMapFilter = new ArrayList<>();
    String mQuery;
    HotelAdapter mAdapter;
    private koneksi myJSON;
    private JSONObject jURL;
    private JSONArray jArray;

    private HashMap<String, String> hash;
    private ListAdapter adapter;
    private ArrayList<HashMap<String, String>> tempat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new HotelAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);

        fillData();

        //ganti localhost (http://localhost:XXXX/hallodunia.php menjadi) ip address yang diperoleh dari ipconfig cmd
        String url = "http://192.168.160.1//contoh.php";

// mendefiniskan permintaan koneksi
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // hasil permintaan
                        System.out.println(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // bila terjadi error
                System.out.println("Something went wrong!");
                error.printStackTrace();

            }
        });

// Pemintaan koneksi dilaksakan
        Volley.newRequestQueue(this).add(stringRequest);

        myJSON = new koneksi();
        jURL = myJSON.getJSONFromUrl(url);
        tempat = new ArrayList<HashMap<String, String>>();
        try {
            jArray = jURL.getJSONArray(TAG_TEMPAT);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject job = jArray.getJSONObject(i);
                String id = job.getString("id_tempat");
                String tempat2 = job.getString("tempat");
                String deskripsi = job.getString("deskrisi");
                String lokasi = job.getString("lokasi");

                hash = new HashMap<String, String>();
                hash.put(TAG_ID, id);
                hash.put(TAG_TEMPAT2, tempat2);
                hash.put(TAG_DESKRIPSI, deskripsi);
                hash.put(TAG_LOKASI, lokasi);


                tempat.add(hash);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            setList();
        }
    }


    private void setList() {
        adapter = new SimpleAdapter(this, tempat, R.layout.activity_main,
                new String[]{TAG_ID, TAG_TEMPAT2, TAG_DESKRIPSI, TAG_LOKASI}, new int[]{
                R.id.fab});
        setList(adapter);
    }


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goAdd();
            }
        });*/

    private void goAdd() {
        startActivityForResult(new Intent(this, InputActivity.class), REQUEST_CODE_ADD);
    }

//    private void goShare()
//    {
//        startActivityForResult(new Intent(this, DetailActivity.class), );
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD && resultCode == RESULT_OK) {
            Hotel hotel = (Hotel) data.getSerializableExtra(HOTEL);
            mList.add(hotel);
            mAdapter.notifyDataSetChanged();
        }
    }
    private void fillData() {
        Resources resources = getResources();
        String[] arJudul = resources.getStringArray(R.array.places);
        String[] arDeskripsi = resources.getStringArray(R.array.place_desc);
        String[] arDetail = resources.getStringArray(R.array.place_details);
        String[] arLokasi = resources.getStringArray(R.array.place_locations);
        TypedArray a = resources.obtainTypedArray(R.array.places_picture);
        String[] arFoto = new String[a.length()];
        for (int i = 0; i < arFoto.length; i++) {
            int id = a.getResourceId(i, 0);
            arFoto[i] = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                    + resources.getResourcePackageName(id) + '/'
                    + resources.getResourceTypeName(id) + '/'
                    + resources.getResourceEntryName(id);
        }
        a.recycle();

        for (int i = 0; i < arJudul.length; i++) {
            mList.add(new Hotel(arJudul[i], arDeskripsi[i], arDetail[i], arLokasi[i], arFoto[i]));
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mQuery = newText.toLowerCase();
                doFilter(mQuery);
                return true;
            }
        });
        return true;
    }

    private void doFilter(String mQuery) {

        if (!isFiltered) {
            mListAll.clear();
            mListAll.addAll(mList);
            isFiltered = true;
        }
        mList.clear();
        if (mQuery == null || mQuery.isEmpty()) {
            mList.addAll(mListAll);
            isFiltered = false;
        } else {
            mListMapFilter.clear();
            for (int i = 0; i < mListAll.size(); i++) {
                Hotel hotel = mListAll.get(i);
                if (hotel.judul.toLowerCase().contains(mQuery) ||
                        hotel.deskripsi.toLowerCase().contains(mQuery) ||
                        hotel.lokasi.toLowerCase().contains(mQuery)) {
                    mList.add(hotel);
                    mListMapFilter.add(i);
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void doClick(int pos) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(HOTEL, mList.get(pos));
        startActivity(intent);

    }

    @Override
    public void doEdit(int pos) {

    }

    @Override
    public void doDelete(int pos) {

    }

    @Override
    public void doFav(int pos) {

    }

    @Override
    public void doShare(int pos)
    {
//        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//        sharingIntent.setType("text/plain");
//        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "AndroidSolved");
//        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Now Learn Android with AndroidSolved clicke here to visit https://androidsolved.wordpress.com/ ");
//        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

}
