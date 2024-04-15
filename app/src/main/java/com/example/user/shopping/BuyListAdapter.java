package com.example.user.shopping;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.List;

public class BuyListAdapter extends RecyclerView.Adapter<BuyListAdapter.ViewHolder> {

    private Context context;
    private List<Item> buyItemList;
    private Fragment parent;
    private String userID = MainActivity.userID;

    public BuyListAdapter(Context context, List<Item> buyItemList, Fragment parent) {
        this.context = context;
        this.buyItemList = buyItemList;
        this.parent = parent;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v;
        v = LayoutInflater.from(context).inflate(R.layout.cardveiw_buy_list, viewGroup, false);

        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.buyListTextView.setText("" + (position + 1) + "");
        holder.buyListImage.setImageResource(buyItemList.get(position).getListImage());
        holder.buyList.setText(" " + buyItemList.get(position).getList());
        holder.itemBuyNumber.setText(buyItemList.get(position).getItemBuyYetNumber() + ""); // 해당 카테고리에 YET로 등록된 물품 가지 수

        holder.itemBuyPrice.setText(buyItemList.get(position).getItemBuyYetPrice() + "");

        holder.buyYetListUpdateButton.setOnClickListener(new View.OnClickListener() {

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

                                builder.setTitle("리스트별 미구입 변환");

                                AlertDialog dialog = builder.setMessage("해당 리스트에 등록된 물품을 미구입으로 변환 하시겠습니까?")
                                        .setCancelable(false)
                                        .setPositiveButton("변환", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                ListFragment.yetList.clear();
                                                new ListFragment.ByYetList().execute();
                                                ListFragment.yetListAdapter.notifyDataSetChanged();
                                                buyItemList.remove(position);

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
                                AlertDialog dialog = builder.setMessage("변환하는데 실패하였습니다.")
                                        .setPositiveButton("다시 시도", null)
                                        .create();
                                dialog.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                ListBuyYetUpdateRequest listBuyYetUpdateRequest = new ListBuyYetUpdateRequest(userID, buyItemList.get(position).getList(), responseListener);
                RequestQueue queue = Volley.newRequestQueue(parent.getActivity());
                queue.add(listBuyYetUpdateRequest);
            }
        });

        holder.buyListDeleteButton.setOnClickListener(new View.OnClickListener() {

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

                                builder.setTitle("리스트별 삭제");

                                AlertDialog dialog = builder.setMessage("해당 리스트에 등록된 물품을 전부 삭제하시겠습니까?")
                                        .setCancelable(false)
                                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                buyItemList.remove(position);
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
                ListBuyDeleteRequest listBuyDeleteRequest = new ListBuyDeleteRequest(userID, buyItemList.get(position).getList(), responseListener);
                RequestQueue queue = Volley.newRequestQueue(parent.getActivity());
                queue.add(listBuyDeleteRequest);
            }
        });
    }

    @Override
    public int getItemCount() {
        return buyItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView buyListTextView; // 리스트별 일련 번호
        private ImageView buyListImage; // 리스트 사진 및 이미지
        private TextView buyList; // 리스트명
        private TextView itemBuyNumber; // 리스트별로 YET 등록된 물건 갯수

        private TextView itemBuyPrice; // YET 등록된 물품 가격

        private Button buyYetListUpdateButton; // 리스트별 BUY -> YET로 변환
        private Button buyListDeleteButton; // 리스트별 삭제

        public ViewHolder(View itemView) {
            super(itemView);

            buyListTextView = (TextView) itemView.findViewById(R.id.buyListTextView);
            buyListImage = (ImageView) itemView.findViewById(R.id.buyListImage);
            buyList = (TextView) itemView.findViewById(R.id.buyList);
            itemBuyNumber = (TextView) itemView.findViewById(R.id.itemBuyNumber);
            itemBuyPrice = (TextView) itemView.findViewById(R.id.itemBuyPrice);

            buyYetListUpdateButton = (Button) itemView.findViewById(R.id.buyYetListUpdateButton);
            buyListDeleteButton = (Button) itemView.findViewById(R.id.buyListDeleteButton);
        }
    }
}
