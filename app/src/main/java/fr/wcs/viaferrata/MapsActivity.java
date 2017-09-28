package fr.wcs.viaferrata;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
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
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final TextView t = (TextView) findViewById(R.id.filter_text);
        final Button button = (Button) findViewById(R.id.buttonCancel);

        button.setVisibility(View.GONE);

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
                }else{
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

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                expListView.collapseGroup((groupPosition + 1)%2);
            }
        });

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

    private  int idVia = 0;


    @Override
    public void onMapReady(GoogleMap googleMap) {

        for (int i = 0; i<120; i++){
            mMap = googleMap;
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

            final DatabaseReference maDatabase;

            maDatabase = FirebaseDatabase.getInstance().getReference();

            Query mQueryRef = maDatabase.child(String.valueOf(i));
            idVia = i;

            mQueryRef.addValueEventListener(new ValueEventListener(){

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    ViaFerrataModel viaFerrata = dataSnapshot.getValue(ViaFerrataModel.class);
                    //  String name = viaFerrata.getNom();
                    String Ville = viaFerrata.getVille();
                    double latitude = viaFerrata.getLatitude();
                    double longitude = viaFerrata.getLongitude();
                    LatLng latlng = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions()
                            .position(latlng)
                            .title(Ville));
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
    }
}

