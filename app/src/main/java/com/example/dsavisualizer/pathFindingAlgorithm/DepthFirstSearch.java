package com.example.dsavisualizer.pathFindingAlgorithm;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Pair;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dsavisualizer.R;

import java.util.HashSet;
import java.util.Objects;
import java.util.Stack;

public class DepthFirstSearch {
    private final RecyclerView recyclerView;
    private final int[][] map;

    private Handler handler;
    private Runnable runnable;
    private final Activity activity;
    private final Context context;
    private final int speed;
    private final Stack<Pair<Integer, Integer>> stack = new Stack<>();
    private final HashSet<Pair<Integer, Integer>> visited = new HashSet<>();

    int x = 0;
    int y = 0;
    private boolean isDelay = true;
    public DepthFirstSearch(RecyclerView recyclerView, int[][] map, Activity activity, Context context, int speed){
        this.map = map;
        this.recyclerView = recyclerView;
        this.activity = activity;
        this.context = context;
        this.speed = speed;
        stack.push(new Pair<>(0, 0));
        visited.add(new Pair<>(0, 0));

    }

    public Runnable play() {

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (stack.isEmpty()) {
                    done(false);
                    handler.removeCallbacks(this);
                    return;
                }
                x = stack.peek().second;
                y = stack.peek().first;
                if (x == map.length - 1 && y == map.length -1){
                    done(true);
                    handler.removeCallbacks(this);
                    return;
                }
                //add color to the current position
                ConstraintLayout layout = Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(getPos(x, y))).itemView.findViewById(R.id.block);
                layout.setBackgroundColor(context.getResources().getColor(R.color.light_blue_600));

                //move to the next position
                Pair<Integer, Integer> move = nextMove(x, y);
                if (move != null){
                    stack.push(move);
                    x = move.second;
                    y = move.first;
                    handler.removeCallbacks(this);
                    handler.postDelayed(this, getSpeed());
                    return;
                }
                if (isDelay){
                    handler.postDelayed(this, getSpeed() / 2);
                    isDelay = false;
                    return;
                }
                layout = Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(getPos(x, y))).itemView.findViewById(R.id.block);
                layout.setBackgroundColor(context.getResources().getColor(R.color.white));
                stack.pop();
                handler.postDelayed(this, 1000);
                isDelay = true;
            }
        };

        return runnable;
    }

    private int getPos(int x, int y){
        int counter = 0;
        for (int i = 0; i < map.length; i++){
            for (int j = 0; j < map.length; j++){
                if (i == y && j == x) return counter;
                counter++;
            }
        }

        return counter;
    }

    private Pair<Integer, Integer> nextMove(int x, int y) {

        //right
        if ((x+1 < map.length) && (map[y][x+1] != 1) && (!visited.contains(new Pair<>(y, x+1)))){
            Pair<Integer, Integer> move = new Pair<>(y, x+1);
            visited.add(move);
            return move;
        }

        //down
        if ((y+1 < map.length) && map[y+1][x] != 1 && (!visited.contains(new Pair<>(y+1, x)))){
            Pair<Integer, Integer> move = new Pair<>(y+1, x);
            visited.add(move);
            return move;
        }

        //left
        if ((x-1 >= 0) && map[y][x-1] != 1 && (!visited.contains(new Pair<>(y, x-1)))){
            Pair<Integer, Integer> move = new Pair<>(y, x-1);
            visited.add(move);
            return move;
        }

        //up
        if ((y-1 >= 0) && map[y-1][x] != 1 && (!visited.contains(new Pair<>(y-1, x)))){
            Pair<Integer, Integer> move = new Pair<>(y-1, x);
            visited.add(move);
            return move;
        }

        return null;
    }
    private void done(boolean isFound) {
        android.app.AlertDialog builder = new android.app.AlertDialog.Builder(activity)
                .setTitle("Path finding complete!")
                .setMessage("The Path finding is complete and "  + (isFound? " Path is found!": " Path is not found, do you want to try again?"))
                .setPositiveButton("yes", (dialog, which) -> {
                    activity.recreate();
                    dialog.dismiss();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    activity.finish();
                    dialog.dismiss();
                }).create();
        builder.show();
    }

    private int getSpeed() {
        if (speed == 1) {
            return 1000;
        }
        if (speed == 2) {
            return 750;
        }
        if (speed == 3) {
            return 500;
        }
        if (speed == 4) {
            return 250;
        }
        return 100;
    }
    public void stop() {
        handler.removeCallbacks(runnable);
    }
}
