package com.example.dsavisualizer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import java.util.ArrayList;
import java.util.Random;

import com.example.dsavisualizer.sortingAlgorithms.insertionSort;
import com.example.dsavisualizer.sortingAlgorithms.quickSort;
import com.example.dsavisualizer.sortingAlgorithms.selectionSort;
import com.example.dsavisualizer.sortingAlgorithms.bubbleSort;

public class sortingAlgorithmMain extends AppCompatActivity {

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
    private quickSort quickSort;
    private insertionSort insertionSort;
    private bubbleSort bubbleSort;
    private selectionSort selectionSort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorting_algorithms);

        intent = getIntent();

        storage storage = new storage(this);
        buttons.addAll(storage.getButtons());
        layouts.addAll(storage.getLayouts());


        ImageView add = findViewById(R.id.add);
        ImageView shuffle = findViewById(R.id.shuffle);
        ImageView play = findViewById(R.id.play);
        ImageView autoAdd = findViewById(R.id.addRandom);
        ImageView deleteAll = findViewById(R.id.deleteAll);
        ImageView speed = findViewById(R.id.speed);
        TextView speedText = findViewById(R.id.speedText);
        TextView stackText = findViewById(R.id.stack);
        TextView title = findViewById(R.id.title);

        TextView greenColor = findViewById(R.id.greenColor);
        TextView greenText = findViewById(R.id.greenText);

        TextView greyColor = findViewById(R.id.greyColor);
        TextView greyText = findViewById(R.id.greyText);

        TextView purpleColor = findViewById(R.id.purpleColor);
        TextView purpleText =findViewById(R.id.purpleText);

        TextView redColor = findViewById(R.id.redColor);
        TextView redText = findViewById(R.id.redText);

        TextView blueColor = findViewById(R.id.blueColor);
        TextView blueText = findViewById(R.id.blueText);

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
        });
        add.setOnClickListener(v -> {
            String temp = inputBox.getText().toString();
            if (!isValid(temp)){
                return;
            }
            int number = Integer.parseInt(temp);

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
                Log.e("numbers", "onClick: " + numbers);
            }
        });

        play.setOnClickListener(v -> {
            if (count < 10){
                Toast.makeText(getApplicationContext(), "please add at least 10 inputs", Toast.LENGTH_SHORT).show();
                return;
            }
            purpleColor.setBackgroundColor(getResources().getColor(R.color.light_blue_900));
            purpleText.setText(R.string.current_element);

            greyColor.setBackgroundColor(getResources().getColor(R.color.green));
            greyText.setText(R.string.green);

            greenText.setVisibility(View.INVISIBLE);
            greenColor.setVisibility(View.INVISIBLE);

            blueColor.setVisibility(View.INVISIBLE);
            blueText.setVisibility(View.INVISIBLE);

            switch (intent.getStringExtra("key")){
                case "Selection sort":
                    selectionSort = new selectionSort(numbers,buttons,getApplicationContext(), sortingAlgorithmMain.this, curSpeed);
                    runnable = selectionSort.play();
                    handler.post(runnable);
                    break;

                case "Bubble sort":

                    bubbleSort = new bubbleSort(numbers,buttons,getApplicationContext(), sortingAlgorithmMain.this, curSpeed);
                    runnable = bubbleSort.play();
                    handler.post(runnable);
                    break;

                case "Insertion sort":
                    insertionSort = new insertionSort(numbers,buttons,getApplicationContext(), sortingAlgorithmMain.this, curSpeed);
                    runnable = insertionSort.play();
                    handler.post(runnable);
                    break;

                case "Quick sort":
                    purpleColor.setBackgroundColor(getResources().getColor(R.color.light_blue_900));
                    purpleText.setText(R.string.pivot);

                    blueText.setText(greenText.getText());
                    blueColor.setBackgroundColor(getResources().getColor(R.color.green));

                    greenColor.setBackgroundColor(getResources().getColor(R.color.purple_200));
                    greenText.setText(R.string.right_pointer);

                    redText.setText(R.string.left_pointer);

                    title.setVisibility(View.VISIBLE);
                    stackText.setVisibility(View.VISIBLE);

                    quickSort = new quickSort(numbers,buttons,getApplicationContext(), sortingAlgorithmMain.this, curSpeed);
                    runnable = quickSort.play();
                    handler.post(runnable);
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
        AlertDialog alertDialog = new AlertDialog.Builder(sortingAlgorithmMain.this)
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
            case "Selection sort":
                selectionSort.stop();
                break;

            case "Bubble sort":
                bubbleSort.stop();
                break;

            case "Insertion sort":
                insertionSort.stop();
                break;

            case "Quick sort":
                quickSort.stop();
                break;

            default:
                break;
        }
    }
}