package com.example.user.shopping;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ManagementAddRequest extends StringRequest {

    final static private String URL = "http://skatkdals5.cafe24.com/ManagementAdd.php";
    private Map<String, String> parameters;

    public ManagementAddRequest(String userID, String itemBarcode, String itemName, String list, String itemPrice, String itemAmount, String itemAddDate, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("itemBarcode", itemBarcode);
        parameters.put("itemName", itemName);
        parameters.put("list", list);
        parameters.put("itemPrice", itemPrice);
        parameters.put("itemAmount", itemAmount);
        parameters.put("itemAddDate", itemAddDate);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
