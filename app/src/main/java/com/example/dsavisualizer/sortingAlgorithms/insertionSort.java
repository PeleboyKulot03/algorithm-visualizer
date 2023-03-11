package com.example.dsavisualizer.sortingAlgorithms;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.dsavisualizer.R;

import java.util.ArrayList;

public class insertionSort {
    private final ArrayList<Integer> numbers;
    private final ArrayList<Button> buttons;
    private final Context context;
    private final Activity activity;

    private final int speed;
    private int i = 1;
    private int j = i;
    private boolean delay = true;
    private boolean inLoop = false;

    public insertionSort(ArrayList<Integer> numbers, ArrayList<Button> buttons, Context context, Activity activity, int speed) {
        this.numbers = numbers;
        this.buttons = buttons;
        this.context = context;
        this.activity = activity;
        this.speed = speed;
    }
    private Handler handler;
    private Runnable runnable;

    public Runnable play(){
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                //outer loop
                if (!inLoop){
                    if (j > 1){
                        buttons.get(j-2).setBackgroundColor(context.getResources().getColor(R.color.white));
                        buttons.get(j-1).setBackgroundColor(context.getResources().getColor(R.color.white));
                        buttons.get(j).setBackgroundColor(context.getResources().getColor(R.color.white));
                    }
                }

                if (j != 0){
                    buttons.get(j-1).setBackgroundColor(context.getResources().getColor(R.color.white));
                }
                buttons.get(j).setBackgroundColor(context.getResources().getColor(R.color.white));

                if (j+1 < numbers.size()){
                    buttons.get(j+1).setBackgroundColor(context.getResources().getColor(R.color.white));
                }
                if (j >= 1){
                    buttons.get(j-1).setBackgroundColor(context.getResources().getColor(R.color.red));
                    buttons.get(j).setBackgroundColor(context.getResources().getColor(R.color.light_blue_600));
                }
                //inner loop
                int tempJ = j;
                if (j > 0 && j < numbers.size() && numbers.get(j - 1) > numbers.get(j)){
                    inLoop = true;
                    if (delay){
                        handler.postDelayed(this, getSpeed() / 2);
                        delay = false;
                        return;
                    }

                    buttons.get(j).setBackgroundColor(context.getResources().getColor(R.color.green));
                    buttons.get(j-1).setBackgroundColor(context.getResources().getColor(R.color.green));

                    //swap the numbers
                    activity.runOnUiThread(() -> {
                        ViewGroup.LayoutParams paramsJ = buttons.get(tempJ).getLayoutParams();
                        ViewGroup.LayoutParams paramsJ1 = buttons.get(tempJ - 1).getLayoutParams();

                        //swap the numbers
                        int temp = numbers.get(tempJ);
                        numbers.set(tempJ, numbers.get(tempJ - 1));
                        numbers.set(tempJ - 1, temp);

                        //swap the buttons text
                        buttons.get(tempJ).setText(String.valueOf(numbers.get(tempJ)));
                        buttons.get(tempJ - 1).setText(String.valueOf(numbers.get(tempJ - 1)));

                        temp = buttons.get(tempJ).getHeight();
                        //swap the height of buttons
                        paramsJ.height = buttons.get(tempJ - 1).getHeight();
                        paramsJ1.height = temp;

                        buttons.get(tempJ - 1).setLayoutParams(paramsJ1);
                        buttons.get(tempJ).setLayoutParams(paramsJ);

                        //elevate the selected buttons
                        buttons.get(tempJ - 1).animate().translationY((-buttons.get(tempJ - 1).getWidth() * 2) + ((buttons.get(tempJ - 1).getWidth() * 2) / 2f)).withEndAction(() -> buttons.get(tempJ - 1).animate().translationY(0).withEndAction(() -> {

                        }).start()).start();
                        buttons.get(tempJ).animate().translationY((buttons.get(tempJ).getWidth() * 2) - ((buttons.get(tempJ).getWidth() * 2) / 2f)).withEndAction(() -> buttons.get(tempJ).animate().translationY(0).withEndAction(() -> {
                        }).start()).start();
                    });
                    delay = true;
                    j--;
                    handler.postDelayed(this, getSpeed());
                    return;
                }
                //end of inner loop

                if (j != i && j > 0){
                    buttons.get(j-1).setBackgroundColor(context.getResources().getColor(R.color.white));
                    buttons.get(j).setBackgroundColor(context.getResources().getColor(R.color.white));
                }

                if (i == buttons.size()-1){
                    android.app.AlertDialog builder = new android.app.AlertDialog.Builder(activity)
                            .setTitle("Sorting Complete!")
                            .setMessage("Sorting completed, do you want to try again?")
                            .setPositiveButton("yes", (dialog, which) -> {
                                activity.recreate();
                                dialog.dismiss();
                            })
                            .setNegativeButton("No", (dialog, which) -> {
                                activity.finish();
                                dialog.dismiss();
                            }).create();
                    builder.show();

                    handler.removeCallbacks(this);
                    return;
                }

                delay = true;
                i++;
                j = i;
                inLoop = false;
                handler.postDelayed(this, 1);
                //end of outer loop
            }

        };
        return runnable;
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
