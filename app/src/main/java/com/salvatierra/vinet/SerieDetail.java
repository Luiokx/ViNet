package com.salvatierra.vinet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.salvatierra.vinet.model.CategoryItem;
import com.salvatierra.vinet.model.DataManager;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SerieDetail extends AppCompatActivity {
    private CategoryItem serieItem;
    private ArrayAdapter cap;
    private ArrayList<String> capitulos;
    private DataManager dbHelper = new DataManager(SerieDetail.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serie_detail);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        loadLayout();

        setSpinner();

        setChapters();

        //calculateListViewHeight();

        setListListener();
    }

    private void setListListener(){
        ((ListView) findViewById(R.id.list_series_chapter)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String nombre = (serieItem.getMovieName()).replace(" ", "%20") + "/";
                String temporada = "Temporada%20" +  (((Spinner) findViewById(R.id.spinner_temporada)).getSelectedItemPosition() + 1) + "/";
                Intent window = new Intent(SerieDetail.this, VideoPlayerActivity.class);
                window.putExtra("url", "http://92.187.160.50:4200/" + nombre + temporada + (position + 1) + "." + serieItem.getExtension());
                startActivity(window);
            }
        });
    }

    private void calculateListViewHeight() {
        ViewGroup.LayoutParams params = ((ListView) findViewById(R.id.list_series_chapter)).getLayoutParams();
        params.height = capitulos.size() * ((ListView) findViewById(R.id.list_series_chapter)).getDividerHeight() * 56;
        ((ListView) findViewById(R.id.list_series_chapter)).setLayoutParams(params);
    }

    private void setChapters() {
        capitulos = dbHelper.getCapitulos(serieItem.getId(), (((Spinner) findViewById(R.id.spinner_temporada)).getSelectedItemPosition() + 1), serieItem, getString(R.string.chaper));

        cap = new ArrayAdapter<String>(SerieDetail.this,  R.layout.chapter_layout,R.id.chapter_cap_number, capitulos);

        ((ListView) findViewById(R.id.list_series_chapter)).setAdapter(cap);
    }

    private void setSpinner() {
        String[] temporadas = getTemporadas();

        ArrayAdapter<String> adapterTrash = new ArrayAdapter<String>(this,
                R.layout.spinner_temporadas_layou, temporadas);
        adapterTrash.setDropDownViewResource(R.layout.spinner_temporadas_layou);

        ((Spinner) findViewById(R.id.spinner_temporada)).setAdapter(adapterTrash);

        ((Spinner) findViewById(R.id.spinner_temporada)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //setChaptersChange();
                capitulos.clear();
                capitulos = dbHelper.getCapitulos(serieItem.getId(), (((Spinner) findViewById(R.id.spinner_temporada)).getSelectedItemPosition() + 1), serieItem, getString(R.string.chaper));
                cap.clear();
                cap.addAll(capitulos);
                cap.notifyDataSetChanged();
                calculateListViewHeight();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private String[] getTemporadas() {
        String response[] = new String[serieItem.getTemporadas()];

        for (int i = 0; i < serieItem.getTemporadas(); i++){
            response[i] = "Temporada " + (i + 1) + "  ▼";
        }

        return response;
    }

    private void loadLayout(){
        serieItem = dbHelper.getDataSerie(getIntent().getIntExtra("idObject",0));

        setImage();

        setDescr();
    }

    private void setDescr(){
        ((TextView) findViewById(R.id.serie_name)).setText(serieItem.getMovieName());
        ((TextView) findViewById(R.id.description_serie)).setText(serieItem.getDescription());
    }

    private void setImage(){
        URL url;
        try {
            url = new URL("http://92.187.160.50:4200/" + serieItem.getMovieName() + "/logo.jpg");
            URLConnection urlc = url.openConnection();
            InputStream is = urlc.getInputStream();
            BufferedInputStream binput = new BufferedInputStream(is);
            ((ImageView) findViewById(R.id.serie_image)).setImageBitmap(BitmapFactory.decodeStream(binput));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}