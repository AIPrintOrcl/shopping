package com.example.user.shopping;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SearchListAdapter extends BaseAdapter {

    private Context context;
    private List<Item> itemList;
    private Fragment parent;
    private String userID = MainActivity.userID;
    private List<Item> saveItemNameList;

    // 현재 시간
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

    public SearchListAdapter(Context context, List<Item> itemList, Fragment parent, List<Item> saveItemNameList) {
        this.context = context;
        this.itemList = itemList;
        this.parent = parent;
        this.saveItemNameList = saveItemNameList;
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
        View v = View.inflate(context, R.layout.search, null);
        TextView searchTextView = (TextView) v.findViewById(R.id.searchTextView);
        TextView listName = (TextView) v.findViewById(R.id.listName);
        final TextView itemName = (TextView) v.findViewById(R.id.itemName);
        TextView itemAddDate = (TextView) v.findViewById(R.id.itemAddDate);
        TextView itemUpdateDate = (TextView) v.findViewById(R.id.itemUpdateDate);
        TextView itemPrice = (TextView) v.findViewById(R.id.itemPrice);
        TextView itemAmount = (TextView) v.findViewById(R.id.itemAmount);
        TextView itemBarcode = (TextView) v.findViewById(R.id.itemBarcode);
        TextView itemSum = (TextView) v.findViewById(R.id.itemSum);
        final TextView itemBuyOrYetText = (TextView) v.findViewById(R.id.itemBuyOrYetText);

        searchTextView.setText("" + (position + 1) + "");

        listName.setText("[" + itemList.get(position).getList() + "]");
        itemName.setText(itemList.get(position).getItemName());

        itemAddDate.setText(itemList.get(position).getItemAddDate());
        itemUpdateDate.setText(itemList.get(position).getItemUpdateDate());

        final int price = itemList.get(position).getItemPrice();

        if (price == 0) {
            itemPrice.setText(" - ");
        } else {
            itemPrice.setText(price + " 원");
        }

        itemAmount.setText(itemList.get(position).getItemAmount() + " 개");

        itemSum.setText("= " + itemList.get(position).getItemPrice() * itemList.get(position).getItemAmount() + "원");

        itemBarcode.setText(itemList.get(position).getItemBarcode());

        itemBuyOrYetText.setText(itemList.get(position).getItemBuyOrYet() + "");

        v.setTag(itemList.get(position).getItemID());

        final String itemBuyOrYet = itemList.get(position).getItemBuyOrYet();

        Button deleteButton = (Button) v.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());
                                builder.setTitle("물품 삭제");
                                AlertDialog dialog = builder.setMessage("물품을 삭제 하시겠습니까?")
                                        .setCancelable(false)
                                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                itemList.remove(position);
                                                for (int i = 0; i < saveItemNameList.size(); i++) {
                                                    if (saveItemNameList.get(i).getItemName().equals(itemName.getText().toString())) {
                                                        saveItemNameList.remove(i);
                                                        break;
                                                    }
                                                }
                                                notifyDataSetChanged();
                                            }
                                        })
                                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        })
                                        .create();
                                dialog.show();

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());
                                AlertDialog dialog = builder.setMessage("물품이 삭제에 실패하였습니다.")
                                        .setPositiveButton("다시 시도", null)
                                        .create();
                                dialog.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                DeleteRequest deleteRequest = new DeleteRequest(userID, itemList.get(position).getItemID() + "", responseListener);
                RequestQueue queue = Volley.newRequestQueue(parent.getActivity());
                queue.add(deleteRequest);
            }
        });

        Button itemPriceAmountButton = (Button) v.findViewById(R.id.itemPriceAmountButton);
        itemPriceAmountButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());

                builder.setTitle(itemList.get(position).getItemName() + "");

                LinearLayout layout = new LinearLayout(parent.getContext());
                layout.setOrientation(LinearLayout.VERTICAL);

                TextView itemPriceTextView = new TextView(parent.getContext()); // 가격
                itemPriceTextView.setText("가격");
                layout.addView(itemPriceTextView);

                final EditText itemPriceEditText = new EditText(parent.getContext()); // 변경할 가격
                itemPriceEditText.setText(itemList.get(position).getItemPrice() + "");
                itemPriceEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                layout.addView(itemPriceEditText);

                TextView itemAmountTextView = new TextView(parent.getContext()); // 수량
                itemAmountTextView.setText("수량");
                layout.addView(itemAmountTextView);

                final EditText itemAmountEditText = new EditText(parent.getContext());
                itemAmountEditText.setText(itemList.get(position).getItemAmount() + "");
                itemAmountEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                layout.addView(itemAmountEditText);

                builder.setView(layout);

                AlertDialog dialog = builder.setPositiveButton("변경", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Response.Listener<String> responseListener = new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {

                                        itemList.remove(position);
                                        itemList.clear();
                                        saveItemNameList.clear();
                                        new SearchFragment.BackgroundTask().execute();
                                        notifyDataSetChanged();

                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());
                                        AlertDialog dialog = builder.setMessage("변경하는데 실패 하였습니다.")
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

                        ItemPriceAmountUpdateRequest itemPriceAmountUpdateRequest = new ItemPriceAmountUpdateRequest(userID, itemList.get(position).getItemID() + "", itemPrice + "", itemAmount + "", responseListener);
                        RequestQueue queue = Volley.newRequestQueue(parent.getActivity());
                        queue.add(itemPriceAmountUpdateRequest);

                    }
                })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .create();
                dialog.show();
            }
        });

        final Button itemBuyOrYetButton = (Button) v.findViewById(R.id.itemBuyOrYetButton);
        itemBuyOrYetButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                if (itemBuyOrYet.equals("YET")) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());
                                    AlertDialog dialog = builder.setMessage("해당 물품을 구입 했습니다.")
                                            .setPositiveButton("확인", null)
                                            .create();
                                    dialog.show();

                                    itemList.remove(position);
                                    for (int i = 0; i < saveItemNameList.size(); i++) {
                                        if (saveItemNameList.get(i).getItemName().equals(itemName.getText().toString())) {
                                            saveItemNameList.remove(i);
                                            break;
                                        }
                                    }
                                    notifyDataSetChanged();

                                } else if (itemBuyOrYet.equals("BUY")) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());
                                    AlertDialog dialog = builder.setMessage("해당 물품을 미구입 했습니다.")
                                            .setPositiveButton("확인", null)
                                            .create();
                                    dialog.show();

                                    itemList.remove(position);
                                    notifyDataSetChanged();
                                }

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());
                                AlertDialog dialog = builder.setMessage("해당 버튼 실행하는데 실패 하였습니다.")
                                        .setPositiveButton("다시 시도", null)
                                        .create();
                                dialog.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                String itembuyoryet = "";
                String itemUpdateDate = getTime();

                if (itemBuyOrYet.equals("YET")) {
                    itembuyoryet = "BUY";
                } else if (itemBuyOrYet.equals("BUY")) {
                    itembuyoryet = "YET";
                }

                ItemBuyOrYetRequest itemBuyOrYetRequest = new ItemBuyOrYetRequest(userID, itemList.get(position).getItemID() + "", itembuyoryet, itemUpdateDate, responseListener);
                RequestQueue queue = Volley.newRequestQueue(parent.getActivity());
                queue.add(itemBuyOrYetRequest);
            }
        });

        return v;
    }

    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }
}
