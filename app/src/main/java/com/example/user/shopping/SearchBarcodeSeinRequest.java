package com.example.user.shopping;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SearchBarcodeSeinRequest extends StringRequest {

    final static private String URL = "http://skatkdals5.cafe24.com/SearchBarcodeSein.php";
    private Map<String, String> parameters;

    public SearchBarcodeSeinRequest(String userID, String itemBarcode, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("itemBarcode", itemBarcode);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
