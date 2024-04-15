package com.example.user.shopping;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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

    private ListView itemSearchListView;
    public static SearchListAdapter SearchAdapter;
    public static List<Item> itemSearchList;
    public static List<Item> saveItemNameList; // DB와 검색에서 물품명이 다를 때

    public static String itemBuyOrYet = ""; // 구입 OR 아직
    private TextView itemBarcodeSearch;

    private String userID = MainActivity.userID;

    @Override
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);
        itemSearchListView = (ListView) getView().findViewById(R.id.itemSearchListView);
        itemSearchList = new ArrayList<Item>();
        saveItemNameList = new ArrayList<Item>();
        SearchAdapter = new SearchListAdapter(getContext().getApplicationContext(), itemSearchList, this, saveItemNameList);
        itemSearchListView.setAdapter(SearchAdapter);

        final EditText itemNameSearch = (EditText) getView().findViewById(R.id.itemNameSearch);
        itemBarcodeSearch = (TextView) getView().findViewById(R.id.itemBarcodeSearch);

        itemBarcodeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BarcodeSearch.class);
                startActivityForResult(intent, 1);
            }
        });


        itemNameSearch.addTextChangedListener(new TextWatcher() { // 텍스트에 변화가 있을 때
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (before == 0) {
                    itemBarcodeSearch.setText(null);

                    itemBarcodeSearch.getText().toString();

                    itemSearchList.clear();
                    saveItemNameList.clear();

                    new SearchFragment.BackgroundTask().execute();
                }
                searchItemName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final RadioGroup itemSearchBuyOrYetGroup = (RadioGroup) getView().findViewById(R.id.itemSearchBuyOrYetGroup);

        itemSearchBuyOrYetGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                final RadioButton buyyetButton = (RadioButton) getView().findViewById(checkedId);
                itemBuyOrYet = buyyetButton.getText().toString();

                itemSearchList.clear();
                saveItemNameList.clear();

                new SearchFragment.BackgroundTask().execute();

                if (itemNameSearch.getText().toString().equals("")) {

                } else {
                    searchItemName(itemNameSearch.getText().toString());
                }

                SearchAdapter.notifyDataSetChanged(); // 갱신

            }
        });


        Button barcodeSearchButton = (Button) getView().findViewById(R.id.barcodeSearchButton);
        barcodeSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String itemBarcode = itemBarcodeSearch.getText().toString();

                if (itemBarcode.equals("")) {
                    Toast.makeText(getActivity(), "바코드를 입력하세요", Toast.LENGTH_SHORT).show();
                } else {
                    Response.Listener<String> responseBooleanListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) { // 해당 유저, YET 또는 BUY에 있는 것 인지 판별
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) { // 해당 유저, 리스트, YET에 존재 한다면
                                    Toast.makeText(getActivity(), "물품을 검색 했습니다.", Toast.LENGTH_SHORT).show();

                                    itemSearchList.clear();
                                    saveItemNameList.clear();

                                    new SearchFragment.ByBarcode().execute();

                                    SearchAdapter.notifyDataSetChanged(); // 갱신

                                } else { // 존재 하지 않는 다면
                                    Response.Listener<String> responseBarcodeSeinListener = new Response.Listener<String>() { // 해당 사용자가 등록한 바코드 존재 여부
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonResponse = new JSONObject(response);
                                                boolean success = jsonResponse.getBoolean("success");
                                                if (success) {
                                                    Toast.makeText(getActivity(), "목록에 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getActivity(), "등록되어 있지 않은 바코드 입니다.", Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    };

                                    SearchBarcodeSeinRequest searchBarcodeSeinRequest = new SearchBarcodeSeinRequest(userID, itemBarcode, responseBarcodeSeinListener);
                                    RequestQueue queue = Volley.newRequestQueue(getActivity());
                                    queue.add(searchBarcodeSeinRequest);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    SearchBarcodeBuyOrYetBooleanRequest searchBarcodeBuyOrYetBooleanRequest = new SearchBarcodeBuyOrYetBooleanRequest(userID, itemBarcode, itemBuyOrYet, responseBooleanListener);
                    RequestQueue queue = Volley.newRequestQueue(getActivity());
                    queue.add(searchBarcodeBuyOrYetBooleanRequest);
                }
            }
        });

        Button resetSearchButton = (Button) getView().findViewById(R.id.resetSearchButton);
        resetSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itemBarcodeSearch.setText(null);
                itemNameSearch.setText("");

                itemBarcodeSearch.getText().toString();
                itemNameSearch.getText().toString();

                new SearchFragment.BackgroundTask().execute();
            }
        });
    }

    static class BackgroundTask extends AsyncTask<Void, Void, String> { // 물품명
        String target;

        @Override
        protected void onPreExecute() {
            try {
                target = "http://skatkdals5.cafe24.com/SearchItemList.php?userID=" + URLEncoder.encode(MainActivity.userID, "UTF-8") +
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
                itemSearchList.clear();
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;

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


                    itemSearchList.add(new Item(itemID, itemBarcode, itemName, itemAddDate, itemUpdateDate, list, itemPrice, itemAmount, itemBuyOrYet));
                    saveItemNameList.add(new Item(itemID, itemBarcode, itemName, itemAddDate, itemUpdateDate, list, itemPrice, itemAmount, itemBuyOrYet));
                    count++;
                }
                SearchAdapter.notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class ByBarcode extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            try {
                target = "http://skatkdals5.cafe24.com/ByBarcode.php?userID=" + URLEncoder.encode(MainActivity.userID, "UTF-8") +
                        "&itemBarcode=" + URLEncoder.encode(itemBarcodeSearch.getText().toString(), "UTF-8") +
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
                itemSearchList.clear();
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;

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

                    itemSearchList.add(new Item(itemID, itemBarcode, itemName, itemAddDate, itemUpdateDate, list, itemPrice, itemAmount, itemBuyOrYet));
                    count++;
                }
                SearchAdapter.notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void searchItemName(String searchItemName) {

        itemSearchList.clear();
        for (int i = 0; i < saveItemNameList.size(); i++) {
            if (saveItemNameList.get(i).getItemName().contains(searchItemName)) {
                itemSearchList.add(saveItemNameList.get(i));
            }
        }
        SearchAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //데이터 받기
                String result = data.getStringExtra("result");
                itemBarcodeSearch.setText(result.toString());
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
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
