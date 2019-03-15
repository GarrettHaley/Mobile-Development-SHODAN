package com.example.shodan.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.shodan.R;
import com.example.shodan.ShodanItem;
import com.example.shodan.utils.ShodanUtils;

import java.text.DecimalFormat;

public class ShodanDetailsActivity extends AppCompatActivity {
    private TextView detailOrg;
    private TextView detailTitle;
    private TextView detailLat;
    private TextView detailLong;
    private TextView detailCity;
    private TextView detailCountryCode;
    private TextView detail_IP;
    private TextView detail_port;
    private TextView detailTransport;
    private TextView detailProduct;
    private TextView detailItemTimestamp;
    private ShodanItem shodanItem;
    private ShodanItem mshodanItem;
    private Button mapButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shodan_details);
        mshodanItem = new ShodanItem();
        detailOrg = (TextView)findViewById(R.id.detail_item_org);
        detailTitle = (TextView)findViewById(R.id.detail_item_title);
        detailLat = (TextView) findViewById(R.id.detail_item_lat);
        detailLong = (TextView) findViewById(R.id.detail_item_long);
        detailCity = (TextView) findViewById(R.id.detail_item_city);
        detailCountryCode = (TextView) findViewById(R.id.detail_item_country_code);
        detail_IP = (TextView) findViewById(R.id.detail_item_ip);
        detail_port = (TextView) findViewById(R.id.detail_item_port);
        detailTransport = (TextView) findViewById(R.id.detail_item_transport);
        detailProduct = (TextView) findViewById(R.id.detail_item_product);
        detailItemTimestamp = (TextView) findViewById(R.id.detail_itemTimestamp);
        mapButton = (Button) findViewById(R.id.map);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(ShodanUtils.EXTRA_SHODAN_ITEM)) {
            shodanItem = (ShodanItem)intent.getSerializableExtra(ShodanUtils.EXTRA_SHODAN_ITEM);
            mshodanItem = shodanItem;
            fillInLayout(shodanItem);
            mapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showShodanLocation(shodanItem);
                }
            });
        }
    }

    private void fillInLayout(ShodanItem shodanItem) {
        DecimalFormat df = new DecimalFormat("#.00000");
        detailOrg.setText(shodanItem.organization);
        detailTitle.setText(shodanItem.title);
        try {
            detailLat.setText("Latitude: " + df.format(shodanItem.latitude));
            detailLong.setText("Longitude: " + df.format(shodanItem.longitude));
            detailCity.setText(shodanItem.city);
            detailCountryCode.setText(shodanItem.countryCode);
            detail_IP.setText("IP: " + shodanItem.ip);
            detail_port.setText("Port: " + shodanItem.port);
            detailTransport.setText("Transport: " + shodanItem.transport);
            detailProduct.setText("Product: " + shodanItem.product);
            mapButton.setVisibility(View.VISIBLE);
        }catch(Exception e){
            detailTitle.setText("Additional Information Cannot be Retrieved for this Item");
            detailCity.setText(shodanItem.city);
            detailCountryCode.setText(shodanItem.countryCode);
            detail_IP.setText("IP: " + shodanItem.ip);
            detail_port.setText("Port: " + shodanItem.port);
            mapButton.setVisibility(View.INVISIBLE);
        }
    }
    public void showShodanLocation(ShodanItem shodanItem) {
        Uri geoUri = Uri.parse("geo:" + shodanItem.latitude + "," + shodanItem.longitude).buildUpon().build();
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, geoUri);
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shodan_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                shareDevice();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void shareDevice() {
        if (mshodanItem != null) {
            String shareText = getString(R.string.share_device_text, mshodanItem.organization, "device latitude: " + mshodanItem.latitude + "device longitude: " + mshodanItem.longitude);
            ShareCompat.IntentBuilder.from(this)
                    .setType("text/plain")
                    .setText(shareText)
                    .setChooserTitle(R.string.share_chooser_title)
                    .startChooser();
        }
    }
}
