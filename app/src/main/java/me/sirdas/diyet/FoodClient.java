package me.sirdas.diyet;

import android.util.Log;

import com.loopj.android.http.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class FoodClient {
    private static final String SEARCH_URL = "https://api.nal.usda.gov/fdc/v1/foods/list?pageSize=100&api_key=GYfXOfEg6e9Al9jxsump1hiLo4a30usCF41qCMbm&dataType=SR%20Legacy&query=";
    private static final String REPORT_URL = "https://api.nal.usda.gov/fdc/v1/food/";
    private static final String API_KEY = "GYfXOfEg6e9Al9jxsump1hiLo4a30usCF41qCMbm";
    private AsyncHttpClient client;

    public FoodClient() {
        this.client = new AsyncHttpClient();
    }

    public void getFoods(final String query, JsonHttpResponseHandler handler) {
//        RequestParams params = new RequestParams();
//        params.put("api_key", API_KEY);
//        //params.put("query", query);
//        params.put("pageSize", 100);
//        params.put("dataType", "SR%20Legacy");
//        Log.d("json", params.toString());
//        this.client.get(SEARCH_URL, params, handler);
        try {
            this.client.get(SEARCH_URL + URLEncoder.encode(query, "utf-8"), handler);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void getFoodReport(final String id, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("api_key", API_KEY);
        params.put("nutrients", "203,204,205");
        this.client.get(REPORT_URL + id, params, handler);
    }
}
