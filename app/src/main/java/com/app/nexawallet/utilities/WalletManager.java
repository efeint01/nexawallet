package com.app.nexawallet.utilities;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import bitcoinunlimited.libbitcoincash.Bip44Wallet;
import bitcoinunlimited.libbitcoincash.ChainSelector;
import bitcoinunlimited.libbitcoincash.CommonWallet;
import bitcoinunlimited.libbitcoincash.DataMissingException;
import bitcoinunlimited.libbitcoincash.Hash256;
import bitcoinunlimited.libbitcoincash.InitKt;
import bitcoinunlimited.libbitcoincash.KvpDatabase;
import bitcoinunlimited.libbitcoincash.NEW_WALLET;
import bitcoinunlimited.libbitcoincash.PayAddress;
import bitcoinunlimited.libbitcoincash.PayDestination;
import bitcoinunlimited.libbitcoincash.PersistKt;
import bitcoinunlimited.libbitcoincash.PlatformContext;
import bitcoinunlimited.libbitcoincash.TransactionHistory;

public class WalletManager {


    /* Note: WalletManager only supports one wallet, yet */

    private final String walletName = "nexa";

    private final KvpDatabase walletDb;
    private final ChainSelector chainSelector;

    public WalletManager(Context context) {
        String walletDbName = "bip44walletdb";
        this.walletDb = PersistKt.OpenKvpDB(new PlatformContext(context), InitKt.getDbPrefix() + walletDbName);
        this.chainSelector = ChainSelector.NEXA;
    }

    public Bip44Wallet createWallet() {
        return new Bip44Wallet(walletDb, walletName, chainSelector, NEW_WALLET.INSTANCE);
    }

    public Bip44Wallet recoverWallet(String secretWords) {
        return new Bip44Wallet(walletDb, walletName, ChainSelector.NEXA, secretWords, 0);
    }

    public Bip44Wallet loadWallet() {
        return new Bip44Wallet(walletDb, walletName);
    }

    public String getWalletAddress() {
        Bip44Wallet wallet = loadWallet();
        return wallet.getNewAddress().toString();
    }

    public long getBalance() {
        Bip44Wallet wallet = loadWallet();
        return wallet.getBalance();
    }

    public long getUnconfirmedBalance() {
        Bip44Wallet wallet = loadWallet();
        return wallet.getBalanceUnconfirmed();
    }

    public long getConfirmedBalance() {
        Bip44Wallet wallet = loadWallet();
        return wallet.getBalanceConfirmed();
    }


    public List<TransactionHistory> getTxHistory() {
        Bip44Wallet wallet = loadWallet();
        List<TransactionHistory> historyList = wallet.getTxHistory().values().stream()
                .sorted(Comparator.comparing(TransactionHistory::getDate))
                .collect(Collectors.toList());
        Collections.reverse(historyList);
        return historyList;
    }
}
