package com.example.shodan.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shodan.R;
import com.example.shodan.ShodanItem;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;
import java.util.concurrent.TimeoutException;

public class ShodanAdapter extends RecyclerView.Adapter<ShodanAdapter.ShodanItemViewHolder> {
    private ArrayList<ShodanItem> shodanItems;
    private OnShodanItemClickListener mShodanItemClickListener;

    public interface OnShodanItemClickListener {
        void onShodanItemClick(ShodanItem shodanItem);
    }

    public void updateShodanItems(ArrayList<ShodanItem> shodanItems) {
        this.shodanItems = shodanItems;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ShodanAdapter.ShodanItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int type) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.shodan_item, parent, false);
        return new ShodanItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShodanAdapter.ShodanItemViewHolder shodanItemViewHolder, int position) {
        shodanItemViewHolder.bind(shodanItems.get(position));
    }

    @Override
    public int getItemCount() {
        if (shodanItems != null) {
            return shodanItems.size();
        } else {
            return 0;
        }
    }

    public class ShodanItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView itemTitle;
        private TextView itemIP_Port;
        private TextView itemOrg_ISP;
        private TextView itemTimestamp;
        private TextView itemProduct;

        public ShodanItemViewHolder(View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemIP_Port = itemView.findViewById(R.id.item_ip_port);
            itemOrg_ISP = itemView.findViewById(R.id.item_org);
            itemTimestamp = itemView.findViewById(R.id.itemTimestamp);
            itemProduct = itemView.findViewById(R.id.item_product);
            itemView.setOnClickListener(this);
        }

        public void bind(ShodanItem shodanItem) {
            itemOrg_ISP.setText(shodanItem.organization);
            itemTitle.setText(shodanItem.title);
            itemIP_Port.setText("IP: " + shodanItem.ip + ", Port: " + shodanItem.port);
            itemTimestamp.setText(shodanItem.timestamp);
            itemProduct.setText("Product: " + shodanItem.product);
        }
        @Override
        public void onClick(View v) {
        }
    }
}
