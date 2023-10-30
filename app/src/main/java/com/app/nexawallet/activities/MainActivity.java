package com.app.nexawallet.activities;


import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.nexawallet.R;
import com.app.nexawallet.adapters.TxHistoryAdapter;
import com.app.nexawallet.databinding.ActivityMainBinding;
import com.app.nexawallet.fragments.BuyFragment;
import com.app.nexawallet.fragments.ReceiveFragment;
import com.app.nexawallet.fragments.SendFragment;
import com.app.nexawallet.fragments.SwapFragment;
import com.app.nexawallet.interfaces.CoinPriceListener;
import com.app.nexawallet.repositories.CoinPriceRepository;
import com.app.nexawallet.utilities.AppUtils;
import com.app.nexawallet.utilities.FragmentUtils;
import com.app.nexawallet.utilities.VolleySingleton;
import com.app.nexawallet.utilities.WalletManager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import bitcoinunlimited.libbitcoincash.TransactionHistory;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    boolean balanceVisibility = true;

    String TAG = MainActivity.class.getSimpleName();

    WalletManager walletManager;

    String walletAddress;
    long balance;
    BigDecimal balanceEquity;
    NumberFormat currencyFormat;

    TxHistoryAdapter txHistoryAdapter;
    VolleySingleton volleySingleton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initViews();

    }

    private void getTransactions(List<TransactionHistory> history) {
        if (history.isEmpty()) {
            //No tx history found
            binding.emptyView.getRoot().setVisibility(View.VISIBLE);
            return;
        }

        binding.emptyView.getRoot().setVisibility(View.GONE);

        //Init adapter
        txHistoryAdapter = new TxHistoryAdapter(history);
        binding.txHistoryRv.setAdapter(txHistoryAdapter);


    }

    public String formatWalletAddress(String walletAddress) {
        String first10 = walletAddress.substring(0, 10);
        String last10 = walletAddress.substring(walletAddress.length() - 10);
        return first10 + "..." + last10;
    }

    private void getCoinPrice() {
        if (balance == 0) {
            return;
        }

        CoinPriceRepository coinPriceRepository = new CoinPriceRepository(this);
        coinPriceRepository.getCoinPrice(new CoinPriceListener() {
            @Override
            public void onResponse(BigDecimal price) {
                balanceEquity = price.multiply(BigDecimal.valueOf(balance));
                // Set the scale to 2 decimal places and use RoundingMode.HALF_UP for rounding
                balanceEquity = balanceEquity.setScale(2, RoundingMode.HALF_UP);
                binding.balanceEquityTw.setText(currencyFormat.format(balanceEquity));
            }

            @Override
            public void onErrorResponse(String msg) {
                Log.e(TAG, "onErrorResponse: " + msg);
            }
        });
    }

    public String formatBalance(long balance) {
        if (balance < 1000) {
            // No formatting needed for balances less than 1000
            return String.format(Locale.getDefault(), "%d %s", balance, getString(R.string.coin_symbol));
        } else if (balance < 1000000) {
            // Format as K (thousands)
            return String.format(Locale.getDefault(), "%.1fK %s", balance / 1000.0, getString(R.string.coin_symbol));
        } else if (balance < 1000000000) {
            // Format as M (millions)
            return String.format(Locale.getDefault(), "%.1fM %s", balance / 1000000.0, getString(R.string.coin_symbol));
        } else {
            // Format as T (trillions)
            return String.format(Locale.getDefault(), "%.1fT %s", balance / 1000000000.0, getString(R.string.coin_symbol));
        }
    }

    private void loadWallet() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                walletManager = new WalletManager(getApplicationContext());
                walletAddress = walletManager.getWalletAddress();
                balance = walletManager.getBalance();

                List<TransactionHistory> transactionHistoryList = walletManager.getTxHistory();

                balance = Long.parseLong("10000000000");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.walletAddressTw.setText(formatWalletAddress(walletAddress));
                        binding.balanceTw.setText(balanceVisibility ? formatBalance(balance) : "******");
                        binding.swipeRefreshLy.setRefreshing(false);
                        getTransactions(transactionHistoryList);
                    }
                });

                getCoinPrice();
            }
        });


    }


    private void initViews() {
        volleySingleton = VolleySingleton.getInstance(this);
        currencyFormat = DecimalFormat.getCurrencyInstance(Locale.US);

        binding.swipeRefreshLy.setOnRefreshListener(this::loadWallet);

        binding.buyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtils.addFragmentWithAnimation(getSupportFragmentManager(), binding.mainLy.getId(), BuyFragment.class,
                        R.anim.slide_in, R.anim.slide_out, R.anim.slide_in, R.anim.slide_out);
            }
        });

        binding.sendCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtils.addFragmentWithAnimation(getSupportFragmentManager(), binding.mainLy.getId(), SendFragment.class,
                        R.anim.slide_in, R.anim.slide_out, R.anim.slide_in, R.anim.slide_out);
            }
        });

        binding.receiveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle args = new Bundle();
                args.putString("walletAddress", walletAddress);
                FragmentUtils.addFragmentWithAnimation(getSupportFragmentManager(), binding.mainLy.getId(), ReceiveFragment.class,
                        R.anim.slide_in, R.anim.slide_out, R.anim.slide_in, R.anim.slide_out, args);
            }
        });

        binding.swapCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtils.addFragmentWithAnimation(getSupportFragmentManager(), binding.mainLy.getId(), SwapFragment.class,
                        R.anim.slide_in, R.anim.slide_out, R.anim.slide_in, R.anim.slide_out);
            }
        });

        binding.balanceVisibilityTw.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= binding.balanceVisibilityTw.getRight() - binding.balanceVisibilityTw.getTotalPaddingRight()) {
                    balanceVisibility = !balanceVisibility;
                    if (!balanceVisibility) {
                        binding.balanceVisibilityTw.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.round_visibility_off_24, 0);
                        binding.balanceTw.setText("******");
                        binding.balanceEquityTw.setText("******");
                    } else {
                        binding.balanceVisibilityTw.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.round_visibility_24, 0);
                        binding.balanceTw.setText(formatBalance(balance));
                        binding.balanceEquityTw.setText(currencyFormat.format(balanceEquity));
                    }
                }
            }
            return true;
        });


        binding.walletAddressTw.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= binding.walletAddressTw.getRight() - binding.walletAddressTw.getTotalPaddingRight()) {
                    AppUtils.copyToClipboard(getApplicationContext(), walletAddress);
                    binding.walletAddressTw.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.round_done_24, 0);
                    Toast.makeText(MainActivity.this, "Wallet Address Copied." + "\n" + walletAddress, Toast.LENGTH_SHORT).show();

                    binding.walletAddressTw.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            binding.walletAddressTw.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.round_content_copy_24, 0);
                        }
                    }, 1000);
                }
            }
            return true;
        });

        //Load exist wallet and update UI
        loadWallet();

    }

    public void disableSwipeToRefresh() {
        binding.swipeRefreshLy.setEnabled(false);
    }
    public void enableSwipeToRefresh() {
        binding.swipeRefreshLy.setEnabled(true);
    }

}