package com.example.tp8tdm;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class FavoriteDatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "FavoriteSongs.db";
    private static final String TABLE_FAVORITES = "favorites";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_PATH = "path";
    private Context context;
    // Add more columns as needed

    public FavoriteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FAVORITES_TABLE = "CREATE TABLE " + TABLE_FAVORITES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_TITLE + " TEXT,"
                + COLUMN_PATH + " TEXT" + ")";
        db.execSQL(CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        onCreate(db);
    }

    // Add a song to favorites
    public void addFavorite(AudioModel song) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if the song already exists in the favorites table
        if (!isSongFavorite(song)) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_TITLE, song.getTitle());
            values.put(COLUMN_PATH, song.getPath());

            db.insert(TABLE_FAVORITES, null, values);
            db.close();
        } else {
            // Song already exists in favorites
            Toast.makeText(context, "Song is already in favorites", Toast.LENGTH_SHORT).show();
        }
    }




    // Method to check if a song already exists in favorites
    private boolean isSongFavorite(AudioModel song) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_FAVORITES + " WHERE " +
                COLUMN_TITLE + " = ? AND " + COLUMN_PATH + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{song.getTitle(), song.getPath()});
        boolean isFavorite = cursor.getCount() > 0;
        cursor.close();
        return isFavorite;
    }

    // Get all favorite songs
    public List<AudioModel> getAllFavorites() {
        List<AudioModel> favoriteSongs = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_FAVORITES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
                @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(COLUMN_PATH));
                // Assuming you have more columns in your table, fetch them similarly

                AudioModel song = new AudioModel(title, path);
                // You can set more properties of the song here if needed
                favoriteSongs.add(song);
            } while (cursor.moveToNext());
        }


        cursor.close();
        db.close();
        return favoriteSongs;
    }
}
