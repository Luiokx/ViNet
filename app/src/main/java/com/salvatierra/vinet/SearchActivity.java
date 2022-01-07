package com.salvatierra.vinet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.salvatierra.vinet.model.CategoryItem;
import com.salvatierra.vinet.model.DataManager;
import java.util.ArrayList;

public class SearchActivity extends Fragment {
    private Context context;
    private View view;
    private ArrayList<CategoryItem> search;
    private DataManager dbHelper;

    public SearchActivity(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View response = inflater.inflate(R.layout.activity_search, container, false);
        this.context = container.getContext();
        this.view = response;

        dbHelper = new DataManager(context);

        ((EditText) view.findViewById(R.id.editTextSearch)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = ((EditText) view.findViewById(R.id.editTextSearch)).getText().toString();
                //If on edit text is only spacing or null
                if (dbHelper.isOnlySpacing(str, ((GridLayout) view.findViewById(R.id.glSearch)))) {
                    return;
                }

                search = dbHelper.getSearch(str);
                refreshGridLayout();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return response;
    }

    private void refreshGridLayout() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels / 2;
        int height = (int) ((width / 100) * 55.55);

        GridLayout gridLayout = (GridLayout) view.findViewById(R.id.glSearch);
        gridLayout.removeAllViews();
        gridLayout.setRowCount(search.size() / 2);


        for (int i = 0; i < search.size(); i++) {
            ImageView iv = new ImageView(context);
            iv.setLayoutParams(new LinearLayout.LayoutParams(width, height));
            iv.setPadding(10,10,10,10);
            iv.setBackground(context.getDrawable(R.drawable.ivradiues));
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(context).load(this.search.get(i).getImageUrl()).into(iv);

            int finalI = i;
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent window = new Intent(context, SerieDetail.class);

                    if (search.get(finalI).getType().equals(DataManager.TABLE_PELICULA)){
                        window = new Intent(context, MovieDetail.class);
                    }

                    window.putExtra("idObject", search.get(finalI).getId());
                    startActivity(window);
                }
            });

            gridLayout.addView(iv);
        }
    }
}
