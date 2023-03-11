package com.example.dsavisualizer.searchingAlgorithms;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;

import com.example.dsavisualizer.R;

import java.util.ArrayList;

public class exponentialSearch {
    private final ArrayList<Integer> numbers;
    private final ArrayList<Button> buttons;
    private final Context context;
    private final Activity activity;
    private final int speed;
    private final int target;
    private Handler handler;
    private Runnable runnable;
    private boolean isFound;
    private boolean isDelay = true;
    private int start, end;
    private boolean isFoundTheScope;
    private int mid;
    private boolean isDelay1;

    public exponentialSearch(ArrayList<Integer> numbers, ArrayList<Button> buttons, Context context, Activity activity, int speed, int target) {
        this.numbers = numbers;
        this.buttons = buttons;
        this.context = context;
        this.activity = activity;
        this.speed = speed;
        this.target = target;
        start = 0;
        end = 1;
    }
    public Runnable play(){
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (!isFoundTheScope){
                    for (int i = start; i <= Math.min(end, numbers.size() - 1); i++){
                        buttons.get(i).setBackgroundColor(context.getResources().getColor(R.color.grey));
                    }

                    if (numbers.get(Math.min(end, numbers.size() - 1)) == target){
                        isFound = true;
                        done();
                        handler.removeCallbacks(this);
                        return;
                    }
                    if (numbers.get(Math.min(end, numbers.size()-1)) < target){
                        if (isDelay){
                            isDelay = false;
                            handler.removeCallbacks(this);
                            handler.postDelayed(this, getSpeed() / 2);
                            return;
                        }

                        for (int i = start; i <= Math.min(end, numbers.size() - 1); i++){
                            buttons.get(i).setBackgroundColor(context.getResources().getColor(R.color.white));
                        }
                        start = end / 2;
                        end *= 2;
                        Log.e("tag", String.valueOf(end));
                        isDelay = true;
                        handler.removeCallbacks(this);
                        handler.postDelayed(this, getSpeed());
                        return;
                    }
                    start = end / 2;
                    end = Math.min(end, numbers.size() - 1);
                    isFoundTheScope = true;
                }

                //do the binary search
                mid = (start + end) / 2;
                buttons.get(mid).setBackgroundColor(context.getResources().getColor(R.color.light_blue_900));
                if (isDelay){
                    isDelay = false;
                    handler.removeCallbacks(this);
                    handler.postDelayed(this, getSpeed()/2);
                    return;
                }
                if (numbers.get(mid) == target){
                    buttons.get(mid).setBackgroundColor(context.getResources().getColor(R.color.green));
                    isFound = true;
                    done();
                    handler.removeCallbacks(this);
                    return;
                }
                if (target < numbers.get(mid)){
                    for (int i = start; i <= mid - 1; i++){
                        buttons.get(i).setBackgroundColor(context.getResources().getColor(R.color.grey));
                    }
                    if (isDelay1){
                        isDelay1 = false;
                        handler.removeCallbacks(this);
                        handler.postDelayed(this, getSpeed()/2);
                        return;
                    }
                    end = mid - 1;
                }
                if (target > numbers.get(mid)){
                    for (int i = mid + 1; i <= end; i++){
                        buttons.get(i).setBackgroundColor(context.getResources().getColor(R.color.grey));
                    }
                    if (isDelay1){
                        isDelay1 = false;
                        handler.removeCallbacks(this);
                        handler.postDelayed(this, getSpeed()/2);
                        return;
                    }
                    start = mid + 1;
                }
                for (int i = 0; i < numbers.size(); i++){
                    buttons.get(i).setBackgroundColor(context.getResources().getColor(R.color.white));
                }
                if (start > end){
                    done();
                    handler.removeCallbacks(this);
                    return;
                }
                isDelay = true;
                isDelay1 = true;
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
