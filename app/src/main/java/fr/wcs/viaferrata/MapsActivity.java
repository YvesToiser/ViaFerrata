package fr.wcs.viaferrata;

import android.Manifest;
import android.content.res.Configuration;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;
    private GoogleMap mMap;
    private LatLngBounds Limite = new LatLngBounds(
            new LatLng(41.36, -5.17), new LatLng(51.1, 9.6));
    //private Marker marker;
    private static final String TAG = "MapActivity";
    private SlidingUpPanelLayout mLayout;
    private Button button;
    private  int idVia = 0;

    ExpListViewAdapterWithCheckbox listAdapter;
    ExpandableListView expListView;
    ArrayList<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        button = findViewById(R.id.buttonCancel);
        button.setVisibility(View.GONE);
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Zone géographique");
        listDataHeader.add("Niveau");

        // Adding child data
        List<String> zoneGeo = new ArrayList<String>();
        zoneGeo.add("");
        zoneGeo.add("");
        zoneGeo.add("");
        zoneGeo.add("");

        List<String> niveau = new ArrayList<String>();
        niveau.add("Facile (F)");
        niveau.add("Peu difficile (PD)");
        niveau.add("Assez difficile (AD)");
        niveau.add("Difficile (D)");
        niveau.add("Très difficile (TD)");
        niveau.add("Extremement difficile (ED)");

        listDataChild.put(listDataHeader.get(0), zoneGeo);
        listDataChild.put(listDataHeader.get(1), niveau);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        final TextView t = findViewById(R.id.filter_text);
        button = findViewById(R.id.buttonCancel);

        mMap = googleMap;
        //MapStyleOptions style = new MapStyleOptions(
        //  JsonMapPerso
        // )
        //mMap.setMapStyle(style);
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.setLatLngBoundsForCameraTarget(Limite);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Log.d(TAG, "largeur : " + metrics.widthPixels + " hauteur : " + metrics.heightPixels);

        float width = metrics.widthPixels;
        float height = metrics.heightPixels;
        float zoom = 0;
        int orientation = this.getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT){

            zoom = width*0.0003f + 4.78f;
            Log.d(TAG, "Portrait" + zoom);
        }
        else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            zoom = height*0.00018f + 4.7f;
            Log.d(TAG, "Paysage" + zoom);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Limite.getCenter(), zoom));

        for (int i = 0; i < 2; i++) {

            final DatabaseReference maDatabase;
            maDatabase = FirebaseDatabase.getInstance().getReference();
            Query mQueryRef = maDatabase.child("viaFerrata").child(String.valueOf(i));
            idVia = i;

            mQueryRef.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    ViaFerrataModel viaFerrata = dataSnapshot.getValue(ViaFerrataModel.class);
                    // String name = viaFerrata.getNom();
                    String nom = viaFerrata.getNom();
                    String ville = viaFerrata.getVille();
                    double latitude = viaFerrata.getLatitude();
                    double longitude = viaFerrata.getLongitude();
                    final LatLng latlng = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions()
                            .position(latlng)
                            .title(nom)
                            .snippet(ville)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marqueur))
                    );
                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 6));
                            return false;
                        }
                    });
                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            Intent intent = new Intent(MapsActivity.this, ViaActivity.class);
                            intent.putExtra("id", idVia);
                            startActivity(intent);
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w(TAG, "Read Data Failed", databaseError.toException());

                }
            });
        }

        mLayout = findViewById(R.id.slidingPanel);
        mLayout.addPanelSlideListener(new PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);

            }

            @Override
            public void onPanelStateChanged(View panel, PanelState previousState, PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
                if (mLayout != null &&
                        (mLayout.getPanelState() == PanelState.EXPANDED)) {
                    mLayout.setEnabled(false);
                    mLayout.setTouchEnabled(false);
                }
                if (mLayout != null &&
                        (mLayout.getPanelState() == PanelState.EXPANDED || mLayout.getPanelState() == PanelState.DRAGGING )) {
                    t.setVisibility(View.GONE);
                    button.setVisibility(View.VISIBLE);
                }
                else if (mLayout.getPanelState() == PanelState.ANCHORED){
                        mLayout.setPanelState(PanelState.COLLAPSED);
                    }
                else{
                    t.setVisibility(View.VISIBLE);
                    button.setVisibility(View.GONE);
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setEnabled(true);
                mLayout.setTouchEnabled(true);
                mLayout.setPanelState(PanelState.COLLAPSED);
            }
        });

        expListView = findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();
        listAdapter = new ExpListViewAdapterWithCheckbox(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                expListView.collapseGroup((groupPosition + 1)%2);
            }
        });
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }
}

