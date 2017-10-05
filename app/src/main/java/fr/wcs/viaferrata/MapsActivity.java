package fr.wcs.viaferrata;

import android.Manifest;
import android.content.res.Configuration;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Parcelable;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ListView;
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
    private ToggleButton buttonSwitch;

    private Marker marker;
    int drawableMarqueur = R.drawable.marqueur;

    ExpListViewAdapterWithCheckbox listAdapter;
    ExpandableListView expListView;
    ArrayList<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    Animation slide_in_left, slide_in_right, slide_out_left, slide_out_right;
    ViewFlipper flipper;

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

        flipper = findViewById(R.id.flipper);

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

        //List adapter
        displayList(mViaFerrataList);

    }

    // Fonction qui remplit la liste
    private void displayList (ArrayList<ViaFerrataModel> viaferrataList){
        final ArrayList<ViaFerrataModel> myListOfVia = viaferrataList;
        final ListView itemsListVia = findViewById(R.id.listVia);

        ViaFerrataAdapter adapter = new ViaFerrataAdapter(this, viaferrataList);
        itemsListVia.setAdapter(adapter);

        itemsListVia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ViaFerrataModel viaItem = myListOfVia.get(i);
                Intent intent = new Intent(MapsActivity.this, ViaActivity.class);
                intent.putExtra("via", viaItem);
                startActivity(intent);


            }
        });
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding child data
        listDataHeader.add("Zone géographique");
        listDataHeader.add("Niveau");

        // Adding child data
        List<String> zoneGeo = new ArrayList<>();
        zoneGeo.add("Auverge-Rhône-Alpes");
        zoneGeo.add("Bourgogne Franche Comté");
        zoneGeo.add("Corse");
        zoneGeo.add("Grand-Est");
        zoneGeo.add("Normandie");
        zoneGeo.add("Nouvelle-Aquitaine");
        zoneGeo.add("Occitanie");
        zoneGeo.add("PACA");
        //zoneGeo.add("Hauts-de-France");
        //zoneGeo.add("Bretagne");
        //zoneGeo.add("Île-de-France");
        //zoneGeo.add("Centre-Val-de-Loire");
        //zoneGeo.add("Pays-de-la-Loire");


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

        Log.d(TAG, "Taille de la Via Ferrata au moment de l'affichage des markers" + mViaFerrataList.size());
        for(int i = 0; i<mViaFerrataList.size(); i++){
            ViaFerrataModel via = mViaFerrataList.get(i);
            String nom = via.getNom();
            Log.d(TAG, "test22 Objet via" + via);
            String ville = via.getVille();
            double latitude = via.getLatitude();
            double longitude = via.getLongitude();
            final LatLng latlng = new LatLng(latitude, longitude);
            int difficulte = via.getDifficulte();
            marker = mMap.addMarker(new MarkerOptions()
                                .position(latlng)
                                .title(nom)
                                .snippet(ville)
                                .icon(BitmapDescriptorFactory.fromResource(drawableMarqueur))
            );
            marker.setTag(via);

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
                    intent.putExtra("via", (ViaFerrataModel) marker.getTag() );
                    Log.d(TAG, "Marker Tagg " + marker.getTag());
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

                         //   listAdapter.resetCheckboxes();

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
                                for (int i=0;i<8;i++){
                                    filtreZoneGeo.add(i);
                                }
                            }
                            Log.i(TAG, "filtre zone géo : " + filtreZoneGeo.toString());
                            Log.i(TAG, "filtre difficulté : " + filtreDiff.toString());
                            // Appelle la fonction qui réactualise les marqueurs sur la map
                            mMap.clear();
                            rechargeMarkersOnMap(filtreZoneGeo, filtreDiff);

                            // Appelle la fonction qui réactualise la liste
                            final ListView itemsListVia = findViewById(R.id.listVia);
                            itemsListVia.setAdapter(null);
                            rechargeList(filtreZoneGeo, filtreDiff);


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
                        buttonSwitch.setVisibility(GONE);
                    }
                    else if (t.getVisibility() == GONE) {
                        t.setVisibility(VISIBLE);
                        buttonCancel.setVisibility(GONE);
                        buttonValider.setVisibility(GONE);
                        buttonSwitch.setVisibility(VISIBLE);
                    }
                }

                else if (mLayout != null && (mLayout.getPanelState() == PanelState.ANCHORED)) {
                    mLayout.setPanelState(PanelState.COLLAPSED);
                }
                else if (mLayout != null && (mLayout.getPanelState() == PanelState.COLLAPSED)){
                    mLayout.setEnabled(true);
                    mLayout.setTouchEnabled(true);
                    t.setVisibility(VISIBLE);
                    buttonCancel.setVisibility(GONE);
                    buttonValider.setVisibility(GONE);
                    buttonSwitch.setVisibility(VISIBLE);
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

    // Fonction qui vérifie si la via correspond aux filtres
    public boolean allFiltersMatch (List<Integer> listDiff, int difficulte, List<Integer> listZoneGeo, int zoneGeoNb){
        // Difficulty filter
        boolean difficultyMatches = false;
        for (int j = 0; j<listDiff.size(); j++){
            if(listDiff.get(j)==difficulte){
                difficultyMatches=true;
            }
        }
        if(!difficultyMatches){
            return false;
        }
        // Zone géo filter
        boolean zoneGeoMatches = false;
        for (int j = 0; j<listZoneGeo.size(); j++){
            if(listZoneGeo.get(j)==zoneGeoNb){zoneGeoMatches=true;}
        }
        if(!zoneGeoMatches){
            return false;
        }
        return true;
    }

    // Fonction qui recharge les marqueurs sur la map
    public void rechargeMarkersOnMap(List<Integer> listZoneGeo, List<Integer> listDiff){

        // Check all vias again
        for(int i = 0; i<mViaFerrataList.size(); i++){
            ViaFerrataModel via = mViaFerrataList.get(i);
            String nom = via.getNom();
            Log.d(TAG, "test28 listDiff" + listDiff);
            String ville = via.getVille();
            double latitude = via.getLatitude();
            double longitude = via.getLongitude();
            final LatLng latlng = new LatLng(latitude, longitude);
            int difficulte = via.getDifficulte()-1;
            int zoneGeoNb = via.getRegionNumber();
            // If all filters match we add the marker
            if(allFiltersMatch(listDiff, difficulte, listZoneGeo, zoneGeoNb)) {
                marker = mMap.addMarker(new MarkerOptions()
                        .position(latlng)
                        .title(nom)
                        .snippet(ville)
                        .icon(BitmapDescriptorFactory.fromResource(drawableMarqueur))

                );
                Log.d(TAG, "test28 Via Nb" + i + " marker added");
                marker.setTag(via);

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
                        intent.putExtra("via", (ViaFerrataModel) marker.getTag());
                        startActivity(intent);
                    }
                });
            }
        }
        // Phantom marker . This is a hack to solve problem of last marker not showing
        marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .visible(false)
                .icon(BitmapDescriptorFactory.fromResource(drawableMarqueur))
        );
    }

// Fonction qui recharge la liste en fonction des nouveaux filtres
    public void rechargeList(List<Integer> listZoneGeo, List<Integer> listDiff){
        // Filtre la liste des via dans une nouvelle liste
        final ArrayList<ViaFerrataModel> newList = new ArrayList<>();
        for(int i = 0; i<mViaFerrataList.size(); i++){
            ViaFerrataModel via = mViaFerrataList.get(i);
            int difficulte = via.getDifficulte()-1;
            int zoneGeoNb = via.getRegionNumber();
            if(allFiltersMatch(listDiff, difficulte, listZoneGeo, zoneGeoNb)) {
                newList.add(via);
            }
        }
        // Affiche la nouvelle liste
        displayList(newList);
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

