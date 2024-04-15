package com.example.user.shopping;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ManagementBarcodeYetBuyRequest extends StringRequest {

    final static private String URL = "http://skatkdals5.cafe24.com/ManagementBarcodeYetBuy.php";
    private Map<String, String> parameters;

    public ManagementBarcodeYetBuyRequest(String userID, String list, String itemBarcode, String itemUpdateDate, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("list", list);
        parameters.put("itemBarcode", itemBarcode);
        parameters.put("itemUpdateDate", itemUpdateDate);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
