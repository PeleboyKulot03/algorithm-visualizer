package com.example.dsavisualizer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PathFindingAlgorithmMainAdapter extends RecyclerView.Adapter<PathFindingAlgorithmMainAdapter.ViewHolder> {
    private final int row;
    private final int column;
    private final Context context;

    public PathFindingAlgorithmMainAdapter(Context context, int column, int row) {
        this.context = context;
        this.column = column;
        this.row = row;
        globals.matrix = new int[row * row];
    }

    @NonNull
    @Override
    public PathFindingAlgorithmMainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int height = (parent.getHeight() / row) - 6;
        int width = parent.getWidth() / column - 4;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_block, parent, false);
        ConstraintLayout layout = view.findViewById(R.id.block);
        layout.getLayoutParams().height = height;
        layout.getLayoutParams().width = width;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PathFindingAlgorithmMainAdapter.ViewHolder holder, int position) {
        holder.blocks.setBackgroundColor(context.getResources().getColor(R.color.white));
        if (holder.getAdapterPosition() == 0) {
            holder.blocks.setBackgroundColor(context.getResources().getColor(R.color.green));
        }
        if (holder.getAdapterPosition() == row * column - 1){
            holder.blocks.setBackgroundColor(context.getResources().getColor(R.color.red));
        }

        holder.blocks.setOnClickListener(v -> {
            if (holder.getAdapterPosition() == 0 || holder.getAdapterPosition() == row * column - 1) {
                return;
            }
            if (globals.matrix[holder.getAdapterPosition()] == 0) {
                holder.blocks.setBackgroundColor(context.getResources().getColor(R.color.grey));
                globals.matrix[holder.getAdapterPosition()] = 1;
                return;
            }
            holder.blocks.setBackgroundColor(context.getResources().getColor(R.color.white));
            globals.matrix[holder.getAdapterPosition()] = 0;
        });

    }


    @Override
    public int getItemCount() {
        return row * column;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ConstraintLayout blocks;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            blocks = itemView.findViewById(R.id.block);
        }
    }
}
