package com.example.foxpuzzle;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    GridLayout puzzleGrid;
    TextView statusText;
    Button newGameButton;
    PuzzleBoard puzzleBoard;
    ImageButton[] tileViews = new ImageButton[9];
    Call<FoxResponse> currentCall;
    int tileSize;
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

        puzzleGrid.setColumnCount(3);
        puzzleGrid.setRowCount(3);

        newGameButton.setEnabled(false);

        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFox();
            }
        });

        tileSize = (puzzleGrid.getWidth() - 12) / 3;

        createTileViews();
        loadFox();
    }

    private void createTileViews() {
        puzzleGrid.removeAllViews();
        for (int i = 0; i < 9; i++) {
            int position = i;

            ImageButton tile = new ImageButton(this);
            tile.setPadding(0, 0, 0, 0);
            tile.setBackgroundColor(Color.LTGRAY);

            GridLayout.LayoutParams params =
                    new GridLayout.LayoutParams();

            params.width = tileSize;
            params.height = tileSize;
            params.setMargins(2, 2, 2, 2);

            tile.setLayoutParams(params);

            tile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(puzzleBoard != null && puzzleBoard.moveTile(position)) {
                        renderBoard();

                        statusText.setText("Moves: " + puzzleBoard.getMoves());

                        if (puzzleBoard.isSolved()) {
                            statusText.setText("You won in " + puzzleBoard.getMoves() + " moves!");
                        }
                    }
                }
            });

            tileViews[i] = tile;
            puzzleGrid.addView(tile);
        }
    }

    private void renderBoard() {
        if(puzzleBoard == null) return;

        for (int i = 0; i < 9; i++) {
            Tile tile = puzzleBoard.getTile(i);
            ImageButton tileView = tileViews[i];

            if (tile.isEmpty()) {
                tileView.setImageDrawable(null);
                tileView.setBackgroundColor(Color.LTGRAY);
            } else {
                tileView.setImageBitmap(tile.getImage());
                tileView.setBackgroundColor(Color.WHITE);
            }
        }
    }

    private Bitmap[] splitImage(Bitmap original) {
        Bitmap[] images = new Bitmap[9];

        int size = Math.min(original.getWidth(), original.getHeight());

        int left = (original.getWidth() - size) / 2;
        int top = (original.getHeight() - size) / 2;

        Bitmap square = Bitmap.createBitmap(original, left, top, size, size);

        Bitmap scaled = Bitmap.createScaledBitmap(square, 600, 600, true);

        int partSize = 200;

        for (int i = 0; i < 9; i++) {
            int x = (i % 3) * partSize;
            int y = (i / 3) * partSize;

            images[i] = Bitmap.createBitmap(scaled, x, y, partSize, partSize);
        }

        return images;
    }

    private void loadFox() {
        statusText.setText("Downloading photo...");
        newGameButton.setEnabled(false);
        RetrofitClient.getApi().getRandomFox().enqueue(
                new Callback<FoxResponse>() {
                    @Override
                    public void onResponse(Call<FoxResponse> call, Response<FoxResponse> response) {
                        if(!response.isSuccessful() || response.body() == null) {
                            statusText.setText("Download error");
                            newGameButton.setEnabled(true);
                            return;
                        }

                        String imageUrl = response.body().getImage();

                        /*downloading an image from url*/
                        Glide.with(MainActivity.this)
                                .asBitmap()
                                .load(imageUrl)
                                .into(new CustomTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        Bitmap[] images = splitImage(resource);

                                        puzzleBoard = new PuzzleBoard(images);
                                        puzzleBoard.shuffle();

                                        renderBoard();
                                        statusText.setText("Moves: 0");
                                        newGameButton.setEnabled(true);
                                    }

                                    @Override
                                    public void onLoadCleared(@Nullable Drawable placeholder) {

                                    }
                                });
                    }

                    @Override
                    public void onFailure(Call<FoxResponse> call, Throwable t) {
                        statusText.setText("Connection error");
                    }
                }
        );
    }


}