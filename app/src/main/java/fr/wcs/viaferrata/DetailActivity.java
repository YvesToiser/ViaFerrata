package fr.wcs.viaferrata;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DetailActivity extends AppCompatActivity {

    private int idVia;


    private static final String TAG = "DetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Get the id from intent
        idVia = getIntent().getIntExtra("id", 0);


        // Create variable for TextViews
        final TextView tvname = (TextView) findViewById(R.id.tvViaName);
        final TextView tvcity = (TextView) findViewById(R.id.tvViaCity);
        final  TextView tvdifficulty = (TextView) findViewById(R.id.tvViaDifficulty);
        final  TextView tvlatitude = (TextView) findViewById(R.id.tvViaLatitude);
        final  TextView tvlongitude = (TextView) findViewById(R.id.tvViaLongitude);


        // Get values from database
        //initialize database
        final DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Query mQueryRef = mDatabase.child(String.valueOf(idVia));
        // This type of listener is not one time, and you need to cancel it to stop
        // receiving updates.
        mQueryRef.addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                ViaFerrataModel viaFerrata = dataSnapshot.getValue(ViaFerrataModel.class);
                String name = viaFerrata.getNom();
                tvname.setText(name);
                String city = viaFerrata.getVille();
                tvcity.setText(city);
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "Read Data Failed", databaseError.toException());

            }
        });


//
//
//        String city;
//        String difficulty;
//        float latitude;
//        float longitude;
//
//        // Assign text to TextViews
//
//        tvcity.setText(city);
//        tvdifficulty.setText(difficulty);
//        tvlatitude.setText(latitude);
//        tvlongitude.setText(longitude);


    }
}
