package com.example.dsavisualizer.sortingAlgorithms;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dsavisualizer.R;
import com.example.dsavisualizer.storage;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class mergeSort extends AppCompatActivity {

    private final Random random = new Random();
    private final ArrayList<Button> buttons = new ArrayList<>();
    private final ArrayList<LinearLayout> layouts = new ArrayList<>();
    private final ArrayList<Integer> numbers = new ArrayList<>();
    private final ArrayList<TextView> leftTextview = new ArrayList<>();
    private final ArrayList<TextView> rightTextview = new ArrayList<>();
    private final ArrayList<Integer> leftArray = new ArrayList<>();
    private final ArrayList<Integer> rightArray = new ArrayList<>();

    private int count = 0;
    private EditText inputBox;
    private LinearLayout barBox;
    private int curSpeed = 3;
    private Handler handler;
    private Runnable runnable;
    private boolean isRunning;
    private boolean back;
    private final Stack<Pair<Integer, Integer>> stack = new Stack<>();
    private final Stack<Pair<Integer, Integer>> stackFrame = new Stack<>();
    private int left;
    private int right;
    private int mid;
    private int i = 0;
    private int j = 0;
    private int k = 0;
    private RelativeLayout legends;
    private TextView stackText;
    private TextView speedText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merge_sort);

        storage storage = new storage(this);
        buttons.addAll(storage.getButtons());
        layouts.addAll(storage.getLayouts());
        leftTextview.addAll(storage.getTempLeft());
        rightTextview.addAll(storage.getTempRight());

        stackText = findViewById(R.id.stack);
        ImageView add = findViewById(R.id.add);
        ImageView shuffle = findViewById(R.id.shuffle);
        ImageView play = findViewById(R.id.play);
        ImageView autoAdd = findViewById(R.id.addRandom);
        ImageView deleteAll = findViewById(R.id.deleteAll);
        ImageView speed = findViewById(R.id.speed);
        speedText = findViewById(R.id.speedText);
        legends = findViewById(R.id.legends);
        inputBox = findViewById(R.id.inputBox);
        barBox = findViewById(R.id.barBox);
        ViewTreeObserver viewTreeObserver = barBox.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    barBox.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    alert(barBox.getHeight() - 72);
                }
            });
        }

        speed.setOnClickListener(v -> {
            if (curSpeed == 5){
                curSpeed = 1;
                speedText.setText(String.valueOf(curSpeed));
                return;
            }
            curSpeed++;
            speedText.setText(String.valueOf(curSpeed));
        });



        deleteAll.setOnClickListener(v -> {
            for (int i = 0; i < buttons.size(); i++){
                LinearLayout layout = layouts.get(i);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
                layoutParams.weight = 0;
                layout.setLayoutParams(layoutParams);
            }
            numbers.clear();
            count = 0;
        });
        autoAdd.setOnClickListener(v -> {
            for (int i = count; i < buttons.size(); i++){
                int rand = random.nextInt(barBox.getMeasuredHeight() - 72);
                Button button = buttons.get(count);
                LinearLayout layout = layouts.get(count);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
                ViewGroup.LayoutParams buttonParams = button.getLayoutParams();
                buttonParams.height = rand + 50;
                layoutParams.weight = 1;
                button.setText(String.valueOf(rand));
                layout.setLayoutParams(layoutParams);
                button.setLayoutParams(buttonParams);
                numbers.add(rand);
                count++;
            }
        });
        add.setOnClickListener(v -> {
            String temp = inputBox.getText().toString();
            if (!isValid(temp)){
                return;
            }
            int number = Integer.parseInt(temp);

            if (count != 49){
                Button button = buttons.get(count);
                LinearLayout layout = layouts.get(count);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
                ViewGroup.LayoutParams buttonParams = button.getLayoutParams();

                buttonParams.height = number + 50;
                layoutParams.weight = 1;
                button.setText(temp);
                layout.setLayoutParams(layoutParams);
                button.setLayoutParams(buttonParams);
                numbers.add(number);
                count++;
                return;
            }
            Toast.makeText(getApplicationContext(), "Maximum capacity is acquired", Toast.LENGTH_SHORT).show();
        });

        shuffle.setOnClickListener(v -> {
            ArrayList<Integer> temp = new ArrayList<>(numbers);
            for (int i = 0; i < temp.size(); i++){
                Button button = buttons.get(i);
                int rand = random.nextInt(count);

                //to swap height in display
                ViewGroup.LayoutParams params = button.getLayoutParams();
                ViewGroup.LayoutParams params2 = buttons.get(rand).getLayoutParams();

                params.height = numbers.get(rand) + 50;
                params2.height = numbers.get(i) + 50;
                button.setLayoutParams(params);
                buttons.get(rand).setLayoutParams(params2);
                String tempStr = String.valueOf(button.getText());
                button.setText(buttons.get(rand).getText());
                buttons.get(rand).setText(tempStr);

                //to swap values in number arraylist
                numbers.set(i, Integer.valueOf(button.getText().toString()));
                numbers.set(rand, Integer.valueOf(buttons.get(rand).getText().toString()));
                Log.e("numbers", "onClick: " + numbers);
            }
        });

        play.setOnClickListener(v -> {
            if (count < 10){
                Toast.makeText(getApplicationContext(), "please add at least 1000 inputs", Toast.LENGTH_SHORT).show();
                return;
            }
            shuffle.setVisibility(View.INVISIBLE);

            deleteAll.setVisibility(View.INVISIBLE);

            autoAdd.setVisibility(View.INVISIBLE);

            add.setVisibility(View.INVISIBLE);

            speed.setVisibility(View.INVISIBLE);

            play.setVisibility(View.INVISIBLE);

            speedText.setVisibility(View.INVISIBLE);

            stack.push(new Pair<>(0, numbers.size()-1));

            legends.setVisibility(View.VISIBLE);
            isRunning = true;
            runnable = execute();
            handler.post(runnable);
        });
    }

    private Runnable execute(){
        final boolean[] isFirst = {true};
        final boolean[] isFirst1 = {true};
        final boolean[] isDividing = {true};
        final boolean[] delay = {true};

        handler = new Handler();
        return new Runnable() {
            @Override
            public void run() {

                //dividing phase acts like recursion
                if (isDividing[0]){
                    left = stack.peek().first;
                    right = stack.peek().second;
                    if ((!stackFrame.isEmpty()) || isFirst[0]){
                        if (right - left <= 1){
                            left = stackFrame.peek().first;
                            right = stackFrame.peek().second;
                            mid = left + (right - left) / 2;

                            String temp = left + "," + right;
                            //give some nice color
                            if (delay[0]){
                                stackText.post(()-> stackText.setText(temp));
                                stackText.setBackgroundColor(getResources().getColor(R.color.green));
                                for (int i = left; i <= right; i++){
                                    buttons.get(i).setBackgroundColor(getResources().getColor(R.color.light_blue_900));
                                }
                                buttons.get(mid).setBackgroundColor(getResources().getColor(R.color.purple_200));
                                handler.removeCallbacks(this);
                                handler.postDelayed(this, getSpeed());
                                delay[0] = false;
                                return;
                            }
                            //remove nice color
                            for (int i = left; i <= right; i++){
                                buttons.get(i).setBackgroundColor(getResources().getColor(R.color.white));
                            }
                            stackText.setBackgroundColor(getResources().getColor(R.color.white));
                            stack.push(new Pair<>(left, right));
                            stackFrame.pop();
                        }
                        String temp = left + "," + right;
                        mid = left + (right - left) / 2;
                        //give some nice color
                        if (delay[0]){
                            stackText.post(()-> stackText.setText(temp));
                            stackText.setBackgroundColor(getResources().getColor(R.color.green));
                            for (int i = left; i <= right; i++){
                                buttons.get(i).setBackgroundColor(getResources().getColor(R.color.light_blue_900));
                            }
                            buttons.get(mid).setBackgroundColor(getResources().getColor(R.color.purple_200));
                            handler.removeCallbacks(this);
                            handler.postDelayed(this, getSpeed());
                            delay[0] = false;
                            return;
                        }
                        //remove nice color
                        for (int i = left; i <= right; i++){
                            buttons.get(i).setBackgroundColor(getResources().getColor(R.color.white));
                        }
                        stackText.setBackgroundColor(getResources().getColor(R.color.white));

                        if (left < mid){
                            stack.push(new Pair<>(left, mid));
                        }
                        if (mid + 1 < right){
                            stackFrame.push(new Pair<>(mid + 1, right));
                        }
                        isFirst[0] = false;
                        delay[0] = true;
                        handler.removeCallbacks(this);
                        handler.postDelayed(this, getSpeed());
                        return;
                    }
                    isFirst[0] = true;
                    isDividing[0] = false;
                    Log.e("TAG", String.valueOf(stack));
                }

                //outer loop
                if (isFirst[0]){
                    if (isFirst1[0]){
                        left = stack.peek().first;
                        right = stack.peek().second;
                        String temp = left + "," + right;
                        stackText.post(()-> stackText.setText(temp));
                        mid = left + (right - left) / 2;
                        k = left;
                        i = 0;
                        j = 0;
                        for (int i = left; i <= right; i++){
                            buttons.get(i).setBackgroundColor(getResources().getColor(R.color.grey));
                        }
                        buttons.get(mid).setBackgroundColor(getResources().getColor(R.color.purple_200));
                        isFirst1[0] = false;
                    }
                    if (i < (mid - left) + 1){
                        leftArray.add(numbers.get(left + i));
                        TextView temp = leftTextview.get(i);
                        LinearLayout.LayoutParams textParams  = (LinearLayout.LayoutParams) temp.getLayoutParams();
                        temp.setText(String.valueOf(numbers.get(left + i)));
                        textParams.weight = 1;
                        temp.setLayoutParams(textParams);
                        i++;
                        handler.removeCallbacks(this);
                        handler.postDelayed(this, getSpeed());
                        return;
                    }

                    if (j < right - mid){
                        rightArray.add(numbers.get(mid + 1 + j));
                        TextView temp = rightTextview.get(j);
                        LinearLayout.LayoutParams textParams  = (LinearLayout.LayoutParams) temp.getLayoutParams();
                        temp.setText(String.valueOf(numbers.get(mid + 1 + j)));
                        textParams.weight = 1;
                        temp.setLayoutParams(textParams);
                        j++;
                        handler.removeCallbacks(this);
                        handler.postDelayed(this, getSpeed());
                        return;
                    }
                    handler.postDelayed(this, 1);
                    i = 0;
                    j = 0;
                    isFirst[0] = false;
                    for (int i = left; i <= right; i++){
                        buttons.get(i).setBackgroundColor(getResources().getColor(R.color.grey));
                    }
                }
                Log.e("TAG", "run: 1");
                if (k != 0 && k > left){
                    buttons.get(k - 1).setBackgroundColor(getResources().getColor(R.color.grey));
                }
                if (i != 0 && i < 25){
                    leftTextview.get(i - 1).setBackgroundColor(getResources().getColor(R.color.white));
                }
                if (j != 0 && j < 25){
                    rightTextview.get(j - 1).setBackgroundColor(getResources().getColor(R.color.white));
                }
                if (i == leftArray.size() - 1){
                    rightTextview.get(i).setBackgroundColor(getResources().getColor(R.color.white));
                }

                if (j == rightArray.size() - 1){
                    rightTextview.get(j).setBackgroundColor(getResources().getColor(R.color.white));
                }
                if (i < mid - left + 1 && j < right - mid){
                    TextView leftT = leftTextview.get(i);
                    TextView rightT = rightTextview.get(j);

                    leftT.setBackgroundColor(getResources().getColor(R.color.red));
                    rightT.setBackgroundColor(getResources().getColor(R.color.red));
                    if (leftArray.get(i) <=  rightArray.get(j)){
                        runOnUiThread(() -> {
                            //change the value in the final array
                            numbers.set(k, leftArray.get(i));

                            //match the height
                            Button tempB = buttons.get(k);
                            tempB.setBackgroundColor(getResources().getColor(R.color.green));
                            ViewGroup.LayoutParams button = buttons.get(k).getLayoutParams();
                            button.height = leftArray.get(i) + 50;
                            tempB.setLayoutParams(button);
                            tempB.setText(String.valueOf(leftArray.get(i)));

                            tempB.animate().translationY((tempB.getWidth() * 2) - ((tempB.getWidth() * 2) / 2f)).withEndAction(() -> tempB.animate().translationY(0).withEndAction(() -> {
                            }).start()).start();

                            //elevate the selected buttons
                            leftT.animate().translationY((-leftT.getWidth() * 2) + ((leftT.getWidth() * 2) / 2f)).withEndAction(() -> leftT.animate().translationY(0).withEndAction(() -> {

                            }).start()).start();
                            rightT.animate().translationY((rightT.getWidth() * 2) - ((rightT.getWidth() * 2) / 2f)).withEndAction(() -> rightT.animate().translationY(0).withEndAction(() -> {
                            }).start()).start();
                            leftT.setBackgroundColor(getResources().getColor(R.color.green));
                        });
                        i++;
                        k++;
                        handler.removeCallbacks(this);
                        handler.postDelayed(this, getSpeed());
                        return;
                    }
                    runOnUiThread(() -> {
                        //change the value in the final array
                        numbers.set(k, rightArray.get(j));

                        //match the height
                        Button tempB = buttons.get(k);
                        tempB.setBackgroundColor(getResources().getColor(R.color.green));
                        ViewGroup.LayoutParams button = buttons.get(k).getLayoutParams();
                        button.height = rightArray.get(j) + 50;
                        tempB.setLayoutParams(button);
                        tempB.setText(String.valueOf(rightArray.get(j)));
                        tempB.animate().translationY((tempB.getWidth() * 2) - ((tempB.getWidth() * 2) / 2f)).withEndAction(() -> tempB.animate().translationY(0).withEndAction(() -> {
                        }).start()).start();
                        //elevate the selected buttons
                        leftT.animate().translationY((-leftT.getWidth() * 2) + ((leftT.getWidth() * 2) / 2f)).withEndAction(() -> leftT.animate().translationY(0).withEndAction(() -> {

                        }).start()).start();
                        rightT.animate().translationY((rightT.getWidth() * 2) - ((rightT.getWidth() * 2) / 2f)).withEndAction(() -> rightT.animate().translationY(0).withEndAction(() -> {
                        }).start()).start();
                        rightT.setBackgroundColor(getResources().getColor(R.color.green));
                    });
                    j++;
                    k++;
                    handler.removeCallbacks(this);
                    handler.postDelayed(this, getSpeed());
                    return;
                }
                if (i < mid - left + 1){
                    TextView leftT = leftTextview.get(i);
                    leftT.setBackgroundColor(getResources().getColor(R.color.green));
                    runOnUiThread(() -> {
                        //change the value in the final array
                        numbers.set(k, leftArray.get(i));

                        //match the height
                        Button tempB = buttons.get(k);

                        ViewGroup.LayoutParams button = buttons.get(k).getLayoutParams();
                        button.height = leftArray.get(i) + 50;
                        tempB.setLayoutParams(button);
                        tempB.setText(String.valueOf(leftArray.get(i)));
                        tempB.setBackgroundColor(getResources().getColor(R.color.green));
                        tempB.animate().translationY((tempB.getWidth() * 2) - ((tempB.getWidth() * 2) / 2f)).withEndAction(() -> tempB.animate().translationY(0).withEndAction(() -> {
                        }).start()).start();

                        //elevate the selected buttons
                        leftT.animate().translationY((-leftT.getWidth() * 2) + ((leftT.getWidth() * 2) / 2f)).withEndAction(() -> leftT.animate().translationY(0).withEndAction(() -> {
                        }).start()).start();

                    });
                    i++;
                    k++;
                    handler.removeCallbacks(this);
                    handler.postDelayed(this, getSpeed());
                    return;
                }
                if (j < right - mid){
                    TextView rightT = rightTextview.get(j);
                    rightT.setBackgroundColor(getResources().getColor(R.color.green));
                    runOnUiThread(() -> {
                        //change the value in the final array
                        numbers.set(k, rightArray.get(j));

                        //match the height
                        Button tempB = buttons.get(k);

                        ViewGroup.LayoutParams button = buttons.get(k).getLayoutParams();
                        button.height = rightArray.get(j) + 50;
                        tempB.setLayoutParams(button);
                        tempB.setText(String.valueOf(rightArray.get(j)));

                        tempB.setBackgroundColor(getResources().getColor(R.color.green));
                        tempB.animate().translationY((tempB.getWidth() * 2) - ((tempB.getWidth() * 2) / 2f)).withEndAction(() -> tempB.animate().translationY(0).withEndAction(() -> {
                        }).start()).start();
                        //elevate the selected buttons
                        rightT.animate().translationY((-rightT.getWidth() * 2) + ((rightT.getWidth() * 2) / 2f)).withEndAction(() -> rightT.animate().translationY(0).withEndAction(() -> {
                        }).start()).start();
                    });
                    j++;
                    k++;
                    handler.removeCallbacks(this);
                    handler.postDelayed(this, getSpeed());
                    return;
                }
                stack.pop();
                if (stack.isEmpty()){
                    Log.e("tag", String.valueOf(numbers));
                    handler.removeCallbacks(this);
                    return;
                }
                leftArray.clear();
                rightArray.clear();
                isFirst[0] = true;
                isFirst1[0] = true;
                count = 0;
                i = 0;
                j = 0;
                for (int i = left; i <= right; i++){
                    buttons.get(i).setBackgroundColor(getResources().getColor(R.color.white));
                }
                int temp = 0;
                while (temp < 25){
                    TextView leftT= leftTextview.get(temp);
                    TextView rightT= rightTextview.get(temp);
                    LinearLayout.LayoutParams leftP  = (LinearLayout.LayoutParams) leftT.getLayoutParams();
                    LinearLayout.LayoutParams rightP  = (LinearLayout.LayoutParams) rightT.getLayoutParams();
                    leftP.weight = 0;
                    rightP.weight = 0;
                    leftT.setLayoutParams(leftP);
                    rightT.setLayoutParams(rightP);
                    temp++;
                }
                handler.removeCallbacks(this);
                handler.postDelayed(this, getSpeed());
            }
        };
    }
    private void alert(int height) {
        AlertDialog alertDialog = new AlertDialog.Builder(mergeSort.this)
                .setTitle("Caution")
                .setMessage("The numbers that can be inputted is limited to " + height + " to visualize the algorithm properly")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss()).create();
        alertDialog.show();
    }

    private boolean isValid(String text) {
        if (text.equals("")){
            inputBox.setError("Cannot be null or empty!");
            return false;
        }
        if (Integer.parseInt(text) > barBox.getHeight() - 72){
            inputBox.setError("Inputs are limited to " + (barBox.getHeight() - 72) + " to visualize the algorithm properly");
            return false;
        }

        return true;
    }
    private int getSpeed() {
        int speed = Integer.parseInt(speedText.getText().toString());
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
    @Override
    public void onBackPressed() {
        if (isRunning){
            Toast.makeText(getApplicationContext(), "press again to exit", Toast.LENGTH_SHORT).show();
            if (back){
                handler.removeCallbacks(runnable);
                super.onBackPressed();
                return;
            }
            back = true;
            Handler time = new Handler();
            time.postDelayed(()-> back = false, 3000);
            return;
        }
        super.onBackPressed();
    }


}