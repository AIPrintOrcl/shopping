package com.example.user.shopping;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
 * {@link ListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView yetListView;
    public static YetListAdapter yetListAdapter;
    public static List<Item> yetList;

    private RecyclerView buyListView;
    public static BuyListAdapter buyListAdapter;
    public static List<Item> buyList;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);

        yetListView = (RecyclerView) view.findViewById(R.id.yetListRecyclerView);

        yetListAdapter = new YetListAdapter(getContext().getApplicationContext(), yetList, this);
        LinearLayoutManager yetLayoutManager = new LinearLayoutManager(getActivity());
        yetLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        yetListView.setHasFixedSize(true);
        yetListView.setLayoutManager(yetLayoutManager);
        yetListView.setAdapter(yetListAdapter);
        new ByYetList().execute();

        buyListView = (RecyclerView) view.findViewById(R.id.buyListRecyclerView);

        buyListAdapter = new BuyListAdapter(getContext().getApplicationContext(), buyList, this);
        LinearLayoutManager buyLayoutManager = new LinearLayoutManager(getActivity());
        buyLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        buyListView.setHasFixedSize(true);
        buyListView.setLayoutManager(buyLayoutManager);
        buyListView.setAdapter(buyListAdapter);
        new ByBuyList().execute();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        yetList = new ArrayList<>();
        buyList = new ArrayList<>();

    }

    public static class ByYetList extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            try {
                target = "http://skatkdals5.cafe24.com/ByYetList.php?userID=" + URLEncoder.encode(MainActivity.userID, "UTF-8");
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
                yetList.clear();
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;

                int listID; // 리스트 고유 번호
                String list; // 리스트명
                int listImage; // 이미지
                int itemBuyYetPrice; // YET로 등록된 물품 가격
                int itemBuyYetNumber; // YET로 등록된 물품 가지수

                while (count < jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(count);
                    listID = object.getInt("listID");
                    list = object.getString("list");
                    itemBuyYetPrice = object.getInt("itemBuyYetPrice");
                    itemBuyYetNumber = object.getInt("COUNT(MANAGEMENT2.itemBuyOrYet)");

                    if(list.equals("월"))
                    {
                        listImage = R.drawable.manlist_img;
                    }
                    else if(list.equals("화"))
                    {
                        listImage = R.drawable.tuelist_img;
                    }
                    else if(list.equals("수"))
                    {
                        listImage = R.drawable.wedlist_img;
                    }
                    else if(list.equals("목"))
                    {
                        listImage = R.drawable.thulist_img;
                    }
                    else if(list.equals("금"))
                    {
                        listImage = R.drawable.frilist_img;
                    }
                    else if(list.equals("토"))
                    {
                        listImage = R.drawable.satlist_img;
                    }
                    else if(list.equals("일"))
                    {
                        listImage = R.drawable.sunlist_img;
                    }
                    else listImage = R.drawable.ic_store_black_24dp;

                    yetList.add(new Item(listID, list, listImage, itemBuyYetPrice, itemBuyYetNumber));
                    count++;
                }
                yetListAdapter.notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class ByBuyList extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            try {
                target = "http://skatkdals5.cafe24.com/ByBuyList.php?userID=" + URLEncoder.encode(MainActivity.userID, "UTF-8");
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
                buyList.clear();
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;

                int listID; // 리스트 고유 번호
                String list; // 리스트명
                int listImage; // 이미지
                int itemBuyYetPrice; // BUY로 등록된 물품 가격
                int itemBuyYetNumber; // BUY로 등록된 물품 가지수

                while (count < jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(count);
                    listID = object.getInt("listID");
                    list = object.getString("list");
                    itemBuyYetPrice = object.getInt("itemBuyYetPrice");
                    itemBuyYetNumber = object.getInt("COUNT(MANAGEMENT2.itemBuyOrYet)");

                    if(list.equals("월"))
                    {
                        listImage = R.drawable.manlist_img;
                    }
                    else if(list.equals("화"))
                    {
                        listImage = R.drawable.tuelist_img;
                    }
                    else if(list.equals("수"))
                    {
                        listImage = R.drawable.wedlist_img;
                    }
                    else if(list.equals("목"))
                    {
                        listImage = R.drawable.thulist_img;
                    }
                    else if(list.equals("금"))
                    {
                        listImage = R.drawable.frilist_img;
                    }
                    else if(list.equals("토"))
                    {
                        listImage = R.drawable.satlist_img;
                    }
                    else if(list.equals("일"))
                    {
                        listImage = R.drawable.sunlist_img;
                    }
                    else listImage = R.drawable.ic_store_black_24dp;

                    buyList.add(new Item(listID, list, listImage, itemBuyYetPrice, itemBuyYetNumber));
                    count++;
                }
                buyListAdapter.notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
            }
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
