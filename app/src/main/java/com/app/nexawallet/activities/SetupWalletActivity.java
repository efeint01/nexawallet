package com.app.nexawallet.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.app.nexawallet.R;
import com.app.nexawallet.databinding.ActivitySetupWalletBinding;
import com.app.nexawallet.fragments.CreateWalletFragment;
import com.app.nexawallet.fragments.RestoreWalletFragment;
import com.app.nexawallet.utilities.FragmentUtils;
import com.app.nexawallet.utilities.WalletManager;
import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SetupWalletActivity extends AppCompatActivity {

    ActivitySetupWalletBinding binding;
    WalletManager walletManager;
    String TAG = SetupWalletActivity.class.getSimpleName();

    boolean showContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        binding = ActivitySetupWalletBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (showContent) {
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                }
                return false;
            }
        });

        checkWalletExits();

        binding.createWalletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtils.addFragmentWithAnimation(getSupportFragmentManager(), binding.setupWalletLy.getId(), CreateWalletFragment.class,
                        R.anim.slide_in, R.anim.slide_out, R.anim.slide_in, R.anim.slide_out);
            }
        });

        binding.restoreWalletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtils.addFragmentWithAnimation(getSupportFragmentManager(), binding.setupWalletLy.getId(), RestoreWalletFragment.class,
                        R.anim.slide_in, R.anim.slide_out, R.anim.slide_in, R.anim.slide_out);
            }
        });

    }

    private void checkWalletExits() {
        walletManager = new WalletManager(getApplicationContext());
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                walletManager.loadWallet();
                runOnUiThread(() -> {
                    startActivity(new Intent(SetupWalletActivity.this,MainActivity.class));
                    finish();
                });
            } catch (Exception e) {
                showContent = true;
                Log.e(TAG, "No wallet found");
            }
        });
    }
}