package fr.wcs.viaferrata;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ShareActionProvider;
import android.widget.Toast;


import static fr.wcs.viaferrata.HomeActivity.mySharedPref;


public class ViaActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private static final String TAG = "ViaActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_via);

        //Shared preferences
        Intent intentFav = getIntent();
        final ViaFerrataModel maviaferrata =  intentFav.getParcelableExtra("via");
        mySharedPref = getSharedPreferences("SP",MODE_PRIVATE);

        final ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        final ImageButton itineraryButton = (ImageButton) findViewById(R.id.itineraryButton);
        final ImageButton favButton = (ImageButton) findViewById(R.id.favButton);
        final ImageButton doneButton = (ImageButton) findViewById(R.id.doneButton);
        final ImageButton shareButton = (ImageButton) findViewById(R.id.shareButton);

        final String favId = "Fav" + maviaferrata.getNom();
        final boolean isFavorite = mySharedPref.getBoolean(favId, false);
        if (isFavorite) {
            favButton.setImageResource(R.drawable.etoilechecked);
        }else {
            favButton.setImageResource(R.drawable.etoileunchecked);
        }

        final String doneId = "Done" + maviaferrata.getNom();
        final boolean isDone = mySharedPref.getBoolean(doneId, false);
        if (isDone) {
            doneButton.setImageResource(R.drawable.check);
        }else {
            doneButton.setImageResource(R.drawable.uncheck);
        }

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViaActivity.this, MapsActivity.class);
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
                if(isFavorite){
                    favButton.setImageResource(R.drawable.etoilechecked);
                    toastMessage = maviaferrata.getNom()+" "+"ne fait plus partie de vos favoris.";
                }else{
                    favButton.setImageResource(R.drawable.etoileunchecked);
                    toastMessage = maviaferrata.getNom()+" "+"a été ajoutée à vos favoris.";
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
                if(isDone){
                    doneButton.setImageResource(R.drawable.check);
                    toastMessage = "Vous n'avez pas fait la via Ferrata"+" : "+maviaferrata.getNom()+".";
                }else{
                    doneButton.setImageResource(R.drawable.uncheck);
                    toastMessage = "Vous avez fait la via Ferrata"+" : "+maviaferrata.getNom()+".";
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
                String shareSubject = "Bonsoir";
                String shareBody = "\nTélécharge vite l'appli ViaFerrata et trouve ta prochaine sortie #sport et #nature !\n\n";
                shareBody = shareBody + "https://play.google.com/store/apps/details?id=Orion.Soft \n\n";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSubject);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

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
            // Show 3 total pages.
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
        startActivity(intent);
    }
}
