package com.example.user.shopping;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

import java.text.SimpleDateFormat;
import java.util.Date;

public class ItemListAdapter extends BaseAdapter {

    private Context context;
    private List<Item> itemList;
    private Fragment parent;
    private String userID = MainActivity.userID;
    private Management management = new Management();
    private List<Integer> foodIDList; // 물품 아이디가 중복 되는지 검사하기 위한

    // 현재 시간
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");


    public ItemListAdapter(Context context, List<Item> itemList, Fragment parent) {
        this.context = context;
        this.itemList = itemList;
        this.parent = parent;
        management = new Management();
        foodIDList = new ArrayList<Integer>();
        new BackgroundTask().execute();
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.item, null);
        TextView itemTextView = (TextView) v.findViewById(R.id.itemTextView);
        final TextView itemName = (TextView) v.findViewById(R.id.itemName);
        final TextView itemBarcode = (TextView) v.findViewById(R.id.itemBarcode);
        TextView foodRace = (TextView) v.findViewById(R.id.foodRace);
        TextView foodSeason = (TextView) v.findViewById(R.id.foodSeason);
        final EditText itemPriceEditText = (EditText) v.findViewById(R.id.itemPriceEditText);
        final EditText itemAmountEditText = (EditText) v.findViewById(R.id.itemAmountEditText);

        itemTextView.setText("" + (position + 1) + "");

        itemName.setText(itemList.get(position).getItemName()); // 물품명
        itemBarcode.setText(itemList.get(position).getItemBarcode()); // 물품 바코드
        foodRace.setText(itemList.get(position).getFoodRace()); // 해당 물품의 특성
        foodSeason.setText("[" + itemList.get(position).getFoodSeason() + "]"); // 해당 물품의 제철

        v.setTag(itemList.get(position).getFoodID());

        Button addButton = (Button) v.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                boolean validate = false; // 물품을 추가 할 수 있는지 타당성
                boolean NumberValidate; // 수량 및 가격이 숫자인지 타당성
                validate = management.validate(itemList.get(position).getFoodID());

                try {
                    int validatePrice;
                    int validateAmount;

                    if(itemPriceEditText.getText().toString().equals(""))
                    {
                        validatePrice = 0;
                    }
                    else {
                        validatePrice = Integer.parseInt(itemPriceEditText.getText().toString());
                    }
                    if(itemAmountEditText.getText().toString().equals(""))
                    {
                        validateAmount = 1;
                    }
                    else {
                        validateAmount = Integer.parseInt(itemAmountEditText.getText().toString());
                    };

                    NumberValidate = true;
                } catch(NumberFormatException nfe) {
                    NumberValidate = false;
                }

                if (!alreadyIn(foodIDList, itemList.get(position).getFoodID())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());
                    AlertDialog dialog = builder.setMessage("이미 추가된 물품입니다.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                }
                else if(!NumberValidate)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());
                    AlertDialog dialog = builder.setMessage("숫자를 입력하세요.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                }
                else {

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());
                                    AlertDialog dialog = builder.setMessage("물품이 추가되었습니다.")
                                            .setPositiveButton("확인", null)
                                            .create();
                                    dialog.show();
                                    foodIDList.add(itemList.get(position).getFoodID());
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());
                                    AlertDialog dialog = builder.setMessage("물품이 추가에 실패하였습니다.")
                                            .setPositiveButton("다시 시도", null)
                                            .create();
                                    dialog.show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    int itemPrice;
                    int itemAmount;
                    String itemAddDate = getTime();

                    if(itemPriceEditText.getText().toString().equals(""))
                    {
                        itemPrice = 0;
                    }
                    else {
                        itemPrice = Integer.parseInt(itemPriceEditText.getText().toString());
                    }
                    if(itemAmountEditText.getText().toString().equals(""))
                    {
                        itemAmount = 1;
                    }
                    else {
                        itemAmount = Integer.parseInt(itemAmountEditText.getText().toString());
                    };



                    AddRequest addRequest = new AddRequest(userID, itemList.get(position).getItemName(), ItemFragment.itemListSpinner.getSelectedItem().toString(), itemList.get(position).getFoodID() + "", itemPrice + "", itemAmount + "", itemList.get(position).getItemBarcode() + "", itemAddDate, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(parent.getActivity());
                    queue.add(addRequest);
                }
            }
        });

        return v;
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> { // 물품 중복 X
        String target;

        @Override
        protected void onPreExecute() {
            try
            {
                target = "http://skatkdals5.cafe24.com/ManagementNotOverlapList.php?userID=" + URLEncoder.encode(MainActivity.userID, "UTF-8");
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
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;

                int foodID;

                while (count < jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(count);
                    foodID = object.getInt("foodID");
                    foodIDList.add(foodID);
                    count++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean alreadyIn(List<Integer> foodIDList, int n) // foodID가 현재 존재하면 체크하는 메소드
    {
        for(int i = 0; i < foodIDList.size(); i++)
        {
            if(foodIDList.get(i) == n)
            {
                return false;
            }
        }
        return  true;
    }

    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }
}
