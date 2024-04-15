package com.example.user.shopping;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Barcode extends AppCompatActivity {

    protected String list; // 리스트
    protected String itemBarcode; // 물품 바코드
    protected String itemName; // 물품명
    protected int itemPrice; // 물품 가격
    protected int itemAmount; // 물품 수량
    public static String userID; // 모든 클래스에 접근 가능

    // 현재 시간
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userID = getIntent().getStringExtra("userID");

        BackPressCloseHandler.onBack = false;

        BarcodeSearch.BarcodeSearchON = false;

    }

    @Override
    public void onResume() {
        super.onResume();

        if (BackPressCloseHandler.onBack) // 뒤로가기 두번 누를 시
        {
            finish();
        } else {
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setCaptureActivity(CustomScannerActivity.class);
            integrator.initiateScan();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d("onActivityResult", "onActivityResult: .");
        if (resultCode == Activity.RESULT_OK) {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            final String re = scanResult.getContents();

            Log.d("onActivityResult", "onActivityResult: ." + re);

            new AsyncTask<Void, Void, String>() { // 등록된 바코드의 물품명, 가격, 수량 정보
                String target;

                @Override
                protected void onPreExecute() {
                    try {
                        target = "http://skatkdals5.cafe24.com/ManagementItemPriceAmount.php?userID=" + URLEncoder.encode(MainActivity.userID, "UTF-8") +
                                "&list=" + URLEncoder.encode(ManagementFragment.managementListSpinner.getSelectedItem().toString(), "UTF-8") +
                                "&itemBarcode=" + URLEncoder.encode(re, "UTF-8");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                protected String doInBackground(Void... voids) {
                    try {
                        URL url = new URL(target);
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String temp;
                        StringBuilder stringBuilder = new StringBuilder();
                        while ((temp = bufferedReader.readLine()) != null) {
                            stringBuilder.append(temp + "\n");
                        }
                        bufferedReader.close();
                        inputStream.close();
                        httpURLConnection.disconnect();
                        return stringBuilder.toString().trim();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                public void onProgressUpdate(Void... values) {
                    super.onProgressUpdate(values);
                }

                @Override
                public void onPostExecute(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("response");

                        JSONObject object = jsonArray.getJSONObject(0);
                        itemBarcode = object.getString("itemBarcode");
                        itemName = object.getString("itemName");
                        list = object.getString("list");
                        itemPrice = object.getInt("itemPrice");
                        itemAmount = object.getInt("itemAmount");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.execute();

            Response.Listener<String> responseBooleanListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) { // 해당 유저, 리스트, YET에 있는 것 인지 판별
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if (success) { // 해당 유저, 리스트, YET에 존재 한다면

                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonResponse = new JSONObject(response);
                                        boolean success = jsonResponse.getBoolean("success");
                                        if (success) {


                                            Toast.makeText(Barcode.this, "물품명 : " + itemName +
                                                    "\n 가격 : " + itemPrice + "\n 수량 : " + itemAmount +
                                                    "\n\n 물품을 구입 했습니다.", Toast.LENGTH_LONG).show();

                                            if (ManagementFragment.managementListSpinner.getSelectedItem().equals("월")) {
                                                ManagementFragment.totalNumberMonday -= 1;
                                                ManagementFragment.numberMonday.setText("[" + ManagementFragment.totalNumberMonday + "]");
                                            } else if (ManagementFragment.managementListSpinner.getSelectedItem().equals("화")) {
                                                ManagementFragment.totalNumberTuesday -= 1;
                                                ManagementFragment.numberTuesday.setText("[" + ManagementFragment.totalNumberTuesday + "]");
                                            } else if (ManagementFragment.managementListSpinner.getSelectedItem().equals("수")) {
                                                ManagementFragment.totalNumberWednesday -= 1;
                                                ManagementFragment.numberWednesday.setText("[" + ManagementFragment.totalNumberWednesday + "]");
                                            } else if (ManagementFragment.managementListSpinner.getSelectedItem().equals("목")) {
                                                ManagementFragment.totalNumberThursday -= 1;
                                                ManagementFragment.numberThursday.setText("[" + ManagementFragment.totalNumberThursday + "]");
                                            } else if (ManagementFragment.managementListSpinner.getSelectedItem().equals("금")) {
                                                ManagementFragment.totalNumberFriday -= 1;
                                                ManagementFragment.numberFriday.setText("[" + ManagementFragment.totalNumberFriday + "]");
                                            } else if (ManagementFragment.managementListSpinner.getSelectedItem().equals("토")) {
                                                ManagementFragment.totalNumberSaturday -= 1;
                                                ManagementFragment.numberSaturday.setText("[" + ManagementFragment.totalNumberSaturday + "]");
                                            } else if (ManagementFragment.managementListSpinner.getSelectedItem().equals("일")) {
                                                ManagementFragment.totalNumberSunday -= 1;
                                                ManagementFragment.numberSunday.setText("[" + ManagementFragment.totalNumberSunday + "]");
                                            } else {
                                            }

                                            ManagementFragment.itemList.clear();
                                            new ManagementFragment.BackgroundTask().execute();
                                            ManagementFragment.managementListAdapter.notifyDataSetChanged(); // 갱신
                                        } else {
                                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Barcode.this);
                                            android.support.v7.app.AlertDialog dialog = builder.setMessage("실패했습니다.")
                                                    .setPositiveButton("다시 시도", null)
                                                    .create();
                                            dialog.show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            String list = ManagementFragment.managementListSpinner.getSelectedItem().toString();

                            String itemUpdateDate = getTime();

                            ManagementBarcodeYetBuyRequest managementBarcodeYetBuyRequest = new ManagementBarcodeYetBuyRequest(userID, list, re, itemUpdateDate, responseListener);
                            RequestQueue queue = Volley.newRequestQueue(Barcode.this);
                            queue.add(managementBarcodeYetBuyRequest);
                        } else { // 존재 하지 않는 다면
                            Response.Listener<String> responseBarcodeSeinListener = new Response.Listener<String>() { // 해당 사용자, 리스트에 바코드 존재 여부
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonResponse = new JSONObject(response);
                                        boolean success = jsonResponse.getBoolean("success");
                                        if (success) {
                                            Toast.makeText(Barcode.this, "물품명 : " + itemName +
                                                    "\n 가격 : " + itemPrice + "\n 수량 : " + itemAmount +
                                                    "\n\n 이미 구입한 물품입니다.", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(Barcode.this, "'" + re + "'는" + "\n등록되어 있지 않은 바코드 입니다.", Toast.LENGTH_LONG).show();

                                            CustomScannerActivity CSA = (CustomScannerActivity) CustomScannerActivity.CustomScannerActivityFnish;

                                            Intent intent2 = new Intent();
                                            intent2.putExtra("result", re);
                                            setResult(RESULT_OK, intent2);

                                            CSA.finish();
                                            finish();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            String list = ManagementFragment.managementListSpinner.getSelectedItem().toString();

                            ManagementBarcodeSeinRequest managementBarcodeSeinRequest = new ManagementBarcodeSeinRequest(userID, list, re, responseBarcodeSeinListener);
                            RequestQueue queue = Volley.newRequestQueue(Barcode.this);
                            queue.add(managementBarcodeSeinRequest);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            String list = ManagementFragment.managementListSpinner.getSelectedItem().toString();

            ManagementBarcodeYetBuyBooleanRequest managementBarcodeYetBuyBooleanRequest = new ManagementBarcodeYetBuyBooleanRequest(userID, list, re, responseBooleanListener);
            RequestQueue queue = Volley.newRequestQueue(Barcode.this);
            queue.add(managementBarcodeYetBuyBooleanRequest);

            Intent intent2 = new Intent();
            intent2.putExtra("result", re);
            setResult(RESULT_OK, intent2);
        }
    }

    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }
}


