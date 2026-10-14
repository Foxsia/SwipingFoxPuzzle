package com.example.foxpuzzle;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PuzzleBoard {
    private static final int SIZE = 3;
    private static final int TILE_COUNT = SIZE * SIZE;

    private final Tile[] tiles;
    private final Random random;

    private int moves;

    public PuzzleBoard(Bitmap[] images) {
        tiles = new Tile[TILE_COUNT];
        random = new Random();

        for (int i = 0; i < TILE_COUNT-1; i++) {
            if(images[i] == null)  return;

            tiles[i] = new Tile(i, images[i]);
        }

        tiles[TILE_COUNT-1] = Tile.createEmpty();
        moves = 0;
    }

    public int getMoves() {
        return moves;
    }

    public Tile getTile(int position) {
        if (position < 0 || position >= TILE_COUNT) return tiles[0];

        return tiles[position];
    }

    private int getEmptyPosition() {
        for (int i = 0; i < TILE_COUNT; i++) {
            if (tiles[i].isEmpty()) {
                return i;
            }
        }
        return -1;
    }
    public boolean canMove(int position) {
        if (position < 0 || position >= TILE_COUNT) return false;

        if (tiles[position].isEmpty()) return false;

        int emptyPosition = getEmptyPosition();

        int row = position / SIZE;
        int col = position % SIZE;

        int emptyRow = emptyPosition / SIZE;
        int emptyCol = emptyPosition % SIZE;

        return Math.abs(row - emptyRow) + Math.abs(col - emptyCol) == 1;
    }

    public boolean moveTile(int position) {
        if(!canMove(position)) return false;

        int emptyPosition = getEmptyPosition();

        Tile temp = tiles[position];
        tiles[position] = tiles[emptyPosition];
        tiles[emptyPosition] = temp;

        moves++;
        return true;
    }

    private List<Integer> getNeighbours(int position) {
        List<Integer> neighbours = new ArrayList<>();

        int row = position / SIZE;
        int col = position % SIZE;

        if (row > 0) {
            neighbours.add(position - SIZE);
        }

        if (row < SIZE - 1) {
            neighbours.add(position + SIZE);
        }

        if (col > 0) {
            neighbours.add(position - 1);
        }

        if (col < SIZE - 1) {
            neighbours.add(position + 1);
        }

        return neighbours;
    }

    public void shuffle() {
        int emptyPosition = getEmptyPosition();
        int previousEmptyPosition = -1;

        for (int i = 0; i < 150; i++) {
            List<Integer> neighbours = getNeighbours(emptyPosition);

            if (neighbours.size() > 1) {
                neighbours.remove(Integer.valueOf(previousEmptyPosition));
            }

            int nextPosition = neighbours.get(random.nextInt(neighbours.size()));

            Tile temp = tiles[emptyPosition];
            tiles[emptyPosition] = tiles[nextPosition];
            tiles[nextPosition] = temp;

            previousEmptyPosition = emptyPosition;
            emptyPosition = nextPosition;
        }

        moves = 0;
    }

    public boolean isSolved() {
        for (int i = 0; i < 8; i++) {
            if (tiles[i].isEmpty()
                    || tiles[i].getCorrectPosition() != i) {
                return false;
            }
        }

        return tiles[8].isEmpty();
    }
}
