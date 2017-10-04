package fr.wcs.viaferrata;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    final private boolean hasFavorite = true;
    public static ArrayList<ViaFerrataModel> mViaFerrataList = new ArrayList<>();
    private static final String TAG = "HomeActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Retrieve all the viaFerrata from database and store it in global var
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference viaFerrataRef = database.getReference("viaFerrata");

        viaFerrataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {

                    ViaFerrataModel viaFerrata = data.getValue(ViaFerrataModel.class);
                    mViaFerrataList.add(viaFerrata);

                }
                Log.e(TAG, "onDataChange: " + mViaFerrataList );
                Log.d(TAG, "onDataChange: Nb ViaFerrata" + mViaFerrataList.size());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.d(TAG, "onDataChange Exit: Nb ViaFerrata" + mViaFerrataList.size());


        // Everything below will be replaced by splashscreen

        Button buttonRecherche=(Button)findViewById(R.id.buttonRecherche);

        buttonRecherche.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Intent intentSearch=new Intent(HomeActivity.this,MapsActivity.class);
                startActivity(intentSearch);
            }
        });
        // Until here
    }
}