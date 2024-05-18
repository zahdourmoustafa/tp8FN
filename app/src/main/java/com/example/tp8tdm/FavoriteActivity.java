package com.example.tp8tdm;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private FavoriteListAdapter adapter;

    AudioService audioService;

    private FavoriteDatabaseHelper dbHelper;

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favorite);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.favorite_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Database Helper
        dbHelper = new FavoriteDatabaseHelper(this);
        List<AudioModel> favoriteSongsList = dbHelper.getAllFavorites();
        // Get favorite songs from the database
        ArrayList<AudioModel> favoriteSongs = new ArrayList<>(favoriteSongsList);        // Initialize and set up the adapter
        adapter = new FavoriteListAdapter(favoriteSongs, this);
        recyclerView.setAdapter(adapter);
        // Apply window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void playFavoriteSong(AudioModel songData) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(this, Uri.parse(songData.getPath()));
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
