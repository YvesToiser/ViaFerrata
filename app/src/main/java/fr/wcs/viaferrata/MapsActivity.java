package fr.wcs.viaferrata;

import android.Manifest;
import android.content.res.Configuration;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
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
import android.widget.Switch;
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
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
import static fr.wcs.viaferrata.HomeActivity.mySharedPref;

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
    private Switch switchFavorite;
    private Switch switchDone;
    private CheckBox seekCheck;
    private float zoom;

    boolean filtreFavoris;
    boolean filtreDone;
    int filtreDistance;

    private Marker marker;
    int drawableMarqueur = R.drawable.marqueur;

    ExpListViewAdapterWithCheckbox listAdapter;
    ExpandableListView expListView;
    ArrayList<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    Animation slide_in_left, slide_in_right, slide_out_left, slide_out_right;
    ViewFlipper flipper;

    Map<Integer, Boolean> listeDiff = new HashMap<>();
    Map<Integer, Boolean> listeZoneGeo = new HashMap<>();

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

        switchFavorite = findViewById(R.id.switchFavorite);
        switchDone = findViewById(R.id.switchDone);

<<<<<<< Updated upstream
        switchFavorite.setText(getString(R.string.favorite)+" ("+numberOfFavorites()+")");
        switchDone.setText(getString(R.string.done)+" ("+numberOfDone()+")");
=======



        final TextView seekText = findViewById(R.id.seekBarText);
        final SeekBar seekBar = findViewById(R.id.seekBar);
        seekBar.setMax(700);
        seekBar.setProgress(0);

        final LinearLayout seekLinear = findViewById(R.id.linearSeek);

        seekCheck = findViewById(R.id.seekCheckBox);
        seekCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    buttonCancel.setText(getResources().getString(R.string.cancelText));
                    seekLinear.setVisibility(VISIBLE);
                }
                else {
                    seekBar.setProgress(0);
                    seekLinear.setVisibility(GONE);
                }
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                String textProg = String.valueOf(progress) + " km";
                filtreDistance = progress;
                seekText.setText(textProg);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

>>>>>>> Stashed changes

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
        final ListView itemsListVia = findViewById(R.id.listVia);
        itemsListVia.setAdapter(null);
        displayList(mViaFerrataList);

