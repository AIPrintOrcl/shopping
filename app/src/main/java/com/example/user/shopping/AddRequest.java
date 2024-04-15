package com.example.user.shopping;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AddRequest extends StringRequest {

    final static private String URL = "http://skatkdals5.cafe24.com/ItemAdd.php";
    private Map<String, String> parameters;

    public AddRequest(String userID, String itemName, String list, String foodID, String itemPrice, String itemAmount, String itemBarcode, String itemAddDate, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("itemName", itemName);
        parameters.put("list", list);
        parameters.put("foodID", foodID);
        parameters.put("itemPrice", itemPrice);
        parameters.put("itemAmount", itemAmount);
        parameters.put("itemBarcode", itemBarcode);
        parameters.put("itemAddDate", itemAddDate);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
