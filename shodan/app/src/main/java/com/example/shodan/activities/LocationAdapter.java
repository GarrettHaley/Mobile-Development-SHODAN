package com.example.shodan.activities;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shodan.R;
import com.example.shodan.data.Location;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationItemViewHolder> {

    private List<Location> mLocationItems;
    private OnLocationItemClickListener mLocationItemClickListener;

    public interface OnLocationItemClickListener {
        void onLocationItemClick(Location locationItem);
    }

    public LocationAdapter(OnLocationItemClickListener clickListener) {
        mLocationItemClickListener = clickListener;
    }

    public void updateLocationItems(List<Location> locationItems) {
        mLocationItems = locationItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mLocationItems != null) {
            return mLocationItems.size();
        } else {
            return 0;
        }
    }

    @Override
    public LocationItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.location_list, parent, false);
        return new LocationItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LocationItemViewHolder holder, int positoin) {
        holder.bind(mLocationItems.get(positoin));
    }

    public class LocationItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mLocationsTV;

        public LocationItemViewHolder(View itemView) {
            super(itemView);
            mLocationsTV = itemView.findViewById(R.id.tv_searched_location);
            itemView.setOnClickListener(this);
        }

        public void bind(Location locationItem) {
            mLocationsTV.setText(locationItem.search_location);
        }

        @Override
        public void onClick(View view) {
            Location locationItem = mLocationItems.get(getAdapterPosition());
            mLocationItemClickListener.onLocationItemClick(locationItem);
        }
    }
}
