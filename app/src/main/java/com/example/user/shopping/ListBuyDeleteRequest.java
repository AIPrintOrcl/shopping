package com.example.user.shopping;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ListBuyDeleteRequest extends StringRequest {

    final static private String URL = "http://skatkdals5.cafe24.com/ListBuyDelete.php";
    private Map<String, String> parameters;

    public ListBuyDeleteRequest(String userID, String list, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("list", list);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
