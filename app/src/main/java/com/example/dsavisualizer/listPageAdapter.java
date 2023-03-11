package com.example.dsavisualizer;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dsavisualizer.sortingAlgorithms.mergeSort;

import java.util.ArrayList;

public class listPageAdapter extends RecyclerView.Adapter<listPageAdapter.ViewHolder> {
    private final ArrayList<helper> list;
    private final Context context;
    private Intent intent;
    private final String alg;
    private final boolean[] isFlip;


    public listPageAdapter(ArrayList<helper> list, Context context, String alg) {
        this.list = list;
        this.context = context;
        this.alg = alg;
        isFlip = new boolean[list.size()];
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        helper helper = list.get(holder.getAdapterPosition());
        holder.title.setText(helper.getTitle());
        holder.average.setText(helper.getAveCom());
        holder.worst.setText(helper.getWorstCom());
        holder.best.setText(helper.getBestCom());
        holder.definitionTitle.setText(helper.getTitle());
        holder.description.setText(helper.getDescription());
        holder.card.setOnClickListener(v -> {
            if (!isFlip[holder.getAdapterPosition()]) {
                holder.card.animate().translationX(holder.card.getWidth()).withEndAction(() -> {
                    isFlip[holder.getAdapterPosition()] = true;
                    LinearLayout.LayoutParams shortDesParams = new LinearLayout.LayoutParams(holder.shortDescription.getLayoutParams());
                    LinearLayout.LayoutParams definitionParams = new LinearLayout.LayoutParams(holder.definition.getLayoutParams());
                    shortDesParams.weight = 0;
                    definitionParams.weight = 1;
                    holder.shortDescription.setLayoutParams(shortDesParams);
                    holder.definition.setLayoutParams(definitionParams);
                    holder.card.animate().translationX(0);
                });
                return;
            }

            holder.card.animate().translationX(-holder.card.getWidth()).withEndAction(() -> {
                LinearLayout.LayoutParams shortDesParams = new LinearLayout.LayoutParams(holder.shortDescription.getLayoutParams());
                LinearLayout.LayoutParams definitionParams = new LinearLayout.LayoutParams(holder.definition.getLayoutParams());
                shortDesParams.weight = 1;
                definitionParams.weight = 0;
                holder.shortDescription.setLayoutParams(shortDesParams);
                holder.definition.setLayoutParams(definitionParams);
                isFlip[holder.getAdapterPosition()] = false;
                holder.card.animate().translationX(0);
            });
        });
        holder.play.setOnClickListener(v -> {
            if (helper.getTitle().equals("Merge sort")) {
                intent = new Intent(context, mergeSort.class);
                context.startActivity(intent);
                return;
            }
            getActivity();
            intent.putExtra("key", helper.getTitle());
            context.startActivity(intent);
        });
    }

    private void getActivity() {
        switch (alg) {
            case "Sorting Algorithm":
                intent = new Intent(context, sortingAlgorithmMain.class);
                break;

            case "Searching Algorithm":
                intent = new Intent(context, searchingAlgorithmMain.class);
                break;

            case "Path Finding\nAlgorithms":
                intent = new Intent(context, PathFindingAlgorithmMain.class);
                break;

            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView shortDescription;
        private final CardView definition;
        private final TextView worst, title, best, average, definitionTitle, description;
        private final Button play;
        private final LinearLayout card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            worst = itemView.findViewById(R.id.worst);
            best = itemView.findViewById(R.id.best);
            average = itemView.findViewById(R.id.average);
            title = itemView.findViewById(R.id.title);
            card = itemView.findViewById(R.id.card);
            shortDescription = itemView.findViewById(R.id.shortDescription);
            definition = itemView.findViewById(R.id.definition);
            definitionTitle = itemView.findViewById(R.id.definitionTitle);
            description = itemView.findViewById(R.id.description);
            play = itemView.findViewById(R.id.play);
        }
    }
    public void changeNewItem(ArrayList<helper> newList){
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }

}