<<<<<<< Updated upstream
=======
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        mLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                mLocation = location;
                Toast.makeText(MapsActivity.this, location.getLatitude() + "    ,   " + location.getLongitude() + "Distance : " ,
                        Toast.LENGTH_LONG).show();
                Log.i(TAG,  "Location changed : " + location.getLatitude() + "    ,   " + location.getLongitude());
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            public void onProviderEnabled(String provider) {
                Log.i(TAG, "onProviderEnabled: ");
            }

            public void onProviderDisabled(String provider) {
                Log.i(TAG, "onProviderDisabled: ");
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkPermission();
    }

    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = (earthRadius * c)/1000;

        dist = Math.round(dist * 100);
        dist = dist/100;

        return dist;
    }

    private void checkPermission() {
        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MapsActivity.this,
                    PERMISSIONS, PERMISSION_REQUEST_LOCALISATION);
            return;
        }
        String provider = mLocationManager.getBestProvider(new Criteria(), false);
        mLocation = mLocationManager.getLastKnownLocation(provider);
        if (mLocation != null) {
            double distance = distFrom(mLocation.getLatitude(), mLocation.getLongitude(), 45, 3);
            Toast.makeText(MapsActivity.this, mLocation.getLatitude() + ",   " + mLocation.getLongitude() + "Distance : " + distance + "km",
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG,  "Location changed : " + mLocation.getLatitude() + "    ,   " + mLocation.getLongitude());
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
>>>>>>> Stashed changes
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        Drawable drawable_groupIndicator =
                getResources().getDrawable(R.drawable.group_indicator);
        int drawable_width = drawable_groupIndicator.getMinimumWidth();

        if(android.os.Build.VERSION.SDK_INT <
                android.os.Build.VERSION_CODES.JELLY_BEAN_MR2){
            expListView.setIndicatorBounds(
                    expListView.getWidth()-drawable_width*2,
                    expListView.getWidth()-drawable_width);
        }else{
            expListView.setIndicatorBoundsRelative(
                    expListView.getWidth()-drawable_width*2,
                    expListView.getWidth()-drawable_width);
        }
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
        final TextView filtreText = findViewById(R.id.filter_text);
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonValider =  findViewById(R.id.buttonValider);

        buttonCancel.setText(getResources().getString(R.string.back));
        buttonCancel.setVisibility(GONE);
        buttonValider.setVisibility(GONE);

        mMap = googleMap;
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can'filtreText find style. Error: ", e);
        }
        mMap.setLatLngBoundsForCameraTarget(Limite);
        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Log.d(TAG, "largeur : " + metrics.widthPixels + " hauteur : " + metrics.heightPixels);

        float width = metrics.widthPixels;
        float height = metrics.heightPixels;
        zoom = 0;
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

            //double distance = distFrom(latitude, longitude, mLocation.getLatitude(), mLocation.getLongitude());

            // get the shared pref
            mySharedPref = getSharedPreferences("SP",MODE_PRIVATE);
            String favId = "Fav" + via.getNom();
            boolean isFavorite = mySharedPref.getBoolean(favId, false);
            String doneId = "Done" + via.getNom();
            boolean isDone = mySharedPref.getBoolean(doneId, false);
            drawableMarqueur = R.drawable.marqueur;
            if(!isFavorite && isDone){
                drawableMarqueur = R.drawable.marqueurfait;

            }
            if(isFavorite && !isDone){
                drawableMarqueur = R.drawable.marqueurfavoris;

            }
            if(isFavorite && isDone){
                drawableMarqueur = R.drawable.marqueurfavorisfait;

            }
            marker = mMap.addMarker(new MarkerOptions()
                    .position(latlng)
                    .title(nom)
                    .snippet(ville)
                    .icon(BitmapDescriptorFactory.fromResource(drawableMarqueur))
            );


            marker.setTag(via);

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

                switchFavorite.setText(getString(R.string.favorite) + " (" + numberOfFavorites() + ")");
                switchDone.setText(getString(R.string.done) + " (" + numberOfDone() + ")");

                listeDiff = listAdapter.getListeDiff();
                listeZoneGeo = listAdapter.getListeZoneGeo();

                List<Integer> filtreDiff = new ArrayList<>();
                List<Integer> filtreZoneGeo = new ArrayList<>();

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



                Log.i(TAG, "onPanelStateChanged " + newState);
                switchFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        filtreFavoris = isChecked;
                        buttonCancel.setText(getResources().getString(R.string.cancelText));

                    }
                });
                switchDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        filtreDone = isChecked;
                        buttonCancel.setText(getResources().getString(R.string.cancelText));
                    }
                });
                if (mLayout != null && (mLayout.getPanelState() == PanelState.EXPANDED)) {
                    buttonCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mLayout.setEnabled(true);
                            mLayout.setTouchEnabled(true);
                            mLayout.setPanelState(PanelState.COLLAPSED);

                            listeDiff = new HashMap<>();
                            listeZoneGeo = new HashMap<>();
                            listAdapter.setListeDiff(listeDiff);
                            listAdapter.setListeZoneGeo(listeZoneGeo);
                            listAdapter.notifyDataSetChanged();

                            List<Integer> filtreDiff = new ArrayList<>();
                            List<Integer> filtreZoneGeo = new ArrayList<>();

                                for (int i=0;i<6;i++){
                                    filtreDiff.add(i);
                                }
                                for (int i=0;i<8;i++){
                                    filtreZoneGeo.add(i);
                                }

                            switchFavorite.setChecked(false);
                            switchDone.setChecked(false);
                            seekCheck.setChecked(false);

                            mMap.clear();
                            rechargeMarkersOnMap(filtreZoneGeo, filtreDiff);

                            final ListView itemsListVia = findViewById(R.id.listVia);
                            itemsListVia.setAdapter(null);
                            rechargeList(filtreZoneGeo, filtreDiff);

                            expListView.collapseGroup(0);
                            expListView.collapseGroup(1);
                            buttonCancel.setText(getResources().getString(R.string.back));

                        }
                    });
                    buttonValider.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            listeDiff = listAdapter.getListeDiff();
                            listeZoneGeo = listAdapter.getListeZoneGeo();

                            List<Integer> filtreDiff = new ArrayList<>();
                            List<Integer> filtreZoneGeo = new ArrayList<>();

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

                            if (filtreDiff.isEmpty() && filtreZoneGeo.isEmpty() &&
                                    !seekCheck.isChecked() &&
                                    !switchFavorite.isChecked() && !switchDone.isChecked()) {
                                buttonCancel.setText(getResources().getString(R.string.back));
                            }
                            else {
                                buttonCancel.setText(getResources().getString(R.string.cancelText));
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


                            // Appelle la fonction qui réactualise les marqueurs sur la map
                            mMap.clear();
                            rechargeMarkersOnMap(filtreZoneGeo, filtreDiff);

                            // Appelle la fonction qui réactualise la liste
                            final ListView itemsListVia = findViewById(R.id.listVia);
                            itemsListVia.setAdapter(null);
                            rechargeList(filtreZoneGeo, filtreDiff);

                            expListView.collapseGroup(0);
                            expListView.collapseGroup(1);


                            mLayout.setEnabled(true);
                            mLayout.setTouchEnabled(true);
                            mLayout.setPanelState(PanelState.COLLAPSED);
                            marker.setVisible(false);

                            Log.i(TAG, "Filtre favoris : " + String.valueOf(filtreFavoris));
                            Log.i(TAG, "Filtre fait : " + String.valueOf(filtreDone));
                            Log.i(TAG, "filtre zone géo : " + filtreZoneGeo.toString());
                            Log.i(TAG, "filtre difficulté : " + filtreDiff.toString());
                        }
                    });
                    mLayout.setEnabled(false);
                    mLayout.setTouchEnabled(false);

                }else if (mLayout != null && (mLayout.getPanelState() == PanelState.DRAGGING)){
                    if (filtreText.getVisibility() == VISIBLE ){
                        filtreText.setVisibility(GONE);
                        buttonSwitch.setVisibility(GONE);

                        buttonCancel.setVisibility(VISIBLE);
                        buttonValider.setVisibility(VISIBLE);

                    }
                    else if (filtreText.getVisibility() == GONE) {
                        filtreText.setVisibility(VISIBLE);
                        buttonSwitch.setVisibility(VISIBLE);

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
                    filtreText.setVisibility(VISIBLE);
                    buttonSwitch.setVisibility(VISIBLE);

                    buttonCancel.setVisibility(GONE);
                    buttonValider.setVisibility(GONE);

                }
            }
        });

        expListView = findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();
        listAdapter = new ExpListViewAdapterWithCheckbox(this, listDataHeader, listDataChild, listeDiff, listeZoneGeo, new OnParameterChangeListener() {
            @Override
            public void onChange() {
                buttonCancel.setText(getResources().getString(R.string.cancelText));
            }
        });

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
<<<<<<< Updated upstream
    public boolean allFiltersMatch (List<Integer> listDiff, int difficulte, List<Integer> listZoneGeo, int zoneGeoNb, boolean filtreFavoris, boolean isFavorite, boolean filtreDone, boolean isDone){
=======
    public boolean allFiltersMatch (List<Integer> listDiff, int difficulte,
                                    List<Integer> listZoneGeo, int zoneGeoNb,
                                    boolean filtreFavoris, boolean isFavorite,
                                    boolean filtreDone, boolean isDone,
                                    int filtreDistance, double distance){
>>>>>>> Stashed changes
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
        if(filtreFavoris && !isFavorite){
            return false;
        }
        if(filtreDone && !isDone){
            return false;
        }
<<<<<<< Updated upstream
=======
        if (filtreDistance != 0 && filtreDistance < distance){
            return false;
        }

>>>>>>> Stashed changes
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
            // get the shared pref
            mySharedPref = getSharedPreferences("SP",MODE_PRIVATE);
            String favId = "Fav" + via.getNom();
            boolean isFavorite = mySharedPref.getBoolean(favId, false);
            String doneId = "Done" + via.getNom();
            boolean isDone = mySharedPref.getBoolean(doneId, false);
            double distance = distFrom(latitude, longitude, mLocation.getLatitude(), mLocation.getLongitude());
            drawableMarqueur = R.drawable.marqueur;
            if(!isFavorite && isDone){
                drawableMarqueur = R.drawable.marqueurfait;

            }
            if(isFavorite && !isDone){
                drawableMarqueur = R.drawable.marqueurfavoris;

            }
            if(isFavorite && isDone){
                drawableMarqueur = R.drawable.marqueurfavorisfait;

            }
            // If all filters match we add the marker
<<<<<<< Updated upstream
            if(allFiltersMatch(listDiff, difficulte, listZoneGeo, zoneGeoNb, filtreFavoris, isFavorite, filtreDone, isDone)) {
=======
            if(allFiltersMatch(listDiff, difficulte, listZoneGeo, zoneGeoNb,
                    filtreFavoris, isFavorite, filtreDone, isDone, filtreDistance, distance)) {
>>>>>>> Stashed changes
                marker = mMap.addMarker(new MarkerOptions()
                        .position(latlng)
                        .title(nom)
                        .snippet(ville)
                        .icon(BitmapDescriptorFactory.fromResource(drawableMarqueur))

                );
                Log.d(TAG, "test28 Via Nb" + i + " marker added");
                marker.setTag(via);

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
            mySharedPref = getSharedPreferences("SP",MODE_PRIVATE);
            final String favId = "Fav" + via.getNom();
            final boolean isFavorite = mySharedPref.getBoolean(favId, false);
            final String doneId = "Done" + via.getNom();
            final boolean isDone = mySharedPref.getBoolean(doneId, false);
<<<<<<< Updated upstream
            if(allFiltersMatch(listDiff, difficulte, listZoneGeo, zoneGeoNb, filtreFavoris, isFavorite, filtreDone, isDone)) {
=======
            double distance = distFrom(via.getLatitude(), via.getLongitude(), mLocation.getLatitude(), mLocation.getLongitude());
            if(allFiltersMatch(listDiff, difficulte, listZoneGeo, zoneGeoNb,
                    filtreFavoris, isFavorite, filtreDone, isDone, filtreDistance, distance)) {
>>>>>>> Stashed changes
                newList.add(via);
            }
        }
        // Affiche la nouvelle liste
        displayList(newList);
    }

    // Fonction qui retourne le nombre de favoris
    public String numberOfFavorites (){
        int nbOfFav = 0;
        final ArrayList<ViaFerrataModel> newList = new ArrayList<>();
        for(int i = 0; i<mViaFerrataList.size(); i++){
            ViaFerrataModel via = mViaFerrataList.get(i);
            mySharedPref = getSharedPreferences("SP",MODE_PRIVATE);
            final String favId = "Fav" + via.getNom();
            final boolean isFavorite = mySharedPref.getBoolean(favId, false);
            if(isFavorite){
                nbOfFav++;
            }
        }
        return String.valueOf(nbOfFav);
    }

    // Fonction qui retourne le nombre de done
    public String numberOfDone (){
        int nbOfDone = 0;
        final ArrayList<ViaFerrataModel> newList = new ArrayList<>();
        for(int i = 0; i<mViaFerrataList.size(); i++){
            ViaFerrataModel via = mViaFerrataList.get(i);
            mySharedPref = getSharedPreferences("SP",MODE_PRIVATE);
            final String doneId = "Done" + via.getNom();
            final boolean isDone = mySharedPref.getBoolean(doneId, false);
            if(isDone){
                nbOfDone++;
            }
        }
        return String.valueOf(nbOfDone);
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
            mLayout.setTouchEnabled(true);
            mLayout.setEnabled(true);
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

