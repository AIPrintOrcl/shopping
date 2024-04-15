package com.example.user.shopping;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ItemBuyOrYetRequest extends StringRequest {

    final static private String URL = "http://skatkdals5.cafe24.com/ManagementItemBuyOrYet.php";
    private Map<String, String> parameters;

    public ItemBuyOrYetRequest(String userID, String itemID , String itemBuyOrYet, String itemUpdateDate, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("itemID", itemID);
        parameters.put("itemBuyOrYet", itemBuyOrYet);
        parameters.put("itemUpdateDate", itemUpdateDate);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
