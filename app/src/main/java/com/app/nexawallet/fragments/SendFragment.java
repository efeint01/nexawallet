package com.app.nexawallet.fragments;

import static bitcoinunlimited.libbitcoincash.IchainKt.txFor;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.nexawallet.R;
import com.app.nexawallet.activities.MainActivity;
import com.app.nexawallet.databinding.FragmentSendBinding;
import com.app.nexawallet.utilities.AppUtils;
import com.app.nexawallet.utilities.WalletManager;
import com.app.nexawallet.views.ProgressBarDialog;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.math.BigDecimal;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import bitcoinunlimited.libbitcoincash.PayAddress;
import bitcoinunlimited.libbitcoincash.PayAddressType;
import bitcoinunlimited.libbitcoincash.UsefulKt;
import bitcoinunlimited.libbitcoincash.Wallet;
import bitcoinunlimited.libbitcoincash.iTransaction;


public class SendFragment extends Fragment {

    FragmentSendBinding binding;
    WalletManager walletManager;
    Wallet wallet;
    String TAG = SendFragment.class.getSimpleName();
    String walletAddress;
    BigDecimal balance;

    private boolean isReceiveAddressValid = false;
    private boolean isAmountValid = false;
    private final float swipeButtonThreshold = 100f;

    ProgressBarDialog progressBarDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSendBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        loadWallet();
    }

    private void loadWallet() {
        //Init wallet
        walletManager = new WalletManager(getContext());
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            wallet = walletManager.loadWallet(); // load existing wallet
            walletAddress = walletManager.getWalletAddress();
            balance = BigDecimal.valueOf(walletManager.getBalance());
            balance = new BigDecimal(10000);
        });
    }


    private void initViews() {
        //Disable swipe refresh layout
        if (getActivity() == null | getContext() == null) return;
        ((MainActivity) getActivity()).disableSwipeToRefresh();
        progressBarDialog = new ProgressBarDialog(getContext());
        binding.toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());

        binding.scanQRBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchQRCodeScanner();
            }
        });

        binding.pasteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() == null) return;
                String pastedText = AppUtils.pasteFromClipboard(getContext());
                binding.receiveAddressEt.setText(pastedText);
            }
        });

        binding.maxTw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.amountEt.setText(balance.toPlainString());
            }
        });

        //Init swipe button
        binding.sendBtn.setOnTouchListener(new View.OnTouchListener() {
            float startX; // Declare startX locally within the listener

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX(); // Initialize startX on touch
                        return true;
                    case MotionEvent.ACTION_UP:
                        float endX = event.getX();
                        if (endX - startX > swipeButtonThreshold) {
                            if (isAmountValid & isReceiveAddressValid) {
                                BigDecimal amount = BigDecimal.valueOf(binding.amountEt.getNumericValue());
                                String receiver = binding.receiveAddressEt.getText().toString().trim();
                                sendAmount(amount, receiver);
                            }
                        }
                        return true;
                }
                return false;
            }
        });

