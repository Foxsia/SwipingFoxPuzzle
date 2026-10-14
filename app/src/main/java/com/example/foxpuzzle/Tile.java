package com.example.foxpuzzle;

import android.graphics.Bitmap;

public class Tile {
    private final int correctPosition;
    private final Bitmap image;
    private final boolean empty;

    public Tile(int correctPosition, Bitmap image, boolean empty) {
        this.correctPosition = correctPosition;
        this.image = image;
        this.empty = empty;
    }

    private Tile() {
        this.correctPosition = -1;
        this.image = null;
        this.empty = true;
    }

    public static Tile createEmpty() {
        return new Tile();
    }

    public int getCorrectPosition() {
        return correctPosition;
    }

    public Bitmap getImage() {
        return image;
    }

    public boolean isEmpty() {
        return empty;
    }
}