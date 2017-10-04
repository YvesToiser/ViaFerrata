package fr.wcs.viaferrata;

import android.Manifest;
import android.content.res.Configuration;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
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
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnMyLocationButtonClickListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;
    private GoogleMap mMap;
    private LatLngBounds Limite = new LatLngBounds(
            new LatLng(41.36, -5.16), new LatLng(51.1, 9.8));
    //private Marker marker;
    private static final String TAG = "MapActivity";
    private SlidingUpPanelLayout mLayout;
    private Button buttonCancel;
    private Button buttonValider;
    private ToggleButton buttonSwitch;
    private  int idVia = 0;
    private Marker marker;

    ExpListViewAdapterWithCheckbox listAdapter;
    ExpandableListView expListView;
    ArrayList<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    Animation slide_in_left, slide_in_right, slide_out_left, slide_out_right;
    ViewFlipper flipper;


    int mFlipping = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonValider = findViewById(R.id.buttonValider);
        buttonCancel.setVisibility(GONE);
        buttonValider.setVisibility(GONE);

        flipper = (ViewFlipper) findViewById(R.id.flipper);

        slide_in_left = AnimationUtils.loadAnimation(this,
                R.anim.in_left);
        slide_in_right = AnimationUtils.loadAnimation(this,
                R.anim.in_right);
        slide_out_left = AnimationUtils.loadAnimation(this,
                R.anim.out_left);
        slide_out_right = AnimationUtils.loadAnimation(this,
                R.anim.out_right);

        buttonSwitch = findViewById(R.id.buttonSwitch);

        buttonSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if( isChecked) {
                    flipper.setOutAnimation(slide_out_left);
                    flipper.setInAnimation(slide_in_right);

                    flipper.showNext();
                }
                else {
                    flipper.setInAnimation(slide_in_left);
                    flipper.setOutAnimation(slide_out_right);

                    flipper.showPrevious();

                }
            }
        });

        /*
        buttonSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipper = (ViewFlipper) findViewById(R.id.flipper);



                if(mFlipping==0){
                    flipper.startFlipping();
                    mFlipping=1;
                    Log.i(TAG, String.valueOf(mFlipping));
                }
                else{
                    flipper.stopFlipping();
                    mFlipping=0;
                    Log.i(TAG, String.valueOf(mFlipping));
                }

            }
        });
        */

    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding child data
        listDataHeader.add("Zone géographique");
        listDataHeader.add("Niveau");

        // Adding child data
        List<String> zoneGeo = new ArrayList<>();
        zoneGeo.add("Occitanie");
        zoneGeo.add("Nouvelle-Aquitaine");
        //zoneGeo.add("PACA");
        zoneGeo.add("Auverge-Rhône-Alpes");
        //zoneGeo.add("Bourgogne Franche Comté");
        //zoneGeo.add("Grand-Est");
        //zoneGeo.add("Hauts-de-France");
        zoneGeo.add("Normandie");
        //zoneGeo.add("Bretagne");
        //zoneGeo.add("Île-de-France");
        //zoneGeo.add("Centre-Val-de-Loire");
        //zoneGeo.add("Pays-de-la-Loire");
        zoneGeo.add("Corse");

        List<String> niveau = new ArrayList<>();
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
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonValider =  findViewById(R.id.buttonValider);

        buttonCancel.setVisibility(GONE);
        buttonValider.setVisibility(GONE);

        mMap = googleMap;
        //MapStyleOptions style = new MapStyleOptions(
        //  JsonMapPerso
        // )
        //mMap.setMapStyle(style);
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.setLatLngBoundsForCameraTarget(Limite);
        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Log.d(TAG, "largeur : " + metrics.widthPixels + " hauteur : " + metrics.heightPixels);

        float width = metrics.widthPixels;
        float height = metrics.heightPixels;
        float zoom = 0;
        int orientation = this.getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {

            zoom = width * 0.0003f + 4.78f;
            Log.d(TAG, "Portrait" + zoom);
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            zoom = height * 0.00018f + 4.7f;
            Log.d(TAG, "Paysage" + zoom);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Limite.getCenter(), zoom));

        for (int i = 0; i < 7; i++) {

            final DatabaseReference maDatabase;
            maDatabase = FirebaseDatabase.getInstance().getReference();
            Query mQueryRef = maDatabase.child("viaFerrata").child(String.valueOf(i));
            idVia = i;

            mQueryRef.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    ViaFerrataModel viaFerrata = dataSnapshot.getValue(ViaFerrataModel.class);
                    String nom = viaFerrata.getNom();
                    String ville = viaFerrata.getVille();
                    double latitude = viaFerrata.getLatitude();
                    double longitude = viaFerrata.getLongitude();
                    final LatLng latlng = new LatLng(latitude, longitude);
                    marker = mMap.addMarker(new MarkerOptions()
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
                if (mLayout != null && (mLayout.getPanelState() == PanelState.EXPANDED)) {
                    buttonCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mLayout.setEnabled(true);
                            mLayout.setTouchEnabled(true);
                            mLayout.setPanelState(PanelState.COLLAPSED);
                            marker.setVisible(false);
                        }
                    });
                    buttonValider.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            List<Integer> filtreDiff = new ArrayList<>();
                            Map<Integer, Boolean> listeDiff = listAdapter.getListeDiff();

                            List<Integer> filtreZoneGeo = new ArrayList<>();
                            Map<Integer, Boolean> listeZoneGeo = listAdapter.getListeZoneGeo();

                            for (Map.Entry<Integer, Boolean> entry : listeDiff.entrySet()){
                                int position = entry.getKey();
                                boolean value = entry.getValue();
                                if (value) {
                                    filtreDiff.add(position);
                                }
                            }

                            for (Map.Entry<Integer, Boolean> entry : listeZoneGeo.entrySet()){
                                int position = entry.getKey();
                                boolean value = entry.getValue();
                                if (value) {
                                    filtreZoneGeo.add(position);
                                }
                            }
                            if (filtreDiff.isEmpty()) {
                                for (int i=0;i<6;i++){
                                    filtreDiff.add(i);
                                }
                            }
                            if (filtreZoneGeo.isEmpty()) {
                                for (int i=0;i<5;i++){
                                    filtreZoneGeo.add(i);
                                }
                            }
                            Log.i(TAG, "filtre zone géo : " + filtreZoneGeo.toString());
                            Log.i(TAG, "filtre difficulté : " + filtreDiff.toString());

                        }
                    });
                    mLayout.setEnabled(false);
                    mLayout.setTouchEnabled(false);
                    t.setVisibility(GONE);
                    buttonCancel.setVisibility(VISIBLE);
                    buttonValider.setVisibility(VISIBLE);

                }else if (mLayout != null && (mLayout.getPanelState() == PanelState.DRAGGING)){
                    if (t.getVisibility() == VISIBLE ){
                        t.setVisibility(GONE);
                        buttonCancel.setVisibility(VISIBLE);
                        buttonValider.setVisibility(VISIBLE);
                    }
                    else if (t.getVisibility() == GONE) {
                        t.setVisibility(VISIBLE);
                        buttonCancel.setVisibility(GONE);
                        buttonValider.setVisibility(GONE);
                    }
                }

                 else if (mLayout != null && (mLayout.getPanelState() == PanelState.ANCHORED)) {
                    mLayout.setPanelState(PanelState.COLLAPSED);
                }
                else if (mLayout != null && (mLayout.getPanelState() == PanelState.COLLAPSED)){
                    mLayout.setEnabled(true);
                    mLayout.setTouchEnabled(true);
                }
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
                expListView.collapseGroup((groupPosition + 1) % 2);
            }
        });

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
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
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == PanelState.EXPANDED || mLayout.getPanelState() == PanelState.ANCHORED)) {
            mLayout.setPanelState(PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
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

