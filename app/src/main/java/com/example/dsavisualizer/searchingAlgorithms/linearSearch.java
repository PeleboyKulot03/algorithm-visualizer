package com.example.dsavisualizer.searchingAlgorithms;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.widget.Button;

import com.example.dsavisualizer.R;

import java.util.ArrayList;

public class linearSearch {
    private final ArrayList<Integer> numbers;
    private final ArrayList<Button> buttons;
    private final Context context;
    private final Activity activity;
    private final int speed;
    private final int target;
    private Handler handler;
    private Runnable runnable;
    private int i = 0;
    private boolean isFound;

    public linearSearch(ArrayList<Integer> numbers, ArrayList<Button> buttons, Context context, Activity activity, int speed, int target) {
        this.numbers = numbers;
        this.buttons = buttons;
        this.context = context;
        this.activity = activity;
        this.speed = speed;
        this.target = target;

    }

    public Runnable play(){
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                buttons.get(i).setBackgroundColor(context.getResources().getColor(R.color.light_blue_900));
                if (i > 0){
                    buttons.get(i - 1).setBackgroundColor(context.getResources().getColor(R.color.white));
                }

                if (numbers.get(i) == target){
                    isFound = true;
                    done();
                    handler.removeCallbacks(this);
                    return;
                }
                i++;
                if (i > numbers.size() - 1){
                    done();
                    handler.removeCallbacks(this);
                    return;
                }
                handler.removeCallbacks(this);
                handler.postDelayed(this, getSpeed());
            }
        };

        return runnable;
    }

    private void done() {
        android.app.AlertDialog builder = new android.app.AlertDialog.Builder(activity)
                .setTitle("Searching complete!")
                .setMessage("The search is complete and " + target + (isFound? " is found!": " is not found, do you want to try again?"))
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

