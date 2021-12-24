package com.diego.modul4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


public class DataHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "FormCekKesehatan";
    private static final String TABLE_NAME = "tbl_customer";
    private static final String KEY_ID = "_id";
    private static final String KEY_NAMALENGKAP = "nama_lengkap";
    private static final String KEY_TGLLAHIR = "tgl_lahir";
    private static final String KEY_UMUR = "umur";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_SERVICE = "service";



    public DataHelper(@Nullable Context context) { super(context, DB_NAME, null, DB_VERSION); }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createUserTable = "CREATE TABLE " + TABLE_NAME +
                " (" + KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_NAMALENGKAP + " TEXT, " +
                KEY_TGLLAHIR + " TEXT, " +
                KEY_UMUR + " TEXT, " +
                KEY_GENDER + " TEXT, " +
                KEY_SERVICE + " TEXT " + ")";
        sqLiteDatabase.execSQL(createUserTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;

        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }

    public void insert (User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_NAMALENGKAP,user.getNamaLengkap());
        values.put(KEY_TGLLAHIR,user.getTglLahir());
        values.put(KEY_UMUR,user.getUmur());
        values.put(KEY_GENDER,user.getGender());
        values.put(KEY_SERVICE,user.getService());

        db.insert(TABLE_NAME, null, values);
    }

    public List<User> selectUserData() {
        ArrayList<User> users = new ArrayList<User>();

        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {KEY_ID, KEY_NAMALENGKAP, KEY_TGLLAHIR, KEY_UMUR, KEY_GENDER, KEY_SERVICE};

        Cursor cursor = db.query(TABLE_NAME, columns, null, null, null, null, null);

        while (cursor.moveToNext()) {

            int id =cursor.getInt(0);
            String namaLengkap = cursor.getString(1);
            String tglLahir = cursor.getString(2);
            String umur = cursor.getString(3);
            String gender = cursor.getString(4);
            String service = cursor.getString(5);

            User user = new User();
            user.setId(id);
            user.setNamaLengkap(namaLengkap);
            user.setTglLahir(tglLahir);
            user.setUmur(umur);
            user.setGender(gender);
            user.setService(service);

            users.add(user);
        }

        return users;
    }

    public void update(User user){
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_NAMALENGKAP,user.getNamaLengkap());
        values.put(KEY_TGLLAHIR,user.getTglLahir());
        values.put(KEY_UMUR,user.getUmur());
        values.put(KEY_GENDER,user.getGender());
        values.put(KEY_SERVICE,user.getService());

        String whereClause = KEY_ID + " = '" + user.getId() +"'";

        db.update(TABLE_NAME, values,whereClause,null);
    }


    public void delete(int id) {
        SQLiteDatabase db = getWritableDatabase();

        String whereClause = KEY_ID + " = '" + id + "'";

        db.delete(TABLE_NAME, whereClause, null);
    }
}

