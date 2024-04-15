package com.example.user.shopping;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ItemFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ItemFragment newInstance(String param1, String param2) {
        ItemFragment fragment = new ItemFragment();
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

    public static ArrayAdapter itemListAdapter;
    public static Spinner itemListSpinner; // 등록할 리스트별
    private ArrayAdapter seasonAdapter;
    private Spinner seasonSpinner; // 제철별
    private ArrayAdapter foodKindAdapter;
    private Spinner foodKindSpinner; // 식품 종류

    private String item = "";

    private ListView itemListView;
    private ItemListAdapter adapter;
    private List<Item> itemList;

    @Override
    public  void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);

        itemListSpinner = (Spinner) getView().findViewById(R.id.itemListSpinner);
        seasonSpinner = (Spinner) getView().findViewById(R.id.seasonSpinner);
        foodKindSpinner = (Spinner) getView().findViewById(R.id.foodKindSpinner);


        itemListAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.list, android.R.layout.simple_spinner_item);
        itemListSpinner.setAdapter(itemListAdapter);
        itemListSpinner.setPopupBackgroundResource(R.color.colorPrimaryDark);

        seasonAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.foodSeason, android.R.layout.simple_spinner_item);
        seasonSpinner.setAdapter(seasonAdapter);
        seasonSpinner.setPopupBackgroundResource(R.color.colorPrimaryDark);

        foodKindAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.foodKind, android.R.layout.simple_spinner_item);
        foodKindSpinner.setAdapter(foodKindAdapter);
        foodKindSpinner.setPopupBackgroundResource(R.color.colorPrimaryDark);

        itemListView = (ListView) getView().findViewById(R.id.itemListView);
        itemList = new ArrayList<Item>();
        adapter = new ItemListAdapter(getContext().getApplicationContext(), itemList, this);
        itemListView.setAdapter(adapter);

        Button searchButton = (Button) getView().findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if(seasonSpinner.getSelectedItem().equals("모든계절")) // 계절에서 모든계절 선택 시
                    {
                        new ByItemAllSeason().execute();
                    }
                    else { new ByItemSeason().execute(); } // 계절 봄, 여름, 가을, 겨울 선택 시 전자, 가구도 해당
            }
        });
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item, container, false);
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

    class ByItemAllSeason extends AsyncTask<Void, Void, String> { // 구입처에서만 전체 클릭 시
        String target;

        @Override
        protected void onPreExecute() {
            try {
                target = "http://skatkdals5.cafe24.com/ItemAllSeasonList.php?foodKind=" + URLEncoder.encode(foodKindSpinner.getSelectedItem().toString(), "UTF-8");
            }
            catch (Exception e)
            {
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
                while ((temp = bufferedReader.readLine()) != null)
                {
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
        public void onProgressUpdate(Void... values)
        {
            super.onProgressUpdate(values);
        }

        @Override
        public void onPostExecute(String result) {
            try {
                itemList.clear();
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;

                int foodID; // 식품 고유 번호
                String foodKind; // 식품 종류
                String foodRace; // 식품 특성
                String foodSeason; // 제철
                String itemName; // 물품명
                String itemBarcode; // 물품 바코드

                while (count < jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(count);
                    foodID = object.getInt("foodID");
                    foodKind = object.getString("foodKind");
                    foodRace = object.getString("foodRace");
                    foodSeason = object.getString("foodSeason");
                    itemName = object.getString("itemName");
                    itemBarcode = object.getString("itemBarcode");

                    Item itemm = new Item(foodID, foodKind, foodRace, foodSeason, itemName, itemBarcode);
                    itemList.add(itemm);
                    count++;
                }
                if(count == 0)
                {
                    AlertDialog dialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(ItemFragment.this.getActivity());
                    dialog = builder.setMessage("조회된 물품이 없습니다.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                }
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class ByItemSeason extends AsyncTask<Void, Void, String> { // 구입처에서만 전체 클릭 시
        String target;

        @Override
        protected void onPreExecute() {
            try {
                target = "http://skatkdals5.cafe24.com/ItemList.php?foodSeason=" + URLEncoder.encode(seasonSpinner.getSelectedItem().toString(), "UTF-8") +
                        "&foodKind=" + URLEncoder.encode(foodKindSpinner.getSelectedItem().toString(), "UTF-8");
            }
            catch (Exception e)
            {
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
                while ((temp = bufferedReader.readLine()) != null)
                {
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
        public void onProgressUpdate(Void... values)
        {
            super.onProgressUpdate(values);
        }

        @Override
        public void onPostExecute(String result) {
            try {
                itemList.clear();
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;

                int foodID; // 식품 고유 번호
                String foodKind; // 식품 종류
                String foodRace; // 식품 특성
                String foodSeason; // 제철
                String itemName; // 물품명
                String itemBarcode; // 물품 바코드

                while (count < jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(count);
                    foodID = object.getInt("foodID");
                    foodKind = object.getString("foodKind");
                    foodRace = object.getString("foodRace");
                    foodSeason = object.getString("foodSeason");
                    itemName = object.getString("itemName");
                    itemBarcode = object.getString("itemBarcode");

                    Item itemm = new Item(foodID, foodKind, foodRace, foodSeason, itemName, itemBarcode);
                    itemList.add(itemm);
                    count++;
                }
                if(count == 0)
                {
                    AlertDialog dialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(ItemFragment.this.getActivity());
                    dialog = builder.setMessage("조회된 물품이 없습니다.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                }
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
