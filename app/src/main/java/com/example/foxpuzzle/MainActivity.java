package com.example.foxpuzzle;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

    GridLayout puzzleGrid;
    TextView statusText;
    Button newGameButton;
    int[] board = new int[9];
    Bitmap[] tiles = new Bitmap[9];

    Random random = new Random();

    int moves = 0;
    int tileSize;
    boolean imageReady = false;
    Call<FoxResponse> currentCall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        puzzleGrid = findViewById(R.id.puzzleGrid);
        statusText = findViewById(R.id.statusText);
        newGameButton = findViewById(R.id.newGameButton);
    }
}