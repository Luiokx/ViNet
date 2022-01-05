 package com.salvatierra.vinet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.os.StrictMode;
import com.google.android.material.tabs.TabLayout;
import com.salvatierra.vinet.adapter.BannerHomePageAdapter;
import com.salvatierra.vinet.adapter.MainRecyclerAdapter;
import com.salvatierra.vinet.model.AllCategory;
import com.salvatierra.vinet.model.BannerMoviews;
import com.salvatierra.vinet.model.CategoryItem;
import com.salvatierra.vinet.model.DataManager;
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

        runOnUiThread(() -> {
            prepareContent(seriesCategory, DataManager.TABLE_SERIE);
            prepareContent(moviesCategory, DataManager.TABLE_PELICULA);
            prepareBannerContent(seriesCategory, moviesCategory);
        });


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
                        runOnUiThread(() -> setMainRecyclerAdapter(seriesCategory));

                        setBannerMoviesPageAdapter(bannerSerieList);

                        break;
                    case 1:
                        runOnUiThread(() -> setMainRecyclerAdapter(moviesCategory));

                        setBannerMoviesPageAdapter(bannerMoviesList);

                        break;
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

    private void prepareContent(ArrayList<AllCategory> itemCategory, String type){
        DataManager dbHelper = new DataManager(MainActivity.this);
        dbHelper.syncWithMySQLServer();

        setRecyclerSeries(itemCategory, dbHelper.getItems(type, DataManager.ACCION), "Accion");
        setRecyclerSeries(itemCategory, dbHelper.getItems(type, DataManager.AVENTURA), "Aventura");
        setRecyclerSeries(itemCategory, dbHelper.getItems(type, DataManager.COMEDIA), "Comedia");
        setRecyclerSeries(itemCategory, dbHelper.getItems(type, DataManager.DRAMA), "Drama");
        setRecyclerSeries(itemCategory, dbHelper.getItems(type, DataManager.ESCOLARES), "Escolares");
        setRecyclerSeries(itemCategory, dbHelper.getItems(type, DataManager.ECCHI), "Ecchi");
        setRecyclerSeries(itemCategory, dbHelper.getItems(type, DataManager.FICCION), "Ficcion");
        setRecyclerSeries(itemCategory, dbHelper.getItems(type, DataManager.HAREM), "Harem");
        setRecyclerSeries(itemCategory, dbHelper.getItems(type, DataManager.ISEKAI), "Isekai");
        setRecyclerSeries(itemCategory, dbHelper.getItems(type, DataManager.MAGIA), "Magia");
        setRecyclerSeries(itemCategory, dbHelper.getItems(type, DataManager.ROMANCE), "Romance");
        setRecyclerSeries(itemCategory, dbHelper.getItems(type, DataManager.SHOUNEN), "Shounen");

        if (type.equals(DataManager.TABLE_SERIE)) {
            setMainRecyclerAdapter(itemCategory);
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
}