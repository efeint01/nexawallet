package com.app.nexawallet.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.app.nexawallet.databinding.DialogProgressBarBinding;

public class ProgressBarDialog extends Dialog {

    private DialogProgressBarBinding binding;
    private final String TAG = ProgressBarDialog.class.getSimpleName();

    public ProgressBarDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DialogProgressBarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setCancelable(false);
        // Set the window background to be transparent
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void show() {
        try {
            if (!isShowing()) super.show();
        } catch (WindowManager.BadTokenException e) {
            Log.e(TAG, "show: " + e.getMessage());
        }
    }

    @Override
    public void dismiss() {
        try {
            if (isShowing()) super.dismiss();
            binding = null;
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "dismiss: " + e.getMessage());
        }
    }

}