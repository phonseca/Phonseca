package com.phonseca.tanquecheio;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.phonseca.tanquecheio.helpers.JsonHelper;
import com.phonseca.tanquecheio.models.PostoCombustivel;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String TAG = "PostosCombustivel";
    private GoogleMap mMap;
    private List<PostoCombustivel> listaPostos;

    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            mMap.setMyLocationEnabled(false);
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));

        setupPostosCombustivel();
        obterLocalizacaoAtual(location);
    }


    private void setupPostosCombustivel() {
        String json = leJsonPostosCombustivel();

        if (!json.isEmpty()) {
            Log.i(TAG, "Carregou patios salvos");
            carregaPostosCombustivel(json);
            marcaPostosCombustivel();
        }
    }

    private String leJsonPostosCombustivel() {
        String json = "[{\"nome\":\"Teste\",\"latitude\":-19.916667,\"longitude\":-43.933333,\"precoGasolina\": 3.89}]";

        return json;
    }

    private void carregaPostosCombustivel(String json) {
        listaPostos = JsonHelper.getList(json, PostoCombustivel[].class);
    }

    private void marcaPostosCombustivel() {
        if (mMap != null && listaPostos != null) {

            for (PostoCombustivel posto : listaPostos) {

                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(posto.getLatitude(), posto.getLongitude()))
                        .title(posto.getNome())
                        .snippet(String.valueOf(posto.getPrecoGasolina()))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(posto.getLatitude(), posto.getLongitude()), 8));
            }

        }
    }

    private void obterLocalizacaoAtual(Location location) {
        if (location != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }else
        {
            location = new Location("");

            location.setLatitude(-21.7672446);
            location.setAltitude(-43.350865);



            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

}