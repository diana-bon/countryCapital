package com.example.naniboni.contrycapital;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lamudi.phonefield.PhoneEditText;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    ImageButton imageInputCalendar;
    EditText inputDate;
    static Dialog popUp;
    int year = Calendar.getInstance().get(Calendar.YEAR);
    Button button;
    PhoneEditText phoneEditText;
    TextView userAddress;

    ImageButton button_maps;
    EditText inputMap;
    static Dialog mapspop;

    Button btnSubmit;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.activity_main );

        imageInputCalendar = (ImageButton) findViewById(R.id.imageInputCalendar);
        inputDate = (EditText) findViewById(R.id.inputBirthDate);
        inputDate.setText("" + year);
        imageInputCalendar.setOnClickListener(this);

        //final PhoneInputLayout phoneInputLayout = (PhoneInputLayout) findViewById(R.id.phone_input_layout);
        phoneEditText = (PhoneEditText) findViewById(R.id.edit_text);
        button = (Button) findViewById(R.id.submit_button);

        userAddress = (TextView)findViewById(R.id.inputPosition);

        // you can set the hint as follows
        phoneEditText.setHint(R.string.phone_hint);

        // you can set the default country as follows
        phoneEditText.setDefaultCountry("FR");

        button_maps = (ImageButton) findViewById(R.id.imageButton);
        inputMap = (EditText) findViewById(R.id.inputPosition);
        button_maps.setOnClickListener(this);

        btnSubmit = (Button) findViewById(R.id.submit_button);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageInputCalendar:
                showYearDialog();
                break;
            case R.id.submit_button:
                boolean valid = true;

                if (phoneEditText.isValid()) {
                    phoneEditText.setError(null);
                } else {
                    phoneEditText.setError(getString(R.string.invalid_phone_number));
                    valid = false;
                }

                if (valid) {
                    Toast.makeText(MainActivity.this, R.string.valid_phone_number, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(MainActivity.this, GameActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, R.string.invalid_phone_number, Toast.LENGTH_LONG).show();
                }
                // Return the phone number as follows
                //String phoneNumber = phoneInputLayout.getPhoneNumber();
                break;
            case R.id.imageButton:
                showMapsDialog();
                break;
        }
    }

    public void showYearDialog() {
        popUp = new Dialog(MainActivity.this);
        popUp.setTitle("Year Picker");
        popUp.setContentView(R.layout.yeardialog);
        Button set = (Button) popUp.findViewById(R.id.button1);
        Button cancel = (Button) popUp.findViewById(R.id.button2);
        TextView year_text=(TextView)popUp.findViewById(R.id.year_text);
        year_text.setText(""+year);
        final NumberPicker nopicker = (NumberPicker) popUp.findViewById(R.id.numberPicker1);

        nopicker.setMaxValue(year+50);
        nopicker.setMinValue(year-50);
        nopicker.setWrapSelectorWheel(false);
        nopicker.setValue(year);
        nopicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        set.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                inputDate.setText(String.valueOf(nopicker.getValue()));
                popUp.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                popUp.dismiss();
            }
        });
        popUp.show();
    }

    public void showMapsDialog(){
        mapspop = new Dialog(MainActivity.this);
        mapspop.setTitle("Google Maps");
        mapspop.setContentView(R.layout.mapdialog);
        MapView mMapView = (MapView) mapspop.findViewById(R.id.mapView);
        MapsInitializer.initialize(MainActivity.this);

        mMapView.onCreate(mapspop.onSaveInstanceState());
        mMapView.onResume();
        mapspop.show();

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {

                LatLng sydney = new LatLng(-33.852, 151.211);
                googleMap.addMarker(new MarkerOptions().position(sydney)
                        .title("Marker in Sydney"));
                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(sydney.latitude, sydney.longitude, 1);
                    Log.e(TAG, addresses.get(0).getAddressLine(0));
                    userAddress.setText(addresses.get(0).getAddressLine(0));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getLocalizedMessage());
                    userAddress.setText("Could not find nearest address");
                }
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


                /*googleMap.addMarker(new MarkerOptions().position(posisiabsen).title("Yout title"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(posisiabsen));
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);*/

            }
        });
    }
}
