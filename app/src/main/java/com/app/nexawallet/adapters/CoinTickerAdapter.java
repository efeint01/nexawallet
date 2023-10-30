package com.app.nexawallet.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.nexawallet.R;
import com.app.nexawallet.databinding.RowCoinTickerItemBinding;
import com.app.nexawallet.models.CoinTicker;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CoinTickerAdapter extends RecyclerView.Adapter<CoinTickerAdapter.ViewHolder> {


    ArrayList<CoinTicker> arrayList;
    RequestManager glideManager;
    Context context;


    public CoinTickerAdapter(ArrayList<CoinTicker> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        glideManager = Glide.with(context);

    }

    @NonNull
    @Override
    public CoinTickerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CoinTickerAdapter.ViewHolder(RowCoinTickerItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CoinTickerAdapter.ViewHolder holder, int position) {

        CoinTicker model = arrayList.get(position);
        holder.binding.nameTw.setText(model.getMarketName());
        holder.binding.tickerTw.setText(String.format("%s/%s", model.getBase(), model.getTarget()));
        holder.binding.lastPriceTw.setText(String.format("%s: %s", context.getString(R.string.last_price), model.getLastPrice().toPlainString()));
        glideManager.load(model.getMarketLogoUrl())
                .placeholder(R.drawable.baseline_image_24)
                .fitCenter()
                .into(holder.binding.logoImg);

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLink(model.getTradeUrl());
            }
        });

    }

    private void openLink(String link) {
        context.startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse(link)));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void setArrayList(ArrayList<CoinTicker> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final RowCoinTickerItemBinding binding;

        public ViewHolder(@NonNull RowCoinTickerItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
