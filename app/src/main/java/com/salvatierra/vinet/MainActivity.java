 package com.salvatierra.vinet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.salvatierra.vinet.adapter.BannerHomePageAdapter;
import com.salvatierra.vinet.adapter.MainRecyclerAdapter;
import com.salvatierra.vinet.model.AllCategory;
import com.salvatierra.vinet.model.BannerMoviews;
import com.salvatierra.vinet.model.CategoryItem;
import com.salvatierra.vinet.model.DataManager;
import java.util.ArrayList;
import java.util.List;


 public class MainActivity extends Fragment {
    private Context context;
    private View view;
    private thread thread;
    private List<BannerMoviews> bannerSerieList;

    public MainActivity(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View response = inflater.inflate(R.layout.activity_main, container, false);
        this.context = container.getContext();
        this.view = response;

        ArrayList<AllCategory> seriesCategory = new ArrayList<>();
        ArrayList<AllCategory> moviesCategory = new ArrayList<>();


        ((Activity) context).runOnUiThread(() -> {
            prepareContent(seriesCategory, DataManager.TABLE_SERIE);
            prepareContent(moviesCategory, DataManager.TABLE_PELICULA);
            prepareBannerContent(seriesCategory, moviesCategory);
        });

        return response;
    }

    private void prepareBannerContent(ArrayList<AllCategory> seriesCategory, ArrayList<AllCategory> moviesCategory){
        bannerSerieList = new ArrayList<>();
        bannerSerieList.add(new BannerMoviews(1, "A silent voice", "http://92.187.160.50:4200/A silent voice/logo.jpg",""));
        bannerSerieList.add(new BannerMoviews(1, "Absolute Duo", "http://92.187.160.50:4200/Absolute Duo/logo.jpg",""));

        setBannerMoviesPageAdapter(bannerSerieList);

        List<BannerMoviews> bannerMoviesList = new ArrayList<>();
        bannerMoviesList.add(new BannerMoviews(1, "Air TV", "http://92.187.160.50:4200/Air TV/logo.jpg",""));
        bannerMoviesList.add(new BannerMoviews(1, "Angel Beats", "http://92.187.160.50:4200/Angel Beats/logo.jpg",""));

        ((TabLayout) view.findViewById(R.id.tabLayout)).addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        ((Activity) context).runOnUiThread(() -> setMainRecyclerAdapter(seriesCategory));

                        setBannerMoviesPageAdapter(bannerSerieList);

                        break;
                    case 1:
                        ((Activity) context).runOnUiThread(() -> setMainRecyclerAdapter(moviesCategory));

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
        DataManager dbHelper = new DataManager(context);

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

        ((ViewPager) view.findViewById(R.id.banner_viewPager)).setAdapter(new BannerHomePageAdapter(context, bannerMoviewsList));

        //Esta parte sirve para indicar en que posicion estamos en el banner incial
        ((TabLayout) view.findViewById(R.id.tab_indicator)).setupWithViewPager((ViewPager) view.findViewById(R.id.banner_viewPager));

        //Esto servira para que se autodeslice
        thread = new thread();
        thread.start();

        ((TabLayout) view.findViewById(R.id.tab_indicator)).setupWithViewPager((ViewPager) view.findViewById(R.id.banner_viewPager), true);
    }

     class thread extends Thread {
         public thread(){
         }

         public void run() {
             while (true){
                 try {

                     this.sleep(9000);

                     if (((ViewPager) view.findViewById(R.id.banner_viewPager)).getCurrentItem() < bannerSerieList.size() - 1) {
                         ((ViewPager) view.findViewById(R.id.banner_viewPager)).setCurrentItem(((ViewPager) view.findViewById(R.id.banner_viewPager)).getCurrentItem() + 1);
                     } else {
                         ((ViewPager) view.findViewById(R.id.banner_viewPager)).setCurrentItem(0);
                     }
                 } catch (Exception e) {
                     e.getLocalizedMessage();
                 }
             }
         }

     }

    public void setMainRecyclerAdapter(List<AllCategory> allCategoryList){

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);

        ((RecyclerView) view.findViewById(R.id.main_recycler)).setLayoutManager(layoutManager);

        ((RecyclerView) view.findViewById(R.id.main_recycler)).setAdapter(new MainRecyclerAdapter(context, allCategoryList));
    }
}