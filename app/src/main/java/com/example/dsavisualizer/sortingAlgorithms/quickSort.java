package com.example.dsavisualizer.sortingAlgorithms;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Pair;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.dsavisualizer.R;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class quickSort {
    private final ArrayList<Integer> numbers;
    private final ArrayList<Button> buttons;
    private final Context context;
    private final Activity activity;

    private final int speed;
    private int leftP;
    private int rightP;
    private int low;
    private int high;
    private int pivot;
    private int pivotI;
    private final Stack<Pair<Integer, Integer>> stack = new Stack<>();
    private TextView stackText;
    public quickSort(ArrayList<Integer> numbers, ArrayList<Button> buttons, Context context, Activity activity, int speed) {
        this.numbers = numbers;
        this.buttons = buttons;
        this.context = context;
        this.activity = activity;
        this.speed = speed;
        stack.push(new Pair<>(0, numbers.size() - 1));
        stackText = activity.findViewById(R.id.stack);
    }

    public void swap(int num1, int num2) {
        final int[] temp = {numbers.get(num2)};
        numbers.set(num2, numbers.get(num1));
        numbers.set(num1, temp[0]);


        buttons.get(num1).setBackgroundColor(context.getResources().getColor(R.color.green));
        buttons.get(num2).setBackgroundColor(context.getResources().getColor(R.color.green));

        //swap the numbers
        activity.runOnUiThread(() -> {
            ViewGroup.LayoutParams paramsJ = buttons.get(num1).getLayoutParams();
            ViewGroup.LayoutParams paramsJ1 = buttons.get(num2).getLayoutParams();

            //swap the buttons text
            buttons.get(num1).setText(String.valueOf(numbers.get(num1)));
            buttons.get(num2).setText(String.valueOf(numbers.get(num2)));

            temp[0] = buttons.get(num1).getHeight();
            //swap the height of buttons
            paramsJ.height = buttons.get(num2).getHeight();
            paramsJ1.height = temp[0];

            buttons.get(num2).setLayoutParams(paramsJ1);
            buttons.get(num1).setLayoutParams(paramsJ);

            //elevate the selected buttons
            buttons.get(num2).animate().translationY((-buttons.get(num2).getWidth() * 2) + ((buttons.get(num2).getWidth() * 2) / 2f)).withEndAction(() -> buttons.get(num2).animate().translationY(0).withEndAction(() -> {

            }).start()).start();
            buttons.get(num1).animate().translationY((buttons.get(num1).getWidth() * 2) - ((buttons.get(num1).getWidth() * 2) / 2f)).withEndAction(() -> buttons.get(num1).animate().translationY(0).withEndAction(() -> {
            }).start()).start();
        });
    }
    private Handler handler;
    private Runnable runnable;
    int tempH = 0;
    public Runnable play(){
        final boolean[] isFirst = {true};
        final boolean[] isFirst1 = {true};
        final boolean[] delay = {true};
        final boolean[] isSwap = {false};

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                //outer loop
                if (isFirst[0]){
                    if (isFirst1[0]){
                        low = stack.peek().first;
                        high = stack.peek().second;
                        tempH = high;
                        String temp = low + "," + high;
                        stackText.post(() -> stackText.setText(temp));
                        leftP = low;
                        rightP = high;
                        pivotI = new Random().nextInt(high - low) + low;
                        pivot = numbers.get(pivotI);
                        isFirst1[0] = false;
                    }
                    buttons.get(pivotI).setBackgroundColor(context.getResources().getColor(R.color.light_blue_600));
                    if (delay[0]){
                        handler.postDelayed(this, getSpeed() / 2);
                        delay[0] = false;
                        return;
                    }
                    if (!isSwap[0]){
                        swap(pivotI, high);
                        isSwap[0] = true;
                        delay[0] = true;
                    }
                    if (delay[0]){
                        handler.postDelayed(this, getSpeed() / 2);
                        delay[0] = false;
                        return;
                    }
                    buttons.get(pivotI).setBackgroundColor(context.getResources().getColor(R.color.white));
                    buttons.get(high).setBackgroundColor(context.getResources().getColor(R.color.white));
                    stack.pop();
                    delay[0] = true;
                    isFirst[0] = false;

                    for (int i = low; i < high; i++){
                        buttons.get(i).setBackgroundColor(context.getResources().getColor(R.color.grey));
                    }
                }
                if (leftP > 0){
                    buttons.get(leftP - 1).setBackgroundColor(context.getResources().getColor(R.color.grey));
                }
                if (rightP < numbers.size() - 1 && rightP+1 != high){
                    buttons.get(rightP + 1).setBackgroundColor(context.getResources().getColor(R.color.grey));
                }
                if (high != numbers.size() - 1){
                    buttons.get(high + 1).setBackgroundColor(context.getResources().getColor(R.color.white));
                }
                buttons.get(tempH).setBackgroundColor(context.getResources().getColor(R.color.light_blue_600));
                buttons.get(leftP).setBackgroundColor(context.getResources().getColor(R.color.red));
                buttons.get(rightP).setBackgroundColor(context.getResources().getColor(R.color.purple_200));
                if (leftP < rightP){
                    if (numbers.get(leftP) <= pivot){
                        leftP++;
                        handler.postDelayed(this, getSpeed());
                        return;
                    }
                    if (numbers.get(rightP) >= pivot){
                        rightP--;
                        handler.postDelayed(this, getSpeed());
                        return;
                    }
                    swap(leftP, rightP);
                    handler.postDelayed(this, getSpeed());
                    return;
                }
                swap(leftP, high);

                for (int i = 0; i < numbers.size(); i++){
                    buttons.get(i).setBackgroundColor(context.getResources().getColor(R.color.white));
                }

                if (leftP-1 > low){
                    stack.push(new Pair<>(low, leftP - 1));
                    String temp = low + "," + (leftP - 1);
                    stackText.post(() -> stackText.setText(temp));
                }
                if (leftP+1 < high){
                    stack.push(new Pair<>(leftP + 1, high));
                    String temp = (leftP + 1) + "," + high;
                    stackText.post(() -> stackText.setText(temp));
                }

                if (stack.empty()){
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
                    return;
                }
                delay[0] = true;
                isFirst[0] = true;
                isFirst1[0] = true;
                isSwap[0] = false;
                handler.postDelayed(this, 1);
            }
        };
        return runnable;
    }
    public void stop(){
        handler.removeCallbacks(runnable);
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
}
