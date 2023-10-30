package com.app.nexawallet.adapters;

import static bitcoinunlimited.libbitcoincash.UsefulKt.CurrencyDecimal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.nexawallet.R;
import com.app.nexawallet.databinding.RowCoinTickerItemBinding;
import com.app.nexawallet.databinding.RowTxHistoryItemBinding;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import bitcoinunlimited.libbitcoincash.TransactionHistory;

public class TxHistoryAdapter extends RecyclerView.Adapter<TxHistoryAdapter.ViewHolder> {

    List<TransactionHistory> transactionHistoryList;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d", Locale.US);


    public TxHistoryAdapter(List<TransactionHistory> transactionHistoryList) {
        this.transactionHistoryList = transactionHistoryList;
    }

    @NonNull
    @Override
    public TxHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(RowTxHistoryItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TxHistoryAdapter.ViewHolder holder, int position) {

        TransactionHistory model = transactionHistoryList.get(position);
        long amount = model.getIncomingAmt() - model.getOutgoingAmt();
        holder.binding.amountTw.setText(String.format(Locale.getDefault(),"%d %d", amount, R.string.coin_symbol));
        if (model.getIncomingAmt() > model.getOutgoingAmt()) {
            //Receive
            holder.binding.amountTw.setTextColor(ContextCompat.getColor(holder.binding.getRoot().getContext(),R.color.green));
            holder.binding.statusImg.setImageResource(R.drawable.round_arrow_downward_24);
            holder.binding.statusTw.setText(R.string.receive);
        } else {
            //Send
            holder.binding.amountTw.setTextColor(ContextCompat.getColor(holder.binding.getRoot().getContext(),R.color.red));
            holder.binding.statusImg.setImageResource(R.drawable.round_arrow_upward_24);
            holder.binding.statusTw.setText(R.string.sent);
        }

        holder.binding.dateTw.setText(dateFormat.format(model.getDate()));

        if (!model.getPriceWhatFiat().equals("")) {
            //Get fiat price of amount when issued
            BigDecimal netFiat = CurrencyDecimal(amount).multiply(model.getPriceWhenIssued());
            holder.binding.amountPriceTw.setText(String.format("%s", netFiat));
            //holder.binding.amountPriceTw.setText(netFiat + " " + model.getPriceWhatFiat());
        }

    }

    public void setTransactionHistoryList(List<TransactionHistory> transactionHistoryList) {
        this.transactionHistoryList = transactionHistoryList;
    }

    @Override
    public int getItemCount() {
        return transactionHistoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RowTxHistoryItemBinding binding;
        public ViewHolder(@NonNull RowTxHistoryItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
