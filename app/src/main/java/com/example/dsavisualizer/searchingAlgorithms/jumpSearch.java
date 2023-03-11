package com.example.dsavisualizer.searchingAlgorithms;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;

import com.example.dsavisualizer.R;

import java.util.ArrayList;

public class jumpSearch {

    private final ArrayList<Integer> numbers;
    private final ArrayList<Button> buttons;
    private final Context context;
    private final Activity activity;
    private final int speed;
    private final int target;

    private Handler handler;
    private Runnable runnable;
    private int steps;
    private boolean isFound, foundTheScope;
    private int start;
    private boolean isDelay = true;

    public jumpSearch(ArrayList<Integer> numbers, ArrayList<Button> buttons, Context context, Activity activity, int speed, int target) {
        this.numbers = numbers;
        this.buttons = buttons;
        this.context = context;
        this.activity = activity;
        this.speed = speed;
        this.target = target;
        start = 0;
        steps = (int) Math.sqrt(numbers.size());
    }
    public Runnable play(){
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                //find the scope of the target
                if (!foundTheScope){
                    for (int i = start; i <= Math.min(steps, numbers.size() - 1); i++){
                        buttons.get(i).setBackgroundColor(context.getResources().getColor(R.color.grey));
                    }

                    if (numbers.get(Math.min(steps, numbers.size() - 1)) == target){
                        isFound = true;
                        done();
                        handler.removeCallbacks(this);
                        return;
                    }
                    //loop until we found the scope of the target
                    if (numbers.get(Math.min(steps, numbers.size() - 1)) < target) {
                        if (isDelay){
                            isDelay = false;
                            handler.removeCallbacks(this);
                            handler.postDelayed(this, getSpeed() / 2);
                            return;
                        }

                        for (int i = start; i <= Math.min(steps, numbers.size() - 1); i++){
                            buttons.get(i).setBackgroundColor(context.getResources().getColor(R.color.white));
                        }
                        start = steps+1;
                        steps += Math.sqrt(numbers.size());
                        isDelay = true;
                        handler.removeCallbacks(this);
                        handler.postDelayed(this, getSpeed());
                        return;
                    }
                    foundTheScope = true;
                }
                //found the scope do the linear search

                buttons.get(start).setBackgroundColor(context.getResources().getColor(R.color.light_blue_900));
                if (start > 0){
                    buttons.get(start - 1).setBackgroundColor(context.getResources().getColor(R.color.grey));
                }
                if (numbers.get(start) == target){
                    isFound = true;
                    done();
                    handler.removeCallbacks(this);
                    return;
                }
                start++;
                if (start > steps){
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
        Log.e("done: ",String.valueOf(numbers));
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
