package fr.wcs.viaferrata;

import android.Manifest;
import android.content.res.Configuration;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.transition.Visibility;
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

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static fr.wcs.viaferrata.HomeActivity.mViaFerrataList;

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

    private Marker marker;
    int drawableMarqueur = R.drawable.marqueur;

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
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonValider = findViewById(R.id.buttonValider);
        buttonCancel.setVisibility(GONE);
        buttonValider.setVisibility(GONE);


    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Zone géographique");
        listDataHeader.add("Niveau");

        // Adding child data
        List<String> zoneGeo = new ArrayList<String>();
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

        Log.d(TAG, "Taille de la Via Ferrata au moment de l'affichage des markers" + mViaFerrataList.size());
        for(int i = 0; i<mViaFerrataList.size(); i++){
            ViaFerrataModel via = mViaFerrataList.get(i);
            String nom = via.getNom();
            Log.d(TAG, "test22 Nom" + nom);
            String ville = via.getVille();
            double latitude = via.getLatitude();
            double longitude = via.getLongitude();
            final LatLng latlng = new LatLng(latitude, longitude);
            int difficulte = via.getDifficulte();

            // TODO Passer les filtres et voir si filterisok



            marker = mMap.addMarker(new MarkerOptions()
                                .position(latlng)
                                .title(nom)
                                .snippet(ville)
                                .icon(BitmapDescriptorFactory.fromResource(drawableMarqueur))
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
                    intent.putExtra("id", marker.getTitle());
                    Log.d(TAG, "ID VIA " + marker.getTitle());
                    startActivity(intent);
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

