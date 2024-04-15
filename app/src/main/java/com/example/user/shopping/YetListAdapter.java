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

public class YetListAdapter extends RecyclerView.Adapter<YetListAdapter.ViewHolder> {

    private Context context;
    private List<Item> yetItemList;
    private Fragment parent;
    private String userID = MainActivity.userID;

    public YetListAdapter(Context context, List<Item> yetItemList, Fragment parent) {
        this.context = context;
        this.yetItemList = yetItemList;
        this.parent = parent;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v;
        v = LayoutInflater.from(context).inflate(R.layout.cardveiw_yet_list, viewGroup, false);

        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.yetListTextView.setText("" + (position + 1) + "");
        holder.yetListImage.setImageResource(yetItemList.get(position).getListImage());
        holder.yetList.setText(" " + yetItemList.get(position).getList());
        holder.itemYetNumber.setText(yetItemList.get(position).getItemBuyYetNumber() + ""); // 해당 카테고리에 YET로 등록된 물품 가지 수

        holder.itemExpensePrice.setText(yetItemList.get(position).getItemBuyYetPrice() + "");

        holder.yetBuyListUpdateButton.setOnClickListener(new View.OnClickListener() {

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

                                builder.setTitle("리스트별 구입 변환");

                                AlertDialog dialog = builder.setMessage("해당 리스트에 등록된 물품을 구입으로 변환 하시겠습니까?")
                                        .setCancelable(false)
                                        .setPositiveButton("변환", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                yetItemList.remove(position);
                                                ListFragment.buyList.clear();
                                                new ListFragment.ByBuyList().execute();
                                                ListFragment.buyListAdapter.notifyDataSetChanged();

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
                ListYetBuyUpdateRequest listYetBuyUpdateRequest = new ListYetBuyUpdateRequest(userID, yetItemList.get(position).getList(), responseListener);
                RequestQueue queue = Volley.newRequestQueue(parent.getActivity());
                queue.add(listYetBuyUpdateRequest);
            }
        });

        holder.yetListDeleteButton.setOnClickListener(new View.OnClickListener() {

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
                                                yetItemList.remove(position);
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
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                ListYetDeleteRequest listYetDeleteRequest = new ListYetDeleteRequest(userID, yetItemList.get(position).getList(), responseListener);
                RequestQueue queue = Volley.newRequestQueue(parent.getActivity());
                queue.add(listYetDeleteRequest);
            }
        });
    }

    @Override
    public int getItemCount() {
        return yetItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView yetListTextView; // 리스트별 일련 번호
        private ImageView yetListImage; // 리스트 사진 및 이미지
        private TextView yetList; // 리스트명
        private TextView itemYetNumber; // 리스트별로 YET 등록된 물건 갯수

        private TextView itemExpensePrice; // YET 등록된 물품 가격

        private Button yetBuyListUpdateButton; // 리스트별 YET -> BUY 변환
        private Button yetListDeleteButton; // 리스트별 삭제

        public ViewHolder(View itemView) {
            super(itemView);

            yetListTextView = (TextView) itemView.findViewById(R.id.yetListTextView);
            yetListImage = (ImageView) itemView.findViewById(R.id.yetListImage);
            yetList = (TextView) itemView.findViewById(R.id.yetList);
            itemYetNumber = (TextView) itemView.findViewById(R.id.itemYetNumber);
            itemExpensePrice = (TextView) itemView.findViewById(R.id.itemExpensePrice);

            yetBuyListUpdateButton = (Button) itemView.findViewById(R.id.yetBuyListUpdateButton);
            yetListDeleteButton = (Button) itemView.findViewById(R.id.yetListDeleteButton);
        }
    }
}
