package com.example.user.shopping;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;

import org.json.JSONObject;

public class BarcodeSearch extends AppCompatActivity {

    public static String userID; // 모든 클래스에 접근 가능
    public static Activity BarcodeSearchActivityFnish;
    public static Boolean BarcodeSearchON;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BarcodeSearchON = true;

        userID = getIntent().getStringExtra("userID");

        BarcodeSearchActivityFnish = BarcodeSearch.this;

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CustomScannerActivity.class);
        integrator.initiateScan();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d("onActivityResult", "onActivityResult: .");
        if (resultCode == Activity.RESULT_OK) {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            String re = scanResult.getContents();

            Log.d("onActivityResult", "onActivityResult: ." + re);

            Intent intent2 = new Intent();
            intent2.putExtra("result", re);
            setResult(RESULT_OK, intent2);

            finish();
        }
    }
}


