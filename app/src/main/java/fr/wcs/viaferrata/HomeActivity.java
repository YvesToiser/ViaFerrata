package fr.wcs.viaferrata;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {

    private NetworkStateReceiver networkStateReceiver;
    public static ArrayList<ViaFerrataModel> mViaFerrataList = new ArrayList<>();
    public static SharedPreferences mySharedPref;
    private static final String TAG = "HomeActivity";
    private boolean network;

    /** Duration of wait for splash screen**/
    private final int SPLASH_DISPLAY_LENGTH = 6000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);

        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));

        //splash screen code
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                if (network) {
                    Intent mainIntent = new Intent(HomeActivity.this,MapsActivity.class);
                    HomeActivity.this.startActivity(mainIntent);
                    HomeActivity.this.finish();
                }
                else {
                    Intent intent = new Intent(HomeActivity.this, MapsActivity.class);
                    intent.putExtra("displayedChild", 1);
                    intent.putExtra("checkMapList", true);
                    startActivity(intent);
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
        //end splash screen code

        // Retrieve all the viaFerrata from database and store it in global var
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference viaFerrataRef = database.getReference("viaFerrata");
        viaFerrataRef.keepSynced(true);

        viaFerrataRef.orderByChild("dptNb").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    ViaFerrataModel viaFerrata = data.getValue(ViaFerrataModel.class);
                    mViaFerrataList.add(viaFerrata);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void networkAvailable() {
        network = true;
    }

    @Override
    public void networkUnavailable() {
        network = false;
        Toast.makeText(this, "Vous n'avez pas de connection réseau, certaines fonctionnalité peuvent être limitées.", Toast.LENGTH_LONG).show();
    }
}