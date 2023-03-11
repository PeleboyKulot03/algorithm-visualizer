package com.example.dsavisualizer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
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

import com.example.dsavisualizer.searchingAlgorithms.binarySearch;
import com.example.dsavisualizer.searchingAlgorithms.jumpSearch;
import com.example.dsavisualizer.searchingAlgorithms.linearSearch;
import com.example.dsavisualizer.searchingAlgorithms.exponentialSearch;
import com.example.dsavisualizer.searchingAlgorithms.interpolationSearch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class searchingAlgorithmMain extends AppCompatActivity {
    private final Random random = new Random();
    private final ArrayList<Button> buttons = new ArrayList<>();
    private final ArrayList<LinearLayout> layouts = new ArrayList<>();
    private final ArrayList<Integer> numbers = new ArrayList<>();
    private int count = 0;
    private EditText inputBox;
    private LinearLayout barBox;
    private Intent intent;
    private int curSpeed = 3;
    private final Handler handler = new Handler();
    private Runnable runnable;
    private boolean isRunning;
    private boolean back;
    private int target = -1;
    private linearSearch linearSearch;
    private ImageView play;
    private boolean hasTarget;
    private binarySearch binarySearch;
    private jumpSearch jumpSearch;
    private exponentialSearch exponentialSearch;
    private interpolationSearch interpolationSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching_algorithm);
        intent = getIntent();

        storage storage = new storage(this);
        buttons.addAll(storage.getButtons());
        layouts.addAll(storage.getLayouts());

        ImageView add = findViewById(R.id.add);
        ImageView shuffle = findViewById(R.id.shuffle);
        play = findViewById(R.id.play);

        LinearLayout purple = findViewById(R.id.purple);
        LinearLayout red = findViewById(R.id.red);
        LinearLayout grey = findViewById(R.id.grey);

        ImageView autoAdd = findViewById(R.id.addRandom);
        ImageView deleteAll = findViewById(R.id.deleteAll);
        ImageView speed = findViewById(R.id.speed);
        TextView speedText = findViewById(R.id.speedText);

        TextView stackText = findViewById(R.id.stack);

        TextView greyText = findViewById(R.id.greyText);

        TextView purpleColor = findViewById(R.id.purpleColor);
        TextView purpleText =findViewById(R.id.purpleText);

        TextView redText = findViewById(R.id.redText);


        if (!intent.getStringExtra("key").equals("Linear Search")){
            shuffle.setEnabled(false);
            shuffle.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
        }

        RelativeLayout legends = findViewById(R.id.legends);

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
            if (!intent.getStringExtra("key").equals("Linear Search")) {
                Collections.sort(numbers);
                for (int i = 0; i < buttons.size(); i++){
                    Button button = buttons.get(i);
                    LinearLayout layout = layouts.get(i);
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
                    ViewGroup.LayoutParams buttonParams = button.getLayoutParams();
                    buttonParams.height = numbers.get(i) + 30;
                    button.setText(String.valueOf(numbers.get(i)));
                    layout.setLayoutParams(layoutParams);
                    button.setLayoutParams(buttonParams);
                }
            }
        });
        add.setOnClickListener(v -> {
            String temp = inputBox.getText().toString();
            if (isValid(temp)){
                return;
            }
            int number = Integer.parseInt(temp);

            if (!intent.getStringExtra("key").equals("Linear Search")){
                if (count != 0) {
                    if (number < numbers.get(count - 1)){
                        inputBox.setError("Numbers are required to be sorted in order to use the algorithm");
                        return;
                    }
                }
            }

            if (count < 50){
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
            }
        });

        play.setOnClickListener(v -> {
            if (count < 10){
                Toast.makeText(getApplicationContext(), "please add at least 10 inputs", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!hasTarget){
                inputBox.setText("");
                hasTarget = true;
                play.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);
                inputBox.setHint("Type the target here");
                Toast.makeText(getApplicationContext(), "Add the target", Toast.LENGTH_SHORT).show();

                autoAdd.setEnabled(false);
                autoAdd.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));

                shuffle.setEnabled(false);
                shuffle.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));

                deleteAll.setEnabled(false);
                deleteAll.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));

                add.setEnabled(false);
                add.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));

                return;
            }

            if (isValid(String.valueOf(target))){
                return;
            }
            target = Integer.parseInt(inputBox.getText().toString());
            legends.setVisibility(View.VISIBLE);

            switch (intent.getStringExtra("key")){
                case "Linear Search":
                    linearSearch = new linearSearch(numbers, buttons, getApplicationContext(), searchingAlgorithmMain.this, curSpeed, target);
                    runnable = linearSearch.play();
                    handler.post(runnable);
                    purple.setVisibility(View.VISIBLE);
                    purpleText.setText(R.string.current_element);
                    purpleColor.setBackgroundColor(getResources().getColor(R.color.light_blue_900));
                    stackText.setText(String.valueOf(target));
                    red.setVisibility(View.VISIBLE);
                    redText.setText(R.string.found_target);
                    break;

                case "Binary Search":
                    binarySearch = new binarySearch(numbers, buttons, getApplicationContext(), searchingAlgorithmMain.this, curSpeed, target);
                    runnable = binarySearch.play();
                    handler.post(runnable);

                    purple.setVisibility(View.VISIBLE);
                    purpleText.setText(R.string.current_element);
                    purpleColor.setBackgroundColor(getResources().getColor(R.color.light_blue_900));
                    stackText.setText(String.valueOf(target));

                    red.setVisibility(View.VISIBLE);
                    redText.setText(R.string.found_target);

                    grey.setVisibility(View.VISIBLE);
                    greyText.setText(R.string.divided);

                    break;

                case "Jump Search":
                    jumpSearch = new jumpSearch(numbers, buttons, getApplicationContext(), searchingAlgorithmMain.this, curSpeed, target);
                    runnable = jumpSearch.play();
                    handler.post(runnable);

                    purple.setVisibility(View.VISIBLE);
                    purpleText.setText(R.string.current_element);
                    purpleColor.setBackgroundColor(getResources().getColor(R.color.light_blue_900));
                    stackText.setText(String.valueOf(target));

                    red.setVisibility(View.VISIBLE);
                    redText.setText(R.string.found_target);

                    grey.setVisibility(View.VISIBLE);
                    greyText.setText(R.string.divided);
                    break;

                case "Interpolation Search":
                    interpolationSearch = new interpolationSearch(numbers, buttons, getApplicationContext(), searchingAlgorithmMain.this, curSpeed, target);
                    runnable = interpolationSearch.play();
                    handler.post(runnable);

                    purple.setVisibility(View.VISIBLE);
                    purpleText.setText(R.string.current_element);
                    purpleColor.setBackgroundColor(getResources().getColor(R.color.light_blue_900));
                    stackText.setText(String.valueOf(target));

                    red.setVisibility(View.VISIBLE);
                    redText.setText(R.string.found_target);

                    grey.setVisibility(View.VISIBLE);
                    greyText.setText(R.string.divided);
                    break;

                case "Exponential Search":
                    exponentialSearch = new exponentialSearch(numbers, buttons, getApplicationContext(), searchingAlgorithmMain.this, curSpeed, target);
                    runnable = exponentialSearch.play();
                    handler.post(runnable);

                    purple.setVisibility(View.VISIBLE);
                    purpleText.setText(R.string.current_element);
                    purpleColor.setBackgroundColor(getResources().getColor(R.color.light_blue_900));
                    stackText.setText(String.valueOf(target));

                    red.setVisibility(View.VISIBLE);
                    redText.setText(R.string.found_target);

                    grey.setVisibility(View.VISIBLE);
                    greyText.setText(R.string.divided);
                    break;

                default:
                    break;
            }
            shuffle.setVisibility(View.INVISIBLE);

            deleteAll.setVisibility(View.INVISIBLE);

            autoAdd.setVisibility(View.INVISIBLE);

            add.setVisibility(View.INVISIBLE);

            speed.setVisibility(View.INVISIBLE);

            play.setVisibility(View.INVISIBLE);

            speedText.setVisibility(View.INVISIBLE);

            legends.setVisibility(View.VISIBLE);

            isRunning = true;
        });
    }

    private void alert(int height) {
        AlertDialog alertDialog = new AlertDialog.Builder(searchingAlgorithmMain.this)
                .setTitle("Caution")
                .setMessage("The numbers that can be inputted is limited to " + height + " to visualize the algorithm properly")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss()).create();
        alertDialog.show();
    }

    private boolean isValid(String text) {
        if (inputBox.getText().toString().isEmpty()){
            inputBox.setError("cannot be null or empty!");
            return true;
        }
        if (Integer.parseInt(text) > barBox.getHeight() - 72){
            inputBox.setError("Inputs are limited to " + (barBox.getHeight() - 72) + " to visualize the algorithm properly");
            return true;
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        if (isRunning){
            Toast.makeText(getApplicationContext(), "press again to exit", Toast.LENGTH_SHORT).show();
            if (back){
                killRunning();
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

    private void killRunning() {
        switch (intent.getStringExtra("key")){
            case "Linear Search":
                linearSearch.stop();
                break;

            case "Binary Search":
                binarySearch.stop();

            case "Jump Search":
                jumpSearch.stop();

            case "Interpolation Search":
                interpolationSearch.stop();

            case "Exponential Search":
                exponentialSearch.stop();

            default:
                break;
        }
    }
}