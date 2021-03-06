package fr.wcs.viaferrata;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import static fr.wcs.viaferrata.HomeActivity.mySharedPref;


public class ViaActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private static final String TAG = "ViaActivity";
    int displayedChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_via);

        final ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        final ImageButton itineraryButton = (ImageButton) findViewById(R.id.itineraryButton);
        final ImageButton favButton = (ImageButton) findViewById(R.id.favButton);
        final ImageButton doneButton = (ImageButton) findViewById(R.id.doneButton);
        final ImageButton shareButton = (ImageButton) findViewById(R.id.shareButton);

        //Shared preferences
        Intent intentFav = getIntent();
        final ViaFerrataModel maviaferrata =  intentFav.getParcelableExtra("via");
        mySharedPref = getSharedPreferences("SP",MODE_PRIVATE);

        final String favId = "Fav" + maviaferrata.getNom();
        final boolean isFavorite = mySharedPref.getBoolean(favId, false);
        if (isFavorite) {
            favButton.setImageResource(R.drawable.fav_ok_btn);
        }else {
            favButton.setImageResource(R.drawable.fav_off_btn);
        }

        final String doneId = "Done" + maviaferrata.getNom();
        final boolean isDone = mySharedPref.getBoolean(doneId, false);
        if (isDone) {
            doneButton.setImageResource(R.drawable.fait_ok_btn);
        }else {
            doneButton.setImageResource(R.drawable.fait_off_btn);
        }
        // End of shared preferences

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        if(intentFav.getStringExtra("fragment")!=null && intentFav.getStringExtra("fragment").equals("photo")){
            mViewPager.setCurrentItem(1);
        }

        Intent intent = getIntent();
        displayedChild = intent.getIntExtra("displayedChild", 0);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViaActivity.this, MapsActivity.class);
                boolean checkMapList;
                if (displayedChild == 0) {
                    checkMapList = false;
                }
                else {
                    checkMapList = true;
                }
                intent.putExtra("displayedChild", displayedChild);
                intent.putExtra("checkMapList", checkMapList);
                startActivity(intent);
            }
        });

        itineraryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentGM = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=&daddr="
                                + maviaferrata.getLatitude()
                                + ", " + maviaferrata.getLongitude()));
                startActivity(intentGM);
            }
        });

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final boolean isFavorite = mySharedPref.getBoolean(favId, false);
                boolean isFavNewValue = !isFavorite;
                String toastMessage;
                if(isFavNewValue){
                    favButton.setImageResource(R.drawable.fav_ok_btn);
                    toastMessage = maviaferrata.getNom()+" "+"a été ajoutée à vos favoris.";
                }else{
                    favButton.setImageResource(R.drawable.fav_off_btn);
                    toastMessage = maviaferrata.getNom()+" "+"ne fait plus partie de vos favoris.";

                }
                Toast.makeText(ViaActivity.this, toastMessage, Toast.LENGTH_LONG).show();
                mySharedPref.edit().putBoolean(favId, isFavNewValue).apply();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final boolean isDone = mySharedPref.getBoolean(doneId, false);
                boolean isDoneNewValue= !isDone;
                String toastMessage;
                if(isDoneNewValue){
                    doneButton.setImageResource(R.drawable.fait_ok_btn);
                    toastMessage = "Vous avez fait la via Ferrata"+" : "+maviaferrata.getNom()+".";
                }else{
                    doneButton.setImageResource(R.drawable.fait_off_btn);
                    toastMessage = "Vous n'avez pas fait la via Ferrata"+" : "+maviaferrata.getNom()+".";
                }
                mySharedPref.edit().putBoolean(doneId, isDoneNewValue).apply();
                Toast.makeText(ViaActivity.this, toastMessage, Toast.LENGTH_LONG).show();
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = getResources().getString(R.string.shareBody) + "\n\nhttps://play.google.com/apps/testing/fr.wcs.viaferrata";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.shareSubject));
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.shareVia)));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //Returning the current tabs
            switch (position){
                case 0:
                    Tab1General tab1 = new Tab1General();
                    return tab1;
                case 1:
                    Tab3Photo tab2 = new Tab3Photo();
                    return tab2;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "General";
                case 1:
                    return "Photos";
            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ViaActivity.this, MapsActivity.class);
        boolean checkMapList;
        if (displayedChild == 0) {
            checkMapList = false;
        }
        else {
            checkMapList = true;
        }
        intent.putExtra("displayedChild", displayedChild);
        intent.putExtra("checkMapList", checkMapList);
        startActivity(intent);
    }
}