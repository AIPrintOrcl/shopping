package com.example.user.shopping;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ItemPriceAmountUpdateRequest extends StringRequest {

    final static private String URL = "http://skatkdals5.cafe24.com/ManagementItemPriceAmountUdate.php";
    private Map<String, String> parameters;

    public ItemPriceAmountUpdateRequest(String userID, String itemID , String itemPrice, String itemAmount, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("itemID", itemID);
        parameters.put("itemPrice", itemPrice);
        parameters.put("itemAmount", itemAmount);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
