package com.example.dsavisualizer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import java.util.ArrayList;


public class homePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        RelativeLayout parentView = findViewById(R.id.parent);

        CardView searchingCard = findViewById(R.id.searchingAlg);
        CardView sortingCard = findViewById(R.id.sortingAlg);
        CardView pathFindingCard = findViewById(R.id.pathFindingAlg);
//        CardView graphCard = findViewById(R.id.graphAlg);

        ArrayList<CardView> cards = new ArrayList<>();
        cards.add(searchingCard);
        cards.add(sortingCard);
        cards.add(pathFindingCard);
//        cards.add(graphCard);

        parentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                parentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int cardWidth = parentView.getWidth() / 2;
                for (CardView card: cards) {
                    ViewGroup.LayoutParams tempCard = card.getLayoutParams();
                    tempCard.width = cardWidth - 10;
                    card.setLayoutParams(tempCard);
                }
            }
        });

    }

    @SuppressLint("NonConstantResourceId")
    public void card(View view) {
        int id = view.getId();
        Intent intent = new Intent(getApplicationContext(), listPage.class);
        switch (id){

            case R.id.searchingAlg:
                intent.putExtra("alg", getString(R.string.search_alg));
                break;
            case R.id.sortingAlg:
                intent.putExtra("alg", getString(R.string.sorting_alg));
                break;
            case R.id.pathFindingAlg:
                intent.putExtra("alg", getString(R.string.path_finding_alg));
                break;
//            case R.id.graphAlg:
//                intent.putExtra("alg", getString(R.string.graph_theory));
//                break;

            default:
                break;
        }
        startActivity(intent);
    }
}