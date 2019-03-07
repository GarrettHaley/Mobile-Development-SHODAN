package com.example.shodan.UserInterface;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.shodan.R;
import com.example.shodan.ShodanItem;
import com.example.shodan.utils.ShodanUtils;

import org.w3c.dom.Text;

public class ShodanDetails extends AppCompatActivity {
    private TextView detailOrg;
    private TextView detailTitle;
    private TextView detailLat;
    private TextView detailLong;
    private TextView detailCity;
    private TextView detailCountryCode;
    private TextView detailIP_Port;
    private TextView detailTransport;
    private TextView detailProduct;
    private TextView detailItemTimestamp;
    private ShodanItem shodanItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shodan_details);
        detailOrg = (TextView)findViewById(R.id.detail_item_org);
        detailTitle = (TextView)findViewById(R.id.detail_item_title);
        detailLat = (TextView) findViewById(R.id.detail_item_lat);
        detailLong = (TextView) findViewById(R.id.detail_item_long);
        detailCity = (TextView) findViewById(R.id.detail_item_city);
        detailCountryCode = (TextView) findViewById(R.id.detail_item_country_code);
        detailIP_Port = (TextView) findViewById(R.id.detail_item_ip_port);
        detailTransport = (TextView) findViewById(R.id.detail_item_transport);
        detailProduct = (TextView) findViewById(R.id.detail_item_product);
        detailItemTimestamp = (TextView) findViewById(R.id.detail_itemTimestamp);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(ShodanUtils.EXTRA_SHODAN_ITEM)) {
            shodanItem = (ShodanItem)intent.getSerializableExtra(ShodanUtils.EXTRA_SHODAN_ITEM);
            fillInLayout(shodanItem);
        }
    }

    private void fillInLayout(ShodanItem shodanItem) {
        detailOrg.setText(shodanItem.organization);
        detailTitle.setText(shodanItem.title);
        detailLat.setText("Latitude: " + shodanItem.latitude.toString());
        detailLong.setText("Longitude: " + shodanItem.longitude.toString());
        detailCity.setText(shodanItem.city);
        detailCountryCode.setText(shodanItem.countryCode);
        detailIP_Port.setText("IP: " + shodanItem.ip + "\nPort: " + shodanItem.port);
        detailTransport.setText("Transport: " + shodanItem.transport);
        detailProduct.setText("Product: " + shodanItem.product);
        detailItemTimestamp.setText(shodanItem.timestamp);
    }
}
