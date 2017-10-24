package fr.wcs.viaferrata;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static fr.wcs.viaferrata.R.id.pager;
import static fr.wcs.viaferrata.R.id.recyclerView;

public class FullScreenViewActivity extends AppCompatActivity {
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private ViewPager mViewPager;
    public static ViaFerrataModel myVia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_view);
        Intent intent = getIntent();
        String viaName = intent.getStringExtra("name");
        myVia = intent.getParcelableExtra("via");
        final int position = intent.getIntExtra("id", 0);

        mViewPager =(ViewPager) findViewById(R.id.pager);

        //RECUPERATION DES PHOTOS
        DatabaseReference galleryPhotoRef = mDatabase.getReference("photos");
        galleryPhotoRef.orderByChild("viaName").equalTo(viaName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> photoList = new ArrayList<>();
                for (DataSnapshot photoSnapshot : dataSnapshot.getChildren()){
                    PhotoModel myPhotoModel = photoSnapshot.getValue(PhotoModel.class);

                    photoList.add(0, myPhotoModel.getPhotoUri());
                }

//                ArrayList<String> listePointee = new ArrayList<>();
//                for(int i = position; i<photoList.size();i++){
//                    listePointee.add(photoList.get(i));
//                }
//                for(int i = 0; i< position; i++){
//                    listePointee.add(photoList.get(i));
//                }

                //creating adapter
                FullScreenAdapter adapter = new FullScreenAdapter(getApplicationContext(), getParent(), photoList);

                //adding adapter to recyclerview
                mViewPager.setAdapter(adapter);
                mViewPager.setCurrentItem(position);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
