package com.app.nexawallet;

import android.app.Application;

import bitcoinunlimited.libbitcoincash.ChainSelector;
import bitcoinunlimited.libbitcoincash.Initialize;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initLibrary();
    }

    private void initLibrary() {
        //Init NEXA Library and network
        System.loadLibrary("nexandroid");  // Used to load the 'native-lib' library on application startup.
        Initialize.LibBitcoinCash(ChainSelector.NEXA.getV()); // Initialize the C library
    }

}
