package com.app.nexawallet.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.nexawallet.R;
import com.app.nexawallet.activities.MainActivity;
import com.app.nexawallet.dialogs.BottomSheetCopiedFragment;
import com.app.nexawallet.databinding.FragmentReceiveBinding;
import com.app.nexawallet.utilities.FragmentUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;


public class ReceiveFragment extends Fragment {


    FragmentReceiveBinding binding;
    String walletAddress;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentReceiveBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Get wallet address args
        Bundle args = getArguments();
        if (args == null) return;
        walletAddress = args.getString("walletAddress");

        initViews();


    }

    private void initViews() {
        //Disable swipe refresh layout
        ((MainActivity) getActivity()).disableSwipeToRefresh();

        binding.toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());
        binding.doneBtn.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        binding.copyWalletAddressBtn.setOnClickListener(v -> FragmentUtils.showBottomSheet(getParentFragmentManager(), BottomSheetCopiedFragment.class, getArguments()));

        generateQRCode(walletAddress);
    }

    private void generateQRCode(String walletAddress) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(walletAddress, BarcodeFormat.QR_CODE, 1000, 1000);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            binding.qrCodeAddressImg.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //Enable swipe refresh layout
        ((MainActivity) getActivity()).enableSwipeToRefresh();
        binding = null;
    }
}