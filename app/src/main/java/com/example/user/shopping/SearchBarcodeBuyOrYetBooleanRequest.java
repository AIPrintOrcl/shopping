package com.example.user.shopping;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SearchBarcodeBuyOrYetBooleanRequest extends StringRequest {

    final static private String URL = "http://skatkdals5.cafe24.com/SearchBarcodeBuyOrYetBoolean.php";
    private Map<String, String> parameters;

    public SearchBarcodeBuyOrYetBooleanRequest(String userID, String itemBarcode, String itemBuyOrYet, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("itemBarcode", itemBarcode);
        parameters.put("itemBuyOrYet", itemBuyOrYet);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
