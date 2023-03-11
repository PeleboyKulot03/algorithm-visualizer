package com.example.dsavisualizer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

public class listPage extends AppCompatActivity {
    RecyclerView recyclerView;
    algorithms algorithms;

    String title = "";

    listPageAdapter adapter;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_page);
        recyclerView = findViewById(R.id.recyclerView);
        algorithms = new algorithms();

        ArrayList<helper> algs = new ArrayList<>();

        Intent intent = getIntent();
        title = intent.getStringExtra("alg");

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(title);

        MenuItem search = toolbar.getMenu().findItem(R.id.search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setQueryHint("Search here...");
        searchView.setIconifiedByDefault(true);
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.black));
        searchEditText.setHintTextColor(getResources().getColor(R.color.black));
        searchEditText.setBackground(getResources().getDrawable(R.drawable.round_edit_text));

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 0) {
                    adapter.changeNewItem(algs);
                    return;
                }
                ArrayList<helper> newHelper = new ArrayList<>();

                for (helper alg: algs) {
                    if (alg.getTitle().toLowerCase().contains(s.toString().toLowerCase())){
                        newHelper.add(alg);
                    }
                }

                adapter.changeNewItem(newHelper);
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        if (title.equals(getResources().getString(R.string.search_alg))){
            algs.addAll(algorithms.getSearchAlg());
            adapter = new listPageAdapter(algorithms.getSearchAlg(), listPage.this, title);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }
        if (title.equals(getResources().getString(R.string.sorting_alg))){
            algs.addAll(algorithms.getSortAlg());
            adapter = new listPageAdapter(algorithms.getSortAlg(), listPage.this, title);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }
        if (title.equals(getResources().getString(R.string.path_finding_alg))){
            algs.addAll(algorithms.getPathFindingAlg());
            adapter = new listPageAdapter(algorithms.getPathFindingAlg(), listPage.this, title);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }


    }
}