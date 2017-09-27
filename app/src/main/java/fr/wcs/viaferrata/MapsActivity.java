package fr.wcs.viaferrata;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    //private Marker marker;
    private static final String TAG = "MapActivity";

    private SlidingUpPanelLayout mLayout;

    ExpListViewAdapterWithCheckbox listAdapter;
    ExpandableListView expListView;
    ArrayList<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final TextView t = (TextView) findViewById(R.id.filter_text);
        final Button button = (Button) findViewById(R.id.buttonCancel);

        button.setVisibility(View.GONE);




        mLayout = findViewById(R.id.slidingPanel);

        /*if (mLayout != null &&
                (mLayout.getPanelState() == PanelState.EXPANDED)){
            mLayout.setEnabled(false);
            mLayout.setTouchEnabled(false);
            t.setVisibility(View.INVISIBLE);
            button.setVisibility(View.VISIBLE);

        }*/

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
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpListViewAdapterWithCheckbox(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

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
        /*mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        }); */


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        // Add a marker in Sydney and move the camera
        LatLng wcs = new LatLng(43.6015191, 1.4420288000000028);
        LatLng maison = new LatLng(43.6161646, 1.4120812999999544);
        mMap.addMarker(new MarkerOptions()
                .position(wcs)
                .title("Welcome to the Wild Code School"));
        mMap.addMarker(new MarkerOptions()
                .position(maison)
                .title("Maison"));
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(MapsActivity.this, DetailActivity.class);
                startActivity(intent);
            }
        });
    }
    /*@Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == PanelState.EXPANDED || mLayout.getPanelState() == PanelState.ANCHORED)) {
            mLayout.setPanelState(PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }*/
}

