package com.salvatierra.vinet.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.GridLayout;
import android.widget.Spinner;

import com.salvatierra.vinet.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DataManager extends SQLiteOpenHelper {
    private static final String URL = "http://92.187.160.50:4200/";
    private static final String LOGO = "/logo.jpg";
    private final Context context;
    private static final String DB_NAME = "animes.db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_SERIE = "serie";
    public static final String TABLE_CAPITULOS = "capitulos";
    public static final String TABLE_PELICULA = "pelicula";
    public static final String TABLE_HASH = "hash";

    // Table columns name
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DESCRIPCION = "descripcion";
    private static final String TEMPORADAS = "temporadas";
    public static final String ACCION = "accion";
    public static final String AVENTURA = "aventura";
    public static final String COMEDIA = "comedia";
    public static final String DRAMA = "drama";
    public static final String ESCOLARES = "escolares";
    public static final String ECCHI = "ecchi";
    public static final String FICCION = "ficcion";
    public static final String HAREM = "harem";
    public static final String ISEKAI = "isekai";
    public static final String MAGIA = "magia";
    public static final String ROMANCE = "romance";
    public static final String SHOUNEN = "shounen";
    private static final String EXTENSION = "extension";
    private static final String CAPITULOS = "capitulos";
    private static final String IDSERIE = "idserie";
    private static final String IDTEMPORADA = "idtemporada";
    private static final String TYPE = "type";
    private static final String HASH = "hash";

    //SERIE TABLE
    private static final String CREATE_TABLE_SERIE = "create table " + TABLE_SERIE + "(" +
            ID + " INTEGER PRIMARY KEY, " +
            NAME + " TEXT NOT NULL, " +
            DESCRIPCION + " TEXT, " +
            TEMPORADAS + " TEXT, " +
            ACCION + " TEXT, " +
            AVENTURA + " TEXT, " +
            COMEDIA + " TEXT, " +
            DRAMA + " TEXT, " +
            ESCOLARES + " TEXT, " +
            ECCHI + " TEXT, " +
            FICCION + " TEXT, " +
            HAREM + " TEXT, " +
            ISEKAI + " TEXT, " +
            MAGIA + " TEXT, " +
            ROMANCE + " TEXT, " +
            SHOUNEN + " TEXT " +
            ");";

    //PELICULA TABLE
    private static final String CREATE_TABLE_PELICULA = "create table " + TABLE_PELICULA + "(" +
            ID + " INTEGER PRIMARY KEY, " +
            NAME + " TEXT NOT NULL, " +
            ACCION + " TEXT, " +
            AVENTURA + " TEXT, " +
            COMEDIA + " TEXT, " +
            DRAMA + " TEXT, " +
            ESCOLARES + " TEXT, " +
            ECCHI + " TEXT, " +
            FICCION + " TEXT, " +
            HAREM + " TEXT, " +
            ISEKAI + " TEXT, " +
            MAGIA + " TEXT, " +
            ROMANCE + " TEXT, " +
            SHOUNEN + " TEXT, " +
            DESCRIPCION + " TEXT, " +
            EXTENSION + " TEXT " +
            ");";

    //CAPITULOS TABLE
    private static final String CREATE_TABLE_CAPITULOS = "create table " + TABLE_CAPITULOS + "(" +
            ID + " INTEGER PRIMARY KEY, " +
            CAPITULOS + " TEXT NOT NULL, " +
            IDSERIE + " TEXT, " +
            IDTEMPORADA + " TEXT, " +
            TYPE + " TEXT " +
            ");";

    //HASH TABLE
    private static final String CREATE_TABLE_HASH = "create table " + TABLE_HASH + "(" +
            ID + " INTEGER PRIMARY KEY, " +
            HASH + " TEXT" +
            ");";

    public DataManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL(CREATE_TABLE_SERIE);
        sQLiteDatabase.execSQL(CREATE_TABLE_PELICULA);
        sQLiteDatabase.execSQL(CREATE_TABLE_CAPITULOS);
        sQLiteDatabase.execSQL(CREATE_TABLE_HASH);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int oldVersion, int newVersion) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SERIE);
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PELICULA);
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CAPITULOS);
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_HASH);
        onCreate(sQLiteDatabase);
    }

    public void syncWithMySQLServer(){
        try {
            Class.forName("com.mysql.jdbc.Driver");

            Connection conexion = DriverManager.getConnection("jdbc:mysql://92.187.160.50:3306/anime", "animes", "vivaelbetis");

            Statement consulta = conexion.createStatement();

            String sql = "SELECT hash FROM " + "hash";

            ResultSet resultado = consulta.executeQuery(sql);

            while (resultado.next()){
                if (!resultado.getString(1).equals(getHash())){
                    resetDatabase();
                    resyncDatabase(consulta);
                }

                return;
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void resyncDatabase(Statement consulta) {
        String sql = "select * from serie";

        try {
            //serie
            ResultSet resultado = consulta.executeQuery(sql);

            while (resultado.next()){
                insertSerie(resultado.getString(1), resultado.getString(2), resultado.getString(3), resultado.getString(4), resultado.getString(5),
                        resultado.getString(6), resultado.getString(7), resultado.getString(8), resultado.getString(9), resultado.getString(10), resultado.getString(11),
                        resultado.getString(12), resultado.getString(13), resultado.getString(14), resultado.getString(15), resultado.getString(16));
            }

            //pelicula
            sql = "select * from pelicula";

            resultado = consulta.executeQuery(sql);

            while (resultado.next()){
                insertPelicula(resultado.getString(1), resultado.getString(2), resultado.getString(3), resultado.getString(4), resultado.getString(5),
                        resultado.getString(6), resultado.getString(7), resultado.getString(8), resultado.getString(9), resultado.getString(10), resultado.getString(11),
                        resultado.getString(12), resultado.getString(13), resultado.getString(14), resultado.getString(15), resultado.getString(16));
            }

            //capitulos
            sql = "select * from capitulos";
            resultado = consulta.executeQuery(sql);

            while (resultado.next()){
                insertCapitulos(resultado.getString(1), resultado.getString(2), resultado.getString(3),
                        resultado.getString(4), resultado.getString(5));
            }

            //HASH
            sql = "select hash from hash";
            resultado = consulta.executeQuery(sql);

            while (resultado.next()){
                insertHash(resultado.getString(1));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void insertHash(String hash){
        ContentValues values = new ContentValues();
        values.put(ID, 1);
        values.put(HASH, hash);

        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        sQLiteDatabase.insert(TABLE_HASH, null, values);
    }

    private void insertCapitulos(String id, String capitulos, String idserie, String idtemporada, String type){
        ContentValues values = new ContentValues();
        values.put(ID, id);
        values.put(CAPITULOS, capitulos);
        values.put(IDSERIE, idserie);
        values.put(IDTEMPORADA, idtemporada);
        values.put(TYPE, type);

        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        sQLiteDatabase.insert(TABLE_CAPITULOS, null, values);
    }

    private void insertPelicula(String id, String name, String accion, String aventura, String comedia, String drama, String escolares, String ecchi,
                                String ficcion, String harem, String isekai, String magia, String romance, String shounen, String descripcion, String extension){

        ContentValues values = new ContentValues();
        values.put(ID, id);
        values.put(NAME, name);
        values.put(ACCION, accion);
        values.put(AVENTURA, aventura);
        values.put(COMEDIA, comedia);
        values.put(DRAMA, drama);
        values.put(ESCOLARES, escolares);
        values.put(ECCHI, ecchi);
        values.put(FICCION, ficcion);
        values.put(HAREM, harem);
        values.put(ISEKAI, isekai);
        values.put(MAGIA, magia);
        values.put(ROMANCE, romance);
        values.put(SHOUNEN, shounen);
        values.put(DESCRIPCION, descripcion);
        values.put(EXTENSION, extension);

        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        sQLiteDatabase.insert(TABLE_PELICULA, null, values);
    }

    private void insertSerie(String id, String name, String descripcion, String temporadas, String accion, String aventura, String comedia, String drama
    , String escolares, String ecchi, String ficcion, String harem, String isekai, String magia, String romance, String shounen){

        ContentValues values = new ContentValues();
        values.put(ID, id);
        values.put(NAME, name);
        values.put(DESCRIPCION, descripcion);
        values.put(TEMPORADAS, temporadas);
        values.put(ACCION, accion);
        values.put(AVENTURA, aventura);
        values.put(COMEDIA, comedia);
        values.put(DRAMA, drama);
        values.put(ESCOLARES, escolares);
        values.put(ECCHI, ecchi);
        values.put(FICCION, ficcion);
        values.put(HAREM, harem);
        values.put(ISEKAI, isekai);
        values.put(MAGIA, magia);
        values.put(ROMANCE, romance);
        values.put(SHOUNEN, shounen);

        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        sQLiteDatabase.insert(TABLE_SERIE, null, values);
    }

    private void resetDatabase() {
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        sQLiteDatabase.execSQL("delete from " + TABLE_SERIE);
        sQLiteDatabase.execSQL("delete from " + TABLE_PELICULA);
        sQLiteDatabase.execSQL("delete from " + TABLE_CAPITULOS);
        sQLiteDatabase.execSQL("delete from " + TABLE_HASH);
    }

    private String getHash(){
        String query = "Select " + HASH + " FROM " + TABLE_HASH + " WHERE " + ID + " = '1'";
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sQLiteDatabase.rawQuery(query, null);

        while (cursor.moveToNext()){
            return cursor.getString(0);
        }

        sQLiteDatabase.close();
        return "";
    }

    public List<CategoryItem> getItems(String type, String genre){
        List<CategoryItem> response = new ArrayList<>();

        String query = "SELECT id, name, descripcion FROM " + type + " WHERE " + genre + " = '1'";

        if (type.equals(TABLE_PELICULA)){
            query = "SELECT id, name, descripcion, extension FROM " + type + " WHERE " + genre + " = '1'";
        }


        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sQLiteDatabase.rawQuery(query, null);

        while (cursor.moveToNext()){
            if (type.equals(TABLE_PELICULA)){
                response.add(new CategoryItem(cursor.getInt(0), cursor.getString(1), cursor.getString(2), URL + cursor.getString(1) + LOGO, cursor.getString(3), type));
            } else {
                response.add(new CategoryItem(cursor.getInt(0), cursor.getString(1), cursor.getString(2), URL + cursor.getString(1) + LOGO, type));
            }
        }

        return response;
    }

    public ArrayList<String> getCapitulos(int idSerie, int idTemporada, CategoryItem item, String chapterName){
        int count = 0;
        ArrayList<String> response = new ArrayList<>();
        String query = "SELECT capitulos, type FROM capitulos WHERE idserie = " + idSerie + " and idtemporada = " + idTemporada;

        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sQLiteDatabase.rawQuery(query, null);

        while (cursor.moveToNext()){
            count = cursor.getInt(0);
            item.setExtension(cursor.getString(1));
        }

        for (int i = 0; i < count; i++){
            response.add(chapterName + " " + (i + 1));
        }

        return response;
    }

    public CategoryItem getDataSerie(int idItem){
        CategoryItem response = new CategoryItem();
        String query = "SELECT id, name, descripcion, temporadas FROM serie WHERE id = " + idItem;

        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sQLiteDatabase.rawQuery(query, null);

        while (cursor.moveToNext()){
            response.setId(cursor.getInt(0));
            response.setMovieName(cursor.getString(1));
            response.setDescription(cursor.getString(2));
            response.setTemporadas(Integer.parseInt(cursor.getString(3)));
        }

        return response;
    }

    public CategoryItem getDataMovie(int idItem){
        CategoryItem response = new CategoryItem();
        String query = "SELECT id, name, descripcion, extension FROM pelicula WHERE id = " + idItem;

        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sQLiteDatabase.rawQuery(query, null);

        while (cursor.moveToNext()){
            response.setId(cursor.getInt(0));
            response.setMovieName(cursor.getString(1));
            response.setDescription(cursor.getString(2));
            response.setExtension(cursor.getString(3));
        }

        return response;
    }

    public ArrayList<CategoryItem> getSearch(String str){
        ArrayList<CategoryItem> response = new ArrayList<>();
        String query = "SELECT id, name from serie";

        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sQLiteDatabase.rawQuery(query, null);

        while(cursor.moveToNext()){
            if (cursor.getString(1).toLowerCase().contains(str.toLowerCase())) {
                response.add(new CategoryItem());
                response.get(response.size() - 1).setId(cursor.getInt(0));
                response.get(response.size() - 1).setMovieName(cursor.getString(1));
                response.get(response.size() - 1).setType(TABLE_SERIE);
                response.get(response.size() - 1).setImageUrl(URL + cursor.getString(1) + LOGO);
            }
        }

        query = "SELECT id, name from pelicula";
        cursor = sQLiteDatabase.rawQuery(query, null);

        while (cursor.moveToNext()) {
            if (cursor.getString(1).toLowerCase().contains(str.toLowerCase())) {
                response.add(new CategoryItem());
                response.get(response.size() - 1).setId(cursor.getInt(0));
                response.get(response.size() - 1).setMovieName(cursor.getString(1));
                response.get(response.size() - 1).setType(TABLE_PELICULA);
                response.get(response.size() - 1).setImageUrl(URL + cursor.getString(1) + LOGO);
            }
        }

        return response;
    }

    public boolean isOnlySpacing(String str, GridLayout gridLayout) {
        if (str.trim().isEmpty()){
            gridLayout.removeAllViewsInLayout();
            return true;
        }

        return false;
    }

}
