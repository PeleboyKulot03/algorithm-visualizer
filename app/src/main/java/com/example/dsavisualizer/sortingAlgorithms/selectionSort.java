package com.example.dsavisualizer.sortingAlgorithms;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.dsavisualizer.R;

import java.util.ArrayList;


public class selectionSort {

    private final ArrayList<Integer> numbers;
    private final ArrayList<Button> buttons;
    private final Context context;
    private final Activity activity;
    private final int speed;

    public selectionSort(ArrayList<Integer> numbers, ArrayList<Button> buttons, Context context, Activity activity, int speed) {
        this.numbers = numbers;
        this.buttons = buttons;
        this.context = context;
        this.activity = activity;
        this.speed = speed;
    }

    int i = 0;
    int j = i+1;
    private Handler handler;
    private Runnable runnable;
    public Runnable play(){
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                //outer loop
                if (i != 0) {
                    buttons.get(i - 1).setBackgroundColor(context.getResources().getColor(R.color.white));
                    buttons.get(numbers.size() - 1).setBackgroundColor(context.getResources().getColor(R.color.white));
                }
                //set background color of i
                buttons.get(i).setBackgroundColor(context.getResources().getColor(R.color.light_blue_600));

                //inner loop
                int tempJ = j;
                int tempI = i;
                //make the button red
                if (j < numbers.size()){
                    buttons.get(j).setBackgroundColor(context.getResources().getColor(R.color.red));
                }
                if (j < numbers.size() && numbers.get(j) < numbers.get(i)) {

                    activity.runOnUiThread(() -> {
                        //make the button green
                        buttons.get(j).setBackgroundColor(context.getResources().getColor(R.color.green));

                        //elevate the selected buttons
                        buttons.get(tempJ).animate().translationY(-100).withEndAction(() -> buttons.get(tempJ).animate().translationY(0).start()).start();

                        buttons.get(tempI).animate().translationY(-100).withEndAction(() -> buttons.get(tempI).animate().translationY(0).start()).start();

                        //swap the numbers
                        int temp = numbers.get(i);
                        numbers.set(i, numbers.get(j));
                        numbers.set(j, temp);

                        //swap the buttons text
                        buttons.get(j).setText(String.valueOf(numbers.get(j)));
                        buttons.get(i).setText(String.valueOf(numbers.get(i)));

                        //swap the height of buttons
                        ViewGroup.LayoutParams paramsI = buttons.get(i).getLayoutParams();
                        ViewGroup.LayoutParams paramsJ = buttons.get(j).getLayoutParams();

                        temp = buttons.get(j).getHeight();
                        //swap the height of buttons
                        paramsJ.height = buttons.get(i).getHeight();
                        paramsI.height = temp;
                        buttons.get(i).setLayoutParams(paramsI);
                        buttons.get(j).setLayoutParams(paramsJ);

                    });
                }
                //back to normal color
                if (j <= numbers.size() && j != i + 1) {
                    buttons.get(j - 1).setBackgroundColor(context.getResources().getColor(R.color.white));
                }
                if (j != numbers.size() - 1){
                    j++;
                    handler.postDelayed(this, getSpeed());
                    return;
                }

                i++;
                j = i + 1;
                //end of inner loop
                if (i == numbers.size() - 1){
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
                handler.postDelayed(this, 1);
                //end of outer loop
            }
        };
        return runnable;
    }

    private int getSpeed() {
        if (speed == 1){
            return 1000;
        }
        if (speed == 2){
            return 750;
        }
        if (speed == 3){
            return 500;
        }
        if (speed == 4){
            return 250;
        }
        return 100;
    }

    public void stop() {
        handler.removeCallbacks(runnable);
    }
}
