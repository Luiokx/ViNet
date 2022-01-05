package com.salvatierra.vinet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class MovieDetail extends AppCompatActivity {
    private CategoryItem movieItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        loadLayout();

        ((Button) findViewById(R.id.play_button_movie)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://92.187.160.50:4200/" + (movieItem.getMovieName()).replace(" ", "%20") + "/1." + movieItem.getExtension();
                Intent window = new Intent(MovieDetail.this, VideoPlayerActivity.class);
                window.putExtra("url", url);
                startActivity(window);
            }
        });
    }

    private void loadLayout(){
        DataManager dbHelper = new DataManager(MovieDetail.this);
        movieItem = dbHelper.getDataMovie(getIntent().getIntExtra("idObject",0));

        setImage();

        setDescr();
    }

    private void setDescr(){
        ((TextView) findViewById(R.id.movie_name)).setText(movieItem.getMovieName());
        ((TextView) findViewById(R.id.description_movie)).setText(movieItem.getDescription());

    }

    private void setImage(){
        URL url;
        try {
            url = new URL("http://92.187.160.50:4200/" + movieItem.getMovieName() + "/logo.jpg");
            URLConnection urlc = url.openConnection();
            InputStream is = urlc.getInputStream();
            BufferedInputStream binput = new BufferedInputStream(is);
            ((ImageView) findViewById(R.id.movie_image)).setImageBitmap(BitmapFactory.decodeStream(binput));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}