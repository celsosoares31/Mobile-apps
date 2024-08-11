package com.example.control_gastos;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ExpenseReportAdapter extends RecyclerView.Adapter<ExpenseReportAdapter.ExpenseViewHolder> {
    private Map<String, Double> totalExpensesPerMonth;
    private List<String> months;
    private List<Double> totals;

    public ExpenseReportAdapter(Map<String, Double> totalExpensesPerMonth) {
        this.totalExpensesPerMonth = totalExpensesPerMonth;
        this.months = new ArrayList<>(totalExpensesPerMonth.keySet());
        this.totals = new ArrayList<>(totalExpensesPerMonth.values());
        Log.d("ExpenseReportAdapter", ""+totals);
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expense_report, parent, false);
        return new ExpenseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        String month = months.get(position);
        Double totalExpense = totals.get(position);
        
        holder.textViewDate.setText(month);
        holder.textViewTotalAmount.setText(String.format(Locale.getDefault(), "%.2f", totalExpense));
    }

    @Override
    public int getItemCount() {
        return months.size();
    }
    

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewDate;
        private TextView textViewTotalAmount;
        

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTotalAmount = itemView.findViewById(R.id.textViewTotalAmount);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            
        }

//        public void bind(Expense expense) {
//            textViewDate.setText(expense.getDate());
//            textViewTotalAmount.setText(expense);
//
//        }
    }
}
