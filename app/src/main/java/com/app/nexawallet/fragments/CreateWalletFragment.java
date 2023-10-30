package com.app.nexawallet.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.nexawallet.R;
import com.app.nexawallet.activities.MainActivity;
import com.app.nexawallet.databinding.FragmentCreateWalletBinding;
import com.app.nexawallet.utilities.WalletManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import bitcoinunlimited.libbitcoincash.Bip44Wallet;
import bitcoinunlimited.libbitcoincash.DataMissingException;
import bitcoinunlimited.libbitcoincash.PayAddress;
import bitcoinunlimited.libbitcoincash.PayDestination;


public class CreateWalletFragment extends Fragment {


    FragmentCreateWalletBinding binding;
    String TAG = CreateWalletFragment.class.getSimpleName();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateWalletBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    private void initViews() {
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });
        createWallet();
    }

    private void createWallet() {
        WalletManager walletManager = new WalletManager(getContext());
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            Bip44Wallet wallet = walletManager.createWallet();
            Log.e(TAG, "createWallet: "+wallet.getNewAddress().toString() );
            String[] secretWords = wallet.getSecretWords().split(" ");
            getActivity().runOnUiThread(() -> parseWords(secretWords));
        });

        binding.doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWarningDialog();
            }
        });

    }

    private void parseWords(String[] secretWords) {
        binding.secretWordTw.setText(secretWords[0]);
        binding.secretWordTw2.setText(secretWords[1]);
        binding.secretWordTw3.setText(secretWords[2]);
        binding.secretWordTw4.setText(secretWords[3]);
        binding.secretWordTw5.setText(secretWords[4]);
        binding.secretWordTw6.setText(secretWords[5]);
        binding.secretWordTw7.setText(secretWords[6]);
        binding.secretWordTw8.setText(secretWords[7]);
        binding.secretWordTw9.setText(secretWords[8]);
        binding.secretWordTw10.setText(secretWords[9]);
        binding.secretWordTw11.setText(secretWords[10]);
        binding.secretWordTw12.setText(secretWords[11]);
    }

    private void showWarningDialog() {
        if (getContext() == null) return;
        new MaterialAlertDialogBuilder(getContext())
                .setTitle(R.string.are_you_sure)
                .setMessage(R.string.secret_words_disclaimer)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (getActivity() == null) return;
                        startActivity(new Intent(getContext(), MainActivity.class));
                        getActivity().finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}