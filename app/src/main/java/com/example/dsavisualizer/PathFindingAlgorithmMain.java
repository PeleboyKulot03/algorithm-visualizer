package com.example.dsavisualizer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dsavisualizer.pathFindingAlgorithm.BreadthFirstSearch;
import com.example.dsavisualizer.pathFindingAlgorithm.DepthFirstSearch;

import java.util.ArrayList;
import java.util.Objects;

public class PathFindingAlgorithmMain extends AppCompatActivity {
    private final ArrayList<Integer> ratio = new ArrayList<>();

    private int counter = 0;
    private String ratioTextString;

    private PathFindingAlgorithmMainAdapter adapter;

    private final Handler handler = new Handler();
    private Runnable runnable;
    private boolean isRunning;

    private boolean back;

    private int curSpeed = 3;
    private DepthFirstSearch depthFirstSearch;

    private BreadthFirstSearch breadthFirstSearch;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_finding_algorithms);
        intent = getIntent();

        ratio.add(3);
        ratio.add(6);
        ratio.add(9);
        ratio.add(12);
        ratio.add(15);

        LinearLayout violet = findViewById(R.id.path);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) violet.getLayoutParams();

        if (intent.getStringExtra("key").equals("Breadth-First Search")){
            layoutParams.weight = 1;
            violet.setLayoutParams(layoutParams);
        }
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        ImageView ratioImage = findViewById(R.id.ratioImage);
        ImageView speed = findViewById(R.id.speed);
        ImageView play = findViewById(R.id.play);
        TextView speedText = findViewById(R.id.speedText);
        ratioTextString = ratio.get(counter) + ":" + ratio.get(counter) + "  ";
        TextView ratioText = findViewById(R.id.ratioText);
        ratioText.setText(ratioTextString);

        speed.setOnClickListener(v -> {
            if (curSpeed == 5){
                curSpeed = 1;
                speedText.setText(String.valueOf(curSpeed));
                return;
            }
            curSpeed++;
            speedText.setText(String.valueOf(curSpeed));
        });

        adapter = new PathFindingAlgorithmMainAdapter(PathFindingAlgorithmMain.this, ratio.get(counter), ratio.get(counter));

        ratioImage.setOnClickListener(v -> {
            recyclerView.removeAllViews();
            counter++;
            if (counter == ratio.size()){
                counter = 0;
            }
            ratioTextString = ratio.get(counter) + ":" + ratio.get(counter);
            if (counter < 3) {
                ratioTextString += "  ";
            }
            ratioText.setText(ratioTextString);
            adapter = new PathFindingAlgorithmMainAdapter(PathFindingAlgorithmMain.this, ratio.get(counter), ratio.get(counter));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new GridLayoutManager(PathFindingAlgorithmMain.this, ratio.get(counter)));
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(PathFindingAlgorithmMain.this, ratio.get(counter)));

        play.setOnClickListener(v -> {
            int[][] map = covert2D();
            speed.setEnabled(false);
            speed.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));

            speed.setEnabled(false);
            speed.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));

            speedText.setEnabled(false);
            speedText.setTextColor(getResources().getColor(R.color.grey));

            play.setEnabled(false);
            play.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));

            ratioImage.setEnabled(false);
            ratioImage.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));

            ratioText.setEnabled(false);
            ratioText.setTextColor(getResources().getColor(R.color.grey));


            for (int i = 0; i < ratio.get(counter) * ratio.get(counter); i++){
                Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(i)).itemView.findViewById(R.id.block).setEnabled(false);
            }

            switch (intent.getStringExtra("key")){
                case "Breadth-First Search":

                    breadthFirstSearch = new BreadthFirstSearch(recyclerView, map, PathFindingAlgorithmMain.this, getApplicationContext(), curSpeed);
                    runnable = breadthFirstSearch.play();
                    handler.post(runnable);
                    break;

                case "Depth-First Search":
                    depthFirstSearch = new DepthFirstSearch(recyclerView, map, PathFindingAlgorithmMain.this, getApplicationContext(), curSpeed);
                    runnable = depthFirstSearch.play();
                    handler.post(runnable);
                    break;

                default:
                    break;
            }
            isRunning = true;

        });



    }

    public int[][] covert2D() {
        int counter1 = 0;

        int[][] map = new int[ratio.get(counter)][ratio.get(counter)];
        for (int i = 0; i < ratio.get(counter); i++){
            for (int j = 0; j < ratio.get(counter); j++){
                map[i][j] = globals.matrix[counter1];
                counter1++;
            }
        }

        return map;
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
            case "Breadth-First Search":

                break;

            case "Depth-First Search":
                depthFirstSearch.stop();

//            case "Jump Search":
//                jumpSearch.stop();
//
//            case "Interpolation Search":
//                interpolationSearch.stop();
//
//            case "Exponential Search":
//                exponentialSearch.stop();

            default:
                break;
        }
    }
}