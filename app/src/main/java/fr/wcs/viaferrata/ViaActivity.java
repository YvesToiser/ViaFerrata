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

        final BottomNavigationItemView favButton = (BottomNavigationItemView)findViewById(R.id.favButton);
        final BottomNavigationItemView doneButton = (BottomNavigationItemView)findViewById(R.id.doneButton);

            //Shared preferences
        Intent intentFav = getIntent();
        final ViaFerrataModel maviaferrata =  intentFav.getParcelableExtra("via");
        mySharedPref = getSharedPreferences("SP",MODE_PRIVATE);

        final String favId = "Fav" + maviaferrata.getNom();
        final boolean isFavorite = mySharedPref.getBoolean(favId, false);

        final String doneId = "Done" + maviaferrata.getNom();
        final boolean isDone = mySharedPref.getBoolean(doneId, false);
        Log.i(TAG, "fav" +isFavorite);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //TODO changer menu en bouton
        //initialiser bouton favori et fait
        if(isFavorite){
            favButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        }else{
            favButton.setBackgroundColor(getResources().getColor(R.color.transparent));

        }

        if(isDone){
            doneButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        }else{
            doneButton.setBackgroundColor(getResources().getColor(R.color.transparent));

        }


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomButton);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.backButton:
                        Intent intent = new Intent(ViaActivity.this, MapsActivity.class);
                        startActivity(intent);
                        break;


                    case R.id.favButton:
                        final boolean isFavorite = mySharedPref.getBoolean(favId, false);
                        boolean isFavNewValue = !isFavorite;
                        String toastMessage;
                        if(isFavorite){
                            toastMessage = maviaferrata.getNom()+" "+"ne fait plus partie de vos favoris.";
                        }else{
                            toastMessage = maviaferrata.getNom()+" "+"a été ajoutée à vos favoris.";
                        }
                        Toast toastFavorite = Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_LONG);
                        toastFavorite.show();
                        mySharedPref.edit().putBoolean(favId, isFavNewValue).apply();
                        final boolean isFavoriteNow = mySharedPref.getBoolean(favId, false);
                        Log.i(TAG, "fav" +isFavoriteNow);

                        if(isFavoriteNow){
                            favButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        }else{
                            favButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                        }


                        break;
                    case R.id.doneButton:
                        final boolean isDone = mySharedPref.getBoolean(doneId, false);
                        boolean isDoneNewValue= !isDone;
                        mySharedPref.edit().putBoolean(doneId, isDoneNewValue).apply();
                        final boolean isDoneNow = mySharedPref.getBoolean(doneId, false);
                        if(isDone){
                            doneButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                            toastMessage = "Vous n'avez pas fait la via Ferrata"+" : "+maviaferrata.getNom()+".";
                        }else{
                            doneButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                            toastMessage = "Vous avez fait la via Ferrata"+" : "+maviaferrata.getNom()+".";
                        }
                        Toast.makeText(ViaActivity.this, toastMessage, Toast.LENGTH_LONG).show();

                        break;
                    case R.id.shareButton:

                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        String shareSubject = "Bonsoir";
                        String shareBody = "\nTélécharge vite l'appli ViaFerrata et trouve ta prochaine sortie #sport et #nature !\n\n";
                        shareBody = shareBody + "https://play.google.com/store/apps/details?id=Orion.Soft \n\n";
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSubject);
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(sharingIntent, "Share via"));

                        break;
                    case R.id.itineraryButton:
                        Intent intentGM = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?saddr=&daddr=" + maviaferrata.getLatitude()
                                        + ", " + maviaferrata.getLongitude()));
                                Log.i(TAG, String.valueOf(maviaferrata.getLatitude()));
                        startActivity(intentGM);

                        break;
                }
                return false;
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
