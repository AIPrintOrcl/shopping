package com.example.user.shopping;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ManagementAddNameBooleanRequest extends StringRequest {

    final static private String URL = "http://skatkdals5.cafe24.com/ManagementAddNameBoolean.php";
    private Map<String, String> parameters;

    public ManagementAddNameBooleanRequest(String userID, String itemName, String list, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("itemName", itemName);
        parameters.put("list", list);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
