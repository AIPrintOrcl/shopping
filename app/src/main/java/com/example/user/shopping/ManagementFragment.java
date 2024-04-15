package com.example.user.shopping;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ManagementFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ManagementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManagementFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ManagementFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManagementFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManagementFragment newInstance(String param1, String param2) {
        ManagementFragment fragment = new ManagementFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public static ListView itemListView;
    public static ManagementListAdapter adapter;
    public static List<Item> itemList;

    public static int totalNumberMonday = 0; // 요일별 리스트 남은 물품 갯수
    public static TextView numberMonday;
    public static int totalNumberTuesday = 0;
    public static TextView numberTuesday;
    public static int totalNumberWednesday = 0;
    public static TextView numberWednesday;
    public static int totalNumberThursday = 0;
    public static TextView numberThursday;
    public static int totalNumberFriday = 0;
    public static TextView numberFriday;
    public static int totalNumberSaturday = 0;
    public static TextView numberSaturday;
    public static int totalNumberSunday = 0;
    public static TextView numberSunday;

    public static int totalPrice = 0;
    public static TextView price;

    public static ArrayAdapter managementListAdapter;
    public static Spinner managementListSpinner; // 리스트 스피널

    public static String itemBuyOrYet = ""; // 구입 OR 아직

    private String userID = MainActivity.userID;

    TextView barcodeTextView;
    // TextView barcodeYetBuy;

    // 현재 시간
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

    @Override
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);
        itemListView = (ListView) getView().findViewById(R.id.itemListView);
        itemList = new ArrayList<Item>();
        adapter = new ManagementListAdapter(getContext().getApplicationContext(), itemList, this);
        itemListView.setAdapter(adapter);

        new NumberBackgroundTask().execute(); // 사용자가 등록한 물품 갯수

        totalNumberMonday = 0;
        numberMonday = (TextView) getView().findViewById(R.id.totalNumberMonday);

        totalNumberTuesday = 0;
        numberTuesday = (TextView) getView().findViewById(R.id.totalNumberTuesday);

        totalNumberWednesday = 0;
        numberWednesday = (TextView) getView().findViewById(R.id.totalNumberWednesday);

        totalNumberThursday = 0;
        numberThursday = (TextView) getView().findViewById(R.id.totalNumberThursday);

        totalNumberFriday = 0;
        numberFriday = (TextView) getView().findViewById(R.id.totalNumberFriday);

        totalNumberSaturday = 0;
        numberSaturday = (TextView) getView().findViewById(R.id.totalNumberSaturday);

        totalNumberSunday = 0;
        numberSunday = (TextView) getView().findViewById(R.id.totalNumberSunday);

        totalPrice = 0;
        price = (TextView) getView().findViewById(R.id.totalPrice);

        managementListSpinner = (Spinner) getView().findViewById(R.id.managementListSpinner);
        managementListAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.list, android.R.layout.simple_spinner_item);
        managementListSpinner.setAdapter(managementListAdapter);
        managementListSpinner.setPopupBackgroundResource(R.color.colorPrimaryDark); //해당 스피널의 배경색 변경


        managementListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (managementListSpinner.getSelectedItem().equals("월")) {
                    itemList.clear();
                    new ManagementFragment.BackgroundTask().execute();
                }
                if (managementListSpinner.getSelectedItem().equals("화")) {
                    itemList.clear();
                    new ManagementFragment.BackgroundTask().execute();
                }
                if (managementListSpinner.getSelectedItem().equals("수")) {
                    itemList.clear();
                    new ManagementFragment.BackgroundTask().execute();
                }
                if (managementListSpinner.getSelectedItem().equals("목")) {
                    itemList.clear();
                    new ManagementFragment.BackgroundTask().execute();
                }
                if (managementListSpinner.getSelectedItem().equals("금")) {
                    itemList.clear();
                    new ManagementFragment.BackgroundTask().execute();
                }
                if (managementListSpinner.getSelectedItem().equals("토")) {
                    itemList.clear();
                    new ManagementFragment.BackgroundTask().execute();
                }
                if (managementListSpinner.getSelectedItem().equals("일")) {
                    itemList.clear();
                    new ManagementFragment.BackgroundTask().execute();
                }
                managementListAdapter.notifyDataSetChanged(); // 갱신
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        final RadioGroup itemBuyOrYetGroup = (RadioGroup) getView().findViewById(R.id.itemBuyOrYetGroup);

        itemBuyOrYetGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                final RadioButton buyyetButton = (RadioButton) getView().findViewById(checkedId);
                itemBuyOrYet = buyyetButton.getText().toString();


                itemList.clear();
                new ManagementFragment.BackgroundTask().execute();

                managementListAdapter.notifyDataSetChanged(); // 갱신

            }
        });

        barcodeTextView = (TextView) getView().findViewById(R.id.barcodeTextView);
        final EditText itemNameEditText = (EditText) getView().findViewById(R.id.itemNameEditText);
        final EditText priceEditText = (EditText) getView().findViewById(R.id.priceEditText);
        final EditText amountEditText = (EditText) getView().findViewById(R.id.amountEditText);

        barcodeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Barcode.class);
                intent.putExtra("userID", userID);
                startActivityForResult(intent, 1);
            }
        });

        Button resetButton = (Button) getView().findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                barcodeTextView.setText(null);
                itemNameEditText.setText("");
                priceEditText.setText("");
                amountEditText.setText("");

                barcodeTextView.getText().toString();
                itemNameEditText.getText().toString();
                priceEditText.getText().toString();
                amountEditText.getText().toString();
            }
        });

        Button managementAddButton = (Button) getView().findViewById(R.id.managementAddButton);
        managementAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String itemBarcode = barcodeTextView.getText().toString();
                final String itemName = itemNameEditText.getText().toString();

                boolean NumberValidate; // 수량 및 가격이 숫자인지 타당성

                try {
                    int validatePrice;
                    int validateAmount;

                    if (priceEditText.getText().toString().equals("")) {
                        validatePrice = 0;
                    } else {
                        validatePrice = Integer.parseInt(priceEditText.getText().toString());
                    }
                    if (amountEditText.getText().toString().equals("")) {
                        validateAmount = 1;
                    } else {
                        validateAmount = Integer.parseInt(amountEditText.getText().toString());
                    }
                    ;

                    NumberValidate = true;
                } catch (NumberFormatException nfe) {
                    NumberValidate = false;
                }

                if (itemName.equals("")) {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                    android.support.v7.app.AlertDialog dialog = builder.setMessage("물품명을 입력하세요.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                } else if (!NumberValidate) {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                    android.support.v7.app.AlertDialog dialog = builder.setMessage("숫자를 입력하세요.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                } else {
                    Response.Listener<String> responseNameBooleanListener = new Response.Listener<String>() { // 해당 유저, 리스트에 물품명이 존재?
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) { // 해당 유저, 리스트에 물품명이 존재하지 않는다면
                                    Response.Listener<String> responseBarcodeBooleanListener = new Response.Listener<String>() { // 해당 유저, 리스트에 바코드가 존재?
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonResponse = new JSONObject(response);
                                                boolean success = jsonResponse.getBoolean("success");
                                                if (success) { // 해당 유저, 리스트 바코드명이 존재하지 않는다면
                                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject jsonResponse = new JSONObject(response);
                                                                boolean success = jsonResponse.getBoolean("success");
                                                                if (success) {
                                                                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                                                                    android.support.v7.app.AlertDialog dialog = builder.setMessage("물품이 추가되었습니다.")
                                                                            .setPositiveButton("확인", null)
                                                                            .create();
                                                                    dialog.show();
                                                                    if (managementListSpinner.getSelectedItem().equals("월")) {
                                                                        ManagementFragment.totalNumberMonday += 1;
                                                                        ManagementFragment.numberMonday.setText("[" + ManagementFragment.totalNumberMonday + "]");
                                                                    } else if (managementListSpinner.getSelectedItem().equals("화")) {
                                                                        ManagementFragment.totalNumberTuesday += 1;
                                                                        ManagementFragment.numberTuesday.setText("[" + ManagementFragment.totalNumberTuesday + "]");
                                                                    } else if (managementListSpinner.getSelectedItem().equals("수")) {
                                                                        ManagementFragment.totalNumberWednesday += 1;
                                                                        ManagementFragment.numberWednesday.setText("[" + ManagementFragment.totalNumberWednesday + "]");
                                                                    } else if (managementListSpinner.getSelectedItem().equals("목")) {
                                                                        ManagementFragment.totalNumberThursday += 1;
                                                                        ManagementFragment.numberThursday.setText("[" + ManagementFragment.totalNumberThursday + "]");
                                                                    } else if (managementListSpinner.getSelectedItem().equals("금")) {
                                                                        ManagementFragment.totalNumberFriday += 1;
                                                                        ManagementFragment.numberFriday.setText("[" + ManagementFragment.totalNumberFriday + "]");
                                                                    } else if (managementListSpinner.getSelectedItem().equals("토")) {
                                                                        ManagementFragment.totalNumberSaturday += 1;
                                                                        ManagementFragment.numberSaturday.setText("[" + ManagementFragment.totalNumberSaturday + "]");
                                                                    } else if (managementListSpinner.getSelectedItem().equals("일")) {
                                                                        ManagementFragment.totalNumberSunday += 1;
                                                                        ManagementFragment.numberSunday.setText("[" + ManagementFragment.totalNumberSunday + "]");
                                                                    } else {
                                                                    }
                                                                    itemList.clear();
                                                                    new ManagementFragment.BackgroundTask().execute();
                                                                    managementListAdapter.notifyDataSetChanged(); // 갱신
                                                                } else {
                                                                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                                                                    android.support.v7.app.AlertDialog dialog = builder.setMessage("물품을 추가하는데 실패하셨습니다.")
                                                                            .setPositiveButton("다시 시도", null)
                                                                            .create();
                                                                    dialog.show();
                                                                }
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    };
                                                    String list = managementListSpinner.getSelectedItem().toString();
                                                    int itemPrice;
                                                    int itemAmount;
                                                    String itemAddDate = getTime();

                                                    if (priceEditText.getText().toString().equals("")) {
                                                        itemPrice = 0;
                                                    } else {
                                                        itemPrice = Integer.parseInt(priceEditText.getText().toString());
                                                    }
                                                    if (amountEditText.getText().toString().equals("")) {
                                                        itemAmount = 1;
                                                    } else {
                                                        itemAmount = Integer.parseInt(amountEditText.getText().toString());
                                                    }

                                                    ManagementAddRequest managementAddRequest = new ManagementAddRequest(userID, itemBarcode, itemName, list, itemPrice + "", itemAmount + "", itemAddDate, responseListener);
                                                    RequestQueue queue = Volley.newRequestQueue(getActivity());
                                                    queue.add(managementAddRequest);
                                                } else { // 바코드가 존재한다면
                                                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                                                    android.support.v7.app.AlertDialog dialog = builder.setMessage("이미 존재하는 바코드 입니다.")
                                                            .setPositiveButton("다시 시도", null)
                                                            .create();
                                                    dialog.show();
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    };

                                    String list = managementListSpinner.getSelectedItem().toString();

                                    ManagementAddBarcodeBooleanRequest managementAddBarcodeBooleanRequest = new ManagementAddBarcodeBooleanRequest(userID, itemBarcode, list, responseBarcodeBooleanListener);
                                    RequestQueue queue = Volley.newRequestQueue(getActivity());
                                    queue.add(managementAddBarcodeBooleanRequest);
                                } else { // 물품명이 존재한다면
                                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                                    android.support.v7.app.AlertDialog dialog = builder.setMessage("이미 존재하는 물품명 입니다.")
                                            .setPositiveButton("다시 시도", null)
                                            .create();
                                    dialog.show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    String list = managementListSpinner.getSelectedItem().toString();

                    ManagementAddNameBooleanRequest managementAddNameBooleanRequest = new ManagementAddNameBooleanRequest(userID, itemName, list, responseNameBooleanListener);
                    RequestQueue queue = Volley.newRequestQueue(getActivity());
                    queue.add(managementAddNameBooleanRequest);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //데이터 받기
                String result = data.getStringExtra("result");
                barcodeTextView.setText(result.toString());
            }
        }
    }

    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }


    static class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            try {
                target = "http://skatkdals5.cafe24.com/ManagementItemList.php?userID=" + URLEncoder.encode(MainActivity.userID, "UTF-8") +
                        "&list=" + URLEncoder.encode(managementListSpinner.getSelectedItem().toString(), "UTF-8") +
                        "&itemBuyOrYet=" + URLEncoder.encode(itemBuyOrYet, "UTF-8");
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
                itemList.clear();
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                totalPrice = 0;

                int itemID; // 물품 고유 번호
                String list; // 리스트
                String itemBarcode; // 물품 바코드
                String itemName; // 물품명
                String itemAddDate; // 물품 등록 날짜
                String itemUpdateDate; // 물품 변경 날짜
                String itemBuyOrYet; // 구입 여부
                int itemPrice; // 물품 가격
                int itemAmount; // 물품 수량

                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    itemID = object.getInt("itemID");
                    itemBarcode = object.getString("itemBarcode");
                    itemName = object.getString("itemName");
                    itemAddDate = object.getString("itemAddDate");
                    itemUpdateDate = object.getString("itemUpdateDate");
                    list = object.getString("list");
                    itemPrice = object.getInt("itemPrice");
                    itemAmount = object.getInt("itemAmount");
                    itemBuyOrYet = object.getString("itemBuyOrYet");

                    ManagementFragment.totalPrice += itemPrice * itemAmount; // 총 가격의 합

                    itemList.add(new Item(itemID, itemBarcode, itemName, itemAddDate, itemUpdateDate, list, itemPrice, itemAmount, itemBuyOrYet));
                    count++;
                }
                price.setText(totalPrice + "원");
                adapter.notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class NumberBackgroundTask extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            try {
                target = "http://skatkdals5.cafe24.com/ManagementItemNumberList.php?userID=" + URLEncoder.encode(MainActivity.userID, "UTF-8");
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
                itemList.clear();
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;

                String list; // 리스트명
                int itemNumber; // 물건 갯수
                totalNumberMonday = 0;
                totalNumberTuesday = 0;
                totalNumberWednesday = 0;
                totalNumberThursday = 0;
                totalNumberFriday = 0;
                totalNumberSaturday = 0;
                totalNumberSunday = 0;

                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);

                    list = object.getString("list");
                    itemNumber = object.getInt("COUNT(userID)");

                    if (list.equals("월")) {
                        totalNumberMonday += itemNumber;
                    } else if (list.equals("화")) {
                        totalNumberTuesday += itemNumber;
                    } else if (list.equals("수")) {
                        totalNumberWednesday += itemNumber;
                    } else if (list.equals("목")) {
                        totalNumberThursday += itemNumber;
                    } else if (list.equals("금")) {
                        totalNumberFriday += itemNumber;
                    } else if (list.equals("토")) {
                        totalNumberSaturday += itemNumber;
                    } else if (list.equals("일")) {
                        totalNumberSunday += itemNumber;
                    } else {
                    }

                    itemList.add(new Item(list, itemNumber));
                    count++;
                }
                adapter.notifyDataSetChanged();

                TextView numberMonday = (TextView) getView().findViewById(R.id.totalNumberMonday);
                numberMonday.setText("[" + totalNumberMonday + "]");
                if (totalNumberMonday == 0) {
                    numberMonday.setTextColor(getResources().getColor(R.color.colorSafe));
                } else if (totalNumberMonday >= 1) {
                    numberMonday.setTextColor(getResources().getColor(R.color.colorBlack));
                } else if (totalNumberMonday >= 100) {
                    numberMonday.setTextColor(getResources().getColor(R.color.colorRed));
                }

                TextView numberTuesday = (TextView) getView().findViewById(R.id.totalNumberTuesday);
                numberTuesday.setText("[" + totalNumberTuesday + "]");
                if (totalNumberTuesday == 0) {
                    numberTuesday.setTextColor(getResources().getColor(R.color.colorSafe));
                } else if (totalNumberTuesday >= 1) {
                    numberTuesday.setTextColor(getResources().getColor(R.color.colorBlack));
                } else if (totalNumberTuesday >= 100) {
                    numberTuesday.setTextColor(getResources().getColor(R.color.colorRed));
                }

                TextView numberWednesday = (TextView) getView().findViewById(R.id.totalNumberWednesday);
                numberWednesday.setText("[" + totalNumberWednesday + "]");
                if (totalNumberWednesday == 0) {
                    numberWednesday.setTextColor(getResources().getColor(R.color.colorSafe));
                } else if (totalNumberWednesday >= 1) {
                    numberWednesday.setTextColor(getResources().getColor(R.color.colorBlack));
                } else if (totalNumberWednesday >= 100) {
                    numberWednesday.setTextColor(getResources().getColor(R.color.colorRed));
                }

                TextView numberThursday = (TextView) getView().findViewById(R.id.totalNumberThursday);
                numberThursday.setText("[" + totalNumberThursday + "]");
                if (totalNumberThursday == 0) {
                    numberThursday.setTextColor(getResources().getColor(R.color.colorSafe));
                } else if (totalNumberThursday >= 1) {
                    numberThursday.setTextColor(getResources().getColor(R.color.colorBlack));
                } else if (totalNumberThursday >= 100) {
                    numberThursday.setTextColor(getResources().getColor(R.color.colorRed));
                }

                TextView numberFriday = (TextView) getView().findViewById(R.id.totalNumberFriday);
                numberFriday.setText("[" + totalNumberFriday + "]");
                if (totalNumberFriday == 0) {
                    numberFriday.setTextColor(getResources().getColor(R.color.colorSafe));
                } else if (totalNumberFriday >= 1) {
                    numberFriday.setTextColor(getResources().getColor(R.color.colorBlack));
                } else if (totalNumberFriday >= 100) {
                    numberFriday.setTextColor(getResources().getColor(R.color.colorRed));
                }

                TextView numberSaturday = (TextView) getView().findViewById(R.id.totalNumberSaturday);
                numberSaturday.setText("[" + totalNumberSaturday + "]");
                if (totalNumberSaturday == 0) {
                    numberSaturday.setTextColor(getResources().getColor(R.color.colorSafe));
                } else if (totalNumberSaturday >= 1) {
                    numberSaturday.setTextColor(getResources().getColor(R.color.colorBlack));
                } else if (totalNumberSaturday >= 100) {
                    numberSaturday.setTextColor(getResources().getColor(R.color.colorRed));
                }

                TextView numberSunday = (TextView) getView().findViewById(R.id.totalNumberSunday);
                numberSunday.setText("[" + totalNumberSunday + "]");
                if (totalNumberSunday == 0) {
                    numberSunday.setTextColor(getResources().getColor(R.color.colorSafe));
                } else if (totalNumberSunday >= 1) {
                    numberSunday.setTextColor(getResources().getColor(R.color.colorBlack));
                } else if (totalNumberSunday >= 100) {
                    numberSunday.setTextColor(getResources().getColor(R.color.colorRed));
                }
            } catch (
                    Exception e)

            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_management, container, false);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}