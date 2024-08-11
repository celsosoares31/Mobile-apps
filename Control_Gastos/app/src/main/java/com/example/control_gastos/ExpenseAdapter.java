package com.example.control_gastos;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.net.URI;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {
    private List<Expense> expenseList;

    public ExpenseAdapter(List<Expense> expenseList) {
        this.expenseList = expenseList;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        holder.bind(expense);
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public void setExpenseList(List<Expense> expenseList) {
        this.expenseList = expenseList;
        notifyDataSetChanged();
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewAmount;
        private TextView textViewTime;
        private TextView textViewDate;
        private ImageView expenseImage;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewAmount = itemView.findViewById(R.id.textViewAmount);
            expenseImage = itemView.findViewById(R.id.expenseImage);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            
        }

        public void bind(Expense expense) {
            Uri uri = Uri.parse(expense.getPhoto());
            textViewTitle.setText(expense.getItem());
            textViewTime.setText(expense.getTime());
            textViewDate.setText(expense.getDate());
            textViewAmount.setText(String.valueOf(expense.getAmount()));
            expenseImage.setImageURI(uri);
        }
    }
}
