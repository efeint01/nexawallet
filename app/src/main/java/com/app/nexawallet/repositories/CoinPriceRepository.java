package com.app.nexawallet.repositories;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.nexawallet.constants.CoinPriceConstant;
import com.app.nexawallet.interfaces.CoinPriceListener;
import com.app.nexawallet.utilities.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class CoinPriceRepository {
    VolleySingleton volleySingleton;

    public CoinPriceRepository(Context context) {
        volleySingleton = VolleySingleton.getInstance(context);
    }

    public void getCoinPrice(CoinPriceListener listener) {
        //Get NEXA last price
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(CoinPriceConstant.GET_PRICE_URL, response -> {
            try {
                String lastSt = response.getString("Last");
                listener.onResponse(new BigDecimal(lastSt));
            } catch (JSONException e) {
                listener.onErrorResponse(e.getMessage());
            }
        }, error -> listener.onErrorResponse(error.getMessage()));
        volleySingleton.addToRequestQueue(jsonObjectRequest);
    }


}
