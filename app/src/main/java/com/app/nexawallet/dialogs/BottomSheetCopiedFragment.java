package com.app.nexawallet.dialogs;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.nexawallet.R;
import com.app.nexawallet.databinding.FragmentBottomSheetCopiedBinding;
import com.app.nexawallet.utilities.AppUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class BottomSheetCopiedFragment extends BottomSheetDialogFragment {

    FragmentBottomSheetCopiedBinding binding;
    String walletAddress;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBottomSheetCopiedBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialog);
        dialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    private void initViews() {
        Bundle args = getArguments();
        if (args == null) return;
        walletAddress = args.getString("walletAddress");
        binding.walletAddressTw.setText(walletAddress);
        //Copy wallet address
        AppUtils.copyToClipboard(getContext(),walletAddress);

        binding.doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        binding.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.shareText(getContext(),walletAddress);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}