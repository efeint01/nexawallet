package com.app.nexawallet.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.nexawallet.activities.MainActivity;
import com.app.nexawallet.interfaces.CoinTickerListener;
import com.app.nexawallet.repositories.CoinTickerRepository;
import com.app.nexawallet.utilities.VolleySingleton;
import com.app.nexawallet.adapters.CoinTickerAdapter;
import com.app.nexawallet.databinding.FragmentBuyBinding;
import com.app.nexawallet.models.CoinTicker;
import com.app.nexawallet.views.ProgressBarDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;


public class BuyFragment extends Fragment {

    FragmentBuyBinding binding;
    CoinTickerAdapter adapter;
    ArrayList<CoinTicker> coinTickerArrayList = new ArrayList<>();

    String TAG = BuyFragment.class.getName();


    ProgressBarDialog progressBarDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBuyBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    private void initViews() {
        //Disable swipe refresh layout
        if (getActivity() == null | getContext() == null) return;
        ((MainActivity) getActivity()).disableSwipeToRefresh();

        binding.toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());
        progressBarDialog = new ProgressBarDialog(getContext());
        adapter = new CoinTickerAdapter(coinTickerArrayList, getContext());
        binding.tickersRecyclerView.setAdapter(adapter);

        getCoinTickers();

    }

    private void getCoinTickers() {
        progressBarDialog.show();
        CoinTickerRepository coinTickerRepository = new CoinTickerRepository(getContext());
        coinTickerRepository.getCoinTickers(new CoinTickerListener() {
            @Override
            public void onResponse(ArrayList<CoinTicker> arrayList) {
                progressBarDialog.dismiss();
                adapter.setArrayList(arrayList);
            }

            @Override
            public void onErrorResponse(String msg) {
                Log.e(TAG, "onErrorResponse: " + msg);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //Enable swipe to refresh
        if (getActivity() == null) return;
        ((MainActivity) getActivity()).enableSwipeToRefresh();
        binding = null;
    }
}