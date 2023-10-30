package com.app.nexawallet.repositories;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.nexawallet.constants.CoinTickerConstant;
import com.app.nexawallet.interfaces.CoinTickerListener;
import com.app.nexawallet.models.CoinTicker;
import com.app.nexawallet.utilities.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;

public class CoinTickerRepository {

    VolleySingleton volleySingleton;

    public CoinTickerRepository(Context context) {
        volleySingleton = VolleySingleton.getInstance(context);
    }

    public void getCoinTickers(CoinTickerListener listener) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(CoinTickerConstant.GET_COIN_TICKERS_URL, response -> {
            try {
                JSONArray tickersArray = response.getJSONArray("tickers");

                ArrayList<CoinTicker> arrayList = new ArrayList<>();

                for (int i = 0; i < tickersArray.length() ; i++) {
                    JSONObject tickerObj = tickersArray.getJSONObject(i);
                    String base = tickerObj.getString("base");
                    String target = tickerObj.getString("target");
                    String lastPriceSt = tickerObj.getString("last");
                    BigDecimal lastPrice = new BigDecimal(lastPriceSt);
                    String tradeUrl = tickerObj.getString("trade_url");
                    JSONObject marketObj = tickerObj.getJSONObject("market");

                    String marketName = marketObj.getString("name");
                    String marketLogoUrl = marketObj.getString("logo");

                    arrayList.add(new CoinTicker(
                            base,
                            target,
                            marketName,
                            marketLogoUrl,
                            lastPrice,
                            tradeUrl
                    ));
                }

                listener.onResponse(arrayList);

            } catch (JSONException e) {
                listener.onErrorResponse(e.getMessage());
            }
        }, error -> listener.onErrorResponse(error.getMessage()));

        volleySingleton.addToRequestQueue(jsonObjectRequest);
    }

}
