package com.example.user.shopping;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DeleteRequest extends StringRequest {

    final static private String URL = "http://skatkdals5.cafe24.com/ManagementDelete.php";
    private Map<String, String> parameters;

    public DeleteRequest(String userID, String itemID , Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("itemID", itemID);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
