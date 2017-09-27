package fr.wcs.viaferrata;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    //private Marker marker;
    private static final String TAG = "MapActivity";

    private SlidingUpPanelLayout mLayout;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final TextView t = (TextView) findViewById(R.id.name);
        final Button button = (Button) findViewById(R.id.button);
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.slidingPanel);

        button.setVisibility(View.GONE);

        /*ListView lv = (ListView) findViewById(R.id.list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MapsActivity.this, "onItemClick", Toast.LENGTH_SHORT).show();
            }
        });

        List<String> your_array_list = Arrays.asList(
                "This",
                "Is",
                "An",
                "Example",
                "ListView",
                "That",
                "You",
                "Can",
                "Scroll",
                ".",
                "It",
                "Shows",
                "How",
                "Any",
                "Scrollable",
                "View",
                "Can",
                "Be",
                "Included",
                "As",
                "A",
                "Child",
                "Of",
                "SlidingUpPanelLayout"
        );

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                your_array_list );

        lv.setAdapter(arrayAdapter); */



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
                if(mLayout != null && (mLayout.getPanelState() == PanelState.EXPANDED || mLayout.getPanelState() == PanelState.DRAGGING || mLayout.getPanelState() == PanelState.ANCHORED)){
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
        /*mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        }); */
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
                            Intent intent = new Intent(MapsActivity.this, DetailActivity.class);
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

