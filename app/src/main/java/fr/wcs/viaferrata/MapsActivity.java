package fr.wcs.viaferrata;

import android.Manifest;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
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

import com.idunnololz.widgets.AnimatedExpandableListView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static fr.wcs.viaferrata.HomeActivity.mViaFerrataList;
import static fr.wcs.viaferrata.HomeActivity.mySharedPref;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnMyLocationButtonClickListener {

    // Variables globales
    private static final String TAG = "MapActivity";

    //Element du layout Map
    private GoogleMap mMap;
    private Marker marker;
    private ViewFlipper flipper;
    private ToggleButton buttonSwitch;

    // Variables du layout
    private int drawableMarqueur = R.drawable.marqueur;
    private LatLngBounds Limite = new LatLngBounds(
            new LatLng(41.36, -5.16), new LatLng(51.1, 9.8));
    private float zoom;
    private Location mLocation;

    // Elements du panel
    private SlidingUpPanelLayout mLayout;
    private Button buttonCancel;
    private Button buttonValider;
    private Switch switchFavorite;
    private Switch switchDone;
    private LinearLayout seekLinear;
    private TextView seekText;
    private CheckBox seekCheck;
    private SeekBar seekBar;
    private AnimatedExpandableListView expListView;
    private Spinner spinner;
    private LinearLayout linearSpinner;

    // Variables du panel
    private boolean filtreFavoris;
    private boolean filtreDone;
    private int filtreDistance;
    private ExpListViewAdapterWithCheckbox listAdapter;
    private ArrayList<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private Map<Integer, Boolean> listeDiff = new HashMap<>();
    private Map<Integer, Boolean> listeZoneGeo = new HashMap<>();
    private List<Integer> filtreZoneGeo = new ArrayList<>();
    private List<Integer> filtreDiff = new ArrayList<>();
    private ListView itemsListVia;
    private String sortBy;

    // Animations
    private Animation slide_in_left, slide_in_right, slide_out_left, slide_out_right;

    // Variables de permissions
    private LocationManager mLocationManager = null;
    private LocationListener mLocationListener = null;
    private static final int PERMISSION_REQUEST_LOCALISATION = 10;
    private boolean mPermissionDenied = false;
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        TextView textOmbre = findViewById(R.id.overSwitch);
        textOmbre.setElevation(200);

        buttonCancel = findViewById(R.id.buttonCancel);
        buttonValider = findViewById(R.id.buttonValider);
        buttonCancel.setVisibility(GONE);
        buttonValider.setVisibility(GONE);

        // Definition des elements du panel
        switchFavorite = findViewById(R.id.switchFavorite);
        switchDone = findViewById(R.id.switchDone);
        seekLinear = findViewById(R.id.linearSeek);
        seekText = findViewById(R.id.seekBarText);
        seekBar = findViewById(R.id.seekBar);
        seekCheck = findViewById(R.id.seekCheckBox);
        // Checkbox qui active le filtre par distance
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
        // Seekbar progress
        seekBar.setMax(700);
        seekBar.setProgress(0);
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

        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {

                if (pos == 0) {
                    sortBy = "DepartementNum";
                }

                else if (pos == 1) {
                    sortBy = "Nom";
                }

                else if (pos == 2) {
                    sortBy = "DepartementNom";
                }
                else if (pos == 3) {
                    sortBy = "Difficulté";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Switch map/liste
        flipper = findViewById(R.id.flipper);
        slide_in_left = AnimationUtils.loadAnimation(this,
                R.anim.in_left);
        slide_in_right = AnimationUtils.loadAnimation(this,
                R.anim.in_right);
        slide_out_left = AnimationUtils.loadAnimation(this,
                R.anim.out_left);
        slide_out_right = AnimationUtils.loadAnimation(this,
                R.anim.out_right);

        linearSpinner = findViewById(R.id.linearSpinner);
        buttonSwitch = findViewById(R.id.buttonSwitch);

        Intent intent = getIntent();
        int dispChild = intent.getIntExtra("displayedChild", 0);
        boolean checkMapList = intent.getBooleanExtra("checkMapList", false);
        if (checkMapList) {
            linearSpinner.setVisibility(VISIBLE);
        }
        else{
            linearSpinner.setVisibility(GONE);
        }
        flipper.setDisplayedChild(dispChild);
        buttonSwitch.setChecked(checkMapList);

        buttonSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    itemsListVia = findViewById(R.id.listVia);
                    itemsListVia.setAdapter(null);
                    rechargeList(filtreZoneGeo, filtreDiff);
                    linearSpinner.setVisibility(VISIBLE);
                    TextView textOmbre = findViewById(R.id.overSwitch);
                    textOmbre.setElevation(200);
                    flipper.setOutAnimation(slide_out_left);
                    flipper.setInAnimation(slide_in_right);
                    flipper.showNext();
                } else {
                    linearSpinner.setVisibility(GONE);
                    rechargeMarkersOnMap(filtreZoneGeo, filtreDiff);
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

        // Recupere coordonnées geoloc
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                mLocation = location;
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        final TextView filtreText = findViewById(R.id.filter_text);
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonValider =  findViewById(R.id.buttonValider);

        buttonCancel.setText(getResources().getString(R.string.back));
        buttonCancel.setVisibility(GONE);
        buttonValider.setVisibility(GONE);

        mMap = googleMap;
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        // Ajouter le style a la map
        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));
            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can'filtreText find style. Error: ", e);
        }

        mMap.getUiSettings().setMapToolbarEnabled (false);
        mMap.setLatLngBoundsForCameraTarget(Limite);
        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();

        // Fonction qui definit le zoom en fonction de l'orientation et de la largeur d'ecran
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float width = metrics.widthPixels;
        float height = metrics.heightPixels;
        zoom = 0;
        int orientation = this.getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            zoom = width * 0.0003f + 4.78f;
        }
        else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            zoom = height * 0.00018f + 4.7f;
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Limite.getCenter(), zoom));

        // Creation de tout les marqueurs
        rechargeMarkersOnMap(filtreZoneGeo, filtreDiff);

        mLayout = findViewById(R.id.slidingPanel);
        mLayout.addPanelSlideListener(new PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);

            }

            @Override
            public void onPanelStateChanged(final View panel, PanelState previousState, PanelState newState) {

                switchFavorite.setText(getString(R.string.favorite) + " (" + numberOfFavorites() + ")");
                switchDone.setText(getString(R.string.done) + " (" + numberOfDone() + ")");

                // Charger les listes de filtres en fonction des données du HashMap
                listeDiff = listAdapter.getListeDiff();
                listeZoneGeo = listAdapter.getListeZoneGeo();
                filtreDiff.clear();
                filtreZoneGeo.clear();
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

                // Changer le boutton cancel en cas de changement du layout
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
                    int currentOrientation = getResources().getConfiguration().orientation;
                    if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                    }
                    else {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                    }
                    // Bouton cancel
                    buttonCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            // Reset l'adapter
                            listeDiff = new HashMap<>();
                            listeZoneGeo = new HashMap<>();
                            listAdapter.setListeDiff(listeDiff);
                            listAdapter.setListeZoneGeo(listeZoneGeo);
                            listAdapter.notifyDataSetChanged();

                            // Reset les listes et reremplir les filtres diff/zonegeo
                            filtreDiff = new ArrayList<>();
                            filtreZoneGeo = new ArrayList<>();

                            // Reset le panel
                            switchFavorite.setChecked(false);
                            switchDone.setChecked(false);
                            seekCheck.setChecked(false);
                            expListView.collapseGroup(0);
                            expListView.collapseGroup(1);

                            // Reset carte et liste
                            rechargeMarkersOnMap(filtreZoneGeo, filtreDiff);
                            itemsListVia = findViewById(R.id.listVia);
                            itemsListVia.setAdapter(null);
                            rechargeList(filtreZoneGeo, filtreDiff);

                            // Reactiver panel et le reduire
                            mLayout.setEnabled(true);
                            mLayout.setPanelState(PanelState.COLLAPSED);
                            mLayout.setTouchEnabled(true);
                            mLayout.addPanelSlideListener(new PanelSlideListener() {
                                @Override
                                public void onPanelSlide(View panel, float slideOffset) {
                                }

                                @Override
                                public void onPanelStateChanged(View panel, PanelState previousState, PanelState newState) {
                                    if (mLayout != null && (mLayout.getPanelState() == PanelState.COLLAPSED)){
                                        buttonCancel.setText(getResources().getString(R.string.back));
                                    }
                                }
                            });
                        }
                    });

                    // Bouton valider
                    buttonValider.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Remplir les listes avec les données du map
                            listeDiff = listAdapter.getListeDiff();
                            listeZoneGeo = listAdapter.getListeZoneGeo();
                            filtreDiff = new ArrayList<>();
                            filtreZoneGeo = new ArrayList<>();
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

                            // Si rien n'est modifié laisser le bouton "retour" sinon "annuler filtres"
                            if (filtreDiff.isEmpty() && filtreZoneGeo.isEmpty() &&
                                    !seekCheck.isChecked() &&
                                    !switchFavorite.isChecked() && !switchDone.isChecked()) {
                                buttonCancel.setText(getResources().getString(R.string.back));
                            }
                            else {
                                buttonCancel.setText(getResources().getString(R.string.cancelText));
                            }

                            // Appelle les fonctions qui réactualise la carte et la liste
                            rechargeMarkersOnMap(filtreZoneGeo, filtreDiff);

                            itemsListVia = findViewById(R.id.listVia);
                            itemsListVia.setAdapter(null);
                            rechargeList(filtreZoneGeo, filtreDiff);

                            // Ferme les list checkbox
                            expListView.collapseGroup(0);
                            expListView.collapseGroup(1);

                            // Activer le panel
                            mLayout.setEnabled(true);
                            mLayout.setTouchEnabled(true);
                            mLayout.setPanelState(PanelState.COLLAPSED);
                            marker.setVisible(false);
                        }
                    });
                    mLayout.setEnabled(false);
                    mLayout.setTouchEnabled(false);

                }else if (mLayout != null && (mLayout.getPanelState() == PanelState.DRAGGING)){
                    // Affichage des boutons en fonction du statut du text visible
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
                }else if (mLayout != null && (mLayout.getPanelState() == PanelState.ANCHORED)) {
                    mLayout.setPanelState(PanelState.COLLAPSED);
                }else if (mLayout != null && (mLayout.getPanelState() == PanelState.COLLAPSED)){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
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
        // Fonction qui ferme l'autre filtre quand on en ouvre un
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                expListView.collapseGroupWithAnimation((groupPosition + 1) % 2);
            }
        });
        // Fonction qui ouvre et ferme les expList avec animation
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                if (expListView.isGroupExpanded(groupPosition)) {
                    expListView.collapseGroupWithAnimation(groupPosition);
                } else {
                    expListView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }
        });

        // Preparer et instorer le list adapter
        prepareListData();
        listAdapter = new ExpListViewAdapterWithCheckbox(this,
                listDataHeader,
                listDataChild,
                listeDiff,
                listeZoneGeo,
                new OnParameterChangeListener() {
                    @Override
                    public void onChange() {
                        buttonCancel.setText(getResources().getString(R.string.cancelText));
                    }
                });
        expListView.setAdapter(listAdapter);
    }


    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    // Fonction qui active la localisation sur l'API
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, PERMISSION_REQUEST_LOCALISATION,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == PanelState.EXPANDED
                        || mLayout.getPanelState() == PanelState.ANCHORED)) {
            mLayout.setTouchEnabled(true);
            mLayout.setEnabled(true);
            mLayout.setPanelState(PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    // Fonction qui change l'indicateur des listes et le place a droite
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Drawable drawable_groupIndicator =
                getResources().getDrawable(R.drawable.group_indicator);
        int drawable_width = drawable_groupIndicator.getMinimumWidth();
        expListView.setIndicatorBoundsRelative(
            expListView.getWidth()-drawable_width-30,
            expListView.getWidth()-30);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, AbsListView.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    // Fonction qui remplit la liste
    private void displayList (ArrayList<ViaFerrataModel> viaferrataList){
        final ArrayList<ViaFerrataModel> myListOfVia = viaferrataList;
        final ListView itemsListVia = findViewById(R.id.listVia);
        if (sortBy == "Nom") {
            Collections.sort(viaferrataList, new Comparator<ViaFerrataModel>() {
                @Override
                public int compare(ViaFerrataModel t1, ViaFerrataModel t2) {
                    return t1.getNom().compareTo(t2.getNom());
                }
            });
        }

        else if (sortBy == "DepartementNum") {
            Collections.sort(viaferrataList, new Comparator<ViaFerrataModel>() {
                @Override
                public int compare(ViaFerrataModel t1, ViaFerrataModel t2) {
                    return String.valueOf(t1.getDptNb()).compareTo(String.valueOf(t2.getDptNb()));
                }
            });
        }

        else if (sortBy == "DepartementNom") {
            Collections.sort(viaferrataList, new Comparator<ViaFerrataModel>() {
                @Override
                public int compare(ViaFerrataModel t1, ViaFerrataModel t2) {
                    return t1.getDptNom().compareTo(t2.getDptNom());
                }
            });
        }

        else if (sortBy == "Difficulté") {
            Collections.sort(viaferrataList, new Comparator<ViaFerrataModel>() {
                @Override
                public int compare(ViaFerrataModel t1, ViaFerrataModel t2) {
                    return (t1.getDifficulteNb().compareTo(t2.getDifficulteNb()));
                }
            });
        }

        ViaFerrataAdapter adapter = new ViaFerrataAdapter(this, viaferrataList);
        itemsListVia.setAdapter(adapter);
        setListViewHeightBasedOnChildren(itemsListVia);

        itemsListVia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ViaFerrataModel viaItem = myListOfVia.get(i);
                Intent intent = new Intent(MapsActivity.this, ViaActivity.class);
                intent.putExtra("via", viaItem);
                intent.putExtra("displayedChild", 1);
                startActivity(intent);
            }
        });
    }

    // Fonction qui definit les filtres Zone géographique et par difficulté
    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding child data
        listDataHeader.add("Zone géographique");
        listDataHeader.add("Difficulté");

        // Adding child data
        List<String> zoneGeo = new ArrayList<>();
        zoneGeo.add("Auverge-Rhône-Alpes");
        zoneGeo.add("Bourgogne-Franche-Comté");
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

    // Fonction qui vérifie si la via correspond aux filtres
    public boolean allFiltersMatch (List<Integer> listDiff, int difficulte,
                                    List<Integer> listZoneGeo, int zoneGeoNb,
                                    boolean filtreFavoris, boolean isFavorite,
                                    boolean filtreDone, boolean isDone,
                                    int filtreDistance, double distance){

        // Difficulty filter
        boolean difficultyMatches = false;
        for (int j = 0; j<listDiff.size(); j++){
            if(listDiff.get(j)==difficulte){
                difficultyMatches=true;
            }
        }
        if(listDiff.size()==0){difficultyMatches=true;}
        if(!difficultyMatches){
            return false;
        }
        // Zone géo filter
        boolean zoneGeoMatches = false;
        for (int j = 0; j<listZoneGeo.size(); j++){
            if(listZoneGeo.get(j)==zoneGeoNb){zoneGeoMatches=true;}
        }
        if(listZoneGeo.size()==0){zoneGeoMatches=true;}

        if(!zoneGeoMatches){
            return false;
        }
        if(filtreFavoris && !isFavorite){
            return false;
        }
        if(filtreDone && !isDone){
            return false;
        }
        if (filtreDistance != 0 && filtreDistance < distance){
            return false;
        }

        return true;
    }

    // Fonction qui recharge les marqueurs sur la map
    public void rechargeMarkersOnMap(List<Integer> listZoneGeo, List<Integer> listDiff){
        mMap.clear();
        // Check all vias again
        for(int i = 0; i<mViaFerrataList.size(); i++){
            ViaFerrataModel via = mViaFerrataList.get(i);
            String nom = via.getNom();
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
            double locLat;
            if(mLocation != null){
                locLat = mLocation.getLatitude();
            }else{
                locLat = 0;
            }
            double locLong;
            if(mLocation != null){
                locLong = mLocation.getLongitude();
            }else{
                locLong = 0;
            }
            double distance = distFrom(latitude, longitude, locLat, locLong);
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
            if(allFiltersMatch(listDiff, difficulte, listZoneGeo, zoneGeoNb,
                    filtreFavoris, isFavorite, filtreDone, isDone, filtreDistance, distance)) {
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
                        intent.putExtra("via", (ViaFerrataModel) marker.getTag());
                        intent.putExtra("displayedChild", 0);
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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Limite.getCenter(), zoom));
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
            double locLat;
            if(mLocation != null){
                locLat = mLocation.getLatitude();
            }else{
                locLat = 0;
            }
            double locLong;
            if(mLocation != null){
                locLong = mLocation.getLongitude();
            }else{
                locLong = 0;
            }
            double distance = distFrom(via.getLatitude(), via.getLongitude(), locLat, locLong);
            if(allFiltersMatch(listDiff, difficulte, listZoneGeo, zoneGeoNb,
                    filtreFavoris, isFavorite, filtreDone, isDone, filtreDistance, distance)) {
                newList.add(via);
            }
        }
        // Affiche la nouvelle liste
        displayList(newList);
    }

    // Fonction qui retourne le nombre de favoris
    public String numberOfFavorites () {
        int nbOfFav = 0;
        final ArrayList<ViaFerrataModel> newList = new ArrayList<>();
        for (int i = 0; i < mViaFerrataList.size(); i++) {
            ViaFerrataModel via = mViaFerrataList.get(i);
            mySharedPref = getSharedPreferences("SP", MODE_PRIVATE);
            final String favId = "Fav" + via.getNom();
            final boolean isFavorite = mySharedPref.getBoolean(favId, false);
            if (isFavorite) {
                nbOfFav++;
            }
        }
        if (nbOfFav == 0) {
            switchFavorite.setChecked(false);
            switchFavorite.setEnabled(false);
        }
        return String.valueOf(nbOfFav);
    }

    // Fonction qui retourne le nombre de done
    public String numberOfDone () {
        int nbOfDone = 0;
        final ArrayList<ViaFerrataModel> newList = new ArrayList<>();
        for (int i = 0; i < mViaFerrataList.size(); i++) {
            ViaFerrataModel via = mViaFerrataList.get(i);
            mySharedPref = getSharedPreferences("SP", MODE_PRIVATE);
            final String doneId = "Done" + via.getNom();
            final boolean isDone = mySharedPref.getBoolean(doneId, false);
            if (isDone) {
                nbOfDone++;
            }
        }
        if (nbOfDone == 0) {
            switchDone.setChecked(false);
            switchDone.setEnabled(false);
        }
        return String.valueOf(nbOfDone);
    }

    // Fonction qui calcule la distance entre deux marqueurs selon leurs coordonnées
    public static double distFrom ( double lat1, double lng1, double lat2, double lng2){
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = (earthRadius * c) / 1000;
        dist = Math.round(dist * 100);
        dist = dist / 100;
        return dist;
    }

    // Toutes les fonctions de permissions
    @Override
    public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
    @NonNull int[] grantResults){

        if (requestCode != PERMISSION_REQUEST_LOCALISATION) {
            return;
        }
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MapsActivity.this,
                    getResources().getString(R.string.permission_granted),
                    Toast.LENGTH_SHORT).show();
            enableMyLocation();

            checkPermission();
        }
    }

    private void checkPermission () {
        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this,
                    PERMISSIONS, PERMISSION_REQUEST_LOCALISATION);
            return;
        }
        String provider = mLocationManager.getBestProvider(new Criteria(), false);
        mLocation = mLocationManager.getLastKnownLocation(provider);
        if (mLocation != null) {
            double distance = distFrom(mLocation.getLatitude(), mLocation.getLongitude(), 45, 3);
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
    }

    @Override
    protected void onResumeFragments () {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    private void showMissingPermissionError () {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }
}