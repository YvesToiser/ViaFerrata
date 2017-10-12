package fr.wcs.viaferrata;
import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity {

    public static ArrayList<ViaFerrataModel> mViaFerrataList = new ArrayList<>();
    public static SharedPreferences mySharedPref;
    private static final String TAG = "HomeActivity";

    /** Duration of wait for splash screen**/
    private final int SPLASH_DISPLAY_LENGTH = 6000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);


        //splash screen code
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the MapsActivity. */
                Intent mainIntent = new Intent(HomeActivity.this,MapsActivity.class);
                HomeActivity.this.startActivity(mainIntent);
                HomeActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
        //end splash screen code

        //TO DO PREVOIR CAS OU  FIREBASE N'EST PAS ACCESSIBLE

        // Retrieve all the viaFerrata from database and store it in global var
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference viaFerrataRef = database.getReference("viaFerrata");

        viaFerrataRef.orderByChild("nom").addValueEventListener(new ValueEventListener() {
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
}