//        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!isAmountValid | !isReceiveAddressValid) {
//                    return;
//                }
//
//                BigDecimal amount = BigDecimal.valueOf(binding.amountEt.getNumericValue());
//                String receiver = binding.receiveAddressEt.getText().toString().trim();
//                Log.e(TAG, "onClick: " + amount);
//                sendAmount(amount, receiver);
//
//
//            }
//        });

        binding.receiveAddressEt.addTextChangedListener(combinedTextWatcher);
        binding.amountEt.addTextChangedListener(combinedTextWatcher);

    }

    private void sendAmount(BigDecimal amount, String receiver) {
        // If we are spending all, then deduct the fee from the amount (which was set above to the full ungrouped balance)
        boolean spendAll = balance.compareTo(amount) == 0;
        PayAddress payAddress = new PayAddress(receiver);
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                iTransaction transaction = txFor(wallet.getChainSelector());
                Log.e(TAG, "run: " + transaction.getFee());
                Log.e(TAG, "run: " + transaction.getFeeRate());
                Log.e(TAG, "run: " + transaction.toHex());

                iTransaction tx = wallet.send(toFinestUnit(amount), payAddress, spendAll, false, "");
                Log.e(TAG, "sendAmount: " + tx);
                Log.e(TAG, "send success: ");
                if (getActivity() == null) return;

            }
        });

    }


    /**
     * Convert the default display units to the finest granularity of this currency.  For example, mBCH to Satoshis
     */
    public long toFinestUnit(BigDecimal amount) {
        long ret = 0;
        switch (wallet.getChainSelector()) {
            case NEXA:
                ret = amount.multiply(new BigDecimal(UsefulKt.SATperNEX)).longValue();
                break;
            case NEXAREGTEST:
                ret = amount.multiply(new BigDecimal(UsefulKt.SATperNEX)).longValue();
                break;
            case NEXATESTNET:
                ret = amount.multiply(new BigDecimal(UsefulKt.SATperNEX)).longValue();
                break;
            case BCH:
                ret = amount.multiply(new BigDecimal(UsefulKt.SATperUBCH)).longValue();
                break;
            case BCHREGTEST:
                ret = amount.multiply(new BigDecimal(UsefulKt.SATperUBCH)).longValue();
                break;
            case BCHTESTNET:
                ret = amount.multiply(new BigDecimal(UsefulKt.SATperUBCH)).longValue();
                break;
        }
        return ret;
    }

    public BigDecimal fromFinestUnit(long amount) {
        BigDecimal factor;
        switch (wallet.getChainSelector()) {
            case NEXA:
                factor = new BigDecimal(UsefulKt.SATperNEX);
                break;
            case NEXAREGTEST:
                factor = new BigDecimal(UsefulKt.SATperNEX);
                break;
            case NEXATESTNET:
                factor = new BigDecimal(UsefulKt.SATperNEX);
                break;
            case BCH:
                factor = new BigDecimal(UsefulKt.SATperUBCH);
                break;
            case BCHREGTEST:
                factor = new BigDecimal(UsefulKt.SATperUBCH);
                break;
            case BCHTESTNET:
                factor = new BigDecimal(UsefulKt.SATperUBCH);
                break;
            default:
                factor = BigDecimal.ZERO; // Handle the default case as needed
                break;
        }
        BigDecimal ret = new BigDecimal(amount, UsefulKt.getCurrencyMath()).setScale(UsefulKt.currencyScale).divide(factor, UsefulKt.currencyScale, BigDecimal.ROUND_HALF_UP);
        return ret;
    }

    TextWatcher combinedTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // No action needed in this method.
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // No action needed in this method.
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Handle changes in the receiveAddressEt EditText.
            if (s == binding.receiveAddressEt.getEditableText()) {
                if (s.toString().isEmpty()) return;
                handleReceiveAddressTextChanged(s.toString().trim());
            }
            // Handle changes in the amountEt EditText.
            else if (s == binding.amountEt.getEditableText()) {
                if (s.toString().isEmpty()) return;
                handleAmountTextChanged(binding.amountEt.getNumericValueBigDecimal());
            }
        }
    };


    private void handleReceiveAddressTextChanged(String address) {
        if (address.isEmpty()) {
            isReceiveAddressValid = false;
            updateSendButtonState();
            return;
        }
        try {
            PayAddress payAddress = new PayAddress(address);

            if (payAddress.toString().equals(walletAddress)) {
                //Same address
                isReceiveAddressValid = false;
                showError("Receiver address cannot be the same address");
            } else if (payAddress.getType() == PayAddressType.NONE) {
                //Invalid address
                isReceiveAddressValid = false;
                showError(getString(R.string.invalid_address));
            } else if (wallet.getChainSelector() != payAddress.getBlockchain()) {
                isReceiveAddressValid = false;
                //Receiver blockchain not matching with this wallet blockchain
                showError(getString(R.string.blockchains_doesnt_match));
            } else {
                isReceiveAddressValid = true;
                clearError();
            }

            updateSendButtonState();
        } catch (Exception e) {
            isReceiveAddressValid = false;
            showError(getString(R.string.invalid_address));
            updateSendButtonState();
        }
    }

    private void handleAmountTextChanged(BigDecimal number) {
        try {
            if (number.compareTo(balance) > 0) {
                //Balance exceed
                isAmountValid = false;
                showError(getString(R.string.balance_exceed));
            } else if (number.compareTo(fromFinestUnit(UsefulKt.Dust(wallet.getChainSelector()))) <= 0) {
                //Number too small
                isAmountValid = false;
                showError(getString(R.string.amount_is_below_network_minimum));
            } else {
                isAmountValid = true;
                clearError();
            }
            updateSendButtonState();
        } catch (NumberFormatException e) {
            Log.e(TAG, "handleAmountTextChanged: " + e.getMessage());
            isAmountValid = false;
            showError(getString(R.string.invalid_number));
            updateSendButtonState();
        }
    }

    private void updateSendButtonState() {
        //Show shimmer effect and enable button if our conditions are true
        boolean conditions = isReceiveAddressValid && isAmountValid;

        binding.sendBtn.setEnabled(conditions);
        if (conditions) {
            binding.sendBtnShimmer.startShimmer();
        } else {
            binding.sendBtnShimmer.stopShimmer();
        }
    }

    private void showError(String errorMessage) {
        binding.errorTw.setText(errorMessage);
        binding.errorTw.setVisibility(View.VISIBLE);
        binding.sendBtn.setEnabled(false);
    }

    private void clearError() {
        binding.errorTw.setVisibility(View.GONE);
        binding.sendBtn.setEnabled(true);
    }

    private void launchQRCodeScanner() {
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setPrompt(getString(R.string.scan_qr_code));
        options.setBeepEnabled(false);
        options.setTorchEnabled(false);
        qrCodeLauncher.launch(options);
    }

    // Register the launcher and result handler
    private final ActivityResultLauncher<ScanOptions> qrCodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result.getContents() == null) {
                    Toast.makeText(getContext(), R.string.cancelled, Toast.LENGTH_LONG).show();
                } else {
                    binding.receiveAddressEt.setText(result.getContents());
                }
            });

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //Enable swipe to refresh
        if (getActivity() == null) return;
        ((MainActivity) getActivity()).enableSwipeToRefresh();
        binding = null;
    }
}