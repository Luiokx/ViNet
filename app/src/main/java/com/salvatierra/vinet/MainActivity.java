 package com.salvatierra.vinet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import android.content.Intent;
import android.os.Bundle;

import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import java.sql.*;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import com.salvatierra.vinet.adapter.BannerHomePageAdapter;
import com.salvatierra.vinet.adapter.MainRecyclerAdapter;
import com.salvatierra.vinet.model.AllCategory;
import com.salvatierra.vinet.model.BannerMoviews;
import com.salvatierra.vinet.model.CategoryItem;


import java.util.ArrayList;
import java.util.List;


 public class MainActivity extends AppCompatActivity {
    private thread thread;
    private List<BannerMoviews> bannerSerieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        ArrayList<AllCategory> seriesCategory = new ArrayList<>();
        ArrayList<AllCategory> moviesCategory = new ArrayList<>();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                prepareToGetContent(seriesCategory, moviesCategory);
            }
        });

        prepareBannerContent(seriesCategory, moviesCategory);
    }

    private void prepareBannerContent(ArrayList<AllCategory> seriesCategory, ArrayList<AllCategory> moviesCategory){
        bannerSerieList = new ArrayList<>();
        bannerSerieList.add(new BannerMoviews(1, "A silent voice", "http://92.187.160.50:4200/A silent voice/logo.jpg",""));
        bannerSerieList.add(new BannerMoviews(1, "Absolute Duo", "http://92.187.160.50:4200/Absolute Duo/logo.jpg",""));

        setBannerMoviesPageAdapter(bannerSerieList);

        List<BannerMoviews> bannerMoviesList = new ArrayList<>();
        bannerMoviesList.add(new BannerMoviews(1, "Air TV", "http://92.187.160.50:4200/Air TV/logo.jpg",""));
        bannerMoviesList.add(new BannerMoviews(1, "Angel Beats", "http://92.187.160.50:4200/Angel Beats/logo.jpg",""));

        ((TabLayout) findViewById(R.id.tabLayout)).addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:

                        setScrollToDefaultState();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setMainRecyclerAdapter(seriesCategory);
                            }
                        });

                        setBannerMoviesPageAdapter(bannerSerieList);

                        return;
                    case 1:

                        setScrollToDefaultState();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setMainRecyclerAdapter(moviesCategory);
                            }
                        });


                        setBannerMoviesPageAdapter(bannerMoviesList);

                        return;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void prepareToGetContent(ArrayList<AllCategory> seriesCategory, ArrayList<AllCategory> moviesCategory){
        try {
            // Clase de conexion con el driver
            Class.forName("com.mysql.jdbc.Driver");

            Connection conexion = DriverManager.getConnection("jdbc:mysql://92.187.160.50:3306/anime", "animes", "vivaelbetis");

            Statement consulta = conexion.createStatement();

            prepareMediaContent(consulta, seriesCategory, true, "serie");

            prepareMediaContent(consulta, moviesCategory, false, "pelicula");

            consulta.close();
            conexion.close();

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

     private void prepareMediaContent(Statement consulta, ArrayList<AllCategory> itemCategory, boolean id, String type) {

        List<CategoryItem> Accion = new ArrayList<>();
        List<CategoryItem> Aventura = new ArrayList<>();
        List<CategoryItem> Ficcion = new ArrayList<>();
        List<CategoryItem> Comedia = new ArrayList<>();
        List<CategoryItem> Drama = new ArrayList<>();
        List<CategoryItem> Escolares = new ArrayList<>();
        List<CategoryItem> Ecchi = new ArrayList<>();
        List<CategoryItem> Harem = new ArrayList<>();
        List<CategoryItem> Isekai = new ArrayList<>();
        List<CategoryItem> Magia = new ArrayList<>();
        List<CategoryItem> Romance = new ArrayList<>();
        List<CategoryItem> Shounen = new ArrayList<>();

         String sql = "SELECT * FROM " + type;

         int resta = 2;
         if (type.equals("serie")){
            resta = 0;
         }

         try {
             ResultSet resultado = consulta.executeQuery(sql);

            while (resultado.next()){
                setCategorySeries(itemCategory, Accion, Aventura, Ficcion, Comedia, Drama, Escolares, Ecchi, Harem, Isekai, Magia,Romance, Shounen,resultado, type, resta);
            }

             resultado.close();

         } catch (SQLException throwables) {
             throwables.printStackTrace();
         }

         setRecyclerSeries(itemCategory, Accion, "Accion");
         setRecyclerSeries(itemCategory, Aventura, "Aventura");

         setRecyclerSeries(itemCategory, Comedia, "Comedia");
         setRecyclerSeries(itemCategory, Drama, "Drama");
         setRecyclerSeries(itemCategory, Escolares, "Escolares");
         setRecyclerSeries(itemCategory, Ecchi, "Ecchi");
         setRecyclerSeries(itemCategory, Ficcion, "Ficcion");
         setRecyclerSeries(itemCategory, Harem, "Harem");
         setRecyclerSeries(itemCategory, Isekai, "Isekai");
         setRecyclerSeries(itemCategory, Magia, "Magia");
         setRecyclerSeries(itemCategory, Romance, "Romance");
         setRecyclerSeries(itemCategory, Shounen, "Shounen");

         if (id) {
             setMainRecyclerAdapter(itemCategory);
         }

     }

     private void setCategorySeries(List<AllCategory> seriesCategory, List<CategoryItem> accion, List<CategoryItem> aventura, List<CategoryItem> ficcion, List<CategoryItem> comedia, List<CategoryItem> drama, List<CategoryItem> escolares, List<CategoryItem> ecchi, List<CategoryItem> harem, List<CategoryItem> isekai, List<CategoryItem> magia, List<CategoryItem> romance, List<CategoryItem> shounen, ResultSet resultado, String type, int resta) {
        try {
            if (resultado.getInt(5 - resta) == 1){
                accion.add(new CategoryItem(resultado.getInt(1), resultado.getString(2), "http://92.187.160.50:4200/" + resultado.getString(2) + "/logo.jpg", ""));
                setType(accion, type);
            }

            if (resultado.getInt(6 - resta) == 1){
                aventura.add(new CategoryItem(resultado.getInt(1), resultado.getString(2), "http://92.187.160.50:4200/" + resultado.getString(2) + "/logo.jpg", ""));
                setType(aventura, type);
            }

            if (resultado.getInt(7 - resta) == 1){
                comedia.add(new CategoryItem(resultado.getInt(1), resultado.getString(2), "http://92.187.160.50:4200/" + resultado.getString(2) + "/logo.jpg", ""));
                setType(comedia, type);
            }

            if (resultado.getInt(8 - resta) == 1){
                drama.add(new CategoryItem(resultado.getInt(1), resultado.getString(2), "http://92.187.160.50:4200/" + resultado.getString(2) + "/logo.jpg", ""));
                setType(drama, type);
            }

            if (resultado.getInt(9 - resta) == 1){
                escolares.add(new CategoryItem(resultado.getInt(1), resultado.getString(2), "http://92.187.160.50:4200/" + resultado.getString(2) + "/logo.jpg", ""));
                setType(escolares, type);
            }

            if (resultado.getInt(10 - resta) == 1){
                ecchi.add(new CategoryItem(resultado.getInt(1), resultado.getString(2), "http://92.187.160.50:4200/" + resultado.getString(2) + "/logo.jpg", ""));
                setType(ecchi, type);
            }

            if (resultado.getInt(11 - resta) == 1){
                ficcion.add(new CategoryItem(resultado.getInt(1), resultado.getString(2), "http://92.187.160.50:4200/" + resultado.getString(2) + "/logo.jpg", ""));
                setType(ficcion, type);
            }

            if (resultado.getInt(12 - resta) == 1){
                harem.add(new CategoryItem(resultado.getInt(1), resultado.getString(2), "http://92.187.160.50:4200/" + resultado.getString(2) + "/logo.jpg", ""));
                setType(harem, type);
            }

            if (resultado.getInt(13 - resta) == 1){
                isekai.add(new CategoryItem(resultado.getInt(1), resultado.getString(2), "http://92.187.160.50:4200/" + resultado.getString(2) + "/logo.jpg", ""));
                setType(isekai, type);
            }

            if (resultado.getInt(14 - resta) == 1){
                magia.add(new CategoryItem(resultado.getInt(1), resultado.getString(2), "http://92.187.160.50:4200/" + resultado.getString(2) + "/logo.jpg", ""));
                setType(magia, type);
            }

            if (resultado.getInt(15 - resta) == 1){
                romance.add(new CategoryItem(resultado.getInt(1), resultado.getString(2), "http://92.187.160.50:4200/" + resultado.getString(2) + "/logo.jpg", ""));
                setType(romance, type);
            }

            if (resultado.getInt(16 - resta) == 1){
                shounen.add(new CategoryItem(resultado.getInt(1), resultado.getString(2), "http://92.187.160.50:4200/" + resultado.getString(2) + "/logo.jpg", ""));
                setType(shounen, type);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

     }

     private void setType(List<CategoryItem> items, String type){
        if (type.equals("serie")){
            items.get(items.size() - 1).setType("s");
        } else {
            items.get(items.size() - 1).setType("p");
        }
     }

     private void setRecyclerSeries(List<AllCategory> seriesCategory, List<CategoryItem> seriesItem, String categoria){
        if (seriesItem.size() > 0){
            seriesCategory.add(new AllCategory(seriesCategory.size() + 1, categoria, seriesItem));
        }



     }

     private void setBannerMoviesPageAdapter(List<BannerMoviews> bannerMoviewsList){
        //Estas lineas nos sirven para añadir las series/peliculas al banner inicial

        ((ViewPager) findViewById(R.id.banner_viewPager)).setAdapter(new BannerHomePageAdapter(MainActivity.this, bannerMoviewsList));

        //Esta parte sirve para indicar en que posicion estamos en el banner incial
        ((TabLayout) findViewById(R.id.tab_indicator)).setupWithViewPager((ViewPager) findViewById(R.id.banner_viewPager));

        //Esto servira para que se autodeslice
        thread = new thread();
        thread.start();

        ((TabLayout) findViewById(R.id.tab_indicator)).setupWithViewPager((ViewPager) findViewById(R.id.banner_viewPager), true);
    }

     class thread extends Thread {
         public thread(){
         }

         public void run() {
             while (true){
                 try {

                     this.sleep(9000);

                     if (((ViewPager) findViewById(R.id.banner_viewPager)).getCurrentItem() < bannerSerieList.size() - 1) {
                         ((ViewPager) findViewById(R.id.banner_viewPager)).setCurrentItem(((ViewPager) findViewById(R.id.banner_viewPager)).getCurrentItem() + 1);
                     } else {
                         ((ViewPager) findViewById(R.id.banner_viewPager)).setCurrentItem(0);
                     }
                 } catch (Exception e) {
                     e.getLocalizedMessage();
                 }
             }
         }

     }

    public void setMainRecyclerAdapter(List<AllCategory> allCategoryList){

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);

        ((RecyclerView) findViewById(R.id.main_recycler)).setLayoutManager(layoutManager);

        ((RecyclerView) findViewById(R.id.main_recycler)).setAdapter(new MainRecyclerAdapter(MainActivity.this, allCategoryList));
    }

    private void setScrollToDefaultState(){
        ((NestedScrollView) findViewById(R.id.nested_scroll)).fullScroll(View.FOCUS_UP);
        ((NestedScrollView) findViewById(R.id.nested_scroll)).scrollTo(0,0);
        ((AppBarLayout) findViewById(R.id.appbar)).setExpanded(true);
    }

}