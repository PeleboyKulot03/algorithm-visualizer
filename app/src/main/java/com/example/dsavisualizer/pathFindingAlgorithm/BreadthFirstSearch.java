package com.example.dsavisualizer.pathFindingAlgorithm;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dsavisualizer.R;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

public class BreadthFirstSearch {
    private final RecyclerView recyclerView;
    private final int[][] map;

    private Handler handler;
    private Runnable runnable;
    private final Activity activity;
    private final Context context;
    private final int speed;
    private final Queue<Pair<Integer, Integer>> queue = new LinkedList<>();
    private final HashSet<Pair<Integer, Integer>> visited = new HashSet<>();
    private final Map<Pair<Integer, Integer>, Pair<Integer, Integer>> previous = new HashMap<>();
    private int backX;
    private int backY;
    private int pos;
    int x = 0;
    int y = 0;
    private boolean isFirst = true;
    public BreadthFirstSearch(RecyclerView recyclerView, int[][] map, Activity activity, Context context, int speed){
        this.map = map;
        this.recyclerView = recyclerView;
        this.activity = activity;
        this.context = context;
        this.speed = speed;
        queue.add(new Pair<>(0, 0));
        visited.add(new Pair<>(0, 0));

    }

    public Runnable play() {

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (queue.isEmpty()) {
                    done(false);
                    handler.removeCallbacks(this);
                    return;
                }

                x = Objects.requireNonNull(queue.peek()).second;
                y = Objects.requireNonNull(queue.peek()).first;

                if (x == map.length - 1 && y == map.length - 1){

                    if (isFirst){
                        backX = map.length - 1;
                        backY = map.length - 1;
                        isFirst = false;
                        ConstraintLayout layout = Objects.requireNonNull(Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(getPos(backX, backY))).itemView.findViewById(R.id.block));
                        layout.setBackgroundColor(context.getResources().getColor(R.color.purple_200));
                        handler.postDelayed(this, getSpeed());
                        return;
                    }
                    pos = getPos(Objects.requireNonNull(previous.get(new Pair<>(backY, backX))).second, Objects.requireNonNull(previous.get(new Pair<>(backY, backX))).first);

                    ConstraintLayout layout = Objects.requireNonNull(Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(getPos(backX, backY))).itemView.findViewById(R.id.block));
                    layout.setBackgroundColor(context.getResources().getColor(R.color.purple_200));

                    if (pos == 0){
                        done(true);
                        handler.removeCallbacks(this);
                        return;
                    }
                    int tempY = backY;
                    int tempX = backX;

                    backY = Objects.requireNonNull(previous.get(new Pair<>(tempY, tempX))).first;
                    backX = Objects.requireNonNull(previous.get(new Pair<>(tempY, tempX))).second;

                    handler.postDelayed(this, getSpeed());
                    return;
                }

                getNeighbours(x, y);
                queue.remove();
                handler.postDelayed(this, getSpeed() / 2);
            }
        };

        return runnable;
    }

    private void reconstructPath(){
        Log.e("Tegeee", "reconstructPath: " + previous);
        int x = map.length - 1;
        int y = map.length - 1;

        int pos = getPos(x, y);
        ConstraintLayout layout = Objects.requireNonNull(Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(pos)).itemView.findViewById(R.id.block));
        layout.setBackgroundColor(context.getResources().getColor(R.color.purple_200));
        while (true){
            pos = getPos(Objects.requireNonNull(previous.get(new Pair<>(y, x))).second, Objects.requireNonNull(previous.get(new Pair<>(y, x))).first);
            layout = Objects.requireNonNull(Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(pos)).itemView.findViewById(R.id.block));
            layout.setBackgroundColor(context.getResources().getColor(R.color.purple_200));
            y = Objects.requireNonNull(previous.get(new Pair<>(y, x))).first;
            x = Objects.requireNonNull(previous.get(new Pair<>(y, x))).second;
            Log.e("Tegeee", "reconstructPath: " + y + ", " + x);
            if (pos == 0){
                break;
            }
        }
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

    private void getNeighbours(int x, int y) {

        //right
        if ((x+1 < map.length) && (map[y][x+1] != 1) && (!visited.contains(new Pair<>(y, x+1)))){
            queue.add(new Pair<>(y, x+1));
            visited.add(new Pair<>(y, x+1));
            //add it to the previous
            previous.put(new Pair<>(y, x+1), new Pair<>(y, x));

            //add color to the next position
            ConstraintLayout layout = Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(getPos(x+1, y))).itemView.findViewById(R.id.block);
            layout.setBackgroundColor(context.getResources().getColor(R.color.light_blue_600));
//            if (y == map.length - 1 && x+1 == map.length-1){
//                previous.put( new Pair<>(y, x+1), new Pair<>(y, x));
//            }
        }

        //down
        if ((y+1 < map.length) && map[y+1][x] != 1 && (!visited.contains(new Pair<>(y+1, x)))){
            queue.add(new Pair<>(y+1, x));
            visited.add(new Pair<>(y+1, x));
            //add it to the previous
            previous.put(new Pair<>(y+1, x), new Pair<>(y, x));
            //add color to the next position
            ConstraintLayout layout = Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(getPos(x, y+1))).itemView.findViewById(R.id.block);
            layout.setBackgroundColor(context.getResources().getColor(R.color.light_blue_600));
//            if (y+1 == map.length - 1 && x == map.length-1){
//                previous.put(new Pair<>(y+1, x), new Pair<>(y, x));
//            }
        }

        //left
        if ((x-1 >= 0) && map[y][x-1] != 1 && (!visited.contains(new Pair<>(y, x-1)))){
            queue.add(new Pair<>(y, x-1));

            visited.add(new Pair<>(y, x-1));
            //add it to the previous
            previous.put(new Pair<>(y, x-1), new Pair<>(y, x));
            //add color to the next position
            ConstraintLayout layout = Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(getPos(x-1, y))).itemView.findViewById(R.id.block);
            layout.setBackgroundColor(context.getResources().getColor(R.color.light_blue_600));
//            if (y == map.length - 1 && x-1 == map.length-1){
//                previous.put(new Pair<>(y, x-1), new Pair<>(y, x));
//            }
        }

        //up
        if ((y-1 >= 0) && map[y-1][x] != 1 && (!visited.contains(new Pair<>(y-1, x)))){
            queue.add(new Pair<>(y-1, x));

            visited.add(new Pair<>(y-1, x));
            //add it to the previous
            previous.put(new Pair<>(y-1, x), new Pair<>(y, x));
            //add color to the next position
            ConstraintLayout layout = Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(getPos(x, y-1))).itemView.findViewById(R.id.block);
            layout.setBackgroundColor(context.getResources().getColor(R.color.light_blue_600));
            if (y-1 == map.length - 1 && x == map.length-1){
                previous.put(new Pair<>(y-1, x), new Pair<>(y, x));
            }
        }

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